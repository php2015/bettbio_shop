package com.salesmanager.core.business.catalog.product.dao.attribute;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.attribute.OptionValueList;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOptionValueDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productOptionValueDao")
public class ProductOptionValueDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductOptionValue>
		implements ProductOptionValueDao {
	
	
	@Override
	public List<ProductOptionValue> listByStore(MerchantStore store, Language language) {
		
		QProductOptionValue qProductOption = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qDescription = QProductOptionValueDescription.productOptionValueDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.merchantStore.id.eq(store.getId())
			.and(qDescription.language.id.eq(language.getId())));
			query.orderBy(qProductOption.id.asc());
			
			
		
		return query.distinct().list(qProductOption);
				//listDistinct(qProductOption);
		
	}
	
	@Override
	public List<ProductOptionValue> listByStoreNoReadOnly(MerchantStore store, Language language) {
		
		QProductOptionValue qProductOption = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qDescription = QProductOptionValueDescription.productOptionValueDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.merchantStore.id.eq(store.getId())
			.and(qProductOption.productOptionDisplayOnly.eq(false))
			.and(qDescription.language.id.eq(language.getId())));
			query.orderBy(qProductOption.id.asc());
			
			
		
		return query.distinct().list(qProductOption);
				//listDistinct(qProductOption);
		
	}
	

	
	@Override
	public ProductOptionValue getById(MerchantStore store, Long id) {
		QProductOptionValue qProductOption = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qDescription = QProductOptionValueDescription.productOptionValueDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.id.eq(id)
			.and(qProductOption.merchantStore.id.eq(store.getId())));
		
		List<ProductOptionValue> ms = query.list(qProductOption);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	
	@Override
	public List<ProductOptionValue> getByName(MerchantStore store, String name, Language language) {
		QProductOptionValue qProductOption = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qDescription = QProductOptionValueDescription.productOptionValueDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qDescription.name.like("%" + name + "%")
			.and(qDescription.language.id.eq(language.getId()))
			.and(qProductOption.merchantStore.id.eq(store.getId())));
		

		
		List<ProductOptionValue> options = query.list(qProductOption);
		return options;
	}
	
	@Override
	public ProductOptionValue getByCode(MerchantStore store, String optionValueCode) {
		QProductOptionValue qProductOption = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qDescription = QProductOptionValueDescription.productOptionValueDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.code.eq(optionValueCode)
			.and(qProductOption.merchantStore.id.eq(store.getId())));
		
		List<ProductOptionValue> ms = query.list(qProductOption);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}		
	}

	@Override
	public OptionValueList getByCriteria(Criteria criteria,
			Language language) {
		// TODO Auto-generated method stub
		OptionValueList opList = new OptionValueList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(op) from ProductOptionValue as op ");
		countBuilderSelect.append("INNER JOIN op.descriptions de");
		StringBuilder countBuilderWhere = new StringBuilder();
		
		countBuilderWhere.append(" where de.language.id = " + language.getId());
				
		if(!StringUtils.isEmpty(criteria.getFindName())) {			
			countBuilderWhere.append(" and (de.name like:den )");
		}
		
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		if(criteria.getStoreId() != -1l){
			countBuilderWhere.append(" and op.merchantStore.id =" +criteria.getStoreId());
		}
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter("den",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());						
		}
		
		Number count = (Number) countQ.getSingleResult ();

		opList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return opList;
        
		QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qProductOptionValueDescription = QProductOptionValueDescription.productOptionValueDescription;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductOptionValue)
		.join(qProductOptionValue.descriptions,qProductOptionValueDescription).fetch()
		.join(qProductOptionValue.merchantStore,qMerchantStore).fetch()
		.orderBy(qProductOptionValue.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qProductOptionValueDescription.language.id.eq(language.getId()));
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			pBuilder.andAnyOf(qProductOptionValueDescription.name.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));					
		}
		
		if(criteria.getStoreId() != -1l){
			pBuilder.and(QMerchantStore.merchantStore.id.eq(criteria.getStoreId()));
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		opList.setValues(query.list(qProductOptionValue));
		return opList;
	}

	@Override
	public List<ProductOptionValue> getListByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		        
		QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qProductOptionValueDescription = QProductOptionValueDescription.productOptionValueDescription;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductOptionValue)
		.join(qProductOptionValue.descriptions,qProductOptionValueDescription).fetch()
		.join(qProductOptionValue.merchantStore,qMerchantStore).fetch()
		.orderBy(qProductOptionValue.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		
		if(criteria.getStoreId() != -1l){
			pBuilder.and(QMerchantStore.merchantStore.id.eq(criteria.getStoreId()));
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		return query.list(qProductOptionValue);
	}



}
