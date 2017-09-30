package com.salesmanager.core.business.catalog.product.model.thirdproof;

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
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PRODUCT_THIRDPROOF", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ProductThirdproof extends SalesManagerEntity<Long, ProductThirdproof> implements Auditable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5464916655928460674L;

	/**
	 * 
	 */

	@Id
	@Column(name = "PRODUCT_THIRDPROOF_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_THIRDPROOF_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="TITLE", length=100)
	private String title;
	
	@Column(name="DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	@Column(name = "THIRDPROOF_IMAGE")
	private String thirdproofImage;
	
	@Column(name="SORT_ORDER")
	private Integer order = new Integer(0);
	
	@ManyToOne(targetEntity = BasedataType.class)
	@JoinColumn(name = "BASEDATATYPE_ID", nullable = false)
	private BasedataType basedataType;
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
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

	public String getThirdproofImage() {
		return thirdproofImage;
	}

	public void setThirdproofImage(String thirdproofImage) {
		this.thirdproofImage = thirdproofImage;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public BasedataType getBasedataType() {
		return basedataType;
	}

	public void setBasedataType(BasedataType basedataType) {
		this.basedataType = basedataType;
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

	
}
