package com.bettbio.core.service;

import java.util.List;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.UserGrade;

public interface UserGradeService extends IService<UserGrade>{
	
	Integer USER_GREAD = 0;
	
	List<UserGrade> selectGradeByType(Integer type);
	
	/**
	 * 根据等级查询值
	 * @param gread
	 * @return
	 */
	Double selectValue(Integer gread);
}
