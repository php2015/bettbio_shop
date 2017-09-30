package com.salesmanager.core.business.merchant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.dao.MerchantRankProfileDao;
import com.salesmanager.core.business.merchant.model.MerchantRankProfile;

@Service("merchantRankProfileService")
public class MerchantRankProfileServiceImpl extends SalesManagerEntityServiceImpl<Long, MerchantRankProfile> 
		implements MerchantRankProfileService {


	@Autowired
	private MerchantRankProfileDao merchantRankProfileDao;
	
	@Autowired
	public MerchantRankProfileServiceImpl(MerchantRankProfileDao merchantRankProfileDao) {
		super(merchantRankProfileDao);
		this.merchantRankProfileDao = merchantRankProfileDao;
	}

	
	@Override
	public List<MerchantRankProfile> listRanks() {
		return this.merchantRankProfileDao.list();
	}

}
