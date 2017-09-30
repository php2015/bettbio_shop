package com.salesmanager.web.admin.entity.products;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class ReadableManufactureList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7514101762844596561L;
	private List<ReadableManufacture> manus;
	public List<ReadableManufacture> getManus() {
		return manus;
	}
	public void setManus(List<ReadableManufacture> manus) {
		this.manus = manus;
	}

}
