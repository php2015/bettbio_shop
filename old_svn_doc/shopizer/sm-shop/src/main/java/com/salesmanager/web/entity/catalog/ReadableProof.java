package com.salesmanager.web.entity.catalog;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class ReadableProof extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6340033737886674282L;
	/**
	 * 
	 */
	private Long id;
	private String title;
	private String buyer; 
	private String description;
	private String proofImage;
	private String dateBuyed;
	private String rimage; //处理前台页面图片的显示，图片的完整路径
	private String displayName;
	private String displayDesc;
	private int qualityNum=0;
	
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
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProofImage() {
		return proofImage;
	}
	public void setProofImage(String proofImage) {
		this.proofImage = proofImage;
	}
	
	public String getDateBuyed() {
		return dateBuyed;
	}
	public void setDateBuyed(String dateBuyed) {
		this.dateBuyed = dateBuyed;
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
