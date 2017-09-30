package com.salesmanager.web.shop.controller.customer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.OrderServiceV2;
import com.salesmanager.core.business.order.service.OrderServiceV2Context;
import com.salesmanager.core.business.order.service.SubOrderService;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductDownloadService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.modules.cms.image.ImageDataInfo;
import com.salesmanager.core.modules.cms.image.ImageStorage;
import com.salesmanager.core.modules.cms.image.PayProofImageUtils;
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
import com.salesmanager.web.utils.OrderUtils;
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
	private ImageStorage imageStorage;
	
	@Autowired
	private LabelUtils messages;

	@Autowired
	private OrderServiceV2 orderServiceV2;

	@Autowired
	private ProductService productService;
	
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
        OrderServiceV2Context orderCtx = OrderUtils.getOrderContext(request, customerFacade, null);
		orderCtx.setUser(null);
        // 计算每个订单可以执行的操作
        if (readable!= null && readable.getEzybioOrder() != null){
        	for( EzybioOrder ezyOrder : readable.getEzybioOrder()){
        		Order order = ezyOrder.getRawOrder();
        		orderServiceV2.fillPricingInfomation(order);
        		orderCtx.setOrder(order);
        		if (order.getSubOrders() != null){
        			for(com.salesmanager.core.business.order.model.SubOrder subOrder : order.getSubOrders()){
        				List<String> actionList = orderServiceV2.getActionList(orderCtx, subOrder, false);
        				subOrder.setActionList(actionList);
        				for(OrderProduct oprod : subOrder.getOrderProducts()){
        					oprod.setProductImageUrl(makeProductImageUrl(oprod));
        				}
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
        // clariones: use limited order status for new order process flow
        OrderStatus[][] allStatus = new OrderStatus[][]{
        	new OrderStatus[]{OrderStatus.ORDERED, OrderStatus.ORDERED}, // pay_first: 审核中
        	new OrderStatus[]{OrderStatus.SHIPPED, OrderStatus.SHIPPED}, // pay_first: 卖家已发货
        	new OrderStatus[]{OrderStatus.PROCESSED, OrderStatus.RECEIPT}, // pay_first: 待付款
        	//new OrderStatus[]{OrderStatus.PAY_CONFIRMED, OrderStatus.PROCESSED}, // pay_first: 待发货
        	//new OrderStatus[]{OrderStatus.PAID, OrderStatus.PAID}, // pay_first: 买家已付款
        	new OrderStatus[]{OrderStatus.CLOSE, OrderStatus.CLOSE}, // pay_first: 已关闭
        };
       
        model.addAttribute("OrderStatus",allStatus);
        model.addAttribute( "criteria", criteria);
        model.addAttribute("activeMenu","orders");
        
        StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerOrders).append(".").append(store.getStoreTemplate());
        return template.toString();
	}
	

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
    @RequestMapping(value="/order.html", method={RequestMethod.GET,RequestMethod.POST})
    public String orderDetails(final Model model,final HttpServletRequest request,@RequestParam(value = "w" ,required=true) final String orderId) throws Exception{
        
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
			Set<SubOrder> subOrderSet = order.getSubOrders();
			List<OrderStatus> subOrderStatus = new ArrayList<OrderStatus>(subOrderSet.size());
			for (SubOrder subOrder : subOrderSet) {
				if(subOrder.getId().equals(Long.valueOf(suborderid))){
					subOrder.setStatus(OrderStatus.CLOSE); //待付款
					subOrder.setLastModified(dt);
					subOrderService.update(subOrder);
				}
				subOrderStatus.add(subOrder.getStatus());
			}
			
			boolean flag = !order.getStatus().equals(OrderStatus.calcFinalStatus(subOrderStatus));
			if (flag) {
				//修订主订单状态，修改为待付款
				order.setStatus(OrderStatus.CLOSE);
				order.setLastModified(dt);
				OrderStatusHistory orderStatusHistory = new OrderStatusHistory();	
				orderStatusHistory.setStatus(OrderStatus.CLOSE);
				orderStatusHistory.setDateAdded(dt);
				orderStatusHistory.setOrder(order);
				order.getOrderHistory().add(orderStatusHistory);
				orderService.update(order);
				
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			resp.setStatusMessage(OrderStatus.CLOSE.getValue().toUpperCase());
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("message.cannot.empty", locale));
		}
		System.out.println(resp.toJSONString());
		return resp.toJSONString();
	}
	
	/*
	 * 子订单支付凭证上传。
	 * 
	 * 可能是第一次上传，也可能是更改支付凭证。
	 * 子订单支付凭证上传后，就认为是‘PAID’状态了。 当所有子订单都变成PAID状态了，订单本身也就是PAID了。
	 * 
	 * 注意：上传文件后，返回的Ajax response， content-type 需要设置为 text/html. Unknown issue.
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/uploadPayProof.html", method=RequestMethod.POST,produces="text/html; charset=utf-8")
	public @ResponseBody String savePayProofImage(HttpServletRequest request, HttpServletResponse response,
			Locale locale) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		Date dt = new Date();
		try {
			MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
			MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;

			String image = request.getParameter("file");
			String fname = request.getParameter("fname");
			String orderId = request.getParameter("oid");
			String suborderId = request.getParameter("soid");
			MultipartFile file = mureq.getFile(image);
			String imageFileName = null;
			// response.setCharacterEncoding("utf-8");
			// String filename=file.getOriginalFilename();
			// file.getOriginalFilename();
			// String callback = request.getParameter("CKEditorFuncNum");
			if (fname != null && !fname.isEmpty() && file.getSize() > 0) {
				String fileSuffix = StringUtils.substring(fname, fname.lastIndexOf(".") + 1);
				if (!StringUtils.equalsIgnoreCase(fileSuffix, "jpg")
						&& !StringUtils.equalsIgnoreCase(fileSuffix, "jpeg")
						&& !StringUtils.equalsIgnoreCase(fileSuffix, "bmp")
						&& !StringUtils.equalsIgnoreCase(fileSuffix, "gif")
						&& !StringUtils.equalsIgnoreCase(fileSuffix, "png")) {
					// return "<script type=\"text/javascript\">alert('Image
					// file format is not correct,must be
					// jpg|jpeg|bmp|gif|png');</script>";
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
					return resp.toJSONString();
				}

				// save image
				imageFileName = PayProofImageUtils.getImageUri(Long.parseLong(orderId), Long.parseLong(suborderId),
						fileSuffix);
				ImageDataInfo imageData = new ImageDataInfo();
				imageData.setContentType("image/" + fileSuffix);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				IOUtils.copy(file.getInputStream(), bout);
				byte[] imageBlob = bout.toByteArray();
				imageData.setImageBlob(imageBlob);
				imageData.setUri(imageFileName);
				imageStorage.saveImage(imageData);
			}
			if (orderId != null) {
				Order order = orderService.getById(Long.valueOf(orderId));

				// 进行子订单遍历处理
				Set<SubOrder> subOrderSet = order.getSubOrders();
				List<OrderStatus> subOrderStatus = new ArrayList<OrderStatus>(subOrderSet.size());
				for (SubOrder subOrder : subOrderSet) {
					if (subOrder.getId().equals(Long.valueOf(suborderId))) {
						subOrder.setStatus(OrderStatus.PAID); // 支付凭证已提交
						subOrder.setLastModified(dt);
						String oldImage = subOrder.getPayProof();
						if (oldImage != null && !oldImage.equals(imageFileName)){
							imageStorage.removeImage(oldImage);
						}
						subOrder.setPayProof(imageFileName);
						subOrderService.update(subOrder);
					} 
					subOrderStatus.add(subOrder.getStatus());
				}
				
				boolean flag = !order.getStatus().equals(OrderStatus.calcFinalStatus(subOrderStatus));
				
				if (flag) {
					// 修订主订单状态，修改为已付款
					order.setStatus(OrderStatus.PAID);
					order.setLastModified(dt);
					OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
					orderStatusHistory.setStatus(OrderStatus.PAID);
					orderStatusHistory.setDateAdded(dt);
					orderStatusHistory.setOrder(order);
					order.getOrderHistory().add(orderStatusHistory);
					orderService.update(order);

				}
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			} else {
				resp.setStatusMessage("没有OrderID");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
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
		
		return new StringBuilder().append("/static/").append(product.getMerchantStore().getCode())
				.append("/").append(FileContentType.PRODUCT.name()).append("/")
				.append(product.getSku()).append("/").append(img.getProductImage()).toString();
	}
}
