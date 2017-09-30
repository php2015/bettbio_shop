package com.bettbio.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 需求详情
 * 
 * @author chang
 *
 */
@Table(name = "S_DEMAND_DETAIL")
public class SDemandDetail {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	

    /**
     * 产品名称
     */
    @Column(name = "PRODUCT_NAME")
    private String productName;

    /**
     * 品牌名称
     */
    @Column(name = "BRAND_NAME")
    private String brandName;

    /**
     * 供应商
     */
    @Column(name = "SUPPLIER_NAME")
    private String supplierName;

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
     * 获取产品名称
     *
     * @return PRODUCT_NAME - 产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置产品名称
     *
     * @param productName 产品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取品牌名称
     *
     * @return BRAND_NAME - 品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 获取供应商
     *
     * @return SUPPLIER_NAME - 供应商
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * 设置供应商
     *
     * @param supplierName 供应商
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
