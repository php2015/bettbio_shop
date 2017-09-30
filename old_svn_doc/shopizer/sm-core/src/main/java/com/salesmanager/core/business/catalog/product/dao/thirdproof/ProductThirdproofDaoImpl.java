package com.salesmanager.core.business.catalog.product.dao.thirdproof;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.model.thirdproof.QProductThirdproof;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productThirdproofDao")
public class ProductThirdproofDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductThirdproof>
		implements ProductThirdproofDao {

	@Override
	public List<ProductThirdproof> getByProduct(Product product) {
		QProductThirdproof qEntity = QProductThirdproof.productThirdproof;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.product,qProduct).fetch()
		.where(qProduct.id.eq(product.getId()));
		
		return query.list(qEntity);
	}
}
