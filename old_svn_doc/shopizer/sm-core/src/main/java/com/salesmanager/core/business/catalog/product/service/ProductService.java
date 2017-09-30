package com.salesmanager.core.business.catalog.product.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;



public interface ProductService extends SalesManagerEntityService<Long, Product> {

	Product getBySeUrlId(MerchantStore store,String seUrl, Locale locale);
	void addProductDescription(Product product, ProductDescription description) throws ServiceException;
	
	ProductDescription getProductDescription(Product product, Language language);
	
	Product getProductForLocale(long productId, Language language, Locale locale) throws ServiceException;
	
	List<Product> getProductsForLocale(Category category, Language language, Locale locale) throws ServiceException;

	List<Product> getProducts(List<Long> categoryIds) throws ServiceException;

	List<Product> getProductsByMan(List<Long> manIds,Long merchantId) throws ServiceException;

	ProductList listByStore(MerchantStore store, Language language,
			ProductCriteria criteria);

	ProductList listByCriteria(Language language,
			ProductCriteria criteria);
	
	ProductList listPsByCriteria(Language language,
			ProductCriteria criteria);
			
	void saveOrUpdate(Product product) throws ServiceException;

	List<Product> listByStore(MerchantStore store);

	
	List<Product> getProducts(List<Long> categoryIds, Language language)
			throws ServiceException;

	Product getBySeUrl(MerchantStore store, String seUrl, Locale locale);

	/**
	 * Get a product by sku (code) field  and the language
	 * @param productCode
	 * @param language
	 * @return
	 */
	Product getByCode(String productCode, Language language);
	
	List<Product> getListByCriteria(Criteria criteria);
	
	List<ProductAvailability> getPrice(ProductCriteria criteria);
	
	Product getNextCriteria(Language language,
			ProductCriteria criteria);
	
	List<Object> getAllProductID();
	List<Object> getAllProductIDByStoreID(Long storeId);
	public List<Object[]> getServicePrice(String ids);

	/**
	 * 商品报表
	 * */
	public List<Product> queryByProductPoi(String beginTime,String endTime);
	
	/**
	 * 不不算分的保存
	 */
	void saveOrUpdateNoQulity(Product product) throws ServiceException;
	/**
	 * 根据产品添加时间段查询出产品Id
	 * */
	public List<BigInteger> queryProductById(Date...dateTemp);

	//品牌
	public Authorization queryByAuthorization(Long productId);
	
	//商家
	public List<Authorization> getAuthorizationbyMerchantId(List<Long> manIds,Long MerchantId);
}
	
