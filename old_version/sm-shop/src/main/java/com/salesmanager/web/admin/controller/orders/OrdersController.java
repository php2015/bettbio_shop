package com.salesmanager.web.admin.controller.orders;

import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_SUBORDER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_SUBORDER;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.common.model.DeliveryCompny;
import com.salesmanager.core.business.common.service.DeliveryCompnyService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.exception.ServiceExceptionV2;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.service.OrderPriceAdjustment;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.OrderServiceV2;
import com.salesmanager.core.business.order.service.OrderServiceV2Constants;
import com.salesmanager.core.business.order.service.OrderServiceV2Context;
import com.salesmanager.core.business.order.service.SubOrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.EzybioOrder;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.entity.order.SubOrder;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.ImageFilePathUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.OrderUtils;
import com.salesmanager.web.utils.PageBuilderUtils;
import com.salesmanager.web.utils.PinyinUtils;

import edu.emory.mathcs.backport.java.util.Collections;


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
	OrderServiceV2 orderServiceV2;
	
	@Autowired
	SubOrderService subOrderService;
	
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
	 
	@Autowired
    private MemberPointsService memberPointsService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerFacade customerFacade;
	
	@Autowired
	private LabelUtils messageUtils;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);

	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/list.html",  method={RequestMethod.GET,RequestMethod.POST})
	public String displayOrders(@Valid @ModelAttribute("criteria") OrderCriteria criteria,Model model, @RequestParam(value = "page", defaultValue = "1") final int page,
			final String splitSubOrder,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		//拆分子订单
		if (splitSubOrder != null && !splitSubOrder.isEmpty()){
			orderServiceV2.spiltSubOrderIntoNewOne(splitSubOrder);
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
        // 计算每个订单可以执行的操作
        if (readable!= null && readable.getEzybioOrder() != null){
        	OrderServiceV2Context orderCtx = OrderUtils.getOrderContext(request, customerFacade, userService);
        	boolean isAdminUser = request.isUserInRole("ADMIN");
        	for( EzybioOrder ezyOrder : readable.getEzybioOrder()){
        		Order order = ezyOrder.getRawOrder();
        		orderCtx.setOrder(order);
        		if (order.getSubOrders() != null){
        			for(com.salesmanager.core.business.order.model.SubOrder subOrder : order.getSubOrders()){
        				List<String> actionList = orderServiceV2.getActionList(orderCtx, subOrder, isAdminUser);
        				subOrder.setActionList(actionList);
        			}
        		}
        	}
        }
        
        model.addAttribute( "customerOrders", readable);
        if(readable!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, readable.getTotal()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
        if(criteria.getStatus() !=null ){
			model.addAttribute("cstatus",criteria.getStatus());
		}
        //OrderStatus[] allStatus = OrderStatus.values();
        // remove unused status for new order process flow
        OrderStatus[][] allStatus = new OrderStatus[][]{
        	new OrderStatus[]{OrderStatus.ORDERED, OrderStatus.ORDERED}, // pay_first: 审核中
        	new OrderStatus[]{OrderStatus.PROCESSED, OrderStatus.RECEIPT}, // pay_first: 待付款
        	new OrderStatus[]{OrderStatus.PAY_CONFIRMED, OrderStatus.PROCESSED}, // pay_first: 待发货
        	new OrderStatus[]{OrderStatus.SHIPPED, OrderStatus.SHIPPED}, // pay_first: 卖家已发货
        	new OrderStatus[]{OrderStatus.PAID, OrderStatus.PAID}, // pay_first: 买家已付款
        	new OrderStatus[]{OrderStatus.CLOSE, OrderStatus.CLOSE}, // pay_first: 已关闭
        };
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
	@RequestMapping(value="/admin/orders/delivery.html", method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public @ResponseBody String delivery(HttpServletRequest request, Locale locale) throws Exception {
		String suborderid = request.getParameter("suborderid");
		String dCode = request.getParameter("dCode");
		String dNo = request.getParameter("dNo");
		AjaxResponse resp = new AjaxResponse();
		if(suborderid != null && dCode !=null && !StringUtils.isBlank(dCode) && dNo != null && !StringUtils.isBlank(dNo)){
			dCode = dCode.trim().toUpperCase();
			List<DeliveryCompny> dels = deliveryCompnyService.getDeliveryByName(dCode);
			if(dels ==null || dels.size()==0){
				DeliveryCompny deliveryCompny = new DeliveryCompny();
				deliveryCompny.setDeliveryName(dCode);
				deliveryCompnyService.create(deliveryCompny);
			}
			//int result = orderFacade.deliverySubOrder(suborderid, dCode, dNo);
			try{
				OrderServiceV2Context orderCtx = OrderUtils.getOrderContext(request, null, userService);
				orderServiceV2.deliverySubOrder(orderCtx, suborderid, dCode, dNo);
				resp.setStatus(1);
				resp.setStatusMessage(OrderStatus.SHIPPED.getValue().toUpperCase());
			} catch (ServiceExceptionV2 e){
				String msg = messages.getServiceExceptionV2Message(e, OrderServiceV2Constants.ERROR_MSG_PREFIX, "无法填写发货信息", locale);
				resp.setErrorString(msg);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}catch (Exception e){
				resp.setErrorMessage(e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}else{
			resp.setErrorString("请正确填写完整的快递单号和快递公司");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/orderDetails.html",  method={RequestMethod.GET,RequestMethod.POST})
	public String displayOrder(final Model model,final HttpServletRequest request,@RequestParam(value = "w" ,required=true) final String orderId) throws Exception {

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
		//User user = (User) request.getAttribute(Constants.ADMIN_USER);
		User user = (User) request.getSession().getAttribute(Constants.ADMIN_USER);
		if (user != null){
			user = userService.getById(user.getId());
		}
		long storeID ;
		if (auth != null && request.isUserInRole("ADMIN")) {
			storeID = -1l;
		} else {
			MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
			storeID = store.getId();
		}
		EzybioOrder order = orderFacade.getEzybioOrder(lOrderId,language,storeID);
		Order rawOrder = order.getRawOrder();
		OrderServiceV2Context orderContext = new OrderServiceV2Context();
		orderContext.setCustomer(null);
		orderContext.setUser(user);
		orderContext.setOrder(rawOrder);

		boolean canAdjustPrice = false;
		Set<Long> viewableSubOrderIds = new HashSet<Long>();
		if (rawOrder != null){
			boolean isAdminUser = request.isUserInRole("ADMIN");
			Set<com.salesmanager.core.business.order.model.SubOrder> subOrders = rawOrder.getSubOrders();
			for(com.salesmanager.core.business.order.model.SubOrder subOrder: subOrders){
				List<String> actions = orderServiceV2.getActionList(orderContext, subOrder, isAdminUser);
				subOrder.setActionList(actions);
				if (actions != null){
					viewableSubOrderIds.add(subOrder.getId());
				}
				canAdjustPrice = canAdjustPrice || actions.contains(GlobalConstants.OrderAction_AdjustPrice);
				for(OrderProduct item: subOrder.getOrderProducts()){
					item.setProductImageUrl(makeProductImageUrl(item));
				}
			}
		}
		model.addAttribute("canAdjustPrice", canAdjustPrice);
        model.addAttribute("order", order);
        Set<OrderStatusHistory> logs = rawOrder.getOrderHistory();
        if (logs != null){
        	List<OrderStatusHistory> logList = new ArrayList<OrderStatusHistory>();
        	for(OrderStatusHistory log : logs){
        		if (log.getSubOrderId() == null){
        			logList.add(log);
        			continue;
        		}
        		if (viewableSubOrderIds.contains(log.getSubOrderId())){
        			logList.add(log);
        		}
        	}
        	Collections.sort(logList, new Comparator<OrderStatusHistory>(){

				@Override
				public int compare(OrderStatusHistory o1, OrderStatusHistory o2) {
					return o2.getDateAdded().compareTo(o1.getDateAdded());
				}
        		
        	});
        	model.addAttribute("logList", logList);
        }
        setMenu(model,request);
		return  ControllerConstants.Tiles.Order.ordersEdit;

	}
	
	private String makeProductImageUrl(OrderProduct item) {
		Long productId = item.getProductID();
		Product product = productService.getById(productId);
		if (product == null){
			return null;
		}
		ProductImage img = product.getProductImage();
		if (img == null){
			return null;
		}
		
		String imagePath = ImageFilePathUtils.buildProductImageFilePath(product.getMerchantStore(), product.getSku(), img.getProductImage());
		return imagePath;
	}

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
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
	
	/*
	 * 对于子订单，进行商品确认付款
	 * 遍历子订单的状态，如果均修正为关闭状态（即交易完成），则主订单状态也修改为关闭状态
	 */
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value={"/admin/orders/confirmPaid.html"}, method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public @ResponseBody String confirmPaid(String orderid, String suborderid, HttpServletRequest request, Locale locale) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		Date dt = new Date();
//		Customer customer = (Customer)request.getAttribute(Constants.CUSTOMER);
		
		if(orderid != null){
			Order order = orderService.getById(Long.valueOf(orderid));
			//判断该订单是否是合法的
//			if(!order.getCustomerId().equals(customer.getId())){//不是该用户的订单记录，无更改权限
//				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
//				resp.setStatusMessage(messages.getMessage("message.access.denied", locale));
//				return resp.toJSONString();
//			}
			//进行子订单遍历处理
			Set<com.salesmanager.core.business.order.model.SubOrder> subOrderSet = order.getSubOrders();
			List<OrderStatus> subOrderStatus = new ArrayList<OrderStatus>(subOrderSet.size());
			for (com.salesmanager.core.business.order.model.SubOrder subOrder : subOrderSet) {
				if(subOrder.getId().equals(Long.valueOf(suborderid))){
					subOrder.setStatus(OrderStatus.PAY_CONFIRMED); //确认收款
					subOrder.setLastModified(dt);
					subOrderService.update(subOrder);
				}
				subOrderStatus.add(subOrder.getStatus());
			}
			
			boolean flag = !order.getStatus().equals(OrderStatus.calcFinalStatus(subOrderStatus));
			
			if (flag) {
				//修订主订单状态，修改为已确认支付
				order.setStatus(OrderStatus.PAY_CONFIRMED);
				order.setLastModified(dt);
				OrderStatusHistory orderStatusHistory = new OrderStatusHistory();	
				orderStatusHistory.setStatus(OrderStatus.PAY_CONFIRMED);
				orderStatusHistory.setDateAdded(dt);
				orderStatusHistory.setOrder(order);
				order.getOrderHistory().add(orderStatusHistory);
				orderService.update(order);
				
				//修改积分状态
				//List<MemberPoints> memberPointsList = memberPointsService.list();
				updateMemberPointAfterPaiConfirmed(order);
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			resp.setStatusMessage(OrderStatus.PAY_CONFIRMED.getValue().toUpperCase());
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("message.cannot.empty", locale));
		}
		//System.out.println(resp.toJSONString());
		return resp.toJSONString();
	}

	protected void updateMemberPointAfterPaiConfirmed(Order order) throws ServiceException {
		String[] paramStr = {"orderId"};
		Object[] paramObj = {order.getId()};
		List<MemberPoints> memberPointsList = memberPointsService.findByProperties(paramStr, paramObj, null);
		if (memberPointsList != null && memberPointsList.size() > 0) {
			for (MemberPoints points : memberPointsList) {
				if (points.getOrderId() != null 
						&& points.getOrderId().longValue() == order.getId().longValue()) {
					points.setStatas(0);
					Date thisTime = new Date();
					points.setUpdateDate(thisTime);//更新日期
					Calendar cal = Calendar.getInstance();
		        	cal.setTime(thisTime);
		        	cal.add(Calendar.YEAR,1);//积分有效期：一年
		        	points.setDateValid(cal.getTime());
		        	points.setLtfePoint(points.getLtfePoint() + Long.valueOf(points.getValue()));
					memberPointsService.update(points);
				}
			}
		}
	}
	
	 protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
	    {
	        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
	       
	        return paginaionData;
	    }
	 
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/order/savePrice.html", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String savePrice(@RequestBody OrderPriceAdjustment priceData, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse ajaxResp = new AjaxResponse();
		try{
			OrderServiceV2Context orderCtx = OrderUtils.getOrderContext(request, null, userService);
			orderServiceV2.adjustPrice(orderCtx, priceData);
			ajaxResp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		}catch (ServiceExceptionV2 e){
			ajaxResp.addEntry("code", String.valueOf(e.getErrorCode()));
			ajaxResp.addEntry("messageKey", e.getMessageKey());
			if (e.getMessageParams() != null){
				for(int i=0; i< e.getMessageParams().size();i++){
					ajaxResp.addEntry("messageParam"+(i+1), String.valueOf(e.getMessageParams().get(i)));
				}
			}
			ajaxResp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}catch(Exception e){
			ajaxResp.addEntry("message", e.getMessage());
			ajaxResp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return ajaxResp.toJSONString();
	}
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "admin/order/action/actionEvent.html", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String noParamAction(Long soid, String actionName, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		AjaxResponse ajaxResp = new AjaxResponse();
		OrderServiceV2Context orderContext = OrderUtils.getOrderContext(request, customerFacade, userService);
		
		try{
			com.salesmanager.core.business.order.model.SubOrder subOrder = subOrderService.getById(soid);
			if (subOrder == null) {
				throw new ServiceExceptionV2(CODE_NO_SUBORDER, MESSAGE_NO_SUBORDER, soid);
			}
			Order order = orderServiceV2.getById(subOrder.getOrder().getId());
			orderContext.setOrder(order);
			orderServiceV2.onSuborderStatusAction(orderContext, soid, actionName, request.isUserInRole("ADMIN"));
//			if (GlobalConstants.OrderAction_ConfirmPay.equals(actionName)){
//				updateMemberPointAfterPaiConfirmed(order);
//			}
			ajaxResp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		}catch (ServiceExceptionV2 e){
			String message = messageUtils.getServiceExceptionV2Message(e, OrderServiceV2Constants.ERROR_MSG_PREFIX, "操作失败", locale);
			LOGGER.error(message);
			ajaxResp.addEntry("code", String.valueOf(e.getErrorCode()));
			ajaxResp.addEntry("message", message);
			ajaxResp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}catch(Exception e){
			e.printStackTrace();
			ajaxResp.addEntry("message", e.getMessage());
			ajaxResp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return ajaxResp.toJSONString();
	}
	
	
}
