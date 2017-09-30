package com.salesmanager.core.business.order.dao.orderproduct;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;

public interface OrderProductDao extends SalesManagerEntityDao<Long, OrderProduct> {
  /**
   * 用户订单总数
   * */
	public int getOraderByCount(Long customerId);
}
