package com.salesmanager.core.business.common.model;

import org.apache.commons.lang3.StringUtils;

public class Criteria {
	
	private int startIndex = 0;
	private int maxCount = 0; //每页的数据数量
	private long totalCount = 0; //查询的总数据数量
	private String code;
	
	private String findName;
	private Long storeId =-1l;
	/**
	 * 1:all status
	 * 2:selected
	 * 0:unseelcted
	 */
	private int avaiable = 1;
	
	private CriteriaOrderBy orderBy = CriteriaOrderBy.DESC;
	
	
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setOrderBy(CriteriaOrderBy orderBy) {
		this.orderBy = orderBy;
	}
	public CriteriaOrderBy getOrderBy() {
		return orderBy;
	}
	public String getFindName() {
		if(!StringUtils.isBlank(findName)){
			return findName.trim();
		}
		return findName;
	}
	public void setFindName(String findName) {
		this.findName = findName;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public int getAvaiable() {
		return avaiable;
	}
	public void setAvaiable(int avaiable) {
		this.avaiable = avaiable;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}