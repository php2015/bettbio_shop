package com.bettbio.core.model.util;

import java.io.Serializable;

public class BreadCrumbItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String label;
	private String url;
	private BreadCrumbItemType itemType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BreadCrumbItemType getItemType() {
		return itemType;
	}

	public void setItemType(BreadCrumbItemType itemType) {
		this.itemType = itemType;
	}
	
	@Override
	public String toString() {
		return "BreadCrumbItem("
				+ "id=" + this.getId()
				+ ",label=" + this.getLabel()
				+ ",url=" + this.getUrl()
				+ ",itemType=" + this.getItemType()
				+ ")";
	}
}
