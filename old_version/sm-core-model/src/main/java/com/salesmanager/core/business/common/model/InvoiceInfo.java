package com.salesmanager.core.business.common.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InvoiceInfo {
	
	@Column(name="COMPANY", length=500)
	private String company;

	@Column (name ="COMPANY_ADDRESS", length=500)
	private String companyAddress;
	
	@Column (name ="COMPANY_TELEPHONE", length=500)
	private String companyTelephone;
	
	@Column (name ="BANK_NANE", length=500)
	private String bankName;
	
	@Column (name ="BANK_ACCOUNT", length=500)
	private String bankAccount;
	
	@Column (name ="TAX_NUMBER", length=500)
	private String taxpayerNumber;
	
	@Column (name ="type" )
	private int type=0;
	
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyTelephone() {
		return companyTelephone;
	}

	public void setCompanyTelephone(String companyTelephone) {
		this.companyTelephone = companyTelephone;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getTaxpayerNumber() {
		return taxpayerNumber;
	}

	public void setTaxpayerNumber(String taxpayerNumber) {
		this.taxpayerNumber = taxpayerNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
