package com.bettbio.core.mapper;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.ExhibitionRecord;

public interface ExhibitionRecordMapper extends MapperSupport<ExhibitionRecord> {
	
	public ExhibitionRecord selectRecordByCode(ExhibitionRecord exhibitionRecord);
	
}