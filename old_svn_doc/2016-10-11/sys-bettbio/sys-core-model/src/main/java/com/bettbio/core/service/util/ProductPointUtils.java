package com.bettbio.core.service.util;

import java.util.List;

import com.bettbio.core.mongo.model.AuthorityCertification;
import com.bettbio.core.mongo.model.ExperimentReport;
import com.bettbio.core.mongo.model.Literature;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.PurchaseVoucher;
/**
 * 质量分计算
 * @author simon
 *
 */
public class ProductPointUtils {
	
	public static Product getPoint(Product product){
		String code = product.getProductClass().getCode();
		if(code.startsWith("01")){
			return	new ProductPointUtils().getReagentPoint(product);
		}else if(code.startsWith("02") || code.startsWith("03")){
			return	new ProductPointUtils().getConsumablePoint(product);
		}else if(code.startsWith("04")){
			return	new ProductPointUtils().getServicePoint(product);
		}
		return product;
	}
	
	/*
	 * 试剂类计算
	 */
	public Product getReagentPoint(Product product) {
		//文献和（详情、图片）
		int points = 0;
		List<Literature> literatures = product.getLiteratures();
		if(literatures.size()==0){
			if(product.getImgUrls().size()!=0){
				points += 20;
			}
		}else if(literatures.size() == 1){
			points += 48; 
		}else if(literatures.size() == 2){
			points += 54; 
		}else{
			points += 60;
		}
		//购买凭证
		List<PurchaseVoucher> purchaseVouchers = product.getPurchaseVouchers();
		if(purchaseVouchers.size() == 1){
			points += 10;
		}else if(purchaseVouchers.size() == 2){
			points += 20;
		}
		//第三方认证
		List<AuthorityCertification> authorityCertifications = product.getAuthorityCertifications();
		if(authorityCertifications.size() > 0){
			points += 40;
		}
		//实验报告
		List<ExperimentReport> experimentReports = product.getExperimentReports();
		if(experimentReports.size() > 0){
			points += 10;
		}
		product.setQualityScore(points);
		return product;
	}
	
	/*
	 * 仪器耗材类计算
	 */
	public Product getConsumablePoint(Product product) {
		return product;
	}
	
	/*
	 * 服务类计算
	 */
	public Product getServicePoint(Product product) {
		return product;
	}
}
