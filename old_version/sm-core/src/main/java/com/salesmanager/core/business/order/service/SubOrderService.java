package com.salesmanager.core.business.order.service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.order.model.SubOrder;


public interface SubOrderService extends SalesManagerEntityService<Long, SubOrder>{
	void saveOrUpdate(SubOrder subOrder) throws ServiceException;
	SubOrder getSubOrderById(long id);
}
