package com.salesmanager.core.business.catalog.product.model.certificate;

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

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PRODUCT_LITERATURE_PATH", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ProductLiteraturePath extends SalesManagerEntity<Long, ProductLiteraturePath> implements Auditable{

	@Id
	@Column(name = "PRODUCT_LITERATURE_PATH_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_CERT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	

	@Column(name="PATH", length=3000)
	private String path;
	
	@ManyToOne(targetEntity = ProductCertificate.class)
	@JoinColumn(name = "PRODUCT_CERTIFICATE_ID", nullable = false)
	private ProductCertificate productCertificate;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ProductCertificate getProductCertificate() {
		return productCertificate;
	}

	public void setProductCertificate(ProductCertificate productCertificate) {
		this.productCertificate = productCertificate;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	
	

}
