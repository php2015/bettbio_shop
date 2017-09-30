package com.salesmanager.core.business.generic.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;

/**
 * <p>Service racine pour la gestion des entités.</p>
 *
 * @param <T> type d'entité
 */
public interface SalesManagerEntityService<K extends Serializable & Comparable<K>, E extends SalesManagerEntity<K, ?>> extends TransactionalAspectAwareService{

	/**
	 * Crée l'entité dans la base de données. Mis à part dans les tests pour faire des sauvegardes simples, utiliser
	 * create() car il est possible qu'il y ait des listeners sur la création d'une entité.
	 * 
	 * @param entity entité
	 */
	void save(E entity) throws ServiceException;
	
	/**
	 * Met à jour l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void update(E entity) throws ServiceException;
	
	/**
	 * Crée l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void create(E entity) throws ServiceException;

	/**
	 * Supprime l'entité de la base de données
	 * 
	 * @param entity entité
	 */
	void delete(E entity) throws ServiceException;
	
	/**
	 * Rafraîchit l'entité depuis la base de données
	 * 
	 * @param entity entité
	 */
	E refresh(E entity);
	

	/**
	 * Retourne une entité à partir de son id.
	 * 
	 * @param id identifiant
	 * @return entité
	 */
	E getById(K id);
	
	/**
	 * Renvoie la liste de l'ensemble des entités de ce type.
	 * 
	 * @return liste d'entités
	 */
	List<E> list();
	
	<V> List<E> listByField(SingularAttribute<? super E, V> fieldName, V fieldValue);
	
	/**
	 * Retourne une entité à partir de sa classe et son id.
	 * 
	 * @param clazz classe
	 * @param id identifiant
	 * @return entité
	 */
	E getEntity(Class<? extends E> clazz, K id);
	
	/**
	 * Compte le nombre d'entités de ce type présentes dans la base.
	 * 
	 * @return nombre d'entités
	 */
	Long count();
	
	/**
	 * Flushe la session.
	 */
	void flush();

	void clear();
	
	/**
	 * 根据输入条件和orderby信息查询数据
	 * @param proNames
	 * @param proValues
	 * @param orderby
	 * @return
	 */
	List<E> findByProperties(String[] proNames, Object[] proValues, HashMap<String, String> orderby);
	
	List<E> findByPropertiesHQL(String[] proNames, Object[] proValues, HashMap<String, String> orderby);
	
	List<E> getListByCriteria(Criteria criteria) ;
	 
	/**
	 * 批量删除主键ID集合，主键属性名必须为id
	 * @param ids
	 */
	public <T> void deleteByIds(List<T> ids); 

	/**
	 * 根据条件删除所对应的对象集合
	 * @param proNames
	 * @param proValues
	 */
	public void deleteByProperties(String[] proNames, Object[] proValues);
}
