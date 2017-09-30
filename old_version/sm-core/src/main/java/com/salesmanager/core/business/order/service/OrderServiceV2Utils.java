package com.salesmanager.core.business.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.salesmanager.core.business.common.model.InvoiceInfo;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.modules.sms.TextShortMessageService;
import com.salesmanager.core.utils.VerificationUtils;

public class OrderServiceV2Utils {
	private static Map<OrderStatus, Integer> payFirstOrderStatusWeights = new HashMap<OrderStatus, Integer>();
	private static Map<OrderStatus, Integer> shipFirstOrderStatusWeights = new HashMap<OrderStatus, Integer>();
	static {
		payFirstOrderStatusWeights.put(OrderStatus.ORDERED, 101);
		payFirstOrderStatusWeights.put(OrderStatus.PROCESSED, 100);
		payFirstOrderStatusWeights.put(OrderStatus.PAID, 99);
		payFirstOrderStatusWeights.put(OrderStatus.PAY_CONFIRMED, 98);
		payFirstOrderStatusWeights.put(OrderStatus.SHIPPED, 97);
		payFirstOrderStatusWeights.put(OrderStatus.RECEIPT, 96);
		payFirstOrderStatusWeights.put(OrderStatus.CLOSE, 95);
		payFirstOrderStatusWeights.put(OrderStatus.REFUNDED, 94);
		payFirstOrderStatusWeights.put(OrderStatus.CANCEL, 93);
		payFirstOrderStatusWeights.put(OrderStatus.REVIEW, 2);
		
		shipFirstOrderStatusWeights.put(OrderStatus.ORDERED, 101);
		shipFirstOrderStatusWeights.put(OrderStatus.PROCESSED, 100);
		shipFirstOrderStatusWeights.put(OrderStatus.SHIPPED, 99);
		shipFirstOrderStatusWeights.put(OrderStatus.RECEIPT, 98);
		shipFirstOrderStatusWeights.put(OrderStatus.PAID, 97);
		shipFirstOrderStatusWeights.put(OrderStatus.PAY_CONFIRMED, 96);
		shipFirstOrderStatusWeights.put(OrderStatus.CLOSE, 95);
		shipFirstOrderStatusWeights.put(OrderStatus.REFUNDED, 94);
		shipFirstOrderStatusWeights.put(OrderStatus.CANCEL, 93);
		shipFirstOrderStatusWeights.put(OrderStatus.REVIEW, 2);
	}

