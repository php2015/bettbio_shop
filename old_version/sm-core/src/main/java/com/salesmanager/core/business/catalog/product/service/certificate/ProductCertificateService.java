package com.salesmanager.core.business.catalog.product.service.certificate;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductCertificateService extends SalesManagerEntityService<Long, ProductCertificate> {

	List<ProductCertificate> getByProduct(Product product);
	void saveOrUpdate(ProductCertificate certificate);
	void deletC(ProductCertificate certificate);
}
