package com.bettbio.core.service;

import java.util.List;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.BasedataType;
import com.bettbio.core.model.bo.PinYinName;

public interface BasedataTypeService extends IService<BasedataType>{

	/**
	 * 根据分类别获取认证信息
	 */
	public List<PinYinName> selectBasedataByType(String type);
}
