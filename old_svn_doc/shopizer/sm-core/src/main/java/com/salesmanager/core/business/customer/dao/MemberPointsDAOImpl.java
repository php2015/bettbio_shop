package com.salesmanager.core.business.customer.dao;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.*;
import com.salesmanager.core.business.customer.model.*;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
@Repository("memberPointsDao")
public class MemberPointsDAOImpl extends SalesManagerEntityDaoImpl<Long, MemberPoints> implements MemberPointsDAO {

    /**
     * 查询客户的积分
     * */
	public int queryByMemberPoints(Long customerid)
    {
        StringBuffer countBuilderSelect = new StringBuffer();
        countBuilderSelect.append((new StringBuilder("select sum(mp.ltfePoint) from MemberPoints mp where mp.customer.id=")).append(customerid).append(" and statas=0").toString());
        Query countQ = super.getEntityManager().createQuery(countBuilderSelect.toString());
        Number piont = (Number)countQ.getSingleResult();
        if(piont != null)
            return piont.intValue();
        else
            return 0;
    }

    public MemberPointList getByCriteria(MemberCriteria memberCriteria)
    {
        MemberPointList memberPointList = new MemberPointList();
        QMemberPoints qMemberPoints = QMemberPoints.memberPoints;
        JPQLQuery query = new JPAQuery(getEntityManager());
        query.from(new EntityPath[] {
            qMemberPoints
        });
        BooleanBuilder pBuilder = new BooleanBuilder();
        if(memberCriteria.getCustomerID() == null)
            return memberPointList;
        pBuilder.and(qMemberPoints.customer.id.eq(memberCriteria.getCustomerID()));
        if(memberCriteria.getStatus() != -1)
            pBuilder.and(qMemberPoints.statas.eq(Integer.valueOf(memberCriteria.getStatus())));
        query.where(new Predicate[] {
            pBuilder
        });
        if(memberCriteria.getMaxCount() > 0)
        {
            query.limit(memberCriteria.getMaxCount());
            query.offset(memberCriteria.getStartIndex());
        }
        memberPointList.setMemberPoints(query.list(qMemberPoints));
        memberPointList.setTotalCount((int)query.count());
        return memberPointList;
    }

	public List getLeftPoint(Long customerid)
    {
        QMemberPoints qMemberPoints = QMemberPoints.memberPoints;
        JPQLQuery query = new JPAQuery(getEntityManager());
        ((JPQLQuery)query.from(new EntityPath[] {
            qMemberPoints
        })).orderBy(new OrderSpecifier[] {
            qMemberPoints.dateValid.desc()
        });
        BooleanBuilder pBuilder = new BooleanBuilder();
        pBuilder.and(qMemberPoints.customer.id.eq(customerid));
        pBuilder.and(qMemberPoints.type.notIn("EXCHANGE_SCORE"));//去除兑换积分
        pBuilder.and(qMemberPoints.statas.eq(Integer.valueOf(0)));
        query.where(new Predicate[] {
            pBuilder
        });
        return query.list(qMemberPoints);
    }
	
}
