package com.salesmanager.core.business.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table (name="SUBORDERS", schema = SchemaConstant.SALESMANAGER_SCHEMA)
public class SubOrder extends SalesManagerEntity<Long, SubOrder>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7701663888302773781L;
	
	@Id
	@Column (name ="SUBORDER_ID" , unique=true , nullable=false )
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "SUBORDER_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column (name ="ORDER_STATUS")
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="LAST_MODIFIED")
	private Date lastModified;
	
	@Column (name ="ORDER_TOTAL")
	private BigDecimal total;
	
	//快递公司code
	@Column (name ="DELIVERY_CODE")
	private String deliveryCode;
	
	//快递单号
	@Column (name ="DELIVERY_NUMBER")
	private String deliveryNumber;
	
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANTID")
	private MerchantStore merchant;
	
	@OneToMany(mappedBy = "subOrder", cascade = CascadeType.ALL)
	private Set<OrderProduct> orderProducts = new LinkedHashSet<OrderProduct>();
	
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	private Order order;
	
	@Column(name="STORE_NAME")
	private String storeName;
	
	@Column(name="PAY_PROOF")
	private String payProof;
	
	@Column (name ="FINAL_TOTAL")
	private BigDecimal finalTotal; // 改过的子订单总价
	
	@Column(name="RELATE_SALES_PHONE")
	private String relatedSalesMobile; // 通知用的手机号
	
	@Column(name="RELATE_SALES_EMAIL") // 通知用的邮箱
	private String relatedSalesEmail;
	
	@Transient
	private BigDecimal NominalTotal;
	
	@Transient
	private GlobalConstants.OrderPriceType priceType;
	
	@Transient
	private List<String> actionList;
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		if (storeName == null || storeName.isEmpty()){
			new Throwable("StoreName was set to empty!").printStackTrace();
		}
		this.storeName = storeName;
	}

	public SubOrder (){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public MerchantStore getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantStore merchant) {
		this.merchant = merchant;
	}

	public Set<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Set<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getDeliveryNumber() {
		return deliveryNumber;
	}

	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}

	public String getPayProof() {
		return payProof;
	}

	public void setPayProof(String payProof) {
		this.payProof = payProof;
	}

	public BigDecimal getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(BigDecimal finalTotal) {
		this.finalTotal = finalTotal;
	}

	public BigDecimal getNominalTotal() {
		return NominalTotal;
	}

	public void setNominalTotal(BigDecimal nominalTotal) {
		NominalTotal = nominalTotal;
	}

	public GlobalConstants.OrderPriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(GlobalConstants.OrderPriceType priceType) {
		this.priceType = priceType;
	}

	public List<String> getActionList() {
		return actionList;
	}

	public void setActionList(List<String> actionList) {
		this.actionList = actionList;
	}

	public String getRelatedSalesMobile() {
		return relatedSalesMobile;
	}

	public void setRelatedSalesMobile(String realtedSalesMobile) {
		this.relatedSalesMobile = realtedSalesMobile;
	}

	public String getRelatedSalesEmail() {
		return relatedSalesEmail;
	}

	public void setRelatedSalesEmail(String realtedSalesEmail) {
		this.relatedSalesEmail = realtedSalesEmail;
	}

	
	
}
