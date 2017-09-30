package com.salesmanager.core.business.catalog.product.dao.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationsipList;
import com.salesmanager.core.business.catalog.product.model.relationship.QProductRelationship;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productRelationshipDao")
public class ProductRelationshipDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductRelationship>
		implements ProductRelationshipDao {
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type, Product product, Language language) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where pr.code=:code ");
		qs.append("and pr.store.id=:storeId ");
		qs.append("and p.id=:id ");
		qs.append("and rpd.language.id=:langId");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);
    	q.setParameter("id", product.getId());
//    	qs.append("and pr.store.id=:storeId ");
//    	modify by sam, correct this set.
    	q.setParameter("storeId", store.getId());
    	q.setParameter("langId", language.getId());


    	
    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type, Language language) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		
		qs.append("left join fetch rp.attributes pattr ");
		qs.append("left join fetch rp.descriptions rpd ");
		qs.append("left join fetch rp.images pd ");
		qs.append("left join fetch rp.merchantStore rpm ");
		qs.append("left join fetch rpm.currency rpmc ");
		qs.append("left join fetch rp.availabilities pa ");
		qs.append("left join fetch rp.manufacturer m ");
		qs.append("left join fetch m.descriptions md ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");

		qs.append("where pr.code=:code ");
		qs.append("and pr.store.id=:storeId ");
		qs.append("and rpd.language.id=:langId");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);
    	q.setParameter("langId", language.getId());
    	q.setParameter("storeId", store.getId());


    	
    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> getByGroup(MerchantStore store, String group) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationship as pr ");
		/*qs.append("left join fetch pr.product p ");
		qs.append("left join fetch pr.relatedProduct rp ");
		
		qs.append("left join fetch rp.attributes pattr ");
		qs.append("left join fetch rp.descriptions rpd ");
		qs.append("left join fetch rp.images pd ");
		qs.append("left join fetch rp.merchantStore rpm ");
		qs.append("left join fetch rpm.currency rpmc ");
		qs.append("left join fetch rp.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");
		qs.append("left join fetch rp.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch rp.type type ");*/

		qs.append("where pr.code=:code ");
		qs.append("and pr.store.id=:storeId ");




    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", group);
    	q.setParameter("storeId", store.getId());



    	
    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> getGroups(MerchantStore store) {

		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationship as pr ");
		//admin 获取所有
		if(store !=null){
			qs.append("where pr.store.id=:store ");
		}
		//qs.append("and pr.product=null");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);
		if(store !=null){
			q.setParameter("store", store.getId());
		}

    	
    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();
    	
    	Map<String,ProductRelationship> relationMap = new HashMap<String,ProductRelationship>();
    	for(ProductRelationship relationship : relations) {
    		if(!relationMap.containsKey(relationship.getCode())) {
    			relationMap.put(relationship.getCode(), relationship);
    		}
    	}
    	
    	List<ProductRelationship> returnList = new ArrayList<ProductRelationship>(relationMap.values());

    	
    	return returnList;
		

	}
	
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where pr.code=:code ");
		qs.append("and pr.store.id=:storeId ");




    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);
    	q.setParameter("storeId", store.getId());


    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> listByProducts(Product product) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("left join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.attributes pattr ");
		qs.append("left join fetch p.descriptions pd ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where p.id=:id");
		qs.append(" or rp.id=:id");




    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("id", product.getId());


    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type, Product product) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		
		StringBuilder qs = new StringBuilder();
		
		qs.append("select distinct pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("left join fetch pr.relatedProduct rp ");
		
		qs.append("left join fetch rp.attributes pattr ");
		qs.append("left join fetch rp.descriptions rpd ");
		qs.append("left join fetch rp.images pd ");
		//关联certificate，proof，thirdproof
		qs.append("left join fetch rp.certificates rpct ");
		qs.append("left join fetch rp.proofs rppr ");
		qs.append("left join fetch rp.thirdproofs rpth ");
		
		qs.append("left join fetch rp.merchantStore rpm ");
		qs.append("left join fetch rpm.currency rpmc ");
		qs.append("left join fetch rp.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");
		
		qs.append("left join fetch rp.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch rp.type type ");

		qs.append("where pr.code=:code ");
		qs.append("and p.id=:pId");




    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);
    	q.setParameter("pId", product.getId());


		@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	private String addWhere (StringBuilder strb){
		if(strb.length()>0 && strb.indexOf("where") <0){
			int index = strb.indexOf("and");
			return " where " + strb.substring(index+3);
		}
		return strb.toString();
	}

	@Override
	public ProductRelationsipList getByCritira(Criteria criteria) {
		// TODO Auto-generated method stub
		ProductRelationsipList ships = new ProductRelationsipList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(s) from ProductRelationship as s ");
		
		StringBuilder countBuilderWhere = new StringBuilder();		
				
		if(!StringUtils.isEmpty(criteria.getFindName())) {			
			countBuilderWhere.append(" where (s.code like:code )");
		}
		
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + addWhere(countBuilderWhere));

		if(criteria.getStoreId() != -1l){
			countBuilderWhere.append(" and s.merchantStore.id =" +criteria.getStoreId());
		}
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter("code",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());			
		}
		if(criteria.getAvaiable() == 2){
			countBuilderWhere.append(" and s.active = true" );
		}else if(criteria.getAvaiable() == 0){
			countBuilderWhere.append(" and s.active = false" );
		}
		
		Number count = (Number) countQ.getSingleResult ();

		ships.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return ships;
        
		QProductRelationship qProductRelationship = QProductRelationship.productRelationship;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductRelationship)		
		.join(qProductRelationship.store,qMerchantStore).fetch()
		.orderBy(qMerchantStore.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			pBuilder.andAnyOf(qProductRelationship.code.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
		}
		
		if(criteria.getStoreId() != -1l){
			pBuilder.and(QMerchantStore.merchantStore.id.eq(criteria.getStoreId()));
		}
		
		if(criteria.getAvaiable() == 2){
			pBuilder.and(qProductRelationship.active.eq(true));
		}else if(criteria.getAvaiable() == 0){
			pBuilder.and(qProductRelationship.active.eq(false));
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		ships.setShips(query.list(qProductRelationship));
		return ships;
	}

	@Override
	public List<ProductRelationship> getListByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		        
		QProductRelationship qProductRelationship = QProductRelationship.productRelationship;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductRelationship)		
		.join(qProductRelationship.store,qMerchantStore).fetch()
		.orderBy(qMerchantStore.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		if(criteria.getStoreId() != -1l){
			pBuilder.and(QMerchantStore.merchantStore.id.eq(criteria.getStoreId()));
		}
		
		if(criteria.getAvaiable() == 2){
			pBuilder.and(qProductRelationship.active.eq(true));
		}else if(criteria.getAvaiable() == 0){
			pBuilder.and(qProductRelationship.active.eq(false));
		}
		
		if(!StringUtils.isBlank(criteria.getCode())){
			pBuilder.and(qProductRelationship.code.eq(criteria.getCode()));
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		return query.list(qProductRelationship);
	}

	@Override
	public List<ProductRelationship> getByCodeProduct(String code, long id) {
		// TODO Auto-generated method stub
		QProductRelationship qProductRelationship = QProductRelationship.productRelationship;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProductRelationship)		
		.join(qProductRelationship.store,qMerchantStore).fetch()
		.orderBy(qMerchantStore.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		//all delete all
		if(!code.equalsIgnoreCase("all")){
			pBuilder.and(qProductRelationship.code.eq(code));
		}
		pBuilder.and(qProductRelationship.product.id.eq(id));
		
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		return query.list(qProductRelationship);
	}

	@Override
	public void deleteByProductId(Long productID) {
		// TODO Auto-generated method stub
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("delete from ProductRelationship as prs ");		
		StringBuilder countBuilderWhere = new StringBuilder();
	
		countBuilderWhere.append(" where prs.product.id = ").append(productID);
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		countQ.executeUpdate();
	}

	


}
