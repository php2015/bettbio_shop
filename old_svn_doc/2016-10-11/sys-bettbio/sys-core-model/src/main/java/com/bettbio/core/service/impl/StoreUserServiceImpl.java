package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.mapper.SStoreMapper;
import com.bettbio.core.mapper.SStoreUserMapper;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.service.StoreUserService;

@Service
public class StoreUserServiceImpl extends BaseService<SStoreUser> implements StoreUserService {

	SStoreUserMapper getStoreUserMapper() {
		return (SStoreUserMapper) mapper;
	}

	@Autowired
	SStoreMapper sStoreMapper;

	@Override
	public SStoreUser adminLogin(SStoreUser sStoreUser) {
		sStoreUser.setPassword(PasswordHelper.newPassword(sStoreUser.getPassword()));
		return getStoreUserMapper().adminLogin(sStoreUser);
	}

	@Override
	public SStoreUser verify(SStoreUser sStoreUser) {
		return getStoreUserMapper().verify(sStoreUser);
	}

	@Override
	public Page<SStoreUser> selectByPage(Map<String, Object> map) {
		Page<SStoreUser> page = new Page<SStoreUser>(map, getStoreUserMapper().getSStoreUserAllCount(map));
		page.setList(getStoreUserMapper().selectByPage(page.getMap()));
		return page;

	}

	@Override
	public int deleteStoreUserList(String ids) throws Exception {
		String[] sStoreUserId = ids.split(",");
		int[] sStoreUserIds = new int[sStoreUserId.length];
		for (int i = 0; i < sStoreUserId.length; i++) {
			sStoreUserIds[i] = Integer.parseInt(sStoreUserId[i]);
		}
		return getStoreUserMapper().deleteStoreUserList(sStoreUserIds);
	}

	@Override
	public int delstoreAll() throws Exception {
		return getStoreUserMapper().deleteAll();
	}

	@Override
	public SStoreUser selectByPhone(SStoreUser sStoreUser) {
		return getStoreUserMapper().selectByPhone(sStoreUser);
	}

	@Override
	public int createStore(SStoreUser sStoreUser, SStore store) {
		if (sStoreMapper.insert(store) == 1) {
			return getStoreUserMapper().insert(sStoreUser) == 1 ? 1 : 0;
		}
		return 0;
	}

	@Override
	public int verifyStoreUserName(String name) {
		return getStoreUserMapper().verifyStoreUserName(name);
	}
	
	@Override
	public SStoreUser selectByStroeCode(String stroeCode) {
		return getStoreUserMapper().selectByStroeCode(stroeCode);
	}

	@Override
	public SStoreUser getStoreUserByParCode(String parentCode,String storeCode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentCode", parentCode);
		map.put("storeCode", storeCode);
		return getStoreUserMapper().getStoreUserByParCode(map);
	}
}
