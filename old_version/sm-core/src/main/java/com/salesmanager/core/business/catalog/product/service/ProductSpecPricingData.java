package com.salesmanager.core.business.catalog.product.service;

import java.math.BigDecimal;

import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;

public class ProductSpecPricingData {
	private Long specId;
	private String specTitle;
	private BigDecimal listPrice;
	private String finalPriceType;
	private BigDecimal finalPrice;
	private double finalDiscount;
	private ProductPrice productPrice;
	
	public Long getSpecId() {
		return specId;
	}
	public void setSpecId(Long specId) {
		this.specId = specId;
	}
	public BigDecimal getListPrice() {
		return listPrice;
	}
	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}
	public String getFinalPriceType() {
		return finalPriceType;
	}
	public void setFinalPriceType(String finalPriceType) {
		this.finalPriceType = finalPriceType;
	}
	public BigDecimal getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}
	public String getSpecTitle() {
		return specTitle;
	}
	public void setSpecTitle(String specTitle) {
		this.specTitle = specTitle;
	}
	public double getFinalDiscount() {
		return finalDiscount;
	}
	public void setFinalDiscount(double finalDiscount) {
		this.finalDiscount = finalDiscount;
	}
	public ProductPrice getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
	}
	
	
}
