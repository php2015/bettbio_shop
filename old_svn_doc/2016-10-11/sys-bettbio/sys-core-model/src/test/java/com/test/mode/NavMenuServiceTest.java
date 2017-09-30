package com.test.mode;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.util.NavMenu;
import com.bettbio.core.service.util.NavMenuService;
import com.google.gson.Gson;
import com.test.mode.base.BaseT;

/**
 * 分类菜单测试
 * 
 * @author GuoChunbo
 *
 */
public class NavMenuServiceTest extends BaseT {

	@Autowired
	NavMenuService navMenuService;

	@Test
	public void navmenu() {

		NavMenu navMenu = navMenuService.getNavMenu();

		for (Category category : navMenu.getMenus()) {
			System.out.println("--" + category);
			for (Category subCate : category.getSonCategory()) {
				System.out.println("\t--" + subCate);
				for (Category thrCate : subCate.getSonCategory()) {
					System.out.println("\t\t--" + thrCate);
				}
			}
			System.out.println("-----------------------------------------------------");
		}

		// 测试缓存
		navMenuService.getNavMenu();
		System.out.println("MENU JSON : "+ new Gson().toJson(navMenu));
		System.out.println("MENU JSON : "+ JSON.toJSONString(navMenu));
	}
}
