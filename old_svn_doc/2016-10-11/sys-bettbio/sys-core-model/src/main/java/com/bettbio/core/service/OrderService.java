package com.bettbio.core.service;

import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.model.SOrder;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.model.vo.OrderVo;
import com.bettbio.core.model.vo.ShoppingCartVo;
import com.bettbio.core.mongo.model.ShoppingCartList;

/**
 * 订单
 * @author simon
 *
 */
public interface OrderService extends IService<SOrder>{
	/**
	 * 创建订单
	 */
	String createOrder(SUserAddress userAddress,InvoiceInfo invoiceInfo,ShoppingCartList shoppingCartList,String userCode,ShoppingCartVo shoppingCartVo);
	
	/**
	 * 根据用户code获取订单集合
	 * @param userCode
	 * @return
	 */
	Page<OrderVo> selectOrders(Map<String,Object> map);
	
}
