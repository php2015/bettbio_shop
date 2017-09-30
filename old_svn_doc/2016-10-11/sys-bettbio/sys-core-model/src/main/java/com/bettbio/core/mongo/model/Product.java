package com.bettbio.core.mongo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.SStore;


public class Product{
 	private Integer id;//主键
    private String code;//产品唯一码
    private Date createDate;//创建时间
    private Date updateDate;//更新时间
    private Integer qualityScore;//质量分
    private Integer isExamine;//确认审核
    private Integer isEnable;//确认上架
    private Date enableDate;//上架时间
    private Integer productIsFree;//合作模式
    private Date dateChargeBegin;//有效范围开始
    private Date dateChargeEnd;//有效范围结束
    private SStore store;//所属店铺
    private SProductClassification productClass;//分类编号
    private Date closingDate;//截止时间
    private SProductBrand productBrand;//品牌
    private String productNameCh;//产品中文名称
    private String productNameEn;//产品英文名称
    private String productNo;//货号
    private String simpleDescription;//简单描述
    private String detailedDescription;//详细描述
    private List<String> imgUrls=new ArrayList<String>();//产品图片集合
    private List<String> files=new ArrayList<String>();//附件集合
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
	private String model;//商品型号
	
	//添加到索引的字段
	private String friendlyUrl;//有好的URL 唯一

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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFriendlyUrl() {
		return friendlyUrl;
	}

	public void setFriendlyUrl(String friendlyUrl) {
		this.friendlyUrl = friendlyUrl;
	}
	
	public String getDefaultImgUrl(){
		if (getImgUrls() != null && getImgUrls().size() > 0) return getImgUrls().get(0);
		return "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	public Integer getIsExamine() {
		return isExamine;
	}

	public void setIsExamine(Integer isExamine) {
		this.isExamine = isExamine;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Date getEnableDate() {
		return enableDate;
	}

	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}

	public SStore getStore() {
		return store;
	}

	public void setStore(SStore store) {
		this.store = store;
	}

	public SProductClassification getProductClass() {
		return productClass;
	}

	public void setProductClass(SProductClassification productClass) {
		this.productClass = productClass;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public SProductBrand getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(SProductBrand productBrand) {
		this.productBrand = productBrand;
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

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getSimpleDescription() {
		return simpleDescription;
	}

	public void setSimpleDescription(String simpleDescription) {
		this.simpleDescription = simpleDescription;
	}

	public String getDetailedDescription() {
		return detailedDescription;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

	public List<String> getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
	
	public Integer getProductIsFree() {
		return productIsFree;
	}

	public void setProductIsFree(Integer productIsFree) {
		this.productIsFree = productIsFree;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Product("
				+ "id="+ this.id
				+ ",name="+ this.productNameCh
				+ ")";
	}
}
