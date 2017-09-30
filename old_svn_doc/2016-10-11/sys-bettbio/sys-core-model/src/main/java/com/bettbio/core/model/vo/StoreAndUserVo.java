package com.bettbio.core.model.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * store
 * @author simon
 *
 */
public class StoreAndUserVo {
	private int storeId;//商家ID
	private int userId;//用户ID
	private String name; //商家名称
	private String linkman;//联系人
	private String landline;//座机电话
	private String phone;//手机
	private String email;//商家邮箱地址
	private String qq; //QQ号
	private String isSeedUser;//确认种子用户
	
	private String address;//商家地址
	private String provincial;//商家省会
	private String city;//商家所在市
	private String area;//商家所在区，县
	private String chineseAbout;//中文简介
	private String englishAbout;//英文简介
	private String storeUrl;//商家网址
	
	private MultipartFile file;
	private String licenseImageUrl;//营业执照图片地址
	
	public String getLicenseImageUrl() {
		return licenseImageUrl;
	}
	public void setLicenseImageUrl(String licenseImageUrl) {
		this.licenseImageUrl = licenseImageUrl;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getLandline() {
		return landline;
	}
	public void setLandline(String landline) {
		this.landline = landline;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getIsSeedUser() {
		return isSeedUser;
	}
	public void setIsSeedUser(String isSeedUser) {
		this.isSeedUser = isSeedUser;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvincial() {
		return provincial;
	}
	public void setProvincial(String provincial) {
		this.provincial = provincial;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getChineseAbout() {
		return chineseAbout;
	}
	public void setChineseAbout(String chineseAbout) {
		this.chineseAbout = chineseAbout;
	}
	public String getEnglishAbout() {
		return englishAbout;
	}
	public void setEnglishAbout(String englishAbout) {
		this.englishAbout = englishAbout;
	}
	public String getStoreUrl() {
		return storeUrl;
	}
	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}
	public int getUserId() {
		return userId;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
		
}
