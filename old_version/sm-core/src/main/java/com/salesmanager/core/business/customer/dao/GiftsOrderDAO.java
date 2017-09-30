package com.salesmanager.core.business.customer.dao;

import com.salesmanager.core.business.customer.model.GftsOrderCriteria;
import com.salesmanager.core.business.customer.model.GiftOrder;
import com.salesmanager.core.business.customer.model.GiftOrdersList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface GiftsOrderDAO extends SalesManagerEntityDao<Long, GiftOrder> {
	
	GiftOrdersList getByCriteria(GftsOrderCriteria gftsordercriteria);

}
