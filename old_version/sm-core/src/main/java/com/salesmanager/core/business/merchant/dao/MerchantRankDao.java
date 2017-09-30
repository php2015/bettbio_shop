package com.salesmanager.core.business.merchant.dao;

import java.util.List;
import java.util.Set;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantRank;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;

public interface MerchantRankDao extends SalesManagerEntityDao<Integer, MerchantRank> {
	List<MerchantRank> listRanks();
}
