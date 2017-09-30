package com.bettbio.core.mongo.dao.impl;

import org.springframework.stereotype.Component;

import com.bettbio.core.common.service.MongoBaseDaoImpl;
import com.bettbio.core.mongo.dao.ServiceProductDao;
import com.bettbio.core.mongo.model.ServiceProduct;
@Component
public class ServiceProductDaoImpl extends MongoBaseDaoImpl<ServiceProduct> implements ServiceProductDao {

}
