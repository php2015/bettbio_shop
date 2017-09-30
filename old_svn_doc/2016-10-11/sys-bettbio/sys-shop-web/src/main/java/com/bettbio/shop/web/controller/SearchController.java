package com.bettbio.shop.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.model.search.BettQueryEntry;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.IndexProduct;
import com.bettbio.core.model.search.QueryEntry;
import com.bettbio.core.model.search.page.SearchPage;
import com.bettbio.core.model.search.vo.SearchVo;
import com.bettbio.core.service.search.BrandService;
import com.bettbio.core.service.search.CategoryService;
import com.bettbio.core.service.search.SearchService;
import com.bettbio.core.service.util.BreadCrumbService;

/**
 * 搜索入口主控制器
 * 
 * @author GuoChunBo
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	BreadCrumbService breadCrumbService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	SearchService searchService;
	
	@RequestMapping()
	public String search(SearchVo searchVo,
			HttpServletRequest request,
			ModelMap modelMap){
		Category category = null;
		if(searchVo.getCate()!=0){
			category = categoryService.selectCategoryById(searchVo.getCate());
			Category nodeCategory = categoryService.selectCategoryByCode(category.getCode());

			searchVo.setCategorys(nodeCategory.getIds());
		}
		
		paramMap = getRequestParameter(request);
		String jsonString = createQueryEntryToJSONString(searchVo);

		LOGGER.info("ES Index Query Json = " + jsonString);
		
		SearchPage<IndexProduct> searchPage = createSearchPage(paramMap);
		searchPage = searchService.search(jsonString,searchPage);

		autowiredSearchCondition(searchPage,null);

		searchPage.setBreadCrumb(breadCrumbService.buildSearchBreadCrumb(category,searchVo));
		
		modelMap.addAttribute(Constants.PAGE_INFO, searchPage);
		modelMap.addAttribute(Constants.PARAM_MAP,paramMap);
		return "/search";
	}
	
	//ES查询JSON构建
	protected String createQueryEntryToJSONString(SearchVo searchVo){
		QueryEntry queryEntry = new BettQueryEntry();
		if (!StringUtils.isEmpty(searchVo.getQ())) queryEntry.setQuery(searchVo.getQ());
		if (!StringUtils.isEmpty(searchVo.getQt())) queryEntry.setQueryType(searchVo.getQt());
		
		if (searchVo.getCategorys().size() > 0) queryEntry.setCategories(searchVo.getCategorys());
		if (searchVo.getBra().size() > 0) queryEntry.setManufacturer(searchVo.getBra());
		
		for (String qty : searchVo.getQty()) {
			switch (qty) {
			case "0":
				queryEntry.setCerticate(true);
				break;
			case "1":
				queryEntry.setThird(true);
				break;
			default:
				queryEntry.setProof(true);
				break;
			}
		}
		
		if(searchVo.getCategory().size()>0) queryEntry.setCategory(searchVo.getCategory());
		return queryEntry.toJSONString();
	}
	
	//查询结果分页bean构建
	protected SearchPage<IndexProduct> createSearchPage(Map<String, Object> map){
		int pageNum = map.containsKey("page")?Integer.parseInt(map.get("page").toString()):1;
		int pageSize = map.containsKey("row")?Integer.parseInt(map.get("row").toString()):1;
		return new SearchPage<>(pageNum, pageSize > 20 ? pageSize : 20);
	}
	
	//装配查询条件数据
	protected void autowiredSearchCondition(SearchPage<IndexProduct> searchPage,Category category){
		Map<String, Object> facets = searchPage.getIndexFacets();
		if(facets!=null){
			
			if(facets.get("category")!=null){
				Object categoryObject = facets.get("category");
				searchPage.setCategorys(categoryService.getCategoryByIndex(categoryObject,category));
			}

			if(facets.get("manufacturer")!=null){
				Object manufacturerObject = facets.get("manufacturer");
				searchPage.setBrands(brandService.getBrandByIndex(manufacturerObject));
			}
		}

	}
	
}
