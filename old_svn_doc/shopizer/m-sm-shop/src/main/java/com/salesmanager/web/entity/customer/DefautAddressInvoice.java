package com.salesmanager.web.entity.customer;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.customer.model.CustomerInvoice;


public class DefautAddressInvoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2805469746484418136L;
	
	private CustomerAdress defaultAddress =null ;
	private long defaultAddresInvice=-1l;
	
	public long getDefaultAddresInvice() {
		return defaultAddresInvice;
	}

	public void setDefaultAddresInvice(long defaultAddresInvice) {
		this.defaultAddresInvice = defaultAddresInvice;
	}

	public CustomerAdress getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(CustomerAdress defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public List<CustomerAdress> getOthersAddresss() {
		return othersAddresss;
	}

	public void setOthersAddresss(List<CustomerAdress> othersAddresss) {
		this.othersAddresss = othersAddresss;
	}

	public CustomerInvoice getDefaultInvoice() {
		return defaultInvoice;
	}

	public void setDefaultInvoice(CustomerInvoice defaultInvoice) {
		this.defaultInvoice = defaultInvoice;
	}


	public Set<CustomerInvoice> getOthersInvoices() {
		return othersInvoices;
	}

	public void setOthersInvoices(Set<CustomerInvoice> othersInvoices) {
		this.othersInvoices = othersInvoices;
	}

	private long sizeOfAddress=0;
	public long getSizeOfAddress() {
		return sizeOfAddress;
	}

	public void setSizeOfAddress(long sizeOfAddress) {
		this.sizeOfAddress = sizeOfAddress;
	}

	public long getSizeOfInvoice() {
		return sizeOfInvoice;
	}

	public void setSizeOfInvoice(long sizeOfInvoice) {
		this.sizeOfInvoice = sizeOfInvoice;
	}

	private long sizeOfInvoice=0;
	
	private List<CustomerAdress> othersAddresss =null;
	
	private CustomerInvoice defaultInvoice =null;
	
	private Set<CustomerInvoice> othersInvoices =null;
}
