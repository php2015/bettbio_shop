package com.bettbio.core.service.manager.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.mapper.SUserMapper;
import com.bettbio.core.model.SUser;
import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.manager.CustomerService;

@Service
public class CustomerServiceImpl extends BaseService<SUser> implements CustomerService {

	@Autowired
	SUserService sUserService;

	private SUserMapper getSUserMapper() {
		return (SUserMapper) mapper;
	}

	@Override
	public Page<SUser> selectByPage(Map<String, Object> map) {
		Page<SUser> page = new Page<SUser>(map, getSUserMapper().selectCountByPage(map));

		page.setList(getSUserMapper().selectByPage(page.getMap()));
		return page;
	}

	@Override
	public int resetPwd(Integer[] ids) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		try {
			for (int id : ids) {
				map.put("id", id);
				map.put("password", PasswordHelper.newPassword("123456"));
				if (getSUserMapper().updatePwd(map) > 1) {
					// 发邮件
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int setGrade(SUser user) {

		return getSUserMapper().updateGrade(user);
	}

	@Override
	public int activate(Integer[] ids) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		try {
			for (int id : ids) {
				map.put("id", id);
				map.put("status", 1);
				if (getSUserMapper().updateActivate(map) > 0) {

				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int unactivate(Integer[] ids) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		try {
			for (int id : ids) {
				map.put("id", id);
				map.put("status", 0);
				if (getSUserMapper().updateActivate(map) > 0) {

				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
