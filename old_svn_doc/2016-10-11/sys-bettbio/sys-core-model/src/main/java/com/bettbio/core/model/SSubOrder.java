package com.bettbio.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "S_SUB_ORDER")
public class SSubOrder implements Serializable {
    /**
     * ����
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * �����������
     */
    @Column(name = "PARENT_ORDER_CODE")
    private String parentOrderCode;

    /**
     * �Ӷ������
     */
    @Column(name = "ORDER_CODE")
    private String orderCode;

    /**
     * �̼ұ��
     */
    @Column(name = "STORE_CODE")
    private String storeCode;

    /**
     * �̼ҵ绰
     */
    @Column(name = "STORE_PHONE")
    private String storePhone;

    /**
     * �̼�����
     */
    @Column(name = "STORE_NAME")
    private String storeName;

    /**
     * ��Ʒ�ܼ�
     */
    @Column(name = "ORDER_COUNT_PRICE")
    private Double orderCountPrice;

    /**
     * 未发货   已发货  已收货  已评价  已取消  未付款 已关闭
     */
    @Column(name = "ORDER_STATE")
    private String orderState;

    /**
     * ��������
     */
    @Column(name = "LOGISTICS_NUM")
    private String logisticsNum;

    /**
     * ������
     */
    @Column(name = "CARRIER")
    private String carrier;
    
    @Column(name="OLD_STORE_CODE")
    private String oldStoreCode;
    
    @Column(name="REMARKS")
    private String remarks;
    
    @Column(name = "CREATE_DATE")
    private Date createDate=new Date();

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
     * ��ȡ�����������
     *
     * @return PARENT_ORDER_CODE - �����������
     */
    public String getParentOrderCode() {
        return parentOrderCode;
    }

    /**
     * ���ø����������
     *
     * @param parentOrderCode �����������
     */
    public void setParentOrderCode(String parentOrderCode) {
        this.parentOrderCode = parentOrderCode;
    }

    /**
     * ��ȡ�Ӷ������
     *
     * @return ORDER_CODE - �Ӷ������
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * �����Ӷ������
     *
     * @param orderCode �Ӷ������
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * ��ȡ�̼ұ��
     *
     * @return STORE_CODE - �̼ұ��
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * �����̼ұ��
     *
     * @param storeCode �̼ұ��
     */
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    /**
     * ��ȡ�̼ҵ绰
     *
     * @return STORE_PHONE - �̼ҵ绰
     */
    public String getStorePhone() {
        return storePhone;
    }

    /**
     * �����̼ҵ绰
     *
     * @param storePhone �̼ҵ绰
     */
    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    /**
     * ��ȡ�̼�����
     *
     * @return STORE_NAME - �̼�����
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * �����̼�����
     *
     * @param storeName �̼�����
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * ��ȡ��Ʒ�ܼ�
     *
     * @return ORDER_COUNT_PRICE - ��Ʒ�ܼ�
     */
    public Double getOrderCountPrice() {
        return orderCountPrice;
    }

    /**
     * ���ò�Ʒ�ܼ�
     *
     * @param orderCountPrice ��Ʒ�ܼ�
     */
    public void setOrderCountPrice(Double orderCountPrice) {
        this.orderCountPrice = orderCountPrice;
    }

    /**
     * ��ȡ����״̬
     *
     * @return ORDER_STATE - ����״̬
     */
    public String getOrderState() {
        return orderState;
    }

    /**
     * ���ö���״̬
     *
     * @param orderState ����״̬
     */
    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    /**
     * ��ȡ��������
     *
     * @return LOGISTICS_NUM - ��������
     */
    public String getLogisticsNum() {
        return logisticsNum;
    }

    /**
     * ������������
     *
     * @param logisticsNum ��������
     */
    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    /**
     * ��ȡ������
     *
     * @return CARRIER - ������
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * ���ó�����
     *
     * @param carrier ������
     */
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

	public String getOldStoreCode() {
		return oldStoreCode;
	}

	public void setOldStoreCode(String oldStoreCode) {
		this.oldStoreCode = oldStoreCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}