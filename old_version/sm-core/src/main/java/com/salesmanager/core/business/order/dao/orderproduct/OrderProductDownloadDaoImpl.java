package com.salesmanager.core.business.order.dao.orderproduct;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.QOrder;
import com.salesmanager.core.business.order.model.QSubOrder;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductDownload;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProductDownload;

@Repository("orderProductDownloadDao")
public class OrderProductDownloadDaoImpl<qOrderProductDownload> extends SalesManagerEntityDaoImpl<Long, OrderProductDownload> implements OrderProductDownloadDao{

	@Override
	public OrderProductDownload getById(Long id) {
		
		QOrderProductDownload qOrderProductDownload = QOrderProductDownload.orderProductDownload;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		QOrder qOrder = QOrder.order;
		QSubOrder qSubOrder = QSubOrder.subOrder;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qOrderProductDownload)
			.leftJoin(qOrderProductDownload.orderProduct, qOrderProduct).fetch()
			.leftJoin(qOrderProduct.subOrder, qSubOrder).fetch()
			.leftJoin(qSubOrder.order, qOrder).fetch()
			.leftJoin(qSubOrder.merchant).fetch()
			.where(qOrderProductDownload.id.eq(id));
		
		List<OrderProductDownload> ms = query.list(qOrderProductDownload);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}		
		
		
	}
	
	@Override
	public List<OrderProductDownload> getByOrderId(Long orderId) {
		
		QOrderProductDownload qOrderProductDownload = QOrderProductDownload.orderProductDownload;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		QOrder qOrder = QOrder.order;
		QSubOrder qSubOrder = QSubOrder.subOrder;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qOrderProductDownload)
			.leftJoin(qOrderProductDownload.orderProduct, qOrderProduct).fetch()
			.leftJoin(qOrderProduct.subOrder, qSubOrder).fetch()
			.leftJoin(qSubOrder.order, qOrder).fetch()
			.leftJoin(qSubOrder.merchant).fetch()
			.where(qOrder.id.eq(orderId));

		
		return query.list(qOrderProductDownload);
		
	}
	
}
