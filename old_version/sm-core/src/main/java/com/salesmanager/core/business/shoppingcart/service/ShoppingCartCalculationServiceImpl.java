/**
 *
 */
package com.salesmanager.core.business.shoppingcart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.service.price.BrandDiscountService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.OrderServiceImpl;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.utils.ProductPriceUtils;
/**
 * <p>Implementation class responsible for calculating state of shopping cart.
 * This class will take care of calculating price of each line items of shopping cart
 * as well any discount including sub-total and total amount.
 * </p>
 *
 * @author Umesh Awasthi
 * @version 1.2
 */
@Service("shoppingCartCalculationService")
public class ShoppingCartCalculationServiceImpl implements ShoppingCartCalculationService
{


    protected  final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
	private BrandDiscountService brandDiscountService;


    /**
     * <p>Method used to recalculate state of shopping cart every time any change has been made
     * to underlying {@link ShoppingCart} object in DB.</p>
     * Following operations will be performed by this method.
     *
     * <li>Calculate price for each {@link ShoppingCartItem} and update it. </li>
     * <p>
     * This method is backbone method for all price calculation related to shopping cart.</p>
     *
     * @see OrderServiceImpl
     *
     * @param cartModel
     * @param customer
     * @param store
     * @param language
     * @throws ServiceException
     */
    @Override
    public OrderTotalSummary calculate( final ShoppingCart cartModel ,final Customer customer, final MerchantStore store, final Language language ) throws ServiceException
    {

        Validate.notNull(cartModel,"cart cannot be null");
        Validate.notNull(cartModel.getLineItems(),"Cart should have line items.");
        Validate.notNull(store,"MerchantStore cannot be null");
        Validate.notNull(customer,"Customer cannot be null");
        OrderTotalSummary orderTotalSummary=orderService.calculateShoppingCartTotal( cartModel, customer,store, language );
        updateCartModel(cartModel);
        return orderTotalSummary;


    }


    /**
     * <p>Method used to recalculate state of shopping cart every time any change has been made
     * to underlying {@link ShoppingCart} object in DB.</p>
     * Following operations will be performed by this method.
     *
     * <li>Calculate price for each {@link ShoppingCartItem} and update it. </li>
     * <p>
     * This method is backbone method for all price calculation related to shopping cart.</p>
     *
     * @see OrderServiceImpl
     *
     * @param cartModel
     * @param store
     * @param language
     * @throws ServiceException
     */
    @Override
    public OrderTotalSummary calculate(final Customer customer, final ShoppingCart cartModel , final MerchantStore store, final Language language ) throws ServiceException
    {

        Validate.notNull(cartModel,"cart cannot be null");
        Validate.notNull(cartModel.getLineItems(),"Cart should have line items.");
        Validate.notNull(store,"MerchantStore cannot be null");
        
        OrderTotalSummary orderTotalSummary=orderService.calculateShoppingCartTotal(customer, cartModel, store, language );
        updateCartModel(cartModel);
        return orderTotalSummary;


    }



    public ShoppingCartService getShoppingCartService()
    {
        return shoppingCartService;
    }


    private void updateCartModel(final ShoppingCart cartModel) throws ServiceException{
        shoppingCartService.saveOrUpdate( cartModel );
    }




}
