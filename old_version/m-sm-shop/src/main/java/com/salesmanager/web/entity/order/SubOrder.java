package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.web.entity.Entity;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;

public class SubOrder extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2628634632652329395L;
	private OrderStatus status;
	private Date lastModified;	
	private BigDecimal total=new BigDecimal(0);
	private List<ShoppingCartItem> cartItems;
	private String deliveryName ="";
	private String deliveryNum ="";
	@JsonIgnore
	private MerchantStore merchantStore;
	private List<ShoppingCartItem> shoppingCartItems;
	public List<ShoppingCartItem> getShoppingCartItems() {
		return shoppingCartItems;
	}
	public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public MerchantStore getMerchantStore() {
		return merchantStore;
	}
	public void setMerchantStore(MerchantStore merchantStore) {
		this.merchantStore = merchantStore;
	}
	public List<ShoppingCartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<ShoppingCartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDeliveryNum() {
		return deliveryNum;
	}
	public void setDeliveryNum(String deliveryNum) {
		this.deliveryNum = deliveryNum;
	}

}
