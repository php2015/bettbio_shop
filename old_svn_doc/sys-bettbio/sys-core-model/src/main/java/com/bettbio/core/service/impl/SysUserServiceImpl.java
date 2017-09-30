package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.enums.Disable;
import com.bettbio.core.mapper.SysUserMapper;
import com.bettbio.core.model.SysUser;
import com.bettbio.core.service.SysUserService;

@Service
public class SysUserServiceImpl extends BaseService<SysUser> implements SysUserService {

	public SysUserMapper getSysUserMapper() {
		return (SysUserMapper) mapper;
	}

	@Override
	public SysUser findSysUserByLogin(SysUser sysUser) {

		return getSysUserMapper().findSysUserByLogin(sysUser);
	}

	@Override
	public SysUser login(SysUser sysUser) {

		sysUser.setPassword(PasswordHelper.newPassword(sysUser.getPassword()));
		return getSysUserMapper().login(sysUser);
	}

	@Override
	public Page<SysUser> selectByPage(Map<String, Object> map) {
		Page<SysUser> page = new Page<SysUser>(map, getSysUserMapper().selectCountByPage(map));

		page.setList(getSysUserMapper().selectByPage(page.getMap()));
		return page;
	}

	@Override
	public SysUser findByJobNumber(String jobNumber) {

		return getSysUserMapper().findByJobNumber(jobNumber);
	}

	@Override
	public SysUser findByAccount(String account) {

		return getSysUserMapper().findByAccount(account);
	}

	@Override
	public int save(SysUser user) {
		if(StringUtils.isEmpty(user.getPassword())){
			user.setPassword(PasswordHelper.newPassword("123456"));
		}
		return getSysUserMapper().add(user);
	}

	@Override
	public int deleteSysUser(Integer id) {

		return getSysUserMapper().deleteSysUser(id);
	}

	@Override
	public int disableSysUser(Integer[] ids) {
		try {
			Map<String, Object> map = new HashMap<String, Object>(2);
			for (Integer id : ids) {
				map.put("id", id);
				map.put("disable", Disable.DISABLE.getVal());
				getSysUserMapper().disableSysUser(map);
			}
			return 1;
		} catch (Exception e) {

		}
		return 0;
	}

	@Override
	public int unDisableSysUser(Integer[] ids) {
		try {
			Map<String, Object> map = new HashMap<String, Object>(2);
			for (Integer id : ids) {
				map.put("id", id);
				map.put("disable", Disable.UN_DISABLE.getVal());
				getSysUserMapper().disableSysUser(map);
			}
			return 1;
		} catch (Exception e) {

		}
		return 0;
	}
}
