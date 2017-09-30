package com.bettbio.shop.web.controller.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.IndexProduct;
import com.bettbio.core.model.search.page.SearchPage;
import com.bettbio.core.model.search.vo.ProductPriceVo;
import com.bettbio.core.model.search.vo.SearchVo;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.service.ProductService;
import com.bettbio.core.service.SStoreService;
import com.bettbio.core.service.StoreUserService;
import com.bettbio.core.service.search.CategoryService;
import com.bettbio.core.service.search.SearchService;
import com.bettbio.core.service.util.BreadCrumbService;
import com.bettbio.shop.web.controller.CategoryController;

@Controller
@RequestMapping("/app")
public class AppSearchController extends CategoryController{

	private static final Logger LOGGER = LoggerFactory.getLogger(AppSearchController.class);

	@Autowired
	SearchService searchService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;
	
	@Autowired
	SStoreService sStoreService;
	
	@Autowired
	StoreUserService storeUserService;
	
	@Autowired
	BreadCrumbService breadCrumbService;
	
	public String search(SearchVo searchVo,
			HttpServletRequest request,
			ModelMap modelMap){
		return "";
	}
	
	@RequestMapping("/search")
	@ResponseBody
	public SearchPage<IndexProduct> search(SearchVo searchVo,
			HttpServletRequest request,
			ModelMap modelMap,
			Boolean f){
		
		Category category = null;
		if(searchVo.getCate()!=0){
			category = categoryService.selectCategoryById(searchVo.getCate());
			Category nodeCategory = categoryService.selectCategoryByCode(category.getCode());

			searchVo.setCategorys(nodeCategory.getIds());
		}
		
		paramMap = getRequestParameter(request);
		String jsonString = createQueryEntryToJSONString(searchVo);

		LOGGER.info("ES App Index Query Json = " + jsonString);
		
		SearchPage<IndexProduct> searchPage = createSearchPage(paramMap);
		searchPage = searchService.search(jsonString,searchPage);

		autowiredSearchCondition(searchPage);
		
		return searchPage;
	}
	
	@RequestMapping("/category/{id}")
	@ResponseBody
	public SearchPage<IndexProduct> category(@PathVariable Integer id, 
			HttpServletRequest request, 
			ModelMap modelMap, 
			SearchVo searchVo){
		
		Category category = categoryService.selectCategoryById(id);
		Category nodeCategory = categoryService.selectCategoryByCode(category.getCode());

		searchVo.setCategory(nodeCategory.getIds());

		paramMap = getRequestParameter(request);
		String jsonString = createQueryEntryToJSONString(searchVo);

		LOGGER.info("ES App Category Query Json = " + jsonString);

		SearchPage<IndexProduct> searchPage = createSearchPage(paramMap);
		searchPage = searchService.search(jsonString, searchPage);

		autowiredSearchCondition(searchPage,category);
		
		return searchPage;
	}

	@ResponseBody
	@RequestMapping("/product-{id}")
	public Map<String, Object> view(@PathVariable Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		Product product = productService.selectProductById(id);
		
		SStore sStore = new SStore();
		SStoreUser sStoreUser = new SStoreUser();
		sStore.setCode(product.getStoreCode());
		try {
			sStore = sStoreService.selectByCode(sStore);
			sStoreUser = storeUserService.selectByStroeCode(product.getStoreCode());
			map.put("sStore", sStore);
			map.put("sStoreUser", sStoreUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("product", product);
		map.put("breadCrumb", breadCrumbService.buildProductBreadCrumb(product));
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/price")
	public Map<Integer, Product> getPrice(ProductPriceVo priceVo){
		
		if(priceVo.getIds()!=null&&priceVo.getIds().size()>0)
			return productService.getPrdouctPrice(priceVo.getIds());
		
		return null;
	}
}
