package com.salesmanager.core.business.merchant.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MERCHANT_RANK_PROFILE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class MerchantRankProfile extends SalesManagerEntity<Long, MerchantRankProfile> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3786127652573709701L;
	@Id
	@Column(name = "MCHTRANKPROFILE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "MCHTPROFILE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	public MerchantRankProfile() {
		
	}
	
	@ManyToOne(targetEntity=MerchantRank.class, fetch = FetchType.EAGER)
	@JoinColumn(name="RANK_ID", nullable=true)
	private MerchantRank parentRank;
	
	
	public MerchantRank getParentRank() {
		return parentRank;
	}

	public void setParentRank(MerchantRank parentRank) {
		this.parentRank = parentRank;
	}

	
	@Column(name="NUM_DIAMOND_PRODUCT", unique=false, nullable=true)
	private Integer diamondProductNumber;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	
	@Override
	public AuditSection getAuditSection() {
		return this.auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
			this.auditSection = audit;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}


	public Integer getDiamondProductNumber() {
		return diamondProductNumber;
	}

	public void setDiamondProductNumber(Integer diamondProductNumber) {
		this.diamondProductNumber = diamondProductNumber;
	}

	
}