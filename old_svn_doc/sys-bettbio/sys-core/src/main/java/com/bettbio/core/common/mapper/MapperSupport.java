package com.bettbio.core.common.mapper;

import java.util.List;
import java.util.Map;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MapperSupport<T> extends Mapper<T>, MySqlMapper<T> {
	
	int selectCountByPage(Map<String, Object> map);
	
	List<T> selectByPage(Map<String, Object> map);
}