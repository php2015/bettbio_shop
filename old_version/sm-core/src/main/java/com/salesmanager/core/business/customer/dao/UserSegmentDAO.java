package com.salesmanager.core.business.customer.dao;



import com.salesmanager.core.business.customer.model.UserSegment;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface UserSegmentDAO extends SalesManagerEntityDao<Long, UserSegment> {

	UserSegment getByName(String name);

}
