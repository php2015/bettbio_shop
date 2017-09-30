package com.salesmanager.web.admin.controller.orders;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.common.model.DeliveryCompny;
import com.salesmanager.core.business.common.service.DeliveryCompnyService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.EzybioOrder;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.entity.order.SubOrder;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;
import com.salesmanager.web.utils.PinyinUtils;


/**
 * Manage order list
 * Manage search order
 * @author csamson 
 *
 */
@Controller
public class OrdersController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private ProductPriceUtils priceUtil;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private DeliveryCompnyService deliveryCompnyService;
	
	@Autowired
	protected ModuleConfigurationService moduleConfigurationService;
	 
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);

	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/list.html",  method={RequestMethod.GET,RequestMethod.POST})
	public String displayOrders(@Valid @ModelAttribute("criteria") OrderCriteria criteria,Model model, @RequestParam(value = "page", defaultValue = "1") final int page,
			final String splitSubOrder,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		//拆分子订单
		if (splitSubOrder != null && splitSubOrder != ""){
			orderFacade.spiltSubOrder(splitSubOrder);
		}
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		PaginationData paginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
		if(criteria == null){
			criteria = new OrderCriteria();
			//最近3个月数据
	        criteria.setBeginDatePurchased("3");
		}
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null &&
        		 request.isUserInRole("ADMIN")){
			criteria.setStoreId(-1l);
			criteria.setStoreId(-1l);
		}else{
			criteria.setStoreId(store.getId());
			criteria.setStoreId(store.getId());
		}
        ReadableOrderList readable= orderFacade.getStoreReadableOrderList(criteria, language);
        
        model.addAttribute( "customerOrders", readable);
        if(readable!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, readable.getTotal()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
        if(criteria.getStatus() !=null ){
			model.addAttribute("cstatus",criteria.getStatus());
		}
        OrderStatus[] allStatus = OrderStatus.values();
       //获取年份
        Calendar a=Calendar.getInstance();
        int endYear = a.get(Calendar.YEAR);
        //获取网站初始年份
        a.setTime(store.getInBusinessSince());
        int sinceYear = a.get(Calendar.YEAR);
       if(endYear>=sinceYear){
    	   ArrayList<String> years = new ArrayList<String>();
    	   for (;sinceYear<=endYear; sinceYear++){
    		   years.add(String.valueOf(sinceYear));
    	   }
    	   model.addAttribute("years",years);
       }
        model.addAttribute("OrderStatus",allStatus);
        model.addAttribute( "criteria", criteria);
		return ControllerConstants.Tiles.Order.orders;
		
		
	}
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/delivery.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String delivery(HttpServletRequest request) throws Exception {
		String suborderid = request.getParameter("suborderid");
		String dCode = request.getParameter("dCode");
		String dNo = request.getParameter("dNo");
		AjaxResponse resp = new AjaxResponse();
		if(suborderid != null && dCode !=null && !StringUtils.isBlank(dCode) && dNo != null && !StringUtils.isBlank(dNo)){
			dCode.trim().toUpperCase();
			List<DeliveryCompny> dels = deliveryCompnyService.getDeliveryByName(dCode);
			if(dels ==null || dels.size()==0){
				DeliveryCompny deliveryCompny = new DeliveryCompny();
				deliveryCompny.setDeliveryName(dCode);
				deliveryCompnyService.create(deliveryCompny);
			}
			int result = orderFacade.deliverySubOrder(suborderid, dCode, dNo);
			if(result == 0){
				resp.setStatus(1);
				resp.setStatusMessage(OrderStatus.SHIPPED.getValue().toUpperCase());
			}else{
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/orderDetails.html",  method={RequestMethod.GET,RequestMethod.POST})
	public String displayOrder(final Model model,final HttpServletRequest request,@RequestParam(value = "orderId" ,required=true) final String orderId) throws Exception {

		//get order id
        Long lOrderId = null;
        try {
        	lOrderId = Long.parseLong(orderId);
        } catch(NumberFormatException nfe) {
        	LOGGER.error("Cannot parse orderId to long " + orderId);
        	return "redirect:/"+Constants.SHOP_URI;
        }
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		long storeID ;
		if(auth != null &&
        		 request.isUserInRole("ADMIN")){
			storeID =-1l;
		}else{
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			storeID = store.getId();
		}
		EzybioOrder order = orderFacade.getEzybioOrder(lOrderId,language,storeID);

        model.addAttribute("order", order);
        setMenu(model,request);
		return  ControllerConstants.Tiles.Order.ordersEdit;

	}
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = deliveryCompnyService.getDeliveryName();
			
			List<String> ch = new ArrayList<String>();
			ch.add("b");
			ch.add("g");
			ch.add("k");
			ch.add("n");
			ch.add("r");
			ch.add("s");
			ch.add("w");
			ch.add("z");
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores,ch);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@RequestMapping(value={"/admin/orders/order.html"}, method=RequestMethod.POST)
	public @ResponseBody
	SubOrder getOrder(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String oId = request.getParameter("orderId");
		 try {
			 Language language = (Language)request.getAttribute(Constants.LANGUAGE);
			 return orderFacade.getSubOrderById(Long.parseLong(oId),language);	
			 
	        } catch(Exception nfe) {
	        	LOGGER.error("Cannot parse orderId to long " + oId);
	        	return null;
	        }
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("order", "order");
		activeMenus.put("order-list", "order-list");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("order");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	 protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
	    {
	        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
	       
	        return paginaionData;
	    }
}
