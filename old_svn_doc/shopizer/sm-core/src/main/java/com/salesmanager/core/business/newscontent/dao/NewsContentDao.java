package com.salesmanager.core.business.newscontent.dao;

import java.util.List;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.newscontent.model.NewsContent;

public interface NewsContentDao extends SalesManagerEntityDao<Long, NewsContent> {

	public List<NewsContent> getListByCriteria(Criteria criteria);
}
