package com.salesmanager.core.business.order.model.orderstatus;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

public enum OrderStatus {
	
	ORDERED("ordered"),
	SHIPPED("shipped"),
	RECEIPT("receipt"),
	PAID("paid"),
	REFUNDED("refunded"),
	PROCESSED("processed"),
	CLOSE("close"),
	CANCEL("cancel"),
	REVIEW("review"),
	PAY_CONFIRMED("pay_confirmed")
	;
	
	private String value;
	
	private OrderStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	/**
	 * Note: 订单的状态顺序不要随便改变
	 */
	@SuppressWarnings("unchecked")
	private static List<OrderStatus> sStatusList = Arrays.asList(new OrderStatus[]{
			ORDERED, PAID, PAY_CONFIRMED, SHIPPED, CLOSE, REVIEW, PROCESSED, REFUNDED
	});
	public static OrderStatus calcFinalStatus(List<OrderStatus> statusList){
		if (statusList == null || statusList.isEmpty()){
			return ORDERED;
		}
		int pos = sStatusList.size()-1;
		for(OrderStatus st : statusList){
			pos = Math.min(pos, sStatusList.indexOf(st));
		}
		if (pos < 0){
			return ORDERED;
		}
		return sStatusList.get(pos);
	}
}
