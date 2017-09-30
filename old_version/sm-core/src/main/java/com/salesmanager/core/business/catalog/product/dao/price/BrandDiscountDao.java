package com.salesmanager.core.business.catalog.product.dao.price;

import java.util.List;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.price.BrandDiscount;
import com.salesmanager.core.business.catalog.product.model.price.QBrandDiscount;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface BrandDiscountDao extends SalesManagerEntityDao<Long, BrandDiscount> {

	BrandDiscount getByBrandAndStoreId(long brandId, long storeId);

	void deleteByStoreId(long storeId);

	List<BrandDiscount> getListByStoreId(long storeId);

	int getListSizeByBrandAndStoreName(String brandName, String storeName);

	List<Long> getPageIdListByBrandAndStoreName(String brandName, String storeName, int startPos, int pageSize);

	void deleteAll();

	void cleanUpInvalidBrands();

	List<Object[]> getDiscountRateByProductIdList(List<Long> ids);
}
