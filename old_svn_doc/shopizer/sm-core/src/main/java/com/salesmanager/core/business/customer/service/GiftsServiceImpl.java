package com.salesmanager.core.business.customer.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.GiftsDAO;
import com.salesmanager.core.business.customer.model.Gifts;
import com.salesmanager.core.business.customer.model.GiftsCriteria;
import com.salesmanager.core.business.customer.model.GiftsList;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("giftsImpl")
public class GiftsServiceImpl extends SalesManagerEntityServiceImpl<Long, Gifts> implements GiftsService{
	@Autowired
	private GiftsDAO giftsDAO;
	@Autowired
	public GiftsServiceImpl(GiftsDAO giftsDAO) {
		super(giftsDAO);
		this.giftsDAO=giftsDAO;
	}

	@Override
	public GiftsList getByCriteria(GiftsCriteria giftsCriteria) {
		return giftsDAO.getByCriteria(giftsCriteria);
	}

}
