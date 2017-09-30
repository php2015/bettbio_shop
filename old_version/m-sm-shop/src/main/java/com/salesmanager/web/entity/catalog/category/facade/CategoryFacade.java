package com.salesmanager.web.entity.catalog.category.facade;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.entity.catalog.category.ReadableCatagoryList;

public interface CategoryFacade {
	ReadableCatagoryList getByCriteria(Criteria criteria,Language language) throws Exception;

}
