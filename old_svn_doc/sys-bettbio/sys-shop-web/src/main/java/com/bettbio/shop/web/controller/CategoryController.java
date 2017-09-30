package com.bettbio.shop.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.IndexProduct;
import com.bettbio.core.model.search.page.SearchPage;
import com.bettbio.core.model.search.vo.SearchVo;

/**
 * 类目搜索控制器
 * 
 * @author GuoChuno
 *
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends SearchController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

	@RequestMapping("/{id}")
	public String search(@PathVariable Integer id, 
			HttpServletRequest request, 
			ModelMap modelMap, 
			SearchVo searchVo) {

		Category category = categoryService.selectCategoryById(id);
		Category nodeCategory = categoryService.selectCategoryByCode(category.getCode());

		searchVo.setCategory(nodeCategory.getIds());

		paramMap = getRequestParameter(request);
		String jsonString = createQueryEntryToJSONString(searchVo);

		LOGGER.info("ES Category Query Json = " + jsonString);

		SearchPage<IndexProduct> searchPage = createSearchPage(paramMap);
		searchPage = searchService.search(jsonString, searchPage);

		autowiredSearchCondition(searchPage,category);
		
		searchPage.setBreadCrumb(breadCrumbService.buildCategoryBreadCrumb(category));
		
		modelMap.addAttribute(Constants.PAGE_INFO, searchPage);
		modelMap.addAttribute(Constants.PARAM_MAP, paramMap);
		modelMap.addAttribute("currentCate", category);
		return "/category";
	}
	
	//装配查询条件数据
	protected void autowiredSearchCondition(SearchPage<IndexProduct> searchPage,Category category){
		Map<String, Object> facets = searchPage.getIndexFacets();
		if(facets!=null){
			Object categoryObject = facets.get("category");
			searchPage.setCategorys(categoryService.getCategoryByIndex(categoryObject,category));

			Object manufacturerObject = facets.get("manufacturer");
			searchPage.setBrands(brandService.getBrandByIndex(manufacturerObject));
		}

	}
}
