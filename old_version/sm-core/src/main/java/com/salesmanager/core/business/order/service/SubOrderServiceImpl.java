package com.salesmanager.core.business.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.order.dao.SubOrderDao;
import com.salesmanager.core.business.order.model.SubOrder;

@Service("subOrderService")
public class SubOrderServiceImpl extends SalesManagerEntityServiceImpl<Long, SubOrder> implements  SubOrderService{
	private final SubOrderDao subOrderDao;
	
	@Autowired
	public SubOrderServiceImpl(final SubOrderDao subDao) {
		super(subDao);
		this.subOrderDao=subDao;
		// TODO Auto-generated constructor stub
	}

	
	/**
	public SubOrderServiceImpl(final SubOrderDao subDao ) {
		super(subDao);
		this.subOrderDao=subDao;
		
	}

	*/
	

	@Override
	public void saveOrUpdate(SubOrder subOrder) throws ServiceException {
		// TODO Auto-generated method stub
		 if(subOrder.getId()!=null && subOrder.getId()>0) {
	           // LOGGER.debug("Updating Order");
	            super.update(subOrder);

	        } else {
	            //LOGGER.debug("Creating Order");
	            super.create(subOrder);

	        }
	}

	@Override
	public SubOrder getSubOrderById(long id) {
		// TODO Auto-generated method stub
		return subOrderDao.getSubOrderById(id);
	}

}
