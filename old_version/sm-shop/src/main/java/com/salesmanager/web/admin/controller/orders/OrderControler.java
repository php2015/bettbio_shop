package com.salesmanager.web.admin.controller.orders;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductDownload;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductDownloadService;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.payments.service.TransactionService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.order.UpdateByTotalMoney;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;

/**
 * Manage order details
 * @author Carl Samson
 *
 */
@Controller
public class OrderControler {
	
private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	PricingService pricingService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	EmailService emailService;
	@Autowired
	OrderProductDownloadService orderProdctDownloadService;
	
	private final static String ORDER_STATUS_TMPL = "email_template_order_status.ftl";
	

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/editOrder.html", method=RequestMethod.GET)
	public String displayOrderEdit(@RequestParam("id") long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayOrder(orderId,model,request,response);

	}

	@PreAuthorize("hasRole('ORDER')")
	private String displayOrder(Long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model,request);
		   
		com.salesmanager.web.admin.entity.orders.Order order = new com.salesmanager.web.admin.entity.orders.Order();
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Country> countries = countryService.getCountries(language);
		if(orderId!=null && orderId!=0) {		//edit mode		
			
			
			Order dbOrder = orderService.getById(orderId);

			if(dbOrder==null) {
				return "redirect:/admin/orders/list.html";
			}
			
			/**
			if(dbOrder.getMerchant().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/orders/list.html";
			}
			*/
			
			order.setId( orderId );
		
			if( dbOrder.getDatePurchased() !=null ){
				order.setDatePurchased(DateUtil.formatDate(dbOrder.getDatePurchased()));
			}
			
			Long customerId = dbOrder.getCustomerId();
			
			if(customerId!=null && customerId>0) {
			
				try {
					
					Customer customer = customerService.getById(customerId);
					if(customer!=null) {
						model.addAttribute("customer",customer);
					}
					
					
				} catch(Exception e) {
					LOGGER.error("Error while getting customer for customerId " + customerId, e);
				}
			
			}
			
			order.setOrder( dbOrder );
			order.setBillingInvoice(dbOrder.getBillingInvoice());
			order.setBillingAddress(dbOrder.getBillingAddress());
			order.setBillingCity(dbOrder.getBillingCity());
			order.setBillingCompany(dbOrder.getBillingCompany());
			order.setBillingCountry(dbOrder.getBillingCountry());
			order.setBillingPostalCode(dbOrder.getBillingPostalCode());
			order.setBillingTelephone(dbOrder.getBillingTelephone());
			order.setBillingUserName(dbOrder.getBillingUserName());
			order.setBillingZone(dbOrder.getBillingZone());
			order.setIvoiceAddress(dbOrder.getIvoiceAddress());
			order.setIvoiceCity(dbOrder.getIvoiceCity());
			order.setIvoiceCompany(dbOrder.getIvoiceCompany());
			order.setIvoiceCountry(dbOrder.getIvoiceCountry());
			order.setIvoicePostalCode(dbOrder.getIvoicePostalCode());
			order.setIvoiceTelephone(dbOrder.getIvoiceTelephone());
			order.setIvoiceUserName(dbOrder.getIvoiceUserName());
			order.setIvoiceZone(dbOrder.getIvoiceZone());

			//get capturable
			if(dbOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
				Transaction capturableTransaction = transactionService.getCapturableTransaction(dbOrder);
				if(capturableTransaction!=null) {
					model.addAttribute("capturableTransaction",capturableTransaction);
				}
			}
			
			
			//get refundable
			if(dbOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
				Transaction refundableTransaction = transactionService.getRefundableTransaction(dbOrder);
				if(refundableTransaction!=null) {
						model.addAttribute("capturableTransaction",null);//remove capturable
						model.addAttribute("refundableTransaction",refundableTransaction);
				}
			}

			
			List<OrderProductDownload> orderProductDownloads = orderProdctDownloadService.getByOrderId(order.getId());
			if(CollectionUtils.isNotEmpty(orderProductDownloads)) {
				model.addAttribute("downloads",orderProductDownloads);
			}
			
		}	
		
		model.addAttribute("countries", countries);
		model.addAttribute("order",order);
		return  ControllerConstants.Tiles.Order.ordersEdit;
	}
	

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/save.html", method=RequestMethod.POST)
	public String saveOrder(@Valid @ModelAttribute("order") com.salesmanager.web.admin.entity.orders.Order entityOrder, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		String email_regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern pattern = Pattern.compile(email_regEx);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Country> countries = countryService.getCountries(language);
		model.addAttribute("countries", countries);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//set the id if fails
		entityOrder.setId(entityOrder.getOrder().getId());
		
		model.addAttribute("order", entityOrder);
		
		Set<OrderProduct> orderProducts = new HashSet<OrderProduct>();
		Set<OrderTotal> orderTotal = new HashSet<OrderTotal>();
		Set<OrderStatusHistory> orderHistory = new HashSet<OrderStatusHistory>();
		
		Date date = new Date();
		if(!StringUtils.isBlank(entityOrder.getDatePurchased() ) ){
			try {
				date = DateUtil.getDate(entityOrder.getDatePurchased());
			} catch (Exception e) {
				ObjectError error = new ObjectError("datePurchased",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
			
		} else{
			date = null;
		}
		 

		if(!StringUtils.isBlank(entityOrder.getOrder().getCustomerEmailAddress() ) ){
			 java.util.regex.Matcher matcher = pattern.matcher(entityOrder.getOrder().getCustomerEmailAddress());
			 
			 if(!matcher.find()) {
				ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("Email.order.customerEmailAddress", locale));
				result.addError(error);
			 }
		}else{
			ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("NotEmpty.order.customerEmailAddress", locale));
			result.addError(error);
		}

		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBillingUserName() ) ){
			 ObjectError error = new ObjectError("billingFirstName", messages.getMessage("NotEmpty.order.billingFirstName", locale));
			 result.addError(error);
		}
		
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBillingAddress() ) ){
			 ObjectError error = new ObjectError("billingAddress", messages.getMessage("NotEmpty.order.billingStreetAddress", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBillingCity() ) ){
			 ObjectError error = new ObjectError("billingCity",messages.getMessage("NotEmpty.order.billingCity", locale));
			 result.addError(error);
		}
		 
				 
		if( StringUtils.isBlank(entityOrder.getOrder().getBillingPostalCode() ) ){
			 ObjectError error = new ObjectError("billingPostalCode", messages.getMessage("NotEmpty.order.billingPostCode", locale));
			 result.addError(error);
		}
		
		com.salesmanager.core.business.order.model.Order newOrder = orderService.getById(entityOrder.getOrder().getId() );
		
		
		//get capturable
		if(newOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
			Transaction capturableTransaction = transactionService.getCapturableTransaction(newOrder);
			if(capturableTransaction!=null) {
				model.addAttribute("capturableTransaction",capturableTransaction);
			}
		}
		
		
		//get refundable
		if(newOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
			Transaction refundableTransaction = transactionService.getRefundableTransaction(newOrder);
			if(refundableTransaction!=null) {
					model.addAttribute("capturableTransaction",null);//remove capturable
					model.addAttribute("refundableTransaction",refundableTransaction);
			}
		}
	
	
		if (result.hasErrors()) {
			//  somehow we lose data, so reset Order detail info.
			//entityOrder.getOrder().setOrderProducts( orderProducts);
			//entityOrder.getOrder().setOrderTotal(orderTotal);
			entityOrder.getOrder().setOrderHistory(orderHistory);
			
			return ControllerConstants.Tiles.Order.ordersEdit;
		/*	"admin-orders-edit";  */
		}
		
		OrderStatusHistory orderStatusHistory = new OrderStatusHistory();		



		
		Country deliveryCountry = countryService.getByCode( entityOrder.getOrder().getIvoiceCountry().getIsoCode()); 
		Country billingCountry  = countryService.getByCode( entityOrder.getOrder().getBillingCountry().getIsoCode()) ;
		Zone billingZone = null;
		Zone deliveryZone = null;
		if(entityOrder.getOrder().getBillingZone()!=null) {
			billingZone = zoneService.getByCode(entityOrder.getOrder().getBillingZone().getCode());
		}
		
		if(entityOrder.getOrder().getIvoiceCountry() !=null) {
			deliveryZone = zoneService.getByCode(entityOrder.getOrder().getIvoiceZone().getCode());
		}

		newOrder.setCustomerEmailAddress(entityOrder.getOrder().getCustomerEmailAddress() );
		newOrder.setStatus(entityOrder.getOrder().getStatus() );		
		
		newOrder.setDatePurchased(date);
		newOrder.setLastModified( new Date() );
		
		if(!StringUtils.isBlank(entityOrder.getOrderHistoryComment() ) ) {
			orderStatusHistory.setComments( entityOrder.getOrderHistoryComment() );
			orderStatusHistory.setCustomerNotified(1);
			orderStatusHistory.setStatus(entityOrder.getOrder().getStatus());
			orderStatusHistory.setDateAdded(new Date() );
			orderStatusHistory.setOrder(newOrder);
			newOrder.getOrderHistory().add( orderStatusHistory );
			entityOrder.setOrderHistoryComment( "" );
		}		
		
		newOrder.setIvoiceAddress( entityOrder.getOrder().getIvoiceAddress() );
		newOrder.setBillingAddress( entityOrder.getOrder().getBillingAddress() );
		
		newOrder.setIvoiceCountry(deliveryCountry );
		newOrder.setBillingCountry(billingCountry );	
		
		if(billingZone!=null) {
			newOrder.setBillingZone(billingZone);
		}
		
		if(deliveryZone!=null) {
			newOrder.setIvoiceZone(deliveryZone);
		}
		
		orderService.saveOrUpdate(newOrder);
		entityOrder.setOrder(newOrder);
		entityOrder.setBillingAddress(newOrder.getBillingAddress());
		entityOrder.setIvoiceAddress(newOrder.getIvoiceAddress());
		model.addAttribute("order", entityOrder);
		
		Long customerId = newOrder.getCustomerId();
		
		if(customerId!=null && customerId>0) {
		
			try {
				
				Customer customer = customerService.getById(customerId);
				if(customer!=null) {
					model.addAttribute("customer",customer);
				}
				
				
			} catch(Exception e) {
				LOGGER.error("Error while getting customer for customerId " + customerId, e);
			}
		
		}

		List<OrderProductDownload> orderProductDownloads = orderProdctDownloadService.getByOrderId(newOrder.getId());
		if(CollectionUtils.isNotEmpty(orderProductDownloads)) {
			model.addAttribute("downloads",orderProductDownloads);
		}
		
		
		/** 
		 * send email if admin posted orderHistoryComment
		 * 
		 * **/
		
		if(StringUtils.isBlank(entityOrder.getOrderHistoryComment())) {
		
			try {
				
				Customer customer = customerService.getById(newOrder.getCustomerId());
				Language lang = store.getDefaultLanguage();
				if(customer!=null) {
					lang = customer.getDefaultLanguage();
				}
				
				Locale customerLocale = LocaleUtils.getLocale(lang);

				StringBuilder customerName = new StringBuilder();
				customerName.append(newOrder.getBillingUserName());
				
				
				Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, customerLocale);
				templateTokens.put(EmailConstants.EMAIL_CUSTOMER_NAME, customerName.toString());
				templateTokens.put(EmailConstants.EMAIL_TEXT_ORDER_NUMBER, messages.getMessage("email.order.confirmation", new String[]{String.valueOf(newOrder.getId())}, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_ORDERED, messages.getMessage("email.order.ordered", new String[]{entityOrder.getDatePurchased()}, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_TEXT_STATUS_COMMENTS, messages.getMessage("email.order.comments", new String[]{entityOrder.getOrderHistoryComment()}, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_UPDATED, messages.getMessage("email.order.updated", new String[]{DateUtils.formatDate(new Date())}, customerLocale));

				
				Email email = new Email();
				email.setFrom(store.getStorename());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.order.status.title",new String[]{String.valueOf(newOrder.getId())},customerLocale));
				email.setTo(entityOrder.getOrder().getCustomerEmailAddress());
				email.setTemplateName(ORDER_STATUS_TMPL);
				email.setTemplateTokens(templateTokens);
	
				emailService.sendHtmlEmail(store, email);
			
			} catch (Exception e) {
				LOGGER.error("Cannot send email to customer",e);
			}
			
		}
		
		model.addAttribute("success","success");

		
		return  ControllerConstants.Tiles.Order.ordersEdit;
	    /*	"admin-orders-edit";  */
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
	
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("order", "order");
		activeMenus.put("order-list", "order-list");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");

		model.addAttribute("activeMenus",activeMenus);
		
		Menu currentMenu = (Menu)menus.get("order");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	/**
	 *@author 百图生物
	 *修改订单总金额和修改积分 
	 */
	@RequestMapping(value="/admin/orders/updateMoney.html", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody String updateByOrader(HttpServletRequest request,HttpServletResponse response){
		AjaxResponse  ap = new AjaxResponse();
		//String userName = request.getRemoteUser();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		UpdateByTotalMoney  updateByTotalMoney = new UpdateByTotalMoney();
		try {
			String orderproductid = request.getParameter("orderproductid");
			String orderprice = request.getParameter("orderprice");
			String nowtotalprice = request.getParameter("nowtotalprice");
			//得到价格
			List<Object[]> priceList = this.orderService.queryByOrderProduct(Long.parseLong(orderproductid));
			for(Object o : priceList)
			{
				Object[] oj = (Object[]) o;
				updateByTotalMoney.setSuid(Long.parseLong(oj[0].toString()));
				updateByTotalMoney.setPrice(Double.parseDouble(oj[1].toString()));
				updateByTotalMoney.setTotalprice(Double.parseDouble(oj[2].toString()));
				
			}
			//处理用户
			List<Object[]> objList = this.orderService.queryByStoreId(updateByTotalMoney.getSuid());
			for(Object obj : objList){
				Object [] ob = (Object[]) obj;
				updateByTotalMoney.setOrderid(Long.parseLong(ob[0].toString()));
				updateByTotalMoney.setSubMoney(Double.parseDouble(ob[1].toString()));
				updateByTotalMoney.setTotalMoney(Double.parseDouble(ob[2].toString()));
				updateByTotalMoney.setMid(Long.parseLong(ob[3].toString()));
			}
			//判定商家真伪
			if(updateByTotalMoney.getMid()==store.getId())
			{
					 ap.setStatus(2);
			}
			//修改价格
			int num =(int)(updateByTotalMoney.getTotalprice()/updateByTotalMoney.getPrice());
			if(Double.parseDouble(orderprice)==updateByTotalMoney.getPrice() && Double.parseDouble(nowtotalprice)==updateByTotalMoney.getTotalprice())
			{
				ap.setStatus(0);
			}
			else if(Double.parseDouble(orderprice)!=updateByTotalMoney.getPrice() && Double.parseDouble(nowtotalprice)==updateByTotalMoney.getTotalprice())
			{
				double total = Double.parseDouble(orderprice)*num;
				if(this.orderService.updateByOrderProduct(Long.parseLong(orderproductid), Double.parseDouble(orderprice), total))
				{
						//修改商品订单价格
						double orderTotalMoney = 0;
					    double suborderprice = 0;
						if(Double.parseDouble(orderprice)>updateByTotalMoney.getPrice())
						{
							suborderprice = total+updateByTotalMoney.getSubMoney()-updateByTotalMoney.getTotalprice();
							orderTotalMoney = updateByTotalMoney.getTotalMoney()+suborderprice-updateByTotalMoney.getSubMoney();
						}
						else
						{
							suborderprice = updateByTotalMoney.getSubMoney()-(updateByTotalMoney.getTotalprice()-total);
							orderTotalMoney = updateByTotalMoney.getTotalMoney()-(updateByTotalMoney.getSubMoney()-suborderprice);
						}
						if(this.orderService.updateBySubOrder(updateByTotalMoney.getSuid(), updateByTotalMoney.getOrderid(),suborderprice,orderTotalMoney))
						{
							ap.setStatus(0);
							int temp_point=(int) (orderTotalMoney%10==0?orderTotalMoney/10:orderTotalMoney/10+1);
							//修改积分
							if(this.orderService.updateByMenberPoint(updateByTotalMoney.getOrderid(),temp_point ))
							{
								ap.setStatus(0);
							}
							else
							{
								ap.setStatus(1);
							}
						}
						else
						{
							ap.setStatus(1);
						}	
				}
				else
				{
					ap.setStatus(1);
				}
			}
			else if(Double.parseDouble(orderprice)==updateByTotalMoney.getPrice() && Double.parseDouble(nowtotalprice)!=updateByTotalMoney.getTotalprice())
			{
				double price = Double.parseDouble(nowtotalprice)/num;
				double total=price*num;
				if(this.orderService.updateByOrderProduct(Long.parseLong(orderproductid), price, Double.parseDouble(nowtotalprice)))
				{
					if(this.orderService.updateByOrderProduct(Long.parseLong(orderproductid),price,Double.parseDouble(nowtotalprice)))
					{
							//修改商品订单价格
							double orderTotalMoney = 0;
						    double suborderprice = 0;
						    if(Double.parseDouble(orderprice)>updateByTotalMoney.getPrice())
							{
								suborderprice = total+updateByTotalMoney.getSubMoney()-updateByTotalMoney.getTotalprice();
								orderTotalMoney = updateByTotalMoney.getTotalMoney()+suborderprice-updateByTotalMoney.getSubMoney();
							}
							else
							{
								suborderprice = updateByTotalMoney.getSubMoney()-(updateByTotalMoney.getTotalprice()-total);
								orderTotalMoney = updateByTotalMoney.getTotalMoney()-(updateByTotalMoney.getSubMoney()-suborderprice);
							}
							if(this.orderService.updateBySubOrder(updateByTotalMoney.getSuid(), updateByTotalMoney.getOrderid(),suborderprice,orderTotalMoney))
							{
								ap.setStatus(0);
								int temp_point=(int) (orderTotalMoney%10==0?orderTotalMoney/10:orderTotalMoney/10+1);
								//修改积分
								if(this.orderService.updateByMenberPoint(updateByTotalMoney.getOrderid(),temp_point ))
								{
									ap.setStatus(0);
								}
								else
								{
									ap.setStatus(1);
								}
							}
							else
							{
								ap.setStatus(1);
							}	
					}
					else
					{
						ap.setStatus(1);
					}	
				}
				else
				{
					ap.setStatus(1);
				}
			}
			else if(Double.parseDouble(orderprice)!=updateByTotalMoney.getPrice() && Double.parseDouble(nowtotalprice)!=updateByTotalMoney.getTotalprice())
			{
				if(Double.parseDouble(orderprice)*num !=  Double.parseDouble(nowtotalprice)){
					ap.setStatus(1);
				}
				else
				{
					double price = Double.parseDouble(nowtotalprice)/num;
					double total=price*num;
					if(this.orderService.updateByOrderProduct(Long.parseLong(orderproductid), price, Double.parseDouble(nowtotalprice)))
					{
						if(this.orderService.updateByOrderProduct(Long.parseLong(orderproductid),price,Double.parseDouble(nowtotalprice)))
						{
								//修改商品订单价格
								double orderTotalMoney = 0;
							    double suborderprice = 0;
							    if(Double.parseDouble(orderprice)>updateByTotalMoney.getPrice())
								{
									suborderprice = total+updateByTotalMoney.getSubMoney()-updateByTotalMoney.getTotalprice();
									orderTotalMoney = updateByTotalMoney.getTotalMoney()+suborderprice-updateByTotalMoney.getSubMoney();
								}
								else
								{
									suborderprice = updateByTotalMoney.getSubMoney()-(updateByTotalMoney.getTotalprice()-total);
									orderTotalMoney = updateByTotalMoney.getTotalMoney()-(updateByTotalMoney.getSubMoney()-suborderprice);
								}
								if(this.orderService.updateBySubOrder(updateByTotalMoney.getSuid(), updateByTotalMoney.getOrderid(),suborderprice,orderTotalMoney))
								{
									ap.setStatus(0);
									int temp_point=(int) (orderTotalMoney%10==0?orderTotalMoney/10:orderTotalMoney/10+1);
									//修改积分
									if(this.orderService.updateByMenberPoint(updateByTotalMoney.getOrderid(),temp_point ))
									{
										ap.setStatus(0);
									}
									else
									{
										ap.setStatus(1);
									}
								}
								else
								{
									ap.setStatus(1);
								}	
						}
						else
						{
							ap.setStatus(1);
						}	
					}
					else
					{
						ap.setStatus(1);
					}
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			ap.setStatus(1);
			e.printStackTrace();
		}
		return ap.toJSONString();
   }
}
