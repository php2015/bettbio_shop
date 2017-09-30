package com.salesmanager.web.entity.catalog.category;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class ReadableCatagoryList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2693677860694378912L;
	private List<Category> cats;
	public List<Category> getCats() {
		return cats;
	}
	public void setCats(List<Category> cats) {
		this.cats = cats;
	}

}
