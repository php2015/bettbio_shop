package com.salesmanager.web.entity.catalog.product;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.Entity;

public class ListProduct extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6961073345088432083L;
	
	private String productName;
	private String productEnname;
	private String friendlyUrl;
	private String imageUrl;
	private int certificateNum=0;
	private int proofNum=0;
	private int thirdNum=0;
	private int reviewNum=0;
	private List<ReadableProductPrice> prices;
	private String manufacturer;
	private String productDesc;
	private String storeName;
	private Integer quality;
	private boolean isFree;
	private int cateType=1;
	private String code="";
	//授权书认证
	private String auth_id;
	private String auth_type;
	private int period=0;//现货
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getFriendlyUrl() {
		return friendlyUrl;
	}
	public void setFriendlyUrl(String friendlyUrl) {
		this.friendlyUrl = friendlyUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getCertificateNum() {
		return certificateNum;
	}
	public void setCertificateNum(int certificateNum) {
		this.certificateNum = certificateNum;
	}
	public int getProofNum() {
		return proofNum;
	}
	public void setProofNum(int proofNum) {
		this.proofNum = proofNum;
	}
	public int getThirdNum() {
		return thirdNum;
	}
	public void setThirdNum(int thirdNum) {
		this.thirdNum = thirdNum;
	}
	public List<ReadableProductPrice> getPrices() {
		return prices;
	}
	public void setPrices(List<ReadableProductPrice> prices) {
		this.prices = prices;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public int getReviewNum() {
		return reviewNum;
	}
	public void setReviewNum(int reviewNum) {
		this.reviewNum = reviewNum;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getProductEnname() {
		return productEnname;
	}
	public void setProductEnname(String productEnname) {
		this.productEnname = productEnname;
	}
	public Integer getQuality() {
		return quality;
	}
	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public int getCateType() {
		return cateType;
	}
	public void setCateType(int cateType) {
		this.cateType = cateType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	public String getAuth_type() {
		return auth_type;
	}
	public void setAuth_type(String auth_type) {
		this.auth_type = auth_type;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
}
