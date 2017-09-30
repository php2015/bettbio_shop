package com.salesmanager.web.utils;

import java.util.List;

import com.salesmanager.core.business.brand.service.BrandBanner;
import com.salesmanager.core.business.brand.service.BrandService;

public class StaticBrandService implements BrandService {
	protected List<BrandBanner> fullBrandBannerList; 
	protected List<BrandBanner> popularBrandBannerList;
	public List<BrandBanner> getFullBrandBannerList() {
		return fullBrandBannerList;
	}
	public void setFullBrandBannerList(List<BrandBanner> fullBrandBannerList) {
		this.fullBrandBannerList = fullBrandBannerList;
	}
	public List<BrandBanner> getPopularBrandBannerList() {
		return popularBrandBannerList;
	}
	public void setPopularBrandBannerList(List<BrandBanner> popularBrandBannerList) {
		this.popularBrandBannerList = popularBrandBannerList;
	}


}
