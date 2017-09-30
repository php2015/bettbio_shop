package com.bettbio.core.model.search.page;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.model.search.Brand;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.SearchEntry;
import com.bettbio.core.model.search.SearchFacet;
import com.bettbio.core.model.util.BreadCrumb;

/**
 * 搜索数据分页
 * 
 * @author GuoChunbo
 *
 * @param <T>
 */
public class SearchPage<T> extends Page<T> {

	private List<SearchEntry> entries;
	private Map<String, List<SearchFacet>> facets;
	private Map<String, Object> indexFacets;

	private BreadCrumb breadCrumb;
	private List<Category> categorys;
	private List<Brand> brands;

	public SearchPage(int pageNum, int pageSize) {
		super(pageNum, pageSize);
	}

	public List<SearchEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<SearchEntry> entries) {
		this.entries = entries;
	}

	public Map<String, List<SearchFacet>> getFacets() {
		return facets;
	}

	public void setFacets(Map<String, List<SearchFacet>> facets) {
		this.facets = facets;
	}

	public Map<String, Object> getIndexFacets() {
		return indexFacets;
	}

	public void setIndexFacets(Map<String, Object> indexFacets) {
		this.indexFacets = indexFacets;
	}

	public List<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	public BreadCrumb getBreadCrumb() {
		return breadCrumb;
	}

	public void setBreadCrumb(BreadCrumb breadCrumb) {
		this.breadCrumb = breadCrumb;
	}

}
