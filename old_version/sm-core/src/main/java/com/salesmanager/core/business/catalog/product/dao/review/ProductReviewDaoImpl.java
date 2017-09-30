package com.salesmanager.core.business.catalog.product.dao.review;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.model.review.QProductReview;
import com.salesmanager.core.business.catalog.product.model.review.QProductReviewDescription;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.QCustomer;
import com.salesmanager.core.business.customer.model.attribute.QCustomerAttribute;
import com.salesmanager.core.business.customer.model.attribute.QCustomerOption;
import com.salesmanager.core.business.customer.model.attribute.QCustomerOptionValue;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProduct;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productReviewDao")
public class ProductReviewDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductReview>
		implements ProductReviewDao {



	@Override
	public List<ProductReview> getByCustomer(Customer customer) {
		
		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QCustomer qCustomer = QCustomer.customer;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.customer,qCustomer).fetch()
		.join(qEntity.product,qProduct).fetch()
		.leftJoin(qProduct.merchantStore).fetch()
		.leftJoin(qEntity.descriptions,qDescription).fetch()
		.where(qCustomer.id.eq(customer.getId()));
		
		return query.list(qEntity);
		
		
		
	}

	@Override
	public List<ProductReview> getByProduct(Product product) {
		
		
		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QProduct qProduct = QProduct.product;
		QCustomer qCustomer = QCustomer.customer;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.customer, qCustomer).fetch()
		.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
		.join(qEntity.product,qProduct).fetch()
		.leftJoin(qProduct.merchantStore).fetch()
		.leftJoin(qEntity.descriptions,qDescription).fetch()
		.where(qProduct.id.eq(product.getId()));
		
		return query.list(qEntity);
		
		
	}
	
	
	@Override
	public ProductReview getByProductAndCustomer(Long productId, Long customerId) {
		
		
		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QProduct qProduct = QProduct.product;
		QCustomer qCustomer = QCustomer.customer;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.customer, qCustomer).fetch()
		.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
		.join(qEntity.product,qProduct).fetch()
		.leftJoin(qProduct.merchantStore).fetch()
		.leftJoin(qEntity.descriptions,qDescription).fetch()
		.where(qProduct.id.eq(productId).and(qCustomer.id.eq(customerId)));
		
		List<ProductReview> ms = query.list(qEntity);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
	}
	
	@Override
	public List<ProductReview> getByProduct(Product product, Language language) {
		
		
		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QProduct qProduct = QProduct.product;
		QCustomer qCustomer = QCustomer.customer;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
				.join(qEntity.customer, qCustomer).fetch()
		.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
		.join(qEntity.product,qProduct).fetch()
		.leftJoin(qProduct.merchantStore).fetch()
		.leftJoin(qEntity.descriptions,qDescription).fetch()
		.where(qProduct.id.eq(product.getId())
				.and(qDescription.language.id.eq(language.getId())));
		
		return query.list(qEntity);
		
		
	}
	
	@Override
	public ProductReview getById(Long id) {

		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.join(qEntity.customer).fetch()
			.join(qEntity.product,qProduct).fetch()
			.leftJoin(qProduct.merchantStore).fetch()
			.leftJoin(qEntity.descriptions,qDescription).fetch()
			.where(qEntity.id.eq(id));
		
		List<ProductReview> ms = query.list(qEntity);
		if(ms !=null && ms.size()>0){
			return ms.get(0);
		}else  {
			return null;
		}
		
		
		
		
	}


	/**
	 * 根据产品订单ID，产品ID，用户ID，获取用户商品订单评价信息
	 * @param orderProductId
	 * @param productId
	 * @param customerId
	 * @return
	 */
	public List<ProductReview> getByOrderProductAndCustomer(Long orderProductId, Long productId, Long customerId) {
		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QProduct qProduct = QProduct.product;
		QCustomer qCustomer = QCustomer.customer;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct; 
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.customer, qCustomer).fetch()
		.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
		.join(qEntity.product,qProduct).fetch()
		.leftJoin(qEntity.descriptions,qDescription).fetch()
		.join(qEntity.orderProduct, qOrderProduct).fetch()
		.where(qProduct.id.eq(productId).and(qCustomer.id.eq(customerId)).and(qOrderProduct.id.eq(orderProductId)));
		return query.list(qEntity);
	}

	@Override
	public void deleteByProductId(Long productID) {
		// TODO Auto-generated method stub
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("delete from ProductReviewDescription as prd ");		
		StringBuilder countBuilderWhere = new StringBuilder();
	
		countBuilderWhere.append(" where prd.productReview.product.id = ").append(productID);
		
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		countQ.executeUpdate();
		
		countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("delete from ProductReview as pr ");		
		 countBuilderWhere = new StringBuilder();
	
		countBuilderWhere.append(" where pr.product.id = ").append(productID);
		
		countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		countQ.executeUpdate();
	}


}
