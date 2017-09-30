package com.salesmanager.core.business.order.dao.orderproduct;



import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;

@Repository("orderProductDao")
public class OrderProductDaoImpl extends SalesManagerEntityDaoImpl<Long, OrderProduct> implements OrderProductDao{

	
	public OrderProductDaoImpl() {
		super();
	}
	/**
	 * 获取个人订单价格的总记录
	 * */
	@Override
	public int getOraderByCount(Long customterid){
		StringBuffer countBuilderSelect = new StringBuffer();
		countBuilderSelect.append("select count(o) from Order as o where o.customerId="+customterid+"");
		
		//add where
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString());
		
		Number count = (Number) countQ.getSingleResult ();
		return count.intValue();
	}
}
