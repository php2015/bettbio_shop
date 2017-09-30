package com.salesmanager.core.business.catalog.product.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.catalog.product.model.file.QDigitalProduct;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Repository("digitalProductDao")
public class DigitalProductDaoImpl extends SalesManagerEntityDaoImpl<Long, DigitalProduct> 
	implements DigitalProductDao {
	
	@Override
	public List<DigitalProduct> getByProduct(MerchantStore store, Product product) {
		
		QDigitalProduct qDigitalProduct = QDigitalProduct.digitalProduct;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qDigitalProduct)
			.innerJoin(qDigitalProduct.product, qProduct).fetch()
			.innerJoin(qProduct.merchantStore).fetch()
			.where(qProduct.merchantStore.id.eq(store.getId())
					.and(qProduct.id.eq(product.getId())));
		
		List<DigitalProduct> results = query.list(qDigitalProduct);
        if (results.isEmpty()){
        	return null;
        }else{
        	return results;
        } 
	}
	
	@Override
	public DigitalProduct getById(Long id) {
		
		QDigitalProduct qDigitalProduct = QDigitalProduct.digitalProduct;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qDigitalProduct)
			.innerJoin(qDigitalProduct.product, qProduct).fetch()
			.innerJoin(qProduct.merchantStore).fetch()
					.where(qDigitalProduct.id.eq(id));
		
		List<DigitalProduct> ms = query.list(qDigitalProduct);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	
	public List<DigitalProduct> queryByDigitalProduct(Long id){
		
		QDigitalProduct qDigitalProduct = QDigitalProduct.digitalProduct;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qDigitalProduct)
			.innerJoin(qDigitalProduct.product, qProduct).fetch()
			.innerJoin(qProduct.merchantStore).fetch()
					.where(qProduct.id.eq(id));
		
		List<DigitalProduct> ms = query.list(qDigitalProduct);
		return ms;
		
		
	}
	
}
