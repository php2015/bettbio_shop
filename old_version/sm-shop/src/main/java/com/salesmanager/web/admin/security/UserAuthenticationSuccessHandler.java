package com.salesmanager.web.admin.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.salesmanager.core.business.common.model.UserLogin;
import com.salesmanager.core.business.common.service.UserLoginService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.GeoLocationUtils;

public class UserAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerFacade customerFacade;	
	@Autowired
	private ShoppingCartService shoppingCartService;	
	@Autowired
	private UserLoginService userLoginService;
	
	  @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		  // last access timestamp
		  String userName = authentication.getName();
		  
		  try {
			  request.getSession().setAttribute(Constants.LOGON_AS, Constants.LOGON_AS_SELLER);
			  request.getSession().removeAttribute("sellerLogonError");
			  User user = userService.getByUserName(userName);
			  
			  Date lastAccess =   new Date();
			  userService.saveOrUpdate(user);
			  UserLogin userLogin  = new UserLogin();
			  userLogin.setUser(user);
			  userLogin.setDateCreated(lastAccess);
			  user.setLastAccess(lastAccess);
			//  user.setLoginTime(new Date());
			  //get ip
			  String ip = GeoLocationUtils.getClientIpAddress(request);
			  if(ip!=null || !ip.equalsIgnoreCase("")){
				  userLogin.setIp(ip);
			  }
			  userLoginService.saveOrUpdate(userLogin);
			  
			  autoLogonBuyer(user, request);
			  response.sendRedirect(request.getContextPath() + "/admin/home.html");
		  } catch (Exception e) {
			 response.sendRedirect(request.getContextPath() + "shop/customer/customLogon.html");
		  }
	   }

	private void autoLogonBuyer(User user, HttpServletRequest request) {
		String mobile = user.getMobile();
		if (mobile == null){
			return;
		}
		Customer customer = customerService.getByPhone(mobile);
		if (customer == null){
			return;
		}
		
		
		LOGGER.info( "Fetching and merging Shopping Cart data" );
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		Customer customerModel;
		try {
			customerModel = customerFacade.getCustomerForLogin(mobile);
			request.getSession().setAttribute(Constants.CUSTOMER, customerModel);
			final String sessionShoppingCartCode= (String)request.getSession().getAttribute( Constants.SHOPPING_CART );
			if(!StringUtils.isBlank(sessionShoppingCartCode)) {
				ShoppingCartData shoppingCartData= customerFacade.mergeCart( customerModel, sessionShoppingCartCode, store, language );
				
				
				if(shoppingCartData !=null){
					request.getSession().setAttribute(Constants.SHOPPING_CART, shoppingCartData.getCode());
				}
			} else {
				
				ShoppingCart cartModel = shoppingCartService.getByCustomer(customerModel);
				if(cartModel!=null) {
					request.getSession().setAttribute(Constants.SHOPPING_CART, cartModel.getShoppingCartCode());
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			LOGGER.warn("Cannot merge shopping cart for user " + mobile);
		}
	}

}
