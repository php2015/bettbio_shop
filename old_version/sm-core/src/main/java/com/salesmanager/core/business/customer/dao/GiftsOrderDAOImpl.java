package com.salesmanager.core.business.customer.dao;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;
import com.salesmanager.core.business.customer.model.*;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
@Repository("giftOderDao")
public class GiftsOrderDAOImpl extends SalesManagerEntityDaoImpl<Long, GiftOrder> implements GiftsOrderDAO {

	@Override
	 public GiftOrdersList getByCriteria(GftsOrderCriteria gftsOrderCriteria)
    {
        GiftOrdersList giftOrdersList = new GiftOrdersList();
        QGiftOrder qGiftOrder = QGiftOrder.giftOrder;
        JPQLQuery query = new JPAQuery(getEntityManager());
        ((JPQLQuery)((JPQLQuery)query.from(new EntityPath[] {
            qGiftOrder
        })).leftJoin(qGiftOrder.customer)).fetch();
        BooleanBuilder pbuilder = null;
        if(!StringUtils.isBlank(gftsOrderCriteria.getName()))
        {
            if(pbuilder == null)
                pbuilder = new BooleanBuilder();
            pbuilder.and(qGiftOrder.gifName.like((new StringBuilder("%")).append(gftsOrderCriteria.getName()).append("%").toString()));
        }
        if(gftsOrderCriteria.getCustomerId() != null)
        {
            if(pbuilder == null)
                pbuilder = new BooleanBuilder();
            pbuilder.and(qGiftOrder.customer.id.eq(gftsOrderCriteria.getCustomerId()));
        }
        if(gftsOrderCriteria.getStatus() != null)
        {
            if(pbuilder == null)
                pbuilder = new BooleanBuilder();
            pbuilder.and(qGiftOrder.status.eq(gftsOrderCriteria.getStatus()));
        }
        if(pbuilder != null)
            query.where(new Predicate[] {
                pbuilder
            });
        if(gftsOrderCriteria.getMaxCount() > 0)
        {
            query.limit(gftsOrderCriteria.getMaxCount());
            query.offset(gftsOrderCriteria.getStartIndex());
        }
        try
        {
            giftOrdersList.setGiftsOrder(query.list(qGiftOrder) != null ? query.list(qGiftOrder) : null);
            giftOrdersList.setTotalCount((int)query.count());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return giftOrdersList;
    }


}
