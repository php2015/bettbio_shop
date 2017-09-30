package com.bettbio.core.service.impl;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.ExhibitionRecordMapper;
import com.bettbio.core.model.ExhibitionRecord;
import com.bettbio.core.service.ExhibitionRecordService;

@Service
public class ExhibitionRecordServiceImpl extends BaseService<ExhibitionRecord> implements ExhibitionRecordService{

	ExhibitionRecordMapper getExhibitionRecordMapper(){
		return (ExhibitionRecordMapper)mapper;
	}
	@Override
	public ExhibitionRecord selectRecordByCode(ExhibitionRecord exhibitionRecord) throws Exception {
		return getExhibitionRecordMapper().selectRecordByCode(exhibitionRecord);
	}

}
