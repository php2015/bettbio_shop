package com.bettbio.core.mongo.service;

import com.bettbio.core.model.vo.ShoppingCartVo;
import com.bettbio.core.mongo.model.ShoppingCart;
import com.bettbio.core.mongo.model.UserShoppingCartList;

public interface ShoppingCartService {
  /**
   * 添加购物车	
   * @return
   */
  public void insert(String userCode,ShoppingCartVo shoppingCartVo);
  /**
   * 删除购物车
   * @return
   */
  public void remove(String userCode,ShoppingCart shoppingCart);
  /**
   * 批量删除
   * @return
   */
  public void removes(String userCode,ShoppingCartVo shoppingCartVo);
  /**
   * 变更购物车
   * @return
   */
  public void changeShoppingCart(String userCode,ShoppingCart shoppingCart);
  /**
   * 获取购物车
   * @param userCode 用户编号
   * @return
   */
  public UserShoppingCartList findShoppingCarts(String userCode);
  
}
