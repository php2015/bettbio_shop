package com.salesmanager.core.business.customer.dao;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.type.OrderedSetType;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLOps;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.customer.model.Gifts;
import com.salesmanager.core.business.customer.model.GiftsCriteria;
import com.salesmanager.core.business.customer.model.GiftsList;
import com.salesmanager.core.business.customer.model.QGifts;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("giftsDao")
public class GiftsDAOImpl extends SalesManagerEntityDaoImpl<Long, Gifts> implements GiftsDAO {

	@Override
	public GiftsList getByCriteria(GiftsCriteria giftsCriteria) {
		
		GiftsList glist=new GiftsList();
		QGifts qGifts=QGifts.gifts;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qGifts);
		BooleanBuilder pbuilder =null;
		
		if(!StringUtils.isBlank(giftsCriteria.getName())){
			if(pbuilder ==null) pbuilder= new BooleanBuilder();
			pbuilder.and(qGifts.name.eq(giftsCriteria.getName()));
		}
		if(giftsCriteria.getPoints()!=null){
			if(pbuilder ==null) pbuilder= new BooleanBuilder();
			pbuilder.and(qGifts.points.eq(giftsCriteria.getPoints()));
		}
		if(giftsCriteria.getPrice()!=null){
			if(pbuilder ==null) pbuilder= new BooleanBuilder();
			pbuilder.and(qGifts.price.eq(giftsCriteria.getPrice()));
		}
//		if(giftsCriteria.getType()!=null){
//			if(pbuilder ==null) pbuilder= new BooleanBuilder();
//			pbuilder.and(qGifts.type.eq(giftsCriteria.getType()));
//		}
//		
//		if(pbuilder !=null){
//			query.where(pbuilder);
//		}
//		//判断是限时商品
//		if(giftsCriteria.getType()!=null&&giftsCriteria.getType()==1L){
//			query.where(qGifts.deadline.gt(new Date()));
//		}
//		//结束时间必须大于当前时间或者等于空
//		query.orderBy(qGifts.total.desc());//根据下载量进行排序
//		
		if(giftsCriteria.getMaxCount()>0){
			query.limit(giftsCriteria.getMaxCount());
			query.offset(giftsCriteria.getStartIndex());
		}
		glist.setGifts(query.list(qGifts));
		glist.setTotalCount((int)query.count());
		return glist;
	}
}
