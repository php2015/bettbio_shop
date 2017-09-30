package com.bettbio.shop.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bettbio.core.model.Country;
import com.bettbio.core.service.CountryService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("country")
public class CountryController {

	@Autowired
	CountryService countryService;
	
	@RequestMapping
	public String list(Model model,Country country,
			@RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int rows){
		List<Country> countrys = countryService.selectByPage(country, page, rows);
		model.addAttribute("pageInfo", new PageInfo<Country>(countrys));
		model.addAttribute("page", page);
		model.addAttribute("rows", rows);
		return "country";
	}
}
