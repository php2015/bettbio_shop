package com.bettbio.core.model.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BreadCrumb implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BreadCrumbItemType itemType;
	private List<BreadCrumbItem> breadCrumbs = new ArrayList<BreadCrumbItem>();

	public List<BreadCrumbItem> getBreadCrumbs() {
		return breadCrumbs;
	}

	public void setBreadCrumbs(List<BreadCrumbItem> breadCrumbs) {
		this.breadCrumbs = breadCrumbs;
	}

	public BreadCrumbItemType getItemType() {
		return itemType;
	}

	public void setItemType(BreadCrumbItemType itemType) {
		this.itemType = itemType;
	}

}
