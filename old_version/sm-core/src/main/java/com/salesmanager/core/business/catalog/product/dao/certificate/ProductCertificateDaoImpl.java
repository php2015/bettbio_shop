package com.salesmanager.core.business.catalog.product.dao.certificate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.model.certificate.QProductCertificate;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productCertificateDao")
public class ProductCertificateDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductCertificate>
		implements ProductCertificateDao {

	@Override
	public List<ProductCertificate> getByProduct(Product product) {
		QProductCertificate qEntity = QProductCertificate.productCertificate;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.product,qProduct).fetch()
		.where(qProduct.id.eq(product.getId()));
		
		return query.list(qEntity);
	}

}