	public static void addAdjustPriceNotifyTask(List<OrderNotifyTask> tasks, Order order, Customer customer,
			boolean useCurrentSales, String purchasingAgentEmail, String purchasingAgentMobile, Map<String, Zone> zones,
			Map<String, Country> countries, boolean sendMerchantNotify, OrderServiceV2Context orderContext, Set<Long> changedSubOrderIds) {
		if (sendMerchantNotify) {
			// 所有子订单的卖家通知
			for (SubOrder subOrder : order.getSubOrders()) {
				if (!changedSubOrderIds.contains(subOrder.getId())){
					continue;
				}
				OrderNotifyTask merchantTask = new OrderNotifyTask();
				Map<String, Object> emailParameters = new HashMap<String, Object>();
				MerchantStore merchant = subOrder.getMerchant();
				emailParameters.put("LOGOPATH", createLogoPath(merchant));
				emailParameters.put("orderId", subOrder.getId());
				emailParameters.put("orderDate", order.getDatePurchased());
				emailParameters.put("recieverName", merchant.getStorename());
				emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
				emailParameters.put("subOrder", subOrder);
	
				merchantTask.setEmailParameters(emailParameters);
				merchantTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
				merchantTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
				merchantTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Merchant_AdjustPrice);
				merchantTask.setEmailTitle("订单"+subOrder.getId()+": 价格修改");
				merchantTask.setReceiverEmail(merchant.getStoreEmailAddress());
	
				// 调价没有商家短信
				tasks.add(merchantTask);
			}
		}
		// 然后是销售
		Map<String, OrderNotifyTask> salesNotifies = new HashMap<String, OrderNotifyTask>();
		for (SubOrder subOrder : order.getSubOrders()) {
			if (!changedSubOrderIds.contains(subOrder.getId())){
				continue;
			}
			String emailAddr = getSalesEmailAddress(customer, useCurrentSales, subOrder);
			if (!VerificationUtils.isEmail(emailAddr)) {
				continue;
			}
			OrderNotifyTask notifyTask = salesNotifies.get(emailAddr);
			if (notifyTask == null) {
				notifyTask = new OrderNotifyTask();
				Map<String, Object> emailParameters = new HashMap<String, Object>();
				emailParameters.put("LOGOPATH", createLogoPath(subOrder.getMerchant()));
				emailParameters.put("orderId", subOrder.getId());
				emailParameters.put("orderDate", order.getDatePurchased());
				emailParameters.put("recieverName", customer.getNick());
				emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
				//emailParameters.put("subOrder", subOrder);
	
				notifyTask.setEmailParameters(emailParameters);
				notifyTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
				notifyTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
				notifyTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_AdjustPrice);
				notifyTask.setEmailTitle("订单"+subOrder.getId()+": 价格修改");
				notifyTask.setReceiverEmail(emailAddr);
				
				salesNotifies.put(emailAddr, notifyTask);
			}
			List<SubOrder> subOrders = (List<SubOrder>) notifyTask.getEmailParameters().get("subOrders");
			if (subOrders == null){
				subOrders = new ArrayList<SubOrder>();
				notifyTask.getEmailParameters().put("subOrders", subOrders);
			}
			subOrders.add(subOrder);
		}
		tasks.addAll(salesNotifies.values());
		
		// 改价没有销售和采购的短信
		// 买家的邮件通知
		OrderNotifyTask customerTask = new OrderNotifyTask();
		Map<String, Object> emailParameters = new HashMap<String, Object>();
		emailParameters.put("LOGOPATH", createLogoPath(orderContext.getStore()));
		emailParameters.put("orderId", order.getId());
		emailParameters.put("orderDate", order.getDatePurchased());
		emailParameters.put("recieverName", customer.getNick());
		emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
		Set<SubOrder> subOrders = new HashSet<SubOrder>(order.getSubOrders());
		Iterator<SubOrder> it = subOrders.iterator();
		while(it.hasNext()){
			SubOrder sOrder = it.next();
			if (!changedSubOrderIds.contains(sOrder.getId())){
				it.remove();
			}
		}
		emailParameters.put("subOrders", subOrders);

		customerTask.setEmailParameters(emailParameters);
		customerTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
		customerTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
		customerTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Customer_AdjustPrice);
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (SubOrder subOrder : subOrders) {
			if (!isFirst){
				sb.append(",");
			}else{
				isFirst = false;
			}
			sb.append(subOrder.getId());
		}
		customerTask.setEmailTitle("订单"+sb.toString()+": 价格修改");
		customerTask.setReceiverEmail(customer.getEmailAddress());
		
		// 买家的短信通知
		if (VerificationUtils.isMobileNumber(customer.getPhone())){
			customerTask.setReceiverPhone(customer.getPhone().trim());
			customerTask.setSmsParameters(createSmsAdjustPriceParameters(customer, orderContext));
			customerTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_CUSTOMER_ADJUSTPRICE);
		}
		
		tasks.add(customerTask);
		
		// 采购的邮件和用户的几乎一致，就是地址和模板不同，参数一致，所以偷个懒
		if (VerificationUtils.isEmail(purchasingAgentEmail)) {
			OrderNotifyTask paTask = new OrderNotifyTask();
			paTask.setEmailParameters(emailParameters);
			paTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			paTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			paTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_AdjustPrice);
			paTask.setEmailTitle(customerTask.getEmailTitle());
			paTask.setReceiverEmail(purchasingAgentEmail);
			tasks.add(paTask);
		}
	}
	
	public static void addConfirmOrderNotifyTask(List<OrderNotifyTask> tasks, Order order, Long subOrderId, Customer customer,
			boolean useCurrentSales, String purchasingAgentEmail, String purchasingAgentMobile, Map<String, Zone> zones,
			Map<String, Country> countries, OrderServiceV2Context orderContext) {
		// 确认订单没有短信
		// 子订单的卖家通知
		SubOrder subOrder = getSubOrderById(order.getSubOrders(), subOrderId);
		if (subOrder == null){
			return;
		}
		OrderNotifyTask merchantTask = new OrderNotifyTask();
		Map<String, Object> emailParameters = new HashMap<String, Object>();
		MerchantStore merchant = subOrder.getMerchant();
		emailParameters.put("LOGOPATH", createLogoPath(merchant));
		emailParameters.put("orderId", subOrder.getId());
		emailParameters.put("orderDate", new Date());
		emailParameters.put("recieverName", merchant.getStorename());
		emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
		emailParameters.put("subOrder", subOrder);

		merchantTask.setEmailParameters(emailParameters);
		merchantTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
		merchantTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
		merchantTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Merchant_ConfirmOrder);
		String emailTitle = makeSubOrderNotifyEmailTitle(subOrder.getId(), order.getProcessType(), subOrder.getStatus());
		merchantTask.setEmailTitle(emailTitle);
		merchantTask.setReceiverEmail(merchant.getStoreEmailAddress());

		tasks.add(merchantTask);
		
		// 然后是销售
		String emailAddr = getSalesEmailAddress(customer, useCurrentSales, subOrder);
		if (VerificationUtils.isEmail(emailAddr)) {
			OrderNotifyTask notifyTask = new OrderNotifyTask();
			// same parameters, so reuse it
			/*
			 * emailParameters = new HashMap<String, Object>();
			 * emailParameters.put("LOGOPATH",
			 * createLogoPath(subOrder.getMerchant()));
			 * emailParameters.put("orderId", subOrder.getId());
			 * emailParameters.put("orderDate", new Date());
			 * emailParameters.put("recieverName",
			 * subOrder.getMerchant().getStorename());
			 * emailParameters.put("shippingInfo", createShippingInfo(order,
			 * zones, countries)); emailParameters.put("subOrder", subOrder);
			 */

			notifyTask.setEmailParameters(emailParameters);
			notifyTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			notifyTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			notifyTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_ConfirmOrder);
			notifyTask.setEmailTitle(emailTitle);
			notifyTask.setReceiverEmail(emailAddr);
			tasks.add(notifyTask);
		}
		// 采购的邮件
		if (VerificationUtils.isEmail(purchasingAgentEmail)){
			OrderNotifyTask paTask = new OrderNotifyTask();
			paTask.setEmailParameters(emailParameters);
			paTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			paTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			paTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_ConfirmOrder);
			paTask.setEmailTitle(emailTitle);
			paTask.setReceiverEmail(purchasingAgentEmail);
			tasks.add(paTask);
		}
		
		// 买家的邮件通知
		OrderNotifyTask customerTask = new OrderNotifyTask();
		emailParameters = new HashMap<String, Object>();
		emailParameters.put("LOGOPATH", createLogoPath(merchant));
		emailParameters.put("orderId", subOrder.getId());
		emailParameters.put("orderDate", new Date());
		emailParameters.put("recieverName", customer.getNick());
		emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
		emailParameters.put("subOrder", subOrder);

		customerTask.setEmailParameters(emailParameters);
		customerTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
		customerTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
		customerTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Customer_ConfirmOrder);
		customerTask.setEmailTitle(emailTitle);
		customerTask.setReceiverEmail(customer.getEmailAddress());
		tasks.add(customerTask);

	}

	// place order 目前不给买家发通知
	public static void addPlaceOrderNotifyTask(List<OrderNotifyTask> tasks, Order order, Customer customer, boolean useCurrentSales,
			String purchasingAgentEmail, String purchasingAgentMobile, Map<String, Zone> zones, Map<String, Country> countries) {
		
		// 先是一个所有子订单的卖家通知
		for(SubOrder subOrder : order.getSubOrders()) {
			OrderNotifyTask merchantTask = new OrderNotifyTask();
			Map<String, Object> emailParameters = new HashMap<String, Object>();
			MerchantStore merchant = subOrder.getMerchant();
			emailParameters.put("LOGOPATH", createLogoPath(merchant));
			emailParameters.put("orderId", subOrder.getId());
			emailParameters.put("orderDate", order.getDatePurchased());
			emailParameters.put("recieverName", merchant.getStorename());
			emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
			emailParameters.put("subOrder", subOrder);
			
			merchantTask.setEmailParameters(emailParameters);
			merchantTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			merchantTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			merchantTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Merchant_PlaceOrder);
			merchantTask.setEmailTitle(makeSubOrderNotifyEmailTitle(subOrder.getId(), order.getProcessType(), subOrder.getStatus()));
			merchantTask.setReceiverEmail(merchant.getStoreEmailAddress());
			
			// 然后判断短信
			String phoneNum = merchant.getStoremobile();
			if (VerificationUtils.isMobileNumber(phoneNum)){
				merchantTask.setReceiverPhone(phoneNum.trim());
				merchantTask.setSmsParameters(createSmsPlaceOrderParameters(subOrder, customer));
				merchantTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_PLACEORDER);
			}
			tasks.add(merchantTask);
		}
		
		// 然后是销售
		for(SubOrder subOrder : order.getSubOrders()) {
			String emailAddr = getSalesEmailAddress(customer, useCurrentSales, subOrder);
			if (!VerificationUtils.isEmail(emailAddr)){
				continue;
			}
			OrderNotifyTask notifyTask = new OrderNotifyTask();
			Map<String, Object> emailParameters = new HashMap<String, Object>();
			emailParameters.put("LOGOPATH", createLogoPath(subOrder.getMerchant()));
			emailParameters.put("orderId", subOrder.getId());
			emailParameters.put("orderDate", order.getDatePurchased());
			emailParameters.put("recieverName", subOrder.getMerchant().getStorename());
			emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
			emailParameters.put("subOrder", subOrder);
			
			notifyTask.setEmailParameters(emailParameters);
			notifyTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			notifyTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			notifyTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_PlaceOrder);
			notifyTask.setEmailTitle(makeSubOrderNotifyEmailTitle(subOrder.getId(), order.getProcessType(), subOrder.getStatus()));
			notifyTask.setReceiverEmail(emailAddr);
			
			tasks.add(notifyTask);
		}
		// 和销售的短信
		for(SubOrder subOrder : order.getSubOrders()) {
			String phoneNumber = getSalesPhoneNumber(customer, useCurrentSales, subOrder);
			if (!VerificationUtils.isMobileNumber(phoneNumber)){
				continue;
			}
			OrderNotifyTask notifyTask = new OrderNotifyTask();
			notifyTask.setReceiverPhone(phoneNumber.trim());
			String[] params = createSmsPlaceOrderParameters(subOrder, customer);
			params[0] = "(代收通知)" + params[0];
			notifyTask.setSmsParameters(params);
			notifyTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_PLACEORDER);
			tasks.add(notifyTask);
		}
		
		// 接着是采购
		if (purchasingAgentEmail != null){
			for(SubOrder subOrder : order.getSubOrders()) {
				OrderNotifyTask notifyTask = new OrderNotifyTask();
				Map<String, Object> emailParameters = new HashMap<String, Object>();
				emailParameters.put("LOGOPATH", createLogoPath(subOrder.getMerchant()));
				emailParameters.put("orderId", subOrder.getId());
				emailParameters.put("orderDate", order.getDatePurchased());
				emailParameters.put("recieverName", subOrder.getMerchant().getStorename());
				emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
				emailParameters.put("subOrder", subOrder);
				
				notifyTask.setEmailParameters(emailParameters);
				notifyTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
				notifyTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
				notifyTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_PlaceOrder);
				notifyTask.setEmailTitle(makeSubOrderNotifyEmailTitle(subOrder.getId(), order.getProcessType(), subOrder.getStatus()));
				notifyTask.setReceiverEmail(purchasingAgentEmail);
				
				tasks.add(notifyTask);
			}
		}
		// 以及采购的短信
		if (VerificationUtils.isMobileNumber(purchasingAgentMobile)){
			for(SubOrder subOrder : order.getSubOrders()) {
				OrderNotifyTask notifyTask = new OrderNotifyTask();
				notifyTask.setReceiverPhone(purchasingAgentMobile.trim());
				String[] params = createSmsPlaceOrderParameters(subOrder, customer);
				params[0] = "(代收通知)" + params[0];
				notifyTask.setSmsParameters(params);
				notifyTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_PLACEORDER);
				tasks.add(notifyTask);
			}
		}
	}

	public static void addStatusChangeNotifyTask(List<OrderNotifyTask> tasks, String actionName, Order order,
			Long subOrderId, Customer customer, boolean useCurrentSales, String purchasingAgentEmail,
			String purchasingAgentMobile, Map<String, Zone> zones, Map<String, Country> countries,
			OrderServiceV2Context orderContext) {
		SubOrder subOrder = getSubOrderById(order.getSubOrders(), subOrderId);
		if (subOrder == null) {
			return;
		}
		Map<String, Object> emailParameters = new HashMap<String, Object>();
		MerchantStore merchant = subOrder.getMerchant();
		String orderStatus = getStatusDisplayName(order.getProcessType(), subOrder.getStatus());
		String emailTitle = makeSubOrderNotifyEmailTitle(subOrder.getId(), order.getProcessType(),
				subOrder.getStatus());

		emailParameters.put("LOGOPATH", createLogoPath(merchant));
		emailParameters.put("orderId", subOrder.getId());
		emailParameters.put("orderDate", new Date());
		emailParameters.put("recieverName", merchant.getStorename());
		emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
		emailParameters.put("subOrder", subOrder);
		emailParameters.put("orderStatus", orderStatus);
		emailParameters.put("summary", getActionSummaryForMerchant(actionName));

		if (orderContext.isAdminUser()) {
			OrderNotifyTask merchantTask = new OrderNotifyTask();
			merchantTask.setEmailParameters(emailParameters);
			merchantTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			merchantTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			merchantTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Merchant_Status);
			merchantTask.setEmailTitle(emailTitle);
			merchantTask.setReceiverEmail(merchant.getStoreEmailAddress());
			tasks.add(merchantTask);
		}

		// 然后是销售
		String emailAddr = getSalesEmailAddress(customer, useCurrentSales, subOrder);
		if (VerificationUtils.isEmail(emailAddr)) {
			OrderNotifyTask notifyTask = new OrderNotifyTask();
			HashMap<String, Object> salesParameters = new HashMap<String, Object>(emailParameters);
			salesParameters.put("summary", getActionSummaryForMonitor(actionName));
			notifyTask.setEmailParameters(salesParameters);
			notifyTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			notifyTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			notifyTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_Status);
			notifyTask.setEmailTitle(emailTitle);
			notifyTask.setReceiverEmail(emailAddr);
			tasks.add(notifyTask);
		}
		// 采购的邮件
		if (VerificationUtils.isEmail(purchasingAgentEmail)) {
			OrderNotifyTask paTask = new OrderNotifyTask();
			HashMap<String, Object> paParameters = new HashMap<String, Object>(emailParameters);
			paParameters.put("summary", getActionSummaryForMonitor(actionName));
			paTask.setEmailParameters(paParameters);
			paTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
			paTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
			paTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Monitor_Status);
			paTask.setEmailTitle(emailTitle);
			paTask.setReceiverEmail(purchasingAgentEmail);
			tasks.add(paTask);
		}

		// 买家的邮件通知
		OrderNotifyTask customerTask = new OrderNotifyTask();
		emailParameters = new HashMap<String, Object>();
		emailParameters.put("LOGOPATH", createLogoPath(merchant));
		emailParameters.put("orderId", subOrder.getId());
		emailParameters.put("orderDate", new Date());
		emailParameters.put("recieverName", customer.getNick());
		emailParameters.put("shippingInfo", createShippingInfo(order, zones, countries));
		emailParameters.put("subOrder", subOrder);
		emailParameters.put("orderStatus", orderStatus);
		emailParameters.put("summary", getActionSummaryForMerchant(actionName));

		customerTask.setEmailParameters(emailParameters);
		customerTask.setEmailSenderAddress(GlobalConstants.EMAIL_SERVICE);
		customerTask.setEmailSenderName(GlobalConstants.EMAIL_SERVICE_SENDER_NAME);
		customerTask.setEmailTemplateName(OrderServiceV2Constants.EMAIL_TEMPLATE_Customer_Status);
		customerTask.setEmailTitle(emailTitle);
		customerTask.setReceiverEmail(customer.getEmailAddress());
		tasks.add(customerTask);
		
		// 然后是短信
		Set<String> actionsHasSms = new HashSet<String>();
		actionsHasSms.add(GlobalConstants.OrderAction_ConfirmReceipt);
		actionsHasSms.add(GlobalConstants.OrderAction_UploadPayProof);
		actionsHasSms.add(GlobalConstants.OrderAction_Ship);
		if (!actionsHasSms.contains(actionName)){
			return;
		}
		if (GlobalConstants.OrderAction_ConfirmReceipt.equals(actionName)){
			// 卖家通知
			if (VerificationUtils.isMobileNumber(merchant.getStoremobile())){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(merchant.getStoremobile());
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_RECIEPT);
				String[] params = createRecieptSmsParams(subOrder, customer);
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
			// 销售通知
			String phoneNum = getSalesPhoneNumber(customer, useCurrentSales, subOrder);
			if (VerificationUtils.isMobileNumber(phoneNum)){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(phoneNum);
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_RECIEPT);
				String[] params = createRecieptSmsParams(subOrder, customer);
				params[0] = "(代收通知)" + params[0];
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
			// 系统采购通知
			if (VerificationUtils.isMobileNumber(purchasingAgentMobile)){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(purchasingAgentMobile);
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_RECIEPT);
				String[] params = createRecieptSmsParams(subOrder, customer);
				params[0] = "(代收通知)" + params[0];
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
		}else if (GlobalConstants.OrderAction_UploadPayProof.equals(actionName)){
			// 卖家通知
			if (VerificationUtils.isMobileNumber(merchant.getStoremobile())){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(merchant.getStoremobile());
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_PAID);
				String[] params = createPaidSmsParams(subOrder, customer);
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
			// 销售通知
			String phoneNum = getSalesPhoneNumber(customer, useCurrentSales, subOrder);
			if (VerificationUtils.isMobileNumber(phoneNum)){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(phoneNum);
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_PAID);
				String[] params = createPaidSmsParams(subOrder, customer);
				params[0] = "(代收通知)" + params[0];
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
			// 系统采购通知
			if (VerificationUtils.isMobileNumber(purchasingAgentMobile)){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(purchasingAgentMobile);
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_MERCHANT_PAID);
				String[] params = createPaidSmsParams(subOrder, customer);
				params[0] = "(代收通知)" + params[0];
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
		}else if (GlobalConstants.OrderAction_Ship.equals(actionName)){
			// 买家通知
			if (VerificationUtils.isMobileNumber(customer.getPhone())){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(customer.getPhone());
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_CUSTOMER_SHIPPED);
				String[] params = createShippedSmsParams(subOrder, customer);
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
			// 销售通知
			String phoneNum = getSalesPhoneNumber(customer, useCurrentSales, subOrder);
			if (VerificationUtils.isMobileNumber(phoneNum)){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(phoneNum);
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_CUSTOMER_SHIPPED);
				String[] params = createShippedSmsParams(subOrder, customer);
				params[0] = "(代收通知)" + params[0];
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
			// 系统采购通知
			if (VerificationUtils.isMobileNumber(purchasingAgentMobile)){
				OrderNotifyTask smsTask = new OrderNotifyTask();
				smsTask.setReceiverPhone(purchasingAgentMobile);
				smsTask.setSmsTemplateId(TextShortMessageService.SMSTPL_NOTIFY_CUSTOMER_SHIPPED);
				String[] params = createShippedSmsParams(subOrder, customer);
				params[0] = "(代收通知)" + params[0];
				smsTask.setSmsParameters(params);
				tasks.add(smsTask);
			}
		}
	}

	/**
	 * <pre>
	 * 先付款，后发货的订单，事件==>状态关系为：
	 * 	上传支付凭证==>PAID
	 * 	确认收钱==>PAY_CONFIRMED
	 * 	发货==>SHIPPED
	 * 	确认收货==>CLOSE
	 * 先发货，后付款的，关系为：
	 * 	发货==>SHIPPED
	 * 	确认收货==>RECEIPT
	 * 	上传支付凭证==>PAID
	 * 	确认收钱==>CLOSE
	 * </pre>
	 * @param processType
	 * @param status
	 * @param actionName
	 * @return
	 */
	public static OrderStatus calcNextStatus(String processType, OrderStatus curStatus, String actionName) {
		// 先来几个简单的
		if (GlobalConstants.OrderAction_AdjustPrice.equals(actionName)) {
			return curStatus; // 改价不改变状态
		}
		if (GlobalConstants.OrderAction_Review.equals(actionName)) {
			return curStatus; // 评价不改变状态
		}
		if (GlobalConstants.OrderAction_Reset.equals(actionName)) {
			return OrderStatus.ORDERED; // 重置操作，总是回到 新订单 状态
		}
		if (GlobalConstants.OrderAction_Close.equals(actionName)) {
			return OrderStatus.CLOSE; // 强制关闭操作，总是关闭
		}
		if (GlobalConstants.OrderAction_Cancel.equals(actionName)) {
			return OrderStatus.CANCEL; // 取消操作，总是取消
		}
		if (GlobalConstants.OrderAction_ConfirmOrder.equals(actionName)) {
			return OrderStatus.PROCESSED; // 取消操作，总是取消
		}
		// 所以现在剩下的就是
		// OrderAction_UploadPayProof = "upload_pay_proof"; // 上传支付凭证
		// OrderAction_ConfirmPay = "confirm_pay"; // 确认收到货款
		// OrderAction_Ship = "ship_order"; // 发货
		// OrderAction_ConfirmReceipt = "confirm_receipt"; // 确认收货
		
		if (GlobalConstants.OrderAction_UploadPayProof.equals(actionName)) {
			return OrderStatus.PAID; // 上传支付凭证,两种order都一样
		}
		if (GlobalConstants.OrderAction_Ship.equals(actionName)) {
			return OrderStatus.SHIPPED; // 发货,两种order都一样
		}
		
		if (GlobalConstants.OrderProcessType_PayFirst.equals(processType)){
			if (GlobalConstants.OrderAction_ConfirmPay.equals(actionName)) {
				return OrderStatus.PAY_CONFIRMED; 
			}
			if (GlobalConstants.OrderAction_ConfirmReceipt.equals(actionName)) {
				return OrderStatus.CLOSE;
			}
		}else{
		//if (GlobalConstants.OrderProcessType_ShipFirst.equals(processType)){
			if (GlobalConstants.OrderAction_ConfirmPay.equals(actionName)) {
				return OrderStatus.CLOSE;
			}
			if (GlobalConstants.OrderAction_ConfirmReceipt.equals(actionName)) {
				return OrderStatus.RECEIPT; 
			}
		}
		return null;
	}

	
	/**
	 * 根据子订单状态，计算整个订单的状态
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	
	public static OrderStatus calcOrderStatus(Order order) throws ServiceException {
		Map<OrderStatus, Integer> statusWeight = null;
		if (GlobalConstants.OrderProcessType_PayFirst.equals(order.getProcessType())){
			statusWeight = payFirstOrderStatusWeights;
		}else{
			statusWeight = shipFirstOrderStatusWeights;
		}
		OrderStatus finalStatus = OrderStatus.ORDERED;
		int weight = 0;
		for(SubOrder subOrder : order.getSubOrders()){
			OrderStatus subStatus = subOrder.getStatus();
			Integer subWeight = statusWeight.get(subStatus);
			if (subStatus == null || subWeight == null){
				// 订单状态没处理到，需要更改代码。前台简单显示系统异常即可。这个需要代码修复
				throw new ServiceException("Order status of " + subOrder.getId()+" not handled.");
			}
			if (subWeight > weight){
				weight = subWeight;
				finalStatus = subStatus;
			}
		}
		return finalStatus;
	}
	
	public static boolean checkItemLevelPriceType(Set<OrderProduct> items) {
		boolean itemsAdjused = false;
		for(OrderProduct item : items){
			BigDecimal orgUnitPrice = item.getPrice();
			BigDecimal orgTotalPrice = item.getOneTimeCharge();
			item.setNominalUnitPrice(orgUnitPrice);
			item.setNominalTotalPrice(orgTotalPrice);
						
			BigDecimal finalUnitPrice = item.getFinalPrice();
			BigDecimal finalTotalPrice = item.getFinalTotal();
			if (finalUnitPrice == null && finalTotalPrice == null){
				item.setPriceType(GlobalConstants.OrderPriceType.NORMAL);
				// if no change for this item, nothing to do but mark it as NORMAL
				continue;
			}
			itemsAdjused = true;
			
			if (finalUnitPrice != null){
				item.setNominalUnitPrice(finalUnitPrice);
				item.setNominalTotalPrice(finalTotalPrice);
				item.setPriceType(GlobalConstants.OrderPriceType.CALCULATED);
				continue;
			}
			
			item.setNominalTotalPrice(finalTotalPrice);
			item.setPriceType(GlobalConstants.OrderPriceType.ADJUSTED);
			
		}
		return itemsAdjused;
	}

	private static String createLogoPath(MerchantStore merchant) {
		StringBuilder sb = new StringBuilder();
		sb.append("<img src='http://www.bettbio.com/sm-shop/static/").append(merchant.getCode()).append("/")
				.append(FileContentType.LOGO).append("/").append(merchant.getStoreLogo())
				.append("' style='max-width:400px;height:70px'>");
		return sb.toString();
	}

	private static String[] createPaidSmsParams(SubOrder subOrder, Customer customer) {
		// 目前两个参数一样
		return createRecieptSmsParams(subOrder, customer);
	}

	private static String[] createRecieptSmsParams(SubOrder subOrder, Customer customer) {
		// {1},您好!用户{2}已经对{3}商品进行了确认收货,订单号:{4}.
		String[] result = new String[4];
		result[0] = subOrder.getMerchant().getStorename();
		result[1] = customer.getNick();
		result[2] = "";
		for(OrderProduct prod: subOrder.getOrderProducts()){
			result[2] = prod.getProductName();
			if (subOrder.getOrderProducts().size() > 1){
				result[2] = result[2] + "等";
			}
			break;
		}
		result[3]=String.valueOf(subOrder.getId());
		return result;
	}

	private static String[] createShippedSmsParams(SubOrder subOrder, Customer customer) {
		// {1},您好.您订购的{2}产品，商家{3}已发货.承运方:{4},单号:{5},订单号:{6}.
		String[] result = new String[6];
		result[0] = customer.getNick();
		result[1] = "";
		for(OrderProduct prod: subOrder.getOrderProducts()){
			result[1] = prod.getProductName();
			if (subOrder.getOrderProducts().size() > 1){
				result[1] = result[1] + "等";
			}
			break;
		}
		result[2] = subOrder.getMerchant().getStorename();
		result[3] = subOrder.getDeliveryCode();
		result[4] = subOrder.getDeliveryNumber();
		result[5]=String.valueOf(subOrder.getId());
		return result;
	}

	private static Object createShippingInfo(Order order, Map<String, Zone> zones, Map<String, Country> countries) {
		Map<String, Object>info = new HashMap<String, Object>();
		Map<String, String> address = new HashMap<String, String>();
		address.put("company", order.getBillingCompany());
		address.put("name", order.getBillingUserName());
		address.put("telephone", order.getBillingTelephone());
		address.put("streetAdress", order.getBillingAddress());
		address.put("city", order.getBillingCity());
		address.put("zone", makeZoneName(order.getBillingZone(), zones));
		address.put("country", makeCountryName(order.getBillingCountry(), countries));
		address.put("postCode", order.getBillingPostalCode());
		info.put("address", address);
		
		InvoiceInfo orderInvoice = order.getBillingInvoice();
		if (orderInvoice != null){
//			Map<String, String> invoic = new HashMap<String, String>();
//			invoic.put("company", orderInvoice.getCompany());
//			invoic.put("companyAddress", orderInvoice.getCompanyAddress());
//			invoic.put("companyTelephone", orderInvoice.getCompanyTelephone());
//			invoic.put("bankName", orderInvoice.getBankName());
//			invoic.put("bankAccount", orderInvoice.getBankAccount());
//			invoic.put("taxpayerNumber", orderInvoice.getTaxpayerNumber());
			info.put("invoice", orderInvoice);
		}else{
			info.put("invoice", new HashMap<String, String>());
		}
		
		
		address = new HashMap<String, String>();
		address.put("company", order.getIvoiceCompany());
		address.put("name", order.getIvoiceUserName());
		address.put("telephone", order.getIvoiceTelephone());
		address.put("streetAdress", order.getIvoiceAddress());
		address.put("city", order.getIvoiceCity());
		address.put("zone", makeZoneName(order.getIvoiceZone(), zones));
		address.put("country", makeCountryName(order.getIvoiceCountry(), countries));
		address.put("postCode", order.getIvoicePostalCode());
		info.put("invoiceAddr", address);
		return info;
	}

	private static String[] createSmsAdjustPriceParameters(Customer customer, OrderServiceV2Context orderContext) {
		// {1},您好!您订购的{2}产品,商家{3}已将价格改为{4},订单号:{5},请您
		String[] result = new String[5];
		result[0] = customer.getNick();
		final int POS_PRICE = 3;
		final int POS_SOID = 4;
		String prodName = null;
		boolean isFirst = true;
		Order order = orderContext.getOrder();
		result[POS_PRICE]=null;
		result[POS_SOID]=null;
		for(SubOrder subOrder : order.getSubOrders()){
			BigDecimal totalPrice = subOrder.getFinalTotal();
			if (totalPrice == null){
				totalPrice = subOrder.getTotal();
			}
			if (subOrder.getFinalTotal() == null){
				continue;
			}
			if (result[POS_PRICE] == null){
				result[POS_PRICE] = String.format("%3.02f", totalPrice.doubleValue());
			}else{
				result[POS_PRICE] = result[POS_PRICE]+","+String.format("%3.02f", subOrder.getFinalTotal().doubleValue());
			}
			if (result[POS_SOID] == null){
				result[POS_SOID] = String.valueOf(subOrder.getId());
			}else{
				result[POS_SOID] = result[POS_SOID]+","+String.valueOf(subOrder.getId());
			}
			for(OrderProduct prod : subOrder.getOrderProducts()){
				if(prodName == null) {
					prodName = prod.getProductName()+"("+prod.getSpecifications()+")";
				}
				break;
			}
		}
		result[1] = prodName+"等";
		String merchantName = "";
		if (orderContext.isAdminUser()){
			merchantName = "管理员";
		}else if (orderContext.getUser() != null){
			User user = orderContext.getUser();
			merchantName = user.getMerchantStore().getStorename();
		}
		result[2]=merchantName;
		return result;
	}

	private static String[] createSmsPlaceOrderParameters(SubOrder subOrder, Customer customer) {
		//{1},您好！用户{2}已订购您公司的{3}产品，订单号:{4},请..
		String[] result = new String[4];
		result[0] = subOrder.getStoreName();
		result[1] = customer.getNick();
		Set<OrderProduct> prods = subOrder.getOrderProducts();
		Iterator<OrderProduct> it = prods.iterator();
		result[2] = "若干";
		while(it.hasNext()){
			OrderProduct prod = it.next();
			StringBuilder sb = new StringBuilder();
			sb.append(prod.getProductName()).append('(').append(prod.getSpecifications()).append(')');
			if (prods.size() > 1){
				sb.append("等");
			}
			result[2] = sb.toString();
			break;
		}
		result[3] = String.valueOf(subOrder.getId());
		return result;
	}

	private static Object getActionSummaryForMerchant(String actionName) {
		if (GlobalConstants.OrderAction_Cancel.equals(actionName)){
			return "您的订单被取消。";
		}else if (GlobalConstants.OrderAction_UploadPayProof.equals(actionName)){
			return "用户已经支付了您的订单。";
		}else if (GlobalConstants.OrderAction_ConfirmPay.equals(actionName)){
			return "订单已经确认收款。";
		}else if (GlobalConstants.OrderAction_Ship.equals(actionName)){
			return "您的订单已发货。";
		}else if (GlobalConstants.OrderAction_ConfirmReceipt.equals(actionName)){
			return "您的订单已被用户确认收货。";
		}else if (GlobalConstants.OrderAction_Close.equals(actionName)){
			return "您的订单被关闭。";
		}else if (GlobalConstants.OrderAction_AdjustPrice.equals(actionName)){
			return "您的订单价格已经修改。";
		}else if (GlobalConstants.OrderAction_Reset.equals(actionName)){
			return "您的订单被重置。";
		}else if (GlobalConstants.OrderAction_Review.equals(actionName)){
			return "您的订单被评价。";
		}else if (GlobalConstants.OrderAction_ConfirmOrder.equals(actionName)){
			return "您的订单已通过审核。";
		}else {
			return "您的订单状态已变更。";
		}
	}

	private static Object getActionSummaryForMonitor(String actionName) {
		if (GlobalConstants.OrderAction_Cancel.equals(actionName)){
			return "订单被取消。";
		}else if (GlobalConstants.OrderAction_UploadPayProof.equals(actionName)){
			return "用户已完成订单支付。";
		}else if (GlobalConstants.OrderAction_ConfirmPay.equals(actionName)){
			return "卖家已确认收款。";
		}else if (GlobalConstants.OrderAction_Ship.equals(actionName)){
			return "订单已发货。";
		}else if (GlobalConstants.OrderAction_ConfirmReceipt.equals(actionName)){
			return "买家已确认收货。";
		}else if (GlobalConstants.OrderAction_Close.equals(actionName)){
			return "订单被直接关闭。";
		}else if (GlobalConstants.OrderAction_AdjustPrice.equals(actionName)){
			return "订单价格被修改。";
		}else if (GlobalConstants.OrderAction_Reset.equals(actionName)){
			return "订单被重置为待审核。";
		}else if (GlobalConstants.OrderAction_Review.equals(actionName)){
			return "订单被评价。";
		}else if (GlobalConstants.OrderAction_ConfirmOrder.equals(actionName)){
			return "订单已通过审核。";
		}else {
			return "订单状态已变更。";
		}
	}

	private static String getSalesEmailAddress(Customer customer, boolean useCurrentSales, SubOrder subOrder) {
		if (customer == null){
			return null;
		}
		if (useCurrentSales){
			return customer.getRelatedSalesEmail();
		}
		return subOrder.getRelatedSalesEmail();
	}

	private static String getSalesPhoneNumber(Customer customer, boolean useCurrentSales, SubOrder subOrder) {
		if (customer == null){
			return null;
		}
		if (useCurrentSales){
			return customer.getRelatedSalesMobile();
		}
		return subOrder.getRelatedSalesMobile();
	}

	private static String getStatusDisplayName(String processType, OrderStatus status) {
		if (GlobalConstants.OrderProcessType_PayFirst.equals(processType)){
			switch (status){
			case ORDERED: return "审核中";
			case SHIPPED: return "卖家已发货";
			case RECEIPT: return "已关闭";
			case PAID: return "买家已付款";
			case REFUNDED: return "已退还";
			case PROCESSED: return "待付款";
			case CLOSE: return "已关闭";
			case CANCEL: return "已取消";
			case REVIEW: return "已评价";
			case PAY_CONFIRMED: return "待发货";
			default:
				return "处理中";
			}
		}
		
		// 先发货
		switch (status){
		case ORDERED: return "审核中";
		case SHIPPED: return "卖家已发货";
		case RECEIPT: return "待付款";
		case PAID: return "买家已付款";
		case REFUNDED: return "已退还";
		case PROCESSED: return "待发货";
		case CLOSE: return "已关闭";
		case CANCEL: return "已取消";
		case REVIEW: return "已评价";
		case PAY_CONFIRMED: return "已关闭";
		default:
			return "处理中";
		}
	}

	public static SubOrder getSubOrderById(Set<SubOrder> subOrders, Long subOid) {
		if (subOrders == null) {
			return null;
		}
		for (SubOrder so : subOrders) {
			if (so.getId().equals(subOid)) {
				return so;
			}
		}
		return null;
	}

	public static boolean isAnAdminUser(User user){
		if (user == null){
			return false;
		}
		List<Group> groups = user.getGroups();
		for(Group group: groups){
			for(Permission permission : group.getPermissions()){
				permission.getPermissionName().equals("ADMIN");
				return true;
			}
		}
		return false;
	}

	private static String makeCountryName(Country oCountry, Map<String, Country> countries) {
		if (oCountry == null){
			return null;
		}
		Country country = countries.get(oCountry.getIsoCode());
		if (country == null) {
			return null;
		}
		return country.getName();
	}

	private static String makeSubOrderNotifyEmailTitle(Long id, String processType, OrderStatus status) {
		return "订单"+id+":"+getStatusDisplayName(processType, status);
	}

	private static String makeZoneName(Zone zone, Map<String, Zone> zones) {
		if (zone == null) {
			return null;
		}
		Zone tgtZone = zones.get(zone.getCode());
		if (tgtZone != null) {
			return tgtZone.getName();
		} else {
			return zone.getCode();
		}
	}

	public static List<String> queryValidActions(String processType, OrderStatus orderStatus, Customer customer,
			User user, boolean isAdminUser) {
		List<String> result = new ArrayList<String>();
		if (orderStatus == null){
			orderStatus = OrderStatus.ORDERED;
		}
		if (customer != null) {
			queryValidCustomerActions(result, processType, orderStatus);
		}
		if (user != null) {
			if (isAdminUser) {
				queryValidAdminActions(result, processType, orderStatus);
			} else {
				queryValidUserActions(result, processType, orderStatus);
			}
		}

		return result;
	}

	private static void queryValidAdminActions(List<String> result, String processType, OrderStatus orderStatus) {
		if (GlobalConstants.OrderProcessType_PayFirst.equals(processType)) {
			// 先付款的订单
			switch (orderStatus) {
			case SHIPPED:
				result.add(GlobalConstants.OrderAction_ConfirmReceipt);
				result.add(GlobalConstants.OrderAction_Reset);
				break;
			case RECEIPT:
				result.add(GlobalConstants.OrderAction_Close);
				result.add(GlobalConstants.OrderAction_Reset);
				break;
			case PAID:
				result.add(GlobalConstants.OrderAction_ConfirmPay);
				result.add(GlobalConstants.OrderAction_Ship);
				result.add(GlobalConstants.OrderAction_ConfirmReceipt);
				result.add(GlobalConstants.OrderAction_Reset);
				break;
			case PAY_CONFIRMED:
				result.add(GlobalConstants.OrderAction_Ship);
				result.add(GlobalConstants.OrderAction_ConfirmReceipt);
				result.add(GlobalConstants.OrderAction_Reset);
				break;
			case CANCEL:
			case REFUNDED:
			case CLOSE:
				result.add(GlobalConstants.OrderAction_Reset);
				break;
			case PROCESSED:
				result.add(GlobalConstants.OrderAction_Cancel);
				result.add(GlobalConstants.OrderAction_ConfirmPay);
				result.add(GlobalConstants.OrderAction_Ship);
				result.add(GlobalConstants.OrderAction_ConfirmReceipt);
				result.add(GlobalConstants.OrderAction_Reset);
				result.add(GlobalConstants.OrderAction_Close);
				break;
			case ORDERED:
			default:
				result.add(GlobalConstants.OrderAction_ConfirmOrder);
				result.add(GlobalConstants.OrderAction_AdjustPrice);
				result.add(GlobalConstants.OrderAction_Cancel);
				result.add(GlobalConstants.OrderAction_ConfirmPay);
				result.add(GlobalConstants.OrderAction_Ship);
				result.add(GlobalConstants.OrderAction_ConfirmReceipt);
				result.add(GlobalConstants.OrderAction_Reset);
				result.add(GlobalConstants.OrderAction_Close);
				break;

			}
			return;
		}

		// 先发货的订单
		switch (orderStatus) {
		case SHIPPED:
			result.add(GlobalConstants.OrderAction_ConfirmPay);
			result.add(GlobalConstants.OrderAction_ConfirmReceipt);
			result.add(GlobalConstants.OrderAction_AdjustPrice);
			result.add(GlobalConstants.OrderAction_Reset);
			break;
		case RECEIPT:
			result.add(GlobalConstants.OrderAction_ConfirmPay);
			result.add(GlobalConstants.OrderAction_Reset);
			break;
		case PAID:
			result.add(GlobalConstants.OrderAction_ConfirmPay);
			result.add(GlobalConstants.OrderAction_Reset);
			break;
		case PAY_CONFIRMED:
			result.add(GlobalConstants.OrderAction_Close);
			result.add(GlobalConstants.OrderAction_Reset);
			break;
		case CANCEL:
		case REFUNDED:
		case CLOSE:
			result.add(GlobalConstants.OrderAction_Reset);
			break;
		case ORDERED:
			result.add(GlobalConstants.OrderAction_ConfirmOrder);
			result.add(GlobalConstants.OrderAction_AdjustPrice);
		case PROCESSED:
		default:
			result.add(GlobalConstants.OrderAction_Ship);
			result.add(GlobalConstants.OrderAction_ConfirmPay);
			result.add(GlobalConstants.OrderAction_ConfirmReceipt);
			result.add(GlobalConstants.OrderAction_Cancel);
			result.add(GlobalConstants.OrderAction_Reset);
			result.add(GlobalConstants.OrderAction_Close);
			break;
		}
	}

	// 买家操作
	private static void queryValidCustomerActions(List<String> result, String processType, OrderStatus orderStatus) {
		if (GlobalConstants.OrderProcessType_PayFirst.equals(processType)) {
			// 先付款的订单
			switch (orderStatus) {
			case SHIPPED:
				result.add(GlobalConstants.OrderAction_ConfirmReceipt);
				break;
			case RECEIPT:
				break;
			case PAID:
				result.add(GlobalConstants.OrderAction_UploadPayProof);
				break;
			case PAY_CONFIRMED:
				break;
			case CLOSE:
				result.add(GlobalConstants.OrderAction_Review);
				break;
			
			case CANCEL:
			case REFUNDED:
				break;
			case PROCESSED:
				result.add(GlobalConstants.OrderAction_Cancel);
				result.add(GlobalConstants.OrderAction_UploadPayProof);
				break;
			case ORDERED:
			default:
				result.add(GlobalConstants.OrderAction_Cancel);
				break;
			}
			return;
		}

		// 先发货的订单
		switch (orderStatus) {
		case SHIPPED:
			result.add(GlobalConstants.OrderAction_ConfirmReceipt);
			break;
		case RECEIPT:
			result.add(GlobalConstants.OrderAction_UploadPayProof);
			break;
		case CLOSE:
			result.add(GlobalConstants.OrderAction_Review);
			break;
		case PAID:
			result.add(GlobalConstants.OrderAction_UploadPayProof);
			break;
		case PAY_CONFIRMED:
		case CANCEL:
		case REFUNDED:
			break;
		case PROCESSED:
		case ORDERED:
		default:
			result.add(GlobalConstants.OrderAction_Cancel);
			break;
		}

	}

	private static void queryValidUserActions(List<String> result, String processType, OrderStatus orderStatus) {
		if (GlobalConstants.OrderProcessType_PayFirst.equals(processType)) {
			// 先付款的订单
			switch (orderStatus) {
			case SHIPPED:
				break;
			case RECEIPT:
				result.add(GlobalConstants.OrderAction_Close);
				break;
			case PAID:
				result.add(GlobalConstants.OrderAction_ConfirmPay);
				break;
			case PAY_CONFIRMED:
				result.add(GlobalConstants.OrderAction_Ship);
				break;
			case CANCEL:
			case REFUNDED:
			case CLOSE:
			case REVIEW:
				break;
			case PROCESSED:
				result.add(GlobalConstants.OrderAction_ConfirmPay);
				result.add(GlobalConstants.OrderAction_Ship);
				break;
			case ORDERED:
			default:
				result.add(GlobalConstants.OrderAction_ConfirmOrder);
				result.add(GlobalConstants.OrderAction_AdjustPrice);
				break;
			}
			return;
		}

		// 先发货的订单
		switch (orderStatus) {
		case SHIPPED:
			result.add(GlobalConstants.OrderAction_ConfirmPay);
			break;
		case RECEIPT:
			result.add(GlobalConstants.OrderAction_ConfirmPay);
			break;
		case PAID:
			result.add(GlobalConstants.OrderAction_ConfirmPay);
			break;
		case PAY_CONFIRMED:
			result.add(GlobalConstants.OrderAction_Close);
			break;
		case CANCEL:
		case REFUNDED:
		case CLOSE:
		case REVIEW:
			break;
		case PROCESSED:
			result.add(GlobalConstants.OrderAction_Ship);
			break;
		case ORDERED:
		default:
			result.add(GlobalConstants.OrderAction_ConfirmOrder);
			result.add(GlobalConstants.OrderAction_AdjustPrice);
			break;
		}
	}

}