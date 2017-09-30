package com.salesmanager.core.business.customer.dao;



import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface CustomerDAO extends SalesManagerEntityDao<Long, Customer> {
	
	Customer getByNick(String nick);

	CustomerList listByCriteria(CustomerCriteria criteria);

	Customer getByNick(String nick, Long storeId);
	
	Customer getByEmail(String email);
	
	Customer getByEmailForRegister(String email);
	
	Customer getByPhone(String phone);
	
	Customer getByPhoneForRegister(String phone);
	
	Customer getByID(String ID);
	
	 boolean getCustomerExistsPhone(String phone);
}
