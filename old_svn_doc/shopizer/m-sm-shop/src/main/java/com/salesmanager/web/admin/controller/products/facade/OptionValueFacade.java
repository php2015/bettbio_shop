package com.salesmanager.web.admin.controller.products.facade;


import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.admin.entity.products.ReadableOptionValueList;

public interface OptionValueFacade {
	ReadableOptionValueList getByCriteria(Criteria criteria,Language language) throws Exception;
}
