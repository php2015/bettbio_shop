package com.salesmanager.core.business.catalog.product.dao.review;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductReviewDao extends SalesManagerEntityDao<Long, ProductReview> {

	List<ProductReview> getByCustomer(Customer customer);
	List<ProductReview> getByProduct(Product product);
	List<ProductReview> getByProduct(Product product, Language language);
	ProductReview getByProductAndCustomer(Long productId, Long customerId);
	void deleteByProductId(Long productID);
	/**
	 * 根据产品订单ID，产品ID，用户ID，获取用户商品订单评价信息
	 * @param orderProductId
	 * @param productId
	 * @param customerId
	 * @return
	 */
	public List<ProductReview> getByOrderProductAndCustomer(Long orderProductId, Long productId, Long customerId);
}
