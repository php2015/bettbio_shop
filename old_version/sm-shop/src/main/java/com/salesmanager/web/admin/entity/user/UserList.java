package com.salesmanager.web.admin.entity.user;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class UserList extends EntityList{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2304905760871981417L;
	private List<User> users ;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
