package com.salesmanager.core.business.catalog.product.model.certificate;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "PRODUCT_CERTIFICATE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ProductCertificate extends SalesManagerEntity<Long, ProductCertificate> implements Auditable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5883212471299350848L;

	@Id
	@Column(name = "PRODUCT_CERTIFICATE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_CERT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	

	@Column(name="TITLE", length=1000)
	private String title;
	
	@Column(name="DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	@Column(name = "CERTIFICATE_IMAGE")
	private String certificateImage;
	
	@Column(name="SORT_ORDER")
	private Integer order = new Integer(0);
	
	@ManyToOne(targetEntity = BasedataType.class)
	@JoinColumn(name = "BASEDATATYPE_ID", nullable = false)
	private BasedataType basedataType;
	
	
	@Column(name="BASEINFO", length=255)
	private String baseinfo; //基本信息，记录文献年份、页码等信息

	@Column(name="DOC_URL", length=1000)
	private String docUrl; //
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "productCertificate")
	private List<ProductLiteraturePath> productListeraturePath = new ArrayList<ProductLiteraturePath>();
	
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

	public String getCertificateImage() {
		return certificateImage;
	}

	public void setCertificateImage(String certificateImage) {
		this.certificateImage = certificateImage;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getBaseinfo() {
		return baseinfo;
	}

	public void setBaseinfo(String baseinfo) {
		this.baseinfo = baseinfo;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
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

	public List<ProductLiteraturePath> getProductListeraturePath() {
		return productListeraturePath;
	}

	public void setProductListeraturePath(
			List<ProductLiteraturePath> productListeraturePath) {
		this.productListeraturePath = productListeraturePath;
	}

	
}
