package com.bettbio.core.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "S_ORDER_DATAILS")
public class SOrderDatails implements Serializable {
    /**
     * ����
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * �Ӷ������
     */
    @Column(name = "SUB_ORDER_CODE")
    private String subOrderCode;

    /**
     * 产品code
     */
    @Column(name = "PRODUCT_CODE")
    private String productCode;
    
    /**
     * ��Ʒ��������
     */
    @Column(name = "PRODUCT_NAME_CH")
    private String productNameCh;

    /**
     * ��ƷӢ������
     */
    @Column(name = "PRODUCT_NAME_EN")
    private String productNameEn;

    /**
     * ���
     */
    @Column(name = "PRODUCT_SPCE")
    private String productSpce;

    /**
     * ��Ʒ����
     */
    @Column(name = "PRODUCT_PRICE")
    private Double productPrice;

    /**
     * ��Ʒ����
     */
    @Column(name = "PRODUCT_NUM")
    private Integer productNum;

    /**
     * ��Ʒ�ܼ�
     */
    @Column(name = "PRODUCT_COUNT_PRICE")
    private Double productCountPrice;
    
    @Column(name="IMG_URL")
    private String imgUrl;

    private static final long serialVersionUID = 1L;

    /**
     * ��ȡ����
     *
     * @return ID - ����
     */
    public Integer getId() {
        return id;
    }

    /**
     * ��������
     *
     * @param id ����
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ��ȡ�Ӷ������
     *
     * @return SUB_ORDER_CODE - �Ӷ������
     */
    public String getSubOrderCode() {
        return subOrderCode;
    }

    /**
     * �����Ӷ������
     *
     * @param subOrderCode �Ӷ������
     */
    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    /**
     * ��ȡ��Ʒ��������
     *
     * @return PRODUCT_NAME_CH - ��Ʒ��������
     */
    public String getProductNameCh() {
        return productNameCh;
    }

    /**
     * ���ò�Ʒ��������
     *
     * @param productNameCh ��Ʒ��������
     */
    public void setProductNameCh(String productNameCh) {
        this.productNameCh = productNameCh;
    }

    /**
     * ��ȡ��ƷӢ������
     *
     * @return PRODUCT_NAME_EN - ��ƷӢ������
     */
    public String getProductNameEn() {
        return productNameEn;
    }

    /**
     * ���ò�ƷӢ������
     *
     * @param productNameEn ��ƷӢ������
     */
    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    /**
     * ��ȡ���
     *
     * @return PRODUCT_SPCE - ���
     */
    public String getProductSpce() {
        return productSpce;
    }

    /**
     * ���ù��
     *
     * @param productSpce ���
     */
    public void setProductSpce(String productSpce) {
        this.productSpce = productSpce;
    }

    /**
     * ��ȡ��Ʒ����
     *
     * @return PRODUCT_PRICE - ��Ʒ����
     */
    public Double getProductPrice() {
        return productPrice;
    }

    /**
     * ���ò�Ʒ����
     *
     * @param productPrice ��Ʒ����
     */
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * ��ȡ��Ʒ����
     *
     * @return PRODUCT_NUM - ��Ʒ����
     */
    public Integer getProductNum() {
        return productNum;
    }

    /**
     * ���ò�Ʒ����
     *
     * @param productNum ��Ʒ����
     */
    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    /**
     * ��ȡ��Ʒ�ܼ�
     *
     * @return PRODUCT_COUNT_PRICE - ��Ʒ�ܼ�
     */
    public Double getProductCountPrice() {
        return productCountPrice;
    }

    /**
     * ���ò�Ʒ�ܼ�
     *
     * @param productCountPrice ��Ʒ�ܼ�
     */
    public void setProductCountPrice(Double productCountPrice) {
        this.productCountPrice = productCountPrice;
    }

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}