package com.salesmanager.core.business.catalog.category.service;

import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.model.CategoryList;
import com.salesmanager.core.business.catalog.category.model.UserGrade;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface CategoryService extends SalesManagerEntityService<Long, Category> {

	List<Category> listByLineage(MerchantStore store, String lineage) throws ServiceException;
	
	List<Category> listBySeUrl(MerchantStore store, String seUrl) throws ServiceException;
	
	CategoryDescription getDescription(Category category, Language language) throws ServiceException;

	void addCategoryDescription(Category category, CategoryDescription description) throws ServiceException;

	void addChild(Category parent, Category child) throws ServiceException;

	List<Category> listByParent(Category category) throws ServiceException;
	
	List<Category> listByStoreAndParent(MerchantStore store, Category category) throws ServiceException;
	
	
	List<Category> getByName(MerchantStore store, String name, Language language) throws ServiceException;
	
	List<Category> listByStore(MerchantStore store) throws ServiceException;

	Category getByCode(MerchantStore store, String code)
			throws ServiceException;

	List<Category> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	void saveOrUpdate(Category category) throws ServiceException;

	List<Category> listByDepth(MerchantStore store, int depth);

	/**
	 * Get root categories by store for a given language
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<Category> listByDepth(MerchantStore store, int depth, Language language);

	List<Category> listByLineage(String storeCode, String lineage)
			throws ServiceException;

	/**
	 * get root cateories and 1 class categories by store for given language
	 * @param storeCode
	 * @param lineage
	 * @return
	 * @throws ServiceException
	 */
	Map<String,Category> listTwoDepthByLanguage(MerchantStore store, Language language)
			throws ServiceException;
	
	Category getByCode(String storeCode, String code) throws ServiceException;

	Category getBySeUrl(MerchantStore store, String seUrl);
	List<Category> getBySeUrl(Language lang);

	List<Category> listByParent(Category category, Language language);

	Category getByLanguage(long categoryId, Language language);

	/**
	 * Returns a list by category containing the category code and the number of products
	 * 1->obj[0] = book
	 *    obj[1] = 150
	 * 2->obj[0] = novell
	 *    obj[1] = 35
	 *   ...
	 * @param store
	 * @param categoryIds
	 * @return
	 * @throws ServiceException
	 */
	List<Object[]> countProductsByCategories(MerchantStore store,
			List<Long> categoryIds) throws ServiceException;

	/**
	 * Returns a list of Category by category code for a given language
	 * @param store
	 * @param codes
	 * @param language
	 * @return
	 */
	List<Category> listByCodes(MerchantStore store, List<String> codes,
			Language language);

	/**
	 * List of Category by id
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 */
	List<Category> listByIds(MerchantStore store, List<Long> ids,
			Language language);


	/**
	 * get user grade
	 */
	List<UserGrade> getGradeByType(int type);
	
	CategoryList getByCritiria(Criteria criteria,Language language);
	
	List<Category> getListByCriteria(Criteria criteria);
	
	/**
	 * 根据name查询指定depth的分类集合，By equalsIgnoreCase
	 * @param name
	 * @depth 分类的层级深度，0为一级菜单，-1为根节点，只查试剂的类别
	 * @param language
	 * @return
	 */
	public List<Category> getByCateName(String name, Integer depth, Language language) ;
	
	/**
	 * 查找services下的分类，根据name查询指定depth的分类集合，By equalsIgnoreCase
	 * @param name
	 * @depth 分类的层级深度，0为一级菜单，-1为根节点
	 * @param language
	 * @return
	 */
	public List<Category> getByCateNameForOthers(String name, Integer depth, Language language);
	
	/**
	 * 查找services下的分类，根据name查询指定depth的分类集合，By equalsIgnoreCase
	 * @param name
	 * @depth 分类的层级深度，0为一级菜单，-1为根节点
	 * @param language
	 * @return
	 */
	public List<Category> getByCateNameForServices(String name, Integer depth, Language language);
	
    public List<String> parseCategoryLineage(String lineage) ;
    
    public List<Object[]> countProductsByCriteria(ProductCriteria criteria);
}
