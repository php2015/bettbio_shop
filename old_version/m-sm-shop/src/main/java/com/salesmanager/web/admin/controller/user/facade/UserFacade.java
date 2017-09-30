package com.salesmanager.web.admin.controller.user.facade;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.web.admin.entity.user.UserList;

public interface UserFacade {
	UserList getByCriteria(Criteria criteria) throws Exception;

	int queryByAccount(int index, String account);
}
