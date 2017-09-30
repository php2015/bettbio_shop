package com.salesmanager.core.business.catalog.product.model.description;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.Description;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name = "PRODUCT_DESCRIPTION", schema=SchemaConstant.SALESMANAGER_SCHEMA, uniqueConstraints={
	@UniqueConstraint(columnNames={
			"PRODUCT_ID",
			"LANGUAGE_ID"
		})
	}
)
public class ProductDescription extends Description {
	private static final long serialVersionUID = -7991123535661321865L;
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
	@Column(name = "EN_NAME", length=512)
	private String enName;
	
	@Column(name = "PRODUCT_HIGHLIGHT")
	private String productHighlight;

	@Column(name = "DOWNLOAD_LNK")
	private String productExternalDl;

	@Column(name = "SEF_URL")
	private String seUrl;

	@Column(name = "META_TITLE")
	private String metatagTitle;

	@Column(name = "META_KEYWORDS")
	private String metatagKeywords;

	@Column(name = "META_DESCRIPTION", length=1000)
	private String metatagDescription;
	
	@Column(name="TEST_DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String testDescription; //实验数据内容字段

	@Column(name="STORECOND_DESCRIPTION", length=1000)
	private String storecondDescription; //存储条件描述
	
	@Column(name="SIMPLE_DESCRIPTION", length=1000)	
	private String simpleDescription; //简单描述
	
	@Column(name="METHOD_DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String methodDescription; //实验方法内容字段
	
	public String getSimpleDescription() {
		return simpleDescription;
	}

	public void setSimpleDescription(String simpleDescription) {
		this.simpleDescription = simpleDescription;
	}

	public ProductDescription() {
	}

	public String getProductHighlight() {
		return productHighlight;
	}

	public void setProductHighlight(String productHighlight) {
		this.productHighlight = productHighlight;
	}

	public String getProductExternalDl() {
		return productExternalDl;
	}

	public void setProductExternalDl(String productExternalDl) {
		this.productExternalDl = productExternalDl;
	}

	public String getSeUrl() {
		return seUrl;
	}

	public void setSeUrl(String seUrl) {
		this.seUrl = seUrl;
	}

	public String getMetatagTitle() {
		return metatagTitle;
	}

	public void setMetatagTitle(String metatagTitle) {
		this.metatagTitle = metatagTitle;
	}

	public String getMetatagKeywords() {
		return metatagKeywords;
	}

	public void setMetatagKeywords(String metatagKeywords) {
		this.metatagKeywords = metatagKeywords;
	}

	public String getMetatagDescription() {
		return metatagDescription;
	}

	public void setMetatagDescription(String metatagDescription) {
		this.metatagDescription = metatagDescription;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	public String getStorecondDescription() {
		return storecondDescription;
	}

	public void setStorecondDescription(String storecondDescription) {
		this.storecondDescription = storecondDescription;
	}

	public String getMethodDescription() {
		return methodDescription;
	}

	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}


}
