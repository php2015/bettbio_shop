package com.salesmanager.core.business.catalog.product.model;

import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PRODUCT", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Product extends SalesManagerEntity<Long, Product> implements Auditable {
	private static final long serialVersionUID = -6228066416290007047L;

	@Id
	@Column(name = "PRODUCT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
	private Set<ProductDescription> descriptions = new HashSet<ProductDescription>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy="product", orphanRemoval = true)
	private Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
	private Set<ProductAttribute> attributes = new HashSet<ProductAttribute>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
	private Set<ProductImage> images = new HashSet<ProductImage>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
	private Set<ProductCertificate> certificates = new HashSet<ProductCertificate>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
	private Set<ProductProof> proofs = new HashSet<ProductProof>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
	private Set<ProductThirdproof> thirdproofs = new HashSet<ProductThirdproof>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
	private Set<ProductSelfproof> selfProofs = new HashSet<ProductSelfproof>();

	public Set<ProductThirdproof> getThirdproofs() {
		return thirdproofs;
	}

	public void setThirdproofs(Set<ProductThirdproof> thirdproofs) {
		this.thirdproofs = thirdproofs;
	}

	public Set<ProductProof> getProofs() {
		return proofs;
	}

	public void setProofs(Set<ProductProof> proofs) {
		this.proofs = proofs;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
	private Set<ProductRelationship> relationships = new HashSet<ProductRelationship>();
	/**
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="Parcel_ID")
	private Parcel parcel;*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MERCHANT_ID", nullable=false)
	private MerchantStore merchantStore;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PRODUCT_CATEGORY", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PRODUCT_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Category> categories = new HashSet<Category>();
	
	@Column(name="DATE_AVAILABLE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateAvailable = new Date();
	
	
	@Column(name="AVAILABLE")
	private boolean available = true;
	

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinColumn(name="MANUFACTURER_ID", nullable=true)
	private Manufacturer manufacturer;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinColumn(name="PRODUCT_TYPE_ID", nullable=true)
	private ProductType type;

	@Column(name = "PRODUCT_FREE")
	private boolean productIsFree =true; //标识产品是收费产品，免费产品
	
	@Column(name="DATE_ChargeBegin")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateChargeBegin ;
	
	@Column(name="DATE_ChargeEnd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateChargeEnd ;

	@Column(name = "REVIEW_AVG")
	private BigDecimal productReviewAvg;

	@Column(name = "REVIEW_COUNT")
	private Integer productReviewCount;
	
	@Column(name = "QUALITY_SCORE")
	private Integer qualityScore=0;

	@Column(name = "QUANTITY_ORDERED")
	private Integer productOrdered;
	
	
	//@NotEmpty
	//@Pattern(regexp="^[a-zA-Z0-9_]*$")
	@Column(name = "SKU")
	private String sku;
	
	@NotEmpty
	@Column(name = "CODE", length=255)
	private String code; //产品编码，由商户自己统一管理
	//对应仪器的型号
	@Column(name = "BATCHNUM", length=255)
	private String batchnum; //产品批次号
	//对应仪器的产地
	@Column(name = "CAS", length=255)
	private String cas;

	//服务的价格
	@Column(name = "SPRICE", length=255)
	private String serverPrice;
	
	
	//商品同分类一对一，方便页面获取该信息，定义非持久化字段
	@Transient
	private String categoryName;
	
	//多 
	
	@Transient
	private String categoryId;

	public Product() {
	}

	@Override
	public Long getId() {
		return this.id;
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

	public BigDecimal getProductReviewAvg() {
		return productReviewAvg;
	}

	public void setProductReviewAvg(BigDecimal productReviewAvg) {
		this.productReviewAvg = productReviewAvg;
	}

	public Integer getProductReviewCount() {
		return productReviewCount;
	}

	public void setProductReviewCount(Integer productReviewCount) {
		this.productReviewCount = productReviewCount;
	}



	public Integer getProductOrdered() {
		return productOrdered;
	}

	public void setProductOrdered(Integer productOrdered) {
		this.productOrdered = productOrdered;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Set<ProductDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ProductDescription> descriptions) {
		this.descriptions = descriptions;
	}


	public boolean getProductIsFree() {
		return productIsFree;
	}

	public void setProductIsFree(boolean productIsFree) {
		this.productIsFree = productIsFree;
	}



	public Set<ProductAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<ProductAttribute> attributes) {
		this.attributes = attributes;
	}



	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}



	public Set<ProductAvailability> getAvailabilities() {
		return availabilities;
	}

	public void setAvailabilities(Set<ProductAvailability> availabilities) {
		this.availabilities = availabilities;
	}

	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}

	public Set<ProductRelationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(Set<ProductRelationship> relationships) {
		this.relationships = relationships;
	}


	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public MerchantStore getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(MerchantStore merchantStore) {
		this.merchantStore = merchantStore;
	}



	public Date getDateAvailable() {
		return dateAvailable;
	}

	public void setDateAvailable(Date dateAvailable) {
		this.dateAvailable = dateAvailable;
	}


	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isAvailable() {
		return available;
	}
		
	public ProductDescription getProductDescription() {
		if(this.getDescriptions()!=null && this.getDescriptions().size()>0) {
			return this.getDescriptions().iterator().next();
		}
		return null;
	}
	
	public ProductImage getProductImage() {
		ProductImage productImage = null;
		if(this.getImages()!=null && this.getImages().size()>0) {
			for(ProductImage image : this.getImages()) {
				productImage = image;
				if(productImage.isDefaultImage()) {
					break;
				}
			}
		}
		return productImage;
	}

	public Set<ProductCertificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(Set<ProductCertificate> certificates) {
		this.certificates = certificates;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBatchnum() {
		return batchnum;
	}

	public void setBatchnum(String batchnum) {
		this.batchnum = batchnum;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	public Integer getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	public Set<ProductSelfproof> getSelfProofs() {
		return selfProofs;
	}

	public void setSelfProofs(Set<ProductSelfproof> selfProofs) {
		this.selfProofs = selfProofs;
	}

	public Date getDateChargeBegin() {
		return dateChargeBegin;
	}

	public void setDateChargeBegin(Date dateChargeBegin) {
		this.dateChargeBegin = dateChargeBegin;
	}

	public Date getDateChargeEnd() {
		return dateChargeEnd;
	}

	public void setDateChargeEnd(Date dateChargeEnd) {
		this.dateChargeEnd = dateChargeEnd;
	}

	public String getCategoryName() {
		if (StringUtils.isNotBlank(categoryName)) {
			return categoryName;
		} else {
			if(this.getCategories()!=null && this.getCategories().size()>0) {
				CategoryDescription cate_desc = this.getCategories().iterator().next().getDescription();
				if (cate_desc!=null) {
					return cate_desc.getName();
				}
			}
			return "";
		}
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryId() {
		if (StringUtils.isNotBlank(categoryId)) {
			return categoryId;
		} else {
			if(this.getCategories()!=null && this.getCategories().size()>0) {
				Category cate = this.getCategories().iterator().next();
				return cate.getId().toString();
			}
			return "";
		}
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
/**
	public Parcel getParcel() {
		return parcel;
	}

	public void setParcel(Parcel parcel) {
		this.parcel = parcel;
	}
*/

	public String getServerPrice() {
		return serverPrice;
	}

	public void setServerPrice(String serverPrice) {
		this.serverPrice = serverPrice;
	}

	

}
