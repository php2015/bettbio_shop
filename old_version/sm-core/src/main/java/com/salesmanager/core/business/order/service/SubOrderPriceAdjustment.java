package com.salesmanager.core.business.order.service;

import java.util.Map;

import com.salesmanager.core.constants.GlobalConstants;

public class SubOrderPriceAdjustment {
	private long id;
	private GlobalConstants.OrderPriceType priceType;
	private double value;
	private Map<Long, ItemPriceAdjustment> items;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public GlobalConstants.OrderPriceType getPriceType() {
		return priceType;
	}
	public void setPriceType(GlobalConstants.OrderPriceType priceType) {
		this.priceType = priceType;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Map<Long, ItemPriceAdjustment> getItems() {
		return items;
	}
	public void setItems(Map<Long, ItemPriceAdjustment> items) {
		this.items = items;
	}
	
}
