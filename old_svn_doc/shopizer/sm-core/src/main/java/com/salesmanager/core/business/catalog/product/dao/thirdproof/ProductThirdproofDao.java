package com.salesmanager.core.business.catalog.product.dao.thirdproof;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface ProductThirdproofDao extends SalesManagerEntityDao<Long, ProductThirdproof> {

	List<ProductThirdproof> getByProduct(Product product);


}
