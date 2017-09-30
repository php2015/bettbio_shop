package com.salesmanager.core.business.customer.dao;

import com.salesmanager.core.business.customer.model.Gifts;
import com.salesmanager.core.business.customer.model.GiftsCriteria;
import com.salesmanager.core.business.customer.model.GiftsList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;


public interface GiftsDAO extends SalesManagerEntityDao<Long, Gifts> {
	
	 GiftsList getByCriteria(GiftsCriteria giftscriteria);

}
