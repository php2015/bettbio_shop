package com.salesmanager.core.business.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

import com.salesmanager.core.business.common.model.InvoiceInfo;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.model.payment.CreditCard;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table (name="ORDERS", schema = SchemaConstant.SALESMANAGER_SCHEMA)
public class Order extends SalesManagerEntity<Long, Order> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name ="ORDER_ID" , unique=true , nullable=false )
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "ORDER_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column (name ="ORDER_STATUS")
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="LAST_MODIFIED")
	private Date lastModified;
	
	//the customer object can be detached. An order can exist and the customer deleted
	@Column (name ="CUSTOMER_ID")
	private Long customerId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="DATE_PURCHASED")
	private Date datePurchased;
	
	//used for an order payable on multiple installment
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="ORDER_DATE_FINISHED")
	private Date orderDateFinished;
	
	//What was the exchange rate
	@Column (name ="CURRENCY_VALUE")
	private BigDecimal currencyValue = new BigDecimal(1);//default 1-1
	
	@Column (name ="ORDER_TOTAL")
	private BigDecimal total;

	@Column (name ="IP_ADDRESS")
	private String ipAddress;

	@Column (name ="CHANNEL")
	@Enumerated(value = EnumType.STRING)
	private OrderChannel channel;

	@Column (name ="ORDER_TYPE")
	@Enumerated(value = EnumType.STRING)
	private OrderType orderType = OrderType.ORDER;

	@Column (name ="PAYMENT_TYPE")
	@Enumerated(value = EnumType.STRING)
	private PaymentType paymentType;
	
	@Column (name ="PAYMENT_MODULE_CODE")
	private String paymentModuleCode;
	
	
	@Column (name ="SHIPPING_MODULE_CODE")
	private String shippingModuleCode;
	
	@Column(name="STORE_NAME")
	private String storeName;//商家名称
	
	@Column(name="PROCESS_TYPE")
	private String processType = GlobalConstants.OrderProcessType_ShipFirst;//订单类型。 是先付款，还是先发货
	
	@Embedded
	private CreditCard creditCard = null;

	
	@ManyToOne(targetEntity = Currency.class)
	@JoinColumn(name = "CURRENCY_ID")
	private Currency currency;
	
	@Type(type="locale")  
	@Column (name ="LOCALE")
	private Locale locale; 
	

	@Transient
	private GlobalConstants.OrderPriceType priceType;
	//modify by cy for mutisore
	/**
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANTID")
	private MerchantStore merchant;*/
	
	//@OneToMany(mappedBy = "order")
	//private Set<OrderAccount> orderAccounts = new HashSet<OrderAccount>();
	//modify by cy for mutisore
	/**
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderProduct> orderProducts = new LinkedHashSet<OrderProduct>();
	*/
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<SubOrder> subOrders = new LinkedHashSet<SubOrder>();
	//直接使用order里面的total
	/**
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@OrderBy(clause = "sort_order asc")
	private Set<OrderTotal> orderTotal = new LinkedHashSet<OrderTotal>();
	*/
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@OrderBy(clause = "ORDER_STATUS_HISTORY_ID asc")
	private Set<OrderStatusHistory> orderHistory = new LinkedHashSet<OrderStatusHistory>();
	
	public Order() {
	}
	
	@Column (name ="CUSTOMER_EMAIL_ADDRESS", length=50, nullable=false)
	private String customerEmailAddress;


	@Override
	public Long getId() {
		return id;
	}
	
	@Override
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
		return CloneUtils.clone(lastModified);
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = CloneUtils.clone(lastModified);
	}

	public Date getDatePurchased() {
		return CloneUtils.clone(datePurchased);
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = CloneUtils.clone(datePurchased);
	}

	public Date getOrderDateFinished() {
		return CloneUtils.clone(orderDateFinished);
	}

	public void setOrderDateFinished(Date orderDateFinished) {
		this.orderDateFinished = CloneUtils.clone(orderDateFinished);
	}

	public BigDecimal getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(BigDecimal currencyValue) {
		this.currencyValue = currencyValue;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}


	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	public String getPaymentModuleCode() {
		return paymentModuleCode;
	}

	public void setPaymentModuleCode(String paymentModuleCode) {
		this.paymentModuleCode = paymentModuleCode;
	}



	public String getShippingModuleCode() {
		return shippingModuleCode;
	}

	public void setShippingModuleCode(String shippingModuleCode) {
		this.shippingModuleCode = shippingModuleCode;
	}



	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
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

	public Set<OrderTotal> getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Set<OrderTotal> orderTotal) {
		this.orderTotal = orderTotal;
	}
