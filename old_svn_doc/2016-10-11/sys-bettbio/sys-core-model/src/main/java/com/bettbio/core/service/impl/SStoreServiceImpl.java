package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.SStoreMapper;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.bo.ExhibitionStoreBo;
import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.model.vo.StoreAndUserVo;
import com.bettbio.core.service.SStoreService;

@Service
public class SStoreServiceImpl extends BaseService<SStore> implements SStoreService{
	
	SStoreMapper getStoreMapper(){
		return (SStoreMapper)mapper;
	}
	
	@Override
	public Page<SStore> selectByPage(Map<String, Object> map) {
		Page<SStore> page = new Page<SStore>(
				map,
				getStoreMapper().getSStoreAllCount(map));
		page.setList(getStoreMapper().selectByPage(page.getMap()));
		return page;
	}
	
	@Override
	public int deleteStoreList(String ids) throws Exception {
		String[] sStoreId = ids.split(",");
		int[] sStoreIds = new int[sStoreId.length];
		for(int i=0;i<sStoreId.length;i++){
			sStoreIds[i] = Integer.parseInt(sStoreId[i]);
		}
		return getStoreMapper().deleteStoreList(sStoreIds);
	}

	@Override
	public int delete(SStore sStore) throws Exception {
		return getStoreMapper().delete(sStore);
	}

	@Override
	public int deleteAll() throws Exception {
		
		return getStoreMapper().deleteAll();
	}

	@Override
	public SStore selectByCode(SStore sStore) throws Exception {
		
		return getStoreMapper().selectByCode(sStore);
	}
	
	@Override
	public StoreAndUserVo getStoreAndUser(int userId) throws Exception {
		StoreAndUserVo storeAndUser = getStoreMapper().getStoreAndUser(userId);
		String address = storeAndUser.getAddress();
		if(!StringUtils.isEmpty(address)){
			String[] addresses = address.split("@");
			storeAndUser.setProvincial(addresses[0].substring(1,addresses[0].length()));
			storeAndUser.setCity(addresses[1].substring(1,addresses[1].length()));
			storeAndUser.setArea(addresses[2].substring(1,addresses[2].length()));
			storeAndUser.setAddress(addresses[3].substring(1,addresses[3].length()));
		}
		return storeAndUser;
	}

	@Override
	public List<ExhibitionStoreBo> selectStoreByExhibitionCode(String code,String name) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("code", code);
		params.put("name", name);
		return getStoreMapper().selectStoreByExhibitionCode(params);
	}

	@Override
	public int verifyStoreName(String name) {
		return getStoreMapper().verifyStoreName(name);
	}

	@Override
	public StoreAndUserVo getStoreAndDefaultUser(SStore sStore) throws Exception {
		StoreAndUserVo storeAndUser = getStoreMapper().getStoreAndDefaultUser(sStore);
		String address = storeAndUser.getAddress();
		if(!StringUtils.isEmpty(address)){
			String[] addresses = address.split("@");
			storeAndUser.setProvincial(addresses[0].substring(1,addresses[0].length()));
			storeAndUser.setCity(addresses[1].substring(1,addresses[1].length()));
			storeAndUser.setArea(addresses[2].substring(1,addresses[2].length()));
			storeAndUser.setAddress(addresses[3].substring(1,addresses[3].length()));
		}
		return storeAndUser;
	}

	@Override
	public List<PinYinName> storeList() {
		return getStoreMapper().storeList();
	}
}
