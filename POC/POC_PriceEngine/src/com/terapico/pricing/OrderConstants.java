package com.terapico.pricing;

public class OrderConstants {
	public static enum COUNTRY {
		CHINA,OTHER
	}
	
	public static enum CONTACT_METHOD {
		EMAIL, MOBILE_CHINA, MOBILE_GLOBAL, QQ, WECHAT, INTEGLE_GROUP,OTHER
	}
	
	public static enum ORDER_SOURCE {
		BAITU_SITE, INTEGLE
	}
	
	public static enum BUYER_SOURCE {
		BAITU_CUSTOMER, INTEGLE
	}
	
	public static enum PRODUCT_SOURCE {
		BAITU_DATABASE
	}
	public static enum PRICING_SCOPE {
		TOTAL, ORDER, SUB_ORDER, ITEM_TOTAL, ITEM_UNIT, SHIPPING_FEE,TAX_FEE
	}
	
	public static enum PAY_METHOD {
		CASH, BUYER_POINT, CREDIT, CHARGE_ACCOUNT
	}
}
