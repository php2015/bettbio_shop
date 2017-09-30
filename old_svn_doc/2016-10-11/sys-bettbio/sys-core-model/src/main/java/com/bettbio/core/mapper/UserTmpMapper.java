package com.bettbio.core.mapper;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.UserTmp;

public interface UserTmpMapper extends MapperSupport<UserTmp>{
	UserTmp getUserTmpByCode(String code);
}
