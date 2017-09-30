package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "INVOICE_INFO")
public class InvoiceInfo {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户编号
     */
    @Column(name = "USER_CODE")
    private String userCode;

    /**
     * 抬头
     */
    @Column(name = "RISE")
    private String rise;

    /**
     * 0普通发票1增值发票
     */
    @Column(name = "INVOICE_TYPE")
    private Integer invoiceType;

    /**
     * 公司名称
     */
    @Column(name = "CORPORATE_NAME")
    private String corporateName;

    /**
     * 注册地址
     */
    @Column(name = "REGISTER_ADDRESS")
    private String registerAddress;

    /**
     * 开户银行
     */
    @Column(name = "OPEN_ACCOUNT_BANK")
    private String openAccountBank;

    /**
     * 银行账户
     */
    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    /**
     * 注册电话
     */
    @Column(name = "REGISTERED_PHONE")
    private String registeredPhone;

    /**
     * 纳税人识别号
     */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 是否默认 0默认地址 1正常地址
     */
    @Column(name = "IS_DEFAULT")
    private Integer isDefault;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 开票编码
     */
    @Column(name = "CODE")
    private String code;

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
     * 获取RISE
     *
     * @return RISE - RISE
     */
    public String getRise() {
        return rise;
    }

    /**
     * 设置RISE
     *
     * @param rise RISE
     */
    public void setRise(String rise) {
        this.rise = rise;
    }

    /**
     * 获取0普通发票1增值发票
     *
     * @return INVOICE_TYPE - 0普通发票1增值发票
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * 设置0普通发票1增值发票
     *
     * @param invoiceType 0普通发票1增值发票
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * 获取公司名称
     *
     * @return CORPORATE_NAME - 公司名称
     */
    public String getCorporateName() {
        return corporateName;
    }

    /**
     * 设置公司名称
     *
     * @param corporateName 公司名称
     */
    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    /**
     * 获取注册地址
     *
     * @return REGISTER_ADDRESS - 注册地址
     */
    public String getRegisterAddress() {
        return registerAddress;
    }

    /**
     * 设置注册地址
     *
     * @param registerAddress 注册地址
     */
    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    /**
     * 获取开户银行
     *
     * @return OPEN_ACCOUNT_BANK - 开户银行
     */
    public String getOpenAccountBank() {
        return openAccountBank;
    }

    /**
     * 设置开户银行
     *
     * @param openAccountBank 开户银行
     */
    public void setOpenAccountBank(String openAccountBank) {
        this.openAccountBank = openAccountBank;
    }

    /**
     * 获取银行账户
     *
     * @return BANK_ACCOUNT - 银行账户
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * 设置银行账户
     *
     * @param bankAccount 银行账户
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * 获取注册电话
     *
     * @return REGISTERED_PHONE - 注册电话
     */
    public String getRegisteredPhone() {
        return registeredPhone;
    }

    /**
     * 设置注册电话
     *
     * @param registeredPhone 注册电话
     */
    public void setRegisteredPhone(String registeredPhone) {
        this.registeredPhone = registeredPhone;
    }

    /**
     * 获取纳税人识别号
     *
     * @return IDENTIFICATION_CODE - 纳税人识别号
     */
    public String getIdentificationCode() {
        return identificationCode;
    }

    /**
     * 设置纳税人识别号
     *
     * @param identificationCode 纳税人识别号
     */
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    /**
     * 获取备注
     *
     * @return REMARKS - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取是否默认 0默认地址 1正常地址
     *
     * @return IS_DEFAULT - 是否默认 0默认地址 1正常地址
     */
    public Integer getIsDefault() {
        return isDefault;
    }

    /**
     * 设置是否默认 0默认地址 1正常地址
     *
     * @param isDefault 是否默认 0默认地址 1正常地址
     */
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
     * 获取开票编码
     *
     * @return CODE - 开票编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置开票编码
     *
     * @param code 开票编码
     */
    public void setCode(String code) {
        this.code = code;
    }
}