package com.bettbio.core.service;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.bo.ExhibitionStoreBo;
import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.model.vo.StoreAndUserVo;

public interface SStoreService extends IService<SStore>{

	/**
	 * 列表分页查询
	 * @param map
	 * @return
	 */
	 Page<SStore> selectByPage(Map<String, Object> map);
	 
	 /**
	  * 单个删除
	  */
	 int delete (SStore sStore)throws Exception;
	 
	 /**
	 * 批量删除
	 */
	int deleteStoreList(String ids)throws Exception;
	
	/**
	 * 删除全部
	 */
	int deleteAll() throws Exception;
	
	/**
	 * 通过code获取商铺
	 * @param sStore
	 * @return
	 * @throws Exception
	 */
	SStore selectByCode(SStore sStore)throws Exception;
	
	/**
	 * 获取StoreAndUser
	 * @param sStore
	 * @return
	 * @throws Exception
	 */
	StoreAndUserVo getStoreAndUser(int userId)throws Exception;
	
	StoreAndUserVo getStoreAndDefaultUser(SStore sStore)throws Exception;
	
	/**
	 * 通过展会code查询参展商家
	 *  @param code 展会编码
	 *  @param name 参展商家
	 */
	List<ExhibitionStoreBo> selectStoreByExhibitionCode(String code,String name) throws Exception;
	/**
	 * 校验商家名称
	 * @param name
	 * @return
	 */
	int verifyStoreName(String name);
	
	/**
	 * 商家下拉框
	 */
	List<PinYinName> storeList();
}
