package com.salesmanager.core.business.customer.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.CustomerInvoiceDAO;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("customerInvoiceService")
public class CustomerInvoiceServiceImpl extends SalesManagerEntityServiceImpl<Long, CustomerInvoice> implements CustomerInvoiceService{
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	public CustomerInvoiceServiceImpl(CustomerInvoiceDAO	invoiceDAO) {
		super(invoiceDAO);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveOrUpdate(CustomerInvoice customerInvoice)
			throws ServiceException {
		// TODO Auto-generated method stub
		if(customerInvoice.getId()!=null && customerInvoice.getId()>0) {
			super.update(customerInvoice);
		} else {			
		
			super.save(customerInvoice);

		}
		
	}

	@Override
	public CustomerInvoice getbyid(long id, Customer customer)
			throws ServiceException {
		// TODO Auto-generated method stub
		Set<CustomerInvoice> invoiceModel= customer.getInvoices();
		for(CustomerInvoice invoice:invoiceModel){
			if (invoice.getId()==id){
				return invoice;
			}
		}
		return null;
	}

	@Override
	public CustomerInvoice formatInvoice(CustomerInvoice customerInvoice)
			throws ServiceException {
		// TODO Auto-generated method stub
		int iType = customerInvoice.getType();
		if(iType == 0){
			customerInvoice.setBankAccount("");
			customerInvoice.setBankName("");
			customerInvoice.setCompanyAddress("");
			customerInvoice.setCompanyTelephone("");
			customerInvoice.setTaxpayerNumber("");
			String iName = customerInvoice.getCompany();
			//去掉最后一个逗号
			//if (iName.lastIndexOf("")==iName.length()-2){
				customerInvoice.setCompany(iName.substring(0,iName.length()-1));
			//}
		}else{
			String iName = customerInvoice.getCompany();
			//去掉最后一个逗号
			//if (iName.indexOf(",")==0){
				customerInvoice.setCompany(iName.substring(1));
			//}
		}
		return customerInvoice;
	}

	@Override
	public void delete(long id) throws ServiceException {
		CustomerInvoice customerInvoice = getById(id);
			super.delete(customerInvoice);
	
	}

	@Override
	public CustomerInvoice getDefaultInvoice(Customer customer)
			throws ServiceException {
		//get default invoice
		Set<CustomerInvoice> invoiceModel= customer.getInvoices();
		if (null != invoiceModel && invoiceModel.size()>0){
			for(CustomerInvoice invoice:invoiceModel){
				if(invoice.getId() == customer.getInvoiceDefault()){
					return invoice;
				  }
			}
			// first one as default
			for(CustomerInvoice invoice:invoiceModel){
				customer.setAddressDefault(invoice.getId());
				customerService.update(customer);
				return invoice;
			}
		}
		return null;
	}

	@Override
	public long saveOrUpdateInvoice(CustomerInvoice customerInvoice)
			throws ServiceException {
		// TODO Auto-generated method stub
		if(customerInvoice.getId()!=null && customerInvoice.getId()>0) {
			super.update(customerInvoice);
		} else {			
		
			super.save(customerInvoice);

		}
		return customerInvoice.getId();
	}

}
