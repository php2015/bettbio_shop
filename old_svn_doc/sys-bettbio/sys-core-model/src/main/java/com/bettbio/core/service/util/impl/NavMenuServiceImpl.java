package com.bettbio.core.service.util.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.util.NavMenu;
import com.bettbio.core.service.search.CategoryService;
import com.bettbio.core.service.util.NavMenuService;

@Service
public class NavMenuServiceImpl implements NavMenuService {

	@Autowired
	CategoryService categoryService;

	@Override
	public NavMenu getNavMenu() {
		NavMenu navMenu = new NavMenu();
		List<Category> menus = categoryService.selectTopCategory();
		for (Category category : menus) {
			category.setSonCategory(categoryService.selectAllSubCategary(category.getId()));
		}
		navMenu.setMenus(menus);
		return navMenu;
	}
}
