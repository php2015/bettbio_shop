package com.test.mode;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.vo.SearchVo;
import com.bettbio.core.model.util.BreadCrumb;
import com.bettbio.core.model.util.BreadCrumbItem;
import com.bettbio.core.mongo.model.ProductBaseModel;
import com.bettbio.core.service.search.CategoryService;
import com.bettbio.core.service.util.BreadCrumbService;
import com.test.mode.base.BaseT;

/**
 * 面包屑导航测试
 * @author GuoChunbo
 *
 */
public class BreadCrumbTest extends BaseT{

	@Autowired
	BreadCrumbService breadCrumbService;
	
	@Autowired
	CategoryService categoryService;
	
	@Test
	public void categoryBreadCrumb(){
		Category category1 = new Category();
		category1.setId(1);
		
		Category category2 = new Category();
		category2.setId(5);

		Category category3 = new Category();
		category3.setId(7);
		
		BreadCrumb breadCrumb1 = breadCrumbService.buildCategoryBreadCrumb(category1);
		BreadCrumb breadCrumb2 = breadCrumbService.buildCategoryBreadCrumb(category2);
		BreadCrumb breadCrumb3 = breadCrumbService.buildCategoryBreadCrumb(category3);
		
		printBreadCrumb(breadCrumb1);
		printBreadCrumb(breadCrumb2);
		printBreadCrumb(breadCrumb3);
		
	}
	
	@Test
	public void searchBreadCrumb(){
		
		Category category1 = new Category();
		category1.setId(1);
		
		SearchVo searchVo = new SearchVo();
		searchVo.setQ("CCK-8");
		searchVo.setQt(0);
		searchVo.setCate(1);
		
		BreadCrumb breadCrumb1 = breadCrumbService.buildSearchBreadCrumb(category1, searchVo);

		printBreadCrumb(breadCrumb1);
	}

	@Test
	public void productBreadCrumb(){
		
		ProductBaseModel product = new ProductBaseModel();
		product.setId(123);
		product.setProductNameCh("试剂类产品");
		
		product.setProductClass(categoryService.selectCategoryById(5));
		
		BreadCrumb breadCrumb = breadCrumbService.buildProductBreadCrumb(product );

		printBreadCrumb(breadCrumb);
	}
	
	void printBreadCrumb(BreadCrumb breadCrumb){
		System.out.println("---------------B R E A D C R U M B-------------------------");
		for (BreadCrumbItem item : breadCrumb.getBreadCrumbs()) {
			System.out.println(item);
		}
		System.out.println();
	}
}
