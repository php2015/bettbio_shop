package com.bettbio.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * app注册临时表
 * 
 * @author simon
 *
 */
@Table(name = "USER_TMP")
public class UserTmp {
	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 唯一编码
	 */
	@Column(name = "CODE")
	private String code;
	
	/**
	 * 手机
	 */
	@Column(name = "PHONE")
	private String phone;

	/**
	 * 密码
	 */
	@Column(name = "PASSWORD")
	private String password;

	/**
	 * 真实姓名
	 */
	@Column(name = "USER_NAME")
	private String userName;

	/**
	 * 邮箱
	 */
	@Column(name = "EMAIL")
	private String email;

	/**
	 * 课题组
	 */
	@Column(name = "MECHANISM_SUB_CODE")
	private String mechanismSubCode;

	/**
	 * 学校
	 */
	@Column(name = "COMPANY")
	private String company;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMechanismSubCode() {
		return mechanismSubCode;
	}

	public void setMechanismSubCode(String mechanismSubCode) {
		this.mechanismSubCode = mechanismSubCode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
