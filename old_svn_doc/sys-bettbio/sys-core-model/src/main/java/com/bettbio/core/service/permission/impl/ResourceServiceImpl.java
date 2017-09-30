package com.bettbio.core.service.permission.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.mapper.SysJurisdictionMapper;
import com.bettbio.core.model.permission.bo.Resource;
import com.bettbio.core.service.permission.ResourceService;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	SysJurisdictionMapper sysJurisdictionMapper;

	@Override
	public int addResource(Resource resource) {
		return sysJurisdictionMapper.insert(resource);
	}

	@Override
	public int delResource(Integer id) {
		return 0;
	}

	@Override
	public Resource findResourceById(Integer id) {
		return sysJurisdictionMapper.findResourceById(id);
	}

	@Override
	public Set<String> findPermissions(Set<Integer> resourceIds) {
		Set<String> permissions = new HashSet<String>();
		for (Integer resourceId : resourceIds) {
			Resource resource = findResourceById(resourceId);
			if (resource != null && !StringUtils.isEmpty(resource.getUrl())) {
				permissions.add(resource.getUrl());
			}
		}
		return permissions;
	}

	@Override
	public Set<Resource> findMenus(Set<Integer> resourceIds) {
		Set<Resource> permissions = new HashSet<Resource>();
		for (Integer resourceId : resourceIds) {
			Resource resource = findResourceById(resourceId);
			if (resource != null && !StringUtils.isEmpty(resource.getUrl())
					&& !StringUtils.isEmpty(resource.getParentCode()) && resource.getParentCode().equals("0")) {
				permissions.add(resource);
			}
		}
		return permissions;
	}

}
