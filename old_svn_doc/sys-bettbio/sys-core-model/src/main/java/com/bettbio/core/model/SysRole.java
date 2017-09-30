package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_ROLE")
public class SysRole {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 编号
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 0禁用 1正常 默认1
     */
    @Column(name = "IS_DISABLE")
    private Integer isDisable;

    /**
     * 权限集合
     */
    @Column(name = "JURISDICTION_IDS")
    private String jurisdictionIds;
    
    /**
     * 商家CODE 默认0 百图平台自己角色
     */
    @Column(name = "STORE_CODE")
    private String storeCode;
    
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
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取角色名称
     *
     * @return name - 角色名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名称
     *
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name;
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
     * @param code 编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATE_TIME - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
     * @param isDisable 0禁用 1正常 默认1
     */
    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    /**
     * 获取权限集合
     *
     * @return JURISDICTION_IDS - 权限集合
     */
    public String getJurisdictionIds() {
        return jurisdictionIds;
    }

    /**
     * 设置权限集合
     *
     * @param jurisdictionIds 权限集合
     */
    public void setJurisdictionIds(String jurisdictionIds) {
        this.jurisdictionIds = jurisdictionIds;
    }

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
}