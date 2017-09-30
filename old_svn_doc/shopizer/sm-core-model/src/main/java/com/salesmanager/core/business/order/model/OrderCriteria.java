package com.salesmanager.core.business.order.model;

import java.util.List;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;

public class OrderCriteria extends Criteria {
	
	private String paymentMethod;
	private List<Long> customerId;
	private long cId = -1;
	//用于组合查询
	private String findName;
	private String beginDatePurchased ;
	private OrderStatus status =null;
	private String billingUserName;
	private String productName;
	private Long orderID =null;
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
   
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public String getBillingUserName() {
		return billingUserName;
	}
	public void setBillingUserName(String billingUserName) {
		this.billingUserName = billingUserName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}	
	
	public List<Long> getCustomerId() {
		return customerId;
	}
	public void setCustomerId(List<Long> customerId) {
		this.customerId = customerId;
	}
	public Long getOrderID() {
		return orderID;
	}
	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	public String getFindName() {
		return findName;
	}
	public void setFindName(String findName) {
		this.findName = findName;
	}
	public String getBeginDatePurchased() {
		return beginDatePurchased;
	}
	public void setBeginDatePurchased(String beginDatePurchased) {
		this.beginDatePurchased = beginDatePurchased;
	}
	public long getcId() {
		return cId;
	}
	public void setcId(long cId) {
		this.cId = cId;
	}

}
