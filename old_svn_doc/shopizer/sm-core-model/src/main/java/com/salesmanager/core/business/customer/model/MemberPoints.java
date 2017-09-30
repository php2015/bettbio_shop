package com.salesmanager.core.business.customer.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

/**
 * 
 * @author Lgh
 *用户积分实体类
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MEMBER_POINTS", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class MemberPoints extends SalesManagerEntity<Long, MemberPoints>{
	private static final long serialVersionUID = -9089806661612703247L;

	@Id
	@Column(name = "MEMBER_POINTS_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MEMBERPOINTS_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="TYPE", length=100)
	private String type; //定义积分类型，当前积分类型有新用户注册、首单
	
	@Column(name="VALUE", length=10)
	private String value; //数值
	
	
	/**
	 * 0：已到账
	 * 1：未到账
	 * 2：已撤销
	 */
	@Column(name="STATAS",length=20)
	private Integer statas; //积分状态
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATEVALID")
	private Date dateValid; //有效时间
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	private Date updateDate;//更新时间
	
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
	private Customer customer;
	
	@Column(name = "ORDER_ID")
	private Long orderId;//对应订单
	
	@Column(name = "LTFE_POINT")
	private Long ltfePoint;//剩余积分
	
	
	
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Date getDateValid() {
		return dateValid;
	}
	public void setDateValid(Date dateValid) {
		this.dateValid = dateValid;
	}
	public Long getLtfePoint() {
		return ltfePoint;
	}
	public void setLtfePoint(Long ltfePoint) {
		this.ltfePoint = ltfePoint;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getStatas() {
		return statas;
	}

	public void setStatas(Integer statas) {
		this.statas = statas;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	

}
