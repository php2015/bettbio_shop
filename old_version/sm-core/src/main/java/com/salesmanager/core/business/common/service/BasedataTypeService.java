package com.salesmanager.core.business.common.service;

import java.util.List;

import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface BasedataTypeService extends SalesManagerEntityService<Long, BasedataType> {
	public List<Object[]> getStoreName(String type);
	public List<BasedataType> getByName(String name);

}
