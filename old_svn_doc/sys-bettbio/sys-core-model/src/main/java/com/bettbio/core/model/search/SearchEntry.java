package com.bettbio.core.model.search;

import java.util.List;
import java.util.Map;

public class SearchEntry {
	
	private IndexProduct indexProduct;//product as saved in the index
	private List<String> highlights;
	private Map<String,Object> product;
	private Float socre;
	public void setHighlights(List<String> highlights) {
		this.highlights = highlights;
	}
	public List<String> getHighlights() {
		return highlights;
	}
	public void setIndexProduct(IndexProduct indexProduct) {
		this.indexProduct = indexProduct;
	}
	public IndexProduct getIndexProduct() {
		return indexProduct;
	}
	public Map<String, Object> getProduct() {
		return product;
	}
	public void setProduct(Map<String, Object> product) {
		this.product = product;
	}
	public Float getSocre() {
		return socre;
	}
	public void setSocre(Float socre) {
		this.socre = socre;
	}

}
