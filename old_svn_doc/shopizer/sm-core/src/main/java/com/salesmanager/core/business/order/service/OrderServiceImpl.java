package com.salesmanager.core.business.order.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.dao.OrderDao;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.OrderTotalType;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.payments.service.TransactionService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.tax.service.TaxService;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.modules.order.InvoiceModule;


@Service("orderService")
public class OrderServiceImpl  extends SalesManagerEntityServiceImpl<Long, Order> implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private InvoiceModule invoiceModule;

    @Autowired
    private ShippingService shippingService;
    
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TaxService taxService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private TransactionService transactionService;

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(final OrderDao orderDao) {
        super(orderDao);
        this.orderDao = orderDao;
    }

    @Override
    public void addOrderStatusHistory(final Order order, final OrderStatusHistory history) throws ServiceException {
        order.getOrderHistory().add(history);
        history.setOrder(order);
        update(order);
    }
    
    @Override
    public Order processOrder(Order order, Customer customer, List<ShoppingCartItem> items, OrderTotalSummary summary, Payment payment, MerchantStore store) throws ServiceException {
    	
    	return this.process(order, customer, items, summary, payment, null, store);
    }
    
    @Override
    public Order processOrder(Order order, Customer customer, List<ShoppingCartItem> items, OrderTotalSummary summary, Payment payment, Transaction transaction, MerchantStore store) throws ServiceException {
    	try{
    	
    	return this.process(order, customer, items, summary, payment, transaction, store);
    	}catch (Exception e) {
    		throw new ServiceException(e);
    	}
		
    }
    
    private Order process(Order order, Customer customer, List<ShoppingCartItem> items, OrderTotalSummary summary, Payment payment, Transaction transaction, MerchantStore store) throws ServiceException {
    	
    	
    	Validate.notNull(order, "Order cannot be null");
    	Validate.notNull(customer, "Customer cannot be null (even if anonymous order)");
    	//Validate.notEmpty(items, "ShoppingCart items cannot be null");
    	Validate.notNull(payment, "Payment cannot be null");
    	Validate.notNull(store, "MerchantStore cannot be null");
    	Validate.notNull(summary, "Order total Summary cannot be null");
    	
    	
    	
    	if(order.getOrderHistory()==null || order.getOrderHistory().size()==0 || order.getStatus()==null) {
    		OrderStatus status = order.getStatus();
    		if(status==null) {
    			status = OrderStatus.ORDERED;
    			order.setStatus(status);
    		}
    		Set<OrderStatusHistory> statusHistorySet = new HashSet<OrderStatusHistory>();
    		OrderStatusHistory statusHistory = new OrderStatusHistory();
    		statusHistory.setStatus(status);
    		statusHistory.setDateAdded(new Date());
    		statusHistory.setOrder(order);
    		statusHistorySet.add(statusHistory);
    		order.setOrderHistory(statusHistorySet);
    		
    	}
    	
    	order.setCustomerId(customer.getId());
    	
    	this.create(order);

    	if(transaction!=null) {
    		transaction.setOrder(order);
    		if(transaction.getId()==null || transaction.getId()==0) {
    			transactionService.create(transaction);
    		} else {
    			transactionService.update(transaction);
    		}
    	}
    	
    	return order;
    	
    	
    }

    private OrderTotalSummary caculateOrder(final OrderSummary summary, final Customer customer, final MerchantStore store, final Language language) throws Exception {

        OrderTotalSummary totalSummary = new OrderTotalSummary();
        List<OrderTotal> orderTotals = new ArrayList<OrderTotal>();
        //Map<String,OrderTotal> otherPricesTotals = new HashMap<String,OrderTotal>();

        //ShippingConfiguration shippingConfiguration = null;

        BigDecimal grandTotal = new BigDecimal(0);
        grandTotal.setScale(2, RoundingMode.HALF_UP);

        //price by item
        /**
         * qty * price
         * subtotal
         */
        BigDecimal subTotal = new BigDecimal(0);
        subTotal.setScale(2, RoundingMode.HALF_UP);
        for(ShoppingCartItem item : summary.getProducts()) {

            BigDecimal st = item.getItemPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setSubTotal(st);
            subTotal = subTotal.add(st);
            //Other prices
           /**
            FinalPrice finalPrice = item.getFinalPrice();
            if(finalPrice!=null) {
                List<FinalPrice> otherPrices = finalPrice.getAdditionalPrices();
                if(otherPrices!=null) {
                    for(FinalPrice price : otherPrices) {
                        if(!price.isDefaultPrice()) {
                            OrderTotal itemSubTotal = otherPricesTotals.get(price.getProductPrice().getCode());

                            if(itemSubTotal==null) {
                                itemSubTotal = new OrderTotal();
                                itemSubTotal.setModule(Constants.OT_ITEM_PRICE_MODULE_CODE);
                                itemSubTotal.setText(Constants.OT_ITEM_PRICE_MODULE_CODE);
                                itemSubTotal.setTitle(Constants.OT_ITEM_PRICE_MODULE_CODE);
                                itemSubTotal.setOrderTotalCode(price.getProductPrice().getCode());
                                itemSubTotal.setOrderTotalType(OrderTotalType.PRODUCT);
                                itemSubTotal.setSortOrder(0);
                                otherPricesTotals.put(price.getProductPrice().getCode(), itemSubTotal);
                            }

                            BigDecimal orderTotalValue = itemSubTotal.getValue();
                            if(orderTotalValue==null) {
                                orderTotalValue = new BigDecimal(0);
                                orderTotalValue.setScale(2, RoundingMode.HALF_UP);
                            }

                            orderTotalValue = orderTotalValue.add(price.getFinalPrice());
                            itemSubTotal.setValue(orderTotalValue);
                            if(price.getProductPrice().getProductPriceType().name().equals(OrderValueType.ONE_TIME)) {
                                subTotal = subTotal.add(price.getFinalPrice());
                            }
                        }
                    }
                }
            }*/

        }

        //modify by cy now not need subtoal and shipping summary
        //totalSummary.setSubTotal(subTotal);
        grandTotal=grandTotal.add(subTotal);
       

        // grand total
        OrderTotal orderTotal = new OrderTotal();
        orderTotal.setModule(Constants.OT_TOTAL_MODULE_CODE);
        orderTotal.setOrderTotalType(OrderTotalType.TOTAL);
        orderTotal.setOrderTotalCode("order.total.total");
        orderTotal.setTitle(Constants.OT_TOTAL_MODULE_CODE);
        orderTotal.setText("order.total.total");
        orderTotal.setSortOrder(300);
        orderTotal.setValue(grandTotal);
        orderTotals.add(orderTotal);

        totalSummary.setTotal(grandTotal);
        totalSummary.setTotals(orderTotals);
        return totalSummary;

    }


    @Override
    public OrderTotalSummary caculateOrderTotal(final OrderSummary orderSummary, final Customer customer, final MerchantStore store, final Language language) throws ServiceException {
        Validate.notNull(orderSummary,"Order summary cannot be null");
        Validate.notNull(orderSummary.getProducts(),"Order summary.products cannot be null");
        Validate.notNull(store,"MerchantStore cannot be null");
        Validate.notNull(customer,"Customer cannot be null");

        try {
            return caculateOrder(orderSummary, customer, store, language);
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }



    @Override
    public OrderTotalSummary caculateOrderTotal(final OrderSummary orderSummary, final MerchantStore store, final Language language) throws ServiceException {
        Validate.notNull(orderSummary,"Order summary cannot be null");
        Validate.notNull(orderSummary.getProducts(),"Order summary.products cannot be null");
        Validate.notNull(store,"MerchantStore cannot be null");

        try {
            return caculateOrder(orderSummary, null, store, language);
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    private OrderTotalSummary caculateShoppingCart( final ShoppingCart shoppingCart, final Customer customer, final MerchantStore store, final Language language) throws Exception {

        
    	
    	
    	
    	OrderSummary orderSummary = new OrderSummary();
    	
    	List<ShoppingCartItem> itemsSet = new ArrayList<ShoppingCartItem>(shoppingCart.getLineItems());
    	orderSummary.setProducts(itemsSet);
    	
    	
    	return this.caculateOrder(orderSummary, customer, store, language);

    }


    /**
     * <p>Method will be used to calculate Shopping cart total as well will update price for each
     * line items.
     * </p>
     * @param shoppingCart
     * @param customer
     * @param store
     * @param language
     * @return {@link OrderTotalSummary}
     * @throws ServiceException
     * 
     */
    @Override
    public OrderTotalSummary calculateShoppingCartTotal(
                                                        final ShoppingCart shoppingCart, final Customer customer, final MerchantStore store,
                                                        final Language language) throws ServiceException {
        Validate.notNull(shoppingCart,"Order summary cannot be null");
        Validate.notNull(customer,"Customery cannot be null");
        Validate.notNull(store,"MerchantStore cannot be null.");
        try {
            return caculateShoppingCart(shoppingCart, customer, store, language);
        } catch (Exception e) {
            LOGGER.error( "Error while calculating shopping cart total" +e );
            throw new ServiceException(e);
        }

    }




    /**
     * <p>Method will be used to calculate Shopping cart total as well will update price for each
     * line items.
     * </p>
     * @param shoppingCart
     * @param store
     * @param language
     * @return {@link OrderTotalSummary}
     * @throws ServiceException
     * 
     */
    @Override
    public OrderTotalSummary calculateShoppingCartTotal(
                                                        final ShoppingCart shoppingCart, final MerchantStore store, final Language language)
                                                                        throws ServiceException {
        Validate.notNull(shoppingCart,"Order summary cannot be null");
        Validate.notNull(store,"MerchantStore cannot be null");

        try {
            return caculateShoppingCart(shoppingCart, null, store, language);
        } catch (Exception e) {
            LOGGER.error( "Error while calculating shopping cart total" +e );
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(final Order order) throws ServiceException {


        super.delete(order);
    }


    @Override
    public ByteArrayOutputStream generateInvoice(final MerchantStore store, final Order order, final Language language) throws ServiceException {

       // Validate.notNull(order.getOrderProducts(),"Order products cannot be null");
       // Validate.notNull(order.getOrderTotal(),"Order totals cannot be null");

        try {
            ByteArrayOutputStream stream = invoiceModule.createInvoice(store, order, language);
            return stream;
        } catch(Exception e) {
            throw new ServiceException(e);
        }



    }

    @Override
    public Order getOrder(final Long orderId ) {
        return getById(orderId);
    }


    @Override
    public OrderList listByCriteria( final OrderCriteria criteria) {

        return orderDao.listByOrderCriteria(criteria);
    }


    @Override
    public void saveOrUpdate(final Order order) throws ServiceException {

        if(order.getId()!=null && order.getId()>0) {
            LOGGER.debug("Updating Order");
            super.update(order);

        } else {
            LOGGER.debug("Creating Order");
            super.create(order);

        }
    }

	@Override
	public boolean hasDownloadFiles(Order order) throws ServiceException {
		
		Validate.notNull(order,"Order cannot be null");
		return true;
	}
	
	/**
	 * add by cy 
	 */
	@Override
	public long getTotal(MerchantStore store) {
		// TODO Auto-generated method stub
		return orderDao.getTotal(store);
	}
	
	/**
	 * add by cy 
	 */
	@Override
	public long getTotal(Customer customer) {
		// TODO Auto-generated method stub
		return orderDao.getTotal(customer);
	}
	
	/**
	 * add by cy 
	 */
	@Override
	public long getTotalMoney(OrderCriteria criteria) {
		// TODO Auto-generated method stub
		return orderDao.getTotalMoney(criteria);
	}

	@Override
	public OrderList listStoreByOrderCriteria(OrderCriteria criteria) {
		// TODO Auto-generated method stub
		return orderDao.listStoreByOrderCriteria(criteria);
	}

	@Override
	public Order getByIdByStore(Long id, Long storeID) {
		// TODO Auto-generated method stub
		return orderDao.getByIdByStore(id, storeID);
	}

	@Override
	public long getTotalMoney(Customer customer) {
		// TODO Auto-generated method stub
		return orderDao.getTotalMoney(customer);
	}

	@Override
	public List<Object[]> queryByStoreId(Long id) {
		// TODO Auto-generated method stub
		return this.orderDao.queryByStoreId(id);
	}

	@Override
	public boolean updateByMenberPoint(Long orderid, int pointScore) {
		// TODO Auto-generated method stub
		if(this.orderDao.updateByMenberPoint(orderid, pointScore)){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateBySubOrder(Long suid, Long orderid, double money1,double money2) {
		// TODO Auto-generated method stub
		if(this.orderDao.updateBySubOrder(suid, orderid, money1,money2))
		{
			return true;
		}
		return false;
	}
	@Override
	public List<Object[]> queryByOrderProduct(Long id)
	{
		return this.orderDao.queryByOrderProduct(id);
	}
	@Override
	public boolean updateByOrderProduct(Long pid,Double price,Double totalprice)
	{
		return this.orderDao.updateByOrderProduct(pid, price, totalprice);
	}

	@Override
	public boolean exist(String phone) {
		
		return this.orderDao.exist(phone);
	}
}
