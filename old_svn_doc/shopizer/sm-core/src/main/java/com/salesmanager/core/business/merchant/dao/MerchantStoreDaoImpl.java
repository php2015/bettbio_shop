package com.salesmanager.core.business.merchant.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;
import com.salesmanager.core.business.merchant.model.StoreList;


@Repository("merchantStoreDao")
public class MerchantStoreDaoImpl extends SalesManagerEntityDaoImpl<Long, MerchantStore> implements MerchantStoreDao {

	public MerchantStoreDaoImpl() {
		super();
	}
	
	@Override
	public Collection<Product> getProducts(MerchantStore merchantStore) throws ServiceException {
		

		

		StringBuilder qs = new StringBuilder();
	
		
		qs.append("from ProductMerchant as pm, Product as p ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch pa.prices pap ");
		qs.append("join fetch pap.descriptions papd ");
		//images
		qs.append("left join fetch p.images images ");
		//options
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		qs.append("where pm.merchantId=:mid ");
		qs.append("and pm.productId=p.productId ");

		
		
		
		
		// TODO : WTF?
    	//String hql = qs.toString();
		//Query q = this.entityManager.createQuery(hql);
    	
		Session session = (Session)super.getEntityManager().getDelegate();
		org.hibernate.Query q = session.createQuery(qs.toString());
		q.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		

    	q.setLong("mid", merchantStore.getId());
    	//q.setParameterList("lid", regionList);
    	//@TODO languageUtil
    	//q.setParameter("lang", 1);
		
		Collection<Product> results = q.list();
    	//MerchantStore s = (MerchantStore)q.getSingleResult();
    	//Collection<Product> results = s.getProducts();

		return results;

		
		
		
	}
	
	@Override
	public MerchantStore getById(Long id)  {
		
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qMerchantStore)
			.innerJoin(qMerchantStore.defaultLanguage).fetch()
			.leftJoin(qMerchantStore.currency).fetch()
			.leftJoin(qMerchantStore.country).fetch()
			.leftJoin(qMerchantStore.zone).fetch()
			.leftJoin(qMerchantStore.languages).fetch()
			.where(qMerchantStore.id.eq(id));
		
