package com.salesmanager.core.business.common.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
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

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "BASEDATA_TYPE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class BasedataType extends SalesManagerEntity<Long, BasedataType> implements Auditable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8575330155415602523L;


	/**
	 * 
	 */

	@Id
	@Column(name = "BASEDATA_TYPE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_BASETYPE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	

	@Column(name="CODE", length=100)
	private String code;
	
	@Column(name="NAME", length=255)
	private String name;
	
	@Column(name="SORT_ORDER")
	private Integer order = new Integer(0);
	
	@Column(name="TYPE", length=100)
	private String type; //定义基础数据类型，目前基础数据类型包括文献方、检测方、权威购买方、积分规则
	
	@Column(name="VALUE", length=20)
	private String value; //数值
	
	
	@Embedded
	private AuditSection auditSection = new AuditSection();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
