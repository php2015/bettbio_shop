package com.salesmanager.core.business.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.service.UserLoginService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.dao.UserDao;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.model.UserList;
import com.salesmanager.core.constants.Constants;


public class UserServiceImpl extends SalesManagerEntityServiceImpl<Long, User>
		implements UserService {


	private UserDao userDao;
	
	@Autowired
	public UserServiceImpl(UserDao userDao) {
		super(userDao);
		this.userDao = userDao;

	}
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserLoginService userLoginService;
	
	@Override
	public User getByUserName(String userName) throws ServiceException {
		
		return userDao.getByUserName(userName);
		
	}
	
	@Override
	public User getByUserMobile(String mobile) throws ServiceException {
		
		return userDao.getByUserMobile(mobile);
		
	}
	
	@Override
	public void delete(User user) throws ServiceException {
		
		User u = this.getById(user.getId());
		//删除登录记录
		userLoginService.deleteByUserId(user.getId());
		super.delete(u);
		
	}

	@Override
	public List<User> listUser() throws ServiceException {
		try {
			return userDao.listUser();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<User> listByStore(MerchantStore store) throws ServiceException {
		try {
			return userDao.listUserByStore(store);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	
	@Override
	public void saveOrUpdate(User user) throws ServiceException {
		
		if(user.getId()==null || user.getId().longValue()==0) {
			userDao.save(user);
		} else {
			userDao.update(user);
		}
		
	}

	@Override
	public UserList listByCriteria(Criteria criteria)
			throws ServiceException {
		// TODO Auto-generated method stub
		return userDao.findByCriteria(criteria);
	}
	
	@Override
	public	List<User> getListByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		return userDao.getListByCriteria(criteria);
	}
	
	@Override
	/**
	 * 验证用户名、商家是否已经存在了
	 * */
	public int queryByUser(String param,String value){
		return userDao.queryByUser(param,value);
	}

	@Override
	public int queryByAccount(int index,String account) {
		return userDao.queryByAccount(index, account);
	}

	@Override
	public User queryUserByEmail(String meail) {
		// TODO Auto-generated method stub
		return userDao.queryUserByEmail(meail);
	}

	@Override
	public User queryUserByPhone(String phone) {
		// TODO Auto-generated method stub
		return userDao.queryUserByPhone(phone);
	}

	@Override
	public Map<String, User> getPossibleUsersByLogonName(String logonName) throws ServiceException {
		return userDao.getPossibleUsersByLogonName(logonName);
	}
	
	
}
