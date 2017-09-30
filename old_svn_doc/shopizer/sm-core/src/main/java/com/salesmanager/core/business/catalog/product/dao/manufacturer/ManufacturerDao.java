package com.salesmanager.core.business.catalog.product.dao.manufacturer;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureList;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ManufacturerDao extends SalesManagerEntityDao<Long, Manufacturer> {

	List<Manufacturer> listByStore(MerchantStore store, Language language);

	List<Manufacturer> listByStore(MerchantStore store);
	
	int getCountManufAttachedProducts(  Manufacturer manufacturer  );

	List<Manufacturer> listByProductsByCategoriesId(List<Long> ids, Language language);

	ManufactureList getByCriteria(Criteria criteria,Language language);
	
	List<Manufacturer> getListByCriteria(Criteria criteria);

	List<Manufacturer> getListByName(String name);
	
	List<Manufacturer> listByProductsByCriteria(ProductCriteria criteria, Language language);
	
	List<Object[]> getStoreName();
	//查询全部的品牌
	public ManufactureList queryByManufacturer();
	//获取产品的品牌mid
    public List<ManufacturerDescription> queryByManufacturerId(Long id);
    //判定该产品是否已经被授权了
    public Integer queryByAuthorization(Long mid);
}
