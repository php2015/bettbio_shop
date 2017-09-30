package com.salesmanager.core.business.order.model.orderproduct;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table (name="ORDER_PRODUCT" , schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class OrderProduct extends SalesManagerEntity<Long, OrderProduct> {
	private static final long serialVersionUID = 176131742783954627L;
	
	@Id
	@Column (name="ORDER_PRODUCT_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_PRODUCT_ID_NEXT_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column (name="PRODUCT_SKU")
	private String sku;
	
	@Column (name="PRODUCT_ID")
	private Long productID;
	
	@Column (name="PRODUCT_NAME" , length=120 , nullable=false)
	private String productName;
	
	@Column (name="PRODUCT_ENNAME" , length=512)
	private String productEnName;
	
	/**
	@Column (name="PRODUCT_STORE" ,nullable=false)
	private int storeID;
*/
	@Column (name="PRODUCT_QUANTITY")
	private int productQuantity=0;

	@Column (name="ONETIME_CHARGE" , nullable=false )
	private BigDecimal oneTimeCharge;

	@Column (name="PRICE" , nullable=false )
	private BigDecimal price;
	
	@Column (name="SPECS" , nullable=false )
	private String specifications;
	
	@OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
	private Set<OrderProductAttribute> orderAttributes = new HashSet<OrderProductAttribute>();

	@OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
	private Set<OrderProductPrice> prices = new HashSet<OrderProductPrice>();

	//@OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
	//private Set<OrderProductDownload> downloads = new HashSet<OrderProductDownload>();
	
	@Column(name = "QUALITY_SCORE")
	private Integer qualityScore;
	
	@ManyToOne(targetEntity = SubOrder.class)
	@JoinColumn(name = "SUBORDER_ID", nullable = false)
	private SubOrder subOrder;
	
	@OneToOne(mappedBy="orderProduct")
	private ProductReview productReview;
	
	public SubOrder getSubOrder() {
		return subOrder;
	}

	public void setSubOrder(SubOrder subOrder) {
		this.subOrder = subOrder;
	}

	public OrderProduct() {
	}
	
	public OrderProduct(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	

	public Set<OrderProductAttribute> getOrderAttributes() {
		return orderAttributes;
	}

	public void setOrderAttributes(Set<OrderProductAttribute> orderAttributes) {
		this.orderAttributes = orderAttributes;
	}

	public Set<OrderProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(Set<OrderProductPrice> prices) {
		this.prices = prices;
	}
/**
	public Set<OrderProductDownload> getDownloads() {
		return downloads;
	}

	public void setDownloads(Set<OrderProductDownload> downloads) {
		this.downloads = downloads;
	}*/


	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSku() {
		return sku;
	}

	public void setOneTimeCharge(BigDecimal oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}

	public BigDecimal getOneTimeCharge() {
		return oneTimeCharge;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public ProductReview getProductReview() {
		return productReview;
	}

	public void setProductReview(ProductReview productReview) {
		this.productReview = productReview;
	}

	public String getProductEnName() {
		return productEnName;
	}

	public void setProductEnName(String productEnName) {
		this.productEnName = productEnName;
	}

	public Integer getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(Integer qualityScore) {
		this.qualityScore = qualityScore;
	}
	
	
}
