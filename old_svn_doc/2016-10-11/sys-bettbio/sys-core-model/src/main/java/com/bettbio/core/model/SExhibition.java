package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_EXHIBITION")
public class SExhibition {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 展会编号
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 展会创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 展会开始时间
     */
    @Column(name = "START_DATE")
    private Date startDate;

    /**
     * 展会结束时间
     */
    @Column(name = "END_DATE")
    private Date endDate;

    /**
     * 展会地点
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 展会名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 展会海报
     */
    @Column(name = "IMG_URL")
    private String imgUrl;

    /**
     * 商家数量
     */
    @Column(name = "STORE_NUMBER")
    private Integer storeNumber;

    /**
     * 展会附件
     */
    @Column(name = "EXCEL_URL")
    private String excelUrl;

    /**
     * 微信URL
     */
    @Column(name = "WEIXIN_URL")
    private String weixinUrl;

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
     * 获取展会编号
     *
     * @return CODE - 展会编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置展会编号
     *
     * @param code 展会编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取展会创建时间
     *
     * @return CREATE_DATE - 展会创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置展会创建时间
     *
     * @param createDate 展会创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取展会开始时间
     *
     * @return START_DATE - 展会开始时间
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置展会开始时间
     *
     * @param startDate 展会开始时间
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取展会结束时间
     *
     * @return END_DATE - 展会结束时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置展会结束时间
     *
     * @param endDate 展会结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取展会地点
     *
     * @return ADDRESS - 展会地点
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置展会地点
     *
     * @param address 展会地点
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取展会名称
     *
     * @return NAME - 展会名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置展会名称
     *
     * @param name 展会名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取展会海报
     *
     * @return IMG_URL - 展会海报
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置展会海报
     *
     * @param imgUrl 展会海报
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取商家数量
     *
     * @return STORE_NUMBER - 商家数量
     */
    public Integer getStoreNumber() {
        return storeNumber;
    }

    /**
     * 设置商家数量
     *
     * @param storeNumber 商家数量
     */
    public void setStoreNumber(Integer storeNumber) {
        this.storeNumber = storeNumber;
    }

    /**
     * 获取展会附件
     *
     * @return EXCEL_URL - 展会附件
     */
    public String getExcelUrl() {
        return excelUrl;
    }

    /**
     * 设置展会附件
     *
     * @param excelUrl 展会附件
     */
    public void setExcelUrl(String excelUrl) {
        this.excelUrl = excelUrl;
    }

    /**
     * 获取微信URL
     *
     * @return WEIXIN_URL - 微信URL
     */
    public String getWeixinUrl() {
        return weixinUrl;
    }

    /**
     * 设置微信URL
     *
     * @param weixinUrl 微信URL
     */
    public void setWeixinUrl(String weixinUrl) {
        this.weixinUrl = weixinUrl;
    }
}