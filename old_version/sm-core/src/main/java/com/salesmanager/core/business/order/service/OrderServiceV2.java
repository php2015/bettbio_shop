package com.salesmanager.core.business.order.service;

import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.exception.ServiceExceptionV2;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.user.model.User;

public interface OrderServiceV2 extends SalesManagerEntityService<Long, Order> {

	void commitOrder(OrderServiceV2Context orderContext, ShoppingCart cart, Long delieveryAddressId, Long invoicId,
			Long invoiceAddressId) throws ServiceExceptionV2, ServiceException;

	// 查询某个用户有几个订单
	int getOrderByCount(Long customerId);

	// 查询当前用户对某个子订单有哪些操作
	List<String> getActionList(OrderServiceV2Context orderContext, SubOrder subOrder, boolean isAdminUser);

	void spiltSubOrderIntoNewOne(String splitSubOrder);

	OrderList listStoreByOrderCriteria(OrderCriteria criteria);

	void fillPricingInfomation(Order order);

	Order getByIdByStore(Long id, Long storeID);

	void adjustPrice(OrderServiceV2Context orderCtx, OrderPriceAdjustment priceData) throws ServiceExceptionV2, ServiceException;

	void onSuborderStatusAction(OrderServiceV2Context orderContext, Long soid, String actionName, boolean isAdminUser)
			throws ServiceExceptionV2, ServiceException;

	void deliverySubOrder(OrderServiceV2Context orderContext, String suborderid, String dCode, String dNo) throws ServiceExceptionV2, ServiceException;

	long getTotal(OrderServiceV2Context orderCtx) throws ServiceExceptionV2, ServiceException;

	double getTotalMoney(OrderServiceV2Context orderCtx, OrderCriteria criteria)throws ServiceExceptionV2, ServiceException;

	OrderList listByCriteria(OrderCriteria criteria)throws ServiceExceptionV2, ServiceException;

	String savePayProof(OrderServiceV2Context orderCtx, long parseLong, String imageFileName, boolean isAdminUser)throws ServiceExceptionV2, ServiceException;

	double getTotalMoney(Customer customer);
}
