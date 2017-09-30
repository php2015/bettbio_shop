package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_STORE")
public class SStore {
	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 商铺编号
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 商铺名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 商铺描述
	 */
	@Column(name = "REMARKS")
	private String remarks;

	/**
	 * 图标路径
	 */
	@Column(name = "LOGO_URL")
	private String logoUrl;

	/**
	 * 确认认证
	 */
	@Column(name = "IS_AUTH")
	private Integer isAuth;

	/**
	 * 分类 0代理商 1经销商 2生产商
	 */
	@Column(name = "TYPE")
	private Integer type=2;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate=new Date();

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	/**
	 * 背景图片路径
	 */
	@Column(name = "BACKGROUND_IMAGE_URL")
	private String backgroundImageUrl;

	/**
	 * 所属机构/公司/生物园区
	 */
	@Column(name = "MECHANISM_CODE")
	private String mechanismCode;

	/**
	 * 参展频率
	 */
	@Column(name = "FREQUENCY")
	private Integer frequency=0;

	/**
	 * 确认激活
	 */
	@Column(name = "IS_ACTIVATION")
	private Integer isActivation;

	/**
	 * 0种子用户 1非种子用户
	 */
	@Column(name = "IS_SEED_USER")
	private Integer isSeedUser = 0;

	/**
	 * 0禁用 1正常 默认1
	 */
	@Column(name = "IS_DISABLE")
	private Integer isDisable = 1;

	/**
	 * 来源0网站 1微信公众号 2 手机APP
	 */
	@Column(name = "SOURCE")
	private Integer source = 0;

	/**
	 * 注册资本
	 */
	@Column(name = "REGISTERED_CAPITAL")
	private String registeredCapital;

	/**
	 * 成立时间
	 */
	@Column(name = "SET_UP_TIME")
	private Date setUpTime;

	/**
	 * 邮箱地址
	 */
	@Column(name = "EMAIL")
	private String email;

	/**
	 * 0删除 1正常
	 */
	@Column(name = "IS_DELETED")
	private Integer isDeleted=1;

	/**
	 * 商家地址
	 */
	@Column(name = "ADDRESS")
	private String address;

	/**
	 * 中文简介
	 */
	@Column(name = "CHINESE_ABOUT")
	private String chineseAbout;

	/**
	 * 中文简介
	 */
	@Column(name = "ENGLISH_ABOUT")
	private String englishAbout;

	/**
	 * 商铺网址
	 */
	@Column(name = "STORE_URL")
	private String storeUrl;
	
	/**
	 * 固话
	 */
	@Column(name = "LANDLINE")
	private String landline;
	
	/**
	 * 营业执照图片
	 */
	@Column(name = "LICENSE_IMAGE_URL")
	private String licenseImageUrl;

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	/**
	 * 获取主键
	 *
	 * @return ID - 主键
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置主键
	 *
	 * @param id
	 *            主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取商铺编号
	 *
	 * @return CODE - 商铺编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置商铺编号
	 *
	 * @param code
	 *            商铺编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取商铺名称
	 *
	 * @return NAME - 商铺名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置商铺名称
	 *
	 * @param name
	 *            商铺名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取商铺描述
	 *
	 * @return REMARKS - 商铺描述
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置商铺描述
	 *
	 * @param remarks
	 *            商铺描述
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 获取图标路径
	 *
	 * @return LOGO_URL - 图标路径
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * 设置图标路径
	 *
	 * @param logoUrl
	 *            图标路径
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	/**
	 * 获取确认认证
	 *
	 * @return IS_AUTH - 确认认证
	 */
	public Integer getIsAuth() {
		return isAuth;
	}

