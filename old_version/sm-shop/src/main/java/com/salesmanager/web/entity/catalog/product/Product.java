package com.salesmanager.web.entity.catalog.product;

import java.io.Serializable;
import java.util.Date;

import com.salesmanager.web.entity.Entity;

public class Product extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productName;
	private String productEnName;
	private String sku;
	private String code;
	private String storeName ;
	private Boolean available = null;
	private String relations="";
	private String availableDate;
	private String createDate;
	private int audit =-1;
	private Date dateChargeBegin ;
	private Date dateChargeEnd ;
	private int qualitysocre=0;
	private int type=1;
	private boolean isDiamondProduct;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public String getRelations() {
		return relations;
	}
	public void setRelations(String relations) {
		this.relations = relations;
	}
	public String getAvailableDate() {
		return availableDate;
	}
	public void setAvailableDate(String availableDate) {
		this.availableDate = availableDate;
	}
	public String getProductEnName() {
		return productEnName;
	}
	public void setProductEnName(String productEnName) {
		this.productEnName = productEnName;
	}
	public int getAudit() {
		return audit;
	}
	public void setAudit(int audit) {
		this.audit = audit;
	}
	public Date getDateChargeBegin() {
		return dateChargeBegin;
	}
	public void setDateChargeBegin(Date dateChargeBegin) {
		this.dateChargeBegin = dateChargeBegin;
	}
	public Date getDateChargeEnd() {
		return dateChargeEnd;
	}
	public void setDateChargeEnd(Date dateChargeEnd) {
		this.dateChargeEnd = dateChargeEnd;
	}
	public int getQualitysocre() {
		return qualitysocre;
	}
	public void setQualitysocre(int qualitysocre) {
		this.qualitysocre = qualitysocre;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public boolean isDiamondProduct() {
		return isDiamondProduct;
	}
	public void setDiamondProduct(boolean isDiamondProduct) {
		this.isDiamondProduct = isDiamondProduct;
	}
	
}
