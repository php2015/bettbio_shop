package com.bettbio.core.service;

import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SUserPoints;

/**
 * 用户积分
 * 
 * @author chang
 *
 */
public interface SUserPointsService extends IService<SUserPoints> {
	
	/**
	 * 列表分页查询
	 * @param map
	 * @return
	 */
	public Page<SUserPoints> selectByPage(Map<String, Object> map);
}
