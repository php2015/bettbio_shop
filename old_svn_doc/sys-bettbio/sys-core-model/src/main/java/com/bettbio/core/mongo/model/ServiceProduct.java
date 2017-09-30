package com.bettbio.core.mongo.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 服务类产品
 * @author chang
 *
 */
public class ServiceProduct extends ProductBaseModel{
 private String price;//服务价格
 private List<Literature> literatures=new ArrayList<Literature>();//文献引用集 
 private List<PurchaseVoucher> purchaseVouchers=new ArrayList<PurchaseVoucher>();//购买凭证
 private List<ExperimentReport> experimentReports=new ArrayList<ExperimentReport>();//实验报告
public String getPrice() {
	return price;
}
public void setPrice(String price) {
	this.price = price;
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
public List<ExperimentReport> getExperimentReports() {
	return experimentReports;
}
public void setExperimentReports(List<ExperimentReport> experimentReports) {
	this.experimentReports = experimentReports;
}
 
}
