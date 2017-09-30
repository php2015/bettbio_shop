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

}
