package com.salesmanager.core.business.catalog.product.service.price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.price.BrandDiscountDao;
import com.salesmanager.core.business.catalog.product.model.price.BrandDiscount;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("brandDiscountService")
public class BrandDiscountServiceImpl extends SalesManagerEntityServiceImpl<Long, BrandDiscount> 
	implements BrandDiscountService {

	private BrandDiscountDao brandDiscountDao;
	@Autowired
	public BrandDiscountServiceImpl(BrandDiscountDao brandDiscountDao) {
		super(brandDiscountDao);
		this.brandDiscountDao = brandDiscountDao;
	}
	@Override
	public void deleteByStoreId(long storeId) {
		brandDiscountDao.deleteByStoreId(storeId);
	}
	@Override
	public BrandDiscount getByBrandAndStoreId(long brandId, long storeId) {
		return brandDiscountDao.getByBrandAndStoreId(brandId, storeId);
	}
	@Override
	public List<BrandDiscount> getListByStoreId(long storeId) {
		return brandDiscountDao.getListByStoreId(storeId);
	}
	
	@Override
	public int getListSizeByBrandAndStoreName(String brandName, String storeName) {
		return brandDiscountDao.getListSizeByBrandAndStoreName(brandName, storeName);
	}
	@Override
	public List<Long> getPageIdListByBrandAndStoreName(String brandName, String storeName, int startPos, int pageSize) {
		return brandDiscountDao.getPageIdListByBrandAndStoreName(brandName, storeName, startPos, pageSize);
	}
	@Override
	public void deleteAll() {
		brandDiscountDao.deleteAll();
	}
	@Override
	public void cleanUpInvalidBrands() {
		brandDiscountDao.cleanUpInvalidBrands();
	}
	@Override
	public List<Object[]> getDiscountRateByProductIdList(List<Long> ids) {
		return brandDiscountDao.getDiscountRateByProductIdList(ids);
	}
	
	
	
}
