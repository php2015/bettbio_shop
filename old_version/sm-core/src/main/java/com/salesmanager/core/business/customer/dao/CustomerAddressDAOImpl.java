package com.salesmanager.core.business.customer.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("customerAddressDAO")
public class CustomerAddressDAOImpl extends SalesManagerEntityDaoImpl<Long, CustomerAddress> implements CustomerAddressDAO {
	public CustomerAddressDAOImpl() {
		super();
	}
}
