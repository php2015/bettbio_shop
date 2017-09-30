package com.salesmanager.core.business.common.service;

import com.salesmanager.core.business.common.model.UserLogin;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface UserLoginService extends SalesManagerEntityService<Long, UserLogin> {

	/**
	 * Create or update a User
	 * @param user
	 * @throws ServiceException
	 */
	void saveOrUpdate(UserLogin userLogin) throws ServiceException;

	void deleteByUserId(Long id) throws ServiceException;



}
