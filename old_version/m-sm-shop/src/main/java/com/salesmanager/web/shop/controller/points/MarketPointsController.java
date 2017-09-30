package com.salesmanager.web.shop.controller.points;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.Gifts;
import com.salesmanager.core.business.customer.model.GiftsCriteria;
import com.salesmanager.core.business.customer.model.GiftsList;
import com.salesmanager.core.business.customer.service.GiftsOrderService;
import com.salesmanager.core.business.customer.service.GiftsService;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.CustomerAdress;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerAddressFacade;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.LabelUtils;

/**
 * 
 * @class com.salesmanager.web.shop.controller.points.MarketPointsController.java
 * @author sam
 * @date 2015年12月30日
 */
@Controller
@RequestMapping("/shop/marketpoints")
public class MarketPointsController extends AbstractController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MarketPointsController.class);
	
	@Autowired
	private LabelUtils messages;
	@Autowired
	private GiftsService giftsService;  //商品兑换
	@Autowired
	private MemberPointsService memberPointsService;	
	@Autowired 
	private GiftsOrderService  giftOrderImpl;
	@Autowired
	private CustomerFacade customerFacade;
    @Autowired
    private CustomerAddressFacade customerAddressFacade;
    @Autowired
	private EmailService emailService;
    
	@RequestMapping(value="/list.html", method={RequestMethod.POST, RequestMethod.GET})
	public String list(@Valid @ModelAttribute("criteria") GiftsCriteria criteria,@RequestParam(value = "page", defaultValue = "1") final int page,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		 /** template **/
		Customer customer = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());
        }
       if(customer !=null){
    	   model.addAttribute( "customer",  customer);
           List<CustomerAdress> addresss=  customerAddressFacade.getCustomerAddress(customer,language);
           model.addAttribute( "addresss",  addresss);
           model.addAttribute( "queryByMemberPoints",memberPointsService.queryByMemberPoints(customer.getId()));
           if(customer.getAddressDefault()!=null){
        	   model.addAttribute("addressDefault",customerAddressFacade.getDefaultAdress(customer, language));
           }
       }
      
       GiftsList giftsList = giftsService.getByCriteria(criteria);
       model.addAttribute("giftsList",giftsList);
       PaginationData paginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
       criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
       
       StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Market.marketlist).append(".").append(store.getStoreTemplate());
		return template.toString();
	}
	
	
	@RequestMapping(value="/cashPoints.html", method={RequestMethod.POST, RequestMethod.GET})
	public  @ResponseBody int cashPoints(Model model,HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		AjaxResponse resp = new AjaxResponse();
		 /** template **/
		Customer customer = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());
        }
       if(customer ==null){
    	  
    	   resp.setStatus(-1);
          
       }
      
       String aid= request.getParameter("addressId");
       String gid = request.getParameter("giftId");
       String num = request.getParameter("qulityGif");
       
       if(!StringUtils.isBlank(aid)  && !StringUtils.isBlank(gid) && !StringUtils.isBlank(num)){
    	   
    	  long qunlity =Long.parseLong(num);//商品数量
    	  Gifts gif = giftsService.getById(Long.parseLong(gid));
		  long point = gif.getPoints();//商品积分
		  
		  if(qunlity*point>memberPointsService.queryByMemberPoints(customer.getId())){
			  resp.setStatus(-2);
		  }else{
			  int stauts = giftOrderImpl.saveGift(customer, Long.parseLong(aid), gif,(int)qunlity);
			  if(stauts==0){
				  resp.setStatus(0);  
				  sendEmail(store, request.getContextPath(), locale);
			  }else{
				  resp.setStatus(-1);
			  }
		  }
    	   
       }else{
    	   resp.setStatus(-1);
       }
      
      
		return resp.getStatus();
	}
	
	private void sendEmail(MerchantStore store,String contextPath,Locale customerLocale){
		 Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, store, messages, customerLocale);		 	 
		 templateTokens.put(EmailConstants.EMAIL_NEW_USER_TEXT, "有新兑换产生，请注意");
			
		 Email email = new Email();
         email.setFrom(store.getStorename());
         email.setFromEmail(store.getStoreEmailAddress());
         email.setSubject("积分兑换");
         email.setTo(GlobalConstants.EMAIL_SERVICE);
         email.setTemplateName("email_template_new_gift.ftl");
         email.setTemplateTokens(templateTokens);

         //LOGGER.debug( "Sending email to {} for order id {} ",subOrder.getMerchant().getStoreEmailAddress(), order.getId() );
         try {
			emailService.sendHtmlEmail(email);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
