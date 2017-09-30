package com.bettbio.core.service;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.bo.PinYinName;

/**
 *	品牌 
 * @author simon
 *
 */
public interface ProductBrandService extends IService<SProductBrand>{

	/**
	 * 根据商铺编号获取品牌
	 */
	List<SProductBrand> getSProductBrandByCode(String code) throws Exception;
	
	/**
	 * 品牌清单分页列表
	 */
	Page<SProductBrand> selectByPage(Map<String, Object> map);
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	int deleteBrandList(String ids) throws Exception;
	
	/**
	 * 删除全部			
	 */
	int deleteAll() throws Exception;
	
	/**
	 * 根据名称或者代码列表查询				
	 */
	List<SProductBrand> selectPagebyNameOrCode(String nameOrCode,int page,int rows) throws Exception;
	
	/**
	 * 产品分类下拉
	 * @return
	 */
	List<PinYinName> brandList();

	/**
	 * 根据品牌名称查询品牌
	 * @param brandName
	 * @return
	 */
	SProductBrand selectByBrandName(String brandName);
}
