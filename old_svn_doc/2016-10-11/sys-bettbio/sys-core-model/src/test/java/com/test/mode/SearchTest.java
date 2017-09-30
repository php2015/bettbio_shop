package com.test.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet;
import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet.LongEntry;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.model.search.BettQueryEntry;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.IndexProduct;
import com.bettbio.core.model.search.QueryEntry;
import com.bettbio.core.model.search.page.SearchPage;
import com.bettbio.core.model.search.vo.SearchVo;
import com.bettbio.core.service.search.BrandService;
import com.bettbio.core.service.search.CategoryService;
import com.bettbio.core.service.search.SearchService;
import com.test.mode.base.BaseT;

/**
 * SEARCH TEST
 * 
 * @author GuoChunbo
 *
 */
public class SearchTest extends BaseT {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	SearchService searchService;

	@Test
	public void category(){

		SearchVo searchVo = new SearchVo();

		Category category = categoryService.selectCategoryById(1);
		Category nodeCategory = categoryService.selectCategoryByCode(category.getCode());

		searchVo.setCategory(nodeCategory.getIds());
		searchVo.getBra().add(5353l);
		searchVo.getQty().add("0");
		
		String jsonString = createQueryEntryToJSONString(searchVo);
		System.out.println("query json = " + jsonString);

		SearchPage<IndexProduct> searchPage = new SearchPage<>(1, 20);
		searchPage = searchService.search(jsonString, searchPage);
		
		System.out.println(searchPage);
		
		Map<String, Object> facets = searchPage.getIndexFacets();
		if(facets!=null){
			Object categoryObject = facets.get("category");
			Object manufacturerObject = facets.get("manufacturer");
			
		}
	}
	
	@Test
	public void search() {
		Category category = categoryService.selectCategoryById(1);
		Category nodeCategory = categoryService.selectCategoryByCode(category.getCode());

		List<Long> bras = new ArrayList<Long>();
		bras.add(5353l);
		
		QueryEntry queryEntry = new BettQueryEntry();
		queryEntry.setQuery("CCK-8");
		queryEntry.setQueryType(0);
		queryEntry.setCategories(nodeCategory.getIds());
		queryEntry.setManufacturer(bras);
		
		System.out.println("query json = " + queryEntry.toJSONString());
		
		SearchVo searchVo = new SearchVo();
		searchVo.setQ("CCK-8");
		searchVo.setQt(0);
		searchVo.setCategorys(nodeCategory.getIds());
//		searchVo.getBra().add(5353l);
//		searchVo.getQty().add("0");
		
		String jsonString = createQueryEntryToJSONString(searchVo);
		System.out.println("query json = " + jsonString);
		
		SearchPage<IndexProduct> searchPage = new SearchPage<>(1, 20);
		searchPage = searchService.search(jsonString, searchPage);
		
		System.out.println(searchPage);
		
		Map<String, Object> facets = searchPage.getIndexFacets();
		if(facets!=null){
			Object categoryObject = facets.get("category");
			Object manufacturerObject = facets.get("manufacturer");
			
		}

	}
	
	public String createQueryEntryToJSONString(SearchVo searchVo){
		QueryEntry queryEntry = new BettQueryEntry();
		if(searchVo.getCategory().size()>0) queryEntry.setCategory(searchVo.getCategory());
		if (!StringUtils.isEmpty(searchVo.getQ())) queryEntry.setQuery(searchVo.getQ());
		
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

		if (!StringUtils.isEmpty(searchVo.getQt())) queryEntry.setQueryType(searchVo.getQt());
		return queryEntry.toJSONString();
	}
}
