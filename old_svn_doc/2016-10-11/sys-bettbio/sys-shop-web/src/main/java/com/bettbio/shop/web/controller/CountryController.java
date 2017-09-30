package com.bettbio.shop.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.model.Country;
import com.bettbio.core.service.CountryService;

@Controller
@RequestMapping("country")
public class CountryController extends BaseController{

	@Autowired
	CountryService countryService;
	
	@RequestMapping
	public String list(Model model,Country country,
            HttpServletRequest request){
		
		paramMap = getRequestParameter(request);
		
		Page<Country> pageInfo = countryService.selectByPage(paramMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("paramMap", paramMap);
		return "country";
	}
}
