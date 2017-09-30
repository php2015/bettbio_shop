package com.salesmanager.core.business.system.dao;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.system.model.SpecialConfiguration;

public interface SpecialConfigurationDao extends SalesManagerEntityDao<Long, SpecialConfiguration> {
	SpecialConfiguration getByKey(String key);
}
