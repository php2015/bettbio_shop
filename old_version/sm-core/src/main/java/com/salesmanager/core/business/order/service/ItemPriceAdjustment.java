package com.salesmanager.core.business.order.service;

import java.util.Map;

import com.salesmanager.core.constants.GlobalConstants;

public class ItemPriceAdjustment {
	private long id;
	private GlobalConstants.OrderPriceType priceType;
	private double value;

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

}
