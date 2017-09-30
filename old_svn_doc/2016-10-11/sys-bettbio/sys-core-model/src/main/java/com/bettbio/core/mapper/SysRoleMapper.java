package com.bettbio.core.mapper;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SysRole;
import com.bettbio.core.model.permission.bo.Role;

public interface SysRoleMapper extends MapperSupport<SysRole> {
	public Role findRoleById(Integer id);
	public int add(SysRole role);
}