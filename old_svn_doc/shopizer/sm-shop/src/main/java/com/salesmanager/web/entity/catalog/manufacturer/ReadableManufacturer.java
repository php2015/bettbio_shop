package com.salesmanager.web.entity.catalog.manufacturer;

import java.io.Serializable;

public class ReadableManufacturer extends ManufacturerEntity implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String manName;
	private ManufacturerDescription description;
	private int manCount;
	public void setDescription(ManufacturerDescription description) {
		this.description = description;
	}
	public ManufacturerDescription getDescription() {
		return description;
	}
	public String getManName() {
		return manName;
	}
	public void setManName(String manName) {
		this.manName = manName;
	}
	public int getManCount() {
		return manCount;
	}
	public void setManCount(int manCount) {
		this.manCount = manCount;
	}

}
