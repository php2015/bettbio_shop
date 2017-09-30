package com.salesmanager.core.business.order.service;

import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_INVALID_OPERATION;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NOT_ACTIVE_CUSTOMER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_ANY_PRODUCT;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_CUSTOMER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_DELIVERY;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_INVOICE_ADDRESS;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_ORDER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_PERMISSION;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_NO_SUBORDER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.CODE_WRONG_PARAM;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_INVALID_OPERATION;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NOT_ACTIVE_CUSTOMER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_ANY_PRODUCT;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_CUSTOMER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_DELIVERY;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_INVOICE_ADDRESS;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_ORDER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_PERMISSION;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_NO_SUBORDER;
import static com.salesmanager.core.business.order.service.OrderServiceV2Constants.MESSAGE_WRONG_PARAM;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductPricingContext;
import com.salesmanager.core.business.catalog.product.service.ProductPricingData;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.ProductSpecPricingData;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.price.BrandDiscountService;
import com.salesmanager.core.business.common.model.InvoiceInfo;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.exception.ServiceExceptionV2;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.order.dao.OrderDao;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductAttribute;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductPrice;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductService;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.system.service.SepcialConfigureService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.modules.sms.TextShortMessageService;
import com.salesmanager.core.utils.BaseDataUtils;
import com.salesmanager.core.utils.ProductPriceUtils;

@Service("orderServiceV2")
public class OrderServiceV2Impl extends SalesManagerEntityServiceImpl<Long, Order>implements OrderServiceV2 {

	private OrderDao orderDao;

	@Autowired
	private BrandDiscountService brandDiscountService;

	@Autowired
	private ProductAttributeService productAttributeService;

	@Autowired
	private MemberPointsService memberPointsService;

	@Autowired
	private SubOrderService subOrderService;

	@Autowired
	private OrderProductService orderProductService;

	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private TextShortMessageService smsService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SepcialConfigureService specialConfigService;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ZoneService zoneService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	public OrderServiceV2Impl(OrderDao genericDao) {
		super(genericDao);
		orderDao = genericDao;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceV2Impl.class);

	@Override
	public void commitOrder(OrderServiceV2Context orderContext, ShoppingCart cart, Long delieveryAddressId,
			Long invoicId, Long invoiceAddressId) throws ServiceExceptionV2, ServiceException {
		// 先校验
		verifyBeforeCommitOrder(orderContext, cart, delieveryAddressId, invoicId, invoiceAddressId);
		// 然后拆子订单
		splitSuborders(orderContext, cart);
		// 确定订单处理类型
		fillProcessType(orderContext);
		// 填写买家信息
		fillBuyerInfo(orderContext);
		// 填写发货信息
		fillDeliveryInfo(orderContext, delieveryAddressId, invoicId, invoiceAddressId);
		// 填写支付信息
		fillPaymentInfo(orderContext);
		// 接着算价
		calculateOrderInitialPrice(orderContext);
		// 最后是其他需要填写的信息
		afterOrderPrepared(orderContext);
		// 持久化
		create(orderContext.getOrder());

		// 订单成功后，计算客户的积分
		int points = calcCustomerPorint(orderContext);

		// 记录Log
		Order order = orderContext.getOrder();
		recordOrderHistory(order, null, order.getStatus(), String.format("创建订单, 获得%d积分。", points));

		sendOrderNotifies(orderContext, GlobalConstants.OrderAction_PlaceOrder, null, null);
	}

