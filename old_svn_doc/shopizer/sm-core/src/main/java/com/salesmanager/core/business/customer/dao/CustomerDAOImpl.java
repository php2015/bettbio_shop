package com.salesmanager.core.business.customer.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.customer.model.QCustomer;
import com.salesmanager.core.business.customer.model.QCustomerAddress;
import com.salesmanager.core.business.customer.model.QCustomerInvoice;
import com.salesmanager.core.business.customer.model.attribute.QCustomerAttribute;
import com.salesmanager.core.business.customer.model.attribute.QCustomerOption;
import com.salesmanager.core.business.customer.model.attribute.QCustomerOptionValue;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.QMerchantStore;

@Repository("customerDao")
public class CustomerDAOImpl extends SalesManagerEntityDaoImpl<Long, Customer> implements CustomerDAO {

	public CustomerDAOImpl() {
		super();
	}
	
	
	@Override
	public Customer getById(Long id){
		QCustomer qCustomer = QCustomer.customer;
		//QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		//QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		//QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			//.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			//.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			//.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			//.leftJoin(qCustomerOption.descriptions).fetch()
			//.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.where(qCustomer.id.eq(id));
		
		List<Customer> ms = query.list(qCustomer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	

	
	@Override
	public CustomerList listByCriteria(CustomerCriteria criteria) {
		
		
		
		CustomerList customerList = new CustomerList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(c) from Customer as c");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		if (criteria.getStoreId()!=null&&criteria.getStoreId()!=-1) {
			countBuilderWhere.append(" and c.merchantStore.id=:mId");
		}

		if(!StringUtils.isBlank(criteria.getNick())) {
			countBuilderWhere.append(" and c.nick like:nm");
		}
		
		if(!StringUtils.isBlank(criteria.getCompnay())) {
			countBuilderWhere.append(" and c.compnay like:fn");
		}
		
		if(!StringUtils.isBlank(criteria.getEmailAddress())) {
			countBuilderWhere.append(" and c.emailAddress like:ln");
		}
		
		if(!StringUtils.isBlank(criteria.getGrade())) {
			countBuilderWhere.append(" and c.grade =:gn");
		}
		
		if(!StringUtils.isBlank(criteria.getPhone())) {
			countBuilderWhere.append(" and c.phone like:pn");
		}

		if(!StringUtils.isBlank(criteria.getProject())) {
			countBuilderWhere.append(" and c.project like:prn");
		}
		
		if(!StringUtils.isBlank(criteria.getAnonymous())) {
			countBuilderWhere.append(" and c.anonymous like:an");
		}
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + this.addWhere(countBuilderWhere));

		if (criteria.getStoreId()!=null&&criteria.getStoreId()!=-1) {
			countQ.setParameter("mId", criteria.getStoreId());
		}

		if(!StringUtils.isBlank(criteria.getNick())) {
			countQ.setParameter("nm",new StringBuilder().append("%").append(criteria.getNick()).append("%").toString());
		}
		
		if(!StringUtils.isBlank(criteria.getCompnay())) {
			countQ.setParameter("fn",new StringBuilder().append("%").append(criteria.getCompnay()).append("%").toString());
		}
		
		if(!StringUtils.isBlank(criteria.getEmailAddress())) {
			countQ.setParameter("ln",new StringBuilder().append("%").append(criteria.getEmailAddress()).append("%").toString());
		}
		
		if(!StringUtils.isBlank(criteria.getGrade())) {
			countQ.setParameter("gn", Integer.parseInt(criteria.getGrade()));
		}
		
		if(!StringUtils.isBlank(criteria.getPhone())) {
			countQ.setParameter("pn",new StringBuilder().append("%").append(criteria.getPhone()).append("%").toString());
		}
		
		if(!StringUtils.isBlank(criteria.getProject())) {
			countQ.setParameter("prn",new StringBuilder().append("%").append(criteria.getProject()).append("%").toString());
		}
		
		if(!StringUtils.isBlank(criteria.getAnonymous())) {
			countQ.setParameter("an", Integer.parseInt(criteria.getAnonymous()));
		}

		Number count = (Number) countQ.getSingleResult ();

		customerList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return customerList;
		
		
		
		QCustomer qCustomer = QCustomer.customer;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch();

			//query.where(qCustomer.merchantStore.id.eq(store.getId()));
			BooleanBuilder pBuilder = null;
			if (criteria.getStoreId()!=-1) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.merchantStore.id.eq(criteria.getStoreId()));
			}
			if(!StringUtils.isBlank(criteria.getNick())) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.nick.like(new StringBuilder().append("%").append(criteria.getNick()).append("%").toString()));				
			}
			
			if(!StringUtils.isBlank(criteria.getCompnay())) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.compnay.like(new StringBuilder().append("%").append(criteria.getCompnay()).append("%").toString()));
			}
			
			if(!StringUtils.isBlank(criteria.getEmailAddress())) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.emailAddress.like(new StringBuilder().append("%").append(criteria.getEmailAddress()).append("%").toString()));
			}
			
			if(!StringUtils.isBlank(criteria.getGrade())) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.grade.eq(Integer.parseInt(criteria.getGrade())));
			}
			
			if(!StringUtils.isBlank(criteria.getPhone())) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.phone.like(new StringBuilder().append("%").append(criteria.getPhone()).append("%").toString()));
			}
			
			if(!StringUtils.isBlank(criteria.getProject())) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.project.like(new StringBuilder().append("%").append(criteria.getProject()).append("%").toString()));
			}
			
			if(!StringUtils.isBlank(criteria.getAnonymous())) {
				if(pBuilder==null) {
					pBuilder = new BooleanBuilder();
				}
				pBuilder.and(qCustomer.anonymous.eq(Integer.parseInt(criteria.getAnonymous())));
			}
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		

		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		
		customerList.setCustomers(query.list(qCustomer));

		return customerList;
		
	}
	

	@Override
	public Customer getByNick(String nick){
		QCustomer qCustomer = QCustomer.customer;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.groups).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.where(qCustomer.nick.eq(nick));
		
		List<Customer> ms = query.list(qCustomer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
	}
	
	@Override
	public Customer getByNick(String nick, Long storeId){
		QCustomer qCustomer = QCustomer.customer;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerAddress qCustomerAddress = QCustomerAddress.customerAddress;
		QCustomerInvoice qCustomerInvoice = QCustomerInvoice.customerInvoice;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		try {
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore, qMerchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.groups).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.leftJoin(qCustomer.addresss,qCustomerAddress).fetch()
			.leftJoin(qCustomer.invoices,qCustomerInvoice).fetch()
			.where(qCustomer.nick.eq(nick).and(qMerchantStore.id.eq(storeId)));
		
		List<Customer> ms = query.list(qCustomer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
		} catch(NoResultException nre) {
			return null;
		}
	}
	
	
	@Override
	public Customer getByEmail(String email) {
		// TODO Auto-generated method stub
		QCustomer qCustomer = QCustomer.customer;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		QCustomerAddress qCustomerAddress = QCustomerAddress.customerAddress;
		QCustomerInvoice qCustomerInvoice = QCustomerInvoice.customerInvoice;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.groups).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.leftJoin(qCustomer.addresss,qCustomerAddress).fetch()
			.leftJoin(qCustomer.invoices,qCustomerInvoice).fetch()
			.where(qCustomer.emailAddress.eq(email));
		
		List<Customer> ms = query.list(qCustomer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
	}

	@Override
	public Customer getByEmailForRegister(String email) {
		// TODO Auto-generated method stub
		QCustomer qCustomer = QCustomer.customer;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.where(qCustomer.emailAddress.eq(email));
		List<Customer> ms = query.list(qCustomer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
	}

	@Override
	public Customer getByPhone(String phone) {
		// TODO Auto-generated method stub
		QCustomer qCustomer = QCustomer.customer;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		QCustomerAddress qCustomerAddress = QCustomerAddress.customerAddress;
		QCustomerInvoice qCustomerInvoice = QCustomerInvoice.customerInvoice;
		

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.groups).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.leftJoin(qCustomer.addresss,qCustomerAddress).fetch()
			.leftJoin(qCustomer.invoices,qCustomerInvoice).fetch()
			.where(qCustomer.phone.eq(phone));
		
		List<Customer> ms = query.list(qCustomer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
	}
	
	@Override
	public Customer getByPhoneForRegister(String phone) {
		// TODO Auto-generated method stub
		QCustomer qCustomer = QCustomer.customer;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.where(qCustomer.phone.eq(phone));
		
		List<Customer> ms = query.list(qCustomer);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
	}
	
	/***
	 * 鏍规嵁鎵嬫満鍙烽獙璇佺敤鎴锋槸鍚﹀瓨鍦�
	 * @param phone
	 * @return
	 */
	public  boolean getCustomerExistsPhone(String phone){
		QCustomer qCustomer = QCustomer.customer;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qCustomer).where(qCustomer.phone.eq(phone));
		
		Customer customer=query.uniqueResult(qCustomer);
		if(customer!=null){
			return false;
		}
		return true;
	}


	@Override
	public Customer getByID(String ID) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				QCustomer qCustomer = QCustomer.customer;
				QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
				QCustomerOption qCustomerOption = QCustomerOption.customerOption;
				QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
				QCustomerAddress qCustomerAddress = QCustomerAddress.customerAddress;
				QCustomerInvoice qCustomerInvoice = QCustomerInvoice.customerInvoice;
				
				JPQLQuery query = new JPAQuery (getEntityManager());
				
				query.from(qCustomer)
					.join(qCustomer.merchantStore).fetch()
					.leftJoin(qCustomer.defaultLanguage).fetch()
					.leftJoin(qCustomer.groups).fetch()
					.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
					.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
					.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
					.leftJoin(qCustomerOption.descriptions).fetch()
					.leftJoin(qCustomerOptionValue.descriptions).fetch()
					.leftJoin(qCustomer.addresss,qCustomerAddress).fetch()
					.leftJoin(qCustomer.invoices,qCustomerInvoice).fetch()
					.where(qCustomer.id.eq(Long.parseLong(ID)));
				
				List<Customer> ms = query.list(qCustomer);
				if(ms !=null && ms.size()>0){
					return ms.get(0);
				}else  {
					return null;
				}
	}
	private String addWhere(StringBuilder strb){
		String tmp = strb.toString().trim();
		if(tmp.length()>0 && tmp.indexOf("where ")!=0) {
			int index = tmp.indexOf("and ");
			if(index == 0) return " where " + tmp.substring(4);
			else return " where " + tmp;
		} else return strb.toString();
	}
}
