package com.bettbio.core.mapper;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.PhoneCode;

public interface PhoneCodeMapper extends MapperSupport<PhoneCode>{

	PhoneCode invalidPhoneCode(PhoneCode phoneCode);
}
