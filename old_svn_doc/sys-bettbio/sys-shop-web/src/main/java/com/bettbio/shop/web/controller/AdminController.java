package com.bettbio.shop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	@RequestMapping("/admin/index")
	public String index() {
		return "/admin/index";
	}

	@RequestMapping("/manager/store/{p1}")
	public String express(@PathVariable String p1) {
		return "/manager/store/" + p1;
	}
	
	@RequestMapping("/manager/brand/{p1}")
	public String brandExpress(@PathVariable String p1) {
		return "/manager/brand/" + p1;
	}
}
