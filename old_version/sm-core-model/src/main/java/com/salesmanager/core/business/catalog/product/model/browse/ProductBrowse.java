package com.salesmanager.core.business.catalog.product.model.browse;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PRODUCT_BROWSW", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ProductBrowse extends SalesManagerEntity<Long, ProductBrowse> {
	private static final long serialVersionUID = -7509351278087554383L;

	@Id
	@Column(name = "PRODUCT_BROWSE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "PRODUCT_BROWSE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BROWSE_DATE")
	private Date browseDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CUSTMOER_ID")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name="PRODUCT_ID")
	private Product product;
	
	@Column(name="IP")
	private String ip;
	
	public Date getBrowseDate() {
		return browseDate;
	}

	public void setBrowseDate(Date browseDate) {
		this.browseDate = browseDate;
	}

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
