package com.bettbio.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "S_USER")
public class SUser{
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 登陆账号
     */
    @Column(name = "ACCOUNT")
    private String account;

    /**
     * 登陆密码
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * 联系电话
     */
    @Column(name = "PHONE")
    private String phone;

    /**
     * 邮箱地址
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 姓名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 所属课题组/实验室
     */
    @Column(name = "MECHANISM_SUB_CODE")
    private String mechanismSubCode;

    /**
     * QQ
     */
    @Column(name = "QQ")
    private String qq;

    /**
     * 积分
     */
    @Column(name = "INTEGRAL")
    private Integer integral;

    /**
     * 等级
     */
    @Column(name = "GRADE")
    private Integer grade;

    /**
     * 用户编号
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate=new Date();

    /**
     * 0删除 1正常
     */
    @Column(name = "IS_DELETED")
    private Integer isDeleted=1;

    /**
     * 确认激活
     */
    @Column(name = "IS_ACTIVATION")
    private Integer isActivation=0;

    /**
     * 0女1男
     */
    @Column(name = "SEX")
    private Integer sex;

    /**
     * 0禁用 1正常 默认1
     */
    @Column(name = "IS_DISABLE")
    private Integer isDisable=1;

    /**
     * 来源0网站 1微信公众号 2 手机APP
     */
    @Column(name = "SOURCE")
    private Integer source=0;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate=new Date();

    /**
     * 公司/学校
     */
    @Column(name = "COMPANY")
    private String company;
    /**
     * 手机验证码
     */
    @Column(name="PHONE_CODE")
    private String phoneCode;
    
    /**
     * 余额
     */
    @Column(name="BALANCE")
    private Double balance = 0.0;
    
    /**
     * 总额
     */
    @Column(name="TOTAL")
    private Double total=0.0;
    
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
     * 获取登陆账号
     *
     * @return ACCOUNT - 登陆账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置登陆账号
     *
     * @param account 登陆账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取登陆密码
     *
     * @return PASSWORD - 登陆密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置登陆密码
     *
     * @param password 登陆密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取联系电话
     *
     * @return PHONE - 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取邮箱地址
     *
     * @return EMAIL - 邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱地址
     *
     * @param email 邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取姓名
     *
     * @return NAME - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取所属课题组/实验室
     *
     * @return MECHANISM_SUB_CODE - 所属课题组/实验室
     */
    public String getMechanismSubCode() {
        return mechanismSubCode;
    }

    /**
     * 设置所属课题组/实验室
     *
     * @param mechanismSubCode 所属课题组/实验室
     */
    public void setMechanismSubCode(String mechanismSubCode) {
        this.mechanismSubCode = mechanismSubCode;
    }

    /**
     * 获取QQ
     *
     * @return QQ - QQ
     */
    public String getQq() {
        return qq;
    }

    /**
     * 设置QQ
     *
     * @param qq QQ
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * 获取积分
     *
     * @return INTEGRAL - 积分
     */
    public Integer getIntegral() {
        return integral;
    }

    /**
     * 设置积分
     *
     * @param integral 积分
     */
    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    /**
     * 获取等级
     *
     * @return GRADE - 等级
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置等级
     *
     * @param grade 等级
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 获取用户编号
     *
     * @return CODE - 用户编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置用户编号
     *
     * @param code 用户编号
     */
    public void setCode(String code) {
        this.code = code;
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
     * 获取0删除 1正常
     *
     * @return IS_DELETED - 0删除 1正常
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置0删除 1正常
     *
     * @param isDeleted 0删除 1正常
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取确认激活
     *
     * @return IS_ACTIVATION - 确认激活
     */
    public Integer getIsActivation() {
        return isActivation;
    }

    /**
     * 设置确认激活
     *
     * @param isActivation 确认激活
     */
    public void setIsActivation(Integer isActivation) {
        this.isActivation = isActivation;
    }

    /**
     * 获取0女1男
     *
     * @return SEX - 0女1男
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置0女1男
     *
     * @param sex 0女1男
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取0禁用 1正常 默认1
     *
     * @return IS_DISABLE - 0禁用 1正常 默认1
     */
    public Integer getIsDisable() {
        return isDisable;
    }

    /**
     * 设置0禁用 1正常 默认1
     *
     * @param isDisable 0禁用 1正常 默认1
     */
    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    /**
     * 获取来源0网站 1微信公众号 2 手机APP
     *
     * @return SOURCE - 来源0网站 1微信公众号 2 手机APP
     */
    public Integer getSource() {
        return source;
    }

    /**
     * 设置来源0网站 1微信公众号 2 手机APP
     *
     * @param source 来源0网站 1微信公众号 2 手机APP
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATE_DATE - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取公司/学校
     *
     * @return COMPANY - 公司/学校
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置公司/学校
     *
     * @param company 公司/学校
     */
    public void setCompany(String company) {
        this.company = company;
    }

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
    
}