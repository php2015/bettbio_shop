package com.salesmanager.core.business.customer.model;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class MemberPointList extends EntityList {
	/**
	 * 获取积分记录
	 */
	private static final long serialVersionUID = -3108842276158069739L;
	private List<MemberPoints> memberPoints;
	public List<MemberPoints> getMemberPoints() {
		return memberPoints;
	}
	public void setMemberPoints(List<MemberPoints> memberPoints) {
		this.memberPoints = memberPoints;
	}
	
}
