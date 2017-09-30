package com.bettbio.core.service.permission.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.mapper.SysRoleMapper;
import com.bettbio.core.model.permission.bo.Resource;
import com.bettbio.core.model.permission.bo.Role;
import com.bettbio.core.service.permission.ResourceService;
import com.bettbio.core.service.permission.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	SysRoleMapper sysRoleMapper;
	
	@Autowired
	ResourceService resourceService;
	
	@Override
	public int addRole(Role role) {
		return sysRoleMapper.add(role);
	}
	
	@Override
	public int delRole(Integer roleId) {
		return sysRoleMapper.deleteByPrimaryKey(roleId);
	}
	
	@Override
	public Role findRoleById(Integer roleId) {
		return sysRoleMapper.findRoleById(roleId);
	}	
	
	@Override
	public Set<String> findRoles(Integer... roleIds) {
		Set<String> roles = new HashSet<String>();
        for(Integer roleId : roleIds) {
            Role role = findRoleById(roleId);
            if(role != null) {
                roles.add(role.getName());
            }
        }
        return roles;
	}

	@Override
	public Set<String> findResources(Integer[] roleIds) {
		 Set<Integer> resourceIds = new HashSet<Integer>();
	        for(Integer roleId : roleIds) {
	            Role role = findRoleById(roleId);
	            if(role != null) {
	                resourceIds.addAll(role.getResourceIds());
	            }
	        }
	        return resourceService.findPermissions(resourceIds);
	}

	@Override
	public Set<Resource> findResourceMenu(Integer[] roleIds) {
		Set<Integer> resourceIds = new HashSet<Integer>();
        for(Integer roleId : roleIds) {
            Role role = findRoleById(roleId);
            if(role != null) {
                resourceIds.addAll(role.getResourceIds());
            }
        }
		return resourceService.findMenus(resourceIds);
	}
}
