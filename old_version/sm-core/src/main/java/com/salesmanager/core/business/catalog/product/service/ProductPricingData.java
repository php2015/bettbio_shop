package com.salesmanager.core.business.catalog.product.service;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;

public class ProductPricingData {
	private Long productId;
	private Product product;
	private List<ProductSpecPricingData> prices;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public List<ProductSpecPricingData> getPrices() {
		return prices;
	}
	public void setPrices(List<ProductSpecPricingData> prices) {
		this.prices = prices;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
