package com.salesmanager.core.business.customer.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;



@Repository("customerInvoiceDAO")
public class CustomerInvoiceDAOImpl extends SalesManagerEntityDaoImpl<Long, CustomerInvoice> implements CustomerInvoiceDAO {
	public CustomerInvoiceDAOImpl() {
		super();
	}
}
