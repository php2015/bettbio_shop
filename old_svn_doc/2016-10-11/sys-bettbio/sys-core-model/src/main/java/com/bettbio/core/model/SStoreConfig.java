package com.bettbio.core.model;

import javax.persistence.*;

@Table(name = "S_STORE_CONFIG")
public class SStoreConfig {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商家编号
     */
    @Column(name = "STORE_CODE")
    private String storeCode;

    /**
     * 配置名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 值
     */
    @Column(name = "VALUE")
    private String value;

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
     * 获取配置名称
     *
     * @return NAME - 配置名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置配置名称
     *
     * @param name 配置名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取值
     *
     * @return VALUE - 值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    public void setValue(String value) {
        this.value = value;
    }
}