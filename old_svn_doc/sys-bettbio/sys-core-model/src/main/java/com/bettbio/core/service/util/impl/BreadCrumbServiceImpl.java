package com.bettbio.core.service.util.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.vo.SearchVo;
import com.bettbio.core.model.util.BreadCrumb;
import com.bettbio.core.model.util.BreadCrumbItem;
import com.bettbio.core.model.util.BreadCrumbItemType;
import com.bettbio.core.mongo.model.ProductBaseModel;
import com.bettbio.core.service.search.CategoryService;
import com.bettbio.core.service.util.BreadCrumbService;

/**
 * 面包屑导航处理
 * @author GuoChunbo
 *
 */
@Service
public class BreadCrumbServiceImpl implements BreadCrumbService{

	@Autowired
	CategoryService categoryService;
	
	@Override
	public BreadCrumb buildCategoryBreadCrumb(Category category) {
		Map<Integer, Category> cateMap =  categoryService.selectAllCategoryToMap();
		BreadCrumb breadCrumb = new BreadCrumb();

		List<BreadCrumbItem> breadCrumbs = new ArrayList<BreadCrumbItem>();
		
		buildCategoryBreadCrumb(category, breadCrumbs, cateMap);
		
		Collections.reverse(breadCrumbs);
		
		breadCrumb.setItemType(BreadCrumbItemType.CATEGORY);
		breadCrumb.setBreadCrumbs(breadCrumbs);
		return breadCrumb;
	}

	@Override
	public BreadCrumb buildSearchBreadCrumb(Category category,SearchVo searchVo) {
		BreadCrumb breadCrumb = new BreadCrumb();
		
		List<BreadCrumbItem> breadCrumbs = new ArrayList<BreadCrumbItem>();
		
		if(category!=null&&category.getId()!=null){
			category = categoryService.selectCategoryById(category.getId());
			
			BreadCrumbItem catItem = new BreadCrumbItem();
			catItem.setId(category.getId());
			catItem.setLabel(category.getName());
			catItem.setItemType(BreadCrumbItemType.SEARCH);
			catItem.setUrl(searchVo.getLinkUrl(true));
			breadCrumbs.add(catItem);
		}

		BreadCrumbItem item = new BreadCrumbItem();
		item.setItemType(BreadCrumbItemType.SEARCH);
		item.setLabel("全部结果");
		item.setUrl(searchVo.getLinkUrl(false));
		breadCrumbs.add(item);
		
		Collections.reverse(breadCrumbs);
		breadCrumb.setItemType(BreadCrumbItemType.SEARCH);
		breadCrumb.setBreadCrumbs(breadCrumbs);
		return breadCrumb;
	}
	
	public void buildCategoryBreadCrumb(Category category,
			List<BreadCrumbItem> breadCrumbs,
			Map<Integer, Category> cateMap){
		BreadCrumbItem item = new BreadCrumbItem();
		Category cat = cateMap.get(category.getId());
		if(cat != null ){
			item.setId(cat.getId());
			item.setLabel(cat.getName());
			item.setItemType(BreadCrumbItemType.CATEGORY);
			item.setUrl(cat.getId()+".html");
			breadCrumbs.add(item);
			if (!"0".equals(cat.getParentCode())) {
				cat.getParentCategory().setId(Integer.parseInt(cat.getParentCode()));
				buildCategoryBreadCrumb(cat.getParentCategory(), breadCrumbs, cateMap);
			}
		}
	}

	@Override
	public BreadCrumb buildProductBreadCrumb(ProductBaseModel product) {

		List<BreadCrumbItem> breadCrumbs = new ArrayList<BreadCrumbItem>();

		BreadCrumbItem item = new BreadCrumbItem();
		item.setItemType(BreadCrumbItemType.SEARCH);
		item.setLabel(product.getProductNameCh());
		item.setUrl("product-"+product.getId()+".html");
		breadCrumbs.add(item);
		
		Map<Integer, Category> cateMap =  categoryService.selectAllCategoryToMap();
		BreadCrumb breadCrumb = new BreadCrumb();
		
		buildProductBreadCrumb(product.getProductClass(), breadCrumbs, cateMap);
		
		Collections.reverse(breadCrumbs);
		
		breadCrumb.setItemType(BreadCrumbItemType.PRODUCT);
		breadCrumb.setBreadCrumbs(breadCrumbs);
		
		return breadCrumb;
	}

	private void buildProductBreadCrumb(SProductClassification productClass, List<BreadCrumbItem> breadCrumbs,
			Map<Integer, Category> cateMap) {
		BreadCrumbItem item = new BreadCrumbItem();
		Category cat = cateMap.get(productClass.getId());
		if(cat != null ){
			item.setId(cat.getId());
			item.setLabel(cat.getName());
			item.setItemType(BreadCrumbItemType.CATEGORY);
			item.setUrl(cat.getId()+".html");
			breadCrumbs.add(item);
			if (!"0".equals(cat.getParentCode())) {
				cat.getParentCategory().setId(Integer.parseInt(cat.getParentCode()));
				buildProductBreadCrumb(cat.getParentCategory(), breadCrumbs, cateMap);
			}
		}
	}

}
