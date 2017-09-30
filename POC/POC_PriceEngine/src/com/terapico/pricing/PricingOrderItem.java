package com.terapico.pricing;

import java.math.BigDecimal;
import java.util.List;

public class PricingOrderItem {
	private String productId;
	private String skuId;
	private List<String> categories;
	private BigDecimal orignalUnitPrice;
	private BigDecimal orignalTotalPrice;
	private int sequenceNo;
	private String brandKey;
	private String merchantKey;
	private BigDecimal quantity;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public BigDecimal getOrignalUnitPrice() {
		return orignalUnitPrice;
	}
	public void setOrignalUnitPrice(BigDecimal orignalUnitPrice) {
		this.orignalUnitPrice = orignalUnitPrice;
	}
	public BigDecimal getOrignalTotalPrice() {
		return orignalTotalPrice;
	}
	public void setOrignalTotalPrice(BigDecimal orignalTotalPrice) {
		this.orignalTotalPrice = orignalTotalPrice;
	}
	public int getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getBrandKey() {
		return brandKey;
	}
	public void setBrandKey(String brandKey) {
		this.brandKey = brandKey;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	
}
