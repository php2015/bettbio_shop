package com.salesmanager.web.admin.controller.products.facade;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.web.admin.entity.products.ReadableRalationshipList;

public interface RelationshipFacade {
	ReadableRalationshipList getByCriteria(Criteria criteria) throws Exception;

}
