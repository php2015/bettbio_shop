package com.salesmanager.core.business.customer.service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface CustomerInvoiceService extends SalesManagerEntityService<Long, CustomerInvoice>{
	void saveOrUpdate(CustomerInvoice customerInvoice) throws ServiceException;
	public CustomerInvoice getbyid(long id,Customer customer)throws ServiceException;
	public CustomerInvoice formatInvoice(CustomerInvoice customerInvoice)throws ServiceException;
	public CustomerInvoice getDefaultInvoice(Customer customer)throws ServiceException;
	public long saveOrUpdateInvoice(CustomerInvoice customerInvoice)throws ServiceException;
	public void delete(long id) throws ServiceException ;
}