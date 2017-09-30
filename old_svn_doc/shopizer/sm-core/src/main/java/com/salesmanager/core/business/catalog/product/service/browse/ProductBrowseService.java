package com.salesmanager.core.business.catalog.product.service.browse;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.browse.ProductBrowse;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductBrowseService extends
		SalesManagerEntityService<Long, ProductBrowse> {
		List<ProductBrowse> getByProductID (Long productID);
		void deleteByProductId(Long productID);
}
