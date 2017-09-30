package com.salesmanager.core.business.catalog.product.dao.browse;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.browse.ProductBrowse;
import com.salesmanager.core.business.catalog.product.model.browse.QProductBrowse;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productBrowseDao")
public class ProductBrowseDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductBrowse>
		implements ProductBrowseDao {

	@Override
	public List<ProductBrowse> getByProductID(Long productID) {
		// TODO Auto-generated method stub
		//List<ProductBrowse> pb = new ArrayList<ProductBrowse>();
		QProductBrowse qProductBrowse =  QProductBrowse.productBrowse;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductBrowse);
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qProductBrowse.product.id.eq(productID));
		query.where(pBuilder);
		return query.list(qProductBrowse);
	}

	@Override
	public void deleteByProductId(Long productID) {
		// TODO Auto-generated method stub
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("delete from ProductBrowse as pb ");		
		StringBuilder countBuilderWhere = new StringBuilder();
	
		countBuilderWhere.append(" where pb.product.id = ").append(productID);
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		countQ.executeUpdate();
	}

	
}
