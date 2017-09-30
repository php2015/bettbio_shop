package com.test.mode;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.service.BasedataTypeService;
import com.test.mode.base.BaseT;

public class TestBasedataTypeCase extends BaseT{
@Autowired
BasedataTypeService basedataTypeService;
	@Test
	public void select(){
	List<PinYinName> BasedataTypeList = basedataTypeService.selectBasedataByType("BTYPE_PROOF_CONSUMABLE");
	}
}
