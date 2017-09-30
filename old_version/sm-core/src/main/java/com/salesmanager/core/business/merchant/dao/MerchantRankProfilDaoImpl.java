package com.salesmanager.core.business.merchant.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantRankProfile;
import com.salesmanager.core.business.merchant.model.QMerchantRankProfile;

@Repository("merchantRankProfileDao")
public class MerchantRankProfilDaoImpl extends SalesManagerEntityDaoImpl<Long, MerchantRankProfile> implements
		MerchantRankProfileDao {

	@Override
	public MerchantRankProfile getById(Long profileId){
		QMerchantRankProfile qProfile = QMerchantRankProfile.merchantRankProfile;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProfile)
			.where(qProfile.id.eq(profileId));
		
		List<MerchantRankProfile> ms = query.list(qProfile);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
	}


}
