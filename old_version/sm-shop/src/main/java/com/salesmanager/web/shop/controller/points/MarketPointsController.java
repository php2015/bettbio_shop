package com.salesmanager.web.shop.controller.points;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.salesmanager.core.business.customer.model.GftsOrderCriteria;
import com.salesmanager.core.business.customer.model.GiftOrder;
import com.salesmanager.core.business.customer.model.GiftOrdersList;
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
 * @date 2015骞�2鏈�0鏃�
 */
@Controller
@RequestMapping("/shop/marketpoints")
public class MarketPointsController extends AbstractController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MarketPointsController.class);
	
	@Autowired
	private CustomerFacade customerFacade;
    @Autowired
    private CustomerAddressFacade customerAddressFacade;
	@Autowired
	private LabelUtils messages;
	@Autowired
	private GiftsService giftsService;  //鍟嗗搧鍏戞崲
	@Autowired
	private MemberPointsService memberPointsService;	
	@Autowired 
	private GiftsOrderService  giftOrderImpl;
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
		// Language language = getSessionAttribute(Constants.LANGUAGE, request);
		// *//** template **//*
		// Customer customer = null;
		// Authentication auth =
		// SecurityContextHolder.getContext().getAuthentication();
		// if(auth != null &&
		// request.isUserInRole("AUTH_CUSTOMER")) {
		// customer = customerFacade.getByID(auth.getName());
		// }
		// if(customer !=null){
		// model.addAttribute( "customer", customer);
		// List<CustomerAdress> addresss=
		// customerAddressFacade.getCustomerAddress(customer,language);
		//
		// model.addAttribute( "addresss", addresss);
		// model.addAttribute(
		// "queryByMemberPoints",memberPointsService.queryByMemberPoints(customer.getId()));
		// if(customer.getAddressDefault()!=null){
		// model.addAttribute("addressDefault",customerAddressFacade.getDefaultAdress(customer,
		// language));
		// }
		// }
		// criteria.setType(1L);
		// GiftsList giftsList = giftsService.getByCriteria(criteria);
		// model.addAttribute("timeLimitGifts",giftsList);
		// PaginationData
		// paginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
		// criteria.setStartIndex((paginaionData.getOffset() -1));
		// criteria.setMaxCount(paginaionData.getPageSize());*/
		// criteria=new GiftsCriteria();
		// criteria.setType(0L);
		// GiftsList hotGiftsList = giftsService.getByCriteria(criteria);
		// model.addAttribute("hotGifts",hotGiftsList);
		// PaginationData
		// hotPaginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
		// criteria.setStartIndex((paginaionData.getOffset() -1));
		// criteria.setMaxCount(paginaionData.getPageSize());
		//
			//当前登录用户
			customer = super.getSessionAttribute(Constants.CUSTOMER, request);
			if(customer!=null){
				model.addAttribute("loginName", customer.getNick());
				model.addAttribute("points", memberPointsService.queryByMemberPoints(customer.getId()));
				model.addAttribute("grade", customer.getGrade());
			}	
		  template = new StringBuilder().append(ControllerConstants.Tiles.Market.marketlist).append(".")
				.append(store.getStoreTemplate());
		return template.toString();
	}
	
	/**
	 * 积分商城
	 * 
	 * @return
	 */
	@RequestMapping("/jfShopping.html")
	public @ResponseBody Map<String, Object> jfShopping(HttpServletRequest request) {
		
		return getJfShoppingValue(request);
	}
	
   /**
    * 获取热门信息列表	
    * @param request
    * @return
    */
   public Map<String, Object> getJfShoppingValue(HttpServletRequest request){
		Map<String, Object> values = new HashMap<String, Object>();
		GiftsCriteria criteria = new GiftsCriteria();
		// 限時
		criteria.setType(1L);
		GiftsList giftsList = giftsService.getByCriteria(criteria);
		values.put("timeLimitGifts", giftsList);
		// 熱銷
		criteria = new GiftsCriteria();
		criteria.setType(0L);
		GiftsList hotGiftsList = giftsService.getByCriteria(criteria);
		values.put("hotGifts", hotGiftsList);
		return values;
   }
	
	/**
	 * 兑换记录
	 * @return
	 */
	@RequestMapping(value="/jfRecord")
	public @ResponseBody  List<GiftOrder> jfRecord(HttpServletRequest request){
		Customer customer = super.getSessionAttribute(Constants.CUSTOMER, request);
		if(customer!=null){
		    GftsOrderCriteria gftsOrderCriteria=new GftsOrderCriteria();
		    gftsOrderCriteria.setCustomerId(customer.getId());//根据登录用户编号查询兑换记录
		    GiftOrdersList giftOrdersList=giftOrderImpl.getByCriteria(gftsOrderCriteria);
		    List<GiftOrder> giftOrders=new ArrayList<GiftOrder>();
		    for (GiftOrder giftOrder : giftOrdersList.getGiftsOrder()) {
		    	GiftOrder newGiftOrder=new GiftOrder();
		    	newGiftOrder.setCreateDate(giftOrder.getCreateDate());
		    	newGiftOrder.setGifName(giftOrder.getGifName());
		    	newGiftOrder.setGifPoint(giftOrder.getGifPoint());
		    	newGiftOrder.setStatus(giftOrder.getStatus());
		    	newGiftOrder.setDeliveryCode(giftOrder.getDeliveryCode());
		    	newGiftOrder.setDeliveryNumber(giftOrder.getDeliveryNumber());
		    	giftOrders.add(newGiftOrder);
			}
		    return giftOrders;
		}
		return null;
	}

	// @Transactional(rollbackFor = { Exception.class })
	@RequestMapping(value = "/cashPoints.html", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody int cashPoints(Model model, HttpServletRequest request, HttpServletResponse response,
			Locale locale) throws Exception {
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
    	   
    	  long qunlity =Long.parseLong(num);//鍟嗗搧鏁伴噺
    	  Gifts gif = giftsService.getById(Long.parseLong(gid));
		  long point = gif.getPoints();//鍟嗗搧绉垎
		  
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
		 templateTokens.put(EmailConstants.EMAIL_NEW_USER_TEXT, "鏈夋柊鍏戞崲浜х敓锛岃娉ㄦ剰");
			
		 Email email = new Email();
         email.setFrom(store.getStorename());
         email.setFromEmail(store.getStoreEmailAddress());
         email.setSubject("绉垎鍏戞崲");
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



