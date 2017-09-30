package com.salesmanager.core.business.merchant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.dao.MerchantRankDao;
import com.salesmanager.core.business.merchant.dao.MerchantStoreDao;
import com.salesmanager.core.business.merchant.model.MerchantRank;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.StoreList;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.user.service.UserService;

@Service("merchantRankService")
public class MerchantRankServiceImpl extends SalesManagerEntityServiceImpl<Integer, MerchantRank> 
		implements MerchantRankService {


	@Autowired
	private MerchantRankDao merchantRankDao;
	
	@Autowired
	public MerchantRankServiceImpl(MerchantRankDao merchantRankDao) {
		super(merchantRankDao);
		this.merchantRankDao = merchantRankDao;
	}

	
	@Override
	public List<MerchantRank> listRanks() {
		return this.merchantRankDao.list();
	}

}
