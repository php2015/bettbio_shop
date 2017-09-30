package com.salesmanager.core.business.customer.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

/**
 * 
 * @author Lgh
 *商品（兑换礼品）实体类
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "GIFTS", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Gifts extends SalesManagerEntity<Long, Gifts> implements Auditable{
	private static final long serialVersionUID = -9089806661612703247L;

	@Id
	@Column(name = "GIFT_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GIFTS_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="GIFT_NAME", length=100)
	private String name; //商品名称
	
	@Column(name="GIFT_POINTS")
	private Long points; //积分数量
	
	@Column(name="GIFT_PRICE")
	private Long price; //商品价格

	@Column(name="GIFT_PICTURE_SRC")
	private String pictureSrc; //图片路径
	@Column(name="GIFT_TYPE")
	private Long type;//商品类别
	@Column(name="GIFT_TOTAL")
	private Long total;//兑换总数
	@Column(name="GIFT_DEADLINE")
	private Date deadline;//截止时间
	
	
	
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getPictureSrc() {
		return pictureSrc;
	}

	public void setPictureSrc(String pictureSrc) {
		this.pictureSrc = pictureSrc;
	}

	@Override
	public AuditSection getAuditSection() {
		return null;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

}
