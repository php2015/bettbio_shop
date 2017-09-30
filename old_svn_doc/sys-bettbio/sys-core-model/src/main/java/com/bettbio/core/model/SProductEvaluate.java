package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_PRODUCT_EVALUATE")
public class SProductEvaluate {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 评价时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 质量评分
     */
    @Column(name = "QUALITY_SCORE")
    private Integer qualityScore;

    /**
     * 服务得分
     */
    @Column(name = "SERVICE_SCORE")
    private Integer serviceScore;

    /**
     * 评论内容
     */
    @Column(name = "CONTENT")
    private String content;

    /**
     * 评论人
     */
    @Column(name = "USER_NAME")
    private String userName;

    /**
     * 回复评论
     */
    @Column(name = "PARENT_CODE")
    private String parentCode;

    /**
     * 用户编号
     */
    @Column(name = "USER_CODE")
    private String userCode;

    /**
     * 产品编号
     */
    @Column(name = "PRODUCT_CODE")
    private String productCode;

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
     * 获取评价时间
     *
     * @return CREATE_DATE - 评价时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置评价时间
     *
     * @param createDate 评价时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取质量评分
     *
     * @return QUALITY_SCORE - 质量评分
     */
    public Integer getQualityScore() {
        return qualityScore;
    }

    /**
     * 设置质量评分
     *
     * @param qualityScore 质量评分
     */
    public void setQualityScore(Integer qualityScore) {
        this.qualityScore = qualityScore;
    }

    /**
     * 获取服务得分
     *
     * @return SERVICE_SCORE - 服务得分
     */
    public Integer getServiceScore() {
        return serviceScore;
    }

    /**
     * 设置服务得分
     *
     * @param serviceScore 服务得分
     */
    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
    }

    /**
     * 获取评论内容
     *
     * @return CONTENT - 评论内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置评论内容
     *
     * @param content 评论内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取评论人
     *
     * @return USER_NAME - 评论人
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置评论人
     *
     * @param userName 评论人
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取回复评论
     *
     * @return PARENT_CODE - 回复评论
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置回复评论
     *
     * @param parentCode 回复评论
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
     * @param userCode 用户编号
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /**
     * 获取产品编号
     *
     * @return PRODUCT_CODE - 产品编号
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * 设置产品编号
     *
     * @param productCode 产品编号
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}