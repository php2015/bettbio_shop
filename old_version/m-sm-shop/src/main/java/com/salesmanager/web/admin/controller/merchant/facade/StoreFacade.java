package com.salesmanager.web.admin.controller.merchant.facade;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.web.entity.shop.ReadableStore;

public interface StoreFacade {
	ReadableStore getByCriteria(Criteria store) throws Exception;

}
