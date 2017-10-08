package com.salesmanager.core.business.customer.service;



import com.salesmanager.core.business.common.model.Address;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface CustomerService  extends SalesManagerEntityService<Long, Customer> {


	Customer getByNick(String nick);
	void saveOrUpdate(Customer customer) throws ServiceException ;

	CustomerList listByCriteria(CustomerCriteria criteria);

	Customer getByNick(String nick, Long storeId);
	
	Customer getByEmail(String email);
	
	Customer getByEmailForRegister(String email);
	
	Customer getByPhone(String phone);
	
	Customer getByPhoneForRegister(String phone);

	Customer getByID(String ID);
	/**
	 * Return an {@link com.salesmanager.core.business.common.model.Address} object from the client IP address. Uses underlying GeoLocation module
	 * @param store
	 * @param ipAddress
	 * @return
	 * @throws ServiceException
	 */
	Address getCustomerAddress(MerchantStore store, String ipAddress)
			throws ServiceException;

	
	 boolean getCustomerExistsPhone(String phone);
	 
	void verifyCustomerPhone(String phone) throws ServiceException;
	void verifyCustomerEmail(String emailAddress) throws ServiceException;

}
