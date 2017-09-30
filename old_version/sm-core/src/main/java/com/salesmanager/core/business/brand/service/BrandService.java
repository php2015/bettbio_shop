package com.salesmanager.core.business.brand.service;

import java.util.List;

public interface BrandService {
	List<BrandBanner> getFullBrandBannerList();
	List<BrandBanner> getPopularBrandBannerList();
}
