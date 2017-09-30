package com.salesmanager.core.business.merchant.service;

import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantRank;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.language.model.Language;

public interface MerchantRankService extends SalesManagerEntityService<Integer, MerchantRank> {
	
	public List<MerchantRank> listRanks();
	
}
