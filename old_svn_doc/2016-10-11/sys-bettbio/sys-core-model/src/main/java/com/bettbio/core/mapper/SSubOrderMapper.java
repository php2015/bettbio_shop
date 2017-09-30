package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SSubOrder;
import com.bettbio.core.model.vo.SubOrderVo;

public interface SSubOrderMapper extends MapperSupport<SSubOrder> {
	
	/**
	 * 根据父订单编号查找所有子订单编号
	 * @param preantCode
	 * @return
	 */
	List<SubOrderVo> selectSubOrders(String preantCode);
	/**
	 * 分页获取子订单集合
	 * @param map
	 * @return
	 */
	List<SubOrderVo> selectSubOrdersByMap(Map<String,Object> map);
	/**
	 * 获取总数
	 * @param map
	 * @return
	 */
	int selectCountByMap(Map<String,Object> map);
	
	/**
	 * 更新订单状态
	 * @param orderState
	 * @param orderCode
	 */
	void updateOrderState(@Param("orderState")String orderState,@Param("orderCode")String orderCode);
	/**
	 * 根据订单号获取订单详情
	 * @param orderCode
	 * @return
	 */
	SubOrderVo selectSubOrderByCode(String orderCode);
	
	/**
	 * 获取商家总价
	 * @param storeCode
	 * @return
	 */
	Double selectCountPriceByCode(String storeCode);
	
}