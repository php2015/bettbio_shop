package com.salesmanager.core.business.catalog.product.dao.browse;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.browse.ProductBrowse;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface ProductBrowseDao extends SalesManagerEntityDao<Long, ProductBrowse> {
	List<ProductBrowse> getByProductID (Long productID);
	void deleteByProductId(Long productID);

}
