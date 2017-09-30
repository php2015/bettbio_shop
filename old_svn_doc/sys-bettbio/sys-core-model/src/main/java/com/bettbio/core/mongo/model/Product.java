package com.bettbio.core.mongo.model;

import java.util.ArrayList;
import java.util.List;


public class Product extends ProductBaseModel {

	private String batchCode;// 批次号
	private String CasCode;// Cas号
	private String storageCondition;// 存储条件描述
	private List<ProductPrice> productPrices = new ArrayList<ProductPrice>();// 价格集
	private List<Literature> literatures = new ArrayList<Literature>();// 文献引用集
	private List<PurchaseVoucher> purchaseVouchers = new ArrayList<PurchaseVoucher>();// 购买凭证
	private List<AuthorityCertification> authorityCertifications = new ArrayList<AuthorityCertification>();// 第三方认证
	private List<ExperimentReport> experimentReports = new ArrayList<ExperimentReport>();// 实验报告

	private String price;// 服务价格private String model;// 型号
	private String placeOfOrigin;// 产地

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getCasCode() {
		return CasCode;
	}

	public void setCasCode(String casCode) {
		CasCode = casCode;
	}

	public String getStorageCondition() {
		return storageCondition;
	}

	public void setStorageCondition(String storageCondition) {
		this.storageCondition = storageCondition;
	}

	public List<ProductPrice> getProductPrices() {
		return productPrices;
	}

	public void setProductPrices(List<ProductPrice> productPrices) {
		this.productPrices = productPrices;
	}

	public List<Literature> getLiteratures() {
		return literatures;
	}

	public void setLiteratures(List<Literature> literatures) {
		this.literatures = literatures;
	}

	public List<PurchaseVoucher> getPurchaseVouchers() {
		return purchaseVouchers;
	}

	public void setPurchaseVouchers(List<PurchaseVoucher> purchaseVouchers) {
		this.purchaseVouchers = purchaseVouchers;
	}

	public List<AuthorityCertification> getAuthorityCertifications() {
		return authorityCertifications;
	}

	public void setAuthorityCertifications(List<AuthorityCertification> authorityCertifications) {
		this.authorityCertifications = authorityCertifications;
	}

	public List<ExperimentReport> getExperimentReports() {
		return experimentReports;
	}

	public void setExperimentReports(List<ExperimentReport> experimentReports) {
		this.experimentReports = experimentReports;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

}
