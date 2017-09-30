package com.bettbio.core.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bettbio.core.common.utils.ConvertHelper;
import com.bettbio.core.common.utils.ReflectUtils;
import com.mongodb.DBObject;

public abstract class MongoBaseDaoImpl<T> implements MongoBaseDao<T>{
	@Autowired
	MongoTemplate mongoTemplate;
	Class<T> thisClass;
    public MongoBaseDaoImpl() {
		thisClass=ReflectUtils.findParameterizedType(getClass(), 0);
	}
	@Override
	public void remove(int id, String collectionName) {
//		mongoTemplate.remove(new Query(Criteria.where("id").is(id)),thisClass,collectionName);
//		Query query;
		mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), collectionName);
	}
	
	@Override
	public void removes(List<Integer> ids, String collectionName) {
		mongoTemplate.remove(new Query(Criteria.where("_id").in(ids)), collectionName);
	}

	@Override
	public void save(T entity, String collectionName) {
		mongoTemplate.save(entity, collectionName);
	}
	@Override
	public T selectOneByQuery(Query query, String collectionName) {
		return mongoTemplate.findOne(query, thisClass,collectionName);
	}
	@Override
	public Long selectCount(String collectionName) {
		return mongoTemplate.getDb().getCollection(collectionName).getCount();
	}
	
	@Override
	public List<T> selectByQuery(Query query, String collectionName) {
		return mongoTemplate.find(query, thisClass, collectionName);
	}
	
	@Override
	public Long selectCount(Query query, String collectionName) {
		//DBObject dbObject = ConvertHelper.convertNotNull(reagentProduct);
		return mongoTemplate.getDb().getCollection(collectionName).getCount();
	}
	public void update(T entity, String collectionName) {
		mongoTemplate.save(entity, collectionName);
	}
}
