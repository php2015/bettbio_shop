package com.bettbio.core.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.CountryMapper;
import com.bettbio.core.model.Country;
import com.bettbio.core.service.CountryService;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

@Service
public class CountryServiceImpl extends BaseService<Country> implements CountryService {

	public CountryMapper getCountryMapper() {
		return (CountryMapper) mapper;
	}

	@Override
	public List<Country> selectByPage(Country country, int page, int rows) {
		 Example example = new Example(Country.class);
	        Example.Criteria criteria = example.createCriteria();
	        if (!StringUtils.isEmpty(country.getCountryName())) {
	            criteria.andLike("countryname", "%" + country.getCountryName() + "%");
	        }
	        if (StringUtils.isEmpty(country.getCountryCode())) {
	            criteria.andLike("countrycode", "%" + country.getCountryName() + "%");
	        }
	        if (country.getId() != null) {
	            criteria.andEqualTo("id", country.getId());
	        }
	        //分页查询
	        PageHelper.startPage(page, rows);
	        return selectByExample(example);

	}

	@Override
	public Page<Country> selectByPage(Map<String, Object> map) {
		Page<Country> page = new Page<Country>(
				map,
				getCountryMapper().getCountryAllCount(map));
		
		page.setList(getCountryMapper().selectByPage(page.getMap()));
		return page;

	}
	
	
	

}
