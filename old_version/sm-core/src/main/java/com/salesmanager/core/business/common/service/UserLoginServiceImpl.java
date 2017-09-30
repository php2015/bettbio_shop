package com.salesmanager.core.business.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.dao.UserLoginDao;
import com.salesmanager.core.business.common.model.UserLogin;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("userLoginService")
public class UserLoginServiceImpl extends SalesManagerEntityServiceImpl<Long, UserLogin> 
implements UserLoginService{
	private UserLoginDao userLoginDao;
	
	@Autowired
	public UserLoginServiceImpl(
			UserLoginDao userLoginDao) {
			super(userLoginDao);
			this.userLoginDao = userLoginDao;
	}

	@Override
	public void saveOrUpdate(UserLogin userLogin) throws ServiceException {
		// TODO Auto-generated method stub
		if(userLogin.getId()==null || userLogin.getId().longValue()==0) {
			userLoginDao.save(userLogin);
		} else {
			userLoginDao.update(userLogin);
		}
	}

	@Override
	public void deleteByUserId(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		userLoginDao.deleteByUserId(id);
	}
	

}