*/
	public Set<OrderStatusHistory> getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(Set<OrderStatusHistory> orderHistory) {
		this.orderHistory = orderHistory;
	}
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	

	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}


	public void setChannel(OrderChannel channel) {
		this.channel = channel;
	}


	public OrderChannel getChannel() {
		return channel;
	}


	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	public CreditCard getCreditCard() {
		return creditCard;
	}


	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}


	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	@Column(name="BILLING_USERNAME",length=255)
	private String billingUserName;
	
	@Column(name="BILLING_COMPANY", length=500)
	private String billingCompany;
	
	@Column (name ="BILLING_STREET_ADDRESS", length=256)
	private String billingAddress;	
	
	@Column (name ="BILLING_CITY", length=100)
	private String billingCity;
	
	@Column (name ="BILLING_POSTCODE", length=20)
	private String billingPostalCode;
	
	@Column(name="BILLING_TELEPHONE", length=32)
	private String billingTelephone;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Zone.class)
	@JoinColumn(name="BILLING_ZONE_ID", nullable=true)
	private Zone billingZone;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
	@JoinColumn(name="BILLING_COUNTRY_ID", nullable=false)
	private Country billingCountry;
	
	@Column(name="INVOICE_USERNAME",length=255)
	private String ivoiceUserName;
	
	@Column(name="INVOICE_COMPANY", length=500)
	private String ivoiceCompany;
	
	@Column (name ="INVOICE_STREET_ADDRESS", length=256)
	private String ivoiceAddress;	
	
	@Column (name ="INVOICE_CITY", length=100)
	private String ivoiceCity;
	
	@Column (name ="INVOICE_POSTCODE", length=20)
	private String ivoicePostalCode;
	
	@Column(name="INVOICE_TELEPHONE", length=32)
	private String ivoiceTelephone;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Zone.class)
	@JoinColumn(name="INVOICE_ZONE_ID", nullable=true)
	private Zone ivoiceZone;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
	@JoinColumn(name="INVOICE_COUNTRY_ID", nullable=false)
	private Country ivoiceCountry;

	/**
	 * billing
	 */
	@Embedded
	private InvoiceInfo billingInvoice = null;

	public String getBillingUserName() {
		return billingUserName;
	}

	public void setBillingUserName(String billingUserName) {
		this.billingUserName = billingUserName;
	}

	public String getBillingCompany() {
		return billingCompany;
	}

	public void setBillingCompany(String billingCompany) {
		this.billingCompany = billingCompany;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingPostalCode() {
		return billingPostalCode;
	}

	public void setBillingPostalCode(String billingPostalCode) {
		this.billingPostalCode = billingPostalCode;
	}

	public String getBillingTelephone() {
		return billingTelephone;
	}

	public void setBillingTelephone(String billingTelephone) {
		this.billingTelephone = billingTelephone;
	}

	public Zone getBillingZone() {
		return billingZone;
	}

	public void setBillingZone(Zone billingZone) {
		this.billingZone = billingZone;
	}

	public Country getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(Country billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getIvoiceUserName() {
		return ivoiceUserName;
	}

	public void setIvoiceUserName(String ivoiceUserName) {
		this.ivoiceUserName = ivoiceUserName;
	}

	public String getIvoiceCompany() {
		return ivoiceCompany;
	}

	public void setIvoiceCompany(String ivoiceCompany) {
		this.ivoiceCompany = ivoiceCompany;
	}

	public String getIvoiceAddress() {
		return ivoiceAddress;
	}

	public void setIvoiceAddress(String ivoiceAddress) {
		this.ivoiceAddress = ivoiceAddress;
	}

	public String getIvoiceCity() {
		return ivoiceCity;
	}

	public void setIvoiceCity(String ivoiceCity) {
		this.ivoiceCity = ivoiceCity;
	}

	public String getIvoicePostalCode() {
		return ivoicePostalCode;
	}

	public void setIvoicePostalCode(String ivoicePostalCode) {
		this.ivoicePostalCode = ivoicePostalCode;
	}

	public String getIvoiceTelephone() {
		return ivoiceTelephone;
	}

	public void setIvoiceTelephone(String ivoiceTelephone) {
		this.ivoiceTelephone = ivoiceTelephone;
	}

	public Zone getIvoiceZone() {
		return ivoiceZone;
	}

	public void setIvoiceZone(Zone ivoiceZone) {
		this.ivoiceZone = ivoiceZone;
	}

	public Country getIvoiceCountry() {
		return ivoiceCountry;
	}

	public void setIvoiceCountry(Country ivoiceCountry) {
		this.ivoiceCountry = ivoiceCountry;
	}

	public InvoiceInfo getBillingInvoice() {
		return billingInvoice;
	}

	public void setBillingInvoice(InvoiceInfo billingInvoice) {
		this.billingInvoice = billingInvoice;
	}

	public Set<SubOrder> getSubOrders() {
		return subOrders;
	}

	public void setSubOrders(Set<SubOrder> subOrders) {
		this.subOrders = subOrders;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public GlobalConstants.OrderPriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(GlobalConstants.OrderPriceType priceType) {
		this.priceType = priceType;
	}

	
	
}