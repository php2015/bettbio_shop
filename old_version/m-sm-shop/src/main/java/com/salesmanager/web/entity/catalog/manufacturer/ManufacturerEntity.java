package com.salesmanager.web.entity.catalog.manufacturer;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.catalog.ProductList;
import com.salesmanager.web.entity.catalog.category.ReadableCategory;
import com.salesmanager.web.entity.shop.Breadcrumb;



public class ManufacturerEntity extends Manufacturer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int order;
	private List<ReadableCategory> categoryList;
	private List<ReadableManufacturer> manufacturerList;
	private Breadcrumb breadcrumb;
	private ProductList productList;
	
	public void setOrder(int order) {
		this.order = order;
	}
	public int getOrder() {
		return order;
	}
	public List<ReadableCategory> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<ReadableCategory> categoryList) {
		this.categoryList = categoryList;
	}
	public List<ReadableManufacturer> getManufacturerList() {
		return manufacturerList;
	}
	public void setManufacturerList(List<ReadableManufacturer> manufacturerList) {
		this.manufacturerList = manufacturerList;
	}
	public Breadcrumb getBreadcrumb() {
		return breadcrumb;
	}
	public void setBreadcrumb(Breadcrumb breadcrumb) {
		this.breadcrumb = breadcrumb;
	}
	public ProductList getProductList() {
		return productList;
	}
	public void setProductList(ProductList productList) {
		this.productList = productList;
	}

	
}
