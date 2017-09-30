package com.salesmanager.core.business.user.service;

import java.util.List;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.model.UserList;

public interface UserService extends SalesManagerEntityService<Long, User> {

	User getByUserName(String userName) throws ServiceException;

	List<User> listUser() throws ServiceException;
	
	/**
	 * Create or update a User
	 * @param user
	 * @throws ServiceException
	 */
	void saveOrUpdate(User user) throws ServiceException;

	List<User> listByStore(MerchantStore store) throws ServiceException;
	
	UserList listByCriteria(Criteria criteria) throws ServiceException;
	
	List<User> getListByCriteria(Criteria criteria) ;

	/**
	 * 验证用户名、商家是否已经存在了
	 * */
	public int queryByUser(String param,String value);
	/**
	 * 验证账号是否存在
	 * @param account
	 * @return
	 */
	public int queryByAccount(int index,String account);
	/**
	 * 根据邮箱获取用户
	 * @param meail
	 * @return
	 */
	public User queryUserByEmail(String meail);
	/**
	 * 根据手机获取用户
	 * @param phone
	 * @return
	 */
	public User queryUserByPhone(String phone);

}
