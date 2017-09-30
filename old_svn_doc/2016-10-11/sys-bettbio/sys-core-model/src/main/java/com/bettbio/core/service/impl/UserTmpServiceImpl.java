package com.bettbio.core.service.impl;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.UserTmpMapper;
import com.bettbio.core.model.UserTmp;
import com.bettbio.core.service.UserTmpService;

@Service
public class UserTmpServiceImpl extends BaseService<UserTmp> implements UserTmpService {
	
	public UserTmpMapper getUserTmpMapper(){
		return (UserTmpMapper) mapper;
	}
	
	@Override
	public UserTmp getUserTmpByCode(String code) {
		return getUserTmpMapper().getUserTmpByCode(code);
	}

}
