package com.salesmanager.core.business.catalog.product.service.selfproof;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductSelfproofService extends SalesManagerEntityService<Long, ProductSelfproof> {

	public List<ProductSelfproof> getByProduct(Product product);
	void saveOrUpdate(Product product,ProductSelfproof productSelfproof);	
}
