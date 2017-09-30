package com.salesmanager.core.business.catalog.category.model;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class CategoryList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2559777854181093666L;
	List<Category> cats;
	public List<Category> getCats() {
		return cats;
	}
	public void setCats(List<Category> cats) {
		this.cats = cats;
	}

}
