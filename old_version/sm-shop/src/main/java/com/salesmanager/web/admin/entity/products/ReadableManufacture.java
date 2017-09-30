package com.salesmanager.web.admin.entity.products;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class ReadableManufacture extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8114893431042476609L;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String url;

}
