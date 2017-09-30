package com.bettbio.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "BASEDATA_TYPE")
public class BasedataType implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
    @Column(name = "PRODUCT_AUDIT")
    private int productAudit;
    
    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;
    
    @Column(name = "UPDT_ID")
    private String updtId;
    
    @Column(name = "CODE")
    private String code;
    
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "SORT_ORDER")
    private int sortOrder;
    
    @Column(name = "TYPE")
    private String type;
    
    @Column(name = "VALUE")
    private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getProductAudit() {
		return productAudit;
	}

	public void setProductAudit(int productAudit) {
		this.productAudit = productAudit;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdtId() {
		return updtId;
	}

	public void setUpdtId(String updtId) {
		this.updtId = updtId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
}
