package com.bettbio.core.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "S_ORDER_STATUS_HISTORY")
public class SOrderStatusHistory implements Serializable {
    /**
     * ����
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * �������
     */
    @Column(name = "SUB_ORDER_CODE")
    private String subOrderCode;

    /**
     * ����ʱ��
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    /**
     * ��ʷ״̬
     */
    @Column(name = "HISTORY_STATUS")
    private Integer historyStatus;

    /**
     * ��״̬
     */
    @Column(name = "NEW_STATUS")
    private Integer newStatus;

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
     * ��ȡ�������
     *
     * @return SUB_ORDER_CODE - �������
     */
    public String getSubOrderCode() {
        return subOrderCode;
    }

    /**
     * ���ö������
     *
     * @param subOrderCode �������
     */
    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return UPDATE_DATE - ����ʱ��
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * ���ø���ʱ��
     *
     * @param updateDate ����ʱ��
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * ��ȡ��ʷ״̬
     *
     * @return HISTORY_STATUS - ��ʷ״̬
     */
    public Integer getHistoryStatus() {
        return historyStatus;
    }

    /**
     * ������ʷ״̬
     *
     * @param historyStatus ��ʷ״̬
     */
    public void setHistoryStatus(Integer historyStatus) {
        this.historyStatus = historyStatus;
    }

    /**
     * ��ȡ��״̬
     *
     * @return NEW_STATUS - ��״̬
     */
    public Integer getNewStatus() {
        return newStatus;
    }

    /**
     * ������״̬
     *
     * @param newStatus ��״̬
     */
    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }
}