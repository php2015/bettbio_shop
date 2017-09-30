package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.bo.ExhibitionStoreBo;
import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.model.vo.StoreAndUserVo;

public interface SStoreMapper extends MapperSupport<SStore> {
	
	List<SStore> selectByPage(Map<String, Object> map);
	int getSStoreAllCount(Map<String, Object> map);
	
	int deleteStoreList(int[] sStoreIds);
	
	int delete(SStore sStore);
	
	int deleteAll();
	
	SStore selectByCode(SStore sStore);
	
	StoreAndUserVo getStoreAndUser(int userId);
	
	StoreAndUserVo getStoreAndDefaultUser(SStore sStore);
	
	List<ExhibitionStoreBo> selectStoreByExhibitionCode(Map<String, Object> params);
	/**
	 * 校验商家名称
	 * @param name
	 * @return
	 */
	int verifyStoreName(String name);
	
	List<PinYinName> storeList();

}