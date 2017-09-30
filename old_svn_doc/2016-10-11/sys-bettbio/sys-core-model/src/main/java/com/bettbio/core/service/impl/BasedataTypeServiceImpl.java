package com.bettbio.core.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.BasedataTypeMapper;
import com.bettbio.core.model.BasedataType;
import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.service.BasedataTypeService;

@Service
public class BasedataTypeServiceImpl extends BaseService<BasedataType> implements BasedataTypeService{

	BasedataTypeMapper getBasedataTypeMapper(){
		return (BasedataTypeMapper)mapper;
	}
	
	@Override
	public List<PinYinName> selectBasedataByType(String type) {
		return getBasedataTypeMapper().selectBasedataByType(type);
	}

}
