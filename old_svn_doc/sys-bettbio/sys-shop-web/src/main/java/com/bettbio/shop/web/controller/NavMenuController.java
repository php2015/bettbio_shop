package com.bettbio.shop.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.util.NavMenu;
import com.bettbio.core.service.util.NavMenuService;

/**
 * 分类菜单
 * 
 * @author GuoChunbo
 *
 */
@Controller
@RequestMapping("/navmenu")
public class NavMenuController{

	private static final Logger LOGGER = LoggerFactory.getLogger(NavMenuController.class);

	@Autowired
	NavMenuService navMenuService;

	@ResponseBody
	@RequestMapping()
	public NavMenu getNavMenu() {

		try {
			return navMenuService.getNavMenu();
		} catch (Exception e) {
			LOGGER.error("Error while paging category", e);
		}
		return null;
	}
}
