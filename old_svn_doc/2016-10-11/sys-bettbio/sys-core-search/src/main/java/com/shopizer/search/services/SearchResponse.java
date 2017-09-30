package com.shopizer.search.services;

import java.util.Collection;

import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facets;

/**
 * Object used for autocomplete and regular search
 * @author Carl Samson
 *
 */
public class SearchResponse {
	
	private String inputSearchJson;
	private Collection<String> ids;
	private int count;


	private SearchHits searchHits;
	private Facets facets;
	
	public Facets getFacets() {
		return facets;
	}

	public void setFacets(Facets facets2) {
		this.facets = facets2;
	}

	private String[] inlineSearchList;
	
	
	public String[] getInlineSearchList() {
		return inlineSearchList;
	}

	public void setInlineSearchList(String[] inlineSearchList) {
		this.inlineSearchList = inlineSearchList;
	}

	public SearchHits getSearchHits() {
		return searchHits;
	}

	public void setSearchHits(SearchHits searchHits2) {
		this.searchHits = searchHits2;
	}

	public String getInputSearchJson() {
		return inputSearchJson;
	}

	public void setInputSearchJson(String inputSearchJson) {
		this.inputSearchJson = inputSearchJson;
	}
	
	public Collection<String> getIds() {
		return ids;
	}

	public void setIds(Collection<String> ids) {
		this.ids = ids;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}




}
