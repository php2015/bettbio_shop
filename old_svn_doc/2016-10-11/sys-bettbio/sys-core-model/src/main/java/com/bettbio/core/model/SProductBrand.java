package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_PRODUCT_BRAND")
public class SProductBrand {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分类编号
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 分类名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 0禁用 1正常 默认1
     */
    @Column(name = "IS_DISABLE")
    private Integer isDisable;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    /**
     * 品牌所属机构名称
     */
    @Column(name = "MECHANISM_NAME")
    private String mechanismName;
    
    /**
     * 品牌网站地址
     */
    @Column(name = "BRAND_URL")
    private String brandUrl;

    /**
     * 品牌描述
     */
    @Column(name = "BRAND_REMARK")
    private String brandRemark;
    
    /**
     * 品牌简称
     */
    @Column(name = "SHORT_NAME")
    private String shortName;
    
    /**
     * 品牌logo
     */
    @Column(name = "BRAND_LOGO")
    private String brandLogo;
    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分类编号
     *
     * @return CODE - 分类编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置分类编号
     *
     * @param code 分类编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取分类名称
     *
     * @return NAME - 分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名称
     *
     * @param name 分类名称
     */
    public void setName(String name) {
        this.name = name;
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
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取品牌所属机构名称
     *
     * @return MECHANISM_NAME - 品牌所属机构名称
     */
    public String getMechanismName() {
        return mechanismName;
    }

    /**
     * 设置品牌所属机构名称
     *
     * @param mechanismName 品牌所属机构名称
     */
    public void setMechanismName(String mechanismName) {
        this.mechanismName = mechanismName;
    }

	public String getBrandUrl() {
		return brandUrl;
	}

	public void setBrandUrl(String brandUrl) {
		this.brandUrl = brandUrl;
	}

	public String getBrandRemark() {
		return brandRemark;
	}

	public void setBrandRemark(String brandRemark) {
		this.brandRemark = brandRemark;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}
    
}