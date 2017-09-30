package com.bettbio.core.mongo.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.bettbio.core.common.service.MongoBaseDaoImpl;
import com.bettbio.core.mongo.dao.ReagentProductDao;
import com.bettbio.core.mongo.model.ReagentProduct;
@Component
public class ReagentProductDaoImpl extends MongoBaseDaoImpl<ReagentProduct> implements ReagentProductDao{
	@Autowired
	MongoTemplate mongoTemplate;
	
}
