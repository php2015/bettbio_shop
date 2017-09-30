package com.salesmanager.core.business.user.dao;

import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.model.UserList;

public interface UserDao extends SalesManagerEntityDao<Long, User> {

	User getByUserName(String userName);
	
	User getByUserMobile(String userName);

	List<User> listUser();

	List<User> listUserByStore(MerchantStore store);
	
	UserList findByCriteria(Criteria criteria);

	List<User> getListByCriteria(Criteria criteria);
	
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

	/**
	 * 根据登录使用的名字/电话/邮件，判断该名称可能对应的用户
	 * @param logonName
	 * @return
	 */
	Map<String, User> getPossibleUsersByLogonName(String logonName);

}
