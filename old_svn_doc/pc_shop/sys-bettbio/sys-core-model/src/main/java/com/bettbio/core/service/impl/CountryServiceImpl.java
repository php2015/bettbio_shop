package com.bettbio.core.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.model.Country;
import com.bettbio.core.service.CountryService;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

@Service
public class CountryServiceImpl extends BaseService<Country> implements CountryService {

	@Override
	public List<Country> selectByPage(Country country, int page, int rows) {
		Example example = new Example(entityClass);
		return selectByPage(example, page, rows);
//        PageHelper.startPage(page, rows);
//        return selectByExample(example);
	}

}
