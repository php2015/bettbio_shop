package com.salesmanager.core.business.catalog.product.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.catalog.product.model.attribute.AttributeCriteria;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.model.CriteriaOrderBy;

public class ProductCriteria extends Criteria {
	
	
	private String productName;
	private List<AttributeCriteria> attributeCriteria;
	private String sku;
	private String code;
	private String findName;
	private String auth;
	private int orderType = -1;
	private int queryType = 0;
	private Boolean availableP = null;
	
	private List<Long> categoryIds;
	private List<String> availabilities;
	private List<Long> productIds;
	private boolean certicate =false;
	private boolean proof = false;
	private boolean third = false;
	private boolean self =false;
	private String audit;
	private Boolean diamond = null;
	public Boolean getDiamond() {
		return diamond;
	}

	public void setDiamond(Boolean diamond) {
		this.diamond = diamond;
	}

	private String relationship ;
	
	private List<Long> manufacturers = null;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}


	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<String> getAvailabilities() {
		return availabilities;
	}

	public void setAvailabilities(List<String> availabilities) {
		this.availabilities = availabilities;
	}

	
	public void setAttributeCriteria(List<AttributeCriteria> attributeCriteria) {
		this.attributeCriteria = attributeCriteria;
	}

	public List<AttributeCriteria> getAttributeCriteria() {
		return attributeCriteria;
	}

	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}

	public List<Long> getProductIds() {
		return productIds;
	}

	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public List<Long> getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(List<Long> manufacturers) {
		this.manufacturers = manufacturers;
	}

	public Boolean getAvailableP() {
		return availableP;
	}

	public void setAvailableP(Boolean availableP) {
		this.availableP = availableP;
	}

	public boolean isCerticate() {
		return certicate;
	}

	public void setCerticate(boolean certicate) {
		this.certicate = certicate;
	}

	public boolean isProof() {
		return proof;
	}

	public void setProof(boolean proof) {
		this.proof = proof;
	}

	public boolean isThird() {
		return third;
	}

	public void setThird(boolean third) {
		this.third = third;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public int getOrderType() {
		return orderType;
	}
	
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getQueryType() {
		return queryType;
	}
	
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
}
