package com.salesmanager.core.business.customer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name = "CUSTOMER_ADDRESS", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class CustomerAddress extends SalesManagerEntity<Long, CustomerAddress>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3304348665954970363L;
	
	@Id
	@Column(name="CUSTOMER_ADDRESS_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_ADDRESS_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="NAME",length=255)
	private String name; 
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	@Column(name="COMPANY", length=500)
	private String company;
	
	@Column (name ="STREET_ADDRESS", length=256)
	private String address;	
	
	@Column (name ="CITY", length=100)
	private String city;
	
	@Column (name ="POSTCODE", length=20)
	private String postalCode;
	
	@Column(name="TELEPHONE", length=32)
	private String telephone;
	
	@Column (name ="STATE", length=100)
	private String state;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
	private Customer customer;

	@Column (name ="MEMO", length=500)
	private String memo;
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 0:
	 * 1:默认收货地址
	 * 2:默认发票地址
	 * 3:默认收货、发票地址
	 */
	@Column (name ="TYPE")
	private int type=0;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
	@JoinColumn(name="BILLING_COUNTRY_ID", nullable=false)
	private Country country;
	
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Zone.class)
	@JoinColumn(name="BILLING_ZONE_ID", nullable=true)
	private Zone zone;
	
	

}
