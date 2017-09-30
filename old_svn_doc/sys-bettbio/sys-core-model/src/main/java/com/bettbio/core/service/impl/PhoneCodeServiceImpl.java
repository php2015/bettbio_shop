package com.bettbio.core.service.impl;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.PhoneCodeMapper;
import com.bettbio.core.model.PhoneCode;
import com.bettbio.core.service.PhoneCodeService;

@Service
public class PhoneCodeServiceImpl extends BaseService<PhoneCode> implements PhoneCodeService{

	public PhoneCodeMapper getPhoneCodeMapper(){
		return (PhoneCodeMapper) mapper;
	}

	@Override
	public PhoneCode invalidPhoneCode(PhoneCode phoneCode) {
		return getPhoneCodeMapper().invalidPhoneCode(phoneCode);
	}
}
