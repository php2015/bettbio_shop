package com.salesmanager.core.constants;

import java.util.Currency;
import java.util.Locale;

public class Constants {
	
	public final static String TEST_ENVIRONMENT= "TEST";
	public final static String PRODUCTION_ENVIRONMENT= "PRODUCTION";
	public final static String SHOP_URI = "/shop";
	
	public static final String ALL_REGIONS = "*";
	
	
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String DEFAULT_DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
	public final static String DEFAULT_DATE_FORMAT_YEAR = "yyyy";
	
	public final static String EMAIL_CONFIG = "EMAIL_CONFIG";
	public final static String MERCHANT_CONFIG = "MERCHANT_CONFIG";
	
	public final static String UNDERSCORE = "_";
	public final static String SLASH = "/";
	public final static String TRUE = "true";
	public final static String FALSE = "false";
	public final static String OT_ITEM_PRICE_MODULE_CODE = "itemprice";
	public final static String OT_SUBTOTAL_MODULE_CODE = "subtotal";
	public final static String OT_TOTAL_MODULE_CODE = "total";
	public final static String OT_SHIPPING_MODULE_CODE = "shipping";
	public final static String OT_HANDLING_MODULE_CODE = "handling";
	public final static String OT_TAX_MODULE_CODE = "tax";
	public final static String OT_REFUND_MODULE_CODE = "refund";
	
//	public final static Locale DEFAULT_LOCALE = Locale.US;
//	public final static Currency DEFAULT_CURRENCY = Currency.getInstance(Locale.US);
	public final static Locale DEFAULT_LOCALE = Locale.CHINA;
	public final static Currency DEFAULT_CURRENCY = Currency.getInstance(Locale.CHINA);
	public final static String CATEGORY_LINEAGE_DELIMITER = "/";
	public final static int AUDIT_FAILED = -2;
	public final static int AUDIT_NONE = -1;
	public final static int AUDIT_PRE = 0;
	public final static int AUDIT_AUDITED=1;
	public final static int AUDIT_AGAIN =2;
	
	// 登录key
	public final static String LOGON_BY_NAME ="username";
	public final static String LOGON_BY_EMAIL ="email";
	public final static String LOGON_BY_MOBILE ="mobile";
	public static final String CURRENCY_CHINA_YUAN = "CNY";
	public static final String USER_SEGEMNT_LOGGED_CUSTOMER = "logged_customer";
	
	
	//#下单积分比例
    public final static String RATIO_BUTILE="RATIO_BUTILE";
    // #客户首次下单
    public final static String FIRST_SCORE="FIRST_SCORE";

}
