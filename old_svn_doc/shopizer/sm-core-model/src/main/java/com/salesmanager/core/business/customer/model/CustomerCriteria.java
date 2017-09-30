package com.salesmanager.core.business.customer.model;

import java.util.Date;

import com.salesmanager.core.business.common.model.Criteria;

public class CustomerCriteria extends Criteria {
	
	private Date dateOfBirth;
	private String emailAddress;
	private String nick;
	private String grade;
	private String compnay ;
	private String project ;
	private String phone ;
	private String anonymous;
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getCompnay() {
		return compnay;
	}
	public void setCompnay(String compnay) {
		this.compnay = compnay;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}
	
}
