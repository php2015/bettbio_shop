package com.bettbio.core.service.permission;

/**
 * 商家用户权限操作
 */
import java.util.Set;

import com.bettbio.core.model.permission.bo.Resource;
import com.bettbio.core.model.permission.bo.StoreUser;

/**
 * 商家用户相关角色权限操作
 * 
 * @author GuoChunbo
 *
 */
public interface PStoreUserService {

	int updateUserRoles(StoreUser storeUser);

	/**
	 * 根据账号名查找用户
	 * @param account
	 * @return
	 */
	StoreUser findByAccount(String account);
	
	/**
	 * 根据帐号名查找其角色
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findRoles(String account);

	/**
	 * 根据帐号名查找其权限
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findResources(String account);
	
	/**
	 * 根据帐号名查找用户菜单
	 * @param account
	 * @return
	 */
	public Set<Resource> findUserMenu(String account);
}
