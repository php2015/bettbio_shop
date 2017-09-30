package com.salesmanager.core.business.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.dao.DeliveryCompnyDao;
import com.salesmanager.core.business.common.model.DeliveryCompny;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("deliveryCompnyService")
public class DeliveryCompnyServiceImpl extends SalesManagerEntityServiceImpl<Long, DeliveryCompny> 
implements DeliveryCompnyService{
	private DeliveryCompnyDao deliveryCompnyDao;
	
	@Autowired
	public DeliveryCompnyServiceImpl(
			DeliveryCompnyDao deliveryCompnyDao) {
			super(deliveryCompnyDao);
			this.deliveryCompnyDao = deliveryCompnyDao;
	}
	@Override
	public List<Object[]> getDeliveryName() throws ServiceException {
		// TODO Auto-generated method stub
		return deliveryCompnyDao.getDeliveryName();
	}

	@Override
	public List<DeliveryCompny> getDeliveryByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return deliveryCompnyDao.getDeliveryByName(name);
	}

}
