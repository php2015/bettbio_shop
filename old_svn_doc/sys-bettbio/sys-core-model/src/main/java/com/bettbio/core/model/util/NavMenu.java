package com.bettbio.core.model.util;

import java.util.ArrayList;
import java.util.List;

import com.bettbio.core.model.search.Category;

public class NavMenu {

	private List<Category> menus = new ArrayList<Category>();

	public List<Category> getMenus() {
		return menus;
	}

	public void setMenus(List<Category> menus) {
		this.menus = menus;
	}

}
