package com.salesmanager.core.business.order.service;

import java.util.Locale;
import java.util.Set;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.constants.GlobalConstants;

public class OrderServiceV2Context {
	protected Order order;
	protected Customer customer;
	protected MerchantStore store;
	protected User user;
	protected Locale locale;
	protected Language language;
	protected GlobalConstants.OrderCommitChannel commitChannel;
	private Set<CustomerAddress> customerAddressSet;
	protected boolean isAdminUser;
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public MerchantStore getStore() {
		return store;
	}
	public void setStore(MerchantStore store) {
		this.store = store;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public GlobalConstants.OrderCommitChannel getCommitChannel() {
		return commitChannel;
	}
	public void setCommitChannel(GlobalConstants.OrderCommitChannel commitChannel) {
		this.commitChannel = commitChannel;
	}
	public void setCustomerAddressSet(Set<CustomerAddress> tempAddress) {
		this.customerAddressSet = tempAddress;
	}
	public Set<CustomerAddress> getCustomerAddressSet() {
		return customerAddressSet;
	}
	public boolean isAdminUser() {
		return isAdminUser;
	}
	public void setAdminUser(boolean isAdminUser) {
		this.isAdminUser = isAdminUser;
	}
	
	
}
