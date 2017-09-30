package com.bettbio.core.service.search;

import java.util.List;
import java.util.Map;

import com.bettbio.core.model.search.Category;

public interface CategoryService {
	Category selectCategoryById(Integer id);

	Category selectCategoryByCode(String code);

	List<Category> getSubCategory(Integer id);

	List<Category> getCategoryByIndex(Object object);

	List<Category> getCategoryByIndex(Object object, Category category);

	Map<Integer, Category> selectAllCategoryToMap();
	
	Map<Integer, Category> getSubCategoryToMap(Integer id);

	List<Category> selectTopCategory();
	
	List<Category> selectAllSubCategary(Integer id);
}
