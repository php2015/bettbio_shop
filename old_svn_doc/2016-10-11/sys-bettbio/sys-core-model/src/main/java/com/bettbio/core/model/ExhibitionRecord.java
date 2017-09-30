package com.bettbio.core.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "EXHIBITION_RECORD")
public class ExhibitionRecord implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 展会code
     */
    @Column(name = "EXHIBITION_CODE")
    private String exhibitionCode;

    /**
     * 商家code
     */
    @Column(name = "STORE_CODE")
    private String storeCode;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "IS_AFFIRM")
    private Integer isAffirm;
    
    /**
     * 商家申请表
     */
    @Column(name = "FILE_URL")
    private String fileUrl;
    
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
     * @return EXHIBITION_CODE - 展会code
     */
    public String getExhibitionCode() {
        return exhibitionCode;
    }

    /**
     * 设置展会code
     *
     * @param exhibitionCode 展会code
     */
    public void setExhibitionCode(String exhibitionCode) {
        this.exhibitionCode = exhibitionCode;
    }

    /**
     * 获取商家code
     *
     * @return STORE_CODE - 商家code
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * 设置商家code
     *
     * @param storeCode 商家code
     */
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
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

	public Integer getIsAffirm() {
		return isAffirm;
	}

	public void setIsAffirm(Integer isAffirm) {
		this.isAffirm = isAffirm;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
    
}