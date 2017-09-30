package com.salesmanager.core.business.catalog.product.dao.certificate;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface ProductCertificateDao extends SalesManagerEntityDao<Long, ProductCertificate> {

	List<ProductCertificate> getByProduct(Product product);

}
