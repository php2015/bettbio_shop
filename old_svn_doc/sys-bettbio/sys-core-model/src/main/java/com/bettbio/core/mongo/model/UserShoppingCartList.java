package com.bettbio.core.mongo.model;

/**
 * 用戶购物车集合
 * 
 * @author chang
 *
 */
public class UserShoppingCartList {
	private String _id;
	private String userCode;
	private ShoppingCartList shoppingCartList=new ShoppingCartList();
	
	public UserShoppingCartList() {
	}
	public UserShoppingCartList(String userCode){
		this.userCode=userCode;
	}
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public ShoppingCartList getShoppingCartList() {
		return shoppingCartList;
	}

	public void setShoppingCartList(ShoppingCartList shoppingCartList) {
		this.shoppingCartList = shoppingCartList;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}

}
