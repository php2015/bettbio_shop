package com.bettbio.shop.web.controller.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.web.controller.BaseController;

/**
 * 百图管理系统统一走/manage/*
 * @author GuoChunbo
 *
 */
@Controller
@RequestMapping("/manager")
public class ManagerIndexController extends BaseController{

	@RequestMapping("/index")
	public String index(HttpServletRequest request,ModelMap modelMap){
		
		return "/manager/index";
	}
}
