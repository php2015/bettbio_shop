package com.bettbio.core.mongo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 耗材
 * 
 * @author chang
 *
 */
public class ConsumableMaterialProduct extends ProductBaseModel {
	private String model;// 型号
	private String placeOfOrigin;// 产地
	private List<ProductPrice> productPrices = new ArrayList<ProductPrice>();// 价格集
	private List<PurchaseVoucher> purchaseVouchers = new ArrayList<PurchaseVoucher>();// 购买凭证
	private List<AuthorityCertification> authorityCertifications = new ArrayList<AuthorityCertification>();// 第三方认证

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public List<ProductPrice> getProductPrices() {
		return productPrices;
	}

	public void setProductPrices(List<ProductPrice> productPrices) {
		this.productPrices = productPrices;
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
}
