package com.salesmanager.web.admin.controller.user.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.web.admin.entity.user.UserList;

@Service("userFacade")
public class UserFacadeImpl implements UserFacade{

	@Autowired
	UserService userService;
	
	@Override
	public UserList getByCriteria(Criteria criteria) throws Exception {
		// TODO Auto-generated method stub
		com.salesmanager.core.business.user.model.UserList ulist = userService.listByCriteria(criteria);
		if(ulist !=null && ulist.getTotalCount()>0){
			UserList users = new UserList();
			users.setTotalCount(ulist.getTotalCount());
			List<com.salesmanager.web.admin.entity.user.User> userlist = new ArrayList<com.salesmanager.web.admin.entity.user.User>();
			for(User u: ulist.getUsers()){
				com.salesmanager.web.admin.entity.user.User user = new com.salesmanager.web.admin.entity.user.User();
				user.setUserid(u.getId());
				user.setActive(u.isActive());
				user.setStoreName(u.getMerchantStore().getStorename());
				user.setUserEmail(u.getAdminEmail());
				user.setUserName(u.getAdminName());
				userlist.add(user);
			}
			users.setUsers(userlist);
			return users;
		}
		return null;
	}

	@Override
	public int queryByAccount(int index, String account) {
		
		return userService.queryByAccount(index, account);
	}

	@Override
	public User getUserByAccount(String account) {
		User user=null;
        user=userService.queryUserByEmail(account);
        if(user!=null){
        	return user;
        }else{
           user=userService.queryUserByPhone(account);
           return user;
        }
	}

}
