package com.bettbio.core.mapper;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SysJurisdiction;
import com.bettbio.core.model.permission.bo.Resource;

public interface SysJurisdictionMapper extends MapperSupport<SysJurisdiction> {
	Resource findResourceById(Integer id);
}