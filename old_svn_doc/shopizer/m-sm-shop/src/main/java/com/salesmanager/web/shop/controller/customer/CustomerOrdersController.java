package com.salesmanager.web.shop.controller.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.SubOrderService;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductDownloadService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.EzybioOrder;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

@Controller
@RequestMapping(Constants.SHOP_URI + "/customer")
public class CustomerOrdersController extends AbstractController {
	
    @Autowired
	private MerchantStoreService merchantStoreService;
    
    @Autowired
    private LanguageService languageService;
    
    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private CustomerFacade customerFacade;
    
	@Autowired
	private OrderProductDownloadService orderProdctDownloadService;
    
	@Autowired
	private SubOrderService subOrderService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private LabelUtils messages;
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrdersController.class);
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/orders.html", method={RequestMethod.GET,RequestMethod.POST})
	public String listOrders(@Valid @ModelAttribute("criteria") OrderCriteria criteria,Model model, @RequestParam(value = "page", defaultValue = "1") final int page, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
    	LOGGER.info( "Fetching orders for current customer" );
        MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
        Language language = getSessionAttribute(Constants.LANGUAGE, request);
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());

        }
    	
    	if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
    	}
    	if(criteria == null){
			criteria = new OrderCriteria();
			
			String status = request.getParameter("status");
			if(status !=null && !StringUtils.isBlank(status)){				
				criteria.setStatus(OrderStatus.valueOf(status));
			}
			
			//最近3个月数据
	        //criteria.setBeginDatePurchased("3");
		} /**else {
			if (StringUtils.isBlank(criteria.getBeginDatePurchased())) {
				criteria.setBeginDatePurchased("3");
			}
		}*/
    	PaginationData paginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
    	criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		List<Long> cid = new ArrayList<Long>();
		cid.add(customer.getId());
		criteria.setCustomerId(cid);
		//criteria.setStoreId(store.getId());
       
        ReadableOrderList readable= orderFacade.getReadableOrderList(criteria, language);
        
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
       
        model.addAttribute("OrderStatus",allStatus);
        model.addAttribute( "criteria", criteria);
        model.addAttribute("activeMenu","orders");
        
        StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerOrders).append(".").append(store.getStoreTemplate());
        return template.toString();
	}
	

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
    @RequestMapping(value="/order.html", method={RequestMethod.GET,RequestMethod.POST})
    public String orderDetails(final Model model,final HttpServletRequest request,@RequestParam(value = "orderId" ,required=true) final String orderId) throws Exception{
        
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		 Language language = getSessionAttribute(Constants.LANGUAGE, request);
				
		if(StringUtils.isBlank( orderId )){
        	LOGGER.error( "Order Id can not be null or empty" );
        }
        LOGGER.info( "Fetching order details for Id " +orderId);
        
        //get order id
        Long lOrderId = null;
        try {
        	lOrderId = Long.parseLong(orderId);
        } catch(NumberFormatException nfe) {
        	LOGGER.error("Cannot parse orderId to long " + orderId);
        	return "redirect:/"+Constants.SHOP_URI;
        }
        
        
        //check if order belongs to customer logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());
        }
    	
    	if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
    	}
    	
    	EzybioOrder order = orderFacade.getEzybioOrder(lOrderId,language,-1l);
    	
        model.addAttribute("order", order);

        StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerOrder).append(".").append(store.getStoreTemplate());
        return template.toString();
        
    }
	
	/*
	 * 对于子订单，进行商品确认收货
	 * 遍历子订单的状态，如果均修正为待付款状态（即已收货），则主订单状态也修改为待付款状态
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/confirmReceived.html", method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public @ResponseBody String confirmReceived(String orderid, String suborderid, HttpServletRequest request, Locale locale) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		Date dt = new Date();
		Customer customer = (Customer)request.getAttribute(Constants.CUSTOMER);
		
		if(orderid != null){
			Order order = orderService.getById(Long.valueOf(orderid));
			//判断该订单是否是合法的
			if(!order.getCustomerId().equals(customer.getId())){//不是该用户的订单记录，无更改权限
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setStatusMessage(messages.getMessage("message.access.denied", locale));
				return resp.toJSONString();
			}
			//进行子订单遍历处理
			boolean flag = true; //判断是否所有的子订单状态都修正为待发货
			Set<SubOrder> subOrderSet = order.getSubOrders();
			for (SubOrder subOrder : subOrderSet) {
				if(subOrder.getId().equals(Long.valueOf(suborderid))){
					subOrder.setStatus(OrderStatus.PAID); //待付款
					subOrder.setLastModified(dt);
					subOrderService.update(subOrder);
				} else {
					if(!subOrder.getStatus().getValue().equalsIgnoreCase(OrderStatus.PAID.getValue())) {
						flag = false; //有子订单仍然处于待发货状态
					}
				}
			}
			if (flag) {
				//修订主订单状态，修改为待付款
				order.setStatus(OrderStatus.PAID);
				order.setLastModified(dt);
				OrderStatusHistory orderStatusHistory = new OrderStatusHistory();	
				orderStatusHistory.setStatus(OrderStatus.SHIPPED);
				orderStatusHistory.setDateAdded(dt);
				orderStatusHistory.setOrder(order);
				order.getOrderHistory().add(orderStatusHistory);
				orderService.update(order);
				
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			resp.setStatusMessage(OrderStatus.SHIPPED.getValue().toUpperCase());
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("message.cannot.empty", locale));
		}
		System.out.println(resp.toJSONString());
		return resp.toJSONString();
	}
}
