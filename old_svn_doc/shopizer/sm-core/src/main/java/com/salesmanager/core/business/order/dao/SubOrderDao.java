package com.salesmanager.core.business.order.dao;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.order.model.SubOrder;

public interface SubOrderDao extends SalesManagerEntityDao<Long, SubOrder>  {
	SubOrder getSubOrderById(long id);
}
