package com.salesmanager.core.business.catalog.product.model.proof;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PRODUCT_PROOF", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ProductProof extends SalesManagerEntity<Long, ProductProof> implements Auditable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6151553665328797133L;


	/**
	 * 
	 */

	@Id
	@Column(name = "PRODUCT_PROOF_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_PROOF_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	

	@Column(name="TITLE", length=255)
	private String title;  //说明
	
	@ManyToOne(targetEntity = BasedataType.class)
	@JoinColumn(name = "BASEDATATYPE_ID", nullable = false)
	private BasedataType basedataType;//购买方
	
	@Column(name="DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	@Column(name = "RPOOF_IMAGE")
	private String proofImage;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_BUYED")
	private Date dateBuyed;  //购买时间
	
	@Column(name="SORT_ORDER")
	private Integer order = new Integer(0);
	
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

	public String getProofImage() {
		return proofImage;
	}

	public void setProofImage(String proofImage) {
		this.proofImage = proofImage;
	}

//	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getDateBuyed() {
		return dateBuyed;
	}

	public void setDateBuyed(Date dateBuyed) {
		this.dateBuyed = dateBuyed;
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

	public BasedataType getBasedataType() {
		return basedataType;
	}

	public void setBasedataType(BasedataType basedataType) {
		this.basedataType = basedataType;
	}
	
}
