package com.salesmanager.web.entity.catalog;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class ReadableSelfProof extends Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731954993867379110L;
	/**
	 * 
	 */
	private Long id;
	private String title;
	private String description;
	private String selfproofImage;
	private String name;
	private String displayName;
	private String displayDesc;
	private int qualityNum=0;
	private String rimage; //处理前台页面图片的显示，图片的完整路径
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSelfproofImage() {
		return selfproofImage;
	}
	public void setSelfproofImage(String selfproofImage) {
		this.selfproofImage = selfproofImage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRimage() {
		return rimage;
	}
	public void setRimage(String rimage) {
		this.rimage = rimage;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayDesc() {
		return displayDesc;
	}
	public void setDisplayDesc(String displayDesc) {
		this.displayDesc = displayDesc;
	}
	public int getQualityNum() {
		return qualityNum;
	}
	public void setQualityNum(int qualityNum) {
		this.qualityNum = qualityNum;
	}
	
	

}
