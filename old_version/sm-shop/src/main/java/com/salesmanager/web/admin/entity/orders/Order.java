package com.salesmanager.web.admin.entity.orders;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;




import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.salesmanager.core.business.common.model.BillingInfo;
import com.salesmanager.core.business.common.model.InvoiceInfo;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.zone.model.Zone;


public class Order implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long Id;
	private String orderHistoryComment = "";
	
	List<OrderStatus> orderStatusList = Arrays.asList(OrderStatus.values());     
	private String datePurchased = "";
	private  com.salesmanager.core.business.order.model.Order order;
			
	public InvoiceInfo getBillingInvoice() {
		return billingInvoice;
	}

	public void setBillingInvoice(InvoiceInfo billingInvoice) {
		this.billingInvoice = billingInvoice;
	}
		
	private InvoiceInfo billingInvoice = null;
	
	
	public String getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(String datePurchased) {
		this.datePurchased = datePurchased;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getOrderHistoryComment() {
		return orderHistoryComment;
	}

	public void setOrderHistoryComment(String orderHistoryComment) {
		this.orderHistoryComment = orderHistoryComment;
	}

	public List<OrderStatus> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<OrderStatus> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public com.salesmanager.core.business.order.model.Order getOrder() {
		return order;
	}

	public void setOrder(com.salesmanager.core.business.order.model.Order order) {
		this.order = order;
	}

	private String billingUserName;
	
	private String billingCompany;
	
	private String billingAddress;	
	
	private String billingCity;
	
	private String billingPostalCode;
	
	private String billingTelephone;
	
	private Zone billingZone;
	
	private Country billingCountry;
	
	private String ivoiceUserName;
	
	private String ivoiceCompany;
	
	private String ivoiceAddress;	
	
	private String ivoiceCity;
	
	private String ivoicePostalCode;
	
	private String ivoiceTelephone;
	
	private Zone ivoiceZone;
	
	private Country ivoiceCountry;

	public String getBillingUserName() {
		return billingUserName;
	}

	public void setBillingUserName(String billingUserName) {
		this.billingUserName = billingUserName;
	}

	public String getBillingCompany() {
		return billingCompany;
	}

	public void setBillingCompany(String billingCompany) {
		this.billingCompany = billingCompany;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingPostalCode() {
		return billingPostalCode;
	}

	public void setBillingPostalCode(String billingPostalCode) {
		this.billingPostalCode = billingPostalCode;
	}

	public String getBillingTelephone() {
		return billingTelephone;
	}

	public void setBillingTelephone(String billingTelephone) {
		this.billingTelephone = billingTelephone;
	}

	public Zone getBillingZone() {
		return billingZone;
	}

	public void setBillingZone(Zone billingZone) {
		this.billingZone = billingZone;
	}

	public Country getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(Country billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getIvoiceUserName() {
		return ivoiceUserName;
	}

	public void setIvoiceUserName(String ivoiceUserName) {
		this.ivoiceUserName = ivoiceUserName;
	}

	public String getIvoiceCompany() {
		return ivoiceCompany;
	}

	public void setIvoiceCompany(String ivoiceCompany) {
		this.ivoiceCompany = ivoiceCompany;
	}

	public String getIvoiceAddress() {
		return ivoiceAddress;
	}

	public void setIvoiceAddress(String ivoiceAddress) {
		this.ivoiceAddress = ivoiceAddress;
	}

	public String getIvoiceCity() {
		return ivoiceCity;
	}

	public void setIvoiceCity(String ivoiceCity) {
		this.ivoiceCity = ivoiceCity;
	}

	public String getIvoicePostalCode() {
		return ivoicePostalCode;
	}

	public void setIvoicePostalCode(String ivoicePostalCode) {
		this.ivoicePostalCode = ivoicePostalCode;
	}

	public String getIvoiceTelephone() {
		return ivoiceTelephone;
	}

	public void setIvoiceTelephone(String ivoiceTelephone) {
		this.ivoiceTelephone = ivoiceTelephone;
	}

	public Zone getIvoiceZone() {
		return ivoiceZone;
	}

	public void setIvoiceZone(Zone ivoiceZone) {
		this.ivoiceZone = ivoiceZone;
	}

	public Country getIvoiceCountry() {
		return ivoiceCountry;
	}

	public void setIvoiceCountry(Country ivoiceCountry) {
		this.ivoiceCountry = ivoiceCountry;
	}

	
}