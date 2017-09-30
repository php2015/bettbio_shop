package com.salesmanager.core.business.catalog.product.service.proof;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductProofService extends SalesManagerEntityService<Long, ProductProof> {

	public List<ProductProof> getByProduct(Product product);
	void saveOrUpdate(ProductProof productProof);
	void deletp(ProductProof productProof);
}
