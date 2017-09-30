package com.salesmanager.core.business.catalog.product.dao.selfproof;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.model.selfproof.QProductSelfproof;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productSelfproofDao")
public class ProductSelfproofDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductSelfproof>
		implements ProductSelfproofDao {

	@Override
	public List<ProductSelfproof> getByProduct(Product product) {
		QProductSelfproof qEntity = QProductSelfproof.productSelfproof;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.product,qProduct).fetch()
		.where(qProduct.id.eq(product.getId()));
		
		return query.list(qEntity);
	}
}
