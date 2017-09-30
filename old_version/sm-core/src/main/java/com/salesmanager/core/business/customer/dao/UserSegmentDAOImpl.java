package com.salesmanager.core.business.customer.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.customer.model.QUserSegment;
import com.salesmanager.core.business.customer.model.UserSegment;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("userSegmentDao")
public class UserSegmentDAOImpl extends SalesManagerEntityDaoImpl<Long, UserSegment> implements UserSegmentDAO {

	public UserSegmentDAOImpl() {
		super();
	}

	@Override
	public UserSegment getByName(String name) {
		QUserSegment qUS = QUserSegment.userSegment;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qUS).where(qUS.name.eq(name));
		return query.singleResult(qUS);
	}
	
	
	
}