	/**
	 * 设置确认认证
	 *
	 * @param isAuth
	 *            确认认证
	 */
	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	/**
	 * 获取分类 0代理商 1经销商
	 *
	 * @return TYPE - 分类 0代理商 1经销商
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置分类 0代理商 1经销商
	 *
	 * @param type
	 *            分类 0代理商 1经销商
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取创建时间
	 *
	 * @return CREATE_DATE - 创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createDate
	 *            创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取更新时间
	 *
	 * @return UPDATE_DATE - 更新时间
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateDate
	 *            更新时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 获取背景图片路径
	 *
	 * @return BACKGROUND_IMAGE_URL - 背景图片路径
	 */
	public String getBackgroundImageUrl() {
		return backgroundImageUrl;
	}

	/**
	 * 设置背景图片路径
	 *
	 * @param backgroundImageUrl
	 *            背景图片路径
	 */
	public void setBackgroundImageUrl(String backgroundImageUrl) {
		this.backgroundImageUrl = backgroundImageUrl;
	}

	/**
	 * 获取所属机构/公司/生物园区
	 *
	 * @return MECHANISM_CODE - 所属机构/公司/生物园区
	 */
	public String getMechanismCode() {
		return mechanismCode;
	}

	/**
	 * 设置所属机构/公司/生物园区
	 *
	 * @param mechanismCode
	 *            所属机构/公司/生物园区
	 */
	public void setMechanismCode(String mechanismCode) {
		this.mechanismCode = mechanismCode;
	}

	/**
	 * 获取参展频率
	 *
	 * @return FREQUENCY - 参展频率
	 */
	public Integer getFrequency() {
		return frequency;
	}

	/**
	 * 设置参展频率
	 *
	 * @param frequency
	 *            参展频率
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	/**
	 * 获取确认激活
	 *
	 * @return IS_ACTIVATION - 确认激活
	 */
	public Integer getIsActivation() {
		return isActivation;
	}

	/**
	 * 设置确认激活
	 *
	 * @param isActivation
	 *            确认激活
	 */
	public void setIsActivation(Integer isActivation) {
		this.isActivation = isActivation;
	}

	/**
	 * 获取0种子用户 1非种子用户
	 *
	 * @return IS_SEED_USER - 0种子用户 1非种子用户
	 */
	public Integer getIsSeedUser() {
		return isSeedUser;
	}

	/**
	 * 设置0种子用户 1非种子用户
	 *
	 * @param isSeedUser
	 *            0种子用户 1非种子用户
	 */
	public void setIsSeedUser(Integer isSeedUser) {
		this.isSeedUser = isSeedUser;
	}

	/**
	 * 获取0禁用 1正常 默认1
	 *
	 * @return IS_DISABLE - 0禁用 1正常 默认1
	 */
	public Integer getIsDisable() {
		return isDisable;
	}

	/**
	 * 设置0禁用 1正常 默认1
	 *
	 * @param isDisable
	 *            0禁用 1正常 默认1
	 */
	public void setIsDisable(Integer isDisable) {
		this.isDisable = isDisable;
	}

	/**
	 * 获取来源0网站 1微信公众号 2 手机APP
	 *
	 * @return SOURCE - 来源0网站 1微信公众号 2 手机APP
	 */
	public Integer getSource() {
		return source;
	}

	/**
	 * 设置来源0网站 1微信公众号 2 手机APP
	 *
	 * @param source
	 *            来源0网站 1微信公众号 2 手机APP
	 */
	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * 获取注册资本
	 *
	 * @return REGISTERED_CAPITAL - 注册资本
	 */
	public String getRegisteredCapital() {
		return registeredCapital;
	}

	/**
	 * 设置注册资本
	 *
	 * @param registeredCapital
	 *            注册资本
	 */
	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	/**
	 * 获取成立时间
	 *
	 * @return SET_UP_TIME - 成立时间
	 */
	public Date getSetUpTime() {
		return setUpTime;
	}

	/**
	 * 设置成立时间
	 *
	 * @param setUpTime
	 *            成立时间
	 */
	public void setSetUpTime(Date setUpTime) {
		this.setUpTime = setUpTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getLicenseImageUrl() {
		return licenseImageUrl;
	}

	public void setLicenseImageUrl(String licenseImageUrl) {
		this.licenseImageUrl = licenseImageUrl;
	}

}