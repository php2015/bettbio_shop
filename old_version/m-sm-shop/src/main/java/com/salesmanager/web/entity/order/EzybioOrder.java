package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.salesmanager.core.business.common.model.InvoiceInfo;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.web.entity.Entity;
import com.salesmanager.web.entity.customer.CustomerAdress;

public class EzybioOrder extends Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2842585534815698250L;
	private BigDecimal total;	
	private OrderStatus orderStatus;
	private Date datePurchased;
	private List<SubOrder> subOrders;
	private CustomerAdress customerAdress;
	private InvoiceInfo customerInvoice;
	private CustomerAdress invoiceAddress;
	private String billName;
	private String buyerCompany;
	private String orderID;
	private Order rawOrder;
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getDatePurchased() {
		return datePurchased;
	}
	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}	
	
	public List<SubOrder> getSubOrders() {
		return subOrders;
	}
	public void setSubOrders(List<SubOrder> subOrders) {
		this.subOrders = subOrders;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public CustomerAdress getInvoiceAddress() {
		return invoiceAddress;
	}
	public void setInvoiceAddress(CustomerAdress invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}
	public CustomerAdress getCustomerAdress() {
		return customerAdress;
	}
	public void setCustomerAdress(CustomerAdress customerAdress) {
		this.customerAdress = customerAdress;
	}
	public InvoiceInfo getCustomerInvoice() {
		return customerInvoice;
	}
	public void setCustomerInvoice(InvoiceInfo customerInvoice) {
		this.customerInvoice = customerInvoice;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getBuyerCompany() {
		return buyerCompany;
	}
	public void setBuyerCompany(String buyerCompany) {
		this.buyerCompany = buyerCompany;
	}
	public Order getRawOrder() {
		return rawOrder;
	}
	public void setRawOrder(Order rawOrder) {
		this.rawOrder = rawOrder;
	}
	

}
