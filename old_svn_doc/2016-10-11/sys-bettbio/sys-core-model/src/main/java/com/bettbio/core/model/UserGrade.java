package com.bettbio.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分配置表
 * @author GuoChunbo
 *
 */
@Table(name = "USER_GRADE")
public class UserGrade {
	
	/**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * 等级 
     */
    @Column(name = "LV")
    private Integer lv;
    
    /**
     * 类型 0 -用户
     */
    @Column(name = "TYPE")
    private Integer type;

    /**
     * 积分值
     */
    @Column(name = "VALUE")
    private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}
	
	
}
