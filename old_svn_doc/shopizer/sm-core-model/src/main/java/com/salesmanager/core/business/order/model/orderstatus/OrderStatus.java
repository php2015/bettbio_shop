package com.salesmanager.core.business.order.model.orderstatus;

public enum OrderStatus {
	
	ORDERED("ordered"),
	SHIPPED("shipped"),
	PAID("paid"),
	REFUNDED("refunded"),
	PROCESSED("processed"),
	CLOSE("close"),
	REVIEW("review"),
	;
	
	private String value;
	
	private OrderStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
