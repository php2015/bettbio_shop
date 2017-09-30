package com.bettbio.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "S_STORE_USER")
public class SStoreUser extends BaseUser{
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
     * 姓名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * QQ
     */
    @Column(name = "QQ")
    private String qq;

    /**
     * 用户编号
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 0删除 1正常
     */
    @Column(name = "IS_DELETED")
    private Integer isDeleted=1;

    /**
     * 父级账号编号
     */
    @Column(name = "PARENT_CODE")
    private String parentCode;


    /**
     * 0女1男
     */
    @Column(name = "SEX")
    private Integer sex;

    /**
     * 商铺编号
     */
    @Column(name = "STORE_CODE")
    private String storeCode;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate;


    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate=new Date();

    /**
     * 角色集合
     */
    @Column(name = "ROLE_IDS")
    private String roleIds;

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
     * 获取父级账号编号
     *
     * @return PARENT_CODE - 父级账号编号
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置父级账号编号
     *
     * @param parentCode 父级账号编号
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
     * 获取商铺编号
     *
     * @return STORE_CODE - 商铺编号
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * 设置商铺编号
     *
     * @param storeCode 商铺编号
     */
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
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
     * 获取角色集合
     *
     * @return ROLE_IDS - 角色集合
     */
    public String getRoleIds() {
        return roleIds;
    }

    /**
     * 设置角色集合
     *
     * @param roleIds 角色集合
     */
    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
}