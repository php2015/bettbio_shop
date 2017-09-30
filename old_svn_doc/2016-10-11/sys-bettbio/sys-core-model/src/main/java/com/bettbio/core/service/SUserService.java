package com.bettbio.core.service;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.SUser;

public interface SUserService extends IService<SUser> {

	/**
	 * 买家用户登录
	 * 
	 * @param sUser
	 * @return
	 */
	SUser login(SUser sUser);

	/**
	 * 验证手机号
	 * 
	 * @param phone
	 * @return
	 */
	String verifyPhone(String phone);

	/**
	 * 通过手机号查找用户
	 */
	SUser selectByPhone(SUser sUser);

	/**
	 * 验证油箱
	 * 
	 * @param phone
	 * @return
	 */
	String verifyEmail(String email);
	

	 /**
	 * 批量删除
	 */
	int deleteStoreList(String ids) throws Exception;


	/**
	 * 更新用户积分
	 * 
	 * @param sUser
	 */
	void updateUserPoints(SUser sUser);
	
	
	/**
	 * 查询用户邮箱
	 * @param ids
	 * @return
	 */
	List<String> selectUserEmails(Integer [] ids);

	/**
	 * 快速登录
	 * @param sUser
	 * @return
	 */
	SUser fastLogin(SUser sUser);

	int updatePhoneCode(SUser sUser); //更新手机验证码
}
