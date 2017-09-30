package com.salesmanager.web.entity.catalog.product;

import com.salesmanager.web.entity.catalog.CatalogEntity;

public class ProductDescription extends CatalogEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String simpleDescription; //简单描述
	
	private String testDescription; //实验数据内容字段

	private String storecondDescription; //存储条件描述
	
	private String methodDescription; //实验方法内容字段
	
	public String getSimpleDescription() {
		return simpleDescription;
	}
	public void setSimpleDescription(String simpleDescription) {
		this.simpleDescription = simpleDescription;
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
	

}
