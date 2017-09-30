package com.salesmanager.web.shop.controller.customer.facade;

import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.entity.customer.CustomerAdress;
import com.salesmanager.web.entity.customer.DefautAddressInvoice;

public interface CustomerAddressFacade {
	public void saveOrUpdate(CustomerAdress customerAdress,Customer customerModel,Language language)throws Exception;
	public long saveOrUpdateAddress(CustomerAdress customerAdress,Customer customerModel,Language language)throws Exception;
	public List<CustomerAdress> getCustomerAddress(Customer customerModel,Language language)throws Exception;
	public CustomerAdress getById(long id,Customer customerModel,Language language)throws Exception;
	public CustomerAdress getDefaultAdress(Customer customerModel,Language language)throws Exception;
	public DefautAddressInvoice initDefaultData(Customer customerModel,Language language)throws Exception;
	public void remove(long id)throws Exception;
}
