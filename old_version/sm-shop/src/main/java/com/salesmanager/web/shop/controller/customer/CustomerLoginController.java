package com.salesmanager.web.shop.controller.customer;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.common.model.UserLogin;
import com.salesmanager.core.business.common.service.UserLoginService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.SecuredCustomer;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.GeoLocationUtils;
import com.salesmanager.web.utils.LabelUtils;

/**
 * Custom Spring Security authentication
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerLoginController extends AbstractController {
	
	@Autowired
    private AuthenticationManager customerAuthenticationManager;
	

    @Autowired
    private  CustomerFacade customerFacade;
    @Autowired
	private CustomerService customerService;
    @Autowired
	private UserLoginService userLoginService;
    @Autowired
	private MerchantStoreService storeService;
    @Autowired
	private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerLoginController.class);
	
	@Autowired
	private LabelUtils messages;
	
	/**
	 * Customer login entry point
	 * @param securedCustomer
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * 根据新的‘合并登录’需求，现重新定义登录错误码的定义：
	 * 0 = RESPONSE_STATUS_SUCCESS， 表示登录成功
	 * -1 = RESPONSE_STATUS_FAIURE，表示登录失败，并且使用的登录名称，可以找到一个买家，
	 * 		根据手机号，可以判断他已经有一个卖家账号了。
	 * 		此时给客户的提示信息就是简单的登录失败，不提示他注册一个卖家账号
	 * -2 = RESPONSE_STATUS_VALIDATION_FAILED， 表示用户状态不对
	 * 		此时提示他状态不对
	 * -101（新定义）表示登录失败，而且登录用的名称，可以找到一个买家，但是根据手机号，
	 * 		找不到对应的卖家，此时提示他和客服联系，注册一个卖家账号
	 * -102（新定义）表示登录失败，而且他提供的名称，找不到任何一个买家。也找不到任何一个卖家
	 * 		此时简单提示他重新登录，因为系统完全没线索可以帮他做什么。
	 * -103（新定义）表示登录失败，而且他提供的名称，找不到任何一个买家。但是这个名称可能对应一个
	 * 		已经注册的卖家，此时提示他去注册一个买家账号。
	 */
	private static final int ERROR_STATE_NoBuyer_NoSeller = -102;
	private static final int ERROR_STATE_NoBuyer_MaybeSeller = -103;
	
	@RequestMapping(value="/logon.html", method=RequestMethod.POST)
	public @ResponseBody String logon(@ModelAttribute SecuredCustomer securedCustomer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        AjaxResponse jsonObject=new AjaxResponse();
        

        try {
        	request.getSession().removeAttribute("sellerLogonError");
        	String logonName =securedCustomer.getUserName();
        	String logonType = securedCustomer.getLogonType();
        	LOG.debug("Authenticating user " + logonName+" logon by " + logonType);
        	
        	//user goes to shop filter first so store and language are set
        	MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
        	Language language = (Language)request.getAttribute("LANGUAGE");

            //check if username is from the appropriate store
        	
        	Map<String, User> possibleUsers = userService.getPossibleUsersByLogonName(logonName);
        	
            Customer customerModel = customerFacade.getCustomerForLogin(logonName);
            if(customerModel==null) {
            	if (possibleUsers.isEmpty()){
            		jsonObject.setStatus(ERROR_STATE_NoBuyer_NoSeller);
            	}else{
            		jsonObject.setStatus(ERROR_STATE_NoBuyer_MaybeSeller);
            	}
            	return jsonObject.toJSONString();
            }
            if(customerModel.getAnonymous()==2) {
            	jsonObject.setStatus(-2);
            	return jsonObject.toJSONString();
            }
            
            customerFacade.authenticate(customerModel, customerModel.getId().toString(), securedCustomer.getPassword());
            //set customer in the http session
            super.setSessionAttribute(Constants.CUSTOMER, customerModel, request);
            jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);


            
            
            LOG.info( "Fetching and merging Shopping Cart data" );
            final String sessionShoppingCartCode= (String)request.getSession().getAttribute( Constants.SHOPPING_CART );
            if(!StringUtils.isBlank(sessionShoppingCartCode)) {
	            ShoppingCartData shoppingCartData= customerFacade.mergeCart( customerModel, sessionShoppingCartCode, store, language );
	
	
	            if(shoppingCartData !=null){
	                jsonObject.addEntry(Constants.SHOPPING_CART, shoppingCartData.getCode());
	                request.getSession().setAttribute(Constants.SHOPPING_CART, shoppingCartData.getCode());
	            }
            } else {

	            ShoppingCart cartModel = shoppingCartService.getByCustomer(customerModel);
	            if(cartModel!=null) {
	                jsonObject.addEntry( Constants.SHOPPING_CART, cartModel.getShoppingCartCode());
	                request.getSession().setAttribute(Constants.SHOPPING_CART, cartModel.getShoppingCartCode());
	            }
            
            }

            
            // clariones for new auto-login both buyer&seller requirement
            // after logon success, try to find the seller-account by the mobile number
            // and add its permissions into the session, and its admin-store into session, too
            request.getSession().setAttribute(Constants.LOGON_AS, Constants.LOGON_AS_BUYER);
            logonSeller(customerModel, request, response);
            
            
        } catch (AuthenticationException ex) {
        	ex.printStackTrace();
        	jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
        } catch(Exception e) {
        	e.printStackTrace();
        	jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
        }
		        
        return jsonObject.toJSONString();	
		
	}
	
	private void logonSeller(Customer customerModel, HttpServletRequest request, HttpServletResponse response) {
		String mobileNumber = customerModel.getPhone();
		if (mobileNumber == null){
			return;
		}
		try {
			User user = userService.getByUserMobile(mobileNumber);
			if(user == null){
				return;
			}
			MerchantStore store = user.getMerchantStore();
			request.getSession().setAttribute(Constants.ADMIN_USER, user);
			request.getSession().setAttribute(Constants.ADMIN_STORE, storeService.getByCode(user.getMerchantStore().getCode()));
			
			Date lastAccess =   new Date();
			  userService.saveOrUpdate(user);
			  UserLogin userLogin  = new UserLogin();
			  userLogin.setUser(user);
			  userLogin.setDateCreated(lastAccess);
			  user.setLastAccess(lastAccess);
			  String ip = GeoLocationUtils.getClientIpAddress(request);
			  if(ip!=null || !ip.equalsIgnoreCase("")){
				  userLogin.setIp(ip);
			  }
			  userLoginService.saveOrUpdate(userLogin);
		} catch (ServiceException e) {
			e.printStackTrace();
			LOG.warn("Cannot login seller with mobile "+mobileNumber);
		}
	}

	/**
	 * Dedicated customer logon page
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="verify.html", method=RequestMethod.GET)
	public String verify(Model model, HttpServletRequest request, HttpServletResponse response,final Locale locale) throws Exception {

		String account = request.getParameter("account");
		String verityCode = request.getParameter("code");
		
		
			
			if(!StringUtils.isBlank(account) && !StringUtils.isBlank(verityCode)){
				try {
		            Customer customerModel = customerFacade.getCustomerForLogin(account);
		            if(customerModel !=null && customerModel.getAnonymous()!=1){
		           		
		           	return "redirect:/shop/customer/verifyconform.html?status=1";
		 				//return template.toString();
		           }
		            if(customerModel !=null && customerModel.getRecievePhoneTime().equalsIgnoreCase(verityCode)){
		            		
		  				customerModel.setAnonymous(3);
		  				
		  				customerService.saveOrUpdate(customerModel);
		  				//set customer in the http session
		  	            super.setSessionAttribute(Constants.CUSTOMER, customerModel, request);
		  				//return template.toString();
		  	          return "redirect:/shop/customer/verifyconform.html?status=0";
		            }
		                       
		        }catch(Exception e) {
		        	 return "redirect:/shop/customer/verifyconform.html?status=-1";
		        }
			}
	        
			
	      
		
		return "redirect:/shop/customer/verifyconform.html?status=-1";
		//return template.toString();
		
	}
	
	@RequestMapping(value="verifyconform.html", method=RequestMethod.GET)
	public String conform(Model model, HttpServletRequest request, HttpServletResponse response,final Locale locale) throws Exception {

		String account = request.getParameter("status");
		String message=  messages.getMessage("label.entity.verify.failed", locale);
		if(!StringUtils.isBlank(account)){
			if(account.equalsIgnoreCase("0")){
				message=  messages.getMessage("message.custmoer.activeed", locale);
			}else if(account.equalsIgnoreCase("1")){
				message=   messages.getMessage("message.custmoer.activeedrepeate", locale);
			}
		}
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.information).append(".").append(store.getStoreTemplate());
		
		//return "redirect:/admin/categories/list.html";
		model.addAttribute( "information", message );	
		return template.toString();
		
	}

}
