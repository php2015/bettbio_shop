package com.salesmanager.core.business.customer.dao;

import java.util.List;

import com.salesmanager.core.business.customer.model.MemberCriteria;
import com.salesmanager.core.business.customer.model.MemberPointList;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface MemberPointsDAO extends SalesManagerEntityDao<Long, MemberPoints> {
	/**
     * 查询客户的积分
     * */
	public int queryByMemberPoints(Long customerid);
	MemberPointList getByCriteria(MemberCriteria memberCriteria);
	List<MemberPoints> getLeftPoint(Long customerid);
	public List<MemberPoints> getByOrderId(long orderId);
}
