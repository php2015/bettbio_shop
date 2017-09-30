package com.salesmanager.core.business.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.common.model.DeliveryCompny;
import com.salesmanager.core.business.common.model.QDeliveryCompny;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("deliveryCompnyDao")
public class DeliveryCompnyDaoImp extends SalesManagerEntityDaoImpl<Long, DeliveryCompny>
implements DeliveryCompnyDao{
	@Override
	public List<Object[]> getDeliveryName() {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select DELIVERYCOMPNY_ID as id,DELIVERY_NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(DELIVERY_NAME USING gbk),1)),16,10), ");
		qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
		qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
		qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(DELIVERY_NAME,1)) as pinyin  from DELIVERYCOMPNY order by pinyin,name");
		
		Query q  =super.getEntityManager().createNativeQuery(qs.toString());

    	//String hql = "select id,storename,coalesce(CONV(HEX(SUBSTRING(CONVERT(STORE_NAME,'gbk'),1,1)),16,10),SUBSTRING(STORE_NAME,1,1)) from MerchantStore";
	//	Query q = super.getEntityManager().createQuery(hql);
    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
	}

	@Override
	public List<DeliveryCompny> getDeliveryByName(String name) {
		// TODO Auto-generated method stub
		QDeliveryCompny qDeliveryCompny = QDeliveryCompny.deliveryCompny;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDeliveryCompny)		
		.orderBy(qDeliveryCompny.id.asc());
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		pBuilder.and(qDeliveryCompny.deliveryName.equalsIgnoreCase(name));
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		
		return query.list(qDeliveryCompny);		
	}
}
