package com.salesmanager.core.business.customer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.CustomerAddressDAO;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("customerAddressService")
public class CustomerAddressServiceImpl extends SalesManagerEntityServiceImpl<Long, CustomerAddress> implements CustomerAddressService{

	@Autowired
	public CustomerAddressServiceImpl(CustomerAddressDAO addressDAO){
		super(addressDAO);
	}
	

	@Override	
	public void saveOrUpdate(CustomerAddress customerAdress) throws ServiceException {

		//LOGGER.debug("Creating customerAdress");		
		if(customerAdress.getId()!=null && customerAdress.getId()>0) {
			super.update(customerAdress);
		} else {			
		
			super.create(customerAdress);

		}
	}
	
	@Override
	public void delete(long id) throws ServiceException {
		CustomerAddress address = getById(id);	
		//address.setCustomer(customerModel);	
		super.delete(address);
		
	}

}
