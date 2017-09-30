package com.salesmanager.web.entity.catalog.category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;

public class ReadableCategory extends CategoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ReadableCategory> sonCategory;
	
	private int productCount;
	
	private CategoryDescription description;//one category based on language
	
	public CategoryDescription getDescription() {
		return description;
	}
	public void setDescription(CategoryDescription description) {
		this.description = description;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public List<ReadableCategory> getSonCategory() {
		if(sonCategory == null) sonCategory = new ArrayList<ReadableCategory>();
		return sonCategory;
	}
	public void setSonCategory(List<ReadableCategory> sonCategory) {
		this.sonCategory = sonCategory;
	}

}
