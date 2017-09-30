package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.Country;


public interface CountryMapper extends MapperSupport<Country> {
	List<Country> selectByPage(Map<String, Object> map);
	int getCountryAllCount(Map<String, Object> map);
}