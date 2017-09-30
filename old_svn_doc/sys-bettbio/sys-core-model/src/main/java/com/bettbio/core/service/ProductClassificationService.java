package com.bettbio.core.service;

import java.util.List;
import java.util.Map;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.bo.ClassifyZtreeBo;

/**
 * 分类
 * @author simon
 *
 */
public interface ProductClassificationService extends IService<SProductClassification> {
	
	/**
	 * 产品分类列表
	 */
	Page<SProductClassification> selectByPage(Map<String, Object> map);
	
	/**
	 * 批量删除
	 */
	int deleteClassificationList(String ids) throws Exception;
	
	/**
	 * 删除全部
	 */
	int deleteAll() throws Exception;
	
	/**
	 * 根据名称或者代码列表查询				
	 */
	List<SProductClassification> selectPagebyNameOrCode(String nameOrCode, int page, int rows) throws Exception;

	/**
	 * 加载分类树
	 * @return
	 * @throws Exception
	 */
	String selectClassChild(String parentCode)throws Exception;
	
	/**
	 * 加载分类树
	 */
	public List<ClassifyZtreeBo> selectClassByparentCode(String parentCode)throws Exception;

	/**
	 * 查询最大code
	 */
	public String selectMaxCode(String code);
}
