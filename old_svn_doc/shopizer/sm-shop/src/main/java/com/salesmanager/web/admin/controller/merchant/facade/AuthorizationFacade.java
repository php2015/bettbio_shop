package com.salesmanager.web.admin.controller.merchant.facade;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.admin.entity.products.ReadableManufactureList;

public interface AuthorizationFacade {
	ReadableManufactureList getByCriteria(Criteria criteria,Language language)throws Exception;;
}
