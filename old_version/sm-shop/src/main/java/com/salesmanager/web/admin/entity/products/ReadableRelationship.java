package com.salesmanager.web.admin.entity.products;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class ReadableRelationship extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1301260549060631056L;
	private String name;
	private boolean active=false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
