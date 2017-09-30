package com.test.mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.model.Country;
import com.bettbio.core.service.CountryService;
import com.github.pagehelper.PageInfo;
import com.test.mode.base.BaseT;

public class TestCountryCase extends BaseT {

	@Autowired
	CountryService countryService;
	
	@Test
	public void find(){
		List<Country> lists  = countryService.selectByPage(new Country(), 1, 10);
		System.out.println(new PageInfo<Country>(lists).toString());
	}
	
	@Test
	public void select(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page",2);
		map.put("row",10);
		Page<Country> page = countryService.selectByPage(map);
		System.out.println(page);
		for (Country country : page.getList()) {
			System.out.println(country.getCountryName());
		}
		
	}
}
