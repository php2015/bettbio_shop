package com.salesmanager.core.business.catalog.category.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.model.CategoryList;
import com.salesmanager.core.business.catalog.category.model.QCategory;
import com.salesmanager.core.business.catalog.category.model.QCategoryDescription;
import com.salesmanager.core.business.catalog.category.model.QUserGrade;
import com.salesmanager.core.business.catalog.category.model.UserGrade;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("categoryDao")
public class CategoryDaoImpl extends SalesManagerEntityDaoImpl<Long, Category> implements CategoryDao {

	public CategoryDaoImpl() {
		super();
	}
	
	@Override
	public List<Category> getByName(MerchantStore store, String name, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qDescription.name.like("%" + name + "%")
			.and(qDescription.language.id.eq(language.getId()))
			.and(qCategory.merchantStore.id.eq(store.getId())))
			.orderBy(qCategory.sortOrder.asc());
		

		
		List<Category> categories = query.list(qCategory);
		return categories;
	}
	


	@Override
	public List<Category> listBySeUrl(MerchantStore store,String seUrl) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qDescription.seUrl.like(seUrl)
			.and(qCategory.merchantStore.id.eq(store.getId())))
			.orderBy(qDescription._super.title.desc(), qDescription._super.name.desc(), qDescription.language.id.desc()).orderBy(qCategory.sortOrder.asc());
		
		return query.list(qCategory);
	}
	
	@Override
	public Category getBySeUrl(MerchantStore store,String seUrl) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qDescription.seUrl.eq(seUrl)
			.and(qCategory.merchantStore.id.eq(store.getId())));

		List<Category> ms = query.list(qCategory);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}		
	}
	
	@Override
	public Category getByCode(MerchantStore store, String code) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.code.eq(code)
			.and(qCategory.merchantStore.id.eq(store.getId())));

		List<Category> cs= query.list(qCategory);
		if(cs !=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}		
	}
	
	@Override
	public List<Category> getByCodes(MerchantStore store, List<String> codes, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.code.in(codes)
			.and(qCategory.merchantStore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc());
		
		return query.list(qCategory);
	}
	
	@Override
	public List<Category> getByIds(MerchantStore store, List<Long> ids, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.id.in(ids)
			.and(qCategory.merchantStore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			//.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
			/**
			 * 为了breadcrumbs显示顺序正确，按depth优先排序
			 */
			.orderBy(qCategory.depth.asc(),qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qDescription.language.id.desc());
		
		return query.list(qCategory);
	}
	
	@Override
	public Category getByLanguage(long categoryId, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.id.eq(categoryId)
			.and(qDescription.language.code.eq(language.getCode())));

		
		List<Category> cs= query.list(qCategory);
		if(cs !=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}	
	}
	
	@Override
	public Category getByCode(String merchantStoreCode, String code) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.code.eq(code)
			.and(qCategory.merchantStore.code.eq(merchantStoreCode)));

		
		List<Category> cs= query.list(qCategory);
		if(cs !=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}	
		}
	
	@Override
	public Category getById(Long id) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.id.eq(id));

		List<Category> cs= query.list(qCategory);
		if(cs !=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}	
	}
	
	@Override
	public List<Category> listByLineage(MerchantStore store, String lineage) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.lineage.like(new StringBuilder().append(lineage).append("%").toString()))
			.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc());
		
		return query.distinct().list(qCategory);
				//.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByLineage(String merchantStoreCode, String lineage) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.lineage.like(new StringBuilder().append(lineage).append("%").toString())
			.and(qCategory.merchantStore.code.eq(merchantStoreCode)))
			.orderBy(qCategory.sortOrder.asc(),qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.distinct().list(qCategory);
				//.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByDepth(MerchantStore store, int depth) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.depth.eq(depth)
			.and(qCategory.merchantStore.id.eq(store.getId())))
			.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.distinct().list(qCategory);
				//.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByDepth(MerchantStore store, int depth, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.depth.eq(depth)
			.and(qCategory.merchantStore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.distinct().list(qCategory);
				//.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByParent(Category category, Language language) {
		
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		if(category != null){
			query.from(qCategory)
				.leftJoin(qCategory.descriptions, qDescription).fetch()
				.leftJoin(qCategory.merchantStore).fetch()
				.leftJoin(qCategory.parent).fetch()
				.where(qCategory.parent.id.eq(category.getId())
				.and(qCategory.merchantStore.id.eq(category.getMerchantStore().getId())))
				.orderBy(qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
			
			return query.distinct().list(qCategory);
		}else{
			query.from(qCategory)
				.leftJoin(qCategory.descriptions, qDescription).fetch()
				.leftJoin(qCategory.merchantStore).fetch()
				.where(qCategory.parent.isNull())					
				.orderBy(qCategory.code.asc(),qCategory.id.asc());
		
		return query.distinct().list(qCategory);
	}
				//listDistinct(qCategory);
		
	}
	
	

	@Override
	public List<Category> listByStoreAndParent(MerchantStore store, Category category) {
		QCategory qCategory = QCategory.category;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		if (store == null) {
			if (category == null) {
				query.from(qCategory)
					.where(qCategory.parent.isNull())
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				query.from(qCategory)
					.where(qCategory.parent.eq(category))
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			}
		} else {
			if (category == null) {
				query.from(qCategory)
					.where(qCategory.parent.isNull()
						.and(qCategory.merchantStore.eq(store)))
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				query.from(qCategory)
					.where(qCategory.parent.eq(category)
						.and(qCategory.merchantStore.eq(store)))
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			}
		}
		
		return query.list(qCategory);
	}
	
	


	@Override
	public List<Category> listByStore(MerchantStore store) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.merchantStore.id.eq(store.getId()))
			.orderBy(qCategory.sortOrder.asc(),qCategory.id.asc());
		
		return query.distinct().list(qCategory);
				//listDistinct(qCategory);
	}
	
	@Override
	public List<Object[]> countProductsByCategories(MerchantStore store, List<Long> categoryIds) {

		
		StringBuilder qs = new StringBuilder();
		qs.append("select categories, count(product.id) from Product product ");
		qs.append("inner join product.categories categories ");
		qs.append("where categories.id in (:cid) ");
		qs.append("and product.available=true and product.dateAvailable<=:dt ");
		qs.append("group by categories.id");
		
    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", categoryIds);
    	q.setParameter("dt", new Date());


    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
		
		
	}
	
	@Override
	public List<Category> listByStore(MerchantStore store, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where((qCategory.merchantStore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qCategory.code.asc());
		
		return query.distinct().list(qCategory);
				//listDistinct(qCategory);
	}
	
	public List<Category> listTwoDepthByStore(MerchantStore store, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where((qCategory.merchantStore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			//.and(qCategory.depth.in(0,1,2,3)))
			.orderBy(qCategory.depth.desc(),qCategory.code.asc(),qCategory.id.asc());
		
		return query.distinct().list(qCategory);
				//listDistinct(qCategory);
	}
	
	@Override
	public Map<String,Category> listTwoDepthByLanguage(MerchantStore store, Language language){
		List<Category> categorys = listTwoDepthByStore(store,language);			
		 Map<String,Category> swapMap = new HashMap<String,Category>();
		 if(categorys!=null && categorys.size()>0) {
			 for(int j=0;j<categorys.size();j++){
				 Category category = categorys.get(j);	
				 	//add descinfo
				 	setDescription(category,language);
					swapMap.put(category.getCode(),category);						
			 }
			 /*for(int i=0;i<categorys.size();i++){
				 Category category = categorys.get(i);
				 if (category.getParent() != null){
					 Category parentCategory = swapMap.get(category.getParent().getCode());
					 if(parentCategory != null){
						 List<Category> sonCategorys = parentCategory.getCategories();
						 if ( null == sonCategorys || sonCategorys.size()==0) {
							 sonCategorys =  new ArrayList<Category>(); 
						 }							
						 sonCategorys.add(category);
						 parentCategory.setCategories(sonCategorys);
						 //swapMap.put(parentCategory.getCode(), parentCategory);
					 }
					 swapMap.remove(category.getCode());
				 }
			 }*/
	}
		 return swapMap;	
	}
	
	/**
	 * add Descriptions
	 * @param category
	 * @param language
	 * @return
	 */
	private Category setDescription(Category category, Language language){
		List<CategoryDescription> descriptions = category.getDescriptions();
		for(CategoryDescription description : descriptions) {
			Language lang = description.getLanguage();
			if(lang.equals(language)){
				category.setDescinfo(description.getName());
				category.setUrl(description.getSeUrl());
				break;
			}
		}
		return category;
	}

	@Override
	public List<Category> getBySeUrl(Language lang) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where((qDescription.language.id.eq(lang.getId())))
			.orderBy(qCategory.sortOrder.asc(),qCategory.id.asc());
		
		return query.distinct().list(qCategory);
	}

	@Override
	public List<UserGrade> getGradeByType(int type) {
		// TODO Auto-generated method stub
		QUserGrade qUserGrade = QUserGrade.userGrade;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qUserGrade)	
		
			.where(qUserGrade.type.eq(type))
			
			.orderBy(qUserGrade.code.asc());
		
		List<UserGrade> userGrades = query.list(qUserGrade);
		
		return userGrades;
	}

	@Override
	public CategoryList getByCritiria(Criteria criteria,Language language) {
		// TODO Auto-generated method stub
		CategoryList cats = new CategoryList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(c) from Category as c ");
		countBuilderSelect.append("INNER JOIN c.descriptions de");
		StringBuilder countBuilderWhere = new StringBuilder();
		
		countBuilderWhere.append(" where de.language.id = " + language.getId());
				
		if(!StringUtils.isEmpty(criteria.getFindName())) {			
			countBuilderWhere.append(" and (de.name like:den or c.code like:cd )");
		}
		
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		if(criteria.getAvaiable() == 2){
			countBuilderWhere.append(" and c.visible = " + true);
		}else if(criteria.getAvaiable() == 0){
			countBuilderWhere.append(" and c.visible = " + false);
		}
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter("den",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
			countQ.setParameter("cd",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());			
		}
		
		Number count = (Number) countQ.getSingleResult ();

		cats.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return cats;
        
		QCategory qCategory = QCategory.category;
		QCategoryDescription categoryDescription = QCategoryDescription.categoryDescription;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qCategory)
		.join(qCategory.descriptions,categoryDescription).fetch()
		.orderBy(qCategory.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(categoryDescription.language.id.eq(language.getId()));
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			pBuilder.andAnyOf(qCategory.code.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					categoryDescription.name.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
		}
		if(criteria.getAvaiable() == 2){
			pBuilder.and(qCategory.visible.eq(true));
		}else if(criteria.getAvaiable() == 0){
			pBuilder.and(qCategory.visible.eq(false));
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		cats.setCats(query.list(qCategory));
		return cats;
	}

	@Override
	public List<Category> getListByCriteria(Criteria criteria) {
		
				
				QCategory qCategory = QCategory.category;
				QCategoryDescription categoryDescription = QCategoryDescription.categoryDescription;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qCategory)
				.join(qCategory.descriptions,categoryDescription).fetch()
				.orderBy(qCategory.id.asc());
				
				BooleanBuilder pBuilder = new BooleanBuilder();
				
				if(criteria.getMaxCount()>0) {
					query.limit(criteria.getMaxCount());
					query.offset(criteria.getStartIndex());
				}
				
				if(pBuilder!=null) {
					query.where(pBuilder);
				}
				
				return query.list(qCategory);
	}
	
	/**
	 * 根据name查询指定depth的分类集合，By equalsIgnoreCase
	 * @param name
	 * @depth 分类的层级深度，0为一级菜单，-1为根节点
	 * @param language
	 * @return
	 */
	@Override
	public List<Category> getByCateName(String name, Integer depth, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch().orderBy(qCategory.sortOrder.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qDescription.name.equalsIgnoreCase(name));
		
		if (depth!=null) {
			pBuilder.and(qCategory.depth.eq(depth));
			if(depth>0){
				pBuilder.and(qCategory.lineage.like("/1/"+"%")); //设定查找试剂下的分类
			}else{
				pBuilder.and(qCategory.lineage.like("/"+"%")); //设定查找试剂下的分类
			}		
		}
		if (language != null) {
			pBuilder.and(qDescription.language.id.eq(language.getId()));
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		List<Category> categories = query.list(qCategory);
		return categories;
	}
	
	/**
	 * 查找services下的分类，根据name查询指定depth的分类集合
	 * @param name
	 * @param depth
	 * @param language
	 * @return
	 */
	public List<Category> getByCateNameForServices(String name, Integer depth, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch().orderBy(qCategory.sortOrder.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qDescription.name.equalsIgnoreCase(name));
		
		if (depth!=null) {
			if(depth>0){
				pBuilder.and(qCategory.lineage.like("/4/"+"%")); //设定查找services下的分类
			}else{
				pBuilder.and(qCategory.lineage.like("/"+"%")); //设定查找services下的分类
			}
			pBuilder.and(qCategory.depth.eq(depth));
		}
		if (language != null) {
			pBuilder.and(qDescription.language.id.eq(language.getId()));
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		List<Category> categories = query.list(qCategory);
		return categories;
	}

	@Override
	public List<Object[]> countProductsByCriteria(ProductCriteria criteria) {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select categories, count(product.id) from Product product ");
		qs.append("inner join product.categories categories ");
		if(criteria.getManufacturers()!=null) {
			qs.append("inner join product.manufacturer manuf  ");
		}
		qs.append("where categories.id in (:cid) ");
		qs.append("and product.available=true and product.dateAvailable<=:dt ");
		if(criteria.getManufacturers()!=null) {
			qs.append("and manuf.id in (:mid) ");
		}
		qs.append("group by categories.id");
		
    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", criteria.getCategoryIds());
    	q.setParameter("dt", new Date());
    	if(criteria.getManufacturers()!=null) {
    		q.setParameter("mid", criteria.getManufacturers());
    	}


    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
	}

	@Override
	public List<Category> getByCateNameForOthers(String name, Integer depth,
			Language language) {
		// TODO Auto-generated method stub
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch().orderBy(qCategory.sortOrder.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qDescription.name.equalsIgnoreCase(name));		 
		
		if (depth!=null) {
			if(depth>0){
				pBuilder.andAnyOf(qCategory.lineage.like("/2/"+"%"),qCategory.lineage.like("/3/"+"%"));//设定查找其他的分类
			}else{
				pBuilder.and(qCategory.lineage.like("/"+"%"));
			}
			pBuilder.and(qCategory.depth.eq(depth));
		}
		if (language != null) {
			pBuilder.and(qDescription.language.id.eq(language.getId()));
		}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		List<Category> categories = query.list(qCategory);
		return categories;
	}
}
