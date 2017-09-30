package com.salesmanager.core.business.order.service;

public class OrderServiceV2Constants {

	//订单邮件模板
	public static final String EMAIL_TEMPLATE_Customer_AdjustPrice = "email_template_adjustOrderPrice4Customer.ftl";
	public static final String EMAIL_TEMPLATE_Monitor_AdjustPrice = "email_template_adjustOrderPrice4Monitor.ftl";
	public static final String EMAIL_TEMPLATE_Merchant_AdjustPrice = "email_template_adjustOrderPrice4Merchant.ftl";
	public static final String EMAIL_TEMPLATE_Customer_ConfirmOrder = "email_template_confirmOrder4Customer.ftl";
	public static final String EMAIL_TEMPLATE_Merchant_ConfirmOrder = "email_template_confirmOrder4Merchant.ftl";
	public static final String EMAIL_TEMPLATE_Monitor_ConfirmOrder = "email_template_confirmOrder4Monitor.ftl";
	public static final String EMAIL_TEMPLATE_Customer_Status = "email_template_orderStatus4Customer.ftl";
	public static final String EMAIL_TEMPLATE_Merchant_Status = "email_template_orderStatus4Merchant.ftl";
	public static final String EMAIL_TEMPLATE_Monitor_Status = "email_template_orderStatus4Monitor.ftl";
	public static final String EMAIL_TEMPLATE_Customer_PlaceOrder = "email_template_placeOrder4Customer.ftl";
	public static final String EMAIL_TEMPLATE_Merchant_PlaceOrder = "email_template_placeOrder4Merchant.ftl";
	public static final String EMAIL_TEMPLATE_Monitor_PlaceOrder = "email_template_placeOrder4Monitor.ftl";
	
	
	private static final int CODE_BASE = 1000;
	public static final int CODE_NO_CUSTOMER = CODE_BASE+1;
	public static final int CODE_NOT_ACTIVE_CUSTOMER = CODE_BASE+2;
	public static final int CODE_NO_DELIVERY = CODE_BASE+3;
	public static final int CODE_NO_INVOICE_ADDRESS = CODE_BASE+4;
	public static final int CODE_NO_ANY_PRODUCT = CODE_BASE+5;
	public static final int CODE_NO_ORDER = CODE_BASE+6;
	public static final int CODE_NO_SUBORDER = CODE_BASE+7;
	public static final int CODE_WRONG_PARAM = CODE_BASE+8;
	public static final int CODE_NO_PERMISSION = CODE_BASE+9;
	public static final int CODE_INVALID_OPERATION = CODE_BASE+10;
	
	public static final String MESSAGE_NO_CUSTOMER = "no_customer";
	public static final String MESSAGE_NOT_ACTIVE_CUSTOMER = "not_active_customer";
	public static final String MESSAGE_NO_DELIVERY = "no_delivery";
	public static final String MESSAGE_NO_INVOICE_ADDRESS = "no_invoice_address";
	public static final String MESSAGE_NO_ANY_PRODUCT = "no_any_product";
	public static final String MESSAGE_NO_ORDER = "no_any_order";
	public static final String MESSAGE_NO_SUBORDER = "no_any_suborder";
	public static final String MESSAGE_WRONG_PARAM = "wrong_param";
	public static final String MESSAGE_NO_PERMISSION = "no_permission";
	public static final String MESSAGE_INVALID_OPERATION = "invalid_operation";
	
	public static final String ERROR_MSG_PREFIX = "order.error.message.";
}
