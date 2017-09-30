package com.salesmanager.web.admin.entity.catalog;

import java.io.Serializable;


public class ReadablePrice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5813334466520918259L;
	private String priceId;
	private String productId;
	private String name;
	private String price;
	private String period;
	private String specialPrice;
	private String begindt;
	private String enddt;
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}
	public String getBegindt() {
		return begindt;
	}
	public void setBegindt(String begindt) {
		this.begindt = begindt;
	}
	public String getEnddt() {
		return enddt;
	}
	public void setEnddt(String enddt) {
		this.enddt = enddt;
	}
	
	
}
