package com.bettbio.core.service;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.UserTmp;

public interface UserTmpService extends IService<UserTmp>{

	/**
	 * 通过code获取
	 */
	public UserTmp getUserTmpByCode(String code);
}
