package com.salesmanager.core.business.catalog.product.dao.image;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.image.QProductImage;
import com.salesmanager.core.business.catalog.product.model.image.QProductImageDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Repository("productImageDao")
public class ProductImageDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductImage> 
	implements ProductImageDao {
	
	@Override
	public ProductImage getProductImageById(Long id) {
		
		QProductImage qProductImage = QProductImage.productImage1;
		QProductImageDescription qProductImageDescription = QProductImageDescription.productImageDescription;
		QProduct qProduct = QProduct.product;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductImage)
			.leftJoin(qProductImage.descriptions, qProductImageDescription).fetch()
			.innerJoin(qProductImage.product,qProduct).fetch()
			.innerJoin(qProduct.merchantStore).fetch()
			.where(qProductImage.id.eq(id));
		
		List<ProductImage> ms = query.list(qProductImage);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}

}
