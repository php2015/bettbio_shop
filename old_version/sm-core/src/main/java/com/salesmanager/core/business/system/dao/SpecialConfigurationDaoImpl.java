package com.salesmanager.core.business.system.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.system.model.QSpecialConfiguration;
import com.salesmanager.core.business.system.model.SpecialConfiguration;

@Repository("specialConfigurationDao")
public class SpecialConfigurationDaoImpl extends SalesManagerEntityDaoImpl<Long, SpecialConfiguration>
		implements SpecialConfigurationDao {

	@Override
	public SpecialConfiguration getByKey(String key) {
		QSpecialConfiguration qCfg = QSpecialConfiguration.specialConfiguration;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qCfg).where(qCfg.key.eq(key));
		
		SpecialConfiguration result = query.singleResult(qCfg);
		return result;
	}



}
