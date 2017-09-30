package com.salesmanager.core.business.customer.service;

import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface CustomerAddressService extends SalesManagerEntityService<Long, CustomerAddress>{
	void saveOrUpdate(CustomerAddress customerAdress) throws ServiceException;
	void delete(long id) throws ServiceException;

}
