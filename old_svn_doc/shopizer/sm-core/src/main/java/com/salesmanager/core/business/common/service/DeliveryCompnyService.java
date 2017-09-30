package com.salesmanager.core.business.common.service;

import java.util.List;

import com.salesmanager.core.business.common.model.DeliveryCompny;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface DeliveryCompnyService extends SalesManagerEntityService<Long, DeliveryCompny> {
	
	List<Object[]> getDeliveryName() throws ServiceException;
	
	List<DeliveryCompny> getDeliveryByName(String name)throws ServiceException;
	

}
