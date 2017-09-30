package com.salesmanager.core.business.catalog.product.service;

import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;

public class ProductPricingContext {
	private Customer customer;
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
		
	
}
