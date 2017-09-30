package com.salesmanager.web.entity.shop;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class PinYinName extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 795056989042564160L;
	
	private String pinyin;
	private String name;
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
