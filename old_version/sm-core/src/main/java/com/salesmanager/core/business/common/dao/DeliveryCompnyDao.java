package com.salesmanager.core.business.common.dao;


import java.util.List;

import com.salesmanager.core.business.common.model.DeliveryCompny;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface DeliveryCompnyDao extends SalesManagerEntityDao<Long, DeliveryCompny>{
	
	List<Object[]> getDeliveryName();
	
	List<DeliveryCompny> getDeliveryByName(String name);

}
