package com.bettbio.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "PHONE_CODE")
public class PhoneCode {

	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 手机号
	 */
	@Column(name = "PHONE")
	private String phone;

	/**
	 * 验证码
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 发送时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 失效时间
	 */
	@Column(name = "INVALID_TIME")
	private Date invalidTime;

	/**
	 * 失效表示 0-失效 1-未失效
	 */
	@Column(name = "IS_INVALID")
	private Integer isInvalid;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	public Integer getIsInvalid() {
		return isInvalid;
	}

	public void setIsInvalid(Integer isInvalid) {
		this.isInvalid = isInvalid;
	}

}
