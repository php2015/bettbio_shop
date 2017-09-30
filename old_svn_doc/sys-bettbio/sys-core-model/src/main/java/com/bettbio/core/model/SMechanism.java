package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_MECHANISM")
public class SMechanism {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 机构代码
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 机构名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 父级机构
     */
    @Column(name = "PARENT_CODE")
    private String parentCode;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 0删除 1正常
     */
    @Column(name = "IS_DELETED")
    private Integer isDeleted;

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
     * 获取机构代码
     *
     * @return CODE - 机构代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置机构代码
     *
     * @param code 机构代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取机构名称
     *
     * @return NAME - 机构名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置机构名称
     *
     * @param name 机构名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取父级机构
     *
     * @return PARENT_CODE - 父级机构
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置父级机构
     *
     * @param parentCode 父级机构
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取0删除 1正常
     *
     * @return IS_DELETED - 0删除 1正常
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置0删除 1正常
     *
     * @param isDeleted 0删除 1正常
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}