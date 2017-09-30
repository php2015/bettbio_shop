package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.shipping.model.ShippingOption;
import com.salesmanager.core.business.shipping.model.ShippingSummary;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;

/**
 * Orders saved on the website
 * @author Carl Samson
 *
 */
public class ShopOrder extends PersistableOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private Set<SubOrder> subOrders;

	private OrderTotalSummary orderTotalSummary;//The order total displayed to the end user. That object will be used when committing the order
	
	
	private ShippingSummary shippingSummary;
	private ShippingOption selectedShippingOption = null;//Default selected option
	
	
	private String paymentMethodType = null;//user selected payment type
	private Map<String,String> payment;//user payment information
	
	private String errorMessage = null;
	
	private long addressid =-1l;
	
	public long getAddressid() {
		return addressid;
	}
	public void setAddressid(long addressid) {
		this.addressid = addressid;
	}
	public long getInvoiceid() {
		return invoiceid;
	}
	public void setInvoiceid(long invoiceid) {
		this.invoiceid = invoiceid;
	}
	public long getInvoicesendid() {
		return invoicesendid;
	}
	public void setInvoicesendid(long invoicesendid) {
		this.invoicesendid = invoicesendid;
	}
	private long invoiceid =-1l;
	
	private long invoicesendid =-1l;
	
	public void setOrderTotalSummary(OrderTotalSummary orderTotalSummary) {
		this.orderTotalSummary = orderTotalSummary;
	}
	public OrderTotalSummary getOrderTotalSummary() {
		return orderTotalSummary;
	}

	public ShippingSummary getShippingSummary() {
		return shippingSummary;
	}
	public void setShippingSummary(ShippingSummary shippingSummary) {
		this.shippingSummary = shippingSummary;
	}
	public ShippingOption getSelectedShippingOption() {
		return selectedShippingOption;
	}
	public void setSelectedShippingOption(ShippingOption selectedShippingOption) {
		this.selectedShippingOption = selectedShippingOption;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getPaymentMethodType() {
		return paymentMethodType;
	}
	public void setPaymentMethodType(String paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}
	public Map<String,String> getPayment() {
		return payment;
	}
	public void setPayment(Map<String,String> payment) {
		this.payment = payment;
	}
	public Set<SubOrder> getSubOrders() {
		return subOrders;
	}
	public void setSubOrders(Set<SubOrder> subOrders) {
		this.subOrders = subOrders;
	}
	
	

}
