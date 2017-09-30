package com.salesmanager.core.business.catalog.product.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.attribute.AttributeCriteria;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.constants.Constants;

@Repository("productDao")
public class ProductDaoImpl extends SalesManagerEntityDaoImpl<Long, Product> implements ProductDao {
	private static final int P_TYPE_STRING = 0;
	private static final int P_TYPE_INT = 1;
	private static final int P_TYPE_LONG = 2;
	private static final int P_TYPE_DATE = 3;
	
	@Autowired
	DataSource dataSource;
	
	class NativeSqlParam {
		public NativeSqlParam(int pType, Object pValue) {
			paramType = pType;
			paramValue = pValue;
		}
		int paramType;
		Object paramValue;
	}
	
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
	
	@Override
	public ProductList listDiamondProductByStore(MerchantStore store, Language language, ProductCriteria criteria) {

		ProductList productList = new ProductList();

        
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(distinct p) from Product as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		countBuilderWhere.append(" where p.merchantStore.id=:mId and p.productIsDiamond=1");
		
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
		
		if (criteria.getDiamond() != null){
			countBuilderWhere.append(" and p.productIsDiamond=:productIsDiamond");
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
		
		if (criteria.getDiamond() != null){
			countQ.setParameter("productIsDiamond", criteria.getDiamond().booleanValue());
		}
		
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
	public ProductList listPsByCriteria_jdbc(Language language, ProductCriteria criteria) {
		// TODO Auto-generated method stub
		ProductList productList = new ProductList();

		boolean useMerchantStore = false;
		boolean useCategory = false;
		boolean useProductDescription = false;

		StringBuilder sbSql = new StringBuilder("select distinct p.PRODUCT_ID ");
//		StringBuilder sbCountSql = new StringBuilder("select count(distinct p.PRODUCT_ID) ");
		StringBuilder sbFromClause = new StringBuilder("from PRODUCT_SEARCH_INFO p ");
		StringBuilder sbWhereClause = new StringBuilder("where 1=1 ");
		List<NativeSqlParam> params = new ArrayList<NativeSqlParam>();

		// 如果指定了商家ID
		if (criteria.getStoreId() != -1l) {
//			if (!useMerchantStore) {
//				sbFromClause.append("inner join MERCHANT_STORE ms on p.MERCHANT_ID=ms.MERCHANT_ID ");
//				useMerchantStore = true;
//			}
			sbWhereClause.append("and p.MERCHANT_ID=? ");
			params.add(new NativeSqlParam(P_TYPE_LONG, criteria.getStoreId()));
		}
		// 如果指定了category
		if (!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
//			if (!useCategory) {
//				sbFromClause.append(
//						"left join PRODUCT_CATEGORY pcat on p.PRODUCT_ID=pcat.PRODUCT_ID left join CATEGORY cat on cat.CATEGORY_ID=pcat.CATEGORY_ID ");
//				useCategory = true;
//			}
			sbWhereClause.append("and p.CATEGORY_ID in (");
			boolean isFirst = true;
			List<Long> cIds = criteria.getCategoryIds();
			for (Long cId : cIds) {
				if (isFirst) {
					isFirst = false;
				} else {
					sbWhereClause.append(',');
				}
				sbWhereClause.append('?');
				params.add(new NativeSqlParam(P_TYPE_LONG, cId));
			}
			sbWhereClause.append(") ");
		}
		// 如果指定了钻石产品
		if (criteria.getDiamond() != null) {
			sbWhereClause.append("and p.PRODUCT_DIAMOND=? ");
			params.add(new NativeSqlParam(P_TYPE_INT, criteria.getDiamond().booleanValue() ? 1 : 0));
		}
		// 如果指定了是否上架
		if (criteria.getAvailableP() != null) {
			sbWhereClause.append("and p.AVAILABLE=? ");
			params.add(new NativeSqlParam(P_TYPE_INT, criteria.getAvailableP().booleanValue() ? 1 : 0));
		}
		// 如果指定了搜索关键字
		if (!StringUtils.isBlank(criteria.getFindName())) {
//			if (!useMerchantStore) {
//				sbFromClause.append("left join MERCHANT_STORE ms on p.MERCHANT_ID=ms.MERCHANT_ID ");
//				useMerchantStore = true;
//			}
//			if (!useProductDescription) {
//				sbFromClause.append("left join PRODUCT_DESCRIPTION pdsc on pdsc.PRODUCT_ID=p.PRODUCT_ID ");
//				useProductDescription = true;
//			}
//			sbWhereClause.append("and pdsc.LANGUAGE_ID=? and (");
//			params.add(new NativeSqlParam(P_TYPE_INT, language.getId()));
//			String searchKey = '%' + criteria.getFindName() + '%';
//			// 描述
//			sbWhereClause.append("pdsc.NAME like ? ");
//			params.add(new NativeSqlParam(P_TYPE_STRING, searchKey));
//			sbWhereClause.append("or pdsc.EN_NAME like ? ");
//			params.add(new NativeSqlParam(P_TYPE_STRING, searchKey));
//			// SKU/CODE/CAS
//			sbWhereClause.append("or p.SKU like ? ");
//			params.add(new NativeSqlParam(P_TYPE_STRING, searchKey));
//			sbWhereClause.append("or p.CODE like ? ");
//			params.add(new NativeSqlParam(P_TYPE_STRING, searchKey));
//			sbWhereClause.append("or p.CAS like ? ");
//			params.add(new NativeSqlParam(P_TYPE_STRING, searchKey));
//			// STORE name
//			sbWhereClause.append("or ms.STORE_NAME like ? ");
//			params.add(new NativeSqlParam(P_TYPE_STRING, searchKey));
//			//
//			sbWhereClause.append(") ");
			sbWhereClause.append("and p.SEARCH_CONTENT like ? ");
			params.add(new NativeSqlParam(P_TYPE_STRING, '%' + criteria.getFindName() + '%'));
		}
		// 审核
		if (!StringUtils.isBlank(criteria.getAudit())) {
			sbWhereClause.append("and p.PRODUCT_AUDIT in (?,?)");
			params.add(new NativeSqlParam(P_TYPE_INT, com.salesmanager.core.constants.Constants.AUDIT_PRE));
			params.add(new NativeSqlParam(P_TYPE_INT, com.salesmanager.core.constants.Constants.AUDIT_AGAIN));
		}
		// prepare FROM and WHERE
		sbFromClause.append(sbWhereClause.toString()).append("order by p.QUALITY_SCORE DESC, p.PRODUCT_ID DESC ");
		sbSql.append(sbFromClause.toString());
//		sbCountSql.append(sbFromClause.toString());
//		if (criteria.getMaxCount() > 0) {
//			sbSql.append("limit ?,? ");
//		}
		List<Long> pids = new ArrayList<Long>();
		int totalCount = 0;
		try {
			PreparedStatement st = null;
			ResultSet rs = null;
			
			Connection conn = dataSource.getConnection();
			String sql = sbSql.toString();
			//String countSql = sbCountSql.toString();
			
			// 先取所有的计数
//			PreparedStatement st = createPagingStatement(criteria, params, countSql, conn, true);
//			ResultSet rs = st.executeQuery();
//			rs.next();
//			totalCount = rs.getInt(1);
//			rs.close();
//			st.close();
//			if (criteria.getMaxCount() > 0){
//				if (criteria.getStartIndex() >= totalCount){
//					int lastPageLeft = totalCount % criteria.getMaxCount();
//					if (lastPageLeft == 0){
//						criteria.setStartIndex(Math.max(0, totalCount-criteria.getMaxCount()));
//					}else{
//						criteria.setStartIndex(Math.max(0, totalCount-lastPageLeft));
//					}
//				}
//			}
			
			st = createPagingStatement(criteria, params, sql, conn, false);
			
			rs = st.executeQuery();
			if (rs.last()){
				totalCount = rs.getRow();
			}
			if (criteria.getMaxCount() > 0){
				if (criteria.getStartIndex() >= totalCount){
					int lastPageLeft = totalCount % criteria.getMaxCount();
					if (lastPageLeft == 0){
						criteria.setStartIndex(Math.max(0, totalCount-criteria.getMaxCount()));
					}else{
						criteria.setStartIndex(Math.max(0, totalCount-lastPageLeft));
					}
				}
			}
			rs.absolute(criteria.getStartIndex());
			int leftCnt = criteria.getMaxCount();
			// 拿到所有的ProductID
			while(rs.next() && leftCnt-- > 0){
				pids.add(rs.getLong(1));
			}
			rs.close();
			st.close();
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		productList.setTotalCount(totalCount);
		if (pids == null || pids.isEmpty()){
			return productList;
		}
		StringBuilder qs = new StringBuilder();
		qs.append("Select PRODUCT.PRODUCT_ID as id ,NAME as name,EN_NAME as"
				+ " ename,SKU as sku,CODE as code,PRODUCT.PRODUCT_AUDIT as audit,"
				+ "AVAILABLE as availiable,DATE_AVAILABLE as adate,PRODUCT.DATE_CREATED "
				+ "as cdate,STORE_NAME as sname, ");
		qs.append(
				" PRODUCT.QUALITY_SCORE as score,PRODUCT_CATEGORY.CATEGORY_ID as pcid, PRODUCT.PRODUCT_DIAMOND as productIsDiamond FROM "
						+ " PRODUCT left join PRODUCT_DESCRIPTION on"
						+ " PRODUCT.PRODUCT_ID = PRODUCT_DESCRIPTION.PRODUCT_ID left join "
						+ "MERCHANT_STORE ON  PRODUCT.MERCHANT_ID= MERCHANT_STORE.MERCHANT_ID "
						+ "left join PRODUCT_CATEGORY on PRODUCT.PRODUCT_ID=PRODUCT_CATEGORY.PRODUCT_ID  ");
		qs.append(" where PRODUCT.PRODUCT_ID in (:pids) ORDER BY PRODUCT.QUALITY_SCORE  DESC");
		
		/**/
		// qs.append("order by QUALITY_SCORE desc");

		Query q = super.getEntityManager().createNativeQuery(qs.toString());
		q.setParameter("pids", pids);
		List<Object[]> counts = q.getResultList();
		productList.setObjs(counts);
		return productList;
	}

	protected PreparedStatement createPagingStatement(ProductCriteria criteria, List<NativeSqlParam> params, String sql,
			Connection conn, boolean isCount) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		for (int i = 0; i < params.size(); i++) {
			NativeSqlParam param = params.get(i);
			switch (param.paramType) {
			case P_TYPE_INT:
				st.setInt(i + 1, (int) param.paramValue);
				break;
			case P_TYPE_LONG:
				st.setLong(i + 1, (long) param.paramValue);
				break;
			case P_TYPE_DATE:
				st.setDate(i + 1, (java.sql.Date) param.paramValue);
				break;
			default:
				st.setString(i + 1, (String) param.paramValue);
			}
		}
//		if (!isCount && criteria.getMaxCount() > 0) {
//			st.setInt(params.size() + 1, criteria.getStartIndex());
//			st.setInt(params.size() + 2, criteria.getMaxCount());
//		}
		return st;
	}

	
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
		
		// add by clariones, for search with productIsDiamond
		if (criteria.getDiamond() != null){
			countBuilderWhere.append(" and p.productIsDiamond=:productIsDiamond");
			countSqlWhere.append("  and PRODUCT.PRODUCT_DIAMOND ="+  String.valueOf(criteria.getDiamond())+" ");
		}
		
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
		
		// add by clarions. for search by diamond flag
		if (criteria.getDiamond() != null){
			countQ.setParameter("productIsDiamond", criteria.getDiamond().booleanValue());
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
			qs.append(" PRODUCT.QUALITY_SCORE as score,PRODUCT_CATEGORY.CATEGORY_ID as pcid, PRODUCT.PRODUCT_DIAMOND as productIsDiamond FROM "
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

	@Override
	public List<Long> getAllByManufactureIds(Set<Manufacturer> manufacturers) {
		StringBuilder qs = new StringBuilder();
		qs.append("Select PRODUCT.PRODUCT_ID as id FROM PRODUCT where PRODUCT.MANUFACTURER_ID in (:manufacturers)"); 
		Query q   =super.getEntityManager().createNativeQuery(qs.toString());
		List<Long> parameter = new ArrayList<Long>();
		for(Manufacturer manf: manufacturers){
			parameter.add(manf.getId());
		}
		q.setParameter("manufacturers", parameter);
    	@SuppressWarnings("unchecked")
    	List<Number> counts =  q.getResultList();
    	if (counts == null){
    		return null;
    	}
    	List<Long> result = new ArrayList<Long>(counts.size());
    	for(int i=0;i<counts.size();i++){
    		result.add(counts.get(i).longValue());
    	}
		return result;
	}

	@Override
	public List<Long> getAllByManufactureAndMerchant(MerchantStore mcht, Set<Manufacturer> menufactures) {
		StringBuilder qs = new StringBuilder();
		qs.append("Select PRODUCT.PRODUCT_ID as id FROM PRODUCT where PRODUCT.MERCHANT_ID=(:mchtId) and PRODUCT.MANUFACTURER_ID in (:manufacturers)"); 
		Query q   =super.getEntityManager().createNativeQuery(qs.toString());
		List<Long> parameter = new ArrayList<Long>();
		for(Manufacturer manf: menufactures){
			parameter.add(manf.getId());
		}
		q.setParameter("mchtId", mcht.getId());
		q.setParameter("manufacturers", parameter);
    	@SuppressWarnings("unchecked")
		List<Number> counts =  q.getResultList();
    	if (counts == null){
    		return null;
    	}
    	List<Long> result = new ArrayList<Long>(counts.size());
    	for(int i=0;i<counts.size();i++){
    		result.add(counts.get(i).longValue());
    	}
		return result;
	}

	@Override
	public List<Long> getAllByNotManufactureNorMerchant(Set<MerchantStore> merchants,
			Set<Manufacturer> menufactures) {
		StringBuilder qs = new StringBuilder();
		qs.append("Select PRODUCT.PRODUCT_ID as id FROM PRODUCT where PRODUCT.MERCHANT_ID not in (:mchtIds) and PRODUCT.MANUFACTURER_ID not in (:manufacturers)"); 
		Query q   =super.getEntityManager().createNativeQuery(qs.toString());
		List<Long> parameter = new ArrayList<Long>();
		for(Manufacturer manf: menufactures){
			parameter.add(manf.getId());
		}
		List<Long> mchtIds = new ArrayList<Long>();
		for(MerchantStore store: merchants){
			mchtIds.add(store.getId());
		}
		q.setParameter("mchtIds", mchtIds);
		q.setParameter("manufacturers", parameter);
    	@SuppressWarnings("unchecked")
		List<Number> counts =  q.getResultList();
    	if (counts == null){
    		return null;
    	}
    	List<Long> result = new ArrayList<Long>(counts.size());
    	for(int i=0;i<counts.size();i++){
    		result.add(counts.get(i).longValue());
    	}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Long> getIdsByNativeSql(String sqlStr) {
		Query q =super.getEntityManager().createNativeQuery(sqlStr);
		List<Number> counts =  q.getResultList();
    	if (counts == null){
    		return null;
    	}
    	List<Long> result = new ArrayList<Long>(counts.size());
    	for(int i=0;i<counts.size();i++){
    		result.add(counts.get(i).longValue());
    	}
		return result;
	}

	private static final String sqlAllInvalidProductIdsByMerchantAndManufacturer = "select p.PRODUCT_ID from PRODUCT p left join MERCHANT_STORE s on p.MERCHANT_ID = s.MERCHANT_ID where p.AVAILABLE=0 and s.STORE_NAME=(:storeName) and p.MANUFACTURER_ID in (:brandIds)";
	@Override
	public List<Long> getAllInvalidProductIdsByMerchantAndManufacturer(String storeName, List<Long> brandIds) {
		Query q =super.getEntityManager().createNativeQuery(sqlAllInvalidProductIdsByMerchantAndManufacturer);
		q.setParameter("storeName", storeName);
		q.setParameter("brandIds", brandIds);
		List<Number> counts =  q.getResultList();
    	if (counts == null){
    		return new ArrayList<Long>();
    	}
    	List<Long> result = new ArrayList<Long>(counts.size());
    	for(int i=0;i<counts.size();i++){
    		result.add(counts.get(i).longValue());
    	}
		return result;
	}
	
	private static final String sqlAllInvalidProductIdsByMerchantName = "select p.PRODUCT_ID from PRODUCT p left join MERCHANT_STORE s on p.MERCHANT_ID = s.MERCHANT_ID where p.AVAILABLE=0 and s.STORE_NAME=(:storeName)";
	@Override
	public List<Long> getAllInvalidProductIdsByMerchantName(String storeName) {
		Query q =super.getEntityManager().createNativeQuery(sqlAllInvalidProductIdsByMerchantName);
		q.setParameter("storeName", storeName);
		List<Number> counts =  q.getResultList();
    	if (counts == null){
    		return new ArrayList<Long>();
    	}
    	List<Long> result = new ArrayList<Long>(counts.size());
    	for(int i=0;i<counts.size();i++){
    		result.add(counts.get(i).longValue());
    	}
		return result;
	}

	private static final String sqlAllInvalidProductIdsByManufacturers = "select p.PRODUCT_ID from PRODUCT p left join MERCHANT_STORE s on p.MERCHANT_ID = s.MERCHANT_ID where p.AVAILABLE=0 and p.MANUFACTURER_ID in (:brandIds)";
	
	@Override
	public List<Long> getAllInvalidProductIdsByManufacturers(List<Long> brandIds) {
		Query q =super.getEntityManager().createNativeQuery(sqlAllInvalidProductIdsByManufacturers);
		q.setParameter("brandIds", brandIds);
		List<Number> counts =  q.getResultList();
    	if (counts == null){
    		return new ArrayList<Long>();
    	}
    	List<Long> result = new ArrayList<Long>(counts.size());
    	for(int i=0;i<counts.size();i++){
    		result.add(counts.get(i).longValue());
    	}
		return result;
	}

	private static  String sqlQueryProductPricingInfo = "select p.PRODUCT_ID as pId,p.SPRICE as sPrice, "
			+ "p.QUALITY_SCORE as pScore,pp.PRODUCT_PRICE_ID as priceId,ppd.NAME as spec, "
	+"pp.PRODUCT_PRICE_AMOUNT as price, pp.PRODUCT_PRICE_SPECIAL_AMOUNT as promoPrice, "
	+"pp.PRODUCT_PRICE_SPECIAL_ST_DATE as sDate, pp.PRODUCT_PRICE_SPECIAL_END_DATE as eDate, pp.DEFAULT_PRICE as isDef "
    +"from PRODUCT p left join PRODUCT_AVAILABILITY pa on p.PRODUCT_ID=pa.PRODUCT_ID "
	+"left join PRODUCT_PRICE pp on pa.PRODUCT_AVAIL_ID=pp.PRODUCT_AVAIL_ID "
    +"left join PRODUCT_PRICE_DESCRIPTION ppd on pp.PRODUCT_PRICE_ID=ppd.PRODUCT_PRICE_ID "
    + "where p.PRODUCT_ID in (:pid)";
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProductListByIdsForPricing(List<Long> prodIds) {
		if (prodIds == null || prodIds.isEmpty()){
			return new ArrayList<Product>();
		}
		List<Product> result = new ArrayList<Product>();
		Query q = super.getEntityManager().createNativeQuery(sqlQueryProductPricingInfo);
		q.setParameter("pid", prodIds);
		List<Object[]> queryResult = q.getResultList();
		for(Object[] qData : queryResult){
			Long productId = ((Number)qData[0]).longValue();	//
			Product  product = findInList4Pricing(result, productId);
			if (product == null){
				product = new Product();
				product.setId(productId);
				product.setServerPrice((String) qData[1]);	//
				product.setQualityScore(((Number)qData[2]).intValue());	//
				result.add(product);
			}
			if (qData[3] == null){
				continue;
			}
			Long priceId = ((Number)qData[3]).longValue();	//
			ProductAvailability pavb = findProductAvaInProduct(product, priceId);
			if (pavb == null){
				pavb = new ProductAvailability();
				pavb.setId(priceId);
				pavb.setPrices(new HashSet<ProductPrice>());
				Set<ProductAvailability> pavs = product.getAvailabilities();
				if (pavs == null){
					pavs = new HashSet<ProductAvailability>();
					product.setAvailabilities(pavs);
				}
				pavs.add(pavb);
			}
			ProductPrice pPrice = new ProductPrice();
			pPrice.setId(priceId);
			Set<ProductPriceDescription> ppds = new HashSet();
			ProductPriceDescription ppd = new ProductPriceDescription();
			ppd.setName((String) qData[4]);	//
			ppds.add(ppd);
			pPrice.setDescriptions(ppds);
			pPrice.setProductPriceAmount(getBigDecimal(qData, 5)); //
			pPrice.setProductPriceSpecialAmount(getBigDecimal(qData, 6)); //
			pPrice.setProductPriceSpecialStartDate((Date) qData[7]);	//
			pPrice.setProductPriceSpecialEndDate((Date) qData[8]); //
			pPrice.setDefaultPrice(getBoolean(qData, 9)); //
			Set<ProductPrice> prices = pavb.getPrices();
			if (prices == null){
				prices = new HashSet<ProductPrice>();
				pavb.setPrices(prices);
			}
			prices.add(pPrice);
		}
		
		
		return result;
// after testing, below not work. They will not combin collections members into correct product
//		StringBuilder qs = new StringBuilder();
//		qs.append("select distinct p from Product as p ");
//		qs.append("join fetch p.merchantStore merch ");
//		qs.append("join fetch p.manufacturer manuf ");
//		qs.append("join fetch p.availabilities pa ");
//		qs.append("join fetch pa.prices pap ");
//		qs.append("join fetch pap.descriptions prs ");
//		qs.append("where p.id in (:pid)");
//		String hql = qs.toString();
//		Query q = super.getEntityManager().createQuery(hql);
//		q.setParameter("pid", prodIds);
//		return q.getResultList();
		
// after testing, below not work. Same issue as above
//		QProduct qProduct = QProduct.product;
//		QProductAvailability qAva = QProductAvailability.productAvailability;
//		QProductPrice qPricDscp = QProductPrice.productPrice;
//		JPQLQuery query = new JPAQuery (getEntityManager());
//		query.from(qProduct)
//			.join(qProduct.merchantStore).fetch()
//			.leftJoin(qProduct.manufacturer).fetch()
//			.leftJoin(qProduct.availabilities, qAva).fetch()
//			.leftJoin(qAva.prices, qPricDscp).fetch()
//			.leftJoin(qPricDscp.descriptions).fetch()
//			.where(qProduct.id.in(prodIds));
//		List<Product> products = query.list(qProduct);
//		
//		return products;
		
		
	}

	private boolean getBoolean(Object[] qData, int i) {
		if (qData[i] == null){
			return false;
		}
		Object data = qData[i];
		if (data instanceof Boolean){
			return ((Boolean) data).booleanValue();
		}
		if (data instanceof Number){
			return ((Number) data).intValue() != 0;
		}
		if (data instanceof String){
			String str = String.valueOf(data).toLowerCase();
			return str.equals("1") || str.equals("true") || str.equals("yes");
		}
		return false;
	}

	private BigDecimal getBigDecimal(Object[] qData, int i) {
		if (qData[i] == null){
			return null;
		}
		return new BigDecimal(((Number) qData[i]).doubleValue());
	}

	private ProductAvailability findProductAvaInProduct(Product product, Long priceId) {
		if (product == null){
			throw new RuntimeException("Code issue. Please check the useage of findProductAvaInProduct()");
		}
		if (product.getAvailabilities() == null){
			return null;
		}
		for(ProductAvailability pva : product.getAvailabilities()){
			if (pva.getId().equals(priceId)){
				return pva;
			}
		}
		return null;
	}

	private Product findInList4Pricing(List<Product> list, Long productId) {
		if (list == null){
			return null;
		}
		for(Product p : list){
			if (p.getId().equals(productId)){
				return p;
			}
		}
		return null;
	}
		
}


	
	