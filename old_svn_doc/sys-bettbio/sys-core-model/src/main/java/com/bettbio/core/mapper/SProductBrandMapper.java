package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.search.Brand;

public interface SProductBrandMapper extends MapperSupport<SProductBrand> {
	public List<SProductBrand> getSProductBrandByCode(String code);
	
	public int deleteBrandList(int[] ids);
	
	public int deleteAll();
	
	public List<SProductBrand> selectPagebyNameOrCode(Map<String, Object> params);
	
	List<SProductBrand> selectByPage(Map<String, Object> map);
	int getBrandAllCount(Map<String, Object> map);
	
	Brand selectBrandById(Integer id);
	
	List<Brand> selectAllBrand();
}