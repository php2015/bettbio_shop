package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SOrder;
import com.bettbio.core.model.vo.OrderVo;

public interface SOrderMapper extends MapperSupport<SOrder> {
	/**
	 * 查询订单总数
	 * @param map
	 * @return
	 */
	int orderCount(Map<String,Object> map);
	
	/**
	 * 查询订单列表
	 * @param map
	 * @return
	 */
	List<OrderVo> selectOrders(Map<String,Object> map);
	/**
	 * 根据编号查询发货人姓名
	 * @param code
	 * @return
	 */
	String selectUserNameByCode(String code);
	/**
	 * 根据订单编号查询订单
	 * @param code
	 * @return
	 */
	SOrder selectOrderByCode(String code);
}