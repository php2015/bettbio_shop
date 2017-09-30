package com.salesmanager.core.business.merchant.dao;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantRankProfile;

public interface MerchantRankProfileDao extends SalesManagerEntityDao<Long, MerchantRankProfile> {

	MerchantRankProfile getById(Long profileId);

}
