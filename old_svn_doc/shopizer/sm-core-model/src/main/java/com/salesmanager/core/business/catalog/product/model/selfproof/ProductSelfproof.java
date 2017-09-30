package com.salesmanager.core.business.catalog.product.model.selfproof;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PRODUCT_SELFPROOF", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ProductSelfproof extends SalesManagerEntity<Long, ProductSelfproof> implements Auditable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8731945357108820632L;

	@Id
	@Column(name = "PRODUCT_SELFPROOF_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_SELFPROOF_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="TITLE", length=100)
	private String title;
	
	@Column(name="DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	@Column(name = "SELFPROOF_IMAGE")
	private String selfproofImage;
	
	@Column(name="SORT_ORDER")
	private Integer order = new Integer(0);
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
	@Column(name = "SELFPROOF_TYPE")
	private String selfproofType;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSelfproofImage() {
		return selfproofImage;
	}

	public void setSelfproofImage(String selfproofImage) {
		this.selfproofImage = selfproofImage;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public String getSelfproofType() {
		return selfproofType;
	}

	public void setSelfproofType(String selfproofType) {
		this.selfproofType = selfproofType;
	}
	

		
}
