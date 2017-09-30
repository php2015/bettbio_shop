package com.bettbio.core.model.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.bettbio.core.model.SProductClassification;

public class Category extends SProductClassification {

	private String idStr;
	
	private Category parentCategory;
	
	private List<Category> sonCategory;
	
	private int productCount;
	
	private List<Long> ids;
	
	public List<Long> getIds() {
		ids = new ArrayList<Long>();
		
		if(!StringUtils.isEmpty(this.idStr)){
			int idx = this.idStr.indexOf(",");
			if(idx!=-1){
				for (String id :  this.idStr.split(",")) {
					ids.add(Long.parseLong(id));
				}
			}else{
				ids.add(Long.parseLong(idStr));
			}
		}
		return ids;
	}
	
	
	public List<Category> getSonCategory() {
		if(sonCategory==null) sonCategory = new ArrayList<Category>();
		return sonCategory;
	}


	public void setSonCategory(List<Category> sonCategory) {
		this.sonCategory = sonCategory;
	}


	public int getProductCount() {
		return productCount;
	}


	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}


	public void setIds(List<Long> ids) {
		this.ids = ids;
	}


	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}


	public Category getParentCategory() {
		if(parentCategory==null)parentCategory = new Category();
		return parentCategory;
	}


	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}
	
	
	@Override
	public String toString() {
		return "Category("
				+ "id=" + getId()
				+ ",name=" + getName()
				+ ")";
	}

}
