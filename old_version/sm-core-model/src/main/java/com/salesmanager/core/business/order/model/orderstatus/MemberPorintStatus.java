package com.salesmanager.core.business.order.model.orderstatus;


/**
 * 
 * @author Lgh
 *枚举型积分状态
 */
public enum MemberPorintStatus {
	
	ACCOUNT("0"),//已到账
	NOTACCOUNT("1"),  //未到账		
	REVOKE("2"),	//已撤销
	;
	
	private String value;
	
	private MemberPorintStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
