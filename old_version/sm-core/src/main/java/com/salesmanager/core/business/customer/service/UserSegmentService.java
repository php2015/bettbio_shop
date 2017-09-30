package com.salesmanager.core.business.customer.service;



import com.salesmanager.core.business.customer.model.UserSegment;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface UserSegmentService  extends SalesManagerEntityService<Long, UserSegment> {

	void ensure(UserSegment uSegment) throws ServiceException;

	UserSegment getByName(String name) throws ServiceException;
}
