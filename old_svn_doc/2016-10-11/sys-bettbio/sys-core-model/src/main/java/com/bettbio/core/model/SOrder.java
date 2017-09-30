package com.bettbio.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "S_ORDER")
public class SOrder implements Serializable {
    /**
     * ���
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * �������
     */
    @Column(name = "CODE")
    private String code;

    /**
     * �û����
     */
    @Column(name = "USER_CODE")
    private String userCode;

    /**
     * ����ʱ��
     */
    @Column(name = "CREATE_DATE")
    private Date createDate=new Date();

    /**
     * �ջ�������
     */
    @Column(name = "USER_NAME")
    private String userName;

    /**
     * �ջ����ֻ�
     */
    @Column(name = "USER_PHONE")
    private String userPhone;

    /**
     * �ջ��˵�ַ
     */
    @Column(name = "USER_ADDRESS")
    private String userAddress;
    
    /**
     * 公司名称
     */
    @Column(name = "CORPORATE_NAME")
    private String corporateName;
    
    /**
     * 邮编
     */
	@Column(name = "POSTCODE")
	private Integer postcode;
    
    /**
     * �����̼Ҷ���
     */
    @Column(name = "JOIN_ORDER")
    private String joinOrder;
    
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
     * 公司注册名称
     */
    @Column(name="COMPANY_REGISTERED_NAME")
    private String companyRegisteredName;

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
     * 总金额
     */
    @Column(name="TOTAL_AMOUNT")
    private Double totalAmount;
    
    /**
     * 订单状态  已拆单 未拆单
     */
    @Column(name = "ORDER_STATE")
    private String orderState="未拆单";

    private static final long serialVersionUID = 1L;

    /**
     * ��ȡ���
     *
     * @return ID - ���
     */
    public Integer getId() {
        return id;
    }

    /**
     * ���ñ��
     *
     * @param id ���
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ��ȡ�������
     *
     * @return CODE - �������
     */
    public String getCode() {
        return code;
    }

    /**
     * ���ö������
     *
     * @param code �������
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * ��ȡ�û����
     *
     * @return USER_CODE - �û����
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * �����û����
     *
     * @param userCode �û����
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return CREATE_DATE - ����ʱ��
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * ���ô���ʱ��
     *
     * @param createDate ����ʱ��
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * ��ȡ�ջ�������
     *
     * @return USER_NAME - �ջ�������
     */
    public String getUserName() {
        return userName;
    }

    /**
     * �����ջ�������
     *
     * @param userName �ջ�������
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * ��ȡ�ջ����ֻ�
     *
     * @return USER_PHONE - �ջ����ֻ�
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * �����ջ����ֻ�
     *
     * @param userPhone �ջ����ֻ�
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * ��ȡ�ջ��˵�ַ
     *
     * @return USER_ADDRESS - �ջ��˵�ַ
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * �����ջ��˵�ַ
     *
     * @param userAddress �ջ��˵�ַ
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * ��ȡ�����̼Ҷ���
     *
     * @return JOIN_ORDER - �����̼Ҷ���
     */
    public String getJoinOrder() {
        return joinOrder;
    }

    /**
     * ���ù����̼Ҷ���
     *
     * @param joinOrder �����̼Ҷ���
     */
    public void setJoinOrder(String joinOrder) {
        this.joinOrder = joinOrder;
    }

	public String getRise() {
		return rise;
	}

	public void setRise(String rise) {
		this.rise = rise;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getOpenAccountBank() {
		return openAccountBank;
	}

	public void setOpenAccountBank(String openAccountBank) {
		this.openAccountBank = openAccountBank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getRegisteredPhone() {
		return registeredPhone;
	}

	public void setRegisteredPhone(String registeredPhone) {
		this.registeredPhone = registeredPhone;
	}

	public String getIdentificationCode() {
		return identificationCode;
	}

	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getCompanyRegisteredName() {
		return companyRegisteredName;
	}

	public void setCompanyRegisteredName(String companyRegisteredName) {
		this.companyRegisteredName = companyRegisteredName;
	}

	public Integer getPostcode() {
		return postcode;
	}

	public void setPostcode(Integer postcode) {
		this.postcode = postcode;
	}
	
}