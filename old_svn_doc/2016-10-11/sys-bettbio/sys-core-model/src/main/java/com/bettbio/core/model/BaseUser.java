package com.bettbio.core.model;

public class BaseUser {

	private int userType; //0-买家 1-商家 2-系统内部

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
