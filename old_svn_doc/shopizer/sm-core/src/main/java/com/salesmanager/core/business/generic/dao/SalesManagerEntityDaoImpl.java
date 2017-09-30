package com.salesmanager.core.business.generic.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.generic.util.GenericEntityUtils;

/**
 * @param <T> entity type
 */
public abstract class SalesManagerEntityDaoImpl<K extends Serializable & Comparable<K>, E extends SalesManagerEntity<K, ?>>
		extends SalesManagerJpaDaoSupport
		implements SalesManagerEntityDao<K, E> {
	
	private Class<E> objectClass;
	
	@SuppressWarnings("unchecked")
	public SalesManagerEntityDaoImpl() {
		this.objectClass = (Class<E>) GenericEntityUtils.getGenericEntityClassFromComponentDefinition(getClass());
	}
	
	protected final Class<E> getObjectClass() {
		return objectClass;
	}
	
	
	public E getEntity(Class<? extends E> clazz, K id) {
		return super.getEntity(getObjectClass(), id);
	}
	
	
	public E getById(K id) {
		return super.getEntity(getObjectClass(), id);
	}
	
	
	public <V> E getByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.getByField(getObjectClass(), attribute, fieldValue);
	}
	
	
	public void update(E entity) {
		super.update(entity);
	}
	
	
	public void save(E entity) {
		super.save(entity);
	}
	
	
	public void delete(E entity) {
		super.delete(entity);
	}
	
	
	public E refresh(E entity) {
		return super.refresh(entity);
	}
	
	
	public List<E> list() {
		return super.listEntity(getObjectClass());
	}
	
	
	public <V> List<E> listByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.listEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	
	public <T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		return super.listEntity(objectClass, filter, limit, offset, orders);
	}
	
	
	public Long count() {
		return super.countEntity(getObjectClass());
	}
	
	
	public <V> Long countByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.countEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	
	public Long count(Expression<Boolean> filter) {
		return super.countEntity(getObjectClass(), filter);
	}
	
	
	@Override
	public EntityManager getEntityManager() {
		return super.getEntityManager();
	}
	
	/**
	 * 不支持子对象属性的查询
	 */
	public List<E> findByProperties(String[] proNames, Object[] proValues, HashMap<String, String> orderby) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(getObjectClass());
		Root<E> root = rootCriteriaQuery(builder, criteria, getObjectClass());
		Predicate p = null;

		if (proNames!=null&&proNames.length>0) {
			for (int i = 0; i < proNames.length; i++) {
				Path<Object> path = root.get(proNames[i]);
				if(p!=null){
					p = builder.and(builder.equal(path, proValues[i]), p);
				} else {
					p = builder.equal(path, proValues[i]);
				}
//			criteria.where(builder.equal(root.get(proNames[i]), proValues[i]));
			}
			criteria.where(p);
		}
		if (orderby!=null && orderby.size()>0) {
			List<Order> orders = new ArrayList<Order>();
			Iterator<Entry<String, String>> iter = orderby.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				if (value.equalsIgnoreCase("asc"))
					orders.add(builder.asc(root.get(key)));
				else if (value.equalsIgnoreCase("desc"))
					orders.add(builder.desc(root.get(key)));
			}
			criteria.orderBy(orders);
		}
		List<E> entities = buildTypedQuery(criteria, null, null).getResultList();
		return entities;
	}
	
	/**
	 * 批量删除组件集合
	 * @param ids
	 */
	public <T> void deleteByIds(List<T> ids) {
		if(ids!=null && ids.size()>0) {
			String delete = "delete from " + getObjectClass().getName() + " o where ";
			delete += " o.id=?0";
			for (int i = 1; i < ids.size(); i++) {
				delete += " or o.id=?" + i + " ";
			}
			Query query = getEntityManager().createQuery(delete);
			for (int i = 0; i < ids.size(); i++) {
				T id = ids.get(i);
				query.setParameter(i, id);
			}
			query.executeUpdate();
		}
	}
	
	/**
	 * 根据条件删除所对应的对象集合
	 * @param proNames
	 * @param proValues
	 */
	public void deleteByProperties(String[] proNames, Object[] proValues) {
		if (proNames!=null && proValues.length>0) {
			StringBuffer delete = new StringBuffer("delete from " + getObjectClass().getName() + " as o where ");
			for (int i = 0; i < proNames.length; i++) {
				delete.append(" o.").append(proNames[i]).append("=?").append(i).append(" and");
			}
			String tmp = delete.substring(0, delete.length()-3);
			Query query = getEntityManager().createQuery(tmp);
			for (int i = 0; i < proValues.length; i++) {
				query.setParameter(i, proValues[i]);
			}
			query.executeUpdate();
		}
	}
	
	public List<E> findByPropertiesHQL(String[] proNames, Object[] proValues, HashMap<String, String> orderby) {
		if (proNames!=null && proValues.length>0) {
			StringBuffer sql = new StringBuffer("from " + getObjectClass().getName() + " as o where ");
			for (int i = 0; i < proNames.length; i++) {
				sql.append(" o.").append(proNames[i]).append("=?").append(i).append(" and");
			}
			String tmp = sql.substring(0, sql.length()-3);
			Query query = getEntityManager().createQuery(tmp);
			for (int i = 0; i < proValues.length; i++) {
				query.setParameter(i, proValues[i]);
			}
			return query.getResultList();
		} else return null;
	}
}
