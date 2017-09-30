package com.salesmanager.web.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.order.service.OrderService;

@Controller
public class CheckController {
	@Autowired
	OrderService orderService;
	
	@RequestMapping("/check.html")
	public String check(){
		return "check";
	}
	
	@RequestMapping("/checkOrder.html")
	@ResponseBody
	public String checkOrder(String phone){
		if(orderService.exist(phone)){
			return "true";
		}
		return "false";
	}
}
