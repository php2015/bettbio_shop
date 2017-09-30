package com.salesmanager.core.business.newscontent.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.newscontent.model.NewsContent;
import com.salesmanager.core.business.newscontent.model.QNewsContent;

@Repository("newsContentDao")
public class NewsContentDaoImpl extends SalesManagerEntityDaoImpl<Long, NewsContent>
		implements NewsContentDao {

	public List<NewsContent> getListByCriteria(Criteria criteria) {
		QNewsContent qNewsContent = QNewsContent.newsContent;
		JPQLQuery query = new JPAQuery(getEntityManager());
		query.from(qNewsContent).orderBy(qNewsContent.publishdt.desc()).orderBy(qNewsContent.linkText.asc());

		BooleanBuilder pBuilder = new BooleanBuilder();

		pBuilder.and(qNewsContent.ispublish.isTrue());

		if (pBuilder != null) {
			query.where(pBuilder);
		}

		long total = query.count();
		criteria.setTotalCount(total);
		
		if (criteria.getMaxCount() > 0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}

		return query.list(qNewsContent);
	}
}
