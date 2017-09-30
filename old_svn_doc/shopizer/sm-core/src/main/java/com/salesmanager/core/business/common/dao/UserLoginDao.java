package com.salesmanager.core.business.common.dao;


import com.salesmanager.core.business.common.model.UserLogin;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface UserLoginDao extends SalesManagerEntityDao<Long, UserLogin>{
	
	void deleteByUserId(Long id);

}
