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

@Service("merchantService")
public class MerchantStoreServiceImpl extends SalesManagerEntityServiceImpl<Long, MerchantStore> 
		implements MerchantStoreService {
	

		
	@Autowired
	protected ProductTypeService productTypeService;
	
	@Autowired
	private MerchantRankService merchantRankService;
	
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
	
	private MerchantStoreDao merchantStoreDao;
	
	@Autowired
	public MerchantStoreServiceImpl(MerchantStoreDao merchantStoreDao) {
		super(merchantStoreDao);
		this.merchantStoreDao = merchantStoreDao;
	}

	
	//@Override
	public MerchantStore getMerchantStore(String merchantStoreCode) throws ServiceException {
		return merchantStoreDao.getMerchantStore(merchantStoreCode);
	}
	
	@Override
	public void saveOrUpdate(MerchantStore store) throws ServiceException {
		
		if(store.getId()==null) {
			super.save(store);
		} else {
			super.update(store);
		}
	}
	

	
	//@Override
	//public Collection<Product> getProducts(MerchantStore merchantStore) throws ServiceException {
		

	//	return merchantStoreDao.getProducts(merchantStore);
		
		
	//}

	@Override
	public MerchantStore getByCode(String code) throws ServiceException {
		
		return merchantStoreDao.getMerchantStore(code);
	}
	
	@Override
	public MerchantStore getByName(String name) throws ServiceException {
		
		return merchantStoreDao.getByname(name);
	}
	
	@Override
	public void delete(MerchantStore merchant) throws ServiceException {
		
		merchant = this.getById(merchant.getId());
		
		
		//reference
		manufacturerService.deleteByProperties(new String[]{"merchantStore.id"}, new Object[]{merchant.getId()});
		/*List<Manufacturer> manufacturers = manufacturerService.listByStore(merchant);
		for(Manufacturer manufacturer : manufacturers) {
			manufacturerService.delete(manufacturer);
		}*/
		
		merchantConfigurationService.deleteByProperties(new String[]{"merchantStore.id"}, new Object[]{merchant.getId()});
		/*List<MerchantConfiguration> configurations = merchantConfigurationService.listByStore(merchant);
		for(MerchantConfiguration configuration : configurations) {
			merchantConfigurationService.delete(configuration);
		}*/
		

		//TODO taxService
		
		
		/*List<TaxClass> taxClasses = taxClassService.listByStore(merchant);
		for(TaxClass taxClass : taxClasses) {
			taxClassService.delete(taxClass);
		}*/
		
		//content
		contentService.removeFiles(merchant.getCode());
		//TODO staticContentService.removeImages
		
		//category / product
		
		List<Category> categories = categoryService.listByStore(merchant);
		for(Category category : categories) {
			categoryService.delete(category);
		}

		//users
		userService.deleteByProperties(new String[]{"merchantStore.id"}, new Object[]{merchant.getId()});
		/*List<User> users = userService.listByStore(merchant);
		for(User user : users) {
			userService.delete(user);
		}*/
		
		//customers
		CustomerCriteria criteria = new CustomerCriteria();
		criteria.setStoreId(merchant.getId());
		CustomerList customers = customerService.listByCriteria(criteria);
		if (customers!=null&&customers.getCustomers()!=null) {
			for(Customer customer : customers.getCustomers()) {
				customerService.delete(customer);
			}
		}
		
		//orders
		OrderCriteria odercriteria = new OrderCriteria();
		odercriteria.setStoreId(merchant.getId());
		OrderList orders = orderService.listByCriteria(odercriteria);
		if (orders!=null && orders.getOrders()!=null) {
			for(Order order : orders.getOrders()) {
				orderService.delete(order);
			}
		}
		
		super.delete(merchant);
		
	}


	@Override
	public StoreList getByCriteria(Criteria store)
			throws ServiceException {
		// TODO Auto-generated method stub
		return merchantStoreDao.getByCriteria(store);
	}
	
	@Override
	public List<MerchantStore> getListByCriteria(Criteria criteria) {
		return merchantStoreDao.getListByCriteria(criteria);
	}


	@Override
	public List<Object[]> getStoreName() {
		// TODO Auto-generated method stub
		return merchantStoreDao.getStoreName();
	}


	@Override
	public String queryByStoreFileName(long id) {
		// TODO Auto-generated method stub
		return this.merchantStoreDao.queryByStoreFileName(id);
	}


	@Override
	public int getMaxDiamondProductsNumber(MerchantStore store) {
		if (store.getRankProfil()!=null){
			Integer proNum = store.getRankProfil().getDiamondProductNumber();
			if (proNum != null && proNum >= 0){
				return proNum.intValue();
			}
			if (store.getRankProfil().getParentRank() != null){
				return store.getRankProfil().getParentRank().getDiamondProductNumber();
			}
		}
		
		List<MerchantRank> rankList = merchantRankService.listRanks();
		if (rankList != null && rankList.size() > 0){
			return rankList.get(0).getDiamondProductNumber();
		}
		return 1;
	}

	
}
