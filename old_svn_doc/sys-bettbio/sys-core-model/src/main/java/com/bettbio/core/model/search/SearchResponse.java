package com.bettbio.core.model.search;

import java.util.List;
import java.util.Map;

public class SearchResponse {
	
	private int totalCount = 0;//total number of entries
	private int entryCount = 0;//number of entries asked
	
	private List<IndexProduct> indexProduct;
	private List<SearchEntry> entries;
	private Map<String,List<SearchFacet>> facets;//facet key (example : category) & facet description (example : category code)
	private Map<String,Object> indexFacets;
	//private Facet categoryFacet;
		
	public Map<String,Object> getIndexFacets() {
		return indexFacets;
	}
	public void setIndexFacets(Map<String,Object> indexFacets) {
		this.indexFacets = indexFacets;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}
	public int getEntryCount() {
		return entryCount;
	}
	public void setEntries(List<SearchEntry> entries) {
		this.entries = entries;
	}
	public List<SearchEntry> getEntries() {
		return entries;
	}
	public void setFacets(Map<String,List<SearchFacet>> facets) {
		this.facets = facets;
	}
	public Map<String,List<SearchFacet>> getFacets() {
		return facets;
	}
	public List<IndexProduct> getIndexProduct() {
		return indexProduct;
	}
	public void setIndexProduct(List<IndexProduct> indexProduct) {
		this.indexProduct = indexProduct;
	}
}
