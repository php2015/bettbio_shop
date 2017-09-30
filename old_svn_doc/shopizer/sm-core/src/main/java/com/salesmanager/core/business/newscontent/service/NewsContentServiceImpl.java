package com.salesmanager.core.business.newscontent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.newscontent.dao.NewsContentDao;
import com.salesmanager.core.business.newscontent.model.NewsContent;

@Service("newsContentService")
public class NewsContentServiceImpl extends SalesManagerEntityServiceImpl<Long, NewsContent> 
	implements NewsContentService {

	private NewsContentDao newsContentDao;
	
	@Autowired
	public NewsContentServiceImpl(NewsContentDao newContentDao) {
		super(newContentDao);
		this.newsContentDao = newContentDao;
	}

	public List<NewsContent> getListByCriteria(Criteria criteria) {
		return newsContentDao.getListByCriteria(criteria);
	}
	

}
