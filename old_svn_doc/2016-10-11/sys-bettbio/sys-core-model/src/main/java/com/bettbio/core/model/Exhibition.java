package com.bettbio.core.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "EXHIBITION")
public class Exhibition implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 展会code
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 展会标题
     */
    @Column(name = "TITLE")
    private String title;

    /**
     * 展会内容
     */
    @Column(name = "CONTENT")
    private String content;

    /**
     * 海报图片
     */
    @Column(name = "POSTER_IMG")
    private String posterImg;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 展会开始时间
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @Column(name = "BEGIN_DATE")
    private Date beginDate;

    /**
     * 展会结束时间
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @Column(name = "END_DATE")
    private Date endDate;

    /**
     * 展会时间
     */
    @Column(name = "STATUS")
    private Integer status;

    /**
     * 微信url地址
     */
    @Column(name = "WECHAT_URL")
    private String wechatUrl;
    
    private static final long serialVersionUID = 1L;

    /**
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取展会code
     *
     * @return CODE - 展会code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置展会code
     *
     * @param code 展会code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取展会标题
     *
     * @return TITLE - 展会标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置展会标题
     *
     * @param title 展会标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取展会内容
     *
     * @return CONTENT - 展会内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置展会内容
     *
     * @param content 展会内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取海报图片
     *
     * @return POSTER_IMG - 海报图片
     */
    public String getPosterImg() {
        return posterImg;
    }

    /**
     * 设置海报图片
     *
     * @param posterImg 海报图片
     */
    public void setPosterImg(String posterImg) {
        this.posterImg = posterImg;
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
     * 获取展会开始时间
     *
     * @return BEGIN_DATE - 展会开始时间
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * 设置展会开始时间
     *
     * @param beginDate 展会开始时间
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * 获取商家结束时间
     *
     * @return END_DATE - 商家结束时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置商家结束时间
     *
     * @param endDate 商家结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取展会时间
     *
     * @return STATUS - 展会时间
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置展会时间
     *
     * @param status 展会时间
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getWechatUrl() {
		return wechatUrl;
	}

	public void setWechatUrl(String wechatUrl) {
		this.wechatUrl = wechatUrl;
	}
    
}