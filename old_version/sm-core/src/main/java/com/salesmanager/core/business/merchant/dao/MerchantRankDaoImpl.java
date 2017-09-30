package com.salesmanager.core.business.merchant.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantRank;
import com.salesmanager.core.business.merchant.model.QMerchantRank;

@Repository("merchantRankDao")
public class MerchantRankDaoImpl extends SalesManagerEntityDaoImpl<Integer, MerchantRank>implements MerchantRankDao {

	@Override
	public List<MerchantRank> listRanks() {
		QMerchantRank qRank = QMerchantRank.merchantRank;

		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qRank).orderBy(qRank.id.asc());

		return query.distinct().list(qRank);
	}

}