		List<MerchantStore> ms = query.list(qMerchantStore);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}		
		
	}
	
	@Override
	public MerchantStore getMerchantStore(String code)   throws ServiceException {
	
/*		
		String q = "from MerchantStore m join fetch m.defaultLanguage left join fetch m.currency left join fetch m.country left join fetch m.zone left join fetch m.languages where m.code=:code";
		
		
		Query queryQ = super.getEntityManager().createQuery(q);
		queryQ.setParameter("code", code);
		
		return (MerchantStore)queryQ.getSingleResult();*/
		//TODO add fetch
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qMerchantStore)
			.innerJoin(qMerchantStore.defaultLanguage).fetch()
			.leftJoin(qMerchantStore.currency).fetch()
			.leftJoin(qMerchantStore.country).fetch()
			.leftJoin(qMerchantStore.zone).fetch()
			.leftJoin(qMerchantStore.languages).fetch()
			.where(qMerchantStore.code.eq(code));
		List<MerchantStore> ms = query.list(qMerchantStore);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
				
		
	}
	
	@Override
	public MerchantStore getByname(String name)   throws ServiceException {
	
/*		
		String q = "from MerchantStore m join fetch m.defaultLanguage left join fetch m.currency left join fetch m.country left join fetch m.zone left join fetch m.languages where m.code=:code";
		
		
		Query queryQ = super.getEntityManager().createQuery(q);
		queryQ.setParameter("code", code);
		
		return (MerchantStore)queryQ.getSingleResult();*/
		//TODO add fetch
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qMerchantStore)
			.innerJoin(qMerchantStore.defaultLanguage).fetch()
			.leftJoin(qMerchantStore.currency).fetch()
			.leftJoin(qMerchantStore.country).fetch()
			.leftJoin(qMerchantStore.zone).fetch()
			.leftJoin(qMerchantStore.languages).fetch()
			.where(qMerchantStore.storename.eq(name));
		
		List<MerchantStore> ms = query.list(qMerchantStore);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	public MerchantStore getMerchantStore(Long merchantStoreId) {
		
		
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		//TODO Zone country
		query.from(qMerchantStore)
			.innerJoin(qMerchantStore.defaultLanguage)
			.leftJoin(qMerchantStore.currency)
			.leftJoin(qMerchantStore.country)
			.leftJoin(qMerchantStore.zone)
			.leftJoin(qMerchantStore.languages)
			.where(qMerchantStore.id.eq(merchantStoreId));
		
		List<MerchantStore> ms = query.list(qMerchantStore);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
	}

	@Override
	public StoreList getByCriteria(Criteria criteria)
			throws ServiceException {
		// TODO Auto-generated method stub
		StoreList stores = new StoreList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(m) from MerchantStore as m");
		StringBuilder countBuilderWhere = new StringBuilder();
		//不显示默认店铺
		countBuilderWhere.append(" where m.code <> 'DEFAULT'");
				
		if(!StringUtils.isEmpty(criteria.getFindName())) {			
			countBuilderWhere.append(" and (m.storename like:sn or m.storeaddress like:ma or m.code like:mc )");
		}
		
		if(criteria.getAvaiable()==2){
			countBuilderWhere.append(" and m.seeded = true");
		}else if(criteria.getAvaiable()==0){
			countBuilderWhere.append(" and m.seeded = false");
		}
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter("sn",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
			countQ.setParameter("ma",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
			countQ.setParameter("mc",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
		}
		
		Number count = (Number) countQ.getSingleResult ();

		stores.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return stores;
        
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qMerchantStore)
		.orderBy(qMerchantStore.code.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.andNot(qMerchantStore.code.equalsIgnoreCase("DEFAULT"));
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			pBuilder.andAnyOf(qMerchantStore.storename.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qMerchantStore.storeaddress.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qMerchantStore.code.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
		}
		if(criteria.getAvaiable()==2){
			pBuilder.and(qMerchantStore.seeded.eq(true));
		}else if(criteria.getAvaiable()==0){
			pBuilder.and(qMerchantStore.seeded.eq(false));
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		stores.setStores(query.list(qMerchantStore));
		return stores;
	}

	@Override
	public List<MerchantStore> getListByCriteria(Criteria criteria) {
		
		        
				QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qMerchantStore)
				.orderBy(qMerchantStore.code.asc());
				
				BooleanBuilder pBuilder = new BooleanBuilder();
				
				pBuilder.andNot(qMerchantStore.code.equalsIgnoreCase("DEFAULT"));
				
				
				if(criteria.getMaxCount()>0) {
					query.limit(criteria.getMaxCount());
					query.offset(criteria.getStartIndex());
				}
				
				if(pBuilder!=null) {
					query.where(pBuilder);
				}				
				return query.list(qMerchantStore);
	}

	@Override
	public List<Object[]> getStoreName() {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select MERCHANT_ID as id,STORE_NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(STORE_NAME USING gbk),1)),16,10), ");
		qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
		qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
		qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(STORE_NAME,1)) as pinyin  from MERCHANT_STORE order by pinyin,name ");
		
		Query q  =super.getEntityManager().createNativeQuery(qs.toString());

    	//String hql = "select id,storename,coalesce(CONV(HEX(SUBSTRING(CONVERT(STORE_NAME,'gbk'),1,1)),16,10),SUBSTRING(STORE_NAME,1,1)) from MerchantStore";
	//	Query q = super.getEntityManager().createQuery(hql);
    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
	}
	 public String queryByStoreFileName(long id){
		 StringBuffer  str = new StringBuffer();
		 str.append("select m.businesslicence from MerchantStore m where m.id="+id);
		 Query query = super.getEntityManager().createQuery(str.toString());
		 String fileName = query.getResultList().get(0).toString();
		   return fileName ;
	   }

	
}
