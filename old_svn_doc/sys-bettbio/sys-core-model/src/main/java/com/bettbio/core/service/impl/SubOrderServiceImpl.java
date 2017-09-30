package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.SOrderDetailsMapper;
import com.bettbio.core.mapper.SOrderMapper;
import com.bettbio.core.mapper.SSubOrderMapper;
import com.bettbio.core.model.SOrder;
import com.bettbio.core.model.SOrderDatails;
import com.bettbio.core.model.SSubOrder;
import com.bettbio.core.model.vo.SubOrderVo;
import com.bettbio.core.service.SubOrderService;

@Service
public class SubOrderServiceImpl extends BaseService<SSubOrder> implements SubOrderService {

	SSubOrderMapper getSOrderMapper() {
		return (SSubOrderMapper) mapper;
	}

	@Autowired
	SOrderDetailsMapper sOrderDetailsMapper;
	@Autowired
	SOrderMapper sOrderMapper;

	@Override
	public Page<SubOrderVo> selectSubOrdersByMap(Map<String, Object> map) {
		Page<SubOrderVo> pageInfo = new Page<SubOrderVo>(map, getSOrderMapper().selectCountByMap(map));
		List<SubOrderVo> subOrderVos = getSOrderMapper().selectSubOrdersByMap(pageInfo.getMap());
		for (SubOrderVo subOrderVo : subOrderVos) {
			SOrderDatails sOrderDatails = new SOrderDatails();
			sOrderDatails.setSubOrderCode(subOrderVo.getOrderCode());
			List<SOrderDatails> orderDatails = sOrderDetailsMapper.select(sOrderDatails);
			subOrderVo.setOrderDatails(orderDatails);
			subOrderVo.setUserName(sOrderMapper.selectUserNameByCode(subOrderVo.getParentOrderCode()));
		}
		pageInfo.setList(subOrderVos);
		return pageInfo;
	}
	
	@Override
	public void updateOrderState(String orderState, String orderCode) {
		getSOrderMapper().updateOrderState(orderState, orderCode);
	}
	
	
	@Override
	public Map<String, Object> selectSubOrderByCode(String code) {
		Map<String,Object> map=new HashMap<String,Object>();
		SubOrderVo subOrder=getSOrderMapper().selectSubOrderByCode(code);
		SOrderDatails sOrderDatails = new SOrderDatails();
		sOrderDatails.setSubOrderCode(subOrder.getOrderCode());
		subOrder.setOrderDatails(sOrderDetailsMapper.select(sOrderDatails));
		map.put("subOrder", subOrder);
		SOrder sOrder=sOrderMapper.selectOrderByCode(subOrder.getParentOrderCode());
		map.put("order", sOrder);
		return map;
	}

	@Override
	public int selectOrderCountByCode(String code) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("storeCode", code);
		return getSOrderMapper().selectCountByMap(map);
	}

	@Override
	public Double selectCountPriceByCode(String code) {
		return getSOrderMapper().selectCountPriceByCode(code);
	}
	
	

}
