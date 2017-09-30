package com.salesmanager.core.business.catalog.product.dao.proof;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface ProductProofDao extends SalesManagerEntityDao<Long, ProductProof> {

	List<ProductProof> getByProduct(Product product);


}
