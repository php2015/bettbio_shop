package com.salesmanager.web.utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.service.OrderServiceV2Context;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;

public class OrderUtils {
	public static Date startTime = new Date();
	
	public static OrderServiceV2Context getOrderContext(HttpServletRequest request, CustomerFacade customerFacade,
			UserService userService) throws Exception {
		// get customer first
		OrderServiceV2Context orderContext = new OrderServiceV2Context();
		

		MerchantStore store = (MerchantStore) request.getSession().getAttribute(Constants.MERCHANT_STORE);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<CustomerAddress> tempAddress = new HashSet<>();
		Customer customer = (Customer) request.getSession().getAttribute(Constants.CUSTOMER);
		if (customer != null && customerFacade != null) {
			customer = customerFacade.getByID(customer.getId().toString());
			if (customer.getAddresss() != null){
				for(CustomerAddress addr : customer.getAddresss()){
					tempAddress.add(addr);
				}
			}
			orderContext.setCustomer(customer);
			orderContext.setCustomerAddressSet(tempAddress);
		}

		orderContext.setStore(store);
		User user = (User) request.getSession().getAttribute(Constants.ADMIN_USER);
		if (user != null && userService != null) {
			orderContext.setUser(userService.getById(user.getId()));
			orderContext.setAdminUser(request.isUserInRole("ADMIN"));
		}else{
			orderContext.setAdminUser(false);
		}
		orderContext.setLocale(LocaleUtils.getLocale(store));
		orderContext.setLanguage((Language) request.getAttribute("LANGUAGE"));
		orderContext.setCommitChannel(GlobalConstants.OrderCommitChannel.PC);

		return orderContext;
	}
}
