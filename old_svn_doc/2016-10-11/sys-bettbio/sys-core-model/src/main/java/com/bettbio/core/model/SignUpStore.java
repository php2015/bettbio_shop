package com.bettbio.core.model;

import javax.persistence.*;

@Table(name = "SIGN_UP_STORE")
public class SignUpStore {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商家名称
     */
    @Column(name = "STORE_NAME")
    private String storeName;

    /**
     * 商家编号
     */
    @Column(name = "STORE_CODE")
    private String storeCode;

    /**
     * 频率0-5
     */
    @Column(name = "FREQUENCY")
    private Integer frequency;

    /**
     * 参展商信息 excel路径
     */
    @Column(name = "EXHIBITORS_STORE_INFO")
    private String exhibitorsStoreInfo;

    /**
     * 展会编号
     */
    @Column(name = "EXHIBITION_CODE")
    private String exhibitionCode;

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
     * 获取商家名称
     *
     * @return STORE_NAME - 商家名称
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * 设置商家名称
     *
     * @param storeName 商家名称
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
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
     * 获取频率0-5
     *
     * @return FREQUENCY - 频率0-5
     */
    public Integer getFrequency() {
        return frequency;
    }

    /**
     * 设置频率0-5
     *
     * @param frequency 频率0-5
     */
    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    /**
     * 获取参展商信息 excel路径
     *
     * @return EXHIBITORS_STORE_INFO - 参展商信息 excel路径
     */
    public String getExhibitorsStoreInfo() {
        return exhibitorsStoreInfo;
    }

    /**
     * 设置参展商信息 excel路径
     *
     * @param exhibitorsStoreInfo 参展商信息 excel路径
     */
    public void setExhibitorsStoreInfo(String exhibitorsStoreInfo) {
        this.exhibitorsStoreInfo = exhibitorsStoreInfo;
    }

    /**
     * 获取展会编号
     *
     * @return EXHIBITION_CODE - 展会编号
     */
    public String getExhibitionCode() {
        return exhibitionCode;
    }

    /**
     * 设置展会编号
     *
     * @param exhibitionCode 展会编号
     */
    public void setExhibitionCode(String exhibitionCode) {
        this.exhibitionCode = exhibitionCode;
    }
}