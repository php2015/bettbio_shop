package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.permission.bo.StoreUser;

public interface SStoreUserMapper extends MapperSupport<SStoreUser> {
	
	public SStoreUser adminLogin(SStoreUser sStoreUser);
	
	public SStoreUser verify(SStoreUser sStoreUser);
	
	List<SStoreUser> selectByPage(Map<String, Object> map);
	int getSStoreUserAllCount(Map<String, Object> map);
	
	public int deleteStoreUserList(int[] ids);
	
	public int deleteAll();

	//根据用户名查用户
	public StoreUser findByAccount(String account);
	//关联用户角色
	public int updateUserRoles(SStoreUser storeUser);
	
	public SStoreUser selectByPhone(SStoreUser storeUser);
	/**
	 * 校验用户名
	 * @param name
	 * @return
	 */
	int verifyStoreUserName(String name);
	
	public SStoreUser selectByStroeCode(String stroeCode);
	
	SStoreUser getStoreUserByParCode(Map<String, String> map);
}