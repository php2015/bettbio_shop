package com.bettbio.core.mongo.dao.impl;


import org.springframework.stereotype.Component;

import com.bettbio.core.common.service.MongoBaseDaoImpl;
import com.bettbio.core.mongo.dao.ProductDao;
import com.bettbio.core.mongo.model.Product;


@Component
public class ProductDaoImpl extends MongoBaseDaoImpl<Product> implements ProductDao {

}
