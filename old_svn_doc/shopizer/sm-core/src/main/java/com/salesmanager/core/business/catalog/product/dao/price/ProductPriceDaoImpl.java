package com.salesmanager.core.business.catalog.product.dao.price;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.availability.QProductAvailability;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.QProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.QProductPriceDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productPriceDao")
public class ProductPriceDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductPrice> 
	implements ProductPriceDao {
	
	@Override
	public ProductPrice getById(Long id) {
		QProductPrice qEntity = QProductPrice.productPrice;
		QProductAvailability qAvailability = QProductAvailability.productAvailability;
		QProductPriceDescription qEntityDescription = QProductPriceDescription.productPriceDescription;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.innerJoin(qEntity.productAvailability,qAvailability).fetch()
			.innerJoin(qAvailability.product,qProduct).fetch()
			.leftJoin(qEntity.descriptions, qEntityDescription).fetch()
			.innerJoin(qProduct.merchantStore).fetch()
			.where(qEntity.id.eq(id));
		
		List<ProductPrice> ms = query.list(qEntity);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	
	/**
	 * 批量更新主键ID！=id的其他规格价格的默认价格属性，更新为false
	 * @param id
	 */
	public void updateDefaultPrices(Long id) {
		Query query = getEntityManager().createQuery("update ProductPrice p set p.defaultPrice=false where p.id<>?1");
		query.setParameter(1, id);
		query.executeUpdate();
	}

	@Override
	public List<ProductPrice> getByProductID(Long id) {
		// TODO Auto-generated method stub
		QProductPrice qEntity = QProductPrice.productPrice;
		QProductAvailability qAvailability = QProductAvailability.productAvailability;
		QProductPriceDescription qEntityDescription = QProductPriceDescription.productPriceDescription;
		QProduct qProduct = QProduct.product;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.innerJoin(qEntity.productAvailability,qAvailability).fetch()
			.innerJoin(qAvailability.product,qProduct).fetch()
			.leftJoin(qEntity.descriptions, qEntityDescription).fetch()			
			.where(qProduct.id.eq(id));
		return query.list(qEntity);
	}

}
