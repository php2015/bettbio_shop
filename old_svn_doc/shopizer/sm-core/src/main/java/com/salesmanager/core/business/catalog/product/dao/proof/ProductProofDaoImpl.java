package com.salesmanager.core.business.catalog.product.dao.proof;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.model.proof.QProductProof;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productProofDao")
public class ProductProofDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductProof>
		implements ProductProofDao {

	@Override
	public List<ProductProof> getByProduct(Product product) {
		QProductProof qEntity = QProductProof.productProof;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.product,qProduct).fetch()
		.where(qProduct.id.eq(product.getId()));
		
		return query.list(qEntity);
	}
}
