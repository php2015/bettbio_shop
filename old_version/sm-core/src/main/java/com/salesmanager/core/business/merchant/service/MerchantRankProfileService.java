package com.salesmanager.core.business.merchant.service;

import java.util.List;

import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantRankProfile;

public interface MerchantRankProfileService extends SalesManagerEntityService<Long, MerchantRankProfile> {
	
	public List<MerchantRankProfile> listRanks();
	
}
