package com.salesmanager.core.business.order.dao;

import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;

public interface OrderDao extends SalesManagerEntityDao<Long, Order> {
	
	OrderList listByOrderCriteria(OrderCriteria criteria);
	/**
	 * 后台获取订单数据
	 * @param criteria
	 * @return
	 */
	OrderList listStoreByOrderCriteria(OrderCriteria criteria);

	long getTotal(MerchantStore store);

	long getTotal(Customer customer);

	double getTotalMoney(OrderCriteria criteria);
	
	double getTotalMoney(Customer customer);
	
	Order getByIdByStore(Long id,Long storeID);
	public List<Object[]> queryByStoreId(Long id);
	public boolean updateByMenberPoint(Long orderid,int pointScore);
	public boolean updateBySubOrder(Long suid,Long orderid,double money1,double money2);
	public List<Object[]> queryByOrderProduct(Long id);
	public boolean updateByOrderProduct(Long pid,Double price,Double totalprice);
	//判断是否存在
	//true存在 false不存在
	public boolean exist(String phone);
	// 看某个用户下了几个单
	int getOrderByCount(Long customerId);
}