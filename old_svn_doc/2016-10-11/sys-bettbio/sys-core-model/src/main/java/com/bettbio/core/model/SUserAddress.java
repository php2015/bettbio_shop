package com.bettbio.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "S_USER_ADDRESS")
public class SUserAddress {
	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户编号
	 */
	@Column(name = "USER_CODE")
	private String userCode;

	/**
	 * 省
	 */
	@Column(name = "PROVINCE")
	private String province;

	/**
	 * 市
	 */
	@Column(name = "CITY")
	private String city;

	/**
	 * 区
	 */
	@Column(name = "AREA")
	private String area;

	/**
	 * 街道
	 */
	@Column(name = "STREET")
	private String street;

	/**
	 * 邮编
	 */
	@Column(name = "POSTCODE")
	private Integer postcode;

	/**
	 * 是否默认 0默认地址 1正常地址
	 */
	@Column(name = "IS_DEFAULT")
	private Integer isDefault = 1;

	/**
	 * 备注
	 */
	@Column(name = "REMARKS")
	private String remarks;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate = new Date();

	/**
	 * 编号
	 */
	@Column(name = "CODE")
	private String code;
	
	/**
	 * 用户名称
	 */
	@Column(name = "USER_NAME")
	private String userName;

	/**
	 * 用户手机
	 */
	@Column(name = "USER_PHONE")
	private String userPhone;

	/**
	 * 公司名称
	 */
	@Column(name = "COMPANY_NAME")
	private String companyName;
	
	/**
	 * 发票接收地址
	 */
	@Column(name="IS_DISTRIBUTION_ADDRESS")
	private Integer isDistributionAddress=1;

	/**
	 * 界面显示是否是默认收货地址
	 * 
	 * @return
	 */
	public String getDefaultAddress() {
		return isDefault == 0 ? "默认地址" : "正常地址";
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
	 * 获取用户编号
	 *
	 * @return USER_CODE - 用户编号
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * 设置用户编号
	 *
	 * @param userCode
	 *            用户编号
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取省
	 *
	 * @return PROVINCE - 省
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 设置省
	 *
	 * @param province
	 *            省
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 获取市
	 *
	 * @return CITY - 市
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 设置市
	 *
	 * @param city
	 *            市
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 获取区
	 *
	 * @return AREA - 区
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置区
	 *
	 * @param area
	 *            区
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 获取街道
	 *
	 * @return STREET - 街道
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * 设置街道
	 *
	 * @param street
	 *            街道
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * 获取邮编
	 *
	 * @return POSTCODE - 邮编
	 */
	public Integer getPostcode() {
		return postcode;
	}

	/**
	 * 设置邮编
	 *
	 * @param postcode
	 *            邮编
	 */
	public void setPostcode(Integer postcode) {
		this.postcode = postcode;
	}

	/**
	 * 获取是否默认 0默认地址 1正常地址
	 *
	 * @return IS_DEFAULT - 是否默认 0默认地址 1正常地址
	 */
	public Integer getIsDefault() {

		return isDefault;
	}

	/**
	 * 设置是否默认 0默认地址 1正常地址
	 *
	 * @param isDefault
	 *            是否默认 0默认地址 1正常地址
	 */
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取备注
	 *
	 * @return REMARKS - 备注
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置备注
	 *
	 * @param remarks
	 *            备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	 * 获取编号
	 *
	 * @return CODE - 编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置编号
	 *
	 * @param code
	 *            编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getIsDistributionAddress() {
		return isDistributionAddress;
	}

	public void setIsDistributionAddress(Integer isDistributionAddress) {
		this.isDistributionAddress = isDistributionAddress;
	}
	
}