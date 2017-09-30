package com.bettbio.core.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.UserGradeMapper;
import com.bettbio.core.model.UserGrade;
import com.bettbio.core.service.UserGradeService;

@Service
public class UserGradeServiceImpl extends BaseService<UserGrade> implements UserGradeService {

	
	public UserGradeMapper getUserGradeMapper(){
		return (UserGradeMapper) mapper;
	}
	
	@Override
	public List<UserGrade> selectGradeByType(Integer type) {
		
		return getUserGradeMapper().selectGradeByType(type);
	}

	@Override
	public Double selectValue(Integer gread) {
		UserGrade record=new UserGrade();
		record.setLv(gread);
		record.setType(0);
		record=getUserGradeMapper().selectOne(record);
		return Double.parseDouble(record.getValue());
	}

}
