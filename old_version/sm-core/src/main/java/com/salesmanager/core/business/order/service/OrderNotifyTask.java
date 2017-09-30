package com.salesmanager.core.business.order.service;

import java.util.Map;

public class OrderNotifyTask {
	private String emailTemplateName;
	private String smsTemplateId;
	private String receiverPhone;
	private String[] smsParameters;
	private String emailTitle;
	private String receiverEmail;
	private Map<String, Object> emailParameters;
	private String emailSenderName;
	private String emailSenderAddress;
	
	public String getEmailTemplateName() {
		return emailTemplateName;
	}
	public void setEmailTemplateName(String emailTemplateName) {
		this.emailTemplateName = emailTemplateName;
	}
	public String getSmsTemplateId() {
		return smsTemplateId;
	}
	public void setSmsTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String[] getSmsParameters() {
		return smsParameters;
	}
	public void setSmsParameters(String[] smsParameters) {
		this.smsParameters = smsParameters;
	}
	public String getEmailTitle() {
		return emailTitle;
	}
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}
	public String getReceiverEmail() {
		return receiverEmail;
	}
	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	public Map<String, Object> getEmailParameters() {
		return emailParameters;
	}
	public void setEmailParameters(Map<String, Object> emailParameters) {
		this.emailParameters = emailParameters;
	}
	public String getEmailSenderName() {
		return emailSenderName;
	}
	public void setEmailSenderName(String emailSenderName) {
		this.emailSenderName = emailSenderName;
	}
	public String getEmailSenderAddress() {
		return emailSenderAddress;
	}
	public void setEmailSenderAddress(String emailSenderAddress) {
		this.emailSenderAddress = emailSenderAddress;
	}
	
	
}
