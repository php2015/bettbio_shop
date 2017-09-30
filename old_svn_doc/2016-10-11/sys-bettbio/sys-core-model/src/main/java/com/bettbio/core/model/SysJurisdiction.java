package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_JURISDICTION")
public class SysJurisdiction {
	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 权限编号
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 权限名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 权限URL
	 */
	@Column(name = "URL")
	private String url;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate;

	/**
	 * 0禁用 1正常 默认1
	 */
	@Column(name = "IS_DISABLE")
	private Integer isDisable;

	/**
	 * 父级权限
	 */
	@Column(name = "PARENT_CODE")
	private String parentCode;

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
	 * 获取权限编号
	 *
	 * @return CODE - 权限编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置权限编号
	 *
	 * @param code
	 *            权限编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取权限名称
	 *
	 * @return NAME - 权限名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置权限名称
	 *
	 * @param name
	 *            权限名称
	 */
	public void setName(String name) {
		this.name = name;
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
	 * 获取父级权限
	 *
	 * @return PARENT_CODE - 父级权限
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * 设置父级权限
	 *
	 * @param parentCode
	 *            父级权限
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}