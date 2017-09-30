package com.bettbio.core.mongo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;

/**
 * 产品父级类
 * @author chang
 *
 */
public class ProductBaseModel{

    private Integer id;//主键
    private String code;//产品唯一码
    private Date createDate;//创建时间
    private Date updateDate;//更新时间
    private Integer qualityScore;//质量分
    private Integer isExamine;//确认审核
    private Integer isEnable;//确认上架
    private Date enableDate;//上架时间
    private String storeCode;//所属店铺
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
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
	//添加图片路径倒集合
	public void addImgUrl(String url){
		this.imgUrls.add(url);
	}
	//添加附件路径倒集合
	public void addfiles(String url){
		this.files.add(url);
	}    
}
