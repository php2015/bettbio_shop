package com.salesmanager.core.business.merchant.authorization.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name = "AUTHORIZATION", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Authorization extends SalesManagerEntity<Long, Authorization> {
	private static final long serialVersionUID = 7671103335743647645L;
	
	@Id
	@Column(name = "AUTH_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "AUTH_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "COMPANY",  length=255)
	private String company;
	
	
	@Column(name = "IMAGE", nullable=false,length=255)
	private String image;
	
	@Column(name = "AUTH_TYPE", length=2)
	private int auth_type;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "START_TIME")
	private Date startTime;

	@Temporal(TemporalType.DATE)
	@Column(name = "END_TIME")
	private Date endTime;
	
	@Column(name = "INTRODUCE", length=1000)
	private String introduce;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="MERCHANT_ID", nullable=false)
	private MerchantStore merchantStore;

	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "AUTH_BRAND", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "AUTH_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "BRAND_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Manufacturer> manufacturer = new HashSet<Manufacturer>();
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getAuth_type() {
		return auth_type;
	}

	public void setAuth_type(int auth_type) {
		this.auth_type = auth_type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public MerchantStore getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(MerchantStore merchantStore) {
		this.merchantStore = merchantStore;
	}

	public Set<Manufacturer> getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Set<Manufacturer> manufacturer) {
		this.manufacturer = manufacturer;
	}

	
}
