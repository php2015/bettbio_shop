package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_AUTHENTICATION")
public class SAuthentication {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 认证编号
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 认证时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 截止日期
     */
    @Column(name = "CLOSING_DATE")
    private Date closingDate;

    /**
     * 认证机构名称
     */
    @Column(name = "MECHANISM_NAME")
    private String mechanismName;

    /**
     * 认证描述
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 认证图片路径
     */
    @Column(name = "IMG_URL")
    private String imgUrl;

    /**
     * 品牌编号 多个使用|分割
     */
    @Column(name = "BRAND_CODE")
    private String brandCode;

    /**
     * 商家编号
     */
    @Column(name = "STORE_CODE")
    private String storeCode;

    /**
     * 0厂家认证 1代理认证
     */
    @Column(name = "TYPE")
    private String type;

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
     * 获取认证编号
     *
     * @return CODE - 认证编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置认证编号
     *
     * @param code 认证编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取认证时间
     *
     * @return CREATE_DATE - 认证时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置认证时间
     *
     * @param createDate 认证时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取截止日期
     *
     * @return CLOSING_DATE - 截止日期
     */
    public Date getClosingDate() {
        return closingDate;
    }

    /**
     * 设置截止日期
     *
     * @param closingDate 截止日期
     */
    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * 获取认证机构名称
     *
     * @return MECHANISM_NAME - 认证机构名称
     */
    public String getMechanismName() {
        return mechanismName;
    }

    /**
     * 设置认证机构名称
     *
     * @param mechanismName 认证机构名称
     */
    public void setMechanismName(String mechanismName) {
        this.mechanismName = mechanismName;
    }

    /**
     * 获取认证描述
     *
     * @return REMARKS - 认证描述
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置认证描述
     *
     * @param remarks 认证描述
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取认证图片路径
     *
     * @return IMG_URL - 认证图片路径
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置认证图片路径
     *
     * @param imgUrl 认证图片路径
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取品牌编号 多个使用|分割
     *
     * @return BRAND_CODE - 品牌编号 多个使用|分割
     */
    public String getBrandCode() {
        return brandCode;
    }

    /**
     * 设置品牌编号 多个使用|分割
     *
     * @param brandCode 品牌编号 多个使用|分割
     */
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    /**
     * 获取商家编号
     *
     * @return STORE_CODE - 商家编号
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * 设置商家编号
     *
     * @param storeCode 商家编号
     */
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    /**
     * 获取0厂家认证 1代理认证
     *
     * @return TYPE - 0厂家认证 1代理认证
     */
    public String getType() {
        return type;
    }

    /**
     * 设置0厂家认证 1代理认证
     *
     * @param type 0厂家认证 1代理认证
     */
    public void setType(String type) {
        this.type = type;
    }
}