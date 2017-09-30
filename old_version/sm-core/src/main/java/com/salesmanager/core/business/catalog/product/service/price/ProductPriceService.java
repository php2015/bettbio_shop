package com.salesmanager.core.business.catalog.product.service.price;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductPriceService extends SalesManagerEntityService<Long, ProductPrice> {

	void addDescription(ProductPrice price, ProductPriceDescription description) throws ServiceException;

	void saveOrUpdate(ProductPrice price) throws ServiceException;
	
	/**
	 * 批量更新主键ID！=id的其他规格价格的默认价格属性，更新为false
	 * @param id
	 */
	public void updateDefaultPrices(Long id);

	void saveOrUpdateList(List<ProductPrice> prices) throws ServiceException;
	
	public List<ProductPrice> getByProductID(Long id);
}
