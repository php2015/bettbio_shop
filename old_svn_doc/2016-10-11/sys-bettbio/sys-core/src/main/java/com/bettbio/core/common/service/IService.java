package com.bettbio.core.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;

/**
 * 通用接口
 */
@Service
public interface IService<T> {

    T selectByKey(Object key);

    int save(T entity);

    int delete(Object key);

    int updateAll(T entity);

    int updateNotNull(T entity);

    List<T> selectAll();
    
    List<T> selectByExample(Object example);

    List<T> selectByPage(Object example,int page,int rows);
    //TODO 其他...
    Page<T> selectByPage(Map<String, Object> map);
}