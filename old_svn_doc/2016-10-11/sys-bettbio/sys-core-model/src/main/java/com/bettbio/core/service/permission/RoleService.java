package com.bettbio.core.service.permission;

import java.util.Set;

import com.bettbio.core.model.permission.bo.Resource;
import com.bettbio.core.model.permission.bo.Role;

/**
 * 角色操作
 * 
 * @author GuoChunbo
 *
 */
public interface RoleService {

	int addRole(Role role);

	int delRole(Integer roleId);

	Role findRoleById(Integer roleId);
	
	/**
	 * 根据角色编号得到角色标识符列表
	 * 
	 * @param roleIds
	 * @return
	 */
	Set<String> findRoles(Integer... roleIds);

	/**
	 * 根据角色编号得到权限字符串列表
	 * 
	 * @param roleIds
	 * @return
	 */
	Set<String> findResources(Integer[] roleIds);
	
	/**
	 * 根据角色id查找资源
	 * @param roleIds
	 * @return
	 */
	Set<Resource> findResourceMenu(Integer[] roleIds);
}
