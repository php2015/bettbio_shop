package com.salesmanager.core.business.catalog.product.service.manufacturer;

import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureList;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ManufacturerService extends SalesManagerEntityService<Long, Manufacturer> {

	List<Manufacturer> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	List<Manufacturer> listByStore(MerchantStore store) throws ServiceException;

	void saveOrUpdate(Manufacturer manufacturer,Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap) throws ServiceException;
	
	void addManufacturerDescription(Manufacturer manufacturer, ManufacturerDescription description) throws ServiceException;
	
	int getCountManufAttachedProducts( Manufacturer manufacturer )  throws ServiceException;
	
	void delete(Manufacturer manufacturer,Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap) throws ServiceException;

	/**
	 * List manufacturers by products from a given list of categories
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	List<Manufacturer> listByProductsByCategoriesId(List<Long> ids, Language language) throws ServiceException;
	
	ManufactureList getByCriteria(Criteria criteria,Language language) throws ServiceException;
	
	List<Manufacturer> getListByCriteria(Criteria criteria);

	List<Manufacturer> getListByName(String name);
	
	List<Manufacturer> listByProductsByCriteria(ProductCriteria criteria, Language language);
	
	List<Object[]> getStoreName();
	//授权
	public ManufactureList queryByManufacturer();
	public List<ManufacturerDescription> queryByManufacturerId(Long id);
	public Integer queryByAuthorization(Long mid);
	
}
