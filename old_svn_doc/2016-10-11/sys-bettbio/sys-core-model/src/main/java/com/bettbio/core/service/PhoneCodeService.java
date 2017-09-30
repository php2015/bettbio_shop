package com.bettbio.core.service;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.PhoneCode;

public interface PhoneCodeService  extends IService<PhoneCode>{

	/**
	 * 验证码校验
	 * @param phoneCode
	 * 	phone 
	 * 	code
	 * @return null or not null
	 *  if not null  judge is_invalid field
	 */
	PhoneCode invalidPhoneCode(PhoneCode phoneCode);
}
