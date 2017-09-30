package com.bettbio.core.mapper;

import java.util.List;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.BasedataType;
import com.bettbio.core.model.bo.PinYinName;

public interface BasedataTypeMapper extends MapperSupport<BasedataType>{

	List<PinYinName> selectBasedataByType(String type);
}
