package com.salesmanager.core.business.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.dao.CommonSoftwareDao;
import com.salesmanager.core.business.common.model.CommonSoftware;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("commonSoftwareService")
public class CommonSoftwareServiceImpl extends SalesManagerEntityServiceImpl<Long, CommonSoftware> 
	implements CommonSoftwareService {

	private CommonSoftwareDao commonSoftwareDao;
	
	@Autowired
	public CommonSoftwareServiceImpl(
			CommonSoftwareDao commonSoftwareDao) {
			super(commonSoftwareDao);
			this.commonSoftwareDao = commonSoftwareDao;
	}
	
	@Override	
	public void saveOrUpdate(CommonSoftware commonSoftware) throws ServiceException {

		if(commonSoftware.getId()!=null && commonSoftware.getId()>0) {
			super.update(commonSoftware);
		} else {			
			super.create(commonSoftware);
		}
	}
}
