package com.bettbio.core.model.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bettbio.core.mongo.model.ShoppingCart;
import com.google.gson.Gson;

/**
 * 购物车业务类
 * 
 * @author chang
 *
 */
public class ShoppingCartVo {
	private List<ShoppingCart> carts;// 购物车集合
	private List<ShopCartRemarksVo> shopCartRemarksVos;// 备注集合

	public ShoppingCartVo(List<ShoppingCart> carts) {
		this.carts = carts;
	}

	public ShoppingCartVo(ShoppingCart shoppingCart) {
		carts = new ArrayList<ShoppingCart>();
		carts.add(shoppingCart);
	}

	public ShoppingCartVo() {

	}

	public List<ShoppingCart> getCarts() {
		return carts;
	}

	public void setCarts(List<ShoppingCart> carts) {
		this.carts = carts;
	}

	public List<ShopCartRemarksVo> getShopCartRemarksVos() {
		shopCartRemarksVos = new ArrayList<ShopCartRemarksVo>();
		return shopCartRemarksVos;
	}

	public void setShopCartRemarksVos(List<ShopCartRemarksVo> shopCartRemarksVos) {
		this.shopCartRemarksVos = shopCartRemarksVos;
	}

	/**
	 * 留言集合转Map
	 * 
	 * @return
	 */
	public Map<String, String> toMap() {
		Map<String, String> maps = new HashMap<>();
		if (shopCartRemarksVos != null) {
			for (ShopCartRemarksVo shopCartRemarksVo : shopCartRemarksVos) {
				maps.put(shopCartRemarksVo.getStoreCode(), shopCartRemarksVo.getRemarks());
			}
		}
		return maps;
	}

	public static void main(String[] args) {

		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setNumber(1);
		shoppingCart.setProductCode("0101010");
		shoppingCart.setUnitPrice(100.0);
		ShoppingCartVo shoppingCartVo = new ShoppingCartVo(shoppingCart);

		ShopCartRemarksVo shopCartRemarksVo = new ShopCartRemarksVo();
		shopCartRemarksVo.setStoreCode("s123");
		shoppingCartVo.getShopCartRemarksVos().add(shopCartRemarksVo);

		System.out.println(new Gson().toJson(shoppingCartVo));
	}
}
