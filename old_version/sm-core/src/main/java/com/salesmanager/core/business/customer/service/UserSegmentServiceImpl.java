package com.salesmanager.core.business.customer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.UserSegmentDAO;
import com.salesmanager.core.business.customer.model.QUserSegment;
import com.salesmanager.core.business.customer.model.UserSegment;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("userSegmentService")
public class UserSegmentServiceImpl extends SalesManagerEntityServiceImpl<Long, UserSegment> implements UserSegmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserSegmentServiceImpl.class);
	
	private UserSegmentDAO userSegmentDAO;
	
	@Autowired
	public UserSegmentServiceImpl(UserSegmentDAO genericDao) {
		super(genericDao);
		this.userSegmentDAO = genericDao;
	}

	@Override
	public UserSegment getById(Long id) {
		return userSegmentDAO.getById(id);
	}

	@Override
	public void ensure(UserSegment uSegment) throws ServiceException {
		UserSegment existed = getByName(uSegment.getName());
		if (existed != null){
			return;
		}
		create(uSegment);
	}

	@Override
	public UserSegment getByName(String name) throws ServiceException{
		return userSegmentDAO.getByName(name);
	}

	
}
