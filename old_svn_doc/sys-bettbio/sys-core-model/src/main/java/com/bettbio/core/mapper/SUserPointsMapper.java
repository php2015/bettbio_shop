package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SUserPoints;

public interface SUserPointsMapper extends MapperSupport<SUserPoints> {

	List<SUserPoints> selectByPage(Map<String, Object> map);

	int getUserPointsCount(Map<String, Object> map);
}