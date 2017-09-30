package com.salesmanager.web.shop.controller.order;

import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_SUBORDER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_SUBORDER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.price.BrandDiscountService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.exception.ServiceExceptionV2;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.OrderServiceV2;
import com.salesmanager.core.business.order.service.OrderServiceV2Constants;
import com.salesmanager.core.business.order.service.OrderServiceV2Context;
import com.salesmanager.core.business.order.service.SubOrderService;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductDownloadService;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductService;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.utils.BaseDataUtils;
import com.salesmanager.core.utils.DataTypeUitls;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.CustomerAdress;
import com.salesmanager.web.entity.customer.DefautAddressInvoice;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.order.ShopOrder;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerAddressFacade;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.OrderUtils;
import com.salesmanager.web.utils.SessionUtil;

@Controller
@RequestMapping(Constants.SHOP_URI+"/order")
public class ShoppingOrderController extends AbstractController {
	
	private static final Logger LOGGER = LoggerFactory
	.getLogger(ShoppingOrderController.class);
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	
    @Autowired
    private ShoppingCartService shoppingCartService;

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private CustomerFacade customerFacade;
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
    private AuthenticationManager customerAuthenticationManager;
	
	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;
	
	@Autowired
	private OrderProductDownloadService orderProdctDownloadService;
	
	@Autowired
    private CustomerAddressFacade customerAddressFacade;
	
	@Autowired
    private MemberPointsService memberPointsService;
	
	@Autowired
	private OrderProductService oraderProductService;
	
	@Autowired
	private BrandDiscountService brandDiscountService;
	
	@Autowired
	private OrderServiceV2 orderServiceV2;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SubOrderService subOrderService;

	@Autowired
	private LabelUtils messageUtils;
	
	@SuppressWarnings("unused")
	@RequestMapping("/checkout.html")
	public String displayCheckout(final String selecteditem,@CookieValue("cart") String cookie, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		//Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());

        }
    	//modify by cy update to cart if the quantity has been changed 
	     if(customer == null){
	    	 //String message=  messages.getMessage("message.logon", locale);
				//model.addAttribute( "information", message );	
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon).append(".").append(store.getStoreTemplate());
				return template.toString();

	     }
	     if(customer.getAnonymous()<3){
	    	 String message=  messages.getMessage("message.custmoer.active", locale);
				model.addAttribute( "information", message );	
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.information).append(".").append(store.getStoreTemplate());
				return template.toString();

	     }
		List<ShoppingCartItem> items =new ArrayList<ShoppingCartItem>();
		if(null == selecteditem || selecteditem ==""){
			return "redirect:/shop/cart/shoppingCart.html";
		}
		
		String code="";
		Object objRegions=JSONValue.parse(selecteditem); 
		JSONArray arrayRegions=(JSONArray)objRegions;
		Iterator i = arrayRegions.iterator();
		while(i.hasNext()) {
			ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
			Map values = (Map)i.next();
			shoppingCartItem.setId(Long.parseLong(values.get("id").toString().substring(9)));
			shoppingCartItem.setQuantity(Integer.parseInt(values.get("quantity").toString()));
			code=values.get("code").toString();
			items.add(shoppingCartItem);
		}
		 //Set<ShoppingCartItem> items = cart.getLineItems();
	     if(CollectionUtils.isEmpty(items) ) {
				return "redirect:/shop/shoppingCart.html";
	     }
		
		ShoppingCartData shoppingCart = shoppingCartFacade.updateModelCartItems(items,code, store, language,customer);
		try {
			BigDecimal total = new BigDecimal(shoppingCart.getTotal());
			//long total = Long.valueOf(shoppingCart.getTotal());
			Map <String,Integer> cmap = (Map <String,Integer>)request.getSession().getServletContext().getAttribute("custmoerGradeMap");
			BigDecimal grade = new BigDecimal(cmap.get(String.valueOf(customer.getGrade())));
			OrderCriteria criteria = new OrderCriteria();
			criteria.setcId(customer.getId());
			BigDecimal uesed = new BigDecimal(orderService.getTotalMoney(customer));
			int result = total.compareTo(grade.subtract(uesed));
				if(result == 1){					 
					StringBuilder message1= new StringBuilder().append(messages.getMessage("label.generic.current", locale)).append(messages.getMessage("label.generic.grade", locale)).append(messages.getMessage("message.order.limit", locale));
					message1.append(pricingService.getDisplayAmount(grade, store)+"                ").append(messages.getMessage("label.generic.current", locale)).append(messages.getMessage("message.order.left", locale));
					message1.append(pricingService.getDisplayAmount(grade.subtract(uesed),store)).append(messages.getMessage("message.order.change", locale));
					model.addAttribute( "information", message1.toString() );	
					StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.information).append(".").append(store.getStoreTemplate());
					return template.toString();
				}
		}catch (Exception e){
			return "redirect:/shop/shoppingCart.html";
		}
		//Get the cart from the DB
		
		String shoppingCartCode  = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
		ShoppingCart cart = null;
	
	    if(StringUtils.isBlank(shoppingCartCode)) {
				
			if(cookie==null) {//session expired and cookie null, nothing to do
				return "redirect:/shop/cart/shoppingCart.html";
			}
			String merchantCookie[] = cookie.split("_");
			String merchantStoreCode = merchantCookie[0];
		
			shoppingCartCode = merchantCookie[1];
	    	
	    } 
	    
	    cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store, customer);
	
	    if(cart==null && customer!=null) {
				cart=shoppingCartFacade.getShoppingCartModel(customer, store);
	    }
	    
	    super.setSessionAttribute(Constants.SHOPPING_CART, cart.getShoppingCartCode(), request);
	
	    if(shoppingCartCode==null && cart==null) {//error
				return "redirect:/shop/cart/shoppingCart.html";
	    }
			
	//delivery change to DefaultAddressInvoice
	      DefautAddressInvoice defautAddressInvoice = null;
	    if(customer!=null) {
	    	/**
			if(cart.getCustomerId()!=customer.getId().longValue()) {
					return "redirect:/shop/shoppingCart.html";
			}
			*/
			//reload customer again
			//customer = customerFacade.getCustomerForLogin(customer.getPhone());
			 //get defaultaddress
	    	 defautAddressInvoice = customerAddressFacade.initDefaultData(customer, language);
	    	//set DefaultAddressInvoice	    
	    	 if(null != defautAddressInvoice.getDefaultAddress()){
	    		 model.addAttribute("defautAddress", defautAddressInvoice.getDefaultAddress());
	    	 }
	    	 if(null != defautAddressInvoice.getDefaultInvoice()){
	    		 model.addAttribute("defautInvoice", defautAddressInvoice.getDefaultInvoice());
	    	 }
	 	    
	     } else {
			 return "redirect:/shop/shoppingCart.html";
	     }
	
	    
		
		boolean freeShoppingCart = shoppingCartService.isFreeShoppingCart(cart);
		
		  
		model.addAttribute( "cart", shoppingCart );
//		model.addAttribute("order",order);
		//model.addAttribute("paymentMethods", paymentMethods);
		//for add new address
		model.addAttribute( "address", new CustomerAdress());
		//model.addAttribute( "customerId", customer.getId() );
		//for add new invoice
		model.addAttribute( "invoice", new CustomerInvoice());
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(store.getStoreTemplate());
		return template.toString();
		
		
	}
	
	
	@RequestMapping("/commitPreAuthorized.html")
	public String commitPreAuthorizedOrder(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		ShopOrder order = super.getSessionAttribute(Constants.ORDER, request);
		if(order==null) {
			StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
			return template.toString();	
		}
		

		
		try {
			
			OrderTotalSummary totalSummary = super.getSessionAttribute(Constants.ORDER_SUMMARY, request);
			
			if(totalSummary==null) {
				totalSummary = orderFacade.calculateOrderTotal(store, order, language);
				super.setSessionAttribute(Constants.ORDER_SUMMARY, totalSummary, request);
			}
			
			
			order.setOrderTotalSummary(totalSummary);
			
			//already validated, proceed with commit
			//add by cy add cu
			Order orderModel = this.commitOrder(order, request, locale);
			super.setSessionAttribute(Constants.ORDER_ID, orderModel.getId(), request);
			
			return "redirect:/shop/order/confirmation.html";
			
		} catch(Exception e) {
			LOGGER.error("Error while commiting order",e);
			throw e;		
			
		}

	}
	
	
	private Order commitOrder(ShopOrder order, HttpServletRequest request, Locale locale) throws Exception, ServiceException {
		
		
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
			Language language = (Language)request.getAttribute("LANGUAGE");
			
			//remove by cy
			//PersistableCustomer customer = order.getCustomer();
			
	        /** set username and password to persistable object **/
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer authCustomer = null;
        	if(auth != null &&
	        		 request.isUserInRole("AUTH_CUSTOMER")) {
        		authCustomer =customerFacade.getByID(auth.getName());        	
	        }
        	//add by cy
           if(authCustomer == null){
        	  return null;
           }
	        
	        Order modelOrder = null;
	        Transaction initialTransaction = (Transaction)super.getSessionAttribute(Constants.INIT_TRANSACTION_KEY, request);
	        if(initialTransaction!=null) {
	        	modelOrder=orderFacade.processOrder(order, authCustomer, initialTransaction, store, language);
	        } else {
	        	modelOrder=orderFacade.processOrder(order, authCustomer, store, language);
	        }
	        
	        //save order id in session
	        super.setSessionAttribute(Constants.ORDER_ID, modelOrder.getId(), request);
	        //set a unique token for confirmation
	        super.setSessionAttribute(Constants.ORDER_ID_TOKEN, modelOrder.getId(), request);
	        

			//get cart
			String cartCode = super.getSessionAttribute(Constants.SHOPPING_CART, request);
			if(StringUtils.isNotBlank(cartCode)) {
				try {
					ShoppingCart cart = shoppingCartService.getByCode(cartCode, store);
					cart = shoppingCartFacade.deleteShoppingCart(cart, store, authCustomer);
					
					if(cart ==null){
						super.removeAttribute(Constants.SHOPPING_CART, request);
					}
				} catch(Exception e) {
					LOGGER.error("Cannot delete cart " + cartCode, e);
					throw new ServiceException(e);
				}
			}

			
	        //cleanup the order objects
	        super.removeAttribute(Constants.ORDER, request);
	        super.removeAttribute(Constants.ORDER_SUMMARY, request);
	        super.removeAttribute(Constants.INIT_TRANSACTION_KEY, request);
	        super.removeAttribute(Constants.SHIPPING_OPTIONS, request);
	        super.removeAttribute(Constants.SHIPPING_SUMMARY, request);
	        
	        

	        try {
		        //refresh customer --
	        	authCustomer = customerFacade.getCustomerForLogin(authCustomer.getPhone());
		       
				//send order confirmation email
	        	if(authCustomer.isRecieveEmail()==true){
	        		emailTemplatesUtils.sendOrderEmail(authCustomer, modelOrder, locale, language, store, request.getContextPath());
	        	}
	        	// send order to store
	        	Set<SubOrder> subs = modelOrder.getSubOrders();
	        	for(SubOrder s:subs){
	        		emailTemplatesUtils.sendOrderEmailtoStore(s, modelOrder, locale, language, store, request.getContextPath());
	        	}
	    		
	        } catch(Exception e) {
	        	LOGGER.error("Error while post processing order",e);
	        }
			
	        return modelOrder;
		
		
	}

	
	protected boolean checkCustomer(OrderProcessContext ctx){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	HttpServletRequest request = ctx.getRequest();
    	MerchantStore store = ctx.getStore();
		if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());

        }
    	//modify by cy update to cart if the quantity has been changed 
	     if(customer == null){
	    	 //String message=  messages.getMessage("message.logon", locale);
				//model.addAttribute( "information", message );	
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon).append(".").append(store.getStoreTemplate());
				ctx.setErrorViewName( template.toString());
				return false;
	     }
	     if(customer.getAnonymous() < 3){
	    	 Locale locale = ctx.getLocale();
			String message=  messages.getMessage("message.custmoer.active", locale);
				Model model = ctx.getModel();
				model.addAttribute( "information", message );	
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.information).append(".").append(store.getStoreTemplate());
				ctx.setErrorViewName( template.toString() );
				return false;

	     }
	     ctx.setCustomer(customer);
		return true;
	}
	
	protected boolean checkShoppingCart(OrderProcessContext ctx) throws Exception{
		HttpServletRequest request = ctx.getRequest();
		String shoppingCartCode  = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
		MerchantStore store = ctx.getStore();
		if(shoppingCartCode==null) {
			String cookie = ctx.getCookie();
			if(cookie==null) {//session expired and cookie null, nothing to do
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
				ctx.setErrorViewName(template.toString());
				return false;
			}
			String merchantCookie[] = cookie.split("_");
			String merchantStoreCode = merchantCookie[0];
			if(!merchantStoreCode.equals(store.getCode())) {
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
				ctx.setErrorViewName( template.toString());
				return false;
			}
			shoppingCartCode = merchantCookie[1];
		}
	
	    if(StringUtils.isBlank(shoppingCartCode)) {
			StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
			ctx.setErrorViewName( template.toString());
			return false;
	    }
	    ShoppingCart cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, ctx.getStore(), ctx.getCustomer());
	    ctx.setCart(cart);
	    return true;
	}
	@RequestMapping("/commitOrder.html")
	public String commitOrder(final String addressid, final String invoiceid, final String invoicesendid,
			@CookieValue(value = "cart", required = false) String cookie,
			@Valid @ModelAttribute(value = "order") ShopOrder order, BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		OrderProcessContext ctx = new OrderProcessContext();
		ctx.setRequest(request);
		ctx.setResponse(response);
		ctx.setLocale(locale);
		ctx.setModel(model);
		ctx.setCookie(cookie);
		ctx.setStore((MerchantStore) request.getAttribute(Constants.MERCHANT_STORE));
		ctx.setLanguage((Language) request.getAttribute("LANGUAGE"));

		try {
			if (!checkCustomer(ctx)) {
				return ctx.getErrorViewName();
			}

			if (!checkShoppingCart(ctx)) {
				return ctx.getErrorViewName();
			}

			splitSubOrders(order, ctx);

			ensureShopOrderCustomer(order, ctx);

			if (!checkDelivery(addressid, invoiceid, invoicesendid, order, ctx)) {
				return ctx.getErrorViewName();
			}

			ensureOrderSummary(order, request);

			orderFacade.validateOrder(order, bindingResult, new HashMap<String, String>(), ctx.getStore(),
					ctx.getLocale());

			if (bindingResult.hasErrors()) {
				LOGGER.info("found {} validation error while validating in customer registration ",
						bindingResult.getErrorCount());
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout)
						.append(".").append(ctx.getStore().getStoreTemplate());
				return template.toString();

			}

			Order modelOrder = this.commitOrder(order, request, locale);
			
			handleMemberPoints(order, ctx, modelOrder);

			customerService.update(ctx.getCustomer());
			// 更新session中的客户信息
			SessionUtil.setSessionAttribute(Constants.CUSTOMER, ctx.getCustomer(), request);
			// }
			if (modelOrder == null) {

				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon)
						.append(".").append(ctx.getStore().getStoreTemplate());
				return template.toString();
			}

		} catch (ServiceException se) {

			LOGGER.error("Error while creating an order ", se);

			String defaultMessage = messages.getMessage("message.error", locale);
			model.addAttribute("errorMessages", defaultMessage);

			if (se.getExceptionType() == ServiceException.EXCEPTION_VALIDATION) {
				if (!StringUtils.isBlank(se.getMessageCode())) {
					String messageLabel = messages.getMessage(se.getMessageCode(), locale, defaultMessage);
					model.addAttribute("errorMessages", messageLabel);
				}
			} else if (se.getExceptionType() == ServiceException.EXCEPTION_PAYMENT_DECLINED) {
				String paymentDeclinedMessage = messages.getMessage("message.payment.declined", locale);
				if (!StringUtils.isBlank(se.getMessageCode())) {
					String messageLabel = messages.getMessage(se.getMessageCode(), locale, paymentDeclinedMessage);
					model.addAttribute("errorMessages", messageLabel);
				} else {
					model.addAttribute("errorMessages", paymentDeclinedMessage);
				}
			}

			model.addAttribute("invoice", new CustomerInvoice());
			model.addAttribute("address", new CustomerAdress());

			StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".")
					.append(ctx.getStore().getStoreTemplate());
			return template.toString();

		} catch (Exception e) {
			LOGGER.error("Error while commiting order", e);
			throw e;

		}
		// for add new address
		model.addAttribute("address", new CustomerAdress());
		// model.addAttribute( "customerId", customer.getId() );
		// for add new invoice
		model.addAttribute("invoice", new CustomerInvoice());
		// redirect to completd
		return "redirect:/shop/order/confirmation.html";
	}


	private void handleMemberPoints(ShopOrder order, OrderProcessContext ctx, Order modelOrder)
			throws ServiceException {
		// 判断是否是首次下单
		MemberPoints memberPoints = new MemberPoints();
		int points = 0;
		try {
			points = Integer.parseInt(getPoint(Constants.RATIO_BUTILE));
		} catch (Exception e) {
			points = 1;
		}

		Integer temp_point = Integer.valueOf(order.getOrderTotalSummary().getTotal().intValue()) % points == 0
				? order.getOrderTotalSummary().getTotal().intValue() / Integer.valueOf(points)
				: order.getOrderTotalSummary().getTotal().intValue() / Integer.valueOf(points) + 1;
		memberPoints.setType(Constants.RATIO_BUTILE);
		memberPoints.setValue(String.valueOf(temp_point));
		// set left
		memberPoints.setLtfePoint((long) temp_point);
		memberPoints.setUpdateDate(new Date());
		memberPoints.setStatas(1);
		memberPoints.setCustomer(ctx.getCustomer());
		memberPoints.setOrderId(modelOrder.getId());
		memberPointsService.save(memberPoints);
		
		if (oraderProductService.getOraderByCount(ctx.getCustomer().getId()) == 1) {
			// 首次下单，赠送积分，更新用户信息和新增积分记录
			MemberPoints mb2 = new MemberPoints();
			try {
				points = Integer.parseInt(getPoint(Constants.FIRST_SCORE));
			} catch (Exception e) {
				points = 0;
			}

			// points+=temp_point;
			mb2.setType(Constants.FIRST_SCORE);
			mb2.setUpdateDate(new Date());
			mb2.setStatas(1);
			mb2.setValue(String.valueOf(points));
			// set left
			mb2.setLtfePoint((long) points);
			mb2.setCustomer(ctx.getCustomer());
			mb2.setOrderId(modelOrder.getId());
			memberPointsService.save(mb2);
		}

//		memberPoints.setCustomer(ctx.getCustomer());
	}


	private void ensureOrderSummary(ShopOrder order, HttpServletRequest request) throws Exception {
		OrderTotalSummary totalSummary = super.getSessionAttribute(Constants.ORDER_SUMMARY, request);
		if(totalSummary==null) {
			totalSummary = orderFacade.calculateOrderTotal(order);
			super.setSessionAttribute(Constants.ORDER_SUMMARY, totalSummary, request);
		}
		order.setOrderTotalSummary(totalSummary);
	}


	protected boolean checkDelivery(final String addressid, final String invoiceid, final String invoicesendid,
			ShopOrder order, OrderProcessContext ctx) {
		// check delivery
		if(addressid !=null && addressid !="" && invoiceid != null && invoiceid !="" && invoicesendid != null && invoicesendid !=""){
			order.setAddressid(Long.parseLong(addressid));
			order.setInvoiceid(Long.parseLong(invoiceid));
			order.setInvoicesendid(Long.parseLong(invoicesendid));
			return true;
		}else{
			LOGGER.error("No delivery  configured");
			ctx.getModel().addAttribute("errorMessages", "No delivery configured");
			StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(ctx.getStore().getStoreTemplate());
			ctx.setErrorViewName(template.toString());
			return false;
		}
	}


	private void ensureShopOrderCustomer(ShopOrder order, OrderProcessContext ctx) throws Exception {
		//add by cy customer redo
		PersistableCustomer persistableCustomer = order.getCustomer();
		//customer = customerFacade.getCustomerForLogin(customer.getPhone());
		if(null == persistableCustomer){					
			persistableCustomer = orderFacade.getPersistableCustomer(ctx.getCustomer(), ctx.getStore(), ctx.getLanguage());
			order.setCustomer(persistableCustomer);
			
		}
	}


	private void splitSubOrders(ShopOrder order, OrderProcessContext ctx) throws Exception {
		ShoppingCart cart = ctx.getCart();
		//modify by cy get seleected items			    
		Set<ShoppingCartItem> items = cart.getLineItems();
		
		// refresh product final price before add into order
		assert(items != null);
		List<Long> pids = new ArrayList<Long>();
		for(ShoppingCartItem item : items){
			pids.add(item.getProductId());
		}
		List<Object[]> brandDiscounts = brandDiscountService.getDiscountRateByProductIdList(pids);
		for(ShoppingCartItem item : items){
			Double discountRate = ProductPriceUtils.calcFinalListDiscount(ctx.getCustomer(), item.getProductId(), brandDiscounts);
			if (discountRate == null){
				// no discount, keep orignal price and subtotal
			}else{
				double finalPrice = discountRate * item.getProductPrice().getProductPriceAmount().doubleValue();
				item.setItemPrice(new BigDecimal(finalPrice));
				item.setSubTotal(new BigDecimal(finalPrice * item.getQuantity()));
			}
		}
		Set<ShoppingCartItem> cartItems = new HashSet<ShoppingCartItem>();								
		for (ShoppingCartItem item : items){
			if(item.isSelected() == true){
				cartItems.add(item);
			}
		}
		Set<SubOrder> subOrders = customerFacade.getSubOrders(cartItems, ctx.getStore(), ctx.getLanguage());
		order.setSubOrders(subOrders);
	}
	
	/**
	 * 商家首次下单
	 * @author 百图生物
	 * */
	private String getPoint(String type) {
		/*ServletContext application = request.getSession().getServletContext();
		Map<String, BasedataType> basedataMap = (Map<String, BasedataType>) application.getAttribute(Constants.BASEDATATYPE_MAP);
		BasedataType basedataType = basedataMap.get(type);
		String points = basedataType.getValue();*/
		
		return String.valueOf(BaseDataUtils.addCoustomerByOrader(type));
	}
	
	@RequestMapping(value={"/defautAddressInvoice.html"}, method=RequestMethod.POST)
	public @ResponseBody
	DefautAddressInvoice getDefaultAdress(final HttpServletRequest request, final HttpServletResponse response) throws Exception {


		DefautAddressInvoice defautAddressInvoice=new DefautAddressInvoice();
		Language language = (Language)request.getAttribute("LANGUAGE");		
		Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);
		
		if(customer!=null) {
			//reload customer again
			customer = customerFacade.getCustomerForLogin(customer.getPhone());
			 //get defaultaddress
			List<CustomerAdress> others = customerAddressFacade.getCustomerAddress(customer, language);
			if(null != others)  defautAddressInvoice.setOthersAddresss(others);
	    	
	     }
			return defautAddressInvoice;

	}
	
	@RequestMapping(value={"/defautInvoice.html"}, method=RequestMethod.POST)
	public @ResponseBody
	DefautAddressInvoice getDefaultInvoice(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		
		DefautAddressInvoice defautAddressInvoice=new DefautAddressInvoice();
		Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);
		
		if(customer!=null) {
			//reload customer again
			customer = customerFacade.getCustomerForLogin(customer.getPhone());
			 //get defaultaddress
			Set<CustomerInvoice> others = customer.getInvoices();
			if(null != others)  defautAddressInvoice.setOthersInvoices(others);
	    	
	     }
			return defautAddressInvoice;

	}
	
	@ModelAttribute("countries")
	protected List<Country> getCountries(final HttpServletRequest request){
	    
        Language language = (Language) request.getAttribute( "LANGUAGE" );
        
        try
        {
                        
            List<Country> countryList=countryService.getCountries( language );
            return countryList;
        }
        catch ( ServiceException e )
        {
            LOGGER.error( "Error while fetching country list ", e );

        }
        return Collections.emptyList();
    }

    //@ModelAttribute("zones")
    //public List<Zone> getZones(final HttpServletRequest request){
    //    return zoneService.list();
    //}
 
	@ModelAttribute("zoneList")
    public List<Zone> getZones(final HttpServletRequest request){
	    return zoneService.list();
	}

	/** *********************************** below are new order processing flow V2.02.2017 **/
	@RequestMapping("/commitOrderV2.html")
	public String commitOrderV2(final String addressid, final String invoiceid, final String invoicesendid,
			@CookieValue(value = "cart", required = false) String cookie,
			@Valid @ModelAttribute(value = "order") ShopOrder order, BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		OrderServiceV2Context orderContext = OrderUtils.getOrderContext(request, customerFacade, userService);
		Customer customer = orderContext.getCustomer();
		MerchantStore store = orderContext.getStore();
		// get shopping cart
		ShoppingCart cart = getShoppingCart(customer, store, cookie, request);

		// get delivery information
		Long addrId = DataTypeUitls.toLong(addressid);
		Long ivcId = DataTypeUitls.toLong(invoiceid);
		Long ivcAddrId = DataTypeUitls.toLong(invoicesendid);

		try {
			orderServiceV2.commitOrder(orderContext, cart, addrId, ivcId, ivcAddrId);
			shoppingCartService.removePurchasedItems(cart);
		} catch (ServiceExceptionV2 e) {
			e.printStackTrace();
			model.addAttribute("errorCode", e.getErrorCode());
			model.addAttribute("errorMessageKey", e.getMessageKey());
			model.addAttribute("errorMessageParams", e.getMessageParams());
			switch (e.getErrorCode()) {
			case OrderServiceV2Constants.CODE_NO_CUSTOMER:
				return returnToLogonPage(store);
			case OrderServiceV2Constants.CODE_NO_DELIVERY:
			case OrderServiceV2Constants.CODE_NO_INVOICE_ADDRESS:
				model.addAttribute("address", new CustomerAdress());
				model.addAttribute("invoice", new CustomerInvoice());
				model.addAttribute("errorMessages", messages.getServiceExceptionV2Message(e, OrderServiceV2Constants.ERROR_MSG_PREFIX, "提交订单失败。", locale));
				return returnToCheckout(store);
			default:
				// TODO 获得Message
				model.addAttribute("information", "您的账号尚未通过审核，不能购买商品");
				return returnToInformationPage(store);
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return returnToCheckout(store);
		}

		Customer authCustomer = customerFacade.getCustomerForLogin(customer.getPhone());
		Order createdOrder = orderContext.getOrder();
		// save order id in session
		super.setSessionAttribute(Constants.ORDER_ID, createdOrder.getId(), request);
		// set a unique token for confirmation
		super.setSessionAttribute(Constants.ORDER_ID_TOKEN, createdOrder.getId(), request);

		/** as new requirement, order related notified will be sent with new rules inside order service **/
		/*try {
			// send order confirmation email
			if (authCustomer.isRecieveEmail() == true) {
				emailTemplatesUtils.sendOrderEmail(authCustomer, createdOrder, locale, orderContext.getLanguage(),
						store, request.getContextPath());
			}
			// send order to store
			Set<SubOrder> subs = createdOrder.getSubOrders();
			for (SubOrder s : subs) {
				emailTemplatesUtils.sendOrderEmailtoStore(s, createdOrder, locale, orderContext.getLanguage(), store,
						request.getContextPath());
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while sending order email", e);
		}*/
		model.addAttribute("address", new CustomerAdress());
		model.addAttribute("invoice", new CustomerInvoice());
		return "redirect:/shop/order/confirmation.html";

	}


	private ShoppingCart getShoppingCart(Customer customer, MerchantStore store, String cookie, HttpServletRequest request) {
		if (customer == null || store == null){
			return null;
		}
		String shoppingCartCode  = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
		if(shoppingCartCode==null) {
			if(cookie==null) {//session expired and cookie null, nothing to do
				return null;
			}
			String merchantCookie[] = cookie.split("_");
			String merchantStoreCode = merchantCookie[0];
			if(!merchantStoreCode.equals(store.getCode())) {
				return null;
			}
			shoppingCartCode = merchantCookie[1];
		}
	
	    if(StringUtils.isBlank(shoppingCartCode)) {
			return null;
	    }
	    ShoppingCart cart = null;
		try {
			cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store, customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return cart;
	}


	private String returnToInformationPage(MerchantStore store) {
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.information).append(".").append(store.getStoreTemplate());
		return template.toString();
	}


	private String returnToLogonPage(MerchantStore store) {
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon).append(".").append(store.getStoreTemplate());
		return template.toString();
	}


	private String returnToCheckout(MerchantStore store) {
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout)
				.append(".").append(store.getStoreTemplate());
		return template.toString();
	}
	
	@RequestMapping(value = "action/actionEvent.html", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String noParamAction(Long soid, String actionName, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			if (GlobalConstants.OrderAction_ConfirmPay.equals(actionName)){
				updateMemberPointAfterPaiConfirmed(order);
			}
			ajaxResp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		}catch (ServiceExceptionV2 e){
			String message = messageUtils.getServiceExceptionV2Message(e, OrderServiceV2Constants.ERROR_MSG_PREFIX, "", orderContext.getLocale());
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
}
