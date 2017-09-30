package com.salesmanager.web.admin.entity.products;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class ReadableRalationshipList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2797820571169163769L;
	
	private List<ReadableRelationship> ships;

	public List<ReadableRelationship> getShips() {
		return ships;
	}

	public void setShips(List<ReadableRelationship> ships) {
		this.ships = ships;
	}

}
