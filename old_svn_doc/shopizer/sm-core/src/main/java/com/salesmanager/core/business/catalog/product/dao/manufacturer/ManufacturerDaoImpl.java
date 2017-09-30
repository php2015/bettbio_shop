package com.salesmanager.core.business.catalog.product.dao.manufacturer;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureCriteria;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureList;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.QManufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.QManufacturerDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("manufacturerDao")
public class ManufacturerDaoImpl extends SalesManagerEntityDaoImpl<Long, Manufacturer>
		implements ManufacturerDao {
	
	@Override
	public int getCountManufAttachedProducts(  Manufacturer manufacturer  ){
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(distinct p) from Product as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		countBuilderWhere.append(" where p.manufacturer.id=:mId");
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		countQ.setParameter("mId", manufacturer.getId() );
		
		Number count = (Number) countQ.getSingleResult ();

		return count.intValue();
	}
	
	@Override
	public List<Manufacturer> listByStore(MerchantStore store, Language language) {
		QManufacturer qManufacturer = QManufacturer.manufacturer;
		QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qManufacturer)
			.leftJoin(qManufacturer.descriptions, qManufacturerDescription).fetch()
			.leftJoin(qManufacturer.merchantStore).fetch()
			.where(qManufacturerDescription.language.id.eq(language.getId())
			.and(qManufacturer.merchantStore.id.eq(store.getId())));
		

		
		List<Manufacturer> manufacturers = query.list(qManufacturer);
		return manufacturers;
	}

	public Manufacturer getById(Long id) {
		QManufacturer qManufacturer = QManufacturer.manufacturer;
		QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qManufacturer)
			.leftJoin(qManufacturer.descriptions, qManufacturerDescription).fetch()
			.leftJoin(qManufacturer.merchantStore).fetch()
			.where(qManufacturer.id.eq(id));
		
		List<Manufacturer> ms = query.list(qManufacturer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	
	@Override
	public List<Manufacturer> listByStore(MerchantStore store) {
		QManufacturer qManufacturer = QManufacturer.manufacturer;
		QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qManufacturer)
			.leftJoin(qManufacturer.descriptions, qManufacturerDescription).fetch()
			.leftJoin(qManufacturer.merchantStore).fetch()
			.where(qManufacturer.merchantStore.id.eq(store.getId()));
		

		
		List<Manufacturer> manufacturers = query.list(qManufacturer);
		return manufacturers;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Manufacturer> listByProductsByCategoriesId(List<Long> ids, Language language) {
		StringBuilder builderSelect = new StringBuilder();
		builderSelect.append("select distinct manufacturer from Product as p ");
		builderSelect.append("join p.manufacturer manufacturer ");
		builderSelect.append("join manufacturer.descriptions md ");
		builderSelect.append("join p.categories categs ");
		builderSelect.append("where categs.id in (:cid) ");
		builderSelect.append("and md.language.id=:lang");

		Query query = super.getEntityManager().createQuery(
				builderSelect.toString());

		query.setParameter("cid", ids);
		query.setParameter("lang", language.getId());
		
		return query.getResultList();
		
	}

	@Override
	public ManufactureList getByCriteria(Criteria criteria, Language language) {
		// TODO Auto-generated method stub
		ManufactureList manus = new ManufactureList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(m) from Manufacturer as m ");
		countBuilderSelect.append("INNER JOIN m.descriptions de");
		StringBuilder countBuilderWhere = new StringBuilder();
		
		countBuilderWhere.append(" where de.language.id = " + language.getId());
				
		if(!StringUtils.isEmpty(criteria.getFindName())) {			
			countBuilderWhere.append(" and (de.name like:den or de.url like:url )");
		}
		
		
		if(criteria.getStoreId() != -1l){
			countBuilderWhere.append(" and m.merchantStore.id =" +criteria.getStoreId());
		}
		if (criteria instanceof ManufactureCriteria) {
			ManufactureCriteria tmp = (ManufactureCriteria) criteria;
			if (tmp.getSrcStoreId()!=null&&tmp.getSrcStoreId()>0) {
				countBuilderWhere.append(" and m.srcMerchantStoreId =" +criteria.getStoreId());
			}
		}
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter("den",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
			countQ.setParameter("url",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());			
		}
		
		Number count = (Number) countQ.getSingleResult ();

		manus.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return manus;
        
		QManufacturer qManufacturer = QManufacturer.manufacturer;
		QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qManufacturer)
		.join(qManufacturer.descriptions,qManufacturerDescription).fetch()
		.join(qManufacturer.merchantStore,qMerchantStore).fetch()
		.orderBy(qManufacturer.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qManufacturerDescription.language.id.eq(language.getId()));
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			pBuilder.andAnyOf(qManufacturerDescription.url.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qManufacturerDescription.name.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
		}
		
		if(criteria.getStoreId() != -1l){
			pBuilder.and(QMerchantStore.merchantStore.id.eq(criteria.getStoreId()));
		}
		/**
		if (criteria instanceof ManufactureCriteria) {
			ManufactureCriteria tmp = (ManufactureCriteria) criteria;
			if (tmp.getSrcStoreId()!=null&&tmp.getSrcStoreId()>0) {
				pBuilder.and(qManufacturer.srcMerchantStoreId.eq(tmp.getSrcStoreId()));
			}
		}*/
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		manus.setManus(query.list(qManufacturer));
		return manus;
	}

	@Override
	public List<Manufacturer> getListByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		        
		QManufacturer qManufacturer = QManufacturer.manufacturer;
		QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qManufacturer)
		.join(qManufacturer.descriptions,qManufacturerDescription).fetch()
		.join(qManufacturer.merchantStore,qMerchantStore).fetch()
		.orderBy(qManufacturer.id.asc());
		
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
		
		return query.list(qManufacturer);
	}

	/**
	 * 根据品牌名称获取品牌信息
	 */
	@Override
	public List<Manufacturer> getListByName(String name) {
		QManufacturer qManufacturer = QManufacturer.manufacturer;
		QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qManufacturer)
		.join(qManufacturer.descriptions,qManufacturerDescription).fetch()
		.orderBy(qManufacturer.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qManufacturerDescription.name.equalsIgnoreCase(name));
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		return query.list(qManufacturer);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Manufacturer> listByProductsByCriteria(
			ProductCriteria criteria, Language language) {
		// TODO Auto-generated method stub
		StringBuilder builderSelect = new StringBuilder();
		StringBuilder countBuilderWhere = new StringBuilder();
		builderSelect.append("select distinct manufacturer from Product as p ");
		builderSelect.append("join p.manufacturer manufacturer ");
		builderSelect.append("join manufacturer.descriptions md ");
		
		countBuilderWhere.append("where md.language.id=:lang ");
		
		if(criteria.getCategoryIds()!=null && criteria.getCategoryIds().size()>0){
			builderSelect.append("join p.categories categs ");
			countBuilderWhere.append("and categs.id in (:cid) ");
		}		
		
		if(criteria.isCerticate() == true){
			builderSelect.append("join p.certificates certificates ");
			countBuilderWhere.append("and certificates.id is not null ");
		}
		
		if(criteria.isProof() == true){
			builderSelect.append("join p.proofs proofs  ");
			countBuilderWhere.append("and proofs.id is not null  ");
		}
		
		if(criteria.isThird() == true){
			builderSelect.append("join p.thirdproofs thirdproofs  ");
			countBuilderWhere.append("and thirdproofs.id is not null  ");
		}

		Query query = super.getEntityManager().createQuery(
				builderSelect.toString()+ countBuilderWhere.toString());
		if(criteria.getCategoryIds()!=null && criteria.getCategoryIds().size()>0){
			query.setParameter("cid", criteria.getCategoryIds());
		}
		
		query.setParameter("lang", language.getId());
		
		return query.getResultList();
	}

	@Override
	public List<Object[]> getStoreName() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				StringBuilder qs = new StringBuilder();
				qs.append("select MANUFACTURER_ID as id,NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(NAME USING gbk),1)),16,10), ");
				qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
				qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
				qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(NAME,1)) as pinyin  from MANUFACTURER_DESCRIPTION order by pinyin,name");
				
				Query q  =super.getEntityManager().createNativeQuery(qs.toString());

		    	 //String hql = "select id,storename,coalesce(CONV(HEX(SUBSTRING(CONVERT(STORE_NAME,'gbk'),1,1)),16,10),SUBSTRING(STORE_NAME,1,1)) from MerchantStore";
			     //	Query q = super.getEntityManager().createQuery(hql);
		    	
		    	@SuppressWarnings("unchecked")
				List<Object[]> counts =  q.getResultList();

		    	
		    	return counts;
	}
	
	//查询全部的品牌
	@Override
	public ManufactureList queryByManufacturer(){
		try{
			ManufactureList manus = new ManufactureList();
			StringBuilder countBuilderSelect = new StringBuilder();
			countBuilderSelect.append("select count(m) from Manufacturer as m ");
			countBuilderSelect.append("INNER JOIN m.descriptions de");
			Query countQ = super.getEntityManager().createQuery(
					countBuilderSelect.toString());
			Number count = (Number) countQ.getSingleResult ();

			manus.setTotalCount(count.intValue());
			
	        if(count.intValue()==0){
	        	return manus;
	        }
	        
			QManufacturer qManufacturer = QManufacturer.manufacturer;
			QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
			JPQLQuery query = new JPAQuery (getEntityManager());
			query.from(qManufacturer)
			.join(qManufacturer.descriptions,qManufacturerDescription).fetch()
			.orderBy(qManufacturer.id.asc());
			
			manus.setManus(query.list(qManufacturer));
			
			return manus;
			
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return null;
	}
	
	//获取产品的品牌mid
	public List<ManufacturerDescription> queryByManufacturerId(Long id){
		List<ManufacturerDescription> manList = null;
		try{
			QManufacturer qManufacturer = QManufacturer.manufacturer;
			QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
			JPQLQuery query = new JPAQuery (getEntityManager());
			query.from(qManufacturer)
			.join(qManufacturer.descriptions,qManufacturerDescription)
			.where(qManufacturer.merchantStore.id.eq(id));
			manList= query.list(qManufacturerDescription);
			return manList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
   //判定该产品是否已经被授权了
   public Integer queryByAuthorization(Long mid){
	   Integer count = 0;
	   try{
			   StringBuilder sb = new StringBuilder();
			   sb.append("select count(*) from AUTH_BRAND ab  ");
			   sb.append(" where ab.BRAND_ID=:id ");
			   
			   Query q = super.getEntityManager().createNativeQuery(sb.toString());
			   q.setParameter("id", mid);
			   
			   Number cou =(Number)q.getSingleResult();
			   
			   if(cou.intValue()>0){
				   count = 1;
			   }else{
				   count = 0;
			   }
	   }catch(Exception e){
			   count=0;
			   e.printStackTrace();
	   }
	   return count;
   }
	
}
