package com.bettbio.core.service;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;

public interface StoreUserService extends IService<SStoreUser> {

	/**
	 * 卖家用户登录
	 * 
	 * @param sStoreUser
	 * @return
	 */
	SStoreUser adminLogin(SStoreUser sStoreUser);

	/**
	 * 验证
	 * 
	 * @param phone
	 * @return
	 */
	SStoreUser verify(SStoreUser sStoreUser);

	/**
	 * 列表分页查询
	 * 
	 * @param map
	 * @return
	 */
	Page<SStoreUser> selectByPage(Map<String, Object> map);

	/**
	 * 批量删除
	 */
	int deleteStoreUserList(String ids) throws Exception;

	/**
	 * 全部删除
	 * 
	 * @return
	 * @throws Exception
	 */
	int delstoreAll() throws Exception;

	/**
	 * 通过手机号查找用户
	 */
	SStoreUser selectByPhone(SStoreUser sStoreUser);

	/**
	 * 通过stroeCode获取默认联系人
	 * @param sStoreUser
	 * @param store
	 * @return
	 */
	SStoreUser selectByStroeCode(String stroeCode);
	
	int createStore(SStoreUser sStoreUser, SStore store);
	
	/**
	 * 校验用户名
	 * @param name
	 * @return
	 */
	int verifyStoreUserName(String name);
	
	/*
	 * 根据parentCode获取用户
	 */
	SStoreUser getStoreUserByParCode(String parentCode,String storeCode);
	
}
