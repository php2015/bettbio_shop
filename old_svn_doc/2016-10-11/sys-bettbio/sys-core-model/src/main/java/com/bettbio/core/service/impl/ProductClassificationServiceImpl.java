package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.SProductClassificationMapper;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.bo.ClassifyZtreeBo;
import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.service.ProductClassificationService;

@Service
public class ProductClassificationServiceImpl extends BaseService<SProductClassification> implements ProductClassificationService{

	SProductClassificationMapper getSProductClassificationMapper(){
		return (SProductClassificationMapper)mapper;
	}
	
	@Override
	public Page<SProductClassification> selectByPage(Map<String, Object> map) {
		Page<SProductClassification> page = new Page<SProductClassification>(
				map,
				getSProductClassificationMapper().getSProductAllCount(map));
		
		page.setList(getSProductClassificationMapper().selectByPage(page.getMap()));
		return page;
	}
	
	@Override
	public int deleteClassificationList(String ids) throws Exception{
		String[] classificationId = ids.split(",");
		int[] classificationIds = new int[classificationId.length];
		for(int i=0;i<classificationId.length;i++){
			classificationIds[i] = Integer.parseInt(classificationId[i]);
		}
		return getSProductClassificationMapper().deleteClassificationList(classificationIds);
	}

	@Override
	public List<SProductClassification> selectPagebyNameOrCode(String nameOrCode, int page, int rows) throws Exception{
		page = (page-1)*rows;
		Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("nameOrCode", nameOrCode);
        params.put("page", page);
        params.put("rows", rows);
        Page<SProductClassification> pages = (Page<SProductClassification>) getSProductClassificationMapper().selectPagebyNameOrCode(params);
//        pages.setTotal(5);
//         new PageInfo<>(pages);
 //       PageHelper.startPage(page, rows);
		return null;
	}

	@Override
	public int deleteAll() throws Exception{
		return getSProductClassificationMapper().deleteAll();
	}

	@Override
	public String selectClassChild(String parentCode) throws Exception {
		return null;
	}

	@Override
	public List<ClassifyZtreeBo> selectClassByparentCode(String parentCode) throws Exception {
		
		List<ClassifyZtreeBo> classifyZtreeBoList =  getSProductClassificationMapper().selectClassByparentCode(parentCode);
		for(int i=0;i<classifyZtreeBoList.size();i++){
			ClassifyZtreeBo classifyZtreeBo = new ClassifyZtreeBo();
			classifyZtreeBo = classifyZtreeBoList.get(i);
			classifyZtreeBo.setIsParent("true");
			classifyZtreeBoList.set(i, classifyZtreeBo);
		}
		return classifyZtreeBoList;
	}

	@Override
	public String selectMaxCode(String code) {
		return getSProductClassificationMapper().selectMaxCode(code);
	}

	@Override
	public SProductClassification getClassById(int id) {
		return getSProductClassificationMapper().getClassById(id);
	}
	
	@Override
	public SProductClassification selectByCateNameAndPCode(Map<String, Object> paramMap) {
		return getSProductClassificationMapper().selectByCateNameAndPCode(paramMap);
	}
}
