package com.salesmanager.core.business.customer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name = "USER_SEGMENT", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class UserSegment extends SalesManagerEntity<Long, UserSegment> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8477360226364615021L;

	@Id
	@Column(name = "USER_SEGMENT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "USER_SEGMENT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@NotEmpty
	@Column(name="NAME", length=80)
	private String name;

	@NotEmpty
	@Column(name="DISCRIPTION_ZH", length=2000)
	private String discriptionZh;
	
	@NotEmpty
	@Column(name="DISCRIPTION_EN", length=2000)
	private String discriptionEn;

	@Column(name="FOR_SELLER")
	private boolean forSeller = false;
	
	@Column(name="FOR_BUYER")
	private boolean forBuyer = true;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscriptionZh() {
		return discriptionZh;
	}

	public void setDiscriptionZh(String discriptionZh) {
		this.discriptionZh = discriptionZh;
	}

	public String getDiscriptionEn() {
		return discriptionEn;
	}

	public void setDiscriptionEn(String discriptionEn) {
		this.discriptionEn = discriptionEn;
	}

	public boolean isForSeller() {
		return forSeller;
	}

	public void setForSeller(boolean forSeller) {
		this.forSeller = forSeller;
	}

	public boolean isForBuyer() {
		return forBuyer;
	}

	public void setForBuyer(boolean forBuyer) {
		this.forBuyer = forBuyer;
	}

	
}
