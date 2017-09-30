package com.bettbio.core.mapper;

import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SysUser;

public interface SysUserMapper extends MapperSupport<SysUser> {
	SysUser findSysUserByLogin(SysUser sysUser);

	SysUser login(SysUser sysUser);

	SysUser findByJobNumber(String jobNumber);

	SysUser findByAccount(String account);

	int deleteSysUser(Integer id);

	int disableSysUser(Map<String, Object> map);
	
	int add(SysUser user);
}