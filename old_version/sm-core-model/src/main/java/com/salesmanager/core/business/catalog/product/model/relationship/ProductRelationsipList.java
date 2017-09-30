package com.salesmanager.core.business.catalog.product.model.relationship;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class ProductRelationsipList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5687206709796168558L;
	
	private List<ProductRelationship> ships;

	public List<ProductRelationship> getShips() {
		return ships;
	}

	public void setShips(List<ProductRelationship> ships) {
		this.ships = ships;
	}

}
