package com.salesmanager.web.admin.controller.user.facade;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.web.admin.entity.user.UserList;

public interface UserFacade {
	UserList getByCriteria(Criteria criteria) throws Exception;
	/**
	 * 校验商家是否存在
	 * @param account
	 * @return
	 */
	int queryByAccount(int index,String account);
	/**
	 * 根据账户获取商家
	 * @return
	 */
	User getUserByAccount(String account);
}
