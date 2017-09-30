package com.salesmanager.core.business.catalog.product.model.manufacturer;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MANUFACTURER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Manufacturer extends SalesManagerEntity<Long, Manufacturer> implements Auditable {
	private static final long serialVersionUID = 80693964563570099L;
	
	@Id
	@Column(name = "MANUFACTURER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MANUFACT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	private Set<ManufacturerDescription> descriptions = new HashSet<ManufacturerDescription>();
	
	@Column(name = "MANUFACTURER_IMAGE")
	private String image;
	
	@Column(name="SORT_ORDER")
	private Integer order = new Integer(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MERCHANT_ID", referencedColumnName="MERCHANT_ID", nullable=true)
	private MerchantStore merchantStore; //全部的品牌都归属于DEFAULT商铺
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "AUTH_BRAND", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "BRAND_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "AUTH_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Authorization> authorization = new HashSet<Authorization>();
	//@Column(name = "SRC_MERCHANT_ID", nullable=true)
	//private Long srcMerchantStoreId; //记录创建该品牌的商铺ID

	public Set<Authorization> getAuthorization() {
		return authorization;
	}

	public void setAuthorization(Set<Authorization> authorization) {
		this.authorization = authorization;
	}

	public Manufacturer() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}
	
	@Override
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Set<ManufacturerDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ManufacturerDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public ManufacturerDescription getDescription(){
		if(descriptions!=null && descriptions.size()>0) {
			return descriptions.iterator().next();
		}
		
		return null;
	}


	public MerchantStore getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(MerchantStore merchantStore) {
		this.merchantStore = merchantStore;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getOrder() {
		return order;
	}
/**
	public Long getSrcMerchantStoreId() {
		return srcMerchantStoreId;
	}

	public void setSrcMerchantStoreId(Long srcMerchantStoreId) {
		this.srcMerchantStoreId = srcMerchantStoreId;
	}
*/

}
