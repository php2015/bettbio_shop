package com.salesmanager.web.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.system.service.SepcialConfigureService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.WorkModeConfiguration;
import com.salesmanager.web.admin.entity.userpassword.UserReset;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.shop.ContactForm;

import edu.emory.mathcs.backport.java.util.Arrays;


@Component
public class EmailTemplatesUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplatesUtils.class);
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WorkModeConfiguration workMode;
	
	@Autowired
	private SepcialConfigureService sepcialConfigureService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final static String LINE_BREAK = "<br/>";
	private final static String TABLE = "<table width=\"100%\">";
	private final static String CLOSING_TABLE = "</table>";
	private final static String TR = "<tr>";
	private final static String TR_BORDER = "<tr class=\"border\">";
	private final static String CLOSING_TR = "</tr>";
	private final static String TD = "<td valign=\"top\">";
	private final static String CLOSING_TD = "</td>";
	
	/**
	 * 
	 * @param nickName
	 * @param email
	 @param order
	 * @param customerLocale
	 * @param language
	 * @param merchantStore
	 * @param contextPath
	 */
	
	@Async
	public void sendOrderEmail(Customer customer, Order order, Locale customerLocale, Language language, MerchantStore merchantStore, String contextPath) {
			   /** issue with putting that elsewhere **/ 
		       LOGGER.info( "Sending welcome email to customer" );
		       try {
		    	   
		    	   Map<String,Zone> zones = zoneService.getZones(language);
		    	   
		    	   Map<String,Country> countries = countryService.getCountriesMap(language);
		    	   
		    	   //format Billing address
		    	   StringBuilder billing = new StringBuilder();
		    	   if(StringUtils.isNotBlank(order.getBillingCompany())) {
		    		   billing.append(order.getBillingCompany()).append(LINE_BREAK);
		    	   }		    	   
		    	   if(StringUtils.isNotBlank(order.getBillingUserName())) {
		    		   billing.append(order.getBillingUserName()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingTelephone())) {
		    		   billing.append(order.getBillingTelephone()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingAddress())) {
		    		   billing.append(order.getBillingAddress()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingCity())) {
		    		   billing.append(order.getBillingCity()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingCity())) {
		    		   billing.append(order.getBillingCity()).append(LINE_BREAK);
		    	   }
		    	   if(order.getBillingZone()!=null) {
		    		   Zone zone = zones.get(order.getBillingZone().getCode());
		    		   if(zone!=null) {
		    			   billing.append(zone.getName());
		    		   } else {
		    			   billing.append(zone.getCode());
		    		   }
		    		   billing.append(LINE_BREAK);
		    	   } 
		    	   Country country = countries.get(order.getBillingCountry().getIsoCode());
		    	   if(country!=null) {
		    		   billing.append(country.getName()).append(" ");
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingPostalCode())) {
		    		   billing.append(order.getBillingPostalCode()).append(LINE_BREAK);
		    	   }  	   
		    	   
		    	   //format shipping address
		    	   StringBuilder shipping = new StringBuilder();;
		    	   if(order.getBillingInvoice()!=null ) {
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getCompany())) {
			    		   shipping.append(order.getBillingInvoice().getCompany()).append(LINE_BREAK);
			    	   } 
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getCompanyAddress())) {
			    		   shipping.append(order.getBillingInvoice().getCompanyAddress()).append(LINE_BREAK);
			    	   } 
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getCompanyTelephone())) {
			    		   shipping.append(order.getBillingInvoice().getCompanyTelephone()).append(LINE_BREAK);
			    	   }
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getBankName())) {
			    		   shipping.append(order.getBillingInvoice().getBankName()).append(LINE_BREAK);
			    	   } 
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getBankAccount())) {
			    		   shipping.append(order.getBillingInvoice().getBankAccount()).append(LINE_BREAK);
			    	   }
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getTaxpayerNumber())) {
			    		   shipping.append(order.getBillingInvoice().getTaxpayerNumber()).append(LINE_BREAK);
			    	   }
		    	   }
		    	   
		    	 //format delivery address
		    	   StringBuilder invoice = new StringBuilder();
		    	   if(StringUtils.isNotBlank(order.getIvoiceCompany())) {
		    		   invoice.append(order.getIvoiceCompany()).append(LINE_BREAK);
		    	   }		    	   
		    	   if(StringUtils.isNotBlank(order.getIvoiceUserName())) {
		    		   invoice.append(order.getIvoiceUserName()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceTelephone())) {
		    		   invoice.append(order.getIvoiceTelephone()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceAddress())) {
		    		   invoice.append(order.getIvoiceAddress()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceCity())) {
		    		   invoice.append(order.getIvoiceCity()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceCity())) {
		    		   invoice.append(order.getIvoiceCity()).append(LINE_BREAK);
		    	   }
		    	   if(order.getIvoiceZone()!=null) {
		    		   Zone zone = zones.get(order.getIvoiceZone().getCode());
		    		   if(zone!=null) {
		    			   invoice.append(zone.getName());
		    		   } else {
		    			   invoice.append(zone.getCode());
		    		   }
		    		   invoice.append(LINE_BREAK);
		    	   } 
		    	   country = countries.get(order.getIvoiceCountry().getIsoCode());
		    	   if(country!=null) {
		    		   invoice.append(country.getName()).append(" ");
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoicePostalCode())) {
		    		   invoice.append(order.getIvoicePostalCode()).append(LINE_BREAK);
		    	   }  	   
		    	   
		    	   //format order
		    	  String storeUri = FilePathUtils.buildStoreUrisELF(merchantStore, contextPath);
		    	   StringBuilder orderTable = new StringBuilder();
		    	   
		    	   orderTable.append(TABLE);
		    	   for(SubOrder subOrder: order.getSubOrders()){
		    		   orderTable.append(TR);
		    		   orderTable.append("<td valign=\"top\" colspan=\"3\">").append(messages.getMessage("menu.store", customerLocale)).append(":").append(subOrder.getStoreName()).append(CLOSING_TD);
		    		   orderTable.append(TD).append("&nbsp;").append(messages.getMessage("label.subtotal", customerLocale)).append(":").append(pricingService.getDisplayAmount(subOrder.getTotal(), merchantStore)).append(CLOSING_TD);
		    		   orderTable.append(CLOSING_TR);
			    	   for(OrderProduct product : subOrder.getOrderProducts()) {
			    		   Product productModel = productService.getByCode(product.getSku(), language);
			    		   orderTable.append(TR);
			    		   	   //images are ugly
			    		       orderTable.append(TD);
			    		       String productImage = "";
				    		   if(productModel!=null && productModel.getProductImage()!=null) {
				    			   productImage = new StringBuilder().append(storeUri).append(ImageFilePathUtils.buildProductImageFilePath(merchantStore, productModel, productModel.getProductImage().getProductImage())).toString();
				    		   } else {
				    			   productImage = new StringBuilder().append(storeUri).append("/resources/img/pimg.jpg").toString();
				    		   }
				    		   String imgSrc = new StringBuilder().append("<img src=\"").append(productImage).append("\" width=\"100\">").toString();
				    		   orderTable.append(imgSrc);
				    		   
				    		   orderTable.append(CLOSING_TD);
				    		   orderTable.append(TD).append(product.getProductName()).append(CLOSING_TD);
			    		   	   orderTable.append(TD).append(messages.getMessage("label.quantity", customerLocale)).append(": ").append(product.getProductQuantity()).append(CLOSING_TD);
		    		   		   orderTable.append(TD).append(pricingService.getDisplayAmount(product.getOneTimeCharge(), merchantStore)).append(CLOSING_TD);
	    		   		   orderTable.append(CLOSING_TR);
			    	   }
		    	   }
		    	   

		    	   //order totals
		    	   /**
		    	   for(OrderTotal total : order.getOrderTotal()) {*/
		    	   orderTable.append(TR);
		    	   orderTable.append("<td valign=\"top\" colspan=\"3\">");
		    	   orderTable.append(CLOSING_TD);
	    		   orderTable.append(TD);
	//		    	orderTable.append("<strong>");
		   			/**if(total.getModule().equals("tax")) {
		   				orderTable.append(total.getText()).append(": ");

		   			} else {
		   				//if(total.getModule().equals("total") || total.getModule().equals("subtotal")) {
		   				//}
		   				orderTable.append(messages.getMessage(total.getOrderTotalCode(), customerLocale)).append(": ");
		   				//if(total.getModule().equals("total") || total.getModule().equals("subtotal")) {
		   					
		   				//}
		   			}*/
	//		    	orderTable.append("</strong>");
		   			orderTable.append("<strong>");
		   			orderTable.append(messages.getMessage("order.total.total", customerLocale)).append(": ").append(pricingService.getDisplayAmount(order.getTotal(), merchantStore));
	   				orderTable.append("</strong>");
	   				orderTable.append(CLOSING_TD);
	   				orderTable.append(CLOSING_TR);
		    	  // }
		    	   orderTable.append(CLOSING_TABLE);

		           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
		           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());
		           //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, order.getBilling().getLastName());
		           
		           String[] params = {String.valueOf(order.getId())};
		           String[] dt = {DateUtil.formatDate(order.getDatePurchased(), DateUtil.format_1)};
		           templateTokens.put(EmailConstants.EMAIL_ORDER_NUMBER, messages.getMessage("email.order.confirmation", params, customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_ORDER_DATE, messages.getMessage("email.order.ordered", dt, customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_ORDER_THANKS, messages.getMessage("email.order.thanks",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_SHIPPING, shipping.toString());
		           
		           templateTokens.put(EmailConstants.ADDRESS_BILLING, billing.toString());
		           
		           templateTokens.put(EmailConstants.ADDRESS_DELIVERY, invoice.toString());
		           
		           templateTokens.put(EmailConstants.ORDER_PRODUCTS_DETAILS, orderTable.toString());
		           templateTokens.put(EmailConstants.EMAIL_ORDER_DETAILS_TITLE, messages.getMessage("label.order.details",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_BILLING_TITLE, messages.getMessage("label.customer.billinginformation",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_DELIVERY_TITLE, messages.getMessage("label.customer.shippinginformation",customerLocale));
		           templateTokens.put(EmailConstants.PAYMENT_METHOD_TITLE, messages.getMessage("label.order.paymentmode",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_BILLING_INVOICE, messages.getMessage("label.customer.invoiceaddress",customerLocale));
		           templateTokens.put(EmailConstants.PAYMENT_METHOD_DETAILS, messages.getMessage(new StringBuilder().append("payment.type.").append(order.getPaymentType().name()).toString(),customerLocale,order.getPaymentType().name()));
		           
		           
		           
		           
		           
			       String status = messages.getMessage("label.order." + order.getStatus().name(), customerLocale, order.getStatus().name());
			       String[] statusMessage = {DateUtil.formatDate(order.getDatePurchased(), DateUtil.format_1),status};
		           templateTokens.put(EmailConstants.ORDER_STATUS, messages.getMessage("email.order.status", statusMessage, customerLocale));
		           

		           String[] title = {merchantStore.getStorename(), String.valueOf(order.getId())};
		           Email email = new Email();
		           email.setFrom(merchantStore.getStorename());
		           email.setFromEmail(merchantStore.getStoreEmailAddress());
		           email.setSubject(messages.getMessage("email.order.title", title, customerLocale));
		           email.setTo(customer.getEmailAddress());
		           
		           email.setTemplateName(EmailConstants.EMAIL_ORDER_TPL);
		           email.setTemplateTokens(templateTokens);

		           LOGGER.debug( "Sending email to {} for order id {} ",customer.getEmailAddress(), order.getId() );
		           emailService.sendHtmlEmail(email);

		       } catch (Exception e) {
		           LOGGER.error("Error occured while sending order confirmation email ",e);
		       }
			
		}
	
	/**
	 * 
	 * @param nickName
	 * @param email
	 @param order
	 * @param customerLocale
	 * @param language
	 * @param merchantStore
	 * @param contextPath
	 */
	
	@Async
	public void sendOrderEmailtoStore(SubOrder subOrder, Order order, Locale customerLocale, Language language, MerchantStore merchantStore, String contextPath) {
			   /** issue with putting that elsewhere **/ 
		boolean sendOption = sepcialConfigureService
				.getBooleanCfg(SepcialConfigureService.KEY_SEND_EMAIL_TO_MERCHANT_WHEN_SUBMIT_ORDER);
		if (!sendOption){
			LOGGER.info( "Don't send email to merchant because emailOp1 is false");
			return;
		}
		       LOGGER.info( "Sending welcome email to customer" );
		       try {
		    	   
		    	   Map<String,Zone> zones = zoneService.getZones(language);
		    	   
		    	   Map<String,Country> countries = countryService.getCountriesMap(language);
		    	   
		    	   //format Billing address
		    	   StringBuilder billing = new StringBuilder();
		    	   if(StringUtils.isNotBlank(order.getBillingCompany())) {
		    		   billing.append(order.getBillingCompany()).append(LINE_BREAK);
		    	   }		    	   
		    	   if(StringUtils.isNotBlank(order.getBillingUserName())) {
		    		   billing.append(order.getBillingUserName()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingTelephone())) {
		    		   billing.append(order.getBillingTelephone()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingAddress())) {
		    		   billing.append(order.getBillingAddress()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingCity())) {
		    		   billing.append(order.getBillingCity()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingCity())) {
		    		   billing.append(order.getBillingCity()).append(LINE_BREAK);
		    	   }
		    	   if(order.getBillingZone()!=null) {
		    		   Zone zone = zones.get(order.getBillingZone().getCode());
		    		   if(zone!=null) {
		    			   billing.append(zone.getName());
		    		   } else {
		    			   billing.append(zone.getCode());
		    		   }
		    		   billing.append(LINE_BREAK);
		    	   } 
		    	   Country country = countries.get(order.getBillingCountry().getIsoCode());
		    	   if(country!=null) {
		    		   billing.append(country.getName()).append(" ");
		    	   }
		    	   if(StringUtils.isNotBlank(order.getBillingPostalCode())) {
		    		   billing.append(order.getBillingPostalCode()).append(LINE_BREAK);
		    	   }  	   
		    	   
		    	   //format shipping address
		    	   StringBuilder shipping = null;
		    	   if(order.getBillingInvoice()!=null ) {
		    		   shipping = new StringBuilder();
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getCompany())) {
			    		   shipping.append(order.getBillingInvoice().getCompany()).append(LINE_BREAK);
			    	   } 
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getCompanyAddress())) {
			    		   shipping.append(order.getBillingInvoice().getCompanyAddress()).append(LINE_BREAK);
			    	   } 
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getCompanyTelephone())) {
			    		   shipping.append(order.getBillingInvoice().getCompanyTelephone()).append(LINE_BREAK);
			    	   }
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getBankName())) {
			    		   shipping.append(order.getBillingInvoice().getBankName()).append(LINE_BREAK);
			    	   } 
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getBankAccount())) {
			    		   shipping.append(order.getBillingInvoice().getBankAccount()).append(LINE_BREAK);
			    	   }
			    	   if(StringUtils.isNotBlank(order.getBillingInvoice().getTaxpayerNumber())) {
			    		   shipping.append(order.getBillingInvoice().getTaxpayerNumber()).append(LINE_BREAK);
			    	   }
		    	   }
		    	   
		    	 //format delivery address
		    	   StringBuilder invoice = new StringBuilder();
		    	   if(StringUtils.isNotBlank(order.getIvoiceCompany())) {
		    		   invoice.append(order.getIvoiceCompany()).append(LINE_BREAK);
		    	   }		    	   
		    	   if(StringUtils.isNotBlank(order.getIvoiceUserName())) {
		    		   invoice.append(order.getIvoiceUserName()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceTelephone())) {
		    		   invoice.append(order.getIvoiceTelephone()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceAddress())) {
		    		   invoice.append(order.getIvoiceAddress()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceCity())) {
		    		   invoice.append(order.getIvoiceCity()).append(LINE_BREAK);
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoiceCity())) {
		    		   invoice.append(order.getIvoiceCity()).append(LINE_BREAK);
		    	   }
		    	   if(order.getIvoiceZone()!=null) {
		    		   Zone zone = zones.get(order.getIvoiceZone().getCode());
		    		   if(zone!=null) {
		    			   invoice.append(zone.getName());
		    		   } else {
		    			   invoice.append(zone.getCode());
		    		   }
		    		   invoice.append(LINE_BREAK);
		    	   } 
		    	   country = countries.get(order.getIvoiceCountry().getIsoCode());
		    	   if(country!=null) {
		    		   invoice.append(country.getName()).append(" ");
		    	   }
		    	   if(StringUtils.isNotBlank(order.getIvoicePostalCode())) {
		    		   invoice.append(order.getIvoicePostalCode()).append(LINE_BREAK);
		    	   }  	   
		    	   
		    	   //format order
		    	  String storeUri = FilePathUtils.buildStoreUri(merchantStore, contextPath);
		    	  StringBuilder orderTable = new StringBuilder();
		    	   
		    	  orderTable.append(TABLE);
		    	  // for(SubOrder subOrder: order.getSubOrders()){
		    	  orderTable.append(TR);
		    	  orderTable.append("<td valign=\"top\" colspan=\"3\">").append(messages.getMessage("menu.store", customerLocale)).append(":").append(subOrder.getMerchant().getStorename()).append(CLOSING_TD);
		    	  orderTable.append(TD).append("&nbsp;").append(messages.getMessage("label.subtotal", customerLocale)).append(":").append(pricingService.getDisplayAmount(subOrder.getTotal(), merchantStore)).append(CLOSING_TD);
		    	  orderTable.append(CLOSING_TR); 
		    	  
				   for(OrderProduct product : subOrder.getOrderProducts()) {
					   Product productModel = productService.getByCode(product.getSku(), language);
					   orderTable.append(TR);
					   	   //images are ugly
				       orderTable.append(TD);
				       String productImage = "";
					   if(productModel!=null && productModel.getProductImage()!=null) {
						   productImage = new StringBuilder().append(storeUri).append(ImageFilePathUtils.buildProductImageFilePath(merchantStore, productModel, productModel.getProductImage().getProductImage())).toString();
					   } else {
						   productImage = new StringBuilder().append(storeUri).append("/resources/img/pimg.jpg").toString();
					   }
					   String imgSrc = new StringBuilder().append("<img src=\"").append(productImage).append("\" width=\"100\">").toString();
					   orderTable.append(imgSrc);
					   orderTable.append(CLOSING_TD);
					   orderTable.append(TD).append(product.getProductName()).append(CLOSING_TD);
				   	   orderTable.append(TD).append(messages.getMessage("label.quantity", customerLocale)).append(": ").append(product.getProductQuantity()).append(CLOSING_TD);
				   		   orderTable.append(TD).append(pricingService.getDisplayAmount(product.getOneTimeCharge(), merchantStore)).append(CLOSING_TD);
					   orderTable.append(CLOSING_TR);
				   }
		    	 //  }

		    	   //order totals
		    	   /**
		    	   for(OrderTotal total : order.getOrderTotal()) {*/
		    	   orderTable.append(TR);
		    	   orderTable.append("<td valign=\"top\" colspan=\"3\">");
		    	   orderTable.append(CLOSING_TD);
	    		   orderTable.append(TD);
	    		   orderTable.append("<strong>");
		    		   			/**if(total.getModule().equals("tax")) {
		    		   				orderTable.append(total.getText()).append(": ");

		    		   			} else {
		    		   				//if(total.getModule().equals("total") || total.getModule().equals("subtotal")) {
		    		   				//}
		    		   				orderTable.append(messages.getMessage(total.getOrderTotalCode(), customerLocale)).append(": ");
		    		   				//if(total.getModule().equals("total") || total.getModule().equals("subtotal")) {
		    		   					
		    		   				//}
		    		   			}*/
		   			orderTable.append(pricingService.getDisplayAmount(subOrder.getTotal(), merchantStore));
	   				orderTable.append("</strong>");
		    		orderTable.append(CLOSING_TD);
		    		orderTable.append(CLOSING_TR);
		    	  // }
		    	   orderTable.append(CLOSING_TABLE);

		           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
		           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, subOrder.getMerchant().getStorename());
		           //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, order.getBilling().getLastName());
		           
		           String[] params = {String.valueOf(order.getId())};
		           String[] dt = {DateUtil.formatDate(order.getDatePurchased(), DateUtil.format_1)};
		           templateTokens.put(EmailConstants.EMAIL_ORDER_NUMBER, messages.getMessage("email.order.confirmation", params, customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_ORDER_DATE, messages.getMessage("email.order.ordered", dt, customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_ORDER_THANKS, messages.getMessage("email.order.send.ontime",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_SHIPPING, shipping.toString());
		           
		           templateTokens.put(EmailConstants.ADDRESS_BILLING, billing.toString());
		           
		           templateTokens.put(EmailConstants.ADDRESS_DELIVERY, invoice.toString());
		           
		           templateTokens.put(EmailConstants.ORDER_PRODUCTS_DETAILS, orderTable.toString());
		           templateTokens.put(EmailConstants.EMAIL_ORDER_DETAILS_TITLE, messages.getMessage("label.order.details",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_BILLING_TITLE, messages.getMessage("label.customer.billinginformation",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_DELIVERY_TITLE, messages.getMessage("label.customer.shippinginformation",customerLocale));
		           templateTokens.put(EmailConstants.PAYMENT_METHOD_TITLE, messages.getMessage("label.order.paymentmode",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_BILLING_INVOICE, messages.getMessage("label.customer.invoiceaddress",customerLocale));
		           templateTokens.put(EmailConstants.PAYMENT_METHOD_DETAILS, messages.getMessage(new StringBuilder().append("payment.type.").append(order.getPaymentType().name()).toString(),customerLocale,order.getPaymentType().name()));
		           
		           
		           
		           
		           
			       String status = messages.getMessage("label.order." + order.getStatus().name(), customerLocale, order.getStatus().name());
			       String[] statusMessage = {DateUtil.formatDate(order.getDatePurchased(), DateUtil.format_1),status};
		           templateTokens.put(EmailConstants.ORDER_STATUS, messages.getMessage("email.order.status", statusMessage, customerLocale));
		           

		           String[] title = {merchantStore.getStorename(), String.valueOf(order.getId())};
		           Email email = new Email();
		           email.setFrom(merchantStore.getStorename());
		           email.setFromEmail(merchantStore.getStoreEmailAddress());
		           email.setSubject(messages.getMessage("email.order.title", title, customerLocale));
		           email.setTo(subOrder.getMerchant().getStoreEmailAddress());
		           
		           email.setTemplateName(EmailConstants.EMAIL_ORDER_TPL);
		           email.setTemplateTokens(templateTokens);

		           LOGGER.debug( "Sending email to {} for order id {} ",subOrder.getMerchant().getStoreEmailAddress(), order.getId() );
		           emailService.sendHtmlEmail(email);

		       } catch (Exception e) {
		           LOGGER.error("Error occured while sending order confirmation email ",e);
		       }
			
		}
	/**
	 * Sends an email to the customer after registration
	 * @param request
	 * @param customer
	 * @param merchantStore
	 * @param customerLocale
	 */
	@Async
	public void sendRegistrationEmail(
		PersistableCustomer customer, MerchantStore merchantStore,
			Locale customerLocale, String contextPath) {
		   /** issue with putting that elsewhere **/ 
	       LOGGER.info( "Sending welcome email to customer" );
	       try {

	           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
	           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getUserName());
	           //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
	           String[] greetingMessage = {merchantStore.getStorename(),FilePathUtils.buildCustomerUri(merchantStore,contextPath),merchantStore.getStoreEmailAddress()};
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_GREETING, messages.getMessage("email.customer.greeting", greetingMessage, customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_USERNAME_LABEL, messages.getMessage("label.generic.username",customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
	           templateTokens.put(EmailConstants.CUSTOMER_ACCESS_LABEL, messages.getMessage("label.customer.accessportal",customerLocale));
	           templateTokens.put(EmailConstants.ACCESS_NOW_LABEL, messages.getMessage("label.customer.accessnow",customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_USER_NAME, messages.getMessage("label.longin.tips",customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, customer.getClearPassword());

	           //shop url
	           String customerUrl = FilePathUtils.buildStoreUri(merchantStore, contextPath);
	           templateTokens.put(EmailConstants.CUSTOMER_ACCESS_URL, customerUrl);

	           Email email = new Email();
	           email.setFrom(merchantStore.getStorename());
	           email.setFromEmail(merchantStore.getStoreEmailAddress());
	           email.setSubject(messages.getMessage("email.newuser.title",customerLocale));
	           email.setTo(customer.getEmailAddress());

	           email.setTemplateName(EmailConstants.EMAIL_CUSTOMER_TPL);
	           email.setTemplateTokens(templateTokens);

	           LOGGER.debug( "Sending email to {} on their  registered email id {} ",customer.getUserName(),customer.getEmailAddress() );
	           emailService.sendHtmlEmail(email);

	       } catch (Exception e) {
	           LOGGER.error("Error occured while sending welcome email ",e);
	       }
		
	}
	
	public int sendActiveEmail(String customerId,MerchantStore store,String contextPath){
		Long id = Long.parseLong(customerId);
		try {
			Customer customer = customerService.getById(id);
			if(customer==null) {
				return -1;
			}
			//已经激活不用再激活
			if(customer.getAnonymous()>1){
				return 1;
			}
			//customer.setAnonymous(true);
			Language userLanguage = customer.getDefaultLanguage();			
			Locale customerLocale = LocaleUtils.getLocale(userLanguage);
			String code = UserReset.generateRandomString();
			String encodedCode = passwordEncoder.encodePassword(code, null);
			//send email
			
	
			//active of a user, send an email
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, store, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			String activeUrl = FilePathUtils.buildStoreUrisELF(store,contextPath);
			
			if(activeUrl.indexOf("sm-shop") ==-1){
				activeUrl+="/sm-shop";
			}
			activeUrl = activeUrl +"/shop/customer/verify.html?account="+customer.getPhone()+"&code="+encodedCode;
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());
	        String[] activingMessage = {store.getStorename()};
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_GREETING, messages.getMessage("email.customer.activing1", activingMessage, customerLocale));
	        templateTokens.put("ACTIVELINK",activeUrl );
	        String[] storeEmail = {store.getStoreEmailAddress()};
	        templateTokens.put("ACTIVECOPY",messages.getMessage("email.customer.activing2", customerLocale) );
	        templateTokens.put("ACTIVEINFO",messages.getMessage("email.customer.activing3", storeEmail,customerLocale) );
	        templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER, messages.getMessage("email.contactowner", storeEmail, customerLocale));

			Email email = new Email();
			email.setFrom(store.getStorename());
			email.setFromEmail(store.getStoreEmailAddress());
			email.setSubject(messages.getMessage("label.generic.amount.active",customerLocale));
			email.setTo(customer.getEmailAddress());
			
			email.setTemplateName(EmailConstants.RESET_ACTIVE_TPL);
			email.setTemplateTokens(templateTokens);			
			emailService.sendHtmlEmail(email);
			//resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			customer.setRecievePhoneTime(encodedCode);
			//waiting for user verify
			customer.setAnonymous(1);
			customerService.saveOrUpdate(customer);
			return 0;
		} catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
			return -1;
		}
	}
	
	public int sendSetPasswordMail(String sid,MerchantStore store,String contextPath){
		int result = -1;
		try{
			Long id = Long.parseLong(sid);
			
			Customer customer = customerService.getById(id);
			
			if(customer==null) {
				return -1;
			}
			
			Language userLanguage = customer.getDefaultLanguage();
			
			Locale customerLocale = LocaleUtils.getLocale(userLanguage);
			
			String password = UserReset.generateRandomString();
			String encodedPassword = passwordEncoder.encodePassword(password, null);
			customer.setPassword(encodedPassword);
			
			String[] storeEmail = {store.getStoreEmailAddress()};
			
			
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, store, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());				
			templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT, messages.getMessage("email.customer.resetpassword.text", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER, messages.getMessage("email.contactowner", storeEmail, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);

			Email email = new Email();
			email.setFrom(store.getStorename());
			email.setFromEmail(store.getStoreEmailAddress());
			email.setSubject(messages.getMessage("label.generic.changepassword",customerLocale));
			email.setTo(customer.getEmailAddress());
			
			email.setTemplateName(EmailConstants.RESET_PASSWORD_TPL);
			email.setTemplateTokens(templateTokens);
			
			emailService.sendHtmlEmail(email);
			customerService.saveOrUpdate(customer);
			result =0;
			
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return result;
	}
	
	public int sendUserSetPasswordMail(String sid,MerchantStore store,String contextPath){
		int result = -1;
		try{
			Long id = Long.parseLong(sid);
			
			User user = userService.getById(id);
			
			if(user==null) {
				return -1;
			}
			
			Language userLanguage = store.getDefaultLanguage();
			
			Locale customerLocale = LocaleUtils.getLocale(userLanguage);
			
			String password = UserReset.generateRandomString();
			String encodedPassword = passwordEncoder.encodePassword(password, null);
			user.setAdminPassword(encodedPassword);
			
			String[] storeEmail = {store.getStoreEmailAddress()};
			
			
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, store, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, user.getAdminName());				
			templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT, messages.getMessage("email.customer.resetpassword.text", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER, messages.getMessage("email.contactowner", storeEmail, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);

			Email email = new Email();
			email.setFrom(store.getStorename());
			email.setFromEmail(store.getStoreEmailAddress());
			email.setSubject(messages.getMessage("label.generic.changepassword",customerLocale));
			email.setTo(user.getAdminEmail());
			
			email.setTemplateName(EmailConstants.RESET_PASSWORD_TPL);
			email.setTemplateTokens(templateTokens);
			
			emailService.sendHtmlEmail(email);
			userService.saveOrUpdate(user);
			result =0;
			
		}catch (Exception e) {
			return -1;
		}
		return result;
	}
	
	public int sendFreezeMail(String customerId,MerchantStore store,String contextPath){
		try{
			Long id = Long.parseLong(customerId);
			Customer customer = customerService.getById(id);
			if(customer==null) {
				
				return -1;
			}
			//非激活用户不能冻结
			if(customer.getAnonymous()!=3){
				return 1;
			}
			Language userLanguage = customer.getDefaultLanguage();			
			Locale customerLocale = LocaleUtils.getLocale(userLanguage);//active of a user, send an email
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, store, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());
	        String[] storeEmail = {store.getStoreEmailAddress()};
	        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, messages.getMessage("email.customer.freeze", storeEmail, customerLocale));
			Email email = new Email();
			email.setFrom(store.getStorename());
			email.setFromEmail(store.getStoreEmailAddress());
			email.setSubject(messages.getMessage("label.generic.amount",customerLocale)+messages.getMessage("label.entity.freeze",customerLocale));
			email.setTo(customer.getEmailAddress());
			
			email.setTemplateName(EmailConstants.EMAIL_NOTIFICATION_TMPL);
			email.setTemplateTokens(templateTokens);			
			emailService.sendHtmlEmail(email);				
			customer.setAnonymous(2);
			customerService.saveOrUpdate(customer);
			return 0;
		}catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
			return -1;
		}
	}
	public int sendUnFreezeMail(String customerId,MerchantStore store,String contextPath){
		try{
			Long id = Long.parseLong(customerId);
			Customer customer = customerService.getById(id);
			if(customer==null) {
				return -1;
			}	
			if (customer.getAnonymous()!=2){
				return 1;
			}
			Language userLanguage = customer.getDefaultLanguage();			
			Locale customerLocale = LocaleUtils.getLocale(userLanguage);
			//send email
			
				//active of a user, send an email
				Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, store, messages, customerLocale);
				templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());
		        String[] activingMessage = {store.getStorename()};
		        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, messages.getMessage("email.customer.unfreeze", activingMessage, customerLocale));
				Email email = new Email();
				email.setFrom(store.getStorename());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("label.generic.amount",customerLocale)+messages.getMessage("label.entity.unfreeze",customerLocale));
				email.setTo(customer.getEmailAddress());
				
				email.setTemplateName(EmailConstants.EMAIL_NOTIFICATION_TMPL);
				email.setTemplateTokens(templateTokens);			
				emailService.sendHtmlEmail(email);				
				customer.setAnonymous(3);
				customerService.saveOrUpdate(customer);
				return 0;
		}catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
			return -1;
		}
	}
	
	public void sendContactEmail(
			ContactForm contact, MerchantStore merchantStore,
				Locale storeLocale, String contextPath) {
			   /** issue with putting that elsewhere **/ 
		       LOGGER.info( "Sending welcome email to customer" );
		       try {

		           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, storeLocale);
		           
		           templateTokens.put(EmailConstants.EMAIL_CONTACT_NAME, contact.getName());
		           templateTokens.put(EmailConstants.EMAIL_CONTACT_EMAIL, contact.getEmail());
		           templateTokens.put(EmailConstants.EMAIL_CONTACT_CONTENT, contact.getComment());
		           
		           String[] contactSubject = {contact.getSubject()};
		           
		           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_CONTACT, messages.getMessage("email.contact",contactSubject, storeLocale));
		           templateTokens.put(EmailConstants.EMAIL_CONTACT_NAME_LABEL, messages.getMessage("label.entity.name",storeLocale));
		           templateTokens.put(EmailConstants.EMAIL_CONTACT_EMAIL_LABEL, messages.getMessage("label.generic.email",storeLocale));



		           Email email = new Email();
		           email.setFrom(merchantStore.getStorename());
		           email.setFromEmail(merchantStore.getStoreEmailAddress());
		           email.setSubject(messages.getMessage("email.contact.title",storeLocale));
		           email.setTo(merchantStore.getStoreEmailAddress());
		           
		           email.setTemplateName(EmailConstants.EMAIL_CONTACT_TMPL);
		           email.setTemplateTokens(templateTokens);

		           LOGGER.debug( "Sending contact email");
		           emailService.sendHtmlEmail(email);

		       } catch (Exception e) {
		           LOGGER.error("Error occured while sending contact email ",e);
		       }
			
		}
	
	/**
	 * Send an email to the customer with last order status
	 * @param request
	 * @param customer
	 * @param order
	 * @param merchantStore
	 * @param customerLocale
	 */
	@Async
	public void sendUpdateOrderStatusEmail(
			Customer customer, Order order, OrderStatusHistory lastHistory, MerchantStore merchantStore,
			Locale customerLocale, String contextPath) {
		   /** issue with putting that elsewhere **/ 
	       LOGGER.info( "Sending order status email to customer" );
	       try {


				Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
				
		        templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		      //remove by cy used for add muti billing && delivery
		        //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
		        //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());
				
		        String[] statusMessageText = {String.valueOf(order.getId()),DateUtils.formatDate(order.getDatePurchased())};
		        String status = messages.getMessage("label.order." + order.getStatus().name(), customerLocale, order.getStatus().name());
		        String[] statusMessage = {DateUtils.formatDate(lastHistory.getDateAdded()),status};
		        
		        String comments = lastHistory.getComments();
		        if(StringUtils.isBlank(comments)) {
		        	comments = messages.getMessage("label.order." + order.getStatus().name(), customerLocale, order.getStatus().name());
		        }
		        
				templateTokens.put(EmailConstants.EMAIL_ORDER_STATUS_TEXT, messages.getMessage("email.order.statustext", statusMessageText, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_ORDER_STATUS, messages.getMessage("email.order.status", statusMessage, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_TEXT_STATUS_COMMENTS, comments);
				
				
				Email email = new Email();
				email.setFrom(merchantStore.getStorename());
				email.setFromEmail(merchantStore.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.order.status.title",new String[]{String.valueOf(order.getId())},customerLocale));
				email.setTo(customer.getEmailAddress());
				
				email.setTemplateName(EmailConstants.ORDER_STATUS_TMPL);
				email.setTemplateTokens(templateTokens);
	
	
				
				emailService.sendHtmlEmail(email);

	       } catch (Exception e) {
	           LOGGER.error("Error occured while sending order download email ",e);
	       }
		
	}
	
	
	
	/**
	 * Sends a change password notification email to the Customer
	 * @param customer
	 * @param merchantStore
	 * @param customerLocale
	 * @param contextPath
	 */
	@Async
	public void changePasswordNotificationEmail(
			Customer customer, MerchantStore merchantStore,
			Locale customerLocale, String contextPath) {
	       LOGGER.debug( "Sending change password email" );
	       try {


				Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
				
		        templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		      //remove by cy used for add muti billing && delivery
		        //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
		        //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());
				
		        String[] date = {DateUtil.formatDate(new Date(), DateUtil.format_2)};
		        
		        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, messages.getMessage("label.notification.message.passwordchanged", date, customerLocale));
		        

				Email email = new Email();
				email.setFrom(merchantStore.getStorename());
				email.setFromEmail(merchantStore.getStoreEmailAddress());
				email.setSubject(messages.getMessage("label.notification.title.passwordchanged",customerLocale));
				email.setTo(customer.getEmailAddress());
				
				email.setTemplateName(EmailConstants.EMAIL_NOTIFICATION_TMPL);
				email.setTemplateTokens(templateTokens);
	
	
				
				emailService.sendHtmlEmail(email);

	       } catch (Exception e) {
	           LOGGER.error("Error occured while sending change password email ",e);
	       }
		
	}
	
	/**
	 * Sends a change password notification email to the Customer
	 * @param customer
	 * @param merchantStore
	 * @param customerLocale
	 * @param contextPath
	 */
	@Async
	public void changeBasicinfoeNotificationEmail(
			Customer customer, MerchantStore merchantStore,
			Locale customerLocale, String contextPath) {
	       LOGGER.debug( "Sending change password email" );
	       try {


				Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
				
		        templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		      //remove by cy used for add muti billing && delivery
		        //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
		        //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getNick());
				
		        String[] date = {DateUtil.formatLongDate(new Date())};
		        
		        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, messages.getMessage("label.notification.message.basciinfochanged", date, customerLocale));
		        

				Email email = new Email();
				email.setFrom(merchantStore.getStorename());
				email.setFromEmail(merchantStore.getStoreEmailAddress());
				email.setSubject(messages.getMessage("label.notification.message.basciinfochanged",customerLocale));
				email.setTo(customer.getEmailAddress());
				
				email.setTemplateName(EmailConstants.EMAIL_NOTIFICATION_TMPL);
				email.setTemplateTokens(templateTokens);
	
	
				
				emailService.sendHtmlEmail(email);

	       } catch (Exception e) {
	           LOGGER.error("Error occured while sending change password email ",e);
	       }
		
	}
	
	/**
	 * 
	 * @param product
	 * @param merchantStore
	 * @param customerLocale
	 * @param contextPath
	 * @param result
	 * @param better
	 */
	 

	@Async
	public void sendDeleteQualityEmail(Product product, MerchantStore merchantStore,Locale customerLocale, String contextPath,String result,String better) {
		   /** issue with putting that elsewhere **/ 
	       LOGGER.info( "Sending download email to customer" );
	       try {

	           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
	           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
	         //remove by cy used for add muti billing && delivery
	           // templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
	           //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
	           ProductDescription description = product.getDescriptions().iterator().next();
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, merchantStore.getStorename());
	           String[] downloadMessage = {description.getName(), result};
	           templateTokens.put(EmailConstants.PRODUCT_AUDIT, messages.getMessage("email.audit.text", downloadMessage, customerLocale));
	           
        	   String[] betterMessage = {description.getName(), better};
        	   templateTokens.put(EmailConstants.PRODUCT_BETTER, messages.getMessage("email.audit.delete", betterMessage, customerLocale));
	          
	          
	           String[] resultTitle = {description.getName()};
	           
	           Email email = new Email();
	           email.setFrom(merchantStore.getStorename());
	           email.setFromEmail(merchantStore.getStoreEmailAddress());
	           email.setSubject(messages.getMessage("email.audit.title", resultTitle, customerLocale));
	           email.setTo(product.getMerchantStore().getStoreEmailAddress());
	           
	           email.setTemplateName(EmailConstants.EMAIL_AUDIT_TPL);
	           email.setTemplateTokens(templateTokens);

//	           LOGGER.debug( "Sending email to {} with download info",product.getMerchantStore().getStoreEmailAddress());
	           emailService.sendHtmlEmail(email);

	       } catch (Exception e) {
	           LOGGER.error("Error occured while sending order download email ",e);
	       }
		
		}
}
