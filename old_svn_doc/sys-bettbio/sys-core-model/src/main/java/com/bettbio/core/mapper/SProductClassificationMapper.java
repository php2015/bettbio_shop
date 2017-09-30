package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.bo.ClassifyZtreeBo;
import com.bettbio.core.model.search.Category;

public interface SProductClassificationMapper extends MapperSupport<SProductClassification> {
	
	public int deleteClassificationList(int[] ids);
	
	public List<SProductClassification> selectPagebyNameOrCode(Map<String, Object> params);
	
	public int deleteAll();
	
	public ClassifyZtreeBo selectClassChild(String parentCode);
	
	public List<ClassifyZtreeBo> selectClassByparentCode(String parentCode);
	
	List<SProductClassification> selectByPage(Map<String, Object> map);
	int getSProductAllCount(Map<String, Object> map);
	 
	Category selectCategoryByCode(String code);
	Category selectCategoryById(Integer id);
	List<Category> getSubCategory(Integer id);
	List<Category> selectAllCategory();
	List<Category> selectTopCategory();
	List<Category> selectAllSubCategary(Integer id);
	
	public String selectMaxCode(String code);
	
}