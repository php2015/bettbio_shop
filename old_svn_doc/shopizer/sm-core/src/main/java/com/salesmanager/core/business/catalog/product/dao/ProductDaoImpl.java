package com.salesmanager.core.business.catalog.product.dao;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.attribute.AttributeCriteria;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.constants.Constants;

@Repository("productDao")
public class ProductDaoImpl extends SalesManagerEntityDaoImpl<Long, Product> implements ProductDao {
	
	SessionFactory sessionFactory;
	public ProductDaoImpl() {
		super();
	}
	
	@Override
	public Product getBySeUrl(MerchantStore store,String seUrl, Locale locale) {
		
		
		//List<String> regionList = new ArrayList<String>();
		//regionList.add("*");
		//regionList.add(locale.getCountry());
		

		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		//qs.append("join fetch p.availabilities pa ");
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		qs.append("join fetch p.merchantStore pm ");
		//qs.append("left join fetch pa.prices pap ");
		//qs.append("left join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");
		
		//options
		//qs.append("left join fetch p.attributes pattr ");
		//qs.append("left join fetch pattr.productOption po ");
		//qs.append("left join fetch po.descriptions pod ");
		//qs.append("left join fetch pattr.productOptionValue pov ");
		//qs.append("left join fetch pov.descriptions povd ");
		//qs.append("left join fetch p.relationships pr ");
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");		
		
		//qs.append("where pa.region in (:lid) ");
		qs.append("where p.id=:seUrl ");
		qs.append("and p.available=true and p.dateAvailable<=:dt ");
		//qs.append("order by pattr.productOptionSortOrder ");


    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	//q.setParameter("lid", regionList);
    	q.setParameter("dt", new Date());
    	q.setParameter("seUrl", Long.parseLong(seUrl));

    	Product p = null;
    	
    	try {
    		List<Product> ms = q.getResultList();
			if(ms !=null && ms.size()>0){
				return ms.get(0);
			}else  {
				return null;
			}		
    		//p = (Product)q.getSingleResult();
    	} catch(javax.persistence.NoResultException ignore) {

    	}
    			
    			


		return p;
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Product> getProductsForLocale(MerchantStore store, Set categoryIds, Language language, Locale locale) {
		
		ProductList products = this.getProductsListForLocale(store, categoryIds, language, locale, 0, -1);
		
		return products.getProducts();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Product> getProductsListByCategories(Set categoryIds) {
		

		//List regionList = new ArrayList();
		//regionList.add("*");
		//regionList.add(locale.getCountry());
		

		//TODO Test performance 
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */

		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");

		
		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch p.type type ");		
		
		//qs.append("where pa.region in (:lid) ");
		qs.append("where categs.id in (:cid)");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", categoryIds);


    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	
    	return products;


	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Product> getProductsListByMan(Set categoryIds,Long merchantId) {
		

		//List regionList = new ArrayList();
		//regionList.add("*");
		//regionList.add(locale.getCountry());
		

		//TODO Test performance 
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */

		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");

		
		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch p.type type ");		
		
		//qs.append("where pa.region in (:lid) ");
		qs.append("where p.manufacturer.id in (:cid) and p.merchantStore.id=:merchantId");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", categoryIds);
    	q.setParameter("merchantId", merchantId);



    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	
    	return products;


	}
	
	@Override
	public List<Product> getProductsListByCategories(Set<Long> categoryIds, Language language) {
		

		//List regionList = new ArrayList();
		//regionList.add("*");
		//regionList.add(locale.getCountry());
		

		//TODO Test performance 
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */

		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");

		
		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");		
		
		//qs.append("where pa.region in (:lid) ");
		qs.append("where categs.id in (:cid) ");
		//qs.append("and pd.language.id=:lang and papd.language.id=:lang and manufd.language.id=:lang ");
		qs.append("and pd.language.id=:lang and papd.language.id=:lang ");
		qs.append("and p.available=true and p.dateAvailable<=:dt ");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", categoryIds);
    	q.setParameter("lang", language.getId());
    	q.setParameter("dt", new Date());

    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	
    	return products;


	}
	
	
/*	@Override
	public ProductList getProductListByCategories(ProductCriteria criteria, Set<Long> categoryIds, Language language) {
		

		//List regionList = new ArrayList();
		//regionList.add("*");
		//regionList.add(locale.getCountry());
		

		//TODO Test performance 
		*//**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 *//*

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		
		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
				
		//qs.append("where pa.region in (:lid) ");
		qs.append("where categs.id in (:cid) ");
		qs.append("and pd.language.id=:lang and papd.language.id=:lang ");
		qs.append("and p.available=true and p.dateAvailable<=:dt ");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", categoryIds);
    	q.setParameter("lang", language.getId());
    	q.setParameter("dt", new Date());

    	q.setFirstResult(criteria.getStartIndex());
    	q.setMaxResults(criteria.getMaxCount());
    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	ProductList productList = new ProductList();
    	productList.setProducts(products);
    	
    	
    	return productList;


	}*/
	

	
	
	@Override
	public List<Product> listByStore(MerchantStore store) {

		
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");

		
		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
				
		//qs.append("where pa.region in (:lid) ");
		qs.append("where merch.id=:mid");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("mid", store.getId());


    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	
    	return products;
		
		
	}
	
	
	/**
	 * Used for all purpose !
	 * @param store
	 * @param first
	 * @param max
	 * @return
	 */
	@Override
	public ProductList listByStore(MerchantStore store, Language language, ProductCriteria criteria) {

		ProductList productList = new ProductList();

        
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(distinct p) from Product as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		countBuilderWhere.append(" where p.merchantStore.id=:mId");
		
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			countBuilderWhere.append(" and p.id in (:pId)");
		}

		if(!StringUtils.isBlank(criteria.getProductName())) {
			countBuilderSelect.append(" INNER JOIN p.descriptions pd");
			countBuilderWhere.append(" and pd.language.id=:lang and lower(pd.name) like:nm");
		}
		
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			countBuilderSelect.append(" INNER JOIN p.categories categs");
			countBuilderWhere.append(" and categs.id in (:cid)");
		}
		
		if(criteria.getManufacturers()!=null) {
			countBuilderSelect.append(" INNER JOIN p.manufacturer manuf");
			countBuilderWhere.append(" and manuf.id in (:manufid)");
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			
			countBuilderWhere.append(" and lower(p.sku) like :sku");
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
		
			countBuilderSelect.append(" INNER JOIN p.attributes pattr");
			countBuilderSelect.append(" INNER JOIN pattr.productOption po");
			countBuilderSelect.append(" INNER JOIN pattr.productOptionValue pov ");
			countBuilderSelect.append(" INNER JOIN pov.descriptions povd ");
			int count = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				if(count==0) {
					countBuilderWhere.append(" and po.code =:").append(attributeCriteria.getAttributeCode());
					countBuilderWhere.append(" and povd.description like :").append("val").append(count).append(attributeCriteria.getAttributeCode());
				} 
				count++;
			}
			countBuilderWhere.append(" and povd.language.id=:lang");

		}
		
		
		if(criteria.getAvailableP()!=null) {
			if(criteria.getAvailableP().booleanValue()) {
				countBuilderWhere.append(" and p.available=true and p.dateAvailable<=:dt");
			} else {
				countBuilderWhere.append(" and p.available=false or p.dateAvailable>:dt");
			}
		}

		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		countQ.setParameter("mId", store.getId());
		
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			countQ.setParameter("cid", criteria.getCategoryIds());
		}
		

		if(criteria.getAvailableP()!=null) {
			countQ.setParameter("dt", new Date());
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			countQ.setParameter("sku", new StringBuilder().append("%").append(criteria.getCode().toLowerCase()).append("%").toString());
		}
		
		if(criteria.getManufacturers()!=null) {
			countQ.setParameter("manufid", criteria.getManufacturers());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			int count = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				countQ.setParameter(attributeCriteria.getAttributeCode(),attributeCriteria.getAttributeCode());
				countQ.setParameter("val" + count + attributeCriteria.getAttributeCode(),"%" + attributeCriteria.getAttributeValue() + "%");
				count ++;
			}
			countQ.setParameter("lang", language.getId());
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			countQ.setParameter("nm", new StringBuilder().append("%").append(criteria.getProductName().toLowerCase()).append("%").toString());
			countQ.setParameter("lang", language.getId());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			countQ.setParameter("pId", criteria.getProductIds());
		}

		Number count = (Number) countQ.getSingleResult ();

		productList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return productList;

		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("left join fetch p.categories categs ");
		

		//images
		qs.append("left join fetch p.images images ");
		
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");

		

		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
		
		
		
		//attributes
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			qs.append(" inner join p.attributes pattr ");
			qs.append(" inner join pattr.productOption po ");
			qs.append(" inner join po.descriptions pod ");
			qs.append(" inner join pattr.productOptionValue pov ");
			qs.append(" inner join pov.descriptions povd ");
		} else {
			qs.append(" left join fetch p.attributes pattr ");
			qs.append(" left join fetch pattr.productOption po ");
			qs.append(" left join fetch po.descriptions pod ");
			qs.append(" left join fetch pattr.productOptionValue pov ");
			qs.append(" left join fetch pov.descriptions povd ");
		}
		
		qs.append(" left join fetch p.relationships pr ");
		

		qs.append(" where merch.id=:mId ");
		qs.append(" and pd.language.id=:lang ");
		
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			qs.append(" and p.id in (:pId) ");
		}
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			qs.append(" and categs.id in (:cid) ");
		}
		
		if(criteria.getManufacturers()!=null) {
			qs.append(" and manuf.id in (:manufid) ");
		}
		

		if(criteria.getAvailableP()!=null) {
			if(criteria.getAvailableP().booleanValue()) {
				qs.append(" and p.available=true and p.dateAvailable<=:dt ");
			} else {
				qs.append(" and p.available=false and p.dateAvailable>:dt ");
			}
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			qs.append(" and lower(pd.name) like :nm ");
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			qs.append(" and lower(p.sku) like :sku ");
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			int cnt = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				qs.append(" and po.code =:").append(attributeCriteria.getAttributeCode());
				qs.append(" and povd.description like :").append("val").append(cnt).append(attributeCriteria.getAttributeCode());
				cnt++;
			}
			qs.append(" and povd.language.id=:lang ");

		}


    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	q.setParameter("lang", language.getId());
    	q.setParameter("mId", store.getId());
    	
    	if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
    		q.setParameter("cid", criteria.getCategoryIds());
    	}
    	
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			q.setParameter("pId", criteria.getProductIds());
		}
		
		if(criteria.getAvailableP()!=null) {
			q.setParameter("dt", new Date());
		}
		
		if(criteria.getManufacturers()!=null) {
			q.setParameter("manufid", criteria.getManufacturers());
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			q.setParameter("sku", new StringBuilder().append("%").append(criteria.getCode().toLowerCase()).append("%").toString());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			int cnt = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				q.setParameter(attributeCriteria.getAttributeCode(),attributeCriteria.getAttributeCode());
				q.setParameter("val" + cnt  + attributeCriteria.getAttributeCode(),"%" + attributeCriteria.getAttributeValue() + "%");
				cnt++;
			}
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			q.setParameter("nm", new StringBuilder().append("%").append(criteria.getProductName().toLowerCase()).append("%").toString());
		}
    	
    	if(criteria.getMaxCount()>0) {
    		
    		
	    	q.setFirstResult(criteria.getStartIndex());
	    	if(criteria.getMaxCount()<count.intValue()) {
	    		q.setMaxResults(criteria.getMaxCount());
	    	}
	    	else {
	    		q.setMaxResults(count.intValue());
	    	}
    	}
    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();
    	productList.setProducts(products);
    	
    	return productList;

		
		
		
	}
	
	
	
	/**
	 * Used for all purpose !	 * 
	 * @param first
	 * @param max
	 * @return
	 */
	@Override
	public ProductList listByCriteria(Language language, ProductCriteria criteria) {

		ProductList productList = new ProductList();

        
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(distinct p) from Product as p");
		
		StringBuilder countBuilderWhere = new StringBuilder(" where 1=1 ");
		//countBuilderWhere.append(" where p.merchantStore.id=:mId");
		
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			countBuilderWhere.append(" and p.id in (:pId)");
		}

		if(criteria.getStoreId() != -1l){
			countBuilderWhere.append(" and p.merchantStore.id=:mId");
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			countBuilderSelect.append(" INNER JOIN p.descriptions pd");
			countBuilderWhere.append(" and pd.language.id=:lang and lower(pd.name) like:nm");
		}
		
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			countBuilderSelect.append(" INNER JOIN p.categories categs");
			
			countBuilderWhere.append(" and categs.id in (:cid)");
		}
		
		if(criteria.getManufacturers()!=null) {
			countBuilderSelect.append(" INNER JOIN p.manufacturer manuf");
			countBuilderWhere.append(" and manuf.id in (:manufid)");
		}
		
		if(criteria.isCerticate() == true){
			countBuilderSelect.append(" left JOIN p.certificates certs");
			countBuilderWhere.append(" and certs.id is not null");
		}
		
		
		if(criteria.isProof() == true){
			countBuilderSelect.append(" left JOIN p.proofs proof");
			countBuilderWhere.append(" and proof.id is not null");
		}
		
		if(criteria.isThird() == true){
			countBuilderSelect.append(" left JOIN p.thirdproofs third");
			countBuilderWhere.append(" and third.id is not null");
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			
			countBuilderWhere.append(" and lower(p.sku) like :sku");
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
		
			countBuilderSelect.append(" INNER JOIN p.attributes pattr");
			countBuilderSelect.append(" INNER JOIN pattr.productOption po");
			countBuilderSelect.append(" INNER JOIN pattr.productOptionValue pov ");
			countBuilderSelect.append(" INNER JOIN pov.descriptions povd ");
			int count = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				if(count==0) {
					countBuilderWhere.append(" and po.code =:").append(attributeCriteria.getAttributeCode());
					countBuilderWhere.append(" and povd.description like :").append("val").append(count).append(attributeCriteria.getAttributeCode());
				} 
				count++;
			}
			countBuilderWhere.append(" and povd.language.id=:lang");

		}
		
		
		if(criteria.getAvailableP()!=null) {
			if(criteria.getAvailableP().booleanValue()) {
				countBuilderWhere.append(" and p.available=true and p.dateAvailable<=:dt");
			} else {
				countBuilderWhere.append(" and p.available=false or p.dateAvailable>:dt");
			}
		}
		
		//modify and to where	
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString());
		if(null != countBuilderWhere && countBuilderWhere.length()>0){
			countQ = super.getEntityManager().createQuery(
					countBuilderSelect.toString() + addWhere(countBuilderWhere));
		}
		
		if(criteria.getStoreId() != -1l){
			countQ.setParameter("mId", criteria.getStoreId());
		}
		//countQ.setParameter("mId", store.getId());
		
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			countQ.setParameter("cid", criteria.getCategoryIds());
		}
		

		if(criteria.getAvailableP()!=null) {
			countQ.setParameter("dt", new Date());
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			countQ.setParameter("sku", new StringBuilder().append("%").append(criteria.getCode().toLowerCase()).append("%").toString());
		}
		
		if(criteria.getManufacturers()!=null) {
			countQ.setParameter("manufid", criteria.getManufacturers());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			int count = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				countQ.setParameter(attributeCriteria.getAttributeCode(),attributeCriteria.getAttributeCode());
				countQ.setParameter("val" + count + attributeCriteria.getAttributeCode(),"%" + attributeCriteria.getAttributeValue() + "%");
				count ++;
			}
			countQ.setParameter("lang", language.getId());
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			countQ.setParameter("nm", new StringBuilder().append("%").append(criteria.getProductName().toLowerCase()).append("%").toString());
			countQ.setParameter("lang", language.getId());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			countQ.setParameter("pId", criteria.getProductIds());
		}

		Number count = (Number) countQ.getSingleResult ();

		productList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return productList;

		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		//qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("left join fetch p.categories categs ");
		

		//images
		qs.append("left join fetch p.images images ");
		
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof
		qs.append("left join fetch p.selfProofs selfProofs ");
		

		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
		
		
		
		//attributes
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			qs.append(" inner join p.attributes pattr ");
			qs.append(" inner join pattr.productOption po ");
			qs.append(" inner join po.descriptions pod ");
			qs.append(" inner join pattr.productOptionValue pov ");
			qs.append(" inner join pov.descriptions povd ");
		} else {
			qs.append(" left join fetch p.attributes pattr ");
			qs.append(" left join fetch pattr.productOption po ");
			qs.append(" left join fetch po.descriptions pod ");
			qs.append(" left join fetch pattr.productOptionValue pov ");
			qs.append(" left join fetch pov.descriptions povd ");
		}
		
		qs.append(" left join fetch p.relationships pr ");
		qs.append(" left join fetch p.merchantStore merch ");

		qs.append(" where pd.language.id=:lang ");
		
		if (criteria.getStoreId() != -1l) {
			qs.append(" and merch.id=:mId ");
		}
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			qs.append(" and p.id in (:pId) ");
		}
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			qs.append(" and categs.id in (:cid) ");
		}
		
		if(criteria.getManufacturers()!=null) {
			qs.append(" and manuf.id in (:manufid) ");
		}
		

		if(criteria.getAvailableP()!=null) {
			if(criteria.getAvailableP().booleanValue()) {
				qs.append(" and p.available=true and p.dateAvailable<=:dt ");
			} else {
				qs.append(" and p.available=false and p.dateAvailable>:dt ");
			}
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			qs.append(" and lower(pd.name) like :nm ");
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			qs.append(" and lower(p.sku) like :sku ");
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			int cnt = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				qs.append(" and po.code =:").append(attributeCriteria.getAttributeCode());
				qs.append(" and povd.description like :").append("val").append(cnt).append(attributeCriteria.getAttributeCode());
				cnt++;
			}
			qs.append(" and povd.language.id=:lang ");

		}

		if(criteria.isCerticate() == true){
			qs.append(" and certificates.id is not null");
		}
		
		
		if(criteria.isProof() == true){
			qs.append(" and proofs.id is not null");
		}
		
		if(criteria.isThird() == true){
			qs.append(" and thirdproofs.id is not null");
		}

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	q.setParameter("lang", language.getId());
    	if (criteria.getStoreId() != -1l) {
    		q.setParameter("mId", criteria.getStoreId());
		}
    	
    	if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
    		q.setParameter("cid", criteria.getCategoryIds());
    	}
    	
		if(!CollectionUtils.isEmpty(criteria.getProductIds())) {
			q.setParameter("pId", criteria.getProductIds());
		}
		
		if(criteria.getAvailableP()!=null) {
			q.setParameter("dt", new Date());
		}
		
		if(criteria.getManufacturers()!=null) {
			q.setParameter("manufid", criteria.getManufacturers());
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			q.setParameter("sku", new StringBuilder().append("%").append(criteria.getCode().toLowerCase()).append("%").toString());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			int cnt = 0;
			for(AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				q.setParameter(attributeCriteria.getAttributeCode(),attributeCriteria.getAttributeCode());
				q.setParameter("val" + cnt  + attributeCriteria.getAttributeCode(),"%" + attributeCriteria.getAttributeValue() + "%");
				cnt++;
			}
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			q.setParameter("nm", new StringBuilder().append("%").append(criteria.getProductName().toLowerCase()).append("%").toString());
		}
    	
    	if(criteria.getMaxCount()>0) {
    		
    		
	    	q.setFirstResult(criteria.getStartIndex());
	    	if(criteria.getMaxCount()<count.intValue()) {
	    		q.setMaxResults(criteria.getMaxCount());
	    	}
	    	else {
	    		q.setMaxResults(count.intValue());
	    	}
    	}
    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();
    	productList.setProducts(products);
    	
    	return productList;

		
		
		
	}
	
	
	/**
	 * This query is used for category listings. All collections are not fully loaded, only the required objects
	 * so the listing page can display everything related to all products
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ProductList getProductsListForLocale(MerchantStore store, Set categoryIds, Language language, Locale locale, int first, int max) {
		

				List<String> regionList = new ArrayList<String>();
				regionList.add(Constants.ALL_REGIONS);
				if(locale!=null) {
					regionList.add(locale.getCountry());
				}
				
				ProductList productList = new ProductList();

		        
				Query countQ = super.getEntityManager().createQuery(
							"select count(p) from Product as p INNER JOIN p.availabilities pa INNER JOIN p.categories categs where p.merchantStore.id=:mId and categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");
							//"select p from Product as p join fetch p.availabilities pa join fetch p.categories categs where categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");
				
				countQ.setParameter("cid", categoryIds);
				countQ.setParameter("lid", regionList);
				countQ.setParameter("dt", new Date());
				countQ.setParameter("mId", store.getId());
				
				//List<Product> ps =  countQ.getResultList();

				Number count = (Number) countQ.getSingleResult ();

				
				productList.setTotalCount(count.intValue());
				
				if(count.intValue()==0)
		        	return productList;
		        
		        

				
				StringBuilder qs = new StringBuilder();
				qs.append("select p from Product as p ");
				qs.append("join fetch p.merchantStore merch ");
				qs.append("join fetch p.availabilities pa ");
				qs.append("left join fetch pa.prices pap ");
				
				qs.append("join fetch p.descriptions pd ");
				qs.append("join fetch p.categories categs ");
			
				
				
				//not necessary
				//qs.append("join fetch pap.descriptions papd ");
				
				
				//images
				qs.append("left join fetch p.images images ");
				//certificate		
				qs.append("left join fetch p.certificates certificates ");
				//proof		
				qs.append("left join fetch p.proofs proofs ");
				//thirdproof		
				qs.append("left join fetch p.thirdproofs thirdproofs ");				
				//selfproof		
				qs.append("left join fetch p.selfProofs selfProofs ");

				//options (do not need attributes for listings)
				//qs.append("left join fetch p.attributes pattr ");
				//qs.append("left join fetch pattr.productOption po ");
				//qs.append("left join fetch po.descriptions pod ");
				//qs.append("left join fetch pattr.productOptionValue pov ");
				//qs.append("left join fetch pov.descriptions povd ");
				
				//other lefts
				qs.append("left join fetch p.manufacturer manuf ");
				qs.append("left join fetch manuf.descriptions manufd ");
				qs.append("left join fetch p.type type ");
				
				
				//qs.append("where pa.region in (:lid) ");
				qs.append("where p.merchantStore.id=mId and categs.id in (:cid) and pa.region in (:lid) ");
				//qs.append("and p.available=true and p.dateAvailable<=:dt and pd.language.id=:lang and manufd.language.id=:lang");
				qs.append("and p.available=true and p.dateAvailable<=:dt and pd.language.id=:lang ");


		    	String hql = qs.toString();
				Query q = super.getEntityManager().createQuery(hql);

		    	q.setParameter("cid", categoryIds);
		    	q.setParameter("lid", regionList);
		    	q.setParameter("dt", new Date());
		    	q.setParameter("lang", language.getId());
		    	q.setParameter("mId", store.getId());
		    	
		    	
		    	q.setFirstResult(first);
		    	if(max>0) {
		    			int maxCount = first + max;

		    			if(maxCount < count.intValue()) {
		    				q.setMaxResults(maxCount);
		    			} else {
		    				q.setMaxResults(count.intValue());
		    			}
		    	}
		    	
		    	List<Product> products =  q.getResultList();
		    	productList.setProducts(products);
		    	
		    	return productList;

		
	}
	
	@Override
	public Product getProductForLocale(long productId, Language language, Locale locale) {


				
				List<String> regionList = new ArrayList<String>();
				regionList.add("*");
				regionList.add(locale.getCountry());
				

				StringBuilder qs = new StringBuilder();
				qs.append("select distinct p from Product as p ");
				qs.append("join fetch p.availabilities pa ");
				qs.append("join fetch p.descriptions pd ");
				qs.append("join fetch p.merchantStore pm ");
				qs.append("left join fetch pa.prices pap ");
				qs.append("left join fetch pap.descriptions papd ");
				
				
				
				
				//images
				qs.append("left join fetch p.images images ");
				//certificate		
				qs.append("left join fetch p.certificates certificates ");
				//proof		
				qs.append("left join fetch p.proofs proofs ");
				//thirdproof		
				qs.append("left join fetch p.thirdproofs thirdproofs ");				
				//selfproof		
				qs.append("left join fetch p.selfProofs selfProofs ");

				//options
				qs.append("left join fetch p.attributes pattr ");
				qs.append("left join fetch pattr.productOption po ");
				qs.append("left join fetch po.descriptions pod ");
				qs.append("left join fetch pattr.productOptionValue pov ");
				qs.append("left join fetch pov.descriptions povd ");
				qs.append("left join fetch p.relationships pr ");
				//other lefts
				qs.append("left join fetch p.manufacturer manuf ");
				qs.append("left join fetch manuf.descriptions manufd ");
				qs.append("left join fetch p.type type ");
				
				
				qs.append("where p.id=:pid and pa.region in (:lid) ");
				qs.append("and pd.language.id=:lang and papd.language.id=:lang ");
				qs.append("and p.available=true and p.dateAvailable<=:dt ");
				//this cannot be done on child elements from left join
				//qs.append("and pod.languageId=:lang and povd.languageId=:lang");

		    	String hql = qs.toString();
				Query q = super.getEntityManager().createQuery(hql);

		    	q.setParameter("pid", productId);
		    	q.setParameter("lid", regionList);
		    	q.setParameter("dt", new Date());
		    	q.setParameter("lang", language.getId());

		    	List<Product> ms = q.getResultList();
				if(ms !=null && ms.size()>0){
					return ms.get(0);
				}else  {
					return null;
				}		
		    	//Product p = (Product)q.getSingleResult();


				//return p;
				
	}

	@Override
	public Product getById(Long productId) {
		
		try {
			


			StringBuilder qs = new StringBuilder();
			qs.append("select distinct p from Product as p ");
			qs.append("join fetch p.availabilities pa ");
			qs.append("join fetch p.merchantStore merch ");
			qs.append("join fetch p.descriptions pd ");
			qs.append("left join fetch p.categories categs ");
			qs.append("left join fetch pa.prices pap ");
			qs.append("left join fetch pap.descriptions papd ");
			qs.append("left join fetch categs.descriptions categsd ");
			
			//images
			qs.append("left join fetch p.images images ");
			//certificate		
			qs.append("left join fetch p.certificates certificates ");
			//proof		
			qs.append("left join fetch p.proofs proofs ");
			//thirdproof		
			qs.append("left join fetch p.thirdproofs thirdproofs ");
			//selfproof		
			qs.append("left join fetch p.selfProofs selfProofs ");

			//options
			
			qs.append("left join fetch p.attributes pattr ");
			qs.append("left join fetch pattr.productOption po ");
			qs.append("left join fetch po.descriptions pod ");
			qs.append("left join fetch pattr.productOptionValue pov ");
			qs.append("left join fetch pov.descriptions povd ");
			qs.append("left join fetch p.relationships pr ");
			//other lefts
			qs.append("left join fetch p.manufacturer manuf ");
			qs.append("left join fetch manuf.descriptions manufd ");
			qs.append("left join fetch p.type type ");
		
			
			qs.append("where p.id=:pid");
	
	
	    	String hql = qs.toString();
			Query q = super.getEntityManager().createQuery(hql);
	
	    	q.setParameter("pid", productId);
	
	
	    	List<Product> ms = q.getResultList();
			if(ms !=null && ms.size()>0){
				return ms.get(0);
			}else  {
				return null;
			}		
	    	//Product p = (Product)q.getSingleResult();
	
	
			//return p;
		
		} catch(javax.persistence.NoResultException ers) {
			return null;
		}
		
	}
	
	@Override
	public Product getByCode(String productCode, Language language) {
		
		try {
			


			StringBuilder qs = new StringBuilder();
			qs.append("select distinct p from Product as p ");
			qs.append("join fetch p.availabilities pa ");
			qs.append("join fetch p.descriptions pd ");
			qs.append("join fetch p.merchantStore pm ");
			qs.append("left join fetch pa.prices pap ");
			qs.append("left join fetch pap.descriptions papd ");
			
			
			
			
			//images
			qs.append("left join fetch p.images images ");
			//certificate		
			qs.append("left join fetch p.certificates certificates ");
			//proof		
			qs.append("left join fetch p.proofs proofs ");
			//thirdproof		
			qs.append("left join fetch p.thirdproofs thirdproofs ");
			//selfproof		
			qs.append("left join fetch p.selfProofs selfProofs ");

			//options
			qs.append("left join fetch p.attributes pattr ");
			qs.append("left join fetch pattr.productOption po ");
			qs.append("left join fetch po.descriptions pod ");
			qs.append("left join fetch pattr.productOptionValue pov ");
			qs.append("left join fetch pov.descriptions povd ");
			qs.append("left join fetch p.relationships pr ");
			//other lefts
			qs.append("left join fetch p.manufacturer manuf ");
			qs.append("left join fetch manuf.descriptions manufd ");
			qs.append("left join fetch p.type type ");
		
			
			qs.append("where p.sku=:code ");
			qs.append("and pd.language.id=:lang and papd.language.id=:lang");
			//this cannot be done on child elements from left join
			//qs.append("and pod.languageId=:lang and povd.languageId=:lang");

	    	String hql = qs.toString();
			Query q = super.getEntityManager().createQuery(hql);

	    	q.setParameter("code", productCode);
	    	q.setParameter("lang", language.getId());

	    	List<Product> ms = q.getResultList();
			if(ms !=null && ms.size()>0){
				return ms.get(0);
			}else  {
				return null;
			}		
	    	//Product p = (Product)q.getSingleResult();


			//return p;
		
		} catch(javax.persistence.NoResultException ers) {
			return null;
		}
		
	}
	
	private String addWhere (StringBuilder strb){
		String tmp = strb.toString().trim();
		if(tmp.length()>0 && tmp.indexOf("where ")!=0) {
			int index = tmp.indexOf("and ");
			if(index == 0) return " where " + tmp.substring(4);
			else return " where " + tmp;
		} else return strb.toString();
	}

	@Override
	public ProductList listPsByCriteria(Language language,ProductCriteria criteria) {
		// TODO Auto-generated method stub
		ProductList productList = new ProductList();
		

        
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(distinct p) from Product as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		StringBuilder countSqlWhere = new StringBuilder();
		
		if(criteria.getStoreId() != -1l){
			countBuilderWhere.append("p.merchantStore.id=:mId");
			countSqlWhere.append("  and PRODUCT.MERCHANT_ID =" +criteria.getStoreId());
		}
		
				
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			countBuilderSelect.append(" INNER JOIN p.categories categs");
			
			countBuilderWhere.append(" and categs.id in (:cid)");
			countSqlWhere.append("  and PRODUCT_CATEGORY.CATEGORY_ID in (" +criteria.getCategoryIds().get(0));
			for(int i=1;i<criteria.getCategoryIds().size();i++){
				countSqlWhere.append(","+criteria.getCategoryIds().get(i));
			}
			countSqlWhere.append(")");
			
		}
		/**
		if(!StringUtils.isBlank(criteria.getRelationship())){
			countBuilderSelect.append(" left join p.relationships relations");
			countBuilderWhere.append("  and relations.code =:rcode");
		}*/
		
		if(criteria.getAvailableP()!=null) {
			if(criteria.getAvailableP().booleanValue()) {
				countBuilderWhere.append(" and p.available=true ");
				countSqlWhere.append("  and PRODUCT.AVAILABLE =true " );
			} else {
				countBuilderWhere.append(" and p.available=false ");
				countSqlWhere.append(" and  PRODUCT.AVAILABLE =false ");
			}
		}
		
		if(!StringUtils.isBlank(criteria.getFindName())){
			countBuilderSelect.append(" INNER JOIN p.descriptions pd");
			countBuilderSelect.append(" INNER JOIN p.merchantStore store");
			countBuilderWhere.append(" and ( pd.language.id=:lang and (lower(pd.name) "
					+ "like:nm or lower(pd.enName) like:enm"
					+ " or p.sku like:sku "
					+ "or p.code like:code or"
					+ " store.storename like:storename))");
			countSqlWhere.append(" and ( NAME like " +"'%"+criteria.getFindName()+"%'" );
			countSqlWhere.append(" or EN_NAME like " +"'%"+criteria.getFindName()+"%'" );
			countSqlWhere.append(" or SKU like " +"'%"+criteria.getFindName()+"%'" );
			countSqlWhere.append(" or CODE like " +"'%"+criteria.getFindName()+"%'" );
			countSqlWhere.append(" or STORE_NAME like " +"'%"+criteria.getFindName()+"%' )" );
			
		}
		
		//审核
		if(!StringUtils.isBlank(criteria.getAudit())){
			countBuilderWhere.append(" and  ").append(criteria.getAudit());
		}
		
		//modify and to where	
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString());
		if(null != countBuilderWhere && countBuilderWhere.length()>0){
			countQ = super.getEntityManager().createQuery(
					countBuilderSelect.toString() + addWhere(countBuilderWhere));
		}
		
		if(criteria.getStoreId() != -1l){
			countQ.setParameter("mId", criteria.getStoreId());
		}
				
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			countQ.setParameter("cid", criteria.getCategoryIds());
		}
		
		if(!StringUtils.isBlank(criteria.getRelationship())){
			countQ.setParameter("rcode", criteria.getRelationship());
		}
		
		
		
		if(!StringUtils.isBlank(criteria.getFindName())){
			countQ.setParameter("lang", language.getId());
			countQ.setParameter("nm", "%"+criteria.getFindName()+"%");
			countQ.setParameter("enm", "%"+criteria.getFindName()+"%");
			countQ.setParameter("sku", "%"+criteria.getFindName()+"%");
			countQ.setParameter("code", "%"+criteria.getFindName()+"%");
			countQ.setParameter("storename", "%"+criteria.getFindName()+"%");
		}
		
		Number count = (Number) countQ.getSingleResult ();

		productList.setTotalCount(count.intValue());
		
		 if(count.intValue()==0){
	        	return productList;
	     }
	     else
	     {
		    StringBuilder qs = new StringBuilder();
			qs.append("Select PRODUCT.PRODUCT_ID as id ,NAME as name,EN_NAME as"
					+ " ename,SKU as sku,CODE as code,PRODUCT.PRODUCT_AUDIT as audit,"
					+ "AVAILABLE as availiable,DATE_AVAILABLE as adate,PRODUCT.DATE_CREATED "
					+ "as cdate,STORE_NAME as sname, "); 
			qs.append(" PRODUCT.QUALITY_SCORE as score,PRODUCT_CATEGORY.CATEGORY_ID as pcid FROM "
					+ " PRODUCT left join PRODUCT_DESCRIPTION on"
					+ " PRODUCT.PRODUCT_ID = PRODUCT_DESCRIPTION.PRODUCT_ID left join "
					+ "MERCHANT_STORE ON  PRODUCT.MERCHANT_ID= MERCHANT_STORE.MERCHANT_ID "
					+ "left join PRODUCT_CATEGORY on PRODUCT.PRODUCT_ID=PRODUCT_CATEGORY.PRODUCT_ID  " );
			if(countSqlWhere !=null && countSqlWhere.length()>0){
				qs.append(addWhere(countSqlWhere));
				qs.append("  ORDER BY PRODUCT.QUALITY_SCORE  DESC");
			}else if(countSqlWhere.length()<=0){
				qs.append("  ORDER BY PRODUCT.QUALITY_SCORE  DESC");
			}
			/**/
			//qs.append("order by QUALITY_SCORE desc");
			
			Query q   =super.getEntityManager().createNativeQuery(qs.toString());

	    	//String hql = "select id,storename,coalesce(CONV(HEX(SUBSTRING(CONVERT(STORE_NAME,'gbk'),1,1)),16,10),SUBSTRING(STORE_NAME,1,1)) from MerchantStore";
		//	Query q = super.getEntityManager().createQuery(hql);
			if(criteria.getMaxCount()>0) {
		    	q.setFirstResult(criteria.getStartIndex());
		    	if(criteria.getMaxCount()<count.intValue()) {
		    		q.setMaxResults(criteria.getMaxCount());
		    	}
		    	else {
		    		q.setMaxResults(count.intValue());
		    	}
	    	}
			
	    	
	    	@SuppressWarnings("unchecked")
			List<Object[]> counts =  q.getResultList();
			productList.setObjs(counts);
			//productList.setTotalCount(totalCount);
		 //replace count
		/**
		 countBuilderSelect.replace(7, 24, "distinct p");
		 
		 Query q = super.getEntityManager().createQuery(
					countBuilderSelect.toString());
		 
		 if(null != countBuilderWhere && countBuilderWhere.length()>0){
				q = super.getEntityManager().createQuery(
						countBuilderSelect.toString() + addWhere(countBuilderWhere));
			}
		 */
		
		
		 /**
		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		
		
		qs.append("join fetch p.descriptions pd ");
		qs.append(" left join fetch p.merchantStore merch ");
		qs.append("left join fetch p.categories categs ");
		qs.append("left join fetch p.relationships relations ");
		
		qs.append(" where pd.language.id=:lang ");
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			//qs.append(" INNER JOIN p.categories categs");
			qs.append(" and categs.id in (:cid) ");
		}
		
		if(!StringUtils.isBlank(criteria.getRelationship())){
			qs.append(" and relations.code =:rcode ");
		}
		
		if(!StringUtils.isBlank(criteria.getFindName())){
			qs.append(" and ( lower(pd.name) like:nm or lower(pd.enName) like:enm or p.sku like:sku or p.code like:code or merch.storename like:storename)");
			
		}

		if(criteria.getAvailable()!=null) {
			if(criteria.getAvailable().booleanValue()) {
				qs.append(" and p.available=true and p.dateAvailable<=:dt ");
			} else {
				qs.append(" and (p.available=false or p.dateAvailable>:dt) ");
			}
		}
		
		if(criteria.getStoreId() != -1l){
			qs.append(" and merch.id=:mId ");
		}
		
		//审核
		if(!StringUtils.isBlank(criteria.getAudit())){
			qs.append(" and ").append(criteria.getAudit());
		}

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	q.setParameter("lang", language.getId());
    	//q.setParameter("mId", store.getId());
    	
    	
		
    	if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
    		q.setParameter("cid", criteria.getCategoryIds());
    	}
    	
		if(criteria.getAvailable()!=null) {
			q.setParameter("dt", new Date());
		}
		    
		if(criteria.getStoreId() != -1l){
			q.setParameter("mId",criteria.getStoreId());
		}
		if(!StringUtils.isBlank(criteria.getRelationship())){
			q.setParameter("rcode",criteria.getRelationship());
		}
		if(!StringUtils.isBlank(criteria.getFindName())){
			q.setParameter("nm", "%"+criteria.getFindName()+"%");
			q.setParameter("enm", "%"+criteria.getFindName()+"%");
			q.setParameter("sku", "%"+criteria.getFindName()+"%");
			q.setParameter("code", "%"+criteria.getFindName()+"%");
			q.setParameter("storename", "%"+criteria.getFindName()+"%");
		}
    	if(criteria.getMaxCount()>0) {
	    	q.setFirstResult(criteria.getStartIndex());
	    	if(criteria.getMaxCount()<count.intValue()) {
	    		q.setMaxResults(criteria.getMaxCount());
	    	}
	    	else {
	    		q.setMaxResults(count.intValue());
	    	}
    	}
    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();
    	productList.setProducts(products);
    	*/
	    }
    	return productList;
	}

	@Override
	public List<Product> getListByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		
		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		
		
		qs.append("join fetch p.descriptions pd ");
		qs.append(" left join fetch p.merchantStore merch ");
		qs.append("left join fetch p.categories categs ");
		
		
		
		if(criteria.getStoreId() != -1l){
			qs.append(" where merch.id=:mId ");
		}

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	
    	
    	
		if(criteria.getStoreId() != -1l){
			q.setParameter("mId",criteria.getStoreId());
		}
		q.setFirstResult(criteria.getStartIndex());
    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();
    	return products;
	}

	@Override
	public List<ProductAvailability> getPrice(ProductCriteria criteria) {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct av from ProductAvailability as av ");
		qs.append("left join fetch av.prices pr ");
		qs.append("left join fetch pr.descriptions prs ");
		
		if(criteria.getProductIds() !=null) qs.append(" where av.product.id in (:pid) ");
		
		String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);
		if(criteria.getProductIds() !=null) q.setParameter("pid",criteria.getProductIds());
		
		q.setFirstResult(criteria.getStartIndex());
		
		@SuppressWarnings("unchecked")
		List<ProductAvailability> counts =  q.getResultList();
		return counts;
	}

	@Override
	public Product getNextCriteria(Language language, ProductCriteria criteria) {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		
		
		qs.append("join fetch p.descriptions pd ");
		qs.append(" left join fetch p.merchantStore merch ");
		qs.append("left join fetch p.categories categs ");
		qs.append("left join fetch p.relationships relations ");
		qs.append("join fetch p.descriptions pd ");
		qs.append("left join fetch p.images images ");
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");
		
		qs.append(" where pd.language.id=:lang ");
		
		if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			//qs.append(" INNER JOIN p.categories categs");
			qs.append(" and categs.id in (:cid) ");
		}
		
		if(!StringUtils.isBlank(criteria.getRelationship())){
			qs.append(" and relations.code =:rcode ");
		}
		
		if(!StringUtils.isBlank(criteria.getFindName())){
			qs.append(" and ( lower(pd.name) like:nm or p.sku like:sku or p.code like:code or merch.storename like:storename)");
			
		}

		if(criteria.getAvailableP()!=null) {
			if(criteria.getAvailableP().booleanValue()) {
				qs.append(" and p.available=true and p.dateAvailable<=:dt ");
			} else {
				qs.append(" and (p.available=false or p.dateAvailable>:dt) ");
			}
		}
		
		if(criteria.getStoreId() != -1l){
			qs.append(" and merch.id=:mId ");
		}
		
		//审核
		if(!StringUtils.isBlank(criteria.getAudit())){
			qs.append(" and ").append(criteria.getAudit());
		}

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	q.setParameter("lang", language.getId());
    	//q.setParameter("mId", store.getId());
    	
    	
		
    	if(!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
    		q.setParameter("cid", criteria.getCategoryIds());
    	}
    	
		if(criteria.getAvailableP()!=null) {
			q.setParameter("dt", new Date());
		}
		    
		if(criteria.getStoreId() != -1l){
			q.setParameter("mId",criteria.getStoreId());
		}
		if(!StringUtils.isBlank(criteria.getRelationship())){
			q.setParameter("rcode",criteria.getRelationship());
		}
		if(!StringUtils.isBlank(criteria.getFindName())){
			q.setParameter("nm", "%"+criteria.getFindName()+"%");
			q.setParameter("sku", "%"+criteria.getFindName()+"%");
			q.setParameter("code", "%"+criteria.getFindName()+"%");
			q.setParameter("storename", "%"+criteria.getFindName()+"%");
		}
		/**
    	if(criteria.getMaxCount()>0) {
	    	q.setFirstResult(criteria.getStartIndex());
	    	if(criteria.getMaxCount()<count.intValue()) {
	    		q.setMaxResults(criteria.getMaxCount());
	    	}
	    	else {
	    		q.setMaxResults(count.intValue());
	    	}
    	}
    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();*/
		 q.setFirstResult(0);
		    q.setMaxResults(1);
		    @SuppressWarnings("unchecked")
		    List<Product> products =  q.getResultList(); 
		    if(products != null && products.size()>0) return products.get(0);
		return null;

	}

	@Override
	public List<Object> getAllProductID() {
		// TODO Auto-generated method stub
				
		
		 StringBuilder qs = new StringBuilder();
			qs.append("Select PRODUCT.PRODUCT_ID as id FROM PRODUCT"); 
			
						Query q   =super.getEntityManager().createNativeQuery(qs.toString());

	    	//String hql = "select id,storename,coalesce(CONV(HEX(SUBSTRING(CONVERT(STORE_NAME,'gbk'),1,1)),16,10),SUBSTRING(STORE_NAME,1,1)) from MerchantStore";
		//	Query q = super.getEntityManager().createQuery(hql);
			    	
	    	@SuppressWarnings("unchecked")
			List<Object> counts =  q.getResultList();
			return counts;
	}

	@Override
	public List<Object> getAllProductIDByStoreID(Long storeId) {

		StringBuilder qs = new StringBuilder();
		qs.append("select p.id from Product as p ");
		qs.append("left join p.merchantStore merch ");
		qs.append("where merch.id=:mid");

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("mid", storeId);
    	
		@SuppressWarnings("unchecked")
		List<Object> counts = q.getResultList();
		return counts;
	}
	
	@Override
	public List<Object[]> getServicePrice(String ids) {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("Select PRODUCT.PRODUCT_ID ,PRODUCT.SPRICE  FROM PRODUCT WHERE PRODUCT_ID in (" + ids + ")"); 
		
					Query q   =super.getEntityManager().createNativeQuery(qs.toString());

    	//String hql = "select id,storename,coalesce(CONV(HEX(SUBSTRING(CONVERT(STORE_NAME,'gbk'),1,1)),16,10),SUBSTRING(STORE_NAME,1,1)) from MerchantStore";
	//	Query q = super.getEntityManager().createQuery(hql);
		    	
    	@SuppressWarnings("unchecked")
		List<Object []> counts =  q.getResultList();
		return counts;
	}
	
	/**
	 * 商品报表
	 * */
	@SuppressWarnings("unchecked")
	public List<Product> queryByProductPoi(String beginTime,String endTime){
		List<Product>  proList =  new ArrayList<Product>();
		try{
			StringBuffer  sql =  new StringBuffer();
			//拼接SQL语句
			if(beginTime==null && endTime==null){
				sql.append("select m.id,m.kefu,m.xiaoshou,p.qualityScore,sum(p.qualityScore),m.storename ,sum(p.id)from Product p inner join p.merchantStore m ");
				sql.append("group by  m.storename,p.qualityScore,p.dateAvailable having p.qualityScore>0");
			}else{
				sql.append("select m.id,p.kefu,p.xiaoshou,p.qualityScore,sum(p.qualityScore),m.storename ,sum(p.id)from Product p inner join p.merchantStore m ");
				sql.append("group by  m.storename,p.qualityScore,p.dateAvailable having p.qualityScore>0");
				sql.append("and ( p.dateAvailable between '?' and '?')");
				
			}
			
			Query query = super.getEntityManager().createQuery(
					sql.toString());
			
			query.setParameter(0, beginTime);
			query.setParameter(1, endTime);
			
			proList=query.getResultList();
			
		}catch(Exception e){
			System.out.println("错误消息:"+e.getMessage());
		}
		
		return proList;
		
	}

	@Override
	public Product getBySeUrlId(MerchantStore store, String seUrl, Locale locale) {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		qs.append("join fetch p.merchantStore pm ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		//certificate		
		qs.append("left join fetch p.certificates certificates ");
		//proof		
		qs.append("left join fetch p.proofs proofs ");
		//thirdproof		
		qs.append("left join fetch p.thirdproofs thirdproofs ");
		//selfproof		
		qs.append("left join fetch p.selfProofs selfProofs ");
		
		//options
		//qs.append("left join fetch p.attributes pattr ");
		//qs.append("left join fetch pattr.productOption po ");
		//qs.append("left join fetch po.descriptions pod ");
		//qs.append("left join fetch pattr.productOptionValue pov ");
		//qs.append("left join fetch pov.descriptions povd ");
		//qs.append("left join fetch p.relationships pr ");
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");		
		
		//qs.append("where pa.region in (:lid) ");
		qs.append("where p.id=:seUrl ");
		qs.append("and p.available=true and p.dateAvailable<=:dt ");
		//qs.append("order by pattr.productOptionSortOrder ");


    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	//q.setParameter("lid", regionList);
    	q.setParameter("dt", new Date());
    	q.setParameter("seUrl", Long.parseLong(seUrl));

    	Product p = null;
    	
    	try {
    		List<Product> ms = q.getResultList();
			if(ms !=null && ms.size()>0){
				return ms.get(0);
			}else  {
				return null;
			}		
    		//p = (Product)q.getSingleResult();
    	} catch(javax.persistence.NoResultException ignore) {

    	}
    			
    			


		return p;
	}
	/**
	 * 根据产品添加时间段查询出产品Id
	 * */
	@SuppressWarnings({ "unchecked", "finally" })
	public List<BigInteger> queryProductById(Date...dateTemp)
	{
		List<BigInteger>  intList =  new ArrayList<BigInteger>();
		try{
			     if(dateTemp.length==1){
			         StringBuffer  sb  =  new StringBuffer();
			    	 sb.append("select PRODUCT_ID from  PRODUCT   where DATE_CREATED=:dt1");
			    	 Query  query = super.getEntityManager().createNativeQuery(sb.toString());
			    	 query.setParameter("dt1",dateTemp[0]); 
			    	 intList.add((BigInteger) query.getSingleResult());
			     }else if(dateTemp.length >1){
		    	     StringBuilder stb = new StringBuilder();
		    	     stb.append("select PRODUCT_ID from  PRODUCT   where DATE_CREATED  between :dt1 and dt2");
			    	 Query  q = super.getEntityManager().createNativeQuery(stb.toString());
			    	 q.setParameter("dt1",dateTemp[0]); 
			    	 q.setParameter("dt2",dateTemp[1]); 
			    	 intList.addAll(q.getResultList());
		    	 }
		   }catch(Exception e){
			   e.printStackTrace();
		   }finally{
			   return intList;
		   }
		
	}
	//品牌
	public Authorization queryByAuthorization(Long productId){
		StringBuffer sb = new StringBuffer();
		sb.append("select a.AUTH_ID ,a.AUTH_TYPE  from AUTHORIZATION a ");
		sb.append(" where a.MERCHANT_ID =(");
		sb.append("select p.MERCHANT_ID from PRODUCT  p");
		sb.append(" where p.PRODUCT_ID=:pid)");
		
		Query q = super.getEntityManager().createNativeQuery(sb.toString());
		
		q.setParameter("pid",productId);
		List<Object[]>  qList = q.getResultList();
		List<Authorization> alist = new ArrayList<Authorization>();
		for(int i =0; i<1;i++){
			Object[] obj = qList.get(i);
			Authorization au = new Authorization();
			au.setId(Long.parseLong(obj[0].toString()));
			au.setAuth_type(Integer.parseInt(obj[1].toString()));
			alist.add(au);
		}
		
		return alist.get(0);
	}
	
		/**
		 * 根据商家获取
		 * @param categoryIds 品牌
		 * @param merchantId 商家
		 */
		public List<Authorization> queryAuthorizationbyMerchantId(Set categoryIds,Long merchantId){
			List<Authorization> alist = new ArrayList<Authorization>();
			try 
	        { 
	            //(3)获取连接实例con,用con创建Statement对象实例 sql_statement 
	             Connection con = Databasetest.getConnection();            
	             Statement sql_statement = (Statement) con.createStatement(); 
	             
	             StringBuffer Ids = new StringBuffer();
	             for(Object id : categoryIds){
	           //(4)执行查询,用ResultSet类的对象,返回查询结果	           
	             String query = "SELECT a.AUTH_ID ,a.AUTH_TYPE from AUTHORIZATION a, MERCHANT_STORE e,AUTH_BRAND d WHERE a.MERCHANT_ID = e.MERCHANT_ID AND d.AUTH_ID = a.AUTH_ID AND d.brand_id=? AND a.MERCHANT_ID = ? "; 
	             PreparedStatement sta = con.prepareStatement(query);
	             sta.setLong(1, (Long)id);
	             sta.setLong(2, merchantId);
	             ResultSet result = sta.executeQuery(); 
	           //对获得的查询结果进行处理,对Result类的对象进行操作 
	            while (result.next()) 
	            {
	            	Authorization au = new Authorization();
	            	au.setId(Long.parseLong(result.getString("AUTH_ID")));
					au.setAuth_type(Integer.parseInt(result.getString("AUTH_TYPE")));
					alist.add(au);
	             } 
	            	 
	             }
	            
	            
	            
	           //关闭连接和声明	            
	            sql_statement.close(); 
	            con.close(); 
	            
	         } catch(java.lang.ClassNotFoundException e) { 
	           //加载JDBC错误,所要用的驱动没有找到	        
	                         System.err.print("ClassNotFoundException"); 
	           //其他错误             

	             System.err.println(e.getMessage()); 
	         } catch (SQLException ex) { 

	            //显示数据库连接错误或查询错误	             
	                  System.err.println("SQLException: " + ex.getMessage()); 
	         } 
			return alist;
			
			
			
			/*StringBuffer sb = new StringBuffer();
			sb.append("select a.AUTH_ID ,a.AUTH_TYPE  from AUTHORIZATION a ");
			sb.append("join AUTH_BRAND ab on(a.AUTH_ID=ab.AUTH_ID)");
			sb.append(" where ab.BRAND_ID in ("+categoryIds+") and a.MERCHANT_ID ="+merchantId);
			
			sb.append("select a.id,a.auth_type from AUTHORIZATION as a");
			sb.append(" join fetch a.manufacturer as m");
			sb.append(" where a.merchantStore.id=:merchantID and m.id in(:categoryIds)");
			System.out.println(sb.toString());
			Query q = super.getEntityManager().createQuery(sb.toString());
			
			q.setParameter("merchantID", merchantId);
			q.setParameter("categoryIds",categoryIds);
			List<Object[]>  qList = q.getResultList();
			List<Authorization> alist = new ArrayList<Authorization>();
			for(int i =0; i<qList.size();i++){
				Object[] obj = qList.get(i);
				Authorization au = new Authorization();
				au.setId(Long.parseLong(obj[0].toString()));
				au.setAuth_type(Integer.parseInt(obj[1].toString()));
				alist.add(au);
			}
			
			return alist;*/
		}
}


	
	