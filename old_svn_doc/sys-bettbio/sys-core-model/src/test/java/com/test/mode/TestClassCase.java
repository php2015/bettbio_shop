package com.test.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.bo.ClassifyZtreeBo;
import com.bettbio.core.service.ProductClassificationService;
import com.test.mode.base.BaseT;

public class TestClassCase extends BaseT{

	@Autowired
	ProductClassificationService productClassificationService;
	
	@Test
	public void ztree(){
		List<ClassifyZtreeBo> classifyZtreeBo = new ArrayList<ClassifyZtreeBo>();
		try {
			//classifyZtreeBo = productClassificationService.selectClassChild("0");
			 classifyZtreeBo = productClassificationService.selectClassByparentCode("0");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void class1(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page",2);
		map.put("row",10);
		Page<SProductClassification> page = productClassificationService.selectByPage(map);
	}
}
