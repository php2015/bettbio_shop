package com.salesmanager.core.business.order.service.orderproduct;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;

public interface OrderProductService extends SalesManagerEntityService<Long, OrderProduct>{
	void saveOrUpdate(OrderProduct orderProduct) throws ServiceException;
	   /**
	   * 用户订单总数
	   * */
		public int getOraderByCount(Long customerId);
}
