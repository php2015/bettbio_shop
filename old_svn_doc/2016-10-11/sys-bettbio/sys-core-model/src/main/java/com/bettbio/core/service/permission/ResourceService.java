package com.bettbio.core.service.permission;

import java.util.Set;

import com.bettbio.core.model.permission.bo.Resource;

/**
 * 权限资源操作
 * 
 * @author GuoChunbo
 *
 */
public interface ResourceService {

	int addResource(Resource resource);

	int delResource(Integer id);
	
	Resource findResourceById(Integer id);
	/**
	 * 得到资源对应的权限字符串
	 * 
	 * @param resourceIds
	 * @return
	 */
	Set<String> findPermissions(Set<Integer> resourceIds);

	/**
	 * 根据用户资源得到菜单
	 * 
	 * @param permissions
	 * @return
	 */
	Set<Resource> findMenus(Set<Integer> resourceIds);
}
