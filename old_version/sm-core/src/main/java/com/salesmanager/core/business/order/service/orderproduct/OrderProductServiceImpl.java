package com.salesmanager.core.business.order.service.orderproduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.order.dao.orderproduct.OrderProductDao;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;

@Service("orderProductService")
public class OrderProductServiceImpl  extends SalesManagerEntityServiceImpl<Long, OrderProduct> implements OrderProductService {

	private final OrderProductDao orderProductDao;
	
	@Autowired
	public OrderProductServiceImpl(final OrderProductDao orderProductDao) {
		super(orderProductDao);
		this.orderProductDao = orderProductDao;
		
	}
	@Override
	public void saveOrUpdate(OrderProduct orderProduct) throws ServiceException {
		// TODO Auto-generated method stub
		 if(orderProduct.getId()!=null && orderProduct.getId()>0) {
	           // LOGGER.debug("Updating Order");
	            super.update(orderProduct);

	        } else {
	            //LOGGER.debug("Creating Order");
	            super.create(orderProduct);

	        }
	}
	/**
	   * 用户订单总数
	   * */
		public int getOraderByCount(Long customerId){
			return orderProductDao.getOraderByCount(customerId);
		}

}
