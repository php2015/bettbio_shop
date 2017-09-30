package com.salesmanager.core.business.order.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.review.QProductReview;
import com.salesmanager.core.business.common.model.CriteriaOrderBy;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.QOrder;
import com.salesmanager.core.business.order.model.QSubOrder;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProductAttribute;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.QOrderStatusHistory;

@Repository("orderDao")
public class OrderDaoImpl  extends SalesManagerEntityDaoImpl<Long, Order> implements OrderDao {

	public OrderDaoImpl() {
		super();
	}
	
	@Override
	public Order getById(Long id) {
		
		
		QOrder qOrder = QOrder.order;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		//QOrderTotal qOrderTotal = QOrderTotal.orderTotal;
		QOrderStatusHistory qOrderStatusHistory = QOrderStatusHistory.orderStatusHistory;
		QOrderProductAttribute qOrderProductAttribute = QOrderProductAttribute.orderProductAttribute;
		QSubOrder qSubOrder = QSubOrder.subOrder;
		//OrderAccount not loaded for now
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qOrder)
			//.join(qOrder.orderProducts, qOrderProduct).fetch()
			.join(qOrder.subOrders, qSubOrder).fetch()
			.join(qSubOrder.orderProducts, qOrderProduct).fetch()
			//.join(qOrder.orderTotal, qOrderTotal).fetch()
			.leftJoin(qOrder.orderHistory, qOrderStatusHistory).fetch()
			//.leftJoin(qOrderProduct.downloads).fetch()
			.leftJoin(qOrderProduct.orderAttributes,qOrderProductAttribute).fetch()
			//.leftJoin(qOrderProduct.prices).fetch()
			.where(qOrder.id.eq(id));
		
		
		List<Order> ms = query.list(qOrder);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}		
				
	}
	
	/**
	 * add by cy 
	 */
	@Override	 
	public long getTotal(MerchantStore store){
		//add select
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(o) from Order as o ,SubOrder as s");
		StringBuilder countBuilderWhere = new StringBuilder();
		
		countBuilderWhere.append(" where s.merchant.id=:mId");
		countBuilderWhere.append(" and s.order.id = o.id and s.merchant.id =:mId");
		
		//add where
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		countQ.setParameter("mId", store.getId());
		
		Number count = (Number) countQ.getSingleResult ();
		return count.longValue();
	}
	
	/**
	 * add by cy 
	 */
	@Override	 
	public long getTotal(Customer customer){
		//add select
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(o) from Order as o");
		StringBuilder countBuilderWhere = new StringBuilder();
		countBuilderWhere.append(" where o.customerId=:mId");
		
		//add where
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		countQ.setParameter("mId", customer.getId());
		
		Number count = (Number) countQ.getSingleResult ();
		if(count.intValue() ==0 ){
			return 0;
		}else{
			return count.longValue();
		}
	}
	
	/**
	 * add by cy 
	 * 
	 * 
	 */
	@Override	 
	public long getTotalMoney(OrderCriteria criteria){
		//add select
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select sum(o.total) from Order as o");
		StringBuilder countBuilderWhere = new StringBuilder();
		if(criteria.getcId() != -1l ){
			countBuilderWhere.append(" where o.customerId=:cId");
		}
		if(criteria.getStoreId() != -1l ){
			countBuilderSelect.append(" ,SubOrder as s");
			countBuilderWhere.append(" and s.order.id = o.id and s.merchant.id =:mId");
		}
		//add where
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + addWhere(countBuilderWhere));
		if(criteria.getcId() != -1l ){
			countQ.setParameter("cId", criteria.getcId());
		}
		if(criteria.getStoreId() != -1l ){
			countQ.setParameter("mId", criteria.getStoreId());
		}
		Number count =0;
		
		try{
			count = (Number) countQ.getSingleResult ();
		}catch (Exception e){
			count=0;
		}
		if(count == null){
			return 0;
		}
		
		return count.longValue();
	}
	

	@Override
	public OrderList listByOrderCriteria(OrderCriteria criteria) {
		Date dt = new Date();
		OrderList orderList = new OrderList();
			
		
		QOrder qOrder = QOrder.order;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		//QOrderTotal qOrderTotal = QOrderTotal.orderTotal;
		//QOrderStatusHistory qOrderStatusHistory = QOrderStatusHistory.orderStatusHistory;
		//QOrderProductAttribute qOrderProductAttribute = QOrderProductAttribute.orderProductAttribute;
		QSubOrder qSubOrder = QSubOrder.subOrder;
		QProductReview qProductReview = QProductReview.productReview;
		//OrderAccount not loaded for now
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qOrder)
			//.join(qOrder.orderProducts, qOrderProduct).fetch()
			//.join(qOrder.orderTotal, qOrderTotal).fetch()
			//.leftJoin(qOrder.orderHistory, qOrderStatusHistory).fetch()
			.leftJoin(qOrder.subOrders,qSubOrder).fetch()
			.leftJoin(qSubOrder.orderProducts,qOrderProduct).fetch()
			.leftJoin(qOrderProduct.productReview, qProductReview).fetch()
			//.leftJoin(qOrderProduct.downloads).fetch()
			//.leftJoin(qOrderProduct.orderAttributes,qOrderProductAttribute).fetch()
			//.leftJoin(qOrderProduct.prices).fetch()
			
			//query.where(qOrder.merchant.id.eq(store.getId()))
			.orderBy(qOrder.id.desc());
			BooleanBuilder pBuilder = new BooleanBuilder();
			//pBuilder.and(qOrder.customerId.in(criteria.getCustomerId()));

				
		if(criteria.getCustomerId()!=null) {
			pBuilder.and(qOrder.customerId.in(criteria.getCustomerId()));
		}
		if(criteria.getStoreId() !=-1l) {
			pBuilder.and(qSubOrder.merchant.id.eq(criteria.getStoreId()));
		}
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			//pBuilder.and(qOrderProduct.productName.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
			pBuilder.andAnyOf(qOrder.billingUserName.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qOrderProduct.productName.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qOrder.id.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
			
		}
		if(criteria.getStatus() != null){
			if(criteria.getStatus().name().equalsIgnoreCase("REVIEW")){
				//pBuilder.and(qOrderProduct.productReview.isNull());
				pBuilder.and(qProductReview.isNull());
				
			}else{
				pBuilder.and(qOrder.status.eq(criteria.getStatus()));
			}			
		}
		if(!StringUtils.isBlank(criteria.getBeginDatePurchased())){
			//最近3个月的数据
			if(criteria.getBeginDatePurchased().equalsIgnoreCase("3")){
				pBuilder.and(qOrder.datePurchased.between(DateUtils.addMonths(dt, -3),dt));			
				}else{
					pBuilder.and(qOrder.datePurchased.between(DateUtils.addYears(dt, -1),dt));	
				}
		}
		if(criteria.getOrderBy().name().equals(CriteriaOrderBy.ASC)) {
			query.orderBy(qOrder.datePurchased.asc());
		} else {
			query.orderBy(qOrder.datePurchased.desc());
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		query.where(pBuilder);
		//orderList.setTotalCount((int)(query.count()));
		orderList.setOrders(query.list(qOrder));
		orderList.setTotalCount((int)(query.count()));
		return orderList;
		
	}

	@Override
	public OrderList listStoreByOrderCriteria(OrderCriteria criteria) {
		Date dt = new Date();
		OrderList orderList = new OrderList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(o) from Order as o");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		if(criteria.getStoreId() !=-1l) {
			countBuilderSelect.append(" ,SubOrder as s");
			countBuilderWhere.append(" and s.order.id = o.id and s.merchant.id =:mId");
		}
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countBuilderSelect.append(" ,OrderProduct as p");
			countBuilderWhere.append(" and o.id = p.subOrder.order.id ");
			countBuilderWhere.append(" and (p.productName like:pn or o.billingUserName like:cn or o.id like '%"+criteria.getFindName()+"%')");
		}
		
		if(!StringUtils.isBlank(criteria.getBeginDatePurchased())){
			countBuilderWhere.append(" and o.datePurchased BETWEEN ?1 AND ?2");
		}
			
		if(criteria.getStatus() != null){
			countBuilderWhere.append(" and o.status=:st");
		}
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + addWhere(countBuilderWhere));

		if(criteria.getStoreId() != -1l){
			countQ.setParameter("mId", criteria.getStoreId());
		}
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			countQ.setParameter("pn",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
			countQ.setParameter("cn",new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString());
		}
		if(criteria.getStatus() != null){
			countQ.setParameter("st", criteria.getStatus());
		}
		if(!StringUtils.isBlank(criteria.getBeginDatePurchased())){
			//最近3个月的数据
			if(criteria.getBeginDatePurchased().equalsIgnoreCase("3")){
				countQ.setParameter(1,DateUtils.addMonths(dt, -3), TemporalType.TIMESTAMP);
				countQ.setParameter(2,dt, TemporalType.TIMESTAMP);
			}else{
				
				countQ.setParameter(1, DateUtils.addYears(dt, -1), TemporalType.TIMESTAMP);
				countQ.setParameter(2, dt, TemporalType.TIMESTAMP);
			}
		}
		Number count = (Number) countQ.getSingleResult ();

		orderList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return orderList;
		
		
		QOrder qOrder = QOrder.order;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		//QOrderTotal qOrderTotal = QOrderTotal.orderTotal;
		//QOrderStatusHistory qOrderStatusHistory = QOrderStatusHistory.orderStatusHistory;
		//QOrderProductAttribute qOrderProductAttribute = QOrderProductAttribute.orderProductAttribute;
		QSubOrder qSubOrder = QSubOrder.subOrder;
		//QCustomer qCustomer = QCustomer.customer;
		//OrderAccount not loaded for now
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qOrder)
			.join(qOrder.subOrders, qSubOrder).fetch()
			//.leftJoin(qOrder.orderTotal, qOrderTotal).fetch()
			//.leftJoin(qOrder.orderHistory, qOrderStatusHistory).fetch()
			.leftJoin(qSubOrder.orderProducts,qOrderProduct).fetch()
			//.leftJoin(qOrderProduct.downloads).fetch()
			//.leftJoin(qOrderProduct.orderAttributes,qOrderProductAttribute).fetch()
			//leftJoin(qOrderProduct.orderAttributes,qCustomer).fetch()
			//.leftJoin(qOrderProduct.prices).fetch()
			//.where(qSubOrder.merchant.id.eq(criteria.getStoreid()))
			.orderBy(qOrder.id.desc());
			BooleanBuilder pBuilder = new BooleanBuilder();
			if(criteria.getStoreId() != -1l){
				pBuilder.and(qSubOrder.merchant.id.eq(criteria.getStoreId()));
			}
		
		if(!StringUtils.isEmpty(criteria.getFindName())) {
			//pBuilder.and(qOrderProduct.productName.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
			pBuilder.andAnyOf(qOrder.billingUserName.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qOrderProduct.productName.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()),
					qOrder.id.like(new StringBuilder().append("%").append(criteria.getFindName()).append("%").toString()));
			
		}
		if(criteria.getStatus() != null){
			pBuilder.and(qOrder.status.eq(criteria.getStatus()));
		}
		if(!StringUtils.isBlank(criteria.getBeginDatePurchased())){
			//最近3个月的数据
			if(criteria.getBeginDatePurchased().equalsIgnoreCase("3")){
				pBuilder.and(qOrder.datePurchased.between(DateUtils.addMonths(dt, -3), dt));			
				}else{
					pBuilder.and(qOrder.datePurchased.between(DateUtils.addYears(dt, -1), dt));	
				}
		}
		if(criteria.getOrderBy().name().equals(CriteriaOrderBy.ASC)) {
			query.orderBy(qOrder.datePurchased.asc());
		} else {
			query.orderBy(qOrder.datePurchased.desc());
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		//if(pBuilder!=null) {
			query.where(pBuilder);
		//}
		
		orderList.setOrders(query.list(qOrder));

		return orderList;
	}
	
	private String addWhere (StringBuilder strb){
		if(strb.length()>0 && strb.indexOf("where") <0 ){
			int index = strb.indexOf("and");
			return " where " + strb.substring(index+3);
		}
		return strb.toString();
	}

	@Override
	public Order getByIdByStore(Long id, Long storeID) {
		// TODO Auto-generated method stub
		QOrder qOrder = QOrder.order;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		//QOrderTotal qOrderTotal = QOrderTotal.orderTotal;
		QOrderStatusHistory qOrderStatusHistory = QOrderStatusHistory.orderStatusHistory;
		QOrderProductAttribute qOrderProductAttribute = QOrderProductAttribute.orderProductAttribute;
		QSubOrder qSubOrder = QSubOrder.subOrder;
		//OrderAccount not loaded for now
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qOrder)
			//.join(qOrder.orderProducts, qOrderProduct).fetch()
			.join(qOrder.subOrders, qSubOrder).fetch()
			.join(qSubOrder.orderProducts, qOrderProduct).fetch()
			//.join(qOrder.orderTotal, qOrderTotal).fetch()
			.leftJoin(qOrder.orderHistory, qOrderStatusHistory).fetch()
			//.leftJoin(qOrderProduct.downloads).fetch()
			.leftJoin(qOrderProduct.orderAttributes,qOrderProductAttribute).fetch()
			//.leftJoin(qOrderProduct.prices).fetch()
			.where(qOrder.id.eq(id).and(qSubOrder.merchant.id.eq(storeID)));
			
		List<Order> ms = query.list(qOrder);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}		
		
	}

	@Override
	public long getTotalMoney(Customer customer) {
		// TODO Auto-generated method stub
		//add select
				StringBuilder countBuilderSelect = new StringBuilder();
				countBuilderSelect.append("select sum(s.total) from SubOrder as s ,Order as o");
				StringBuilder countBuilderWhere = new StringBuilder();
				
				
				countBuilderWhere.append(" where o.customerId=:mId");
				countBuilderWhere.append(" and s.order.id=o.id");
				countBuilderWhere.append(" and (s.status ='");
				countBuilderWhere.append(OrderStatus.ORDERED);
				countBuilderWhere.append("' or s.status ='");
				countBuilderWhere.append(OrderStatus.SHIPPED);
				countBuilderWhere.append("') ");
				
				//add where
				Query countQ = super.getEntityManager().createQuery(
						countBuilderSelect.toString() + countBuilderWhere.toString());
				countQ.setParameter("mId", customer.getId());
				
				Number count =0;
				
				try{
					count = (Number) countQ.getSingleResult ();
				}catch (Exception e){
					count=0;
				}
				if(count == null){
					return 0;
				}
				
				return count.longValue();
	}
	/**
	 * @author 百图生物
	 * 判定商家是否存在,其他信息查询功能
	 */
	public List<Object[]> queryByOrderProduct(Long id)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select op.SUBORDER_ID,op.PRICE,op.ONETIME_CHARGE from ORDER_PRODUCT op where op.ORDER_PRODUCT_ID=:id");
		Query q = super.getEntityManager().createNativeQuery(sb.toString());
		q.setParameter("id", id);
		return q.getResultList();
	}
	
	public List<Object[]> queryByStoreId(Long id)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT s.ORDER_ID as oid,s.ORDER_TOTAL as ot,o.ORDER_TOTAL as ort,s.MERCHANTID as mm from  SUBORDERS AS s  INNER JOIN ORDERS o on o.ORDER_ID=s.ORDER_ID");
		sb.append("  WHERE s.SUBORDER_ID=:id");
		Query q = super.getEntityManager().createNativeQuery(sb.toString());
		q.setParameter("id",id);
		return q.getResultList();
	}
	/***
	 *@author 百图生物
	 *修改积分 
	 */
	public boolean updateByMenberPoint(Long orderid,int pointScore){
		boolean flag = false;
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE MEMBER_POINTS SET LTFE_POINT=:score WHERE ORDER_ID=:oid");
			Query q = super.getEntityManager().createNativeQuery(sb.toString());
			q.setParameter("score", pointScore);
			q.setParameter("oid", orderid);
			if(q.executeUpdate()>0)
			{
				flag = true;
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @author 百图生物
	 * 修改价格
	 * */
	public boolean updateByOrderProduct(Long pid,Double price,Double totalprice)
	{
		boolean flag = false;
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("update ORDER_PRODUCT set PRICE=:price,ONETIME_CHARGE=:totalprice");
			sb.append(" where ORDER_PRODUCT_ID=:id");
			Query q =super.getEntityManager().createNativeQuery(sb.toString());
			q.setParameter("id",pid);
			q.setParameter("price",price);
			q.setParameter("totalprice",totalprice);
			if(q.executeUpdate()>0){
				flag = true;
			}
			else
			{
				flag =false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			flag =false;
		}
		return flag;
	}
	public boolean updateBySubOrder(Long suid,Long orderid,double money1,double money2)
	{
		boolean flag = false;
		try{
			
			StringBuffer sb1 = new StringBuffer();
			sb1.append("UPDATE SUBORDERS SET ORDER_TOTAL="+money1+" WHERE SUBORDER_ID="+suid+"");
			StringBuffer sb2 = new StringBuffer();
			sb2.append("UPDATE ORDERS SET ORDER_TOTAL="+money2+" WHERE ORDER_ID="+orderid+"");
			
			Query q1 = super.getEntityManager().createNativeQuery(sb1.toString());
			
			Query q2 = super.getEntityManager().createNativeQuery(sb2.toString());
			
			if(q1.executeUpdate() > 0 && q2.executeUpdate() > 0)
			{
				flag = true;
			}
			
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean exist(String phone) {
		boolean flag = false;
		try{
			
			StringBuffer sb1 = new StringBuffer();
			sb1.append("SELECT * from ORDERS where INVOICE_TELEPHONE='"+phone+"' and date(DATE_PURCHASED) = curdate() order by DATE_PURCHASED desc");
			Query q1 = super.getEntityManager().createNativeQuery(sb1.toString());
			 List<Order> orders=q1.getResultList();
			 if(orders!=null&&orders.size()>0){
				 flag=true; 
			 }
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
}
