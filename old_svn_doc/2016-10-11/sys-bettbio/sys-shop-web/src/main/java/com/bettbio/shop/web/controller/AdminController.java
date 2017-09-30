package com.bettbio.shop.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.service.SubOrderService;
import com.bettbio.shop.base.controller.BaseAdminController;

@Controller
public class AdminController extends BaseAdminController{

	@Autowired
	SubOrderService subOrderService;
	
	@RequestMapping("/admin/index")
	public String index(HttpServletRequest request,ModelMap model) throws NotLoginEXception{
		paramMap=getRequestParameter(request);
		String storeCode=getCurrentUser(request).getStoreCode();
		paramMap.put("storeCode",storeCode);
		model.put("pageInfo",subOrderService.selectSubOrdersByMap(paramMap));
		model.put("orderCount", subOrderService.selectOrderCountByCode(storeCode));
		model.put("countPrice", subOrderService.selectCountPriceByCode(storeCode));
		model.addAttribute("paramMap", paramMap);
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
