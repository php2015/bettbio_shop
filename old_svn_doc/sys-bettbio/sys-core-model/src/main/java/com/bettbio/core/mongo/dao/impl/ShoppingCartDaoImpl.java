package com.bettbio.core.mongo.dao.impl;

import org.springframework.stereotype.Component;

import com.bettbio.core.common.service.MongoBaseDaoImpl;
import com.bettbio.core.mongo.dao.ShoppingCartDao;
import com.bettbio.core.mongo.model.UserShoppingCartList;
@Component
public class ShoppingCartDaoImpl extends MongoBaseDaoImpl<UserShoppingCartList> implements ShoppingCartDao {

}
