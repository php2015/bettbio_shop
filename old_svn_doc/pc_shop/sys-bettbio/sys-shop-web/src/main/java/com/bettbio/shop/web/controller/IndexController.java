package com.bettbio.shop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index(){

		return "index";
	}
	
	@RequestMapping("/ex")
	public String ex(){
		int i = 12 / 0x00;
		return "index";
	}
}
