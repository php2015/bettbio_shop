package com.salesmanager.core.business.catalog.product.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductDao extends SalesManagerEntityDao<Long, Product> {
	
	Product getProductForLocale(long productId, Language language, Locale locale);

	@SuppressWarnings("rawtypes")
	List<Product> getProductsForLocale(MerchantStore store, Set categoryIds, Language language,
			Locale locale);

	@SuppressWarnings("rawtypes")
	List<Product> getProductsListByCategories(Set categoryIds);
	
	Product getBySeUrlId(MerchantStore store,String seUrl, Locale locale);

	/**
	 * Method to be used for getting a list of products in a given language based on one to many criteria
	 * @param store
	 * @param language
	 * @param criteria
	 * @return
	 */
	ProductList listByStore(MerchantStore store, Language language, ProductCriteria criteria);
	ProductList listByCriteria(Language language, ProductCriteria criteria);
	
	ProductList listPsByCriteria(Language language,ProductCriteria criteria);

	List<Product> listByStore(MerchantStore store);
	

	List<Product> getProductsListByCategories(Set<Long> categoryIds,
			Language language);

	Product getBySeUrl(MerchantStore store, String seUrl, Locale locale);

	Product getByCode(String productCode, Language language);
	
	List<Product> getListByCriteria(Criteria criteria);
	
	List<ProductAvailability> getPrice(ProductCriteria criteria);
	
	Product getNextCriteria(Language language, ProductCriteria criteria);	

	List<Object> getAllProductID();
	
	List<Object> getAllProductIDByStoreID(Long storeId);
	
	
	List<Object[]> getServicePrice(String ids);
	/**
	 * 商品报表
	 * */
	public List<Product> queryByProductPoi(String beginTime,String endTime);
	/**
	 * 根据产品添加时间段查询出产品Id
	 * */
	public List<BigInteger> queryProductById(Date...dateTemp);

	List<Product> getProductsListByMan(Set ids,Long merchantId);
	
	//品牌
    public Authorization queryByAuthorization(Long productId);
    
    //商家
  	public List<Authorization> queryAuthorizationbyMerchantId(Set ids,Long merchantId);
}
