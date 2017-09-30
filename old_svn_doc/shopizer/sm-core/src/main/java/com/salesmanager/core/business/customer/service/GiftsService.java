package com.salesmanager.core.business.customer.service;





import com.salesmanager.core.business.customer.model.Gifts;
import com.salesmanager.core.business.customer.model.GiftsCriteria;
import com.salesmanager.core.business.customer.model.GiftsList;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface GiftsService  extends SalesManagerEntityService<Long, Gifts> {
	GiftsList getByCriteria(GiftsCriteria giftsCriteria);

}
