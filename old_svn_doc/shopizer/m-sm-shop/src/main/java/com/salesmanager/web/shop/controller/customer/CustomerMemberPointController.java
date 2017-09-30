package com.salesmanager.web.shop.controller.customer;



import java.util.ArrayList;
import java.util.List;

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

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.GftsOrderCriteria;
import com.salesmanager.core.business.customer.model.GiftOrder;
import com.salesmanager.core.business.customer.model.GiftOrdersList;
import com.salesmanager.core.business.customer.model.GiftStatus;
import com.salesmanager.core.business.customer.model.MemberCriteria;
import com.salesmanager.core.business.customer.model.MemberPointList;
import com.salesmanager.core.business.customer.service.GiftsOrderService;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderstatus.MemberPorintStatus;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.ReadableGiftOrder;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerAddressFacade;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.PageBuilderUtils;

/**
 * Registration of a new customer
 * @author Carl Samson
 *
 */

// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
@Controller
@RequestMapping("/shop/customer")
public class CustomerMemberPointController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMemberPointController.class);
  
	@Autowired
	private CustomerFacade customerFacade;
	
	@Autowired
	private GiftsOrderService giftOrderImpl;
	    
    @Autowired
    private MemberPointsService memberPointsService;
    @Autowired
    private CustomerAddressFacade customerAddressFacade;

    /**
     * 查询积分
     * */
	@RequestMapping(value="/memberPoint.html", method=RequestMethod.GET)
	public String displayRegistration(@Valid @ModelAttribute("criteria") MemberCriteria criteria,final Model model,@RequestParam(value = "page", defaultValue = "1") final int page, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
				
		PaginationData paginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
    	criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		
		
		Customer customer =null;
		//权限
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null &&
       		 request.isUserInRole("AUTH_CUSTOMER")) {
   		customer = customerFacade.getByID(auth.getName());

       }
		if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
		}
		/**
		if(!StringUtils.isBlank(request.getParameter("memberPointStatus"))){
			criteria.setStatus(Integer.parseInt(request.getParameter("memberPointStatus")));
		}
		*/
		
		
		criteria.setCustomerID(customer.getId());	
		MemberPointList memberPointList =memberPointsService.getByCriteria(criteria);
		 if(memberPointList!=null) {
	        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, memberPointList.getTotalCount()));
	        	model.addAttribute("emberPointList",memberPointList.getMemberPoints());
		 } else {
	        	model.addAttribute( "paginationData", null);
	        	}
		MemberPorintStatus[] allStatus = MemberPorintStatus.values();
		if(criteria.getMpsStatus() !=null ){
			model.addAttribute("mpsStatus",criteria.getMpsStatus());
		}
		
        model.addAttribute("MemberSpointStatus",allStatus);
		model.addAttribute("activeMenu","memberpoint");
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.memberpoint).append(".").append(store.getStoreTemplate());

		return template.toString();


	}
	
	@RequestMapping(value="/giftInfo.html", method=RequestMethod.GET)
	public String displayGift(@Valid @ModelAttribute("criteria") GftsOrderCriteria criteria,final Model model,@RequestParam(value = "page", defaultValue = "1") final int page, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
				
		PaginationData paginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
    	criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		
		
		Customer customer =null;
		//权限
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null &&
       		 request.isUserInRole("AUTH_CUSTOMER")) {
   		customer = customerFacade.getByID(auth.getName());

       }
		if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
		}
		
		
		
		criteria.setCustomerId(customer.getId());	
		GiftOrdersList gifts =giftOrderImpl.getByCriteria(criteria);
		 if(gifts!=null) {
	        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, gifts.getTotalCount()));
	        	List<ReadableGiftOrder> reads = new ArrayList<ReadableGiftOrder>();
	        	for(GiftOrder gorder:gifts.getGiftsOrder()){
	        		try{
	        			ReadableGiftOrder read = new ReadableGiftOrder();
		        		read.setCustomerName(gorder.getCustomerName());
			        		if(gorder!=null){
			        			 read.setDeliverAddress(gorder.getShippingAddress());
		        		         read.setDeliveryName(gorder.getDeliveryCode());
		        		         read.setDeliverNumber(gorder.getDeliveryNumber());
			        		}
		        		if(!StringUtils.isBlank(gorder.getDeliveryCode()))read.setDeliverCompany(gorder.getDeliveryCode());
		        		if(!StringUtils.isBlank(gorder.getDeliveryNumber()))read.setDeliverNumber(gorder.getDeliveryNumber());
		        		read.setImge(gorder.getGifImge());
		        		read.setQulity(gorder.getNumber());
		        		read.setName(gorder.getGifName());
		        		read.setStatus(gorder.getStatus());
		        		read.setOrderDate(gorder.getCreateDate().toString());
		        		reads.add(read);
	        		}catch (Exception e){
	        			continue;
	        		}
	        		   		
	        	}
	        	model.addAttribute("emberPointList",reads);   
		 
		 } else {
	        	model.addAttribute( "paginationData", null);
	    }		 
		 GiftStatus[] allStatus = GiftStatus.values();
		 
		if(criteria.getStatus() !=null ){
			model.addAttribute("mpsStatus",criteria.getStatus());
		}
		
		
        model.addAttribute("giftStatus",allStatus);
		model.addAttribute("activeMenu","giftInfo");
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.giftOrder).append(".").append(store.getStoreTemplate());

		return template.toString();


	}

	
	}

	



