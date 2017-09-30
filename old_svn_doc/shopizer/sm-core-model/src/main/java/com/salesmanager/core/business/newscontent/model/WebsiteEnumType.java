package com.salesmanager.core.business.newscontent.model;

public enum WebsiteEnumType {

	DEFAULT(0, "通用"), BIOON(1,"生物谷"), BIOMART(2, "丁香通"), BIODISCOVER(3, "生物探索");
	
	private int code;
	private String name;
	
	private WebsiteEnumType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
