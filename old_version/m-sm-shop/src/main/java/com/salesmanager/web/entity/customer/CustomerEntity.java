package com.salesmanager.web.entity.customer;

import java.io.Serializable;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;

public class CustomerEntity extends Customer implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	@Email (message="{messages.invalid.email}")
   // @NotEmpty(message="{NotEmpty.customer.emailAddress}")
	private String emailAddress;
	@Valid
	//private Address billing;
	//private Address delivery;
	private String gender;

	private String language;
	//private String Name;
	//private String lastName;
	
	private String encodedPassword = null;
	private String clearPassword = null;
	private String nick;
	//private String hidenick;
	
	private String storeCode;
	
	private String phone;
	
	private String company;
	
	private String project;
	
	private String points; //客户积分
	
	private long addressDefault =-1l;
	
	private long invoiceDefault =-1l;
	
	private long invoiceaddressdefault =-1l;
	
	private int grade = 0;
	
	private int anonymous =0;
	
	//@NotEmpty(message="{NotEmpty.customer.userName}")
	private String userName;
	
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}


	public void setStoreCode(final String storeCode) {
		this.storeCode = storeCode;
	}


	public String getStoreCode() {
		return storeCode;
	}


	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}
	

	public String getEmailAddress() {
		return emailAddress;
	}


	public void setLanguage(final String language) {
		this.language = language;
	}
	public String getLanguage() {
		return language;
	}
	
	public void setGender(final String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return gender;
	}


	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public String getClearPassword() {
		return clearPassword;
	}

	public void setClearPassword(String clearPassword) {
		this.clearPassword = clearPassword;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public long getAddressDefault() {
		return addressDefault;
	}

	public void setAddressDefault(long addressDefault) {
		this.addressDefault = addressDefault;
	}

	public long getInvoiceDefault() {
		return invoiceDefault;
	}

	public void setInvoiceDefault(long invoiceDefault) {
		this.invoiceDefault = invoiceDefault;
	}

	public long getInvoiceaddressdefault() {
		return invoiceaddressdefault;
	}

	public void setInvoiceaddressdefault(long invoiceaddressdefault) {
		this.invoiceaddressdefault = invoiceaddressdefault;
	}

	public int getGrade() {
		return grade;
	}

	public int getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(int anonymous) {
		this.anonymous = anonymous;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getHidenick() {
		if (StringUtils.isBlank(nick)) {
			return "";
		} else {
			return nick.substring(0,1)+"**";
		}
	}

	
	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

}
