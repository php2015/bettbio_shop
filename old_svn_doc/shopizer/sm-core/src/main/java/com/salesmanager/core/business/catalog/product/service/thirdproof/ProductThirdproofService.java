package com.salesmanager.core.business.catalog.product.service.thirdproof;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductThirdproofService extends SalesManagerEntityService<Long, ProductThirdproof> {

	public List<ProductThirdproof> getByProduct(Product product);
	void saveOrUpdate(ProductThirdproof productThirdproof);
	void delett(ProductThirdproof productThirdproof);
}
