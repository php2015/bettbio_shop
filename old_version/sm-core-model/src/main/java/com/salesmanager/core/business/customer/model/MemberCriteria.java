package com.salesmanager.core.business.customer.model;

import java.util.Date;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.order.model.orderstatus.MemberPorintStatus;

public class MemberCriteria extends Criteria {
	
	private Long customerID; //用户id
	
	private int status =-1;		//积分状态 0。未到账  1.已到账 2.已撤销    默认为-1：查询所有
	
	private Date dateVilad;		//有效期
	
	private Date dateCreate;	//积分更新时间
	
	private MemberPorintStatus mpsStatus =null;  //枚举型积分状态
	
	
	public MemberPorintStatus getMpsStatus() {
		return mpsStatus;
	}

	public void setMpsStatus(MemberPorintStatus mpsStatus) {
		this.mpsStatus = mpsStatus;
	}

	public Long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDateVilad() {
		return dateVilad;
	}

	public void setDateVilad(Date dateVilad) {
		this.dateVilad = dateVilad;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}
	
	
}
