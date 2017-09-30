package com.bettbio.core.service;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.ExhibitionRecord;

public interface ExhibitionRecordService extends IService<ExhibitionRecord>{

	/**
	 * 根据
	 */
	public ExhibitionRecord selectRecordByCode(ExhibitionRecord exhibitionRecord) throws Exception;
	
	
}
