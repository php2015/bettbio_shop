package com.salesmanager.core.business.merchant.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MERCHANT_RANK", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class MerchantRank extends SalesManagerEntity<Integer, MerchantRank> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3786127652573709701L;
	@Id
	@Column(name = "RANK_ID", unique=true, nullable=false)
	private Integer id;
	
	public MerchantRank() {
		
	}
	
	@NotEmpty
	@Column(name="RANK_NAME", unique=true)
	private String rankName;
	
	@NotEmpty
	@Column(name="RANK_NAME_ZH", unique=true)
	private String rankChineseName;
	
	public String getRankChineseName() {
		return rankChineseName;
	}

	public void setRankChineseName(String rankChineseName) {
		this.rankChineseName = rankChineseName;
	}

	@NotEmpty
	@Column(name="NUM_DIAMOND_PRODUCT", unique=false)
	private int diamondProductNumber;
	
	public MerchantRank(String name) {
		this.rankName = name;
	}
	
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
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public int getDiamondProductNumber() {
		return diamondProductNumber;
	}

	public void setDiamondProductNumber(int diamondProductNumber) {
		this.diamondProductNumber = diamondProductNumber;
	}

	
}