package com.salesmanager.core.business.merchant.dao;

import java.util.Collection;
import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.StoreList;

public interface MerchantStoreDao extends SalesManagerEntityDao<Long, MerchantStore> {
	
	public Collection<Product> getProducts(MerchantStore merchantStore) throws ServiceException;
	
	public MerchantStore getMerchantStore(Long merchantStoreId);

	public MerchantStore getMerchantStore(String code) throws ServiceException;

	public MerchantStore getByname(String name) throws ServiceException;
	
	StoreList getByCriteria(Criteria store) throws ServiceException;
	
	List<MerchantStore> getListByCriteria(Criteria criteria);
	
	List<Object[]> getStoreName();
	public String queryByStoreFileName(long id);
}
