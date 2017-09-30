package com.salesmanager.core.business.catalog.product.model.manufacturer;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class ManufactureList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5683911238765988771L;
	
	private List<Manufacturer> manus ;

	public List<Manufacturer> getManus() {
		return manus;
	}

	public void setManus(List<Manufacturer> manus) {
		this.manus = manus;
	}
	

}
