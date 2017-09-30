package com.salesmanager.web.entity.catalog.category.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryList;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.entity.catalog.category.ReadableCatagoryList;
@Service("categoryFacade")
public class CategoryFacadeImpl implements CategoryFacade{

	@Autowired
	CategoryService categoryService;
	
	@Override
	public ReadableCatagoryList getByCriteria(Criteria criteria,
			Language language) throws Exception {
		// TODO Auto-generated method stub
		CategoryList catList = categoryService.getByCritiria(criteria, language);
		if(catList !=null && catList.getTotalCount()>0){
			ReadableCatagoryList cats = new ReadableCatagoryList();
			cats.setTotalCount(catList.getTotalCount());
			List<com.salesmanager.web.entity.catalog.category.Category> lists = new ArrayList<com.salesmanager.web.entity.catalog.category.Category>();
			for(Category c:catList.getCats()){
				com.salesmanager.web.entity.catalog.category.Category cat = new com.salesmanager.web.entity.catalog.category.Category();
				cat.setId(c.getId());
				cat.setCode(c.getCode());
				cat.setName(c.getDescription().getName());
				cat.setVisible(c.isVisible());
				lists.add(cat);
			}
			cats.setCats(lists);
			return cats;
		}
		return null;
	}

}
