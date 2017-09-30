/**
 *
 */
package com.salesmanager.web.shop.controller.shoppingCart.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionDescription;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValueDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartCalculationService;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.CartModificationException;
import com.salesmanager.web.entity.order.OrderTotal;
import com.salesmanager.web.entity.order.SubOrder;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartAttribute;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartDataPopulator;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.utils.ImageFilePathUtils;

/**
 * @author Umesh Awasthi
 * @version 1.2
 * @since 1.2
 */
@Service( value = "shoppingCartFacade" )
public class ShoppingCartFacadeImpl
    implements ShoppingCartFacade
{

    
    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartFacadeImpl.class);

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    ShoppingCartCalculationService shoppingCartCalculationService;

    @Autowired
    private ProductPriceUtils productPriceUtils;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductPriceService productPrice;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private ProductAttributeService productAttributeService;
    
    @Autowired
	private OrderFacade orderFacade;

    public void deleteShoppingCart(final Long id, final MerchantStore store) throws Exception {
    	ShoppingCart cart = shoppingCartService.getById(id, store);
    	if(cart!=null) {
    		shoppingCartService.deleteCart(cart);
    	}
    }
    
    @Override
    public void deleteShoppingCart(final String code, final MerchantStore store) throws Exception {
    	ShoppingCart cart = shoppingCartService.getByCode(code, store);
    	shoppingCartService.deleteCart(cart);
    }

    @Override
    public ShoppingCartData addItemsToShoppingCart( final ShoppingCartData shoppingCartData,
                                                    final ShoppingCartItem item, final MerchantStore store, final Language language,final Customer customer )
        throws Exception
    {

        ShoppingCart cartModel = null;
        if ( !StringUtils.isBlank( item.getCode() ) )
        {
            // get it from the db
            cartModel = getShoppingCartModel( item.getCode(), store );
            if ( cartModel == null )
            {
                cartModel = createCartModel( shoppingCartData.getCode(), store,customer );
            }

        }

        if ( cartModel == null )
        {

            final String shoppingCartCode =
                StringUtils.isNotBlank( shoppingCartData.getCode() ) ? shoppingCartData.getCode() : null;
            cartModel = createCartModel( shoppingCartCode, store,customer );

        }
        com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem =
            createCartItem( cartModel, item, store );
        
        boolean duplicateFound = false;
        if(CollectionUtils.isEmpty(item.getShoppingCartAttributes())) {//increment quantity
        	//get duplicate item from the cart
        	Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> cartModelItems = cartModel.getLineItems();
        	for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem cartItem : cartModelItems) {
        		//判断产品id和价格id是否相同，如果不同，则是单独的一条购买记录
        		if((cartItem.getProduct().getId().longValue()==shoppingCartItem.getProduct().getId().longValue())&&
        				cartItem.getPriceId().equals(shoppingCartItem.getPriceId())) {
        			if(CollectionUtils.isEmpty(cartItem.getAttributes())) {
        				if(!duplicateFound) {
        					//if(!shoppingCartItem.isProductVirtual()) {
	        					cartItem.setQuantity(cartItem.getQuantity() + shoppingCartItem.getQuantity());
        					//}
        					duplicateFound = true;
        					break;
        				}
        			}
        		}
        	}
        } 
        
        if(!duplicateFound) {
        	cartModel.getLineItems().add( shoppingCartItem );
        }
        
        /** Update cart in database with line items **/
        shoppingCartService.saveOrUpdate( cartModel );

        //refresh cart
        cartModel = shoppingCartService.getById(cartModel.getId(), store);

        shoppingCartCalculationService.calculate( cartModel, store, language );

        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
        shoppingCartDataPopulator.setPricingService( pricingService );

        return shoppingCartDataPopulator.populate( cartModel, store, language );
    }

    private com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem createCartItem( final ShoppingCart cartModel,
                                                                                               final ShoppingCartItem shoppingCartItem,
                                                                                               final MerchantStore store )
        throws Exception
    {

        Product product = productService.getById( shoppingCartItem.getProductId() );

        if ( product == null )
        {
            throw new Exception( "Item with id " + shoppingCartItem.getProductId() + " does not exist" );
        }
      //set all product
        /**
        if ( product.getMerchantStore().getId().intValue() != store.getId().intValue() )
        {
            throw new Exception( "Item with id " + shoppingCartItem.getProductId() + " does not belong to merchant "
                + store.getId() );
        }*/

                
        com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item =
            shoppingCartService.populateShoppingCartItem( product );
        
       // ProductPrice price = productService
        ProductPrice pPrice = productPrice.getById(shoppingCartItem.getPriceId());
        if(pPrice == null) throw new Exception( "Item with price id " + shoppingCartItem.getPriceId() + " does not exist" );
        item.setPriceId(pPrice.getId());
        item.setSpec(pPrice.getProductPriceDescription().getName());
        item.setItemPrice(pPrice.getFinalPrice());
        
        item.setQuantity( shoppingCartItem.getQuantity() );
        item.setShoppingCart( cartModel );

        // attributes
        /**
        List<ShoppingCartAttribute> cartAttributes = shoppingCartItem.getShoppingCartAttributes();
        if ( !CollectionUtils.isEmpty( cartAttributes ) )
        {
            for ( ShoppingCartAttribute attribute : cartAttributes )
            {
                ProductAttribute productAttribute = productAttributeService.getById( attribute.getAttributeId() );
                if ( productAttribute != null
                    && productAttribute.getProduct().getId().longValue() == product.getId().longValue() )
                {
                    com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem attributeItem =
                        new com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem( item,
                                                                                                         productAttribute );

                    item.addAttributes( attributeItem );
                }
            }
        }*/
        return item;

    }

    @Override
    public ShoppingCart createCartModel( final String shoppingCartCode, final MerchantStore store,final Customer customer )
        throws Exception
    {
        final Long CustomerId = customer != null ? customer.getId() : null;
        ShoppingCart cartModel = new ShoppingCart();
        if ( StringUtils.isNotBlank( shoppingCartCode ) )
        {
            cartModel.setShoppingCartCode( shoppingCartCode );
        }
        else
        {
            cartModel.setShoppingCartCode( UUID.randomUUID().toString().replaceAll( "-", "" ) );
        }

        cartModel.setMerchantStore( store );
        if ( CustomerId != null )
        {
            cartModel.setCustomerId( CustomerId );
            ;
        }
        shoppingCartService.create( cartModel );
        return cartModel;
    }





    private com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem getEntryToUpdate( final long entryId,
                                                                                                 final ShoppingCart cartModel )
    {
        if ( CollectionUtils.isNotEmpty( cartModel.getLineItems() ) )
        {
            for ( com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem : cartModel.getLineItems() )
            {
                if ( shoppingCartItem.getId().longValue() == entryId )
                {
                    LOG.info( "Found line item  for given entry id: " + entryId );
                    return shoppingCartItem;

                }
            }
        }
        LOG.info( "Unable to find any entry for given Id: " + entryId );
        return null;
    }

    private Object getKeyValue( final String key )
    {
        ServletRequestAttributes reqAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return reqAttr.getRequest().getAttribute( key );
    }

    @Override
    public ShoppingCartData getShoppingCartData( final Customer customer, final MerchantStore store,
                                                 final String shoppingCartId )
        throws Exception
    {

        ShoppingCart cart = null;
        try
        {
            if ( customer != null )
            {
                LOG.info( "Reteriving customer shopping cart..." );

                cart = shoppingCartService.getShoppingCart( customer );

            }

            else
            {
                if ( StringUtils.isNotBlank( shoppingCartId ) && cart == null )
                {
                    cart = shoppingCartService.getByCode( shoppingCartId, store );
                }

            }
        }
        catch ( ServiceException ex )
        {
            LOG.error( "Error while retriving cart from customer", ex );
        }
        catch( NoResultException nre) {
        	//nothing
        }

        if ( cart == null )
        {
        	LOG.info( "Cart model found." );
            return null;
            
        }

        

        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
        shoppingCartDataPopulator.setPricingService( pricingService );

        Language language = (Language) getKeyValue( Constants.LANGUAGE );
        MerchantStore merchantStore = (MerchantStore) getKeyValue( Constants.MERCHANT_STORE );
        return shoppingCartDataPopulator.populate( cart, merchantStore, language );

    }

    @Override
    public ShoppingCartData getShoppingCartData( final ShoppingCart shoppingCartModel )
        throws Exception
    {

        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
        shoppingCartDataPopulator.setPricingService( pricingService );
        Language language = (Language) getKeyValue( Constants.LANGUAGE );
        MerchantStore merchantStore = (MerchantStore) getKeyValue( Constants.MERCHANT_STORE );
        return shoppingCartDataPopulator.populate( shoppingCartModel, merchantStore, language );
    }

    @Override
    public ShoppingCartData removeCartItem( final Long itemID, final String cartId ,final MerchantStore store,final Language language )
        throws Exception
    {
        if ( StringUtils.isNotBlank( cartId ) )
        {

            ShoppingCart cartModel = getCartModel( cartId,store );
            if ( cartModel != null )
            {
                if ( CollectionUtils.isNotEmpty( cartModel.getLineItems() ) )
                {
                    Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> shoppingCartItemSet =
                        new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
                    for ( com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem : cartModel.getLineItems() )
                    {
                        if ( shoppingCartItem.getId().longValue() != itemID.longValue() )
                        {
                            shoppingCartItemSet.add( shoppingCartItem );
                        }
                    }
                    cartModel.setLineItems( shoppingCartItemSet );
                    shoppingCartService.saveOrUpdate( cartModel );




                    ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
                    shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
                    shoppingCartDataPopulator.setPricingService( pricingService );
                    return shoppingCartDataPopulator.populate( cartModel, store, language );
                }
            }
        }
        return null;
    }

    @Override
    public ShoppingCartData updateCartItem( final Long itemID, final String cartId, final long newQuantity,final MerchantStore store, final Language language )
        throws Exception
    {
        if ( newQuantity < 1 )
        {
            throw new CartModificationException( "Quantity must not be less than one" );
        }
        if ( StringUtils.isNotBlank( cartId ) )
        {
            ShoppingCart cartModel = getCartModel( cartId,store );
            if ( cartModel != null )
            {
                com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem entryToUpdate =
                    getEntryToUpdate( itemID.longValue(), cartModel );

                if ( entryToUpdate == null )
                {
                    throw new CartModificationException( "Unknown entry number." );
                }

                entryToUpdate.getProduct();

                LOG.info( "Updating cart entry quantity to" + newQuantity );
                entryToUpdate.setQuantity( (int) newQuantity );
                List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();
                productAttributes.addAll( entryToUpdate.getProduct().getAttributes() );
                final FinalPrice finalPrice =
                    productPriceUtils.getFinalProductPrice( entryToUpdate.getProduct(), productAttributes );
                entryToUpdate.setItemPrice( finalPrice.getFinalPrice() );
                shoppingCartService.saveOrUpdate( cartModel );

                LOG.info( "Cart entry updated with desired quantity" );
                ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
                shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
                shoppingCartDataPopulator.setPricingService( pricingService );
                return shoppingCartDataPopulator.populate( cartModel, store, language );

            }
        }
        return null;
    }
    
    //modify by cy use for add chioce with cart
    
    @Override
    public ShoppingCartData updateCartItems( final List<ShoppingCartItem> shoppingCartItems, final MerchantStore store, final Language language )
            throws Exception
        {
    	
    		Validate.notEmpty(shoppingCartItems,"shoppingCartItems null or empty");
    		ShoppingCart cartModel = null;
    		//Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> cartItems = new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
    		Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> cartItems = null; 
    		for(ShoppingCartItem item : shoppingCartItems) {
    			
    			if(item.getQuantity()<1) {
    				throw new CartModificationException( "Quantity must not be less than one" );
    			}
    			
    			if(cartModel==null) {
    				cartModel = getCartModel( item.getCode(), store );
    				cartItems = cartModel.getLineItems();
    			}
    			
                com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem entryToUpdate =
                        getEntryToUpdate( item.getId(), cartModel );

                if ( entryToUpdate == null ) {
                        throw new CartModificationException( "Unknown entry number." );
                }

                entryToUpdate.getProduct();

                LOG.info( "Updating cart entry quantity to" + item.getQuantity() );
                entryToUpdate.setQuantity( (int) item.getQuantity() );
                
                List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();
                productAttributes.addAll( entryToUpdate.getProduct().getAttributes() );
                
                final FinalPrice finalPrice =
                        productPriceUtils.getFinalProductPrice( entryToUpdate.getProduct(), productAttributes );
                entryToUpdate.setItemPrice( finalPrice.getFinalPrice() );
                    

                cartItems.add(entryToUpdate);
    			
    			
    			
    			
    		}
    		
    		cartModel.setLineItems(cartItems);
    		shoppingCartService.saveOrUpdate( cartModel );
            LOG.info( "Cart entry updated with desired quantity" );
            ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
            shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
            shoppingCartDataPopulator.setPricingService( pricingService );
            return shoppingCartDataPopulator.populate( cartModel, store, language );

        }


    private ShoppingCart getCartModel( final String cartId,final MerchantStore store )
    {
        if ( StringUtils.isNotBlank( cartId ) )
        {
           try
            {
                return shoppingCartService.getByCode( cartId, store );
            }
            catch ( ServiceException e )
            {
                LOG.error( "unable to find any cart asscoiated with this Id: " + cartId );
                LOG.error( "error while fetching cart model...", e );
                return null;
            }
            catch( NoResultException nre) {
           	//nothing
            }

        }
        return null;
    }

	@Override
	public ShoppingCartData getShoppingCartData(String code, MerchantStore store) {
		try {
			ShoppingCart cartModel = shoppingCartService.getByCode( code, store );
			if(cartModel!=null) {
				ShoppingCartData cart = getShoppingCartData(cartModel);
				return cart;
			}
		} catch( NoResultException nre) {
	        	//nothing

		} catch(Exception e) {
			LOG.error("Cannot retrieve cart code " + code,e);
		}


		return null;
	}

	@Override
	public ShoppingCart getShoppingCartModel(String shoppingCartCode,
			MerchantStore store) throws Exception {
		return shoppingCartService.getByCode( shoppingCartCode, store );
	}

	@Override
	public ShoppingCart getShoppingCartModel(Customer customer,
			MerchantStore store) throws Exception {
		return shoppingCartService.getByCustomer(customer);
	}

	@Override
	public ShoppingCartData updateModelCartItems(
			List<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> shoppingCartItems,String code,
			MerchantStore store, Language language,Customer customer) throws Exception {
		// TODO Auto-generated method stub
		Validate.notEmpty(shoppingCartItems,"shoppingCartItems null or empty");
		ShoppingCartData shoppingCartData = new ShoppingCartData();
		int cartQuantity = 0;
		ShoppingCart cartModel =  getCartModel( code, store );
		//List<ShoppingCartItem> entityDataItems=  new ArrayList<ShoppingCartItem>();
		List<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> selectedItems = new ArrayList<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
		Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> cartItems = cartModel.getLineItems();; 
		//modify by cy set all isselected forget used selected
		if(cartItems == null){
			throw new CartModificationException( "Quantity must not be less than one" );
		}
		for (com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item : cartItems){
			item.setSelected(false);;
		}
		for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item : shoppingCartItems) {
			
			if(item.getQuantity()<1) {
				throw new CartModificationException( "Quantity must not be less than one" );
			}
			
            com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem entryToUpdate =
                    getEntryToUpdate( item.getId(), cartModel );

            if ( entryToUpdate == null ) {
                    throw new CartModificationException( "Unknown entry number." );
            }

            ShoppingCartItem entityDateItem = new ShoppingCartItem();
           // entryToUpdate.getProduct();

            LOG.info( "Updating cart entry quantity to" + item.getQuantity() );
            entryToUpdate.setQuantity( (int) item.getQuantity() );
            
            
            List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();
            productAttributes.addAll( entryToUpdate.getProduct().getAttributes() );
            
           // final FinalPrice finalPrice = new FinalPrice();
           // finalPrice.setProductPrice(item.getProductPrice());
            //        productPriceUtils.getFinalProductPrice( entryToUpdate.getProduct(), productAttributes );
            entryToUpdate.setItemPrice( entryToUpdate.getProductPrice().getFinalPrice() );
            //计算单商品总价
            entryToUpdate.setSubTotal(entryToUpdate.getItemPrice().multiply(new BigDecimal(item.getQuantity())));
            
            entityDateItem.setId(entryToUpdate.getId());
            entityDateItem.setCode(code);
            entityDateItem.setQuantity((int) item.getQuantity());
            entityDateItem.setName(entryToUpdate.getProduct().getProductDescription().getName());                      
            entityDateItem.setPrice(pricingService.getDisplayAmount(entryToUpdate.getItemPrice(),store));
            entityDateItem.setSubTotal(pricingService.getDisplayAmount(entryToUpdate.getSubTotal(), store));
            //entityDateItem.setQuantity(entryToUpdate.getQuantity());
            cartQuantity += entryToUpdate.getQuantity();
            entityDateItem.setProductPrice(entryToUpdate.getItemPrice());
            entityDateItem.setSpecs(entryToUpdate.getSpec());
            Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem> attributes = entryToUpdate.getAttributes();
            if(attributes!=null) {
                List<ShoppingCartAttribute> cartAttributes = new ArrayList<ShoppingCartAttribute>();
                for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem attribute : attributes) {
                    ShoppingCartAttribute cartAttribute = new ShoppingCartAttribute();
                    cartAttribute.setId(attribute.getId());
                    cartAttribute.setAttributeId(attribute.getProductAttributeId());
                    cartAttribute.setOptionId(attribute.getProductAttribute().getProductOption().getId());
                    cartAttribute.setOptionValueId(attribute.getProductAttribute().getProductOptionValue().getId());
                    List<ProductOptionDescription> optionDescriptions = attribute.getProductAttribute().getProductOption().getDescriptionsSettoList();
                    List<ProductOptionValueDescription> optionValueDescriptions = attribute.getProductAttribute().getProductOptionValue().getDescriptionsSettoList();
                    if(!CollectionUtils.isEmpty(optionDescriptions) && !CollectionUtils.isEmpty(optionValueDescriptions)) {
                    	cartAttribute.setOptionName(optionDescriptions.get(0).getName());
                    	cartAttribute.setOptionValue(optionValueDescriptions.get(0).getName());
                    	cartAttributes.add(cartAttribute);
                    }
                }
                entityDateItem.setShoppingCartAttributes(cartAttributes);
            }
            
            entryToUpdate.setSelected(true);
            cartItems.add(entryToUpdate);
            selectedItems.add(entryToUpdate);
            //entityDataItems.add(entityDateItem);
            //add subOrders
            List<SubOrder> suboders = shoppingCartData.getSubOrders();
            if(suboders == null) suboders = new ArrayList<SubOrder>();
            SubOrder subOrder = orderFacade.getSubOrderByStore(suboders, entryToUpdate.getProduct().getMerchantStore());
            subOrder.setMerchantStore(entryToUpdate.getProduct().getMerchantStore()); 
            List<ShoppingCartItem> shoppingCartItemsList = subOrder.getShoppingCartItems();
            if(shoppingCartItemsList == null) shoppingCartItemsList = new ArrayList<ShoppingCartItem>();
            shoppingCartItemsList.add(entityDateItem);
            subOrder.setTotal(subOrder.getTotal().add(entryToUpdate.getSubTotal()));
            subOrder.setShoppingCartItems(shoppingCartItemsList);
            suboders = orderFacade.setSubOrder(suboders, subOrder);
            //suboders.add(subOrder);
            shoppingCartData.setSubOrders(suboders);
            
            
		}
		
		cartModel.setLineItems(cartItems);
		shoppingCartService.saveOrUpdate( cartModel );
        OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store,selectedItems,language,customer);
        if(CollectionUtils.isNotEmpty(orderTotalSummary.getTotals())) {
    		List<OrderTotal> totals = new ArrayList<OrderTotal>();
    		for(com.salesmanager.core.business.order.model.OrderTotal t : orderTotalSummary.getTotals()) {
    		OrderTotal total = new OrderTotal();
    		total.setCode(t.getOrderTotalCode());
    		total.setValue(t.getValue());
    		totals.add(total);
    		}
    		shoppingCartData.setTotal(orderTotalSummary.getTotal().toString());
    		shoppingCartData.setTotals(totals);
    		}
        LOG.info( "Cart entry updated with desired quantity" );
       
        shoppingCartData.setQuantity(cartQuantity);
       
        return shoppingCartData;
	}

	@Override
	public ShoppingCart deleteShoppingCart(ShoppingCart cart,
			MerchantStore store) throws Exception {
		if(cart!=null) {
    		
    		Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> items =   cart.getLineItems();
    		Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> leftItems =    new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>() ;
    		for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item : items){
    			if(item.isSelected()==false){
    				leftItems.add(item);
    			}
    		}
    		if(leftItems != null && leftItems.size()<1){
    			shoppingCartService.deleteCart(cart);
    		}else{
    			cart.setLineItems(leftItems);
    			shoppingCartService.saveOrUpdate(cart);
    		}
    	}
		return cart;
	}

	@Override
	public ShoppingCartData addItemsToShoppingCart(
			ShoppingCartData shoppingCart, ShoppingCartItem[] items,
			MerchantStore store, Language language, Customer customer,String code)
			throws Exception {
		// TODO Auto-generated method stub
		ShoppingCart cartModel = null;
        if ( !StringUtils.isBlank( code ) )
        {
            // get it from the db
            cartModel = getShoppingCartModel(code, store );
            if ( cartModel == null )
            {
                cartModel = createCartModel( shoppingCart.getCode(), store,customer );
            }

        }

        if ( cartModel == null )
        {

            final String shoppingCartCode =
                StringUtils.isNotBlank( shoppingCart.getCode() ) ? shoppingCart.getCode() : null;
            cartModel = createCartModel( shoppingCartCode, store,customer );

        }
        List<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> shoppingCartItems = new ArrayList<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
       // boolean duplicateFound = false;
        for(int i=0; i<items.length;i++){
        	com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem =createCartItem( cartModel, items[i], store );
        	// if(CollectionUtils.isEmpty(items[i].getShoppingCartAttributes())) {//increment quantity
        		boolean duplicateFound = false;
             	//get duplicate item from the cart
             	Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> cartModelItems = cartModel.getLineItems();
             	for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem cartItem : cartModelItems) {
             		//判断产品id和价格id是否相同，如果不同，则是单独的一条购买记录
             		if((cartItem.getProduct().getId().longValue()==shoppingCartItem.getProduct().getId().longValue())&&
             				cartItem.getPriceId().equals(shoppingCartItem.getPriceId())) {
             			
             			//if(CollectionUtils.isEmpty(cartItem.getAttributes())) {
             				if(!duplicateFound) {
             					//if(!shoppingCartItem.isProductVirtual()) {
     	        					cartItem.setQuantity(cartItem.getQuantity() + shoppingCartItem.getQuantity());
             					//}
             					duplicateFound = true;
             					break;
             				}
             			//}*/
             			duplicateFound=true;
             			break;
             		}
             	}
             	if(!duplicateFound){
             		shoppingCartItems.add(shoppingCartItem);
             	}
        }
        
        
        
        
       if(shoppingCartItems.size()>0){
    	   for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem it:shoppingCartItems){
    		   cartModel.getLineItems().add(it);
    	   }
       }
        
        
        /** Update cart in database with line items **/
        shoppingCartService.saveOrUpdate( cartModel );

        //refresh cart
        cartModel = shoppingCartService.getById(cartModel.getId(), store);

        shoppingCartCalculationService.calculate( cartModel, store, language );

        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
        shoppingCartDataPopulator.setPricingService( pricingService );

        return shoppingCartDataPopulator.populate( cartModel, store, language );
	}
	 
}
