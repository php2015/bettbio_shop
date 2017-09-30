package com.salesmanager.core.business.customer.service;





import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.GftsOrderCriteria;
import com.salesmanager.core.business.customer.model.GiftOrder;
import com.salesmanager.core.business.customer.model.GiftOrdersList;
import com.salesmanager.core.business.customer.model.Gifts;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface GiftsOrderService  extends SalesManagerEntityService<Long, GiftOrder> {
	GiftOrdersList getByCriteria(GftsOrderCriteria giftOderCriteria);
	void saveOrUpdate(GiftOrder giftOrder)  throws ServiceException;
	@Transactional(rollbackFor = { Exception.class })
	int saveGift(Customer customer,long cid,Gifts gift,int num) throws ServiceException;
}
