package com.salesmanager.core.business.user.service;

import java.util.HashMap;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.model.UserList;

public class UserServiceLDAPImpl implements UserService {

	@Override
	public void save(User entity) throws ServiceException {
		throw new ServiceException("Not implemented");

	}

	@Override
	public void update(User entity) throws ServiceException {
		throw new ServiceException("Not implemented");

	}

	@Override
	public void create(User entity) throws ServiceException {
		throw new ServiceException("Not implemented");

	}

	@Override
	public void delete(User entity) throws ServiceException {
		throw new ServiceException("Not implemented");

	}

	@Override
	public User refresh(User entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> list() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Long count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public User getByUserName(String userName) throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	@Override
	public List<User> listUser() throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	@Override
	public void saveOrUpdate(User user) throws ServiceException {
		throw new ServiceException("Not implemented");

	}

	@Override
	public List<User> listByStore(MerchantStore store)
			throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	@Override
	public User getEntity(Class<? extends User> clazz, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByProperties(String[] proNames, Object[] proValues,
			HashMap<String, String> orderby) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList listByCriteria(Criteria criteria)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getListByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> List<User> listByField(
			SingularAttribute<? super User, V> fieldName, V fieldValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void deleteByIds(List<T> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByProperties(String[] proNames, Object[] proValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> findByPropertiesHQL(String[] proNames,
			Object[] proValues, HashMap<String, String> orderby) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int queryByUser(String param,String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryByAccount(int index, String account) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User queryUserByEmail(String meail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User queryUserByPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}



}
