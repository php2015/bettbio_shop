package com.salesmanager.web.entity.catalog.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.salesmanager.web.shop.model.paging.PaginationData;

public class ProductsList implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private List<Product> products = new ArrayList<Product>();
	private PaginationData paginationData;
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public PaginationData getPaginationData() {
		return paginationData;
	}
	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}

}
