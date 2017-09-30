package com.salesmanager.core.business.catalog.product.service.attribute;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.attribute.OptionValueList;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductOptionValueService extends SalesManagerEntityService<Long, ProductOptionValue> {

	void saveOrUpdate(ProductOptionValue entity) throws ServiceException;

	List<ProductOptionValue> getByName(MerchantStore store, String name,
			Language language) throws ServiceException;

	ProductOptionValue getById(MerchantStore store, Long id)
			throws ServiceException;

	List<ProductOptionValue> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	List<ProductOptionValue> listByStoreNoReadOnly(MerchantStore store,
			Language language) throws ServiceException;

	ProductOptionValue getByCode(MerchantStore store, String optionValueCode);
	
	OptionValueList getByCriteria(Criteria criteria, Language language);
	
	List<ProductOptionValue> getListByCriteria(Criteria criteria);

}
