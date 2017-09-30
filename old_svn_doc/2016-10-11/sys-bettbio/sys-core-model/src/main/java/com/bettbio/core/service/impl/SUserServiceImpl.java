package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.mapper.SUserMapper;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.SUser;
import com.bettbio.core.service.SUserService;

@Service
public class SUserServiceImpl extends BaseService<SUser> implements SUserService {

	SUserMapper getSUserMapper() {
		return (SUserMapper) mapper;
	}

	@Override
	public SUser login(SUser sUser) {
		sUser.setPassword(PasswordHelper.newPassword(sUser.getPassword()));
		return getSUserMapper().login(sUser);
	}

	@Override
	public String verifyPhone(String phone) {

		return getSUserMapper().verifyPhone(phone);
	}

	@Override
	public SUser selectByPhone(SUser sUser) {
		return getSUserMapper().selectByPhone(sUser);
	}

	@Override
	public String verifyEmail(String email) {
		return getSUserMapper().verifyEmail(email);
	}
	
	@Override
	public int deleteStoreList(String ids) throws Exception {

		return 0;
	}

	@Override
	public void updateUserPoints(SUser sUser) {
		getSUserMapper().updateUserPoints(sUser);
	}

	@Override
	public List<String> selectUserEmails(Integer[] ids) {
		Map<String, Object> map = new HashMap<String,Object>(2);
		map.put("ids", ids);
		return getSUserMapper().selectUserEmail(map);
	}

	@Override
	public SUser fastLogin(SUser sUser) {
		return getSUserMapper().fastLogin(sUser);
	}
	
	@Override
	public int updatePhoneCode(SUser sUser) {
		return getSUserMapper().updatePhoneCode(sUser);
	}
}
