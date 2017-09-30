package com.salesmanager.core.business.user.model;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class UserList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2947004387394672160L;
	private List<User> users;
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

}
