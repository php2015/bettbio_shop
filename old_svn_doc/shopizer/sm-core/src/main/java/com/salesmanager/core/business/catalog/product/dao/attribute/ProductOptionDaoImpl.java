package com.salesmanager.core.business.catalog.product.dao.attribute;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.attribute.OptionList;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOptionDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productOptionDao")
public class ProductOptionDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductOption>
		implements ProductOptionDao {
	
	@Override
	public List<ProductOption> listByStore(MerchantStore store, Language language) {
		
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.merchantStore.id.eq(store.getId())
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qProductOption.id.asc());
		
		return query.distinct().list(qProductOption);
				//listDistinct(qProductOption);
		
	}
	
	@Override
	public ProductOption getById(Long id) {
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.id.eq(id));
		
		List<ProductOption> ms = query.list(qProductOption);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	
	@Override
	public List<ProductOption> getByName(MerchantStore store, String name, Language language) {
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qDescription.name.like("%" + name + "%")
			.and(qDescription.language.id.eq(language.getId()))
			.and(qProductOption.merchantStore.id.eq(store.getId())));
		

		
		List<ProductOption> options = query.list(qProductOption);
		return options;
	}
	
	@Override
	public List<ProductOption> getReadOnly(MerchantStore store, Language language) {
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.readOnly.eq(true)
			.and(qDescription.language.id.eq(language.getId()))
			.and(qProductOption.merchantStore.id.eq(store.getId())));
		

		
		List<ProductOption> options = query.list(qProductOption);
		return options;
	}
	
	@Override
	public void saveOrUpdate(ProductOption entity) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {
			super.update(entity);
		} else {
			super.save(entity);
		}
	}
	
	@Override
	public ProductOption getByCode(MerchantStore store, String optionCode) {
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.merchantStore.id.eq(store.getId())
			.and(qProductOption.code.eq(optionCode)));
		
		List<ProductOption> ms = query.list(qProductOption);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}

	@Override
	public OptionList getByCriteria(Criteria criteria, Language language) {
		// TODO Auto-generated method stub
		OptionList opList = new OptionList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(op) from ProductOption as op ");
		countBuilderSelect.append("INNER JOIN op.descriptions de");
		StringBuilder countBuilderWhere = new StringBuilder();
		
		countBuilderWhere.append(" where de.language.id = " + language.getId());
				
		if(!StringUtils.isEmpty(criteria.getFindName())) {			
			countBuilderWhere.append(" and (de.name like:den or op.productOptionType like:ty )");
		}
		
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		if(criteria.getStoreId() != -1l){
			countBuilderWhere.append(" and op.merchantStore.id =" +criteria.getStoreId());
		}
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter("den",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
			countQ.setParameter("ty",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());			
		}
		
		Number count = (Number) countQ.getSingleResult ();

		opList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return opList;
        
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qProductOptionDescription = QProductOptionDescription.productOptionDescription;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductOption)
		.join(qProductOption.descriptions,qProductOptionDescription).fetch()
		.join(qProductOption.merchantStore,qMerchantStore).fetch()
		.orderBy(qProductOption.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qProductOptionDescription.language.id.eq(language.getId()));
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			pBuilder.andAnyOf(qProductOption.productOptionType.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qProductOptionDescription.name.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
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
		
		opList.setOptions(query.list(qProductOption));
		return opList;
	}

	@Override
	public List<ProductOption> getListByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		
        
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qProductOptionDescription = QProductOptionDescription.productOptionDescription;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductOption)
		.join(qProductOption.descriptions,qProductOptionDescription).fetch()
		.join(qProductOption.merchantStore,qMerchantStore).fetch()
		.orderBy(qProductOption.id.asc());
		
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
		
		return query.list(qProductOption);
	}



}
