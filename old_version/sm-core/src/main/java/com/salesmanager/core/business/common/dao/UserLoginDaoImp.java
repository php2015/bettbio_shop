package com.salesmanager.core.business.common.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.common.model.UserLogin;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("userLoginDao")
public class UserLoginDaoImp extends SalesManagerEntityDaoImpl<Long, UserLogin>
implements UserLoginDao{

	@Override
	public void deleteByUserId(Long id) {
		// TODO Auto-generated method stub
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("delete from UserLogin as u ");		
		StringBuilder countBuilderWhere = new StringBuilder();
	
		countBuilderWhere.append(" where u.user.id = ").append(id);
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		countQ.executeUpdate();

	}
	
}
