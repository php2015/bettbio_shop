package com.salesmanager.core.business.catalog.product.dao.price;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface ProductPriceDao extends SalesManagerEntityDao<Long, ProductPrice> {

	public void updateDefaultPrices(Long id);
	public List<ProductPrice> getByProductID(Long id);
}
