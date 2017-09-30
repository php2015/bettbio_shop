package com.salesmanager.core.business.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.dao.BasedataTypeDao;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("basedataTypeService")
public class BasedataTypeServiceImpl extends SalesManagerEntityServiceImpl<Long, BasedataType> 
	implements BasedataTypeService {

	private BasedataTypeDao basedataTypeDao;
	
	@Autowired
	public BasedataTypeServiceImpl(
			BasedataTypeDao basedataTypeDao) {
			super(basedataTypeDao);
			this.basedataTypeDao = basedataTypeDao;
	}

	@Override
	public List<Object[]> getStoreName(String type) {
		// TODO Auto-generated method stub
		return basedataTypeDao.getStoreName(type);
	}

	@Override
	public List<BasedataType> getByName(String name) {
		// TODO Auto-generated method stub
		return basedataTypeDao.getByName(name);
	}

}
