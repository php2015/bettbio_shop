package com.bettbio.core.mongo.model;

/**
 * 购物车
 * 
 * @author chang
 *
 */
public class ShoppingCart{
	private String productCode;// 产品编号
    private String productNameCh;//产品中文名称
    private String productNameEn;//产品英文名称
	private String productSpec;// 规格
	private int number;//数量
	private Double unitPrice;// 单价
	private String storeName;//商家名称
	private String storeCode;//商家编号
	private String productImg;//产品图片

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getProductNameCh() {
		return productNameCh;
	}

	public void setProductNameCh(String productNameCh) {
		this.productNameCh = productNameCh;
	}

	public String getProductNameEn() {
		return productNameEn;
	}

	public void setProductNameEn(String productNameEn) {
		this.productNameEn = productNameEn;
	}

	public String getProductSpec() {
		return productSpec;
	}

	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public boolean productEquals(Object obj) {
		if(obj!=null){
			ShoppingCart shoppingCart=(ShoppingCart)obj;
			
			if(shoppingCart.getProductCode()!=null&&shoppingCart.getProductSpec()!=null&&shoppingCart.getProductCode().equals(this.productCode)&&//判断产品编号以及产品规格一致
			  shoppingCart.getProductSpec().equals(this.productSpec)){
				return true;
			}
		}else{
			return false;
		}
		return false;
	}
	
	

}
