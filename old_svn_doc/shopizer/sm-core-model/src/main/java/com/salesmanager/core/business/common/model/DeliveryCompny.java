package com.salesmanager.core.business.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table (name="DELIVERYCOMPNY", schema = SchemaConstant.SALESMANAGER_SCHEMA)
public class DeliveryCompny extends SalesManagerEntity<Long, DeliveryCompny>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6737494672470814577L;

	
	
	@Id
	@Column (name ="DELIVERYCOMPNY_ID" , unique=true , nullable=false )
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "DELIVERYCOMPNY_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	//快递公司code
	@Column (name ="DELIVERY_CODE", length=32)
	private String deliveryCode;
	
	//快递公司名称
	@Column (name ="DELIVERY_NAME", length=256)
	private String deliveryName;
	
	//快递公司地址
	@Column (name ="DELIVERY_ADDRESS", length=256)
	private String deliveryAddress;	

	@Column(name="DELIVERY_TELEPHONE", length=32)
	private String deliveryTelephone;
	
	@Column(name="DELIVERY_CONTACTS", length=32)
	private String deliveryContacts;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryTelephone() {
		return deliveryTelephone;
	}

	public void setDeliveryTelephone(String deliveryTelephone) {
		this.deliveryTelephone = deliveryTelephone;
	}

	public String getDeliveryContacts() {
		return deliveryContacts;
	}

	public void setDeliveryContacts(String deliveryContacts) {
		this.deliveryContacts = deliveryContacts;
	}

}