	private void sendOrderNotifies(OrderServiceV2Context orderContext, String actionName, Long subOrderId, Set<Long> changedSubOrderIds) {
		try {
			List<OrderNotifyTask> notifyTasks = createNotifyTasks(orderContext, actionName, subOrderId, changedSubOrderIds);
			if (notifyTasks == null) {
				return;
			}
			for (OrderNotifyTask task : notifyTasks) {
				if (task.getReceiverPhone() != null) {
					smsService.sendNotifySms(task.getReceiverPhone(), task.getSmsTemplateId(), task.getSmsParameters());
					// ignore the result. Whatever success or not, the main
					// process will not be affected.
				}
				if (task.getReceiverEmail() != null) {
					Email email = prepareEmail(orderContext, task);
					try {
						emailService.sendHtmlEmail(email);
					} catch (Exception e) {
						e.printStackTrace();
						// ignore the result. Whatever success or not, the main
						// process will not be affected.
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private List<OrderNotifyTask> createNotifyTasks(OrderServiceV2Context orderContext, String actionName,
			Long subOrderId, Set<Long> changedSubOrderIds) throws Exception {
		List<OrderNotifyTask> tasks = new ArrayList<OrderNotifyTask>();
		// 展会模式特别控制
		boolean sendOrderEmail = specialConfigService.getBooleanCfg(SepcialConfigureService.KEY_SEND_EMAIL_TO_MERCHANT_WHEN_SUBMIT_ORDER);
		if (!sendOrderEmail){
			if (GlobalConstants.OrderAction_PlaceOrder.equals(actionName)
					|| GlobalConstants.OrderAction_Close.equals(actionName)
					|| GlobalConstants.OrderAction_Cancel.equals(actionName)){
				// 如果设定了不发邮件，只有下单/取消/关闭不发通知。其他操作，例如改价/确认订单，都正常发通知
				return tasks;
			}
		}
		boolean useCurrentSales = specialConfigService.getBooleanCfg(SepcialConfigureService.KEY_USE_CUSTOMER_SALES_DO_ORDER_NOTIFY);
		String purchasingAgentEmail = specialConfigService.getStringCfg(SepcialConfigureService.KEY_SYSTEM_PURCHASING_AGENT_EMAIL);
		String purchasingAgentMobile = specialConfigService.getStringCfg(SepcialConfigureService.KEY_SYSTEM_PURCHASING_AGENT_Mobile);
		Order order = orderContext.getOrder();
		Map<String,Zone> zones = zoneService.getZones(orderContext.getLanguage());
		Map<String,Country> countries = countryService.getCountriesMap(orderContext.getLanguage());
		assert (order != null);
		Customer customer = customerService.getById(order.getCustomerId());
		if (GlobalConstants.OrderAction_PlaceOrder.equals(actionName)) {
			OrderServiceV2Utils.addPlaceOrderNotifyTask(tasks, order, customer, useCurrentSales, purchasingAgentEmail,
					purchasingAgentMobile, zones, countries);
		} else if (GlobalConstants.OrderAction_AdjustPrice.equals(actionName)) {
			OrderServiceV2Utils.addAdjustPriceNotifyTask(tasks, order, customer, useCurrentSales, purchasingAgentEmail,
					purchasingAgentMobile, zones, countries, orderContext.isAdminUser(), orderContext, changedSubOrderIds);
		} else if (GlobalConstants.OrderAction_ConfirmOrder.equals(actionName)){
			OrderServiceV2Utils.addConfirmOrderNotifyTask(tasks, order, subOrderId, customer, useCurrentSales, purchasingAgentEmail,
					purchasingAgentMobile, zones, countries, orderContext);
		} else {
			// 剩下都是状态变化
			OrderServiceV2Utils.addStatusChangeNotifyTask(tasks, actionName, order, subOrderId, customer, useCurrentSales, purchasingAgentEmail,
					purchasingAgentMobile, zones, countries, orderContext);
		}
		// TODO
		
		
		return tasks;
	}

	private Email prepareEmail(OrderServiceV2Context orderContext, OrderNotifyTask task) {
		Email email = new Email();
        email.setFrom(task.getEmailSenderName());
        email.setFromEmail(task.getEmailSenderAddress());
        email.setSubject(task.getEmailTitle());
        email.setTo(task.getReceiverEmail());
        
        email.setTemplateName(task.getEmailTemplateName());
        email.setTemplateTokens((Map)task.getEmailParameters());
		return email;
	}

	private int calcCustomerPorint(OrderServiceV2Context orderContext) throws ServiceException {
		int earnPoints = 0;
		MemberPoints memberPoints = new MemberPoints();
		Order order = orderContext.getOrder();
		Integer temp_point = calcMemberPointByOrderTotal(order);

		Customer customer = orderContext.getCustomer();
		memberPoints.setType(Constants.RATIO_BUTILE);
		memberPoints.setValue(String.valueOf(temp_point));
		// set left
		memberPoints.setLtfePoint(0L);
		memberPoints.setUpdateDate(new Date());
		memberPoints.setStatas(GlobalConstants.MEMBER_POINT_STATE_PREPARED);
		memberPoints.setCustomer(customer);
		memberPoints.setOrderId(order.getId());
		memberPointsService.save(memberPoints);
		earnPoints += temp_point;

		if (getOrderByCount(customer.getId()) == 1) {
			// 首次下单，赠送积分，更新用户信息和新增积分记录
			MemberPoints mb2 = new MemberPoints();
			int points;
			try {
				points = Integer.parseInt(getPoint(Constants.FIRST_SCORE));
			} catch (Exception e) {
				points = 0;
			}

			// points+=temp_point;
			mb2.setType(Constants.FIRST_SCORE);
			mb2.setUpdateDate(new Date());
			mb2.setStatas(GlobalConstants.MEMBER_POINT_STATE_PREPARED);
			mb2.setValue(String.valueOf(points));
			// set left
			mb2.setLtfePoint((long) points);
			earnPoints += points;
			mb2.setCustomer(customer);
			mb2.setOrderId(order.getId());
			memberPointsService.save(mb2);
		}
		return earnPoints;
	}

	private Integer calcMemberPointByOrderTotal(Order order) {
		int points = 0;
		try {
			points = Integer.parseInt(getPoint(Constants.RATIO_BUTILE));
		} catch (Exception e) {
			points = 10;
		}

		int orderTotal = order.getTotal().intValue();
		Integer temp_point = (orderTotal + points - 1) / points;
		return temp_point;
	}

	@Override
	public int getOrderByCount(Long customerId) {
		return orderDao.getOrderByCount(customerId);
	}

	private String getPoint(String type) {
		return String.valueOf(BaseDataUtils.addCoustomerByOrader(type));
	}

	private void recordOrderHistory(Order order, Long subOrderId, OrderStatus status, String comments)
			throws ServiceException {
		OrderStatusHistory statusHistory = new OrderStatusHistory();
		statusHistory.setOrder(order);
		statusHistory.setSubOrderId(subOrderId);
		statusHistory.setStatus(status);
		statusHistory.setComments(comments);
		statusHistory.setDateAdded(new Date());

		Set<OrderStatusHistory> histrSet = order.getOrderHistory();
		if (histrSet == null) {
			histrSet = new HashSet<OrderStatusHistory>();
			order.setOrderHistory(histrSet);
		}
		histrSet.add(statusHistory);
		order.setLastModified(new Date());
		update(order);
	}

	private void fillProcessType(OrderServiceV2Context orderContext) {
		Order order = orderContext.getOrder();
		Customer customer = orderContext.getCustomer();
		if (customer.getAccountType() == null
				|| GlobalConstants.BuyerType_ResearchWorker.equals(customer.getAccountType())) {
			order.setProcessType(GlobalConstants.OrderProcessType_ShipFirst);
		} else {
			order.setProcessType(GlobalConstants.OrderProcessType_PayFirst);
		}
	}

	private void fillBuyerInfo(OrderServiceV2Context orderContext) {
		Order order = orderContext.getOrder();
		Customer customer = orderContext.getCustomer();
		order.setCustomerId(customer.getId());
		order.setCustomerEmailAddress(customer.getEmailAddress());
	}

	private void afterOrderPrepared(OrderServiceV2Context orderContext) {
		Order order = orderContext.getOrder();
		Set<SubOrder> subOrders = order.getSubOrders();
		for (SubOrder subOrder : subOrders) {
			for (OrderProduct item : subOrder.getOrderProducts()) {
				item.setSubOrder(subOrder);
			}
			subOrder.setOrder(order);
		}
	}

	private void fillPaymentInfo(OrderServiceV2Context orderContext) {
		// 目前支付类型固定地，所以没得参数
		Order order = orderContext.getOrder();
		order.setPaymentModuleCode("moneyorder");
		order.setPaymentType(PaymentType.MONEYORDER);
		order.setCurrency(orderContext.getStore().getCurrency());
	}

	private void fillDeliveryInfo(OrderServiceV2Context orderContext, Long delieveryAddressId, Long invoicId,
			Long invoiceAddressId) {
		Order order = orderContext.getOrder();
		Customer customer = orderContext.getCustomer();
		order.setLocale(orderContext.getLocale());
		Set<CustomerAddress> addresss = customer.getAddresss();
		assert(order != null);
		assert(customer != null);
		assert(delieveryAddressId != null);
		for (CustomerAddress address : addresss) {
			if (address.getId().equals(delieveryAddressId)) {
				order.setBillingAddress(address.getAddress());
				order.setBillingCity(address.getCity());
				order.setBillingCompany(address.getCompany());
				order.setBillingCountry(address.getCountry());
				order.setBillingPostalCode(address.getPostalCode());
				order.setBillingTelephone(address.getTelephone());
				order.setBillingUserName(address.getName());
				order.setBillingZone(address.getZone());
				break;
			}
		}
		if (invoicId == null) {
			return;
		}
		Set<CustomerInvoice> invoices = customer.getInvoices();
		for (CustomerInvoice invoice : invoices) {
			if (invoice.getId().equals(invoicId)) {
				InvoiceInfo invoiceInfo = new InvoiceInfo();
				invoiceInfo.setBankAccount(invoice.getBankAccount());
				invoiceInfo.setBankName(invoice.getBankName());
				invoiceInfo.setCompany(invoice.getCompany());
				invoiceInfo.setCompanyAddress(invoice.getCompanyAddress());
				invoiceInfo.setCompanyTelephone(invoice.getCompanyTelephone());
				invoiceInfo.setTaxpayerNumber(invoice.getTaxpayerNumber());
				invoiceInfo.setType(invoice.getType());
				order.setBillingInvoice(invoiceInfo);
				break;
			}
		}

		for (CustomerAddress address : addresss) {
			if (address.getId().equals(invoiceAddressId)) {
				order.setIvoiceAddress(address.getAddress());
				order.setIvoiceCity(address.getCity());
				order.setIvoiceCompany(address.getCompany());
				order.setIvoiceCountry(address.getCountry());
				order.setIvoicePostalCode(address.getPostalCode());
				order.setIvoiceTelephone(address.getTelephone());
				order.setIvoiceUserName(address.getName());
				order.setIvoiceZone(address.getZone());
				break;
			}
		}
	}

	private void calculateOrderInitialPrice(OrderServiceV2Context orderContext) {
		// 算价目前只和产品的折扣有关
		Order order = orderContext.getOrder();
		assert(order != null);
		Set<SubOrder> subOrders = order.getSubOrders();
		assert(subOrders != null);
		Customer customer = orderContext.getCustomer();
		
		// 首先拿到产品的价格数据
		List<Long> productIds = new ArrayList<Long>();
		for (SubOrder subOrder : subOrders) {
			for (OrderProduct item : subOrder.getOrderProducts()) {
				productIds.add(item.getProductID());
			}
		}
		ProductPricingContext priceCtx = new ProductPricingContext();
		priceCtx.setCustomer(customer);
		List<ProductPricingData> prodPriceDatas = pricingService.calculateProductListPrice(priceCtx, productIds);
		
		BigDecimal orderTotal = new BigDecimal(0.0);
		
		for (SubOrder subOrder : subOrders) {
			BigDecimal subOrderTotal = new BigDecimal(0.0);
			for (OrderProduct item : subOrder.getOrderProducts()) {
				ProductPricingData prodPriceData = ProductPriceUtils.getProductPriceData(prodPriceDatas, item.getProductID());
				BigDecimal finalPrice = item.getPrice();
				if (prodPriceData != null) {
					ProductSpecPricingData specPrice = ProductPriceUtils.getProductSpecPrice(prodPriceData, item.getProductSpecID(), item.getSpecifications());
					if (specPrice != null){
						if (specPrice.getFinalPrice() != null){
							finalPrice = specPrice.getFinalPrice();
						}
					}
				}

				BigDecimal itemTotal = new BigDecimal(finalPrice.doubleValue() * item.getProductQuantity());
				item.setPrice(finalPrice); // 条目单价
				item.setOneTimeCharge(itemTotal); // 条目总价。 这个名字有点困惑。 后续需要关注
				subOrderTotal = subOrderTotal.add(itemTotal);
			}
			subOrder.setTotal(subOrderTotal);
			orderTotal = orderTotal.add(subOrderTotal);
		}
		order.setTotal(orderTotal);
	}

	

	

	private void splitSuborders(OrderServiceV2Context orderContext, ShoppingCart cart) {
		Set<ShoppingCartItem> items = cart.getLineItems();
		assert(items != null); // 前面verifyBeforeCommitOrder已经验证过。
		Customer customer = orderContext.getCustomer();
		
		Map<Long, SubOrder> subOrders = new HashMap<Long, SubOrder>();
		for (ShoppingCartItem item : items) {
			if (!item.isSelected()) {
				continue;
			}
			Product product = item.getProduct();
			long productStoreId = product.getMerchantStore().getId();
			SubOrder subOrder = subOrders.get(productStoreId);
			if (subOrder == null) {
				subOrder = new SubOrder();
				subOrder.setMerchant(product.getMerchantStore());
				subOrder.setStoreName(product.getMerchantStore().getStorename());
				subOrder.setLastModified(new Date());
				subOrder.setStatus(OrderStatus.ORDERED);
				subOrder.setRelatedSalesEmail(customer.getRelatedSalesEmail());
				subOrder.setRelatedSalesMobile(customer.getRelatedSalesMobile());
				subOrders.put(productStoreId, subOrder);
			}
			Set<OrderProduct> orderProducts = subOrder.getOrderProducts();
			if (orderProducts == null) {
				orderProducts = new HashSet<OrderProduct>();
				subOrder.setOrderProducts(orderProducts);
			}
			OrderProduct orderProduct = prepareOrderProduct(item, orderContext.getLanguage());
			orderProducts.add(orderProduct);
		}

		Order order = orderContext.getOrder();
		if (order == null) {
			order = new Order();
			orderContext.setOrder(order);
			order.setDatePurchased(new Date());
			order.setStatus(OrderStatus.ORDERED);
		}
		order.setSubOrders(new HashSet<SubOrder>(subOrders.values()));
	}

	private Set<OrderProductPrice> getOrderPrice(ProductPrice price, OrderProduct orderProduct) {
		Set<OrderProductPrice> orderPriceSet = new HashSet<OrderProductPrice>();
		OrderProductPrice orderProductPrice = new OrderProductPrice();
		orderProductPrice.setProductPrice(price.getFinalPrice());
		orderProductPrice.setProductPriceCode(price.getCode());
		orderProductPrice.setProductPriceName(price.getProductPriceDescription().getName());
		if (price.getProductPriceSpecialAmount() != null) {
			orderProductPrice.setProductPriceSpecial(price.getProductPriceSpecialAmount());
			orderProductPrice.setProductPriceSpecialStartDate(price.getProductPriceSpecialStartDate());
			orderProductPrice.setProductPriceSpecialStartDate(price.getProductPriceSpecialEndDate());
		}
		orderProductPrice.setOrderProduct(orderProduct);
		orderPriceSet.add(orderProductPrice);
		return orderPriceSet;
	}

	private OrderProduct prepareOrderProduct(ShoppingCartItem source, Language language) {
		OrderProduct target = new OrderProduct();
		target.setOneTimeCharge(source.getSubTotal());
		target.setPrice(source.getItemPrice());
		target.setProductName(source.getProduct().getDescriptions().iterator().next().getName());
		target.setProductEnName(source.getProduct().getDescriptions().iterator().next().getEnName());
		target.setProductCode(source.getProduct().getCode());

		target.setProductQuantity(source.getQuantity());
		target.setSku(source.getProduct().getSku());
		target.setProductID(source.getProductId());
		target.setSpecifications(source.getSpec());
		target.setProductSpecID(source.getProductPrice().getId());
		target.setPrices(this.getOrderPrice(source.getProductPrice(), target));
		
		
		// OrderProductAttribute
		Set<ShoppingCartAttributeItem> attributeItems = source.getAttributes();
		if (!CollectionUtils.isEmpty(attributeItems)) {
			Set<OrderProductAttribute> attributes = new HashSet<OrderProductAttribute>();
			for (ShoppingCartAttributeItem attribute : attributeItems) {
				OrderProductAttribute orderProductAttribute = new OrderProductAttribute();
				orderProductAttribute.setOrderProduct(target);
				Long id = attribute.getProductAttributeId();
				ProductAttribute attr = productAttributeService.getById(id);
				if (attr == null) {
					continue;
				}

				orderProductAttribute.setProductAttributeIsFree(attr.getProductAttributeIsFree());
				orderProductAttribute
						.setProductAttributeName(attr.getProductOption().getDescriptionsSettoList().get(0).getName());
				orderProductAttribute.setProductAttributeValueName(
						attr.getProductOptionValue().getDescriptionsSettoList().get(0).getName());
				orderProductAttribute.setProductAttributePrice(attr.getProductAttributePrice());
				orderProductAttribute.setProductAttributeWeight(attr.getProductAttributeWeight());
				orderProductAttribute.setProductOptionId(attr.getProductOption().getId());
				orderProductAttribute.setProductOptionValueId(attr.getProductOptionValue().getId());
				attributes.add(orderProductAttribute);
			}
			target.setOrderAttributes(attributes);
			target.setProductImageUrl(makeProductImageUrl(target));
		}
		return target;
	}

	private void verifyBeforeCommitOrder(OrderServiceV2Context orderContext, ShoppingCart cart, Long delieveryAddressId,
			Long invoicId, Long invoiceAddressId) throws ServiceExceptionV2 {
		// Customer must be valid
		verifyCustomerCanCommitOrder(orderContext);
		// Store should be valid
		verifyStoreCanSaleOrder(orderContext);
		// all products should be legal to sale
		verifyProducts(orderContext, cart);
		// verify customer provide enough delivery information
		verifyDeliveryInfo(orderContext, delieveryAddressId, invoicId, invoiceAddressId);
	}

	private void verifyDeliveryInfo(OrderServiceV2Context orderContext, Long delieveryAddressId, Long invoicId,
			Long invoiceAddressId) throws ServiceExceptionV2 {
		Customer customer = orderContext.getCustomer();
		assert(customer != null);
		if (delieveryAddressId == null) {
			throw new ServiceExceptionV2(CODE_NO_DELIVERY, MESSAGE_NO_DELIVERY);
		}
		if (invoicId == null) {
			return; // 可以不要发票信息
		}
		if (invoiceAddressId == null) {
			throw new ServiceExceptionV2(CODE_NO_INVOICE_ADDRESS, MESSAGE_NO_INVOICE_ADDRESS);
		}
	}

	private void verifyProducts(OrderServiceV2Context orderContext, ShoppingCart cart) throws ServiceExceptionV2 {
		Set<ShoppingCartItem> items = cart.getLineItems();
		if (items == null || items.isEmpty()) {
			throw new ServiceExceptionV2(CODE_NO_ANY_PRODUCT, MESSAGE_NO_ANY_PRODUCT);
		}

		for (ShoppingCartItem item : items) {
			if (item.isSelected()) {
				// 目前没有产品销售限制。 理论上是收货地和发货地的一些法律法规。可能在产品属性上有表现。
				// 所以只要有产品被选中就可以了。
				return;
			}
		}
		throw new ServiceExceptionV2(CODE_NO_ANY_PRODUCT, MESSAGE_NO_ANY_PRODUCT);
	}

	private void verifyStoreCanSaleOrder(OrderServiceV2Context orderContext) throws ServiceExceptionV2 {
		return; // 目前不对商家做校验
	}

	private void verifyCustomerCanCommitOrder(OrderServiceV2Context orderContext) throws ServiceExceptionV2 {
		// 只要用户状态正常即可
		Customer customer = orderContext.getCustomer();
		if (customer == null) {
			throw new ServiceExceptionV2(CODE_NO_CUSTOMER, MESSAGE_NO_CUSTOMER);
		}
		// 这里换用accountState来校验。不在用customer.getAnonymouse了
		if (!GlobalConstants.AccountState_Certified.equals(customer.getAccountState())) {
			throw new ServiceExceptionV2(CODE_NOT_ACTIVE_CUSTOMER, MESSAGE_NOT_ACTIVE_CUSTOMER, customer.getId(),
					customer.getNick(), customer.getAccountState());
		}
	}

	/**
	 * 根据订单数据计算有哪些可以执行的操作
	 * 
	 * @param orderContext
	 * @return 可执行的操作列表. null 表示没有任何权限。 空列表：表示至少有读取权限，但是当前无可用操作。
	 * 
	 *         决定订单有哪些有效操作的因素包括：是哪个用户，是哪个商家，当前什么状态，订单是什么处理类型。
	 * 
	 */
	@Override
	public List<String> getActionList(OrderServiceV2Context orderContext, SubOrder subOrder, boolean isAdminUser) {
		Order order = orderContext.getOrder();
		Customer customer = orderContext.getCustomer();
		User user = orderContext.getUser();

		// 调用这个方法前，需要保证以下断言
		assert(order != null);
		assert(subOrder != null);
		assert(customer != null || user != null);

		// 然后检查所有权
		if (customer != null) {
			if (!order.getCustomerId().equals(customer.getId())) {
				customer = null;
			}
		}
		if (user != null && !isAdminUser) {
			if (!subOrder.getMerchant().getId().equals(user.getMerchantStore().getId())) {
				user = null;
			}
		}
		if (customer == null && user == null) {
			return null;
		}
		OrderStatus orderStatus = subOrder.getStatus();
		String processType = order.getProcessType();
		return OrderServiceV2Utils.queryValidActions(processType, orderStatus, customer, user, isAdminUser);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void spiltSubOrderIntoNewOne(String itmes) { // TODO Auto-generated method stub

		try {
			String[] ids = itmes.split(",");
			if (ids == null || ids.length == 0) {
				return;
			}
			SubOrder suborde = subOrderService.getSubOrderById(Long.parseLong(ids[0]));

			// prepare a new empty suborder
			SubOrder newSub = new SubOrder();
			Order order = suborde.getOrder();
			newSub.setMerchant(suborde.getMerchant());
			newSub.setStoreName(suborde.getStoreName());
			newSub.setOrder(order);
			newSub.setStatus(suborde.getStatus());
			newSub.setRelatedSalesEmail(suborde.getRelatedSalesEmail());
			newSub.setRelatedSalesMobile(suborde.getRelatedSalesMobile());

			Set<OrderProduct> prs = suborde.getOrderProducts();
			Set<OrderProduct> newPrs = new HashSet<OrderProduct>();
			for (int i = 1; i < ids.length; i++) {
				for (OrderProduct product : prs) {
					if (product.getId() == Long.parseLong(ids[i])) {
						// product.setSubOrder(newSub);
						newPrs.add(product);
						prs.remove(product);
						break;
					}
				}
			}

			if (newPrs.size() > 0) {
				subOrderService.saveOrUpdate(newSub);
				suborde.setOrderProducts(prs);
				newSub.setOrderProducts(newPrs);
				repriceSplitedSubOrders(suborde, newSub);

				subOrderService.saveOrUpdate(newSub);
				subOrderService.saveOrUpdate(suborde);
				for (OrderProduct product : newPrs) {
					product.setSubOrder(newSub);
					orderProductService.saveOrUpdate(product);

				}
			}
			recordOrderHistory(order, newSub.getId(), newSub.getStatus(), "人工拆分订单");
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("Error while split suborder" + e);
		}
	}

	/** 拆单以后的价格计算.
	 * 
	 * @param suborde
	 * @param newSub
	 */
	private void repriceSplitedSubOrders(SubOrder suborde, SubOrder newSub) {
		// 首先保存原来的subTotal
		BigDecimal oldTotal = suborde.getFinalTotal(); // 只需要记录原调整过后的价格即可。
		Object[] returnDataOrg = new Object[3];
		Object[] returnDataNew = new Object[3];
		checkSubOrderItems(suborde, returnDataOrg);
		checkSubOrderItems(newSub, returnDataNew);
		
		boolean newItemsAdjusted = (boolean) returnDataNew[0];
		BigDecimal newItemsOrgTotal = (BigDecimal) returnDataNew[1];
		BigDecimal newItemsFinalTotal = (BigDecimal) returnDataNew[2];
		
		boolean oldItemsAdjusted = (boolean) returnDataOrg[0];
		BigDecimal oldItemsOrgTotal = (BigDecimal) returnDataOrg[1];
		BigDecimal oldItemsFinalTotal = (BigDecimal) returnDataOrg[2];
		
		suborde.setTotal(oldItemsOrgTotal);
		newSub.setTotal(newItemsOrgTotal);
		newSub.setFinalTotal(null);
		suborde.setFinalTotal(null);
		// 如果子订单未调价，但是有商品调过价
		if (newItemsAdjusted){
			if (newItemsOrgTotal.equals(newItemsFinalTotal)){
				newSub.setFinalTotal(null);
			}else{
				newSub.setFinalTotal(newItemsFinalTotal);
			}
		}
		if (oldItemsAdjusted){
			if (oldItemsOrgTotal.equals(oldItemsFinalTotal)){
				suborde.setFinalTotal(null);
			}else{
				suborde.setFinalTotal(oldItemsFinalTotal);
			}
		}
		if (oldTotal != null && !oldItemsAdjusted && !newItemsAdjusted){
			double factor = newItemsOrgTotal.add(oldItemsOrgTotal).doubleValue();
			factor = oldTotal.doubleValue() / factor;
			suborde.setFinalTotal(new BigDecimal(oldItemsOrgTotal.doubleValue()*factor));
			newSub.setFinalTotal(new BigDecimal(newItemsOrgTotal.doubleValue()*factor));
		}
	}

	private void checkSubOrderItems(SubOrder suborde, Object[] returnDataOrg) {
		BigDecimal finalTotal = new BigDecimal(0);
		BigDecimal orgTotal = new BigDecimal(0);
		boolean itemAdjusted = false;
		for(OrderProduct oprod : suborde.getOrderProducts()){
			orgTotal = orgTotal.add(oprod.getOneTimeCharge());
			if (oprod.getFinalTotal() != null){
				itemAdjusted = true;
				finalTotal = finalTotal.add(oprod.getFinalTotal());
			}else{
				finalTotal = finalTotal.add(oprod.getOneTimeCharge());
			}
		}
		returnDataOrg[0] = itemAdjusted;
		returnDataOrg[1] = orgTotal;
		returnDataOrg[2] = finalTotal;
	}

	@Override
	public OrderList listStoreByOrderCriteria(OrderCriteria criteria) {
		return orderDao.listStoreByOrderCriteria(criteria);
	}

	@Override
	public void fillPricingInfomation(Order order) {
		Set<SubOrder> subOrders = order.getSubOrders();
		if (subOrders == null) {
			return;
		}
		boolean orderPriceAdjusted = false;
		for (SubOrder subOrder : subOrders) {
			// subOrder level price type check
			Set<OrderProduct> items = subOrder.getOrderProducts();
			BigDecimal finalTotal = subOrder.getFinalTotal();

			// items level price type check
			boolean itemsAdjused = false;
			if (items != null) {
				itemsAdjused = OrderServiceV2Utils.checkItemLevelPriceType(items);
			}

			if (itemsAdjused) {
				orderPriceAdjusted = true;
				subOrder.setPriceType(GlobalConstants.OrderPriceType.CALCULATED);
			} else if (finalTotal != null) {
				orderPriceAdjusted = true;
				subOrder.setPriceType(GlobalConstants.OrderPriceType.ADJUSTED);
			} else {
				subOrder.setPriceType(GlobalConstants.OrderPriceType.NORMAL);
			}
			if (finalTotal == null) {
				subOrder.setNominalTotal(subOrder.getTotal());
			} else {
				subOrder.setNominalTotal(finalTotal);

			}
		}
		if (orderPriceAdjusted) {
			order.setPriceType(GlobalConstants.OrderPriceType.CALCULATED);
		} else {
			order.setPriceType(GlobalConstants.OrderPriceType.NORMAL);
		}
	}

	@Override
	public Order getByIdByStore(Long id, Long storeID) {
		Order order = orderDao.getByIdByStore(id, storeID);
		if (order != null) {
			fillPricingInfomation(order);
		}
		return order;
	}

	@Override
	public Order getById(Long id) {
		Order order = orderDao.getById(id);
		if (order != null) {
			fillPricingInfomation(order);
			//fillProductImage(order);
		}
		return order;
	}

	private void fillProductImage(Order order) {
		for(SubOrder subOrder : order.getSubOrders()){
			for(OrderProduct prod : subOrder.getOrderProducts()){
				if (prod.getProductImageUrl() == null){
					prod.setProductImageUrl(makeProductImageUrl(prod));
				}
			}
		}
	}

	@Override
	public void adjustPrice(OrderServiceV2Context orderCtx, OrderPriceAdjustment priceData) throws ServiceExceptionV2, ServiceException {
		Order order = orderDao.getById(priceData.getOid());
		orderCtx.setOrder(order);
		List<Object[]> logs = new ArrayList<Object[]>();
		StringBuilder sbLog = new StringBuilder();
		if (order == null) {
			throw new ServiceExceptionV2(CODE_NO_ORDER, MESSAGE_NO_ORDER, priceData.getOid());
		}
		if (priceData.getSuborder() == null) {
			throw new ServiceExceptionV2(CODE_WRONG_PARAM, MESSAGE_WRONG_PARAM, priceData.getOid());
		}
		Map<Long, BigDecimal> oldPrices = new HashMap();
		for (Long subOid : priceData.getSuborder().keySet()) {
			SubOrder subOrder = OrderServiceV2Utils.getSubOrderById(order.getSubOrders(), subOid);
			if (subOrder == null) {
				continue; // 找不到就跳过，处理后续的
			}
			SubOrderPriceAdjustment subOrderPriceData = priceData.getSuborder().get(subOid);
			oldPrices.put(subOrder.getId(), subOrder.getFinalTotal()==null?subOrder.getTotal():subOrder.getFinalTotal());
			// 先处理看是不是子订单级别的直接修改
			if (subOrderPriceData.getPriceType().equals(GlobalConstants.OrderPriceType.ADJUSTED)) {
				logs.add(new Object[] { subOid, subOrder.getStatus(), String.format("子订单合计由%3.2f调整为%3.2f",
						subOrder.getTotal().doubleValue(), subOrderPriceData.getValue()) });
				// 直接改子订单总额
				subOrder.setFinalTotal(new BigDecimal(subOrderPriceData.getValue()));
				// 然后清除所有条目的修改价格
				if (subOrder.getOrderProducts() != null) {
					for (OrderProduct item : subOrder.getOrderProducts()) {
						item.setFinalPrice(null);
						item.setFinalTotal(null);
					}
				}
				continue;
			}

			// 如果是商品级别的调整，需要重算价格
			if (subOrderPriceData.getItems() == null) {
				continue;
			}
			// 先把每个条目都调整了
			boolean adjusted = false;
			for (Long itemId : subOrderPriceData.getItems().keySet()) {
				OrderProduct oprod = getOrderProductById(subOrder, itemId);
				if (oprod == null) {
					continue;
				}
				ItemPriceAdjustment itemPriceData = subOrderPriceData.getItems().get(itemId);
				switch (itemPriceData.getPriceType()) {
				case CALCULATED:
					adjusted = true;
					logs.add(new Object[] { subOid, subOrder.getStatus(),
							String.format("商品%s[%s]单价%s调整为%3.2f", oprod.getProductName(), oprod.getSpecifications(),
									oprod.getFinalPrice() == null ? "" : "由" + oprod.getFinalPrice().doubleValue(),
									itemPriceData.getValue()) });
					oprod.setFinalPrice(new BigDecimal(itemPriceData.getValue()));
					oprod.setFinalTotal(oprod.getFinalPrice().multiply(new BigDecimal(oprod.getProductQuantity())));
					break;
				case ADJUSTED:
					adjusted = true;
					logs.add(new Object[] { subOid, subOrder.getStatus(),
							String.format("商品%s[%s]合计金额%s调整为%3.2f", oprod.getProductName(), oprod.getSpecifications(),

							oprod.getFinalTotal() == null ? "" : "由" + oprod.getFinalTotal().doubleValue(),
									itemPriceData.getValue()) });
					oprod.setFinalPrice(null);
					oprod.setFinalTotal(new BigDecimal(itemPriceData.getValue()));
					break;
				default:
					logs.add(new Object[] { subOid, subOrder.getStatus(),
							String.format("商品%s[%s]价格调整被取消", oprod.getProductName(), oprod.getSpecifications()) });
					oprod.setFinalPrice(null);
					oprod.setFinalTotal(null);
					break;
				}
			}
			// 然后重新计算子订单价格。 因为没有调整过的商品还没有处理过
			BigDecimal subTotal = new BigDecimal(0);
			for (OrderProduct oprod : subOrder.getOrderProducts()) {
				if (oprod.getFinalTotal() != null) {
					subTotal = subTotal.add(oprod.getFinalTotal());
				} else {
					subTotal = subTotal.add(oprod.getOneTimeCharge());
				}
			}
			if (!adjusted && subOrder.getTotal().equals(subTotal)) {
				subOrder.setFinalTotal(null);
			} else {
				subOrder.setFinalTotal(subTotal);
			}

		}

		recalculateOrderPrice(order);
		batchRecordLog(order, logs);
		for (SubOrder subOrder : order.getSubOrders()) {
			if (!oldPrices.containsKey(subOrder.getId())){
				continue;
			}
			BigDecimal total = subOrder.getFinalTotal();
			if (total == null){
				total = subOrder.getTotal();
			}
			if (total.equals(oldPrices.get(subOrder.getId()))){
				oldPrices.remove(subOrder.getId());
			}
		}
		sendOrderNotifies(orderCtx, GlobalConstants.OrderAction_AdjustPrice, null, oldPrices.keySet());
	}

	private void batchRecordLog(Order order, List<Object[]> logs) throws ServiceException {
		Set<OrderStatusHistory> histrSet = order.getOrderHistory();
		if (histrSet == null) {
			histrSet = new HashSet<OrderStatusHistory>();
			order.setOrderHistory(histrSet);
		}

		for (Object[] logData : logs) {
			OrderStatusHistory statusHistory = new OrderStatusHistory();
			statusHistory.setOrder(order);
			statusHistory.setSubOrderId((Long) logData[0]);
			statusHistory.setStatus((OrderStatus) logData[1]);
			statusHistory.setComments((String) logData[2]);
			statusHistory.setDateAdded(new Date());
			histrSet.add(statusHistory);
		}
		order.setLastModified(new Date());
		update(order);
	}

	private void recalculateOrderPrice(Order order) {
		BigDecimal total = new BigDecimal(0);
		for (SubOrder subOrder : order.getSubOrders()) {
			if (subOrder.getFinalTotal() != null) {
				total = total.add(subOrder.getFinalTotal());
			} else {
				total = total.add(subOrder.getTotal());
			}
		}
		order.setTotal(total);
	}

	private OrderProduct getOrderProductById(SubOrder subOrder, Long itemId) {
		if (subOrder.getOrderProducts() == null) {
			return null;
		}
		for (OrderProduct item : subOrder.getOrderProducts()) {
			if (item.getId().equals(itemId)) {
				return item;
			}
		}
		return null;
	}

	private static Map<String, String> actionZhName = new HashMap<String, String>();
	static {
		actionZhName.put(GlobalConstants.OrderAction_Cancel, "订单被取消");
		actionZhName.put(GlobalConstants.OrderAction_UploadPayProof, "买家付款");
		actionZhName.put(GlobalConstants.OrderAction_ConfirmPay, "卖家确认收到货款");
		actionZhName.put(GlobalConstants.OrderAction_Ship, "卖家发货");
		actionZhName.put(GlobalConstants.OrderAction_ConfirmReceipt, "买家确认收货");
		actionZhName.put(GlobalConstants.OrderAction_Close, "订单被关闭");
		actionZhName.put(GlobalConstants.OrderAction_Reset, "订单被重置");
		actionZhName.put(GlobalConstants.OrderAction_Review, "订单被评价");
		actionZhName.put(GlobalConstants.OrderAction_ConfirmOrder, "订单审核通过");
	}
	@Override
	public void onSuborderStatusAction(OrderServiceV2Context orderContext, Long soid, String actionName,boolean isAdminUser)
			throws ServiceExceptionV2, ServiceException {
		Order order = orderContext.getOrder();
		SubOrder subOrder = OrderServiceV2Utils.getSubOrderById(order.getSubOrders(), soid);
		//User user = orderContext.getUser();
		//boolean isAdminUser = OrderServiceV2Utils.isAnAdminUser(user);

		List<String> validActions = OrderServiceV2Utils.queryValidActions(order.getProcessType(), subOrder.getStatus(), orderContext.getCustomer(),
				orderContext.getUser(), isAdminUser);
		if (validActions == null){
			throw new ServiceExceptionV2(CODE_NO_PERMISSION, MESSAGE_NO_PERMISSION, order.getId(), soid);
		}
		if (!validActions.contains(actionName)){
			throw new ServiceExceptionV2(CODE_NO_PERMISSION, MESSAGE_NO_PERMISSION, order.getId(), soid);
		}
		
		doSuborderStatusAction(soid, actionName, order, subOrder);
		
		sendOrderNotifies(orderContext, actionName, soid, null);
	}


	protected void doSuborderStatusAction(Long soid, String actionName, Order order, SubOrder subOrder)
			throws ServiceExceptionV2, ServiceException {
		OrderStatus newStatus = OrderServiceV2Utils.calcNextStatus(order.getProcessType(), subOrder.getStatus(), actionName);
		if (newStatus == null){
			throw new ServiceExceptionV2(CODE_INVALID_OPERATION, MESSAGE_INVALID_OPERATION, order.getId(), soid, subOrder.getStatus(), actionName);
		}
		subOrder.setStatus(newStatus);
		OrderStatus orderStatus = OrderServiceV2Utils.calcOrderStatus(order);
		
		OrderStatus oldOrderStatus = order.getStatus();
		order.setStatus(orderStatus);
		
		String memberPointAdjustMessage = "";
		if (!oldOrderStatus.equals(orderStatus)){
			// 如果整体状态发生了变化，调整订单
			Long newMemPoints = null;
			switch(orderStatus){
			case PROCESSED:
				newMemPoints = adjustMemberPoints(order);
				if (newMemPoints != null && newMemPoints > 0){
					memberPointAdjustMessage=" 订单积分为"+newMemPoints+".";
				}
				break;
			case CLOSE:
				newMemPoints = confirmMemberPoints(order);
				if (newMemPoints != null && newMemPoints > 0){
					memberPointAdjustMessage=" 订单积分"+newMemPoints+"已到帐.";
				}
				break;
			default:
				if (GlobalConstants.OrderAction_Reset.equals(actionName)){
					newMemPoints = resetMemberPoints(order);
					if (newMemPoints != null && newMemPoints > 0){
						memberPointAdjustMessage=" 订单积分"+newMemPoints+"恢复为未到账.";
					}
				}
			}
		}
		recordOrderHistory(order, soid, newStatus, actionZhName.get(actionName)+memberPointAdjustMessage);
	}
	
	/**
	 * 更新订单相关的积分。
	 * @param orderId
	 * @param newPointValue Null 表示不更新值，只更新状态
	 */
	private Long updateMemberPoint(Long orderId, Long newPointValue, int newState) {
		if (orderId == null){
			return null;
		}
		List<MemberPoints> mps = memberPointsService.getByOrderId(orderId);
		if (mps == null){
			return null;
		}
		Long totalPoints = 0l;
		for(MemberPoints mp : mps){
			boolean changed = false;
			if (Constants.RATIO_BUTILE.equals(mp.getType())){
				if (mp.getStatas() == GlobalConstants.MEMBER_POINT_STATE_CANCELLED){
					// 已经取消的积分不处理。只处理未到账和已到帐之间的状态变化
					continue;
				}
				if (newPointValue != null){
					changed = true;
					totalPoints += newPointValue;
					mp.setValue(String.valueOf(newPointValue));
				}else{
					totalPoints += Long.parseLong(mp.getValue());
				}
				if (mp.getStatas() != newState){
					changed = true;
					mp.setStatas(newState);
				}
			}else if (Constants.FIRST_SCORE.equals(mp.getType())){
				// 首单积分值固定，不会被调整
				totalPoints += Long.parseLong(mp.getValue());
				if (mp.getStatas() != newState){
					changed = true;
					mp.setStatas(newState);
				}
			}// 其他类型的积分，不在订单中调整
			if (changed){
				try {
					Date thisTime = new Date();
					mp.setUpdateDate(thisTime);
					if (newState == GlobalConstants.MEMBER_POINT_STATE_CONFIRMED){
						Calendar cal = Calendar.getInstance();
			        	cal.setTime(thisTime);
			        	cal.add(Calendar.YEAR,1);//积分有效期：一年
			        	mp.setDateValid(cal.getTime());
			        	mp.setLtfePoint(Long.parseLong(mp.getValue()));
					}
					memberPointsService.update(mp);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		return totalPoints;
	}

	// 当管理员把订单恢复为初始状态（审核中/ORDERED)时，所有因此订单而导致的积分，全部改成 未到账。
	private Long resetMemberPoints(Order order) {
		return updateMemberPoint(order.getId(), null, GlobalConstants.MEMBER_POINT_STATE_PREPARED);
	}


	// 当订单关闭时，因此订单而产生的积分，状态变成 已到帐
	private Long confirmMemberPoints(Order order) {
		return updateMemberPoint(order.getId(), null, GlobalConstants.MEMBER_POINT_STATE_CONFIRMED);
	}

	// 当订单完成审核后，可能改过价了。根据新的订单价格，重新算一下是不是需要调整积分。
	private Long adjustMemberPoints(Order order) {
		Long newPoints = new Long(calcMemberPointByOrderTotal(order));
		return updateMemberPoint(order.getId(), newPoints, GlobalConstants.MEMBER_POINT_STATE_PREPARED);
	}

	@Override
	public void deliverySubOrder(OrderServiceV2Context orderContext, String suborderid, String dCode, String dNo)
			throws ServiceExceptionV2, ServiceException {
		long soid = Long.parseLong(suborderid);
		SubOrder subOrder= subOrderService.getSubOrderById(soid);
		if (subOrder == null){
			throw new ServiceExceptionV2(CODE_NO_SUBORDER, MESSAGE_NO_SUBORDER, suborderid);
		}
		Order order = getById(subOrder.getOrder().getId());
		if (order == null){
			throw new ServiceExceptionV2(CODE_NO_ORDER, MESSAGE_NO_ORDER, subOrder.getOrder().getId());
		}
		subOrder = OrderServiceV2Utils.getSubOrderById(order.getSubOrders(), subOrder.getId());
		subOrder.setDeliveryCode(dCode);
		subOrder.setDeliveryNumber(dNo);
		
		doSuborderStatusAction(subOrder.getId(), GlobalConstants.OrderAction_Ship, order, subOrder);

		orderContext.setOrder(order);
		sendOrderNotifies(orderContext, GlobalConstants.OrderAction_Ship, soid, null);
	}

	@Override
	public long getTotal(OrderServiceV2Context orderCtx) throws ServiceExceptionV2, ServiceException {
		return orderDao.getTotal(orderCtx.getCustomer());
	}

	@Override
	public double getTotalMoney(OrderServiceV2Context orderCtx, OrderCriteria criteria)
			throws ServiceExceptionV2, ServiceException {
		return orderDao.getTotalMoney(criteria);
	}

	@Override
	public OrderList listByCriteria(OrderCriteria criteria) throws ServiceExceptionV2, ServiceException {
		return orderDao.listByOrderCriteria(criteria);
	}

	@Override
	public String savePayProof(OrderServiceV2Context orderCtx, long subOrderId, String imageFileName,
			boolean isAdminUser) throws ServiceExceptionV2, ServiceException {
		Order order = orderCtx.getOrder();
		SubOrder subOrder = OrderServiceV2Utils.getSubOrderById(order.getSubOrders(), subOrderId);
		if (subOrder == null) {
			throw new ServiceExceptionV2(CODE_NO_SUBORDER, MESSAGE_NO_SUBORDER, subOrderId);
		}
		List<String> validActions = OrderServiceV2Utils.queryValidActions(order.getProcessType(), subOrder.getStatus(),
				orderCtx.getCustomer(), orderCtx.getUser(), isAdminUser);
		if (validActions == null) {
			throw new ServiceExceptionV2(CODE_NO_PERMISSION, MESSAGE_NO_PERMISSION, order.getId(), subOrderId);
		}
		if (!validActions.contains(GlobalConstants.OrderAction_UploadPayProof)) {
			throw new ServiceExceptionV2(CODE_NO_PERMISSION, MESSAGE_NO_PERMISSION, order.getId(), subOrderId);
		}

		String oldImage = subOrder.getPayProof();
		subOrder.setPayProof(imageFileName);
		this.doSuborderStatusAction(subOrderId, GlobalConstants.OrderAction_UploadPayProof, order, subOrder);
		sendOrderNotifies(orderCtx, GlobalConstants.OrderAction_UploadPayProof, subOrderId, null);
		return oldImage;
	}

	@Override
	public double getTotalMoney(Customer customer) {
		return orderDao.getTotalMoney(customer);
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
