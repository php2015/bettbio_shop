package com.salesmanager.core.business.catalog.product.model.price;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name = "BRAND_DISCOUNT", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class BrandDiscount extends SalesManagerEntity<Long, BrandDiscount> {

	@Id
	@Column(name = "BRAND_DISCOUNT_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "BRAND_DISCOUNT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	protected Long id;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "BRAND_ID", nullable=false)
	protected long brandId;
	
	@Column(name = "STORE_ID", nullable=false)
	protected long storeId;
	
//	@ElementCollection(targetClass = Double.class)
//	@MapKeyClass(String.class)
//	@OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
//	@JoinTable(name = "BRAND_USERSEGMENT_DISCOUNT", schema=SchemaConstant.SALESMANAGER_SCHEMA, 
//		joinColumns = { 
//			@JoinColumn(name = "BRAND_DISCOUNT_ID", nullable = false, updatable = false)
//		}, 
//		inverseJoinColumns = {
//			@JoinColumn(name = "BU_DISCOUNT_ID", nullable = false, updatable = false) 
//		}
//	)
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "BRAND_USERSEGMENT_DISCOUNT", joinColumns = @JoinColumn(name = "BRAND_DISCOUNT_ID"))
	@Column(name = "DISCOUNTS")
	protected Map<String, Double> discounts = new HashMap<String, Double>();

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public Map<String, Double> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Map<String, Double> discounts) {
		this.discounts = discounts;
	}
	
	
	
}