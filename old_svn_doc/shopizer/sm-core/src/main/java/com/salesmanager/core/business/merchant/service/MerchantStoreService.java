package com.salesmanager.core.business.merchant.service;

import java.util.List;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.StoreList;

public interface MerchantStoreService extends SalesManagerEntityService<Long, MerchantStore>{
	

	//Collection<Product> getProducts(MerchantStore merchantStore) throws ServiceException;
	
	//MerchantStore getMerchantStore(Integer merchantStoreId) throws ServiceException;

	MerchantStore getMerchantStore(String merchantStoreCode)
			throws ServiceException;
	
	MerchantStore getByCode(String code) throws ServiceException ;

	void saveOrUpdate(MerchantStore store) throws ServiceException;

	MerchantStore getByName(String name) throws ServiceException;
	
	StoreList getByCriteria(Criteria store)throws ServiceException;
	
	List<MerchantStore> getListByCriteria(Criteria criteria);
	
	List<Object[]> getStoreName();
	 public String queryByStoreFileName(long id);
}
