package com.bettbio.core.service;

import java.util.Map;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SysUser;

/**
 * 系统内部员工操作
 * 
 * @author GuoChunbo
 *
 */
public interface SysUserService extends IService<SysUser> {
	SysUser findSysUserByLogin(SysUser sysUser);

	SysUser login(SysUser sysUser);

	SysUser findByJobNumber(String jobNumber);

	SysUser findByAccount(String account);
	
	int deleteSysUser(Integer id);

	/**
	 * 禁用
	 * @param id
	 * @return
	 */
	int disableSysUser(Integer [] ids);
	

	/**
	 * 取消禁用
	 * @param id
	 * @return
	 */
	int unDisableSysUser(Integer [] ids);
}
