package com.salesmanager.core.business.merchant.authorization.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.authorization.dao.AuthorizationDao;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.user.service.UserService;

@Service("authorizationService")
public class AuthorizationServiceImpl extends SalesManagerEntityServiceImpl<Long, Authorization> 
		implements AuthorizationService {
	

		
	@Autowired
	protected ProductTypeService productTypeService;
	
		
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	private AuthorizationDao authorizationDao;
	
	@Autowired
	public AuthorizationServiceImpl(AuthorizationDao authorizationDao) {
		super(authorizationDao);
		this.authorizationDao = authorizationDao;
	}

	

}
