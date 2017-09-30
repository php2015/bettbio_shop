package com.salesmanager.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkModeConfiguration {
	protected String workMode="product";
	protected List<String> emailCCAddress;
	protected String emailToAddress;
	protected String mobileNumber;
	protected String domainName;
	protected Map<String, List<String>> emailCarbonCopyMap = new HashMap<String, List<String>>();
	protected boolean sendSms = false;
	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getWorkMode() {
		return workMode;
	}
	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}
	public List<String> getEmailCCAddress() {
		return emailCCAddress;
	}
	public void setEmailCCAddress(List<String> emailCCAddress) {
		this.emailCCAddress = emailCCAddress;
	}
	public String getEmailToAddress() {
		return emailToAddress;
	}
	public void setEmailToAddress(String emailToAddress) {
		this.emailToAddress = emailToAddress;
	}
	public boolean isProductEnv() {
		return getWorkMode().equalsIgnoreCase("product");
	}
	public boolean isSendSms() {
		return sendSms;
	}
	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}
	public Map<String, List<String>> getEmailCarbonCopyMap() {
		return emailCarbonCopyMap;
	}
	public void setEmailCarbonCopyMap(Map<String, List<String>> emailCarbonCopyMap) {
		this.emailCarbonCopyMap = emailCarbonCopyMap;
	}
	
	
	
}
