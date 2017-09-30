package com.bettbio.core.service.manager;


import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SUser;

public interface CustomerService extends IService<SUser> {

	/**
	 * 重置密码
	 * 
	 * @param ids
	 * @return
	 */
	public int resetPwd(Integer[] ids);
	
	/**
	 * 设置用户等级
	 * @param user
	 * @return
	 */
	public int setGrade(SUser user);
	
	/**
	 * 激活-
	 * @param ids
	 * @return
	 */
	public int activate(Integer[] ids);
	

	/**
	 * 冻结
	 * @param ids
	 * @return
	 */
	public int unactivate(Integer[] ids);
}
