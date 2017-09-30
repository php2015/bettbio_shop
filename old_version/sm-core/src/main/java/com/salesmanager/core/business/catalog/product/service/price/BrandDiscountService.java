package com.salesmanager.core.business.catalog.product.service.price;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.price.BrandDiscount;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface BrandDiscountService extends SalesManagerEntityService<Long, BrandDiscount> {
	public void deleteByStoreId(long storeId);
	
	public BrandDiscount getByBrandAndStoreId(long brandId, long storeId);
	
	public List<BrandDiscount> getListByStoreId(long storeId);

	public int getListSizeByBrandAndStoreName(String brandName, String storeName);

	public List<Long> getPageIdListByBrandAndStoreName(String brandName, String storeName, int startPos, int pageSize);

	public void deleteAll();

	public void cleanUpInvalidBrands();

	public List<Object[]> getDiscountRateByProductIdList(List<Long> ids);
}
