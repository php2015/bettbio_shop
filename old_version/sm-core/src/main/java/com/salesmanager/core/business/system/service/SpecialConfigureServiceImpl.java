package com.salesmanager.core.business.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.system.dao.SpecialConfigurationDao;
import com.salesmanager.core.business.system.model.SpecialConfiguration;

@Service("sepcialConfigureService")
public class SpecialConfigureServiceImpl implements
		SepcialConfigureService {

	@Autowired
	private SpecialConfigurationDao specialConfigurationDao;
	
	@Override
	public void saveConfigure(Map<String, Object> config) {
		for(String key: config.keySet()){
			SpecialConfiguration oldData = specialConfigurationDao.getByKey(key);
			if (oldData == null){
				oldData = new SpecialConfiguration();
				oldData.setKey(key);
				oldData.setValue(String.valueOf(config.get(key)));
				specialConfigurationDao.save(oldData);
			}else{
				oldData.setValue(String.valueOf(config.get(key)));
				specialConfigurationDao.update(oldData);
			}
		}
	}
	

	@Override
	public String getStringCfg(String key) {
		Object obj = getCfg(key);
		if (obj == null){
			return null;
		}
		return String.valueOf(obj);
	}

	@Override
	public boolean getBooleanCfg(String key) {
		Object obj = getCfg(key);
		if (obj == null){
			return false;
		}
		if (obj instanceof Boolean){
			return ((Boolean) obj).booleanValue();
		}
		if (obj instanceof String){
			return Boolean.parseBoolean((String) obj);
		}
		if (obj instanceof Number){
			return ((Number) obj).intValue() != 0;
		}
		return false;
	}

	@Override
	public Long getLongCfg(String key) {
		Object obj = getCfg(key);
		if (obj == null){
			return null;
		}
		if (obj instanceof Boolean){
			return ((Boolean) obj).booleanValue() ? 1L: 0;
		}
		if (obj instanceof String){
			try{
				return Long.parseLong((String) obj);
			}catch(Exception e){
				return null;
			}
		}
		if (obj instanceof Number){
			return ((Number) obj).longValue();
		}
		return null;
	}
	
	@Override
	public Double getDoubleCfg(String key) {
		Object obj = getCfg(key);
		if (obj == null){
			return null;
		}
		if (obj instanceof Boolean){
			return ((Boolean) obj).booleanValue() ? 1d: 0;
		}
		if (obj instanceof String){
			try{
				return Double.parseDouble((String) obj);
			}catch(Exception e){
				return null;
			}
		}
		if (obj instanceof Number){
			return ((Number) obj).doubleValue();
		}
		return null;
	}

	@Override
	public Object getCfg(String key) {
		SpecialConfiguration rst = specialConfigurationDao.getByKey(key);
		if (rst == null){
			return null;
		}
		return rst.getValue();
	}


	@Override
	public Map<String, Object> getAllConfigure() {
		List<SpecialConfiguration> list = specialConfigurationDao.list();
		Map<String, Object> result = new HashMap<String, Object>();
		for(SpecialConfiguration cfg : list){
			result.put(cfg.getKey(), cfg.getValue());
		}
		return result;
	}

	
}
