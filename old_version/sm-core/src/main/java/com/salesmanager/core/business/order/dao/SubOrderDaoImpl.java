package com.salesmanager.core.business.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.QSubOrder;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProduct;

@Repository("subOrderDao")
public class SubOrderDaoImpl  extends SalesManagerEntityDaoImpl<Long, SubOrder> implements SubOrderDao{
	public SubOrderDaoImpl() {
		super();
	}
	
	@Override
	public SubOrder getSubOrderById(long id) {
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		QSubOrder qSubOrder = QSubOrder.subOrder;
		JPQLQuery query = new JPAQuery (getEntityManager());		
		query.from(qSubOrder)			
			.join(qSubOrder.orderProducts, qOrderProduct).fetch()			
			//.leftJoin(qOrderProduct.prices).fetch()
			.where(qSubOrder.id.eq(id));
		
		List<SubOrder> ms = query.list(qSubOrder);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}		
		
	}

}
