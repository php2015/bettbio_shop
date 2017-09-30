package com.bettbio.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.ExhibitionMapper;
import com.bettbio.core.model.Exhibition;
import com.bettbio.core.service.ExhibitionService;


import tk.mybatis.mapper.entity.Example;

@Service
public class ExhibitionServiceImpl extends BaseService<Exhibition> implements ExhibitionService {
	
	ExhibitionMapper getExhibitionMapper(){
		return (ExhibitionMapper)mapper;
	}
	
	@Override
	public List<Exhibition> selectListByCode(String code) throws Exception {
		return getExhibitionMapper().selectListByCode(code);
	}

	@Override
	public Exhibition selectExhibition() throws Exception {
		return getExhibitionMapper().selectExhibition();
	}

	@Override
	public int setExhibition() throws Exception {
		return getExhibitionMapper().setExhibition();
	}

	
	@Override
	public Page<Exhibition> selectByPage(Map<String, Object> map) {
		Page<Exhibition> page = new Page<Exhibition>(
				map,
				getExhibitionMapper().getExhibitionAllCount(map));
		page.setList(getExhibitionMapper().selectByPage(page.getMap()));
		return page;

	}
	
}
