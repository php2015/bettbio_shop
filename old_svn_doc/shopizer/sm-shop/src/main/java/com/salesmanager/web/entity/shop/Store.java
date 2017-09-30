package com.salesmanager.web.entity.shop;

import java.io.Serializable;

import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.web.entity.Entity;

public class Store extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 784045518568970156L;
	
	private Long id;
	private String code;
	private String storename;
	private String storeEmailAddress;
	private String continueshoppingurl;
	private String storephone;
	private Zone zone;
	private String storecity;
	private String storeaddress;
	private String storepostalcode;
	private boolean useQQ =false;
	private String qqNum;
	private Boolean seeded = false;
	private String storeLogo;
	private String storecontacts;
	private String storemobile;
	private String storeUrl;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getStoreEmailAddress() {
		return storeEmailAddress;
	}
	public void setStoreEmailAddress(String storeEmailAddress) {
		this.storeEmailAddress = storeEmailAddress;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStorephone() {
		return storephone;
	}
	public void setStorephone(String storephone) {
		this.storephone = storephone;
	}
	public Zone getZone() {
		return zone;
	}
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	public String getStorecity() {
		return storecity;
	}
	public void setStorecity(String storecity) {
		this.storecity = storecity;
	}
	public String getStoreaddress() {
		return storeaddress;
	}
	public void setStoreaddress(String storeaddress) {
		this.storeaddress = storeaddress;
	}
	public String getStorepostalcode() {
		return storepostalcode;
	}
	public void setStorepostalcode(String storepostalcode) {
		this.storepostalcode = storepostalcode;
	}
	public String getContinueshoppingurl() {
		return continueshoppingurl;
	}
	public void setContinueshoppingurl(String continueshoppingurl) {
		this.continueshoppingurl = continueshoppingurl;
	}
	public boolean isUseQQ() {
		return useQQ;
	}
	public void setUseQQ(boolean useQQ) {
		this.useQQ = useQQ;
	}
	public String getQqNum() {
		return qqNum;
	}
	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}
	public Boolean getSeeded() {
		return seeded;
	}
	public void setSeeded(Boolean seeded) {
		this.seeded = seeded;
	}
	public String getStoreLogo() {
		return storeLogo;
	}
	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}
	public String getStorecontacts() {
		return storecontacts;
	}
	public void setStorecontacts(String storecontacts) {
		this.storecontacts = storecontacts;
	}
	public String getStoremobile() {
		return storemobile;
	}
	public void setStoremobile(String storemobile) {
		this.storemobile = storemobile;
	}
	public String getStoreUrl() {
		return storeUrl;
	}
	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}
}
