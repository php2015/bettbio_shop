package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_PRODUCT_CLASSIFICATION")
public class SProductClassification {
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
     * 父级分类
     */
    @Column(name = "PARENT_CODE")
    private String parentCode;

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
     * 简称
     */
    @Column(name = "SHORT_NAME")
    private String shortName;
    
    /**
     * 分类称号
     */
    @Column(name = "TITLE")
    private String title;
    
    /**
     * 描述
     */
    @Column(name = "REMARK")
    private String remark;
    
    /**
     * 可视的
     */
    @Column(name = "IS_VISUAL")
    private Integer isVisual;
    
    /**
     * 排序
     */
    @Column(name = "RANK_ORDER")
    private Integer rankOrder;
    
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
     * 获取父级分类
     *
     * @return PARENT_CODE - 父级分类
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置父级分类
     *
     * @param parentCode 父级分类
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsVisual() {
		return isVisual;
	}

	public void setIsVisual(Integer isVisual) {
		this.isVisual = isVisual;
	}

	public Integer getRankOrder() {
		return rankOrder;
	}

	public void setRankOrder(Integer rankOrder) {
		this.rankOrder = rankOrder;
	}
    
    
}