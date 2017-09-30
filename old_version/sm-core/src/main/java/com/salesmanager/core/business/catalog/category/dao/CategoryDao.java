package com.salesmanager.core.business.catalog.category.dao;

import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryList;
import com.salesmanager.core.business.catalog.category.model.UserGrade;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;


public interface CategoryDao extends SalesManagerEntityDao<Long, Category> {

	List<Category> listBySeUrl(MerchantStore store, String seUrl);

	List<Category> listByStoreAndParent(MerchantStore store, Category category);

	List<Category> listByLineage(MerchantStore store, String lineage);

	List<Category> getByName(MerchantStore store, String name, Language language);

	Category getByCode(MerchantStore store, String code);
	
	List<Category> listByStore(MerchantStore store);

	List<Category> listByStore(MerchantStore store, Language language);

	Category getById(Long id);
	
	List<Category> getBySeUrl(Language lang);

	List<Category> listByDepth(MerchantStore store, int depth);

	List<Category> listByDepth(MerchantStore store, int depth, Language language);
	
	Map<String,Category> listTwoDepthByLanguage(MerchantStore store, Language language);

	List<Category> listByLineage(String merchantStoreCode, String lineage);

	Category getByCode(String merchantStoreCode, String code);

	Category getBySeUrl(MerchantStore store, String seUrl);

	List<Category> listByParent(Category category, Language language);

	Category getByLanguage(long categoryId, Language language);

	List<Object[]> countProductsByCategories(MerchantStore store,
			List<Long> categoryIds);

	List<Category> getByCodes(MerchantStore store, List<String> codes,
			Language language);

	List<Category> getByIds(MerchantStore store, List<Long> ids,
			Language language);
	List<UserGrade> getGradeByType(int type);
	
	CategoryList getByCritiria(Criteria criteria,Language language);
	
	List<Category> getListByCriteria(Criteria criteria);

	/**
	 * 根据name查询指定depth的分类集合，By equalsIgnoreCase
	 * @param name
	 * @depth 分类的层级深度，0为一级菜单，-1为根节点
	 * @param language
	 * @return
	 */
	List<Category> getByCateName(String name, Integer depth, Language language);
	
	/**
	 * 查找services下的分类，根据name查询指定depth的分类集合，By equalsIgnoreCase
	 * @param name
	 * @depth 分类的层级深度，0为一级菜单，-1为根节点
	 * @param language
	 * @return
	 */
	public List<Category> getByCateNameForServices(String name, Integer depth, Language language);
	/**
	 * 查找其他下的分类，根据name查询指定depth的分类集合，By equalsIgnoreCase
	 * @param name
	 * @depth 分类的层级深度，0为一级菜单，-1为根节点
	 * @param language
	 * @return
	 */
	public List<Category> getByCateNameForOthers(String name, Integer depth, Language language);
	List<Object[]> countProductsByCriteria(ProductCriteria criteria);

}
