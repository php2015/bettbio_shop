package com.salesmanager.core.business.merchant.authorization.dao;



import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;


@Repository("authorizationDao")
public class AuthorizationDaoImpl extends SalesManagerEntityDaoImpl<Long, Authorization> implements AuthorizationDao {

	public AuthorizationDaoImpl() {
		super();
	}
	
	
	
}
