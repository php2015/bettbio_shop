package com.salesmanager.core.business.customer.model;

public enum GiftStatus {
	
	ORDERED("0"),//已下单
	SHIPPED("1"),  //已发货		
	REVOKE("2"),	//已撤销
	;
	
	private String value;
	
	private GiftStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
