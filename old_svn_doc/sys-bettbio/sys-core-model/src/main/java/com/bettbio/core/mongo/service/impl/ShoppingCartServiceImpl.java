package com.bettbio.core.mongo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bettbio.core.model.vo.ShoppingCartVo;
import com.bettbio.core.mongo.dao.ShoppingCartDao;
import com.bettbio.core.mongo.model.ShoppingCart;
import com.bettbio.core.mongo.model.UserShoppingCartList;
import com.bettbio.core.mongo.service.ShoppingCartService;
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	String collectionName = "shoppingCart";
	@Autowired
	ShoppingCartDao shoppingCartDao;
	
	@Override
	public void insert(String userCode,ShoppingCartVo shoppingCartVo) {
		UserShoppingCartList userShoppingCartList=shoppingCartDao.selectOneByQuery(new Query(Criteria.where("userCode").is(userCode)), collectionName);
		userShoppingCartList=userShoppingCartList==null?new UserShoppingCartList(userCode):userShoppingCartList;
		if(shoppingCartVo.getCarts()!=null&&shoppingCartVo.getCarts().size()>=1){
			for (ShoppingCart shoppingCart : shoppingCartVo.getCarts()) {
				if(shoppingCart.getProductCode()!=null&&shoppingCart.getNumber()!=0&&shoppingCart.getProductSpec()!=null){
					userShoppingCartList.getShoppingCartList().add(shoppingCart);
				}
			}
		}
		shoppingCartDao.save(userShoppingCartList, collectionName);
	}
	
	@Override
	public void remove(String userCode,ShoppingCart shoppingCart) {
		UserShoppingCartList userShoppingCartList=shoppingCartDao.selectOneByQuery(new Query(Criteria.where("userCode").is(userCode)), collectionName);
		userShoppingCartList=userShoppingCartList==null?new UserShoppingCartList(userCode):userShoppingCartList;
		userShoppingCartList.getShoppingCartList().remove(shoppingCart);
		shoppingCartDao.update(userShoppingCartList, collectionName);
	}
	
	@Override
	public void removes(String userCode,ShoppingCartVo shoppingCartVo) {
		UserShoppingCartList userShoppingCartList=shoppingCartDao.selectOneByQuery(new Query(Criteria.where("userCode").is(userCode)), collectionName);
		userShoppingCartList=userShoppingCartList==null?new UserShoppingCartList(userCode):userShoppingCartList;
		userShoppingCartList.getShoppingCartList().removes(shoppingCartVo);
		shoppingCartDao.update(userShoppingCartList, collectionName);
	}
	
	@Override
	public void changeShoppingCart(String userCode,ShoppingCart shoppingCart) {
		UserShoppingCartList userShoppingCartList=shoppingCartDao.selectOneByQuery(new Query(Criteria.where("userCode").is(userCode)), collectionName);
		userShoppingCartList=userShoppingCartList==null?new UserShoppingCartList(userCode):userShoppingCartList;
		userShoppingCartList.getShoppingCartList().changeNumber(shoppingCart);
		shoppingCartDao.update(userShoppingCartList, collectionName);
	}

	@Override
	public UserShoppingCartList findShoppingCarts(String userCode) {
		UserShoppingCartList userShoppingCartList=shoppingCartDao.selectOneByQuery(new Query(Criteria.where("userCode").is(userCode)), collectionName);
		userShoppingCartList=userShoppingCartList==null?new UserShoppingCartList(userCode):userShoppingCartList;
		return userShoppingCartList;
	}
	
	
}
