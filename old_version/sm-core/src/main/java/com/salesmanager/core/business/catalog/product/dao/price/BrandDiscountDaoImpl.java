package com.salesmanager.core.business.catalog.product.dao.price;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.price.BrandDiscount;
import com.salesmanager.core.business.catalog.product.model.price.QBrandDiscount;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("brandDiscountDao")
public class BrandDiscountDaoImpl extends SalesManagerEntityDaoImpl<Long, BrandDiscount>implements BrandDiscountDao {

	@Override
	public BrandDiscount getByBrandAndStoreId(long brandId, long storeId) {
		QBrandDiscount qDiscount = QBrandDiscount.brandDiscount;
		JPQLQuery query = new JPAQuery(getEntityManager());
		query.from(qDiscount).where(qDiscount.brandId.eq(brandId).and(qDiscount.storeId.eq(storeId)));
		List<BrandDiscount> results = query.list(qDiscount);
		if (results != null && results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<BrandDiscount> getListByStoreId(long storeId) {
		QBrandDiscount qDiscount = QBrandDiscount.brandDiscount;
		JPQLQuery query = new JPAQuery(getEntityManager());
		query.from(qDiscount).where(qDiscount.storeId.eq(storeId));
		List<BrandDiscount> results = query.list(qDiscount);
		return results;
	}

	@Override
	public void deleteByStoreId(long storeId) {
		super.deleteByProperties(new String[] { "storeId" }, new Long[] { storeId });
	}

	private int getQueryType(String brandName, String storeName) {
		if (brandName != null && !brandName.isEmpty()) {
			if (storeName != null && !storeName.isEmpty()) {
				return 3; // has both brand and store name
			} else {
				return 2; // only has brand name
			}
		} else {
			if (storeName != null && !storeName.isEmpty()) {
				return 1; // only has store name
			} else {
				return 0; // no any name assigned
			}
		}
	}

	@Override
	public int getListSizeByBrandAndStoreName(String brandName, String storeName) {
		int queryType = getQueryType(brandName, storeName);
		// select clause
		StringBuilder sb = new StringBuilder();
		sb.append("select count(d.BRAND_DISCOUNT_ID) from BRAND_DISCOUNT d ");
		sb.append("left join MANUFACTURER b on d.BRAND_ID=b.MANUFACTURER_ID ");
		sb.append("left join MANUFACTURER_DESCRIPTION bd on d.BRAND_ID=bd.MANUFACTURER_ID ");
		sb.append("left join MERCHANT_STORE s on d.STORE_ID=s.MERCHANT_ID ");

		fillWhereClause(queryType, sb);

		// create query
		String queryStr = sb.toString();
		Query query = getEntityManager().createNativeQuery(queryStr);

		fillParameters(brandName, storeName, queryType, query);

		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public List<Long> getPageIdListByBrandAndStoreName(String brandName, String storeName, int startPos, int pageSize) {
		int queryType = getQueryType(brandName, storeName);
		// select clause
		StringBuilder sb = new StringBuilder();
		sb.append("select d.BRAND_DISCOUNT_ID from BRAND_DISCOUNT d ");
		sb.append("left join MANUFACTURER b on d.BRAND_ID=b.MANUFACTURER_ID ");
		sb.append("left join MANUFACTURER_DESCRIPTION bd on d.BRAND_ID=bd.MANUFACTURER_ID ");
		sb.append("left join MERCHANT_STORE s on d.STORE_ID=s.MERCHANT_ID ");

		fillWhereClause(queryType, sb);

		// order by clause
		sb.append("order by bd.NAME ASC, s.STORE_NAME ASC ");
		// limit clause
		sb.append("limit :fromNo, :sizeNo");
		// create query
		String queryStr = sb.toString();
		Query query = getEntityManager().createNativeQuery(queryStr);

		fillParameters(brandName, storeName, queryType, query);
		query.setParameter("fromNo", startPos);
		query.setParameter("sizeNo", pageSize);

		@SuppressWarnings("unchecked")
		List<Number> queryResultList = query.getResultList();
		List<Long> result = new ArrayList<Long>();
		for (Number num : queryResultList) {
			result.add(num.longValue());
		}
		return result;
	}

	protected void fillParameters(String brandName, String storeName, int queryType, Query query) {
		// set parameters
		switch (queryType) {
		case 0: // no any name assigned
			break;
		case 1: // only has store name
			query.setParameter("storeName", "%" + storeName + "%");
			break;
		case 2: // only has brand name
			query.setParameter("brandName", "%" + brandName + "%");
			break;
		default: // has both brand and store name
			query.setParameter("storeName", "%" + storeName + "%");
			query.setParameter("brandName", "%" + brandName + "%");
			break;
		}
	}

	protected void fillWhereClause(int queryType, StringBuilder sb) {
		// where clause
		switch (queryType) {
		case 0: // no any name assigned
			break;
		case 1: // only has store name
			sb.append("where s.STORE_NAME like :storeName ");
			break;
		case 2: // only has brand name
			sb.append("where bd.NAME like :brandName ");
			break;
		default: // has both brand and store name
			sb.append("where s.STORE_NAME like :storeName and bd.NAME like :brandName ");
			break;
		}
	}

	@Override
	public void deleteAll() {
		super.getEntityManager().createNativeQuery("DELETE FROM BRAND_USERSEGMENT_DISCOUNT").executeUpdate();
		super.getEntityManager().createQuery("DELETE FROM BrandDiscount bd").executeUpdate();
	}

	private static final String sqlGetInvalidIds = "select a.BRAND_DISCOUNT_ID from BRAND_DISCOUNT a left join BRAND_USERSEGMENT_DISCOUNT b on a.BRAND_DISCOUNT_ID=b.BRAND_DISCOUNT_ID "
			+ "left join MANUFACTURER_DESCRIPTION c on a.BRAND_ID=c.MANUFACTURER_ID "
			+ "left join MERCHANT_STORE d on a.STORE_ID=d.MERCHANT_ID "
			+ "where c.MANUFACTURER_ID is null or d.MERCHANT_ID is null ";

	@Override
	public void cleanUpInvalidBrands() {
		Query query = super.getEntityManager().createNativeQuery(sqlGetInvalidIds);
		@SuppressWarnings("unchecked")
		List<Number> ids = query.getResultList();
		if (ids == null || ids.size() == 0) {
			return;
		}
		List<Long> bdIds = new ArrayList<Long>();
		for (Number id : ids) {
			BrandDiscount d = getById(id.longValue());
			delete(d);
		}
	}

	private static final String sqlGetDiscountByProductIds = "select p.PRODUCT_ID,bdr.DISCOUNTS_KEY, bdr.DISCOUNTS "
			+ "from PRODUCT p left join BRAND_DISCOUNT bd on p.MANUFACTURER_ID=bd.BRAND_ID "
			+ "left join BRAND_USERSEGMENT_DISCOUNT bdr on bd.BRAND_DISCOUNT_ID=bdr.BRAND_DISCOUNT_ID "
			+ "where bd.BRAND_DISCOUNT_ID is not null and bdr.DISCOUNTS_KEY='logged_customer' and p.PRODUCT_ID in (:pids)";

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDiscountRateByProductIdList(List<Long> ids) {
		if (ids == null || ids.isEmpty()){
			return new ArrayList<Object[]>();
		}
		Query query = getEntityManager().createNativeQuery(sqlGetDiscountByProductIds);
		query.setParameter("pids", ids);
		return query.getResultList();
	}

}
