package com.salesmanager.web.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.shop.Breadcrumb;
import com.salesmanager.web.entity.shop.BreadcrumbItem;
import com.salesmanager.web.entity.shop.BreadcrumbItemType;

import edu.emory.mathcs.backport.java.util.Arrays;

@Component
public class BreadcrumbsUtils {
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private CategoryService categoryService;
	
	
	public Breadcrumb buildCategoryBreadcrumb(Category categoryClicked, MerchantStore store, Language language, String contextPath,Map <Long,Category> cmap) throws Exception {
		
		/** Rebuild breadcrumb **/
		//BreadcrumbItem home = new BreadcrumbItem();
		//home.setItemType(BreadcrumbItemType.HOME);
		//home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, LocaleUtils.getLocale(language)));
		//home.setUrl(FilePathUtils.buildStoreUri(store, contextPath) + Constants.SHOP_URI);

		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language);
		
		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		//items.add(home);
		
		//if(!StringUtils.isBlank(refContent)) {

			//List<String> categoryIds = parseBreadCrumb(refContent);
			List<String> categoryIds = categoryService.parseCategoryLineage(categoryClicked.getLineage());
			
			List<Long> ids = new ArrayList<Long>();
			categoryIds.add(categoryClicked.getId().toString());
			for(String c : categoryIds) {
				ids.add(Long.parseLong(c));
				Category category = cmap.get(Long.parseLong(c));
				BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
				categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
				categoryBreadcrump.setLabel(category.getDescription().getName());
				categoryBreadcrump.setId(category.getId());
				//categoryBreadcrump.setUrl(FilePathUtils.buildCategoryUrl(store, contextPath, category.getDescription().getSeUrl()));
				items.add(categoryBreadcrump);
			}
			
			//ids.add(categoryClicked.getId());
			

			//List<Category> categories = categoryService.listByIds(store, ids, language);
			
			//category path - use lineage
			/**
			for(Category c : categories) {
				BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
				categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
				categoryBreadcrump.setLabel(c.getDescription().getName());
				categoryBreadcrump.setUrl(FilePathUtils.buildCategoryUrl(store, contextPath, c.getDescription().getSeUrl()));
				items.add(categoryBreadcrump);
			}*/
			
			breadCrumb.setUrlRefContent(buildBreadCrumb(ids));
			
		//}
		


		breadCrumb.setBreadCrumbs(items);
		breadCrumb.setItemType(BreadcrumbItemType.CATEGORY);
		
		
		return breadCrumb;
	}
	
	
	public Breadcrumb buildProductBreadcrumb(String refContent, ReadableProduct productClicked, MerchantStore store, Language language, String contextPath,Map <Long,Category> cmap) throws Exception {
		
		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, LocaleUtils.getLocale(language)));
		home.setUrl(FilePathUtils.buildStoreUri(store, contextPath));

		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language);
		
		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(home);
		
		if(!StringUtils.isBlank(refContent)) {

			List<String> categoryIds = parseBreadCrumb(refContent);
			
			List<Long> ids = new ArrayList<Long>();
			
			for(String c : categoryIds) {
				ids.add(Long.parseLong(c));
				Category category = cmap.get(Long.parseLong(c));
				BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
				categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
				categoryBreadcrump.setLabel(category.getDescription().getName());
				categoryBreadcrump.setUrl(FilePathUtils.buildCategoryUrl(store, contextPath,c));
				items.add(categoryBreadcrump);

			}
			
			
			//List<Category> categories = categoryService.listByIds(store, ids, language);
			
			//category path - use lineage
			/**
			for(Category c : categories) {
				BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
				categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
				categoryBreadcrump.setLabel(c.getDescription().getName());
				categoryBreadcrump.setUrl(FilePathUtils.buildCategoryUrl(store, contextPath, c.getDescription().getSeUrl()));
				items.add(categoryBreadcrump);
			}*/
			

			breadCrumb.setUrlRefContent(buildBreadCrumb(ids));
		} 
		
		BreadcrumbItem productBreadcrump = new BreadcrumbItem();
		productBreadcrump.setItemType(BreadcrumbItemType.PRODUCT);
		productBreadcrump.setLabel(productClicked.getDescription().getName());
		productBreadcrump.setUrl(FilePathUtils.buildProductUrl(store, contextPath,productClicked.getId().toString()));
		items.add(productBreadcrump);
		
		
		


		breadCrumb.setBreadCrumbs(items);
		breadCrumb.setItemType(BreadcrumbItemType.CATEGORY);
		
		
		return breadCrumb;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<String> parseBreadCrumb(String refContent) throws Exception {
		refContent =refContent.substring(1);
		/** c:1,2,3 **/
		String[] categoryComa = refContent.split("/");
		
		if(categoryComa !=null && categoryComa.length>0){
			if(categoryComa.length==1){
				return new LinkedList(Arrays.asList(categoryComa[0].split(",")));
			}else{
				
				return new LinkedList(Arrays.asList(categoryComa));
			}
			
		}
		
		return null;
	}
	

	
	private String buildBreadCrumb(List<Long> ids) throws Exception {
		
		if(CollectionUtils.isEmpty(ids)) {
			return null;
		}
			StringBuilder sb = new StringBuilder();
			int count = 1;
			for(Long c : ids) {
				sb.append(c);
				if(count < ids.size()) {
					sb.append(",");
				}
				count++;
			}
		
		
		return sb.toString();
		
	}

}
