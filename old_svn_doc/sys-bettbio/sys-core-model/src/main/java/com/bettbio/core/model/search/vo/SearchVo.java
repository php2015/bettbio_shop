package com.bettbio.core.model.search.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索条件bean
 * 
 * @author GuoChunbo
 *
 */
public class SearchVo {
	private String q;
	private int qt;
	private int cate;
	private List<Long> categorys = new ArrayList<Long>();
	private List<String> qty = new ArrayList<String>();
	private List<Long> bra = new ArrayList<Long>();

	// 用于分类
	private List<Long> category = new ArrayList<Long>();

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getQt() {
		return qt;
	}

	public void setQt(int qt) {
		this.qt = qt;
	}

	public List<Long> getCategory() {
		return category;
	}

	public void setCategory(List<Long> category) {
		this.category = category;
	}

	public int getCate() {
		return cate;
	}

	public void setCate(int cate) {
		this.cate = cate;
	}

	public List<Long> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Long> categorys) {
		this.categorys = categorys;
	}

	public List<String> getQty() {
		return qty;
	}

	public void setQty(List<String> qty) {
		this.qty = qty;
	}

	public List<Long> getBra() {
		return bra;
	}

	public void setBra(List<Long> bra) {
		this.bra = bra;
	}
	
	public String getLinkUrl(Boolean flag){
		if(flag){
			return "search?q="+this.q+"&qt="+this.qt+"&cate="+this.cate;
		}
		return "search?q="+this.q+"&qt="+this.qt;
	}
}
