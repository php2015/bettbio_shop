package com.salesmanager.web.admin.entity.products;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class ReadableOption extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4887322924788111325L;
	private String name;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
