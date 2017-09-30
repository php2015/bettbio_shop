package com.salesmanager.core.business.customer.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

/**
 * 
 * @author Lgh
 *礼品订单
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "GIFT_ORDER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class GiftOrder extends SalesManagerEntity<Long, GiftOrder> {
	private static final long serialVersionUID = -9089806661612703247L;

	@Id
	@Column(name = "GIFT_ORDER_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GIFT_ORDER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="GIFT_NUMBER")
	private int number; //商品数量
	
	@Column(name="GIFT_NAME")
	private String gifName; //商品数量
	
	@Column(name="GIFT_POINT")
	private int gifPoint; //商品数量
	
	@Column(name="GIFT_IMG")
	private String gifImge; //商品数量
	
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
	private Customer customer;
	
	//收货人城市
	@Column(name="CUSTOMER_CITY")
	private	String customerCity;
	//收货地址
	@Column (name ="SHIPPING_ADDRESS")
	private String shippingAddress; 
	//收货人电话
	@Column (name ="PHONE_NUMBER")
	private String phoneNumber;
	//收货人姓名
	@Column (name ="CUSTOMER_NAME")
	private String customerName;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date createDate;//更新时间
	
	@Column (name ="GIFT_STATUS")
	@Enumerated(value = EnumType.STRING)
	private GiftStatus status;
	
	//快递公司code
	@Column (name ="DELIVERY_CODE")
	private String deliveryCode;
	@Column(name="CUSTOMER_ADDRESS_ID")
	private long customerAddressId;
	
	
	
	//快递单号
	@Column (name ="DELIVERY_NUMBER")
	private String deliveryNumber;
	
	@Column(name="GITF_ID")
	private long gitfid;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

		
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public void setStatus(GiftStatus status) {
		this.status = status;
	}

	public GiftStatus getStatus() {
		return status;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public String getGifName() {
		return gifName;
	}

	public void setGifName(String gifName) {
		this.gifName = gifName;
	}

	public int getGifPoint() {
		return gifPoint;
	}

	public void setGifPoint(int gifPoint) {
		this.gifPoint = gifPoint;
	}

	public String getGifImge() {
		return gifImge;
	}

	public void setGifImge(String gifImge) {
		this.gifImge = gifImge;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String string) {
		this.phoneNumber = string;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getCustomerAddressId() {
		return customerAddressId;
	}

	public void setCustomerAddressId(long customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	public long getGitfid() {
		return gitfid;
	}

	public void setGitfid(long gitfid) {
		this.gitfid = gitfid;
	}
	
}
