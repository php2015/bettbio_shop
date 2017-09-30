package com.salesmanager.web.shop.controller.customer;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.SecuredCustomer;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
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
    private ShoppingCartService shoppingCartService;
	
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
	 */
	@RequestMapping(value="/logon.html", method=RequestMethod.POST)
	public @ResponseBody String logon(@ModelAttribute SecuredCustomer securedCustomer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        AjaxResponse jsonObject=new AjaxResponse();
        

        try {

        	LOG.debug("Authenticating user " + securedCustomer.getUserName());
        	
        	//user goes to shop filter first so store and language are set
        	MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
        	Language language = (Language)request.getAttribute("LANGUAGE");

            //check if username is from the appropriate store
        	
            Customer customerModel = customerFacade.getCustomerForLogin(securedCustomer.getUserName());
            if(customerModel==null) {
            	jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
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

            
            
            
            
        } catch (AuthenticationException ex) {
        	jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
        } catch(Exception e) {
        	jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
        }
		        
        return jsonObject.toJSONString();	
		
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
