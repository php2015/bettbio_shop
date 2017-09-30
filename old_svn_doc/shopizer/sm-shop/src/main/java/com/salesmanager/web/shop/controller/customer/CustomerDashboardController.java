package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;

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
	private OrderFacade orderFacade;
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
    
	@Autowired
	private MemberPointsService memberpointsService;
	
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/dashboard.html", method=RequestMethod.GET)
	public String displayCustomerDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    
	    
		Customer customer = (Customer)request.getAttribute(Constants.CUSTOMER);
		//customer的option不知道有什么用，先屏蔽
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		
		long totalorders = orderService.getTotal(customer);
		OrderCriteria criteria = new OrderCriteria();
		criteria.setcId(customer.getId());
		long totalordermoney = orderService.getTotalMoney(criteria);
		ReadableOrderList readable= orderFacade.getReadableOrderList(customer, 0,3, language);
		
		
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
	
	
}
