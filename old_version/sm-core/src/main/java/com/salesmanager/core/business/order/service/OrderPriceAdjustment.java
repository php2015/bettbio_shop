package com.salesmanager.core.business.order.service;

import java.util.Map;

public class OrderPriceAdjustment {
	private Long oid;
	private Map<Long, SubOrderPriceAdjustment> suborder;
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public Map<Long, SubOrderPriceAdjustment> getSuborder() {
		return suborder;
	}
	public void setSuborder(Map<Long, SubOrderPriceAdjustment> suborder) {
		this.suborder = suborder;
	}
	
	
}
