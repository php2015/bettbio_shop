/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bettbio.core.common.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public abstract class BaseService<T> implements IService<T> {

    @Autowired
    protected Mapper<T> mapper;

    protected final Class<T> entityClass;
    
    public BaseService(){
        this.entityClass = ReflectUtils.findParameterizedType(getClass(), 0);
    }
    
    public Mapper<T> getMapper() {
        return mapper;
    }

    @Override
    public T selectByKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    public int save(T entity) {
        return mapper.insert(entity);
    }

    public int delete(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

    public int updateAll(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    public int updateNotNull(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    public List<T> selectAll(){
    	return mapper.selectAll();
    }
    
    @Override
    public List<T> selectByPage(Object example, int page, int rows) {
    	 PageHelper.startPage(page, rows);
         return selectByExample(example);
    }
    //TODO 其他...
    
    @Override
    public Page<T> selectByPage(Map<String, Object> map) {
    	return null;
    }
}
