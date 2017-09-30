package com.salesmanager.core.business.common.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.common.model.CommonSoftware;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("commonSoftwareDao")
public class CommonSoftwareDaoImpl extends SalesManagerEntityDaoImpl<Long, CommonSoftware>
		implements CommonSoftwareDao {

	public CommonSoftwareDaoImpl() {
		super();
	}
}
