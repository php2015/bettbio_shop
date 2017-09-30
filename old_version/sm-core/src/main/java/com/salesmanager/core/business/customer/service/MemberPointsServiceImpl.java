package com.salesmanager.core.business.customer.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.MemberPointsDAO;
import com.salesmanager.core.business.customer.model.MemberCriteria;
import com.salesmanager.core.business.customer.model.MemberPointList;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("memberPointsService")
public class MemberPointsServiceImpl extends SalesManagerEntityServiceImpl<Long, MemberPoints> implements MemberPointsService {
    @Autowired
    private MemberPointsDAO mpdao;
	@Autowired
	private MemberPointsDAO memberPointsDAO;
	@Autowired
	public MemberPointsServiceImpl(MemberPointsDAO memberPointsDAO) {
		super(memberPointsDAO);
		this.memberPointsDAO=memberPointsDAO;
	}
	/**
     * 查询客户的积分
     * */
	public int queryByMemberPoints(Long customerid){
		return mpdao.queryByMemberPoints(customerid);
	}
	@Override
	public MemberPointList getByCriteria(MemberCriteria memberCriteria) {
		// TODO Auto-generated method stub
		return memberPointsDAO.getByCriteria(memberCriteria);
	}
	@Override
	public List<MemberPoints> getLeftPoint(Long customerid) {
		// TODO Auto-generated method stub
		return memberPointsDAO.getLeftPoint(customerid);
	}
	@Override
	public List<MemberPoints> getByOrderId(long orderId) {
		return memberPointsDAO.getByOrderId(orderId);
	}

	
}
