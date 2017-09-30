package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.Exhibition;

public interface ExhibitionMapper extends MapperSupport<Exhibition> {
	
	public List<Exhibition> selectListByCode(String code);
	
	public Exhibition selectExhibition();
	
	public int setExhibition();
	
	List<Exhibition> selectByPage(Map<String, Object> map);
	int getExhibitionAllCount(Map<String, Object> map);
}