package com.bettbio.core.service.search;

import java.util.List;
import java.util.Map;

import com.bettbio.core.model.search.Brand;

public interface BrandService {

	Brand selectBrandById(Integer id);
	
	List<Brand> getBrandByIndex(Object object);
	
	Map<Integer, Brand> selectAllBrandToMap();
}
