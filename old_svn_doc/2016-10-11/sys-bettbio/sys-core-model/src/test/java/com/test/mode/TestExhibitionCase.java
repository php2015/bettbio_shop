package com.test.mode;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.service.ExhibitionRecordService;
import com.test.mode.base.BaseT;

public class TestExhibitionCase extends BaseT{

	@Autowired
	ExhibitionRecordService  exhibitionRecordService;
	
	@Test
	public void select(){
		
	}
}
