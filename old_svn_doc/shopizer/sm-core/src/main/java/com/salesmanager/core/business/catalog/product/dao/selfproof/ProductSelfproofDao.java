package com.salesmanager.core.business.catalog.product.dao.selfproof;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface ProductSelfproofDao extends SalesManagerEntityDao<Long, ProductSelfproof> {

	List<ProductSelfproof> getByProduct(Product product);
}
