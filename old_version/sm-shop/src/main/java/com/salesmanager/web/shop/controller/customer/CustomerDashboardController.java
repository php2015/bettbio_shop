package com.salesmanager.web.shop.controller.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.OrderServiceV2;
import com.salesmanager.core.business.order.service.OrderServiceV2Context;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.EzybioOrder;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;
import com.salesmanager.web.utils.ImageFilePathUtils;
import com.salesmanager.web.utils.OrderUtils;

/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerDashboardController extends AbstractController {
	
		
	//add by cy
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderServiceV2 orderServiceV2;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
    
	@Autowired
	private MemberPointsService memberpointsService;

	@Autowired
	private CustomerFacade customerFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;
	
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/dashboard.html", method=RequestMethod.GET)
	public String displayCustomerDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    
	    
		Customer customer = (Customer)request.getAttribute(Constants.CUSTOMER);
		//customer的option不知道有什么用，先屏蔽
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		
		OrderServiceV2Context orderCtx = OrderUtils.getOrderContext(request, customerFacade, userService);
		orderCtx.setUser(null);
		
		long totalorders = orderServiceV2.getTotal(orderCtx);
		OrderCriteria criteria = new OrderCriteria();
		criteria.setcId(customer.getId());
		double totalordermoney = orderServiceV2.getTotalMoney(orderCtx, criteria);
		ReadableOrderList readable= orderFacade.getReadableOrderList(customer, 0,3, language);
		// 计算每个订单可以执行的操作
        if (readable!= null && readable.getEzybioOrder() != null){
        	for( EzybioOrder ezyOrder : readable.getEzybioOrder()){
        		Order order = ezyOrder.getRawOrder();
        		orderServiceV2.fillPricingInfomation(order);
        		orderCtx.setOrder(order);
        		if (order.getSubOrders() != null){
        			for(com.salesmanager.core.business.order.model.SubOrder subOrder : order.getSubOrders()){
        				List<String> actionList = orderServiceV2.getActionList(orderCtx, subOrder, false);
        				subOrder.setActionList(actionList);
        				for(OrderProduct oprod : subOrder.getOrderProducts()){
        					oprod.setProductImageUrl(makeProductImageUrl(oprod));
        				}
        			}
        		}
        	}
        }
		
		 /** there must be a cart in the session **/
        String cartCode = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
              
        ShoppingCartData shoppingCart = shoppingCartFacade.getShoppingCartData(customer, store, cartCode);
        if(shoppingCart !=null){
        	 model.addAttribute("quantity",shoppingCart.getQuantity());
        }else{
        	 model.addAttribute("quantity",0);
        }
        
		model.addAttribute("section","dashboard");
		model.addAttribute("orders", totalorders);
		model.addAttribute("total", totalordermoney);
		model.addAttribute("customer", customer);
		model.addAttribute( "customerOrders", readable);
		model.addAttribute("activeMenu","dashboard");
		model.addAttribute("menberPoints", memberpointsService.queryByMemberPoints(customer.getId()));
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	private String makeProductImageUrl(OrderProduct item) {
		Long productId = item.getProductID();
		Product product = productService.getById(productId);
		if (product == null){
			return null;
		}
		ProductImage img = product.getProductImage();
		if (img == null){
			return null;
		}
		
		String imagePath = ImageFilePathUtils.buildProductImageFilePath(product.getMerchantStore(), product.getSku(), img.getProductImage());
		return imagePath;
	}
}
