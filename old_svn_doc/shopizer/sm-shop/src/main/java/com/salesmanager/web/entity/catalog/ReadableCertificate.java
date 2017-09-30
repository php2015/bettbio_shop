package com.salesmanager.web.entity.catalog;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class ReadableCertificate extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5172038178612982948L;
	private String name;
	private String baseinfo;
	private String docUrl;
	private String certificateImage;
	private String title;
	private String description;
	private String displayName;
	private String displayDesc;
	private int qualityNum=0;
	
	private String rimage; //处理前台页面图片的显示，图片的完整路径
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBaseinfo() {
		return baseinfo;
	}
	public void setBaseinfo(String baseinfo) {
		this.baseinfo = baseinfo;
	}
	public String getDocUrl() {
		return docUrl;
	}
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	public String getCertificateImage() {
		return certificateImage;
	}
	public void setCertificateImage(String certificateImage) {
		this.certificateImage = certificateImage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
