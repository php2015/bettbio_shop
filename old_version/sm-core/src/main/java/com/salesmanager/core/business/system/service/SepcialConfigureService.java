package com.salesmanager.core.business.system.service;

import java.util.Map;

public interface SepcialConfigureService {

	public static final String KEY_SEND_EMAIL_TO_MERCHANT_WHEN_SUBMIT_ORDER = "emailOp1";
	public static final String KEY_CERTIFIED_CUSTOMER_BY_DEFAULT = "certifyRegisterCustomer";
	public static final String KEY_ACTIVE_CUSTOMER_BY_DEFAULT = "activeRegisterCustomer";
	public static final String KEY_USE_CUSTOMER_SALES_DO_ORDER_NOTIFY = "orderNofifyForceCustomerCurrentSales";
	public static final String KEY_SYSTEM_PURCHASING_AGENT_EMAIL = "purchasingAgentEmail";
	public static final String KEY_SYSTEM_PURCHASING_AGENT_Mobile = "purchasingAgentMobile";
	public static final String KEY_ADMIN_SEARCH_PRODUCT_WAY = "adminSearchProductWay";
	
	void saveConfigure(Map<String, Object> config);
	Map<String, Object> getAllConfigure();
	
	String getStringCfg(String key);
	boolean getBooleanCfg(String key);
	Long getLongCfg(String key);
	Double getDoubleCfg(String key);
	Object getCfg(String key);

}
