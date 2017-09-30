package com.bettbio.core.mapper;

import java.util.List;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.UserGrade;

public interface UserGradeMapper extends MapperSupport<UserGrade> {

	List<UserGrade> selectGradeByType(Integer type);
}
