package com.bettbio.core.service;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.Exhibition;
import com.bettbio.core.model.SProductBrand;

/**
 * 展会
 * @author simon
 *
 */
public interface ExhibitionService extends IService<Exhibition> {

	/**
	 * 展会列表
	 */
public List<Exhibition>	selectListByCode(String code) throws Exception;  

	/**
	 * 展会
	 */
public Exhibition selectExhibition() throws Exception;

	/**
	 * 设置所有展会为无效
	 */
public int setExhibition() throws Exception;

/**
 * 展会列表
 * @param exhibition
 * @param page
 * @param rows
 * @return
 * @throws Exception
 */
Page<Exhibition> selectByPage(Map<String, Object> map);
}
