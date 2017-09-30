package com.salesmanager.core.business.common.service;

import com.salesmanager.core.business.common.model.CommonSoftware;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface CommonSoftwareService extends SalesManagerEntityService<Long, CommonSoftware> {

	public void saveOrUpdate(CommonSoftware commonSoftware) throws ServiceException;
}
