package com.salesmanager.web.admin.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.common.model.CriteriaOrderBy;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.OrderServiceV2;
import com.salesmanager.core.business.order.service.OrderServiceV2Context;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.utils.VerificationUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.EzybioOrder;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.utils.OrderUtils;

@Controller
public class AdminController {
	

	@Autowired
	UserService userService;
	
	//add by cy
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	OrderService orderService;

	@Autowired
	private OrderServiceV2 orderServiceV2;

	@Autowired
	private CustomerFacade customerFacade;
	
	@RequestMapping(value={"/admin/home.html","/admin/","/admin"}, method=RequestMethod.GET)
	public String displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("sellerhome", "sellerhome");
		
		model.addAttribute("activeMenus",activeMenus);
		
		
		//get store information
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//Map<String,Country> countries = countryService.getCountriesMap(language);
		
		//Country storeCountry = store.getCountry();
		//Country country = countries.get(storeCountry.getIsoCode());
		
		String sCurrentUser = request.getRemoteUser();
		User currentUser = userService.getByUserName(sCurrentUser);
		
		model.addAttribute("store", store);
		//model.addAttribute("country", country);
		model.addAttribute("user", currentUser);
		model.addAttribute("correctMobile", VerificationUtils.isMobileNumber(currentUser.getMobile()));
		//get last 10 orders
		OrderCriteria orderCriteria = new OrderCriteria();
		orderCriteria.setStartIndex(0);
		orderCriteria.setMaxCount(5);
		orderCriteria.setOrderBy(CriteriaOrderBy.DESC);
		
		//long totalorders = orderService.getTotal(store);
		OrderCriteria criteria = new OrderCriteria();
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null &&
        		 request.isUserInRole("ADMIN")){
			criteria.setStoreId(-1l);
			orderCriteria.setStoreId(-1l);
		}else{
			criteria.setStoreId(store.getId());
			orderCriteria.setStoreId(store.getId());
		}
		
		double totalordermoney = orderService.getTotalMoney(criteria);
		
		ReadableOrderList orderList = orderFacade.getStoreReadableOrderList(orderCriteria,language);
		// 计算每个订单可以执行的操作
        if (orderList != null && orderList.getEzybioOrder() != null){
        	OrderServiceV2Context orderCtx = OrderUtils.getOrderContext(request, customerFacade, userService);
        	boolean isAdminUser = request.isUserInRole("ADMIN");
        	for( EzybioOrder ezyOrder : orderList.getEzybioOrder()){
        		Order order = ezyOrder.getRawOrder();
        		orderCtx.setOrder(order);
        		if (order.getSubOrders() != null){
        			for(com.salesmanager.core.business.order.model.SubOrder subOrder : order.getSubOrders()){
        				List<String> actionList = orderServiceV2.getActionList(orderCtx, subOrder, isAdminUser);
        				subOrder.setActionList(actionList);
        			}
        		}
        	}
        }
		//ReadableOrderList readable= orderFacade.getReadableOrderList(store, customer, 0,3, language);
		model.addAttribute("customerOrders", orderList);
		model.addAttribute("totalordermoney", totalordermoney);
		
		return ControllerConstants.Tiles.adminDashboard;
	}

}
