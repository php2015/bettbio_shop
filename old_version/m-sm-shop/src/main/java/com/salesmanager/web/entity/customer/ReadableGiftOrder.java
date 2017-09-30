package com.salesmanager.web.entity.customer;

import java.io.Serializable;

import com.salesmanager.core.business.customer.model.GiftStatus;

public class ReadableGiftOrder  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String imge;
	private String customerName;
	private int qulity;
	private String deliveryName;
	private String deliverAddress;
	private String deliverCompany;
	private String deliverNumber;
	private String orderDate;
	private GiftStatus status;
	private long id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImge() {
		return imge;
	}
	public void setImge(String imge) {
		this.imge = imge;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getQulity() {
		return qulity;
	}
	public void setQulity(int qulity) {
		this.qulity = qulity;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDeliverAddress() {
		return deliverAddress;
	}
	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}
	public String getDeliverCompany() {
		return deliverCompany;
	}
	public void setDeliverCompany(String deliverCompany) {
		this.deliverCompany = deliverCompany;
	}
	public GiftStatus getStatus() {
		return status;
	}
	public void setStatus(GiftStatus status) {
		this.status = status;
	}
	public String getDeliverNumber() {
		return deliverNumber;
	}
	public void setDeliverNumber(String deliverNumber) {
		this.deliverNumber = deliverNumber;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

}
