package com.bettbio.core.common.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

public interface MongoBaseDao<T> {
	/**
	 * 更新
	 * @param entity
	 * @param collectionName
	 */
	void update(T entity,String collectionName);
	/**
	 * 删除
	 * @param id
	 * @param collectionName
	 */
	void remove(int id,String collectionName);
	/**
	 * 批量删除
	 * @param id
	 * @param collectionName
	 */
	void removes(List<Integer> ids,String collectionName);
	/**
	 * 添加
	 * @param entity
	 * @param collectionName
	 */
	void save(T entity,String collectionName);
	/**
	 * 查询单个实体
	 * @param code
	 * @param collectionName
	 * @return
	 */
    T selectOneByQuery(Query query,String collectionName);
    /**
     * 查询总数
     * @param query
     * @param collectionName 集合名称
     * @return
     */
    Long selectCount(String collectionName);
    
    /**
     * 根据条件查询总数
     * @param query
     * @param collectionName
     * @return
     */
    Long selectCount(Query query,String collectionName);
    /**
     * 
     * 查询集合
     * @param query
     * @param collectionName
     * @return
     */
    List<T> selectByQuery(Query query,String collectionName);
}
