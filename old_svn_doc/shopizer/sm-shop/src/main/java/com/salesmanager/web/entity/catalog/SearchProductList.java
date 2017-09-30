package com.salesmanager.web.entity.catalog;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.salesmanager.core.business.search.model.IndexProduct;
import com.salesmanager.web.entity.catalog.category.ReadableCategory;

/**
 * Object representing the results of a search query
 * @author Carl Samson
 *
 */
public class SearchProductList  implements Serializable{
	private int productCount;
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public List<IndexProduct> getProducts() {
		return products;
	}
	public void setProducts(List<IndexProduct> products) {
		this.products = products;
	}
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private String jsonString;

	private static final long serialVersionUID = 1L;
	private String activeMenu;
	private List<IndexProduct> products = new ArrayList<IndexProduct>();
	
	
	private List<ReadableCategory> categoryFacets = new ArrayList<ReadableCategory>();
	public List<ReadableCategory> getCategoryFacets() {
		return categoryFacets;
	}
	public void setCategoryFacets(List<ReadableCategory> categoryFacets) {
		this.categoryFacets = categoryFacets;
	}
	public String getActiveMenu() {
		return activeMenu;
	}
	public void setActiveMenu(String activeMenu) {
		this.activeMenu = activeMenu;
	}


}
