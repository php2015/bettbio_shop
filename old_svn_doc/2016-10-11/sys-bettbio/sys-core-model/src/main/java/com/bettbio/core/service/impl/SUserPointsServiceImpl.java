package com.bettbio.core.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.SUserPointsMapper;
import com.bettbio.core.model.SUserPoints;
import com.bettbio.core.service.SUserPointsService;

@Service
public class SUserPointsServiceImpl extends BaseService<SUserPoints> implements SUserPointsService {
	SUserPointsMapper getSUserPointsMapper() {
		return (SUserPointsMapper) mapper;
	}

	@Override
	public Page<SUserPoints> selectByPage(Map<String, Object> map) {
		Page<SUserPoints> pageSUserPoints = new Page<SUserPoints>(map, getSUserPointsMapper().getUserPointsCount(map));
		pageSUserPoints.setList(getSUserPointsMapper().selectByPage(pageSUserPoints.getMap()));
		return pageSUserPoints;
	}
}
