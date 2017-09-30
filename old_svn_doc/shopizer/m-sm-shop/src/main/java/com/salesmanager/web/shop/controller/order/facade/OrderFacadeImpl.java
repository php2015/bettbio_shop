package com.salesmanager.web.shop.controller.order.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.common.model.InvoiceInfo;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.OrderTotalType;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.SubOrderService;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductService;
import com.salesmanager.core.business.payments.model.CreditCardType;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.shipping.model.ShippingSummary;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.web.entity.customer.CustomerAdress;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.order.EzybioOrder;
import com.salesmanager.web.entity.order.OrderEntity;
import com.salesmanager.web.entity.order.OrderTotal;
import com.salesmanager.web.entity.order.PersistableOrder;
import com.salesmanager.web.entity.order.PersistableOrderProduct;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.entity.order.ShopOrder;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerPopulator;
import com.salesmanager.web.populator.order.ShoppingCartItemPopulator;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.ImageFilePathUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;

@Service("orderFacade")
public class OrderFacadeImpl implements OrderFacade {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderFacadeImpl.class);


	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private ShippingService shippingService;
	@Autowired
	private CustomerFacade customerFacade;
	@Autowired
	private SubOrderService subOrderService;
	@Autowired
	private OrderProductService orderProductService;
	
	
	@Autowired
	private LabelUtils messages;


	@Override
	public ShopOrder initializeOrder(MerchantStore store, Customer customer,
			ShoppingCart shoppingCart, Language language) throws Exception {

		//assert not null shopping cart items
		
		ShopOrder order = new ShopOrder();
		
		OrderStatus orderStatus = OrderStatus.ORDERED;
		order.setOrderStatus(orderStatus);
		
		if(customer==null) {
				customer = this.initEmptyCustomer(store);
		}
		
		PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
		order.setCustomer(persistableCustomer);

		//keep list of shopping cart items for core price calculation
		//List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>(shoppingCart.getLineItems());
		//order.setShoppingCartItems(items);
		
		return order;
	}
	


	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			ShopOrder order, Language language) throws Exception {
		

		Customer customer = customerFacade.getCustomerModel(order.getCustomer(), store, language);
		OrderTotalSummary summary = this.calculateOrderTotal(store, customer, order, language);
		this.setOrderTotals(order, summary);
		return summary;
	}

	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			PersistableOrder order, Language language) throws Exception {
	
		List<PersistableOrderProduct> orderProducts = order.getOrderProductItems();
		
		ShoppingCartItemPopulator populator = new ShoppingCartItemPopulator();
		populator.setProductAttributeService(productAttributeService);
		populator.setProductService(productService);
		populator.setShoppingCartService(shoppingCartService);
		
		List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>();
		for(PersistableOrderProduct orderProduct : orderProducts) {
			ShoppingCartItem item = populator.populate(orderProduct, new ShoppingCartItem(), store, language);
			items.add(item);
		}
		

		Customer customer = customer(order.getCustomer(), store, language);
		
		OrderTotalSummary summary = this.calculateOrderTotal(store, customer, order, language);

		return summary;
	}
	
	private OrderTotalSummary calculateOrderTotal(MerchantStore store, Customer customer, PersistableOrder order, Language language) throws Exception {
		
		OrderTotalSummary orderTotalSummary = null;
		
		OrderSummary summary = new OrderSummary();
		
		
		if(order instanceof ShopOrder) {
			ShopOrder o = (ShopOrder)order;
			Set<SubOrder>subOrders = o.getSubOrders();
			List<ShoppingCartItem> products = new ArrayList<ShoppingCartItem>();
			for(SubOrder subOrder:subOrders){				
				ShoppingCartItemPopulator populator = new ShoppingCartItemPopulator();
				populator.setProductAttributeService(productAttributeService);
				populator.setProductService(productService);
				populator.setShoppingCartService(shoppingCartService);
				List<ShoppingCartItem> shoppingCartItems =  populator.populateList(subOrder.getOrderProducts(),language);
				for(ShoppingCartItem shoppingCartItem:shoppingCartItems){
					products.add(shoppingCartItem);
				}
				//summary.addProducts(shoppingCartItems);
			}
			if(products != null){
				summary.setProducts(products);
			}else {
				throw new Exception("calculateOrderTotal not yet implemented for PersistableOrder");
			}
			if(o.getShippingSummary()!=null) {
				summary.setShippingSummary(o.getShippingSummary());
			}
			orderTotalSummary = orderService.caculateOrderTotal(summary, customer, store, language);
		} else {
			//need Set of ShoppingCartItem
			//PersistableOrder not implemented
			throw new Exception("calculateOrderTotal not yet implemented for PersistableOrder");
		}

		return orderTotalSummary;
		
	}
	
	
	private PersistableCustomer persistableCustomer(Customer customer, MerchantStore store, Language language) throws Exception {
		
		PersistableCustomerPopulator customerPopulator = new PersistableCustomerPopulator();
		PersistableCustomer persistableCustomer = customerPopulator.populate(customer, new PersistableCustomer(), store, language);
		return persistableCustomer;
		
	}
	
	private Customer customer(PersistableCustomer customer, MerchantStore store, Language language) throws Exception {
		CustomerPopulator customerPopulator = new CustomerPopulator();
		Customer cust = customerPopulator.populate(customer, new Customer(), store, language);
		return cust;
		
	}
	
	private void setOrderTotals(OrderEntity order, OrderTotalSummary summary) {
		
		List<OrderTotal> totals = new ArrayList<OrderTotal>();
		List<com.salesmanager.core.business.order.model.OrderTotal> orderTotals = summary.getTotals();
		for(com.salesmanager.core.business.order.model.OrderTotal t : orderTotals) {
			OrderTotal total = new OrderTotal();
			total.setCode(t.getOrderTotalCode());
			total.setTitle(t.getTitle());
			total.setValue(t.getValue());
			totals.add(total);
		}
		
		order.setTotals(totals);
		
	}


	/**
	 * Submitted object must be valided prior to the invocation of this method
	 */
	@Override
	public Order processOrder(ShopOrder order, Customer customer, MerchantStore store,
			Language language) throws ServiceException {
				
		return this.processOrderModel(order, customer, null, store, language);

	}
	
	@Override
	public Order processOrder(ShopOrder order, Customer customer, Transaction transaction, MerchantStore store,
			Language language) throws ServiceException {
				
		return this.processOrderModel(order, customer, transaction, store, language);

	}
	
	private Order processOrderModel(ShopOrder order, Customer customer, Transaction transaction, MerchantStore store,
			Language language) throws ServiceException {
		
		try {
			
			//modify by cy use for add muti delivery
			/**
			if(order.isShipToBillingAdress()) {//customer shipping is billing
				PersistableCustomer orderCustomer = order.getCustomer();
				Address billing = orderCustomer.getBilling();
				orderCustomer.setDelivery(billing);
			}

 */

			
			Order modelOrder = setDeliveryInfo(order.getAddressid(),order.getInvoiceid(),order.getInvoicesendid(),customer);
			modelOrder.setDatePurchased(new Date());
			//remove by cy used for add muti billing && delivery
			//modelOrder.setBilling(customer.getBilling());
			//modelOrder.setDelivery(customer.getDelivery());
			//modify by cy
			modelOrder.setPaymentModuleCode("moneyorder");
			//modify by cy
			modelOrder.setPaymentType(PaymentType.MONEYORDER);
			//modelOrder.setShippingModuleCode(order.getShippingModule());
			modelOrder.setLocale(LocaleUtils.getLocale(store));//set the store locale based on the country for order $ formatting
			//init suborder
			Set<SubOrder> subOrders = order.getSubOrders();
			if(subOrders == null ) throw new ServiceException("suborder is null");
			for(SubOrder sub:subOrders){
				sub.setOrder(modelOrder);
			}
			modelOrder.setSubOrders(order.getSubOrders());
			
			/**
			OrderProductPopulator orderProductPopulator = new OrderProductPopulator();
			orderProductPopulator.setDigitalProductService(digitalProductService);
			orderProductPopulator.setProductAttributeService(productAttributeService);
			orderProductPopulator.setProductService(productService);
			*/
			OrderTotalSummary summary = order.getOrderTotalSummary();
			/*
			List<com.salesmanager.core.business.order.model.OrderTotal> totals = summary.getTotals();

			//re-order totals
			/**
			Collections.sort(
					totals,
					new Comparator<com.salesmanager.core.business.order.model.OrderTotal>() {
					       public int compare(com.salesmanager.core.business.order.model.OrderTotal x, com.salesmanager.core.business.order.model.OrderTotal y) {
					            if(x.getSortOrder()==y.getSortOrder())
					            	return 0;
					            return x.getSortOrder() < y.getSortOrder() ? -1 : 1;
					        }
				
			});
			/**
			Set<com.salesmanager.core.business.order.model.OrderTotal> modelTotals = new LinkedHashSet<com.salesmanager.core.business.order.model.OrderTotal>();
			for(com.salesmanager.core.business.order.model.OrderTotal total : totals) {
				total.setOrder(modelOrder);
				modelTotals.add(total);
			}
			*/
			//modelOrder.setSubOrders((Set<SubOrder>)order.getSubOrders());
			//modelOrder.setOrderTotal(modelTotals);
			modelOrder.setTotal(order.getOrderTotalSummary().getTotal());
	
			//order misc objects
			modelOrder.setCurrency(store.getCurrency());
			//modelOrder.setMerchant(store);

			
			
			//customer object
			orderCustomer(customer, modelOrder, language);
			
			//populate shipping information
			if(!StringUtils.isBlank(order.getShippingModule())) {
				modelOrder.setShippingModuleCode(order.getShippingModule());
			}
			
			//String paymentType = order.getPaymentMethodType();
			Payment payment = new Payment();
			payment.setPaymentType(PaymentType.MONEYORDER);
			/**
			if(PaymentType.CREDITCARD.name().equals(paymentType)) {
				
				
				
				payment = new CreditCardPayment();
				((CreditCardPayment)payment).setCardOwner(order.getPayment().get("creditcard_card_holder"));
				((CreditCardPayment)payment).setCredidCardValidationNumber(order.getPayment().get("creditcard_card_cvv"));
				((CreditCardPayment)payment).setCreditCardNumber(order.getPayment().get("creditcard_card_number"));
				((CreditCardPayment)payment).setExpirationMonth(order.getPayment().get("creditcard_card_expirationmonth"));
				((CreditCardPayment)payment).setExpirationYear(order.getPayment().get("creditcard_card_expirationyear"));
				
				CreditCardType creditCardType =null;
				String cardType = order.getPayment().get("creditcard_card_type");
				
				if(cardType.equalsIgnoreCase(CreditCardType.AMEX.name())) {
					creditCardType = CreditCardType.AMEX;
				} else if(cardType.equalsIgnoreCase(CreditCardType.VISA.name())) {
					creditCardType = CreditCardType.VISA;
				} else if(cardType.equalsIgnoreCase(CreditCardType.MASTERCARD.name())) {
					creditCardType = CreditCardType.MASTERCARD;
				} else if(cardType.equalsIgnoreCase(CreditCardType.DINERS.name())) {
					creditCardType = CreditCardType.DINERS;
				} else if(cardType.equalsIgnoreCase(CreditCardType.DISCOVERY.name())) {
					creditCardType = CreditCardType.DISCOVERY;
				}
				

				
				
				((CreditCardPayment)payment).setCreditCard(creditCardType);
			
				CreditCard cc = new CreditCard();
				cc.setCardType(creditCardType);
				cc.setCcCvv(((CreditCardPayment)payment).getCredidCardValidationNumber());
				cc.setCcOwner(((CreditCardPayment)payment).getCardOwner());
				cc.setCcExpires(((CreditCardPayment)payment).getExpirationMonth() + "-" + ((CreditCardPayment)payment).getExpirationYear());
			
				//hash credit card number
				String maskedNumber = CreditCardUtils.maskCardNumber(order.getPayment().get("creditcard_card_number"));
				cc.setCcNumber(maskedNumber);
				modelOrder.setCreditCard(cc);

			}
			
			if(PaymentType.PAYPAL.name().equals(paymentType)) {
				
				//check for previous transaction
				if(transaction==null) {
					throw new ServiceException("payment.error");
				}
				
				payment = new com.salesmanager.core.business.payments.model.PaypalPayment();
				
				((com.salesmanager.core.business.payments.model.PaypalPayment)payment).setPayerId(transaction.getTransactionDetails().get("PAYERID"));
				((com.salesmanager.core.business.payments.model.PaypalPayment)payment).setPaymentToken(transaction.getTransactionDetails().get("TOKEN"));
				
				
			}
			**/

			modelOrder.setPaymentModuleCode(order.getPaymentModule());
			payment.setModuleName(order.getPaymentModule());

			if(transaction!=null) {
				orderService.processOrder(modelOrder, customer, null, summary, payment, store);
			} else {
				try{
					orderService.processOrder(modelOrder, customer, null, summary, payment, transaction, store);
		    	}catch (Exception e) {
		    		throw new ServiceException(e);
		    	}
				
			}
			

			
			return modelOrder;
		
		} catch(ServiceException se) {//may be invalid credit card
			throw se;
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	private void orderCustomer(Customer customer, Order order, Language language) throws Exception {

		
		order.setCustomerEmailAddress(customer.getEmailAddress());
		order.setCustomerId(customer.getId());

		
		
	}



	@Override
	public Customer initEmptyCustomer(MerchantStore store) {
		
		Customer customer = new Customer();
		
		return customer;
	}



	@Override
	public void refreshOrder(ShopOrder order, MerchantStore store,
			Customer customer, ShoppingCart shoppingCart, Language language)
			throws Exception {
		if(customer==null && order.getCustomer()!=null) {
			order.getCustomer().setId(0L);//reset customer id
		}
		
		if(customer!=null) {
			PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
			order.setCustomer(persistableCustomer);
		}
		
		Set<ShoppingCartItem> items = shoppingCart.getLineItems();
		Set<SubOrder> subOrders = customerFacade.getSubOrders(items, store, language);
		order.setSubOrders(subOrders);
		
		return;
	}
	
	@Override
	public ShippingQuote getShippingQuote(PersistableCustomer persistableCustomer, ShoppingCart cart, ShopOrder order, MerchantStore store, Language language) throws Exception {
		

		//create shipping products
		List<ShippingProduct> shippingProducts = shoppingCartService.createShippingProduct(cart);

		if(CollectionUtils.isEmpty(shippingProducts)) {
			return null;//products are virtual
		}
				
		//Customer customer = customerFacade.getCustomerModel(persistableCustomer, store, language);

		//remove by cy used for add muti billing && delivery
		
		////remove by cy used for add muti billing && delivery
		ShippingQuote quote = shippingService.getShippingQuote(store, null, shippingProducts, language);

		return quote;

	}
	
	@Override
	public List<Country> getShipToCountry(MerchantStore store, Language language) throws Exception {
		
		List<Country> shippingCountriesList = shippingService.getShipToCountryList(store, language);
		return shippingCountriesList;
		
	}
	




	@Override
	public ShippingSummary getShippingSummary(ShippingQuote quote,
			MerchantStore store, Language language) {
		
		if(quote.getSelectedShippingOption()!=null) {
		
			ShippingSummary summary = new ShippingSummary();
			summary.setFreeShipping(quote.isFreeShipping());
			summary.setTaxOnShipping(quote.isApplyTaxOnShipping());
			summary.setHandling(quote.getHandlingFees());
			summary.setShipping(quote.getSelectedShippingOption().getOptionPrice());
			summary.setShippingOption(quote.getSelectedShippingOption().getOptionName());
			summary.setShippingModule(quote.getShippingModuleCode());
			
			return summary;
		
		} else {
			return null;
		}
	}

	@Override
	public void validateOrder(ShopOrder order, BindingResult bindingResult, Map<String,String> messagesResult, MerchantStore store,  Locale locale) throws ServiceException {


		Validate.notNull(messagesResult,"messagesResult should not be null");
	
	
		try {
	

			//Language language = (Language)request.getAttribute("LANGUAGE");
			//modify by cy use for add muti delivey
			//validate order shipping and billing
						String paymentType = order.getPaymentMethodType();
			//
						/**
			if(!shoppingCartService.isFreeShoppingCart(order.getShoppingCartItems()) && paymentType==null) {
				
			}*/
			
			//validate payment
						//modify by cy now not need unity shipping
			/**
						if(paymentType==null) {
				ServiceException serviceException = new ServiceException(ServiceException.EXCEPTION_VALIDATION,"payment.required");
				throw serviceException;
			}
			*/
			//validate shipping
			//modify by cy now not need unity shipping
			/**
			if(shippingService.requiresShipping(order.getShoppingCartItems(), store) && order.getSelectedShippingOption()==null) {
				ServiceException serviceException = new ServiceException(ServiceException.EXCEPTION_VALIDATION,"shipping.required");
				throw serviceException;
			}*/
			
			//pre-validate credit card
			if(PaymentType.CREDITCARD.name().equals(paymentType)) {
				String cco = order.getPayment().get("creditcard_card_holder");
				String cvv = order.getPayment().get("creditcard_card_cvv");
				String ccn = order.getPayment().get("creditcard_card_number");
				String ccm = order.getPayment().get("creditcard_card_expirationmonth");
				String ccd = order.getPayment().get("creditcard_card_expirationyear");
				
				if(StringUtils.isBlank(cco) || StringUtils.isBlank(cvv) || StringUtils.isBlank(ccn) ||
					StringUtils.isBlank(ccm) || StringUtils.isBlank(ccd)) {
					ObjectError error = new ObjectError("creditcard_card_holder",messages.getMessage("NotEmpty.customer.shipping.stateProvince", locale));
	            	bindingResult.addError(error);
	            	messagesResult.put("creditcard_card_holder",messages.getMessage("NotEmpty.customer.shipping.stateProvince", locale));
	            	return;
				}
				
				CreditCardType creditCardType =null;
				String cardType = order.getPayment().get("creditcard_card_type");
				
				if(cardType.equalsIgnoreCase(CreditCardType.AMEX.name())) {
					creditCardType = CreditCardType.AMEX;
				} else if(cardType.equalsIgnoreCase(CreditCardType.VISA.name())) {
					creditCardType = CreditCardType.VISA;
				} else if(cardType.equalsIgnoreCase(CreditCardType.MASTERCARD.name())) {
					creditCardType = CreditCardType.MASTERCARD;
				} else if(cardType.equalsIgnoreCase(CreditCardType.DINERS.name())) {
					creditCardType = CreditCardType.DINERS;
				} else if(cardType.equalsIgnoreCase(CreditCardType.DISCOVERY.name())) {
					creditCardType = CreditCardType.DISCOVERY;
				}
				
				if(creditCardType==null) {
					ServiceException serviceException = new ServiceException(ServiceException.EXCEPTION_VALIDATION,"cc.type");
					throw serviceException;
				}

			}
			
			
			

      
	        
        
		} catch(ServiceException se) {
			LOGGER.error("Error while commiting order",se);
			throw se;
		}

}



	@Override
	public ReadableOrderList getReadableOrderList(Customer customer, int start, int maxCount, Language language) throws Exception {
		
		OrderCriteria criteria = new OrderCriteria();
		criteria.setStartIndex(start);
		criteria.setMaxCount(maxCount);
		List<Long> cid = new ArrayList<Long>();
		cid.add(customer.getId());
		criteria.setCustomerId(cid);
		return this.getReadableOrderList(criteria, language);
		
	}
	
	

	@Override
	public ShippingQuote getShippingQuote(Customer customer, ShoppingCart cart,
			PersistableOrder order, MerchantStore store, Language language)
			throws Exception {
		//create shipping products
		List<ShippingProduct> shippingProducts = shoppingCartService.createShippingProduct(cart);

		if(CollectionUtils.isEmpty(shippingProducts)) {
			return null;//products are virtual
		}

		
		
		ShippingQuote quote = shippingService.getShippingQuote(store, null, shippingProducts, language);

		return quote;
	}
	
	

 
	@Override
	public ReadableOrderList getReadableOrderList(OrderCriteria criteria,Language language) throws Exception {
    	OrderList orderList = orderService.listByCriteria(criteria);
    	
    	ReadableOrderList returnList = new ReadableOrderList();
    	List<Order> orders = orderList.getOrders();
    	if(CollectionUtils.isEmpty(orders)) {
			returnList.setTotal(0);
			returnList.setMessage("No results for store code " );
			return null;
		}
    	boolean isAll = true;
    	if(criteria.getStoreId() != -1l) isAll=false;
    		
    	List<EzybioOrder> ezybioOrders = this.populateEzybio(orders,language,isAll);
    	if(ezybioOrders== null){
    		returnList.setTotal(0);
			returnList.setMessage("No results for store code " );
			return null;
    	}
    	returnList.setEzybioOrder(ezybioOrders);
    	returnList.setTotal(orderList.getTotalCount());
		return returnList;
	}
    
    private List<EzybioOrder> populateEzybio(List<Order> orders,Language language,boolean isALL){
    	if(orders !=null){
			List <EzybioOrder> ezybioOrders = new ArrayList<EzybioOrder>();
			for(Order order:orders){
				EzybioOrder ezybioOrder = this.popEzybio(order,language,isALL);
				ezybioOrders.add(ezybioOrder);
			}
			return ezybioOrders;
    	}
    	
    	return null;
    }
    private EzybioOrder popEzybio(Order order,Language language,boolean isALL){
    	EzybioOrder ezybioOrder = new EzybioOrder();
    	ezybioOrder.setDatePurchased(order.getDatePurchased());
    	ezybioOrder.setOrderStatus(order.getStatus());
    	ezybioOrder.setTotal(order.getTotal());
    	ezybioOrder.setOrderID(order.getId().toString());
    	ezybioOrder.setBillName(order.getBillingUserName());
    	ezybioOrder = this.popSubOrder(ezybioOrder,order.getSubOrders(),language,isALL);
    	
    	return ezybioOrder;
    }
    
    private EzybioOrder popSubOrder(EzybioOrder ezybioOrder,Set<SubOrder> subOrders,Language language,boolean isALL){
    	if(subOrders !=null){
    		BigDecimal totol = new BigDecimal(0);
    		List<com.salesmanager.web.entity.order.SubOrder> returnSubOrderList = new ArrayList<com.salesmanager.web.entity.order.SubOrder>();
    		for (SubOrder subOrder:subOrders){
    			totol.add(subOrder.getTotal());
    			returnSubOrderList.add(this.addSubOrder(subOrder,language));
    		}
    		if(isALL == false) ezybioOrder.setTotal(totol);
    		if(returnSubOrderList != null) ezybioOrder.setSubOrders(returnSubOrderList);
    		return ezybioOrder;
    	}
    	return null;
    }
    
    private com.salesmanager.web.entity.order.SubOrder addSubOrder(SubOrder subOrder,Language language){
    	com.salesmanager.web.entity.order.SubOrder returnSubOrder = new com.salesmanager.web.entity.order.SubOrder();
		returnSubOrder.setId(subOrder.getId());
		returnSubOrder.setMerchantStore(subOrder.getMerchant());
		returnSubOrder.setStatus(subOrder.getStatus());
		returnSubOrder.setTotal(subOrder.getTotal());
		if(subOrder.getDeliveryCode() !=null && !StringUtils.isBlank(subOrder.getDeliveryCode())){
			returnSubOrder.setDeliveryName(subOrder.getDeliveryCode());
		}
		if(subOrder.getDeliveryNumber() !=null && !StringUtils.isBlank(subOrder.getDeliveryNumber())){
			returnSubOrder.setDeliveryNum(subOrder.getDeliveryNumber());
		}
		
		List<com.salesmanager.web.entity.shoppingcart.ShoppingCartItem> shoppingCartItems = this.getShoppingCartItem(subOrder.getOrderProducts(),language);
		if(shoppingCartItems != null) returnSubOrder.setCartItems(shoppingCartItems);
		return returnSubOrder;
    }
    
    private List<com.salesmanager.web.entity.shoppingcart.ShoppingCartItem> getShoppingCartItem(Set<OrderProduct> products,Language language){
    	if(products != null){
    		List<com.salesmanager.web.entity.shoppingcart.ShoppingCartItem> shoppingCartItems = new ArrayList<com.salesmanager.web.entity.shoppingcart.ShoppingCartItem>();
    		for(OrderProduct product:products){
    			com.salesmanager.web.entity.shoppingcart.ShoppingCartItem cartItem = new com.salesmanager.web.entity.shoppingcart.ShoppingCartItem();
    			cartItem.setProductId(product.getProductID());
    			cartItem.setName(product.getProductName());
    			cartItem.setEnName(product.getProductEnName());
    			cartItem.setQuantity(product.getProductQuantity());
    			cartItem.setId(product.getId());
    			cartItem.setSpecs(product.getSpecifications());
    			if (product.getProductReview()!=null) {
					cartItem.setReviewed(1);
				}
    			/**
    			Set<OrderProductPrice> prices = product.getPrices();
    			for(OrderProductPrice price:prices){
    				cartItem.setProductPrice(price.getProductPrice());    				
    				//now only one price
    				break;
    			}*/
    			cartItem.setPrice(product.getPrice().toString());
    			Product product1 = productService.getById(product.getProductID());
    			if(product1 != null){
    				Set<ProductDescription> descs = product1.getDescriptions();
    				//String url = this.getUrl(descs, language.getCode());
    				//if(url != null) cartItem.setFriendlyUrl(url);
    				if (descs!=null&&descs.size()>0) {
    					ProductDescription des = descs.iterator().next();
    					cartItem.setFriendlyUrl(StringUtils.trimToEmpty(des.getSeUrl()));
					}
    				ProductImage image = product1.getProductImage();
                    if(image!=null) {
                    	
                        String imagePath = ImageFilePathUtils.buildProductImageFilePath(product1.getMerchantStore(), product1.getSku(), image.getProductImage());
                    	cartItem.setImage(imagePath);
                    }
    			}
    			cartItem.setSubTotal(product.getOneTimeCharge().toString());
    			shoppingCartItems.add(cartItem);
    		}
    		return shoppingCartItems;
    	}
    	return null;
    }
    /**
    private String  getUrl(Set<ProductDescription> descs,String code){
    	if(descs != null){
    		for(ProductDescription desc:descs ){
    			if (desc.getLanguage().getCode().equalsIgnoreCase(code)){
    				return desc.getSeUrl();
    			}
    		}
    	}
    	return null;
    }*/
    
	@Override
	public ReadableOrderList getReadableOrderList(MerchantStore store,
			int start, int maxCount, Language language) throws Exception {
		
		OrderCriteria criteria = new OrderCriteria();
		criteria.setStartIndex(start);
		criteria.setMaxCount(maxCount);

		return this.getReadableOrderList(criteria, language);
	}



	@Override
	public PersistableCustomer getPersistableCustomer(Customer customer,
			MerchantStore store, Language language) throws Exception {
		// TODO Auto-generated method stub
		if(customer!=null) {
			PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
			return persistableCustomer;
		}
		return null;
	}



	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			List<ShoppingCartItem> items, Language language,Customer customer) throws Exception {
		// TODO Auto-generated method stub
			OrderTotalSummary orderTotalSummary = null;
			OrderSummary summary = new OrderSummary();
			summary.setProducts(items);
			orderTotalSummary = orderService.caculateOrderTotal(summary, customer, store, language);
		return orderTotalSummary;
		
	}




	private Order setDeliveryInfo(long billingid, long invoiceid,
			long deliverytype,Customer customer) throws Exception {
		// TODO Auto-generated method stub
		//set billing info to order
		Order order = new Order();
		Set<CustomerAddress> addresss =  customer.getAddresss();
		for(CustomerAddress address : addresss){
			if(address.getId()==billingid){
				order.setBillingAddress((String)getEntiryDate(address.getAddress()));
				order.setBillingCity((String)getEntiryDate(address.getCity()));
				order.setBillingCompany((String)getEntiryDate(address.getCompany()));
				order.setBillingCountry((Country)getEntiryDate(address.getCountry()));
				order.setBillingPostalCode((String)getEntiryDate(address.getPostalCode()));
				order.setBillingTelephone((String)getEntiryDate(address.getTelephone()));
				order.setBillingUserName((String)getEntiryDate(address.getName()));
				order.setBillingZone((Zone)getEntiryDate(address.getZone()));
				break;
			}
		}
		Set<CustomerInvoice> invoices = customer.getInvoices();
		for(CustomerInvoice invoice : invoices){
			if(invoice.getId() == invoiceid){
				InvoiceInfo invoiceInfo = new InvoiceInfo();
				invoiceInfo.setBankAccount((String)getEntiryDate(invoice.getBankAccount()));
				invoiceInfo.setBankName((String)getEntiryDate(invoice.getBankName()));
				invoiceInfo.setCompany((String)getEntiryDate(invoice.getCompany()));
				invoiceInfo.setCompanyAddress((String)getEntiryDate(invoice.getCompanyAddress()));
				invoiceInfo.setCompanyTelephone((String)getEntiryDate(invoice.getCompanyTelephone()));
				invoiceInfo.setTaxpayerNumber((String)getEntiryDate(invoice.getTaxpayerNumber()));
				invoiceInfo.setType((int)getEntiryDate(invoice.getType()));
				order.setBillingInvoice(invoiceInfo);
				break;
			}
		}
		//if(billingid != deliverytype){
			for(CustomerAddress address : addresss){
				if(address.getId()==deliverytype){					
					order.setIvoiceAddress((String)getEntiryDate(address.getAddress()));
					order.setIvoiceCity((String)getEntiryDate(address.getCity()));
					order.setIvoiceCompany((String)getEntiryDate(address.getCompany()));
					order.setIvoiceCountry((Country)getEntiryDate(address.getCountry()));
					order.setIvoicePostalCode((String)getEntiryDate(address.getPostalCode()));
					order.setIvoiceTelephone((String)getEntiryDate(address.getTelephone()));
					order.setIvoiceUserName((String)getEntiryDate(address.getName()));
					order.setIvoiceZone((Zone)getEntiryDate(address.getZone()));
					break;
				}
			}
		//}
		return order;
	}
	
	private Object getEntiryDate(Object o){
		if(o != null){
			return o;
		}
		return null;
	}



	@Override
	public com.salesmanager.web.entity.order.SubOrder getSubOrderByStore(
			List<com.salesmanager.web.entity.order.SubOrder> subOrders,
			MerchantStore store) throws Exception {
		// TODO Auto-generated method stub
		if(subOrders == null) return new com.salesmanager.web.entity.order.SubOrder();
		for(com.salesmanager.web.entity.order.SubOrder subOrder : subOrders){
			if(subOrder.getMerchantStore().getId()==store.getId()) return subOrder;
		}
		
		return new com.salesmanager.web.entity.order.SubOrder();
	}



	@Override
	public List<com.salesmanager.web.entity.order.SubOrder> setSubOrder(
			List<com.salesmanager.web.entity.order.SubOrder> subOrders,
			com.salesmanager.web.entity.order.SubOrder subOrder)
			throws Exception {
		// TODO Auto-generated method stub
		int i=0;
		for(com.salesmanager.web.entity.order.SubOrder order:subOrders){
			if(order.getMerchantStore().getId()==subOrder.getMerchantStore().getId()){
				subOrders.remove(i);
				subOrders.add(subOrder);
				return subOrders;
			}
			i++;
		}
		subOrders.add(subOrder);
		return subOrders;
	}



	@Override
	public OrderTotalSummary calculateOrderTotal(ShopOrder order)
			throws Exception {
		// TODO Auto-generated method stub
		OrderTotalSummary orderTotalSummary = new OrderTotalSummary();
		BigDecimal total = new BigDecimal(0);
		Set<SubOrder> suborder = order.getSubOrders();
		if(suborder !=null){
			for(SubOrder sub:suborder){
				total = total.add(sub.getTotal());
			}
		}
		orderTotalSummary.setTotal(total);
		List<com.salesmanager.core.business.order.model.OrderTotal> totals = new ArrayList<com.salesmanager.core.business.order.model.OrderTotal>();
		com.salesmanager.core.business.order.model.OrderTotal orderTotal = new com.salesmanager.core.business.order.model.OrderTotal();
        orderTotal.setModule(Constants.OT_TOTAL_MODULE_CODE);
        orderTotal.setOrderTotalType(OrderTotalType.TOTAL);
        orderTotal.setOrderTotalCode("order.total.total");
        orderTotal.setTitle(Constants.OT_TOTAL_MODULE_CODE);
        orderTotal.setText("order.total.total");
        orderTotal.setSortOrder(300);
        orderTotal.setValue(total);
        totals.add(orderTotal);
        orderTotalSummary.setTotals(totals);
		return orderTotalSummary;
	}
	
	
	@Override
	public EzybioOrder getEzybioOrder(Long orderId,Language language,long storeID) throws Exception {
		Order modelOrder;
		// TODO Auto-generated method stub
		boolean isAll = true;

		if(storeID==-1l){
			modelOrder = orderService.getById(orderId);
		}else{
			modelOrder = orderService.getByIdByStore(orderId, storeID);
			isAll = false;
		}
		if(modelOrder==null) {
			throw new Exception("Order not found with id " + orderId);
		}
		EzybioOrder ezybioOrder = this.popEzybio(modelOrder, language,isAll);
		CustomerAdress customerAdress = this.getCusmaddress(modelOrder,0);
		CustomerAdress invoiceAdress = this.getCusmaddress(modelOrder, 1);
		ezybioOrder.setCustomerAdress(customerAdress);
		ezybioOrder.setInvoiceAddress(invoiceAdress);
		ezybioOrder.setCustomerInvoice(modelOrder.getBillingInvoice());
		return ezybioOrder;
	}
	/**
	 * set customeraddress ,invoiceinfo
	 * @param modelOrder
	 * @param type 0:custmoeraddress 1:custmoerinvoice
	 * @return
	 */
	private CustomerAdress getCusmaddress(Order modelOrder,int type){
		CustomerAdress customerAddress = new CustomerAdress();
		if(type==0){
			customerAddress.setCity(modelOrder.getBillingCity());
			customerAddress.setCompany(modelOrder.getBillingCompany());
			customerAddress.setCountry(modelOrder.getBillingCountry().getName());
			customerAddress.setName(modelOrder.getBillingUserName());
			customerAddress.setPostCode(modelOrder.getBillingPostalCode());
			customerAddress.setTelephone(modelOrder.getBillingTelephone());
			customerAddress.setStreetAdress(modelOrder.getBillingAddress());
			customerAddress.setZone(modelOrder.getBillingZone().getName());
		}else{
			customerAddress.setCity(modelOrder.getIvoiceCity());
			customerAddress.setCompany(modelOrder.getIvoiceCompany());
			customerAddress.setCountry(modelOrder.getIvoiceCountry().getName());
			customerAddress.setName(modelOrder.getIvoiceUserName());
			customerAddress.setPostCode(modelOrder.getIvoicePostalCode());
			customerAddress.setTelephone(modelOrder.getIvoiceTelephone());
			customerAddress.setStreetAdress(modelOrder.getIvoiceAddress());
			customerAddress.setZone(modelOrder.getIvoiceZone().getName());
		}
		return  customerAddress;
		
	}



	@Override
	public ReadableOrderList getStoreReadableOrderList(OrderCriteria criteria,
			Language language) throws Exception {
		// TODO Auto-generated method stub
		OrderList orderList = orderService.listStoreByOrderCriteria(criteria);
    	
    	ReadableOrderList returnList = new ReadableOrderList();
    	List<Order> orders = orderList.getOrders();
    	if(CollectionUtils.isEmpty(orders)) {
			returnList.setTotal(0);
			returnList.setMessage("No results for store code " );
			return null;
		}
    	boolean isAll = true;
    	if(criteria.getStoreId() != -1l) isAll=false;
    	List<EzybioOrder> ezybioOrders = this.populateEzybio(orders,language,isAll);
    	if(ezybioOrders== null){
    		returnList.setTotal(0);
			returnList.setMessage("No results for store code " );
			return null;
    	}
    	returnList.setEzybioOrder(ezybioOrders);
    	returnList.setTotal(orderList.getTotalCount());
		return returnList;
	}



	@Override
	public com.salesmanager.web.entity.order.SubOrder getSubOrderById(
			Long orderId,Language language) throws Exception {
		// TODO Auto-generated method stub
		SubOrder subOrder = subOrderService.getSubOrderById(orderId);
		return  this.addSubOrder(subOrder, language);		
	}


	@Transactional(rollbackFor = { Exception.class }) 
	@Override
	public void spiltSubOrder(String itmes) throws Exception {		// TODO Auto-generated method stub
		
		try{
			String[] ids = itmes.split(",");
			com.salesmanager.core.business.order.model.SubOrder suborde= subOrderService.getSubOrderById(Long.parseLong(ids[0]));
			com.salesmanager.core.business.order.model.SubOrder newSub = new SubOrder();
			
			Order order = suborde.getOrder();
			//order = orderService.getById(order.getId());
			newSub.setMerchant(suborde.getMerchant());
			newSub.setOrder(order);
			newSub.setStatus(suborde.getStatus());
			
			
			Set<OrderProduct> prs = suborde.getOrderProducts();
			Set<OrderProduct> newPrs = new HashSet<OrderProduct>();
			for (int i=1;i<ids.length;i++){
				for(OrderProduct product:prs){
					if(product.getId()==Long.parseLong(ids[i])){
						//product.setSubOrder(newSub);
						newPrs.add(product);
						prs.remove(product);
						break;
					}
				}
			}
			if(newPrs.size()>0){
				subOrderService.saveOrUpdate(newSub);
				suborde.setOrderProducts(prs);
				newSub.setOrderProducts(newPrs);
				newSub.setTotal(this.getSuborderTotal(newSub));
				suborde.setTotal(this.getSuborderTotal(suborde));
				/**
				Set<SubOrder> subOrders=order.getSubOrders();
				for(SubOrder suborder:subOrders){
					if(suborder.getId()==suborde.getId()){
						subOrders.remove(suborder);
						subOrders.add(suborde);
					}
				}
				subOrders.add(newSub);
				order.setSubOrders(subOrders);*/
				subOrderService.saveOrUpdate(newSub);
				subOrderService.saveOrUpdate(suborde);
				for(OrderProduct product:newPrs ){
					product.setSubOrder(newSub);
					orderProductService.saveOrUpdate(product);
					
				}
			}
			//orderService.saveOrUpdate(order);
			//newSub.s
			
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error( "Error while split suborder" +e );
		}
	}
	
	private BigDecimal getSuborderTotal(com.salesmanager.core.business.order.model.SubOrder  suborder){
		BigDecimal totals =new BigDecimal(0);
		Set<OrderProduct> products = suborder.getOrderProducts();
		for(OrderProduct product:products){
			//totals.add(product.getPrices()*product.getPrices())
			totals = product.getPrice().multiply(new BigDecimal(product.getProductQuantity()));
		}
		
		return  totals;
	}



	@Override
	public int deliverySubOrder(String subOrderId,String dCode,String dNo) throws Exception {
		// TODO Auto-generated method stub
		try {
			SubOrder subOrder= subOrderService.getSubOrderById(Long.parseLong(subOrderId));
			subOrder.setDeliveryCode(dCode);
			subOrder.setDeliveryNumber(dNo);
			
			subOrder.setStatus(OrderStatus.SHIPPED);
			subOrderService.saveOrUpdate(subOrder);
			setOrderDelivery(subOrder.getOrder().getId());
		} catch (Exception e) {
			LOGGER.error( "Error while set  delivery suborder" +e );
			return 1;
		}
		
		return 0;
	}
	
	private void setOrderDelivery(long orderid){
		Order order = orderService.getOrder(orderid);
		Set<SubOrder> subs = order.getSubOrders();
		for(SubOrder sub : subs){
			if(sub.getStatus()!=OrderStatus.SHIPPED){
				return ;
			}
		}
		order.setStatus(OrderStatus.SHIPPED);
		try {
			orderService.saveOrUpdate(order);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			LOGGER.error( "Error while set  delivery order" +e );
		}
	}
}
