package com.salesmanager.core.business.user.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;
import com.salesmanager.core.business.user.model.QGroup;
import com.salesmanager.core.business.user.model.QUser;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.model.UserList;

@Repository("userDao")
public class UserDaoImpl extends SalesManagerEntityDaoImpl<Long, User>
		implements UserDao {

	@Override
	public User getByUserName(String userName) {

		QUser qUser = QUser.user;
		QGroup qGroup = QGroup.group;

		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qUser).innerJoin(qUser.groups, qGroup).fetch()
				.innerJoin(qUser.merchantStore).fetch()
				.leftJoin(qUser.defaultLanguage).fetch()
				.where(qUser.adminName.eq(userName));

		List<User> ms = query.list(qUser);
		if (ms != null && ms.size() > 0) {
			return ms.get(0);
		} else {
			return null;
		}

	}

	@Override
	public User getById(Long id) {

		QUser qUser = QUser.user;
		QGroup qGroup = QGroup.group;

		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qUser).innerJoin(qUser.groups, qGroup).fetch()
				.innerJoin(qUser.merchantStore).fetch()
				.leftJoin(qUser.defaultLanguage).fetch().where(qUser.id.eq(id));

		List<User> ms = query.list(qUser);
		if (ms != null && ms.size() > 0) {
			return ms.get(0);
		} else {
			return null;
		}

	}

	@Override
	public List<User> listUser() {
		QUser qUser = QUser.user;
		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qUser).innerJoin(qUser.groups).fetch()
				.innerJoin(qUser.merchantStore).fetch()
				.leftJoin(qUser.defaultLanguage).fetch()
				.orderBy(qUser.id.asc());

		return query.distinct().list(qUser);
		// listDistinct(qUser);
	}

	@Override
	public List<User> listUserByStore(MerchantStore store) {
		QUser qUser = QUser.user;
		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qUser).innerJoin(qUser.groups).fetch()
				.innerJoin(qUser.merchantStore).fetch()
				.leftJoin(qUser.defaultLanguage).fetch()
				.orderBy(qUser.id.asc())
				.where(qUser.merchantStore.id.eq(store.getId()));

		return query.distinct().list(qUser);
		// listDistinct(qUser);
	}

	@Override
	public UserList findByCriteria(Criteria criteria) {
		UserList users = new UserList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(u) from User as u ");
		countBuilderSelect.append("INNER JOIN u.merchantStore us");
		StringBuilder countBuilderWhere = new StringBuilder();
		// 不显示默认admin用户
		countBuilderWhere.append(" where u.id != 1");

		if (!StringUtils.isEmpty(criteria.getFindName())) {
			countBuilderWhere
					.append(" and (us.storename like:usn or u.adminName like:un or u.adminEmail like:ue )");
		}

		if (criteria.getAvaiable() == 2) {
			countBuilderWhere.append(" and u.active = true");
		} else if (criteria.getAvaiable() == 0) {
			countBuilderWhere.append(" and u.active = false");
		}

		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		if (!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter(
					"usn",
					new StringBuilder().append("%")
							.append(criteria.getFindName()).append("%")
							.toString());
			countQ.setParameter(
					"un",
					new StringBuilder().append("%")
							.append(criteria.getFindName()).append("%")
							.toString());
			countQ.setParameter(
					"ue",
					new StringBuilder().append("%")
							.append(criteria.getFindName()).append("%")
							.toString());
		}

		Number count = (Number) countQ.getSingleResult();

		users.setTotalCount(count.intValue());

		if (count.intValue() == 0)
			return users;

		QUser qUser = QUser.user;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery(getEntityManager());
		query.from(qUser).join(qUser.merchantStore, qMerchantStore).fetch()
				.orderBy(qUser.id.asc());

		BooleanBuilder pBuilder = new BooleanBuilder();

		pBuilder.andNot(qUser.id.eq((long) 1));
		if (!StringUtils.isEmpty(criteria.getFindName())) {
			pBuilder.andAnyOf(
					qMerchantStore.storename.like(new StringBuilder()
							.append("%").append(criteria.getFindName())
							.append("%").toString()),
					qUser.adminName.like(new StringBuilder().append("%")
							.append(criteria.getFindName()).append("%")
							.toString()),
					qUser.adminEmail.like(new StringBuilder().append("%")
							.append(criteria.getFindName()).append("%")
							.toString()));
		}
		if (criteria.getAvaiable() == 2) {
			pBuilder.and(qUser.active.eq(true));
		} else if (criteria.getAvaiable() == 0) {
			pBuilder.and(qUser.active.eq(false));
		}

		if (criteria.getMaxCount() > 0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}

		if (pBuilder != null) {
			query.where(pBuilder);
		}

		users.setUsers(query.list(qUser));
		return users;
	}

	@Override
	public List<User> getListByCriteria(Criteria criteria) {

		QUser qUser = QUser.user;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		JPQLQuery query = new JPAQuery(getEntityManager());
		query.from(qUser).join(qUser.merchantStore, qMerchantStore).fetch()
				.orderBy(qUser.id.asc());

		BooleanBuilder pBuilder = new BooleanBuilder();

		pBuilder.andNot(qUser.id.eq((long) 1));

		if (criteria.getMaxCount() > 0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}

		if (pBuilder != null) {
			query.where(pBuilder);
		}

		return query.list(qUser);
	}

	/**
	 * 验证用户名、商家是否已经存在了
	 * */
	public int queryByUser(String param, String value) {
		int flag = 0;
		try {
			StringBuffer countBuilderSelect = new StringBuffer();
			if (param.equals("adminName")) {
				countBuilderSelect
						.append("select count(u) from User as u where u.adminName='"
								+ value + "'");
			} else if (param.equals("storename")) {
				countBuilderSelect
						.append("select count(u) from User as u where u.merchantStore.storename='"
								+ value + "'");
			}
			Long count = (Long) getEntityManager().createQuery(
					countBuilderSelect.toString()).getSingleResult();
			if (count > 0) {
				flag = 1;
			} else {
				flag = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public int queryByAccount(int index,String account) {
		int email = 0;
		int phone = 0;
		int adminName=0;
		int storeName=0;
		StringBuffer countBuilderSelect = new StringBuffer();
		switch (index) {
		case 0:
			countBuilderSelect
			.append("select count(u) from User as u where u.adminEmail='"
					+ account + "'");
			Long count = (Long) getEntityManager().createQuery(
			countBuilderSelect.toString()).getSingleResult();
			if (count > 0) {
					email = 1;
				} else {
					email = 0;
			}
			return email;
		case 1:
			count = (Long) getEntityManager().createQuery(
					"select count(u) from User as u where u.merchantStore.storemobile='"
							+ account + "'").getSingleResult();
			if (count > 0) {
				phone = 1;
			} else {
				phone = 0;
			}
			return phone;
		case 2:
			count = (Long) getEntityManager().createQuery(
					"select count(u) from User as u where u.adminName='"
							+ account + "'").getSingleResult();
			if (count > 0) {
				adminName = 1;
			} else {
				adminName = 0;
			}
			return adminName;
		case 3:
			count = (Long) getEntityManager().createQuery(
					"select count(u) from User as u where u.merchantStore.storename='"
							+ account + "'").getSingleResult();
			if (count > 0) {
				storeName = 1;
			} else {
				storeName = 0;
			}
			return storeName;
		case 4:
			countBuilderSelect
			.append("select count(u) from User as u where u.adminEmail='"
					+ account + "'");
			 count = (Long) getEntityManager().createQuery(
			countBuilderSelect.toString()).getSingleResult();
			if (count > 0) {
					email = 1;
				} else {
					email = 0;
			}
			count = (Long) getEntityManager().createQuery(
					"select count(u) from User as u where u.merchantStore.storemobile='"
							+ account + "'").getSingleResult();
			if (count > 0) {
				phone = 1;
			} else {
				phone = 0;
			}
			if(email!=0||phone!=0){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public User queryUserByEmail(String meail) {
		StringBuffer countBuilderSelect = new StringBuffer();
		countBuilderSelect.append("from User as u where u.adminEmail='"+ meail + "'");
		User user=(User)getEntityManager().createQuery(countBuilderSelect.toString()).getSingleResult();
		return user;
	}

	@Override
	public User queryUserByPhone(String phone) {
		StringBuffer countBuilderSelect = new StringBuffer();
		countBuilderSelect.append("from User as u where u.merchantStore.storemobile='"+ phone + "'");
		User user=(User)getEntityManager().createQuery(countBuilderSelect.toString()).getSingleResult();
		return user;
	}

}
