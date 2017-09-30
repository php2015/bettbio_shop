package com.salesmanager.core.business.common.model;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;

public class EntityList implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6135941880202635567L;
	private int totalCount;
	private List<SalesManagerEntity> entitis;

	public List<SalesManagerEntity> getEntitis() {
		return entitis;
	}

	public void setEntitis(List<SalesManagerEntity> entitis) {
		this.entitis = entitis;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
