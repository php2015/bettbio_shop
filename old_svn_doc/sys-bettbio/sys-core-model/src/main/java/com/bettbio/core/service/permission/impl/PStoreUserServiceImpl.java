package com.bettbio.core.service.permission.impl;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.mapper.SStoreUserMapper;
import com.bettbio.core.model.permission.bo.Resource;
import com.bettbio.core.model.permission.bo.StoreUser;
import com.bettbio.core.service.permission.PStoreUserService;
import com.bettbio.core.service.permission.RoleService;

@Service
public class PStoreUserServiceImpl implements PStoreUserService {

	@Autowired
	SStoreUserMapper sStoreUserMapper;
	
	
	@Autowired
	RoleService roleService;
	
	@Override
	public StoreUser findByAccount(String account) {
		return sStoreUserMapper.findByAccount(account);
	}
	
	@Override
	public Set<String> findRoles(String account) {
		StoreUser user = findByAccount(account);
        if(user == null) {
            return Collections.emptySet();
        }
        return roleService.findRoles(user.getRoleIdsList().toArray(new Integer[0]));
	}

	@Override
	public Set<String> findResources(String account) {
		StoreUser user = findByAccount(account);
        if(user == null) {
            return Collections.emptySet();
        }
        return roleService.findResources(user.getRoleIdsList().toArray(new Integer[0]));
	}
	
	@Override
	public int updateUserRoles(StoreUser storeUser) {
		return sStoreUserMapper.updateUserRoles(storeUser);
	}
	
	@Override
	public Set<Resource> findUserMenu(String account) {
		StoreUser user = findByAccount(account);
        if(user == null) {
            return Collections.emptySet();
        }
        return roleService.findResourceMenu(user.getRoleIdsList().toArray(new Integer[0]));
	}
}
