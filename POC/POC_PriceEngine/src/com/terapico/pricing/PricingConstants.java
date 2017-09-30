package com.terapico.pricing;

public class PricingConstants {
	public static enum PRICE_SCOPE {
		WHOLE_TOTAL, ORDER_TOTAL, PACKAGE_TOTAL, ITEM_TOTAL, ITEM_UNIT, SHIPPING_FEE, TAX_FEE, OTHER;
	}
	
	public static enum ORDER_SOURCE {
		BAITU_SITE, INTLGE;
	}
	
	public static enum CALCULATOR_TYPE {
		CALC_LIST_PRICE, CALC_DISCOUNT, CALC_SHIPPING, CALC_TAX, CALC_PACKAGING;
	}
}
