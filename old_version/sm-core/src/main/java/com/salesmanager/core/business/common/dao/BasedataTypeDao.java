package com.salesmanager.core.business.common.dao;

import java.util.List;

import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface BasedataTypeDao extends SalesManagerEntityDao<Long, BasedataType> {
	public List<Object[]> getStoreName(String type);
	public List<BasedataType> getByName(String name);

}
