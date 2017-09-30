package com.salesmanager.core.business.catalog.product.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullIndexHelper {
	private FullIndexHelper() {
	}

	private static final String[] simpleStringFields = new String[] { "pname", "penname", "brandName", "simpledesc",
			"friendlyUrl", "store", "code", "cas", "quality" };
	private static final String[] simpleBooleanFields = new String[] { "isfree" };
	private static final String[] simpleLongFields = new String[] { "manufacturer" };

	private static final String sqlGetProductTotalNumber = "select sum(PRODUCT_ID!=0) from PRODUCT";
	private static final String sqlQueryCategory = "select CATEGORY_ID, PARENT_ID from CATEGORY";
	private static final String sqlQueryLanguage = "select LANGUAGE_ID, CODE from LANGUAGE";
	private static final String sqlGetProductImage = "select pi.DEFAULT_IMAGE as def, pi.PRODUCT_IMAGE as image from PRODUCT_IMAGE pi where pi.PRODUCT_ID=? order by pi.DEFAULT_IMAGE desc limit 1";
	private static final String sqlGetProductAuth = "select au.AUTH_ID as aId, au.AUTH_TYPE as aType from AUTH_BRAND ab left join AUTHORIZATION au on ab.AUTH_ID=au.AUTH_ID where ab.BRAND_ID=? and au.MERCHANT_ID=?;";
	private static String sqlGetProductCertificates = "select b.NAME as name, c.BASEINFO as info from PRODUCT_CERTIFICATE c left join BASEDATA_TYPE b on c.BASEDATATYPE_ID=b.BASEDATA_TYPE_ID where c.PRODUCT_ID=?";
	private static String sqlGetProductProof = "select b.NAME as name, p.DATE_BUYED as info from PRODUCT_PROOF p left join BASEDATA_TYPE b on p.BASEDATATYPE_ID=b.BASEDATA_TYPE_ID where p.PRODUCT_ID=?";
	private static String sqlGetProductThirdProof = "select b.NAME as name, p.DESCRIPTION as info from PRODUCT_THIRDPROOF p left join BASEDATA_TYPE b on p.BASEDATATYPE_ID=b.BASEDATA_TYPE_ID where p.PRODUCT_ID=?";
	private static String sqlGetProductTestreport = "select p.TITLE as name, p.DESCRIPTION as info from PRODUCT_SELFPROOF p where PRODUCT_ID=?";
	private static final String sqlGetProductPrice = "select pp.DEFAULT_PRICE as isDefault, pp.PRODUCT_PRICE_AMOUNT as amount, pp.PRODUCT_PRICE_PERIOD as period, "
			+ "pp.PRODUCT_PRICE_SPECIAL_AMOUNT as special, pp.PRODUCT_PRICE_SPECIAL_ST_DATE as startDate, pp.PRODUCT_PRICE_SPECIAL_END_DATE as endDate "
			+ "from PRODUCT_AVAILABILITY pv left join PRODUCT_PRICE pp on pv.PRODUCT_AVAIL_ID=pp.PRODUCT_AVAIL_ID "
			+ "left join PRODUCT_PRICE_DESCRIPTION pd on pp.PRODUCT_PRICE_ID=pd.PRODUCT_PRICE_ID where pv.PRODUCT_ID=?";
	private static final String sqlGetProductIdByLimits = "select PRODUCT_ID from PRODUCT order by PRODUCT_ID ASC limit ?,?";
	private static final String sqlQueryProduct = "select p.PRODUCT_ID as id, p.AVAILABLE as avaialble, p.PRODUCT_AUDIT as audit, d.EN_NAME as penname, d.NAME as pname, "
			+ "manf.MANUFACTURER_ID as manufacturer, manfdscp.NAME as brandName, "
			+ "p.CODE as code, p.CAS as cas, p.QUALITY_SCORE as quality, p.SKU as _fi_sku, "
			+ "store.STORE_NAME as store, store.STORE_CODE as _fi_storeCode, store.MERCHANT_ID as _fi_storeId, c.CATEGORY_ID as categoryId, "
			+ "d.LANGUAGE_ID as lang, p.PRODUCT_FREE as isfree, p.PRODUCT_DIAMOND as isDiamond, "
			+ "d.SIMPLE_DESCRIPTION as simpledesc, d.SEF_URL as friendlyUrl "
			+ " from PRODUCT p left join PRODUCT_DESCRIPTION d on p.PRODUCT_ID=d.PRODUCT_ID "
			+ "left join MERCHANT_STORE store on p.MERCHANT_ID=store.MERCHANT_ID "
			+ "left join PRODUCT_CATEGORY c on p.PRODUCT_ID=c.PRODUCT_ID "
			+ "left join MANUFACTURER manf on p.MANUFACTURER_ID=manf.MANUFACTURER_ID "
			+ "left join MANUFACTURER_DESCRIPTION manfdscp on manf.MANUFACTURER_ID=manfdscp.MANUFACTURER_ID "
			+ " where p.PRODUCT_ID>=? and p.PRODUCT_ID<? ";

	private static Map<Integer, Integer> categories = null;
	private static Map<Integer, String> languages = null;

	public static long getTotalNumber(Connection connection) throws SQLException {
		Statement st = connection.createStatement();
		st.execute(sqlGetProductTotalNumber);
		ResultSet rs = st.getResultSet();
		rs.next();
		long result = rs.getLong(1);
		rs.close();
		return result;
	}

	/**
	 * 将产品ID分拆成几个段，并找出每段的ID范围。 SQL的注意事项：第一要按照ID排序，第二要使用索引。否则会很慢。
	 * 
	 * @param connection
	 * @param totalNumber
	 * @param segsize
	 * @return
	 * @throws SQLException
	 */
	public static List<Long> getIdSegmentStartValues(Connection connection, long totalNumber, long segsize)
			throws SQLException {
		int pos = 0;
		List<Long> result = new ArrayList<Long>();
		PreparedStatement st = connection.prepareStatement(sqlGetProductIdByLimits);
		ResultSet rs = null;
		while (pos < totalNumber) {
			st.setLong(1, pos);
			pos += segsize;
			st.setLong(2, segsize);
			rs = st.executeQuery();
			while (rs.next()) {
				result.add(rs.getLong(1));
				break;
			}
		}
		rs.last();
		result.add(rs.getLong(1) + 1);
		st.close();
		return result;
	}

	public static PreparedStatement createProductQueryStatement(Connection connection, long lowerId, long higherId)
			throws SQLException {
		PreparedStatement st = connection.prepareStatement(sqlQueryProduct);
		st.setLong(1, lowerId);
		st.setLong(2, higherId);
		return st;
	}

	public static boolean getAvailable(ResultSet rs) throws SQLException {
		return rs.getBoolean(2);
	}

	public static long getProductId(ResultSet rs) throws SQLException {
		return rs.getLong(1);
	}

	public static Map<String, Object> makeProductData(ResultSet rs, PreparedStatement priceSt,
			PreparedStatement imageSt, PreparedStatement authSt, PreparedStatement certificatesSt,
			PreparedStatement proofSt, PreparedStatement thirdProofSt, PreparedStatement testreportSt)
					throws SQLException {
		Map<String, Object> result = new HashMap<String, Object>();
		long productId = rs.getLong(1);
		result.put("id", productId);
		result.put("_id", productId);
		Object tempValue = null;

		for (String fName : simpleStringFields) {
			String value = rs.getString(fName);
			if (value == null) {
				continue;
			}
			result.put(fName, value);
		}
		for (String fName : simpleBooleanFields) {
			Boolean value = rs.getBoolean(fName);
			result.put(fName, value);
		}
		for (String fName : simpleLongFields) {
			Long value = rs.getLong(fName);
			result.put(fName, value);
		}
		result.put("lang", getLanguageCode(rs.getLong("lang")));
		tempValue = getProductImage(imageSt, productId, rs.getString("_fi_storeCode"), rs.getString("_fi_sku"));
		if (tempValue != null) {
			result.put("image", tempValue);
		}
		Boolean tempBool = rs.getBoolean("isDiamond");
		if (tempBool != null && tempBool.booleanValue()) {
			result.put("isDiamond", true);
			result.put("diamond", 1);
		}
		result.put("chkey", result.get("pname"));
		tempValue = result.get("penname");
		if (tempValue != null) {
			result.put("enkey", tempValue);
		}
		tempValue = getProductCategories(rs.getInt("categoryId"));
		if (tempValue != null) {
			result.put("category", tempValue);
		}
		tempValue = getProductCertificates(certificatesSt, productId);
		if (tempValue != null) {
			result.put("certificates", tempValue);
		}
		tempValue = getProductProofs(proofSt, productId);
		if (tempValue != null) {
			result.put("proofs", tempValue);
		}
		tempValue = getProductThirdProofs(thirdProofSt, productId);
		if (tempValue != null) {
			result.put("thirdproofs", tempValue);
		}
		tempValue = getProductTestreports(testreportSt, productId);
		if (tempValue != null) {
			result.put("testreports", tempValue);
		}
		tempValue = getProductAuth(authSt, productId, rs.getLong("manufacturer"), rs.getLong("_fi_storeId"));
		if (tempValue != null) {
			result.put("auth", tempValue);
		}
		setProductPriceAndPeriod(priceSt, result, productId);
		return result;
	}

	private static void setProductPriceAndPeriod(PreparedStatement priceSt, Map<String, Object> result, long productId)
			throws SQLException {
		priceSt.setLong(1, productId);
		ResultSet rs = priceSt.executeQuery();

		Double resultPrice = 0.0;
		boolean hasSpotGoods = false;

		Double amount;
		Double specialAmount;
		Date startDate;
		Date endDate;
		String period;
		Double finalPrice;
		while (rs.next()) {
			amount = rs.getDouble("amount");
			specialAmount = rs.getDouble("special");
			startDate = rs.getDate("startDate");
			endDate = rs.getDate("endDate");
			period = rs.getString("period");
			finalPrice = amount;
			if (startDate != null && endDate != null) {
				Date today = new Date();
				if (startDate.getTime() <= today.getTime() && today.getTime() <= endDate.getTime()) {
					finalPrice = specialAmount;
				}
			}
			// get one final price so far
			resultPrice = Math.max(finalPrice, resultPrice);
			if ("现货".equals(period)) {
				hasSpotGoods = true;
			}
		}
		result.put("price", resultPrice.longValue());
		result.put("period", hasSpotGoods ? 1 : 0);
	}

	private static Object getProductAuth(PreparedStatement authSt, long productId, long brandId, long storeId)
			throws SQLException {
		if (brandId == 0) {
			return null;
		}
		authSt.setLong(1, brandId);
		authSt.setLong(2, storeId);
		List<String[]> datas = new ArrayList<String[]>();
		ResultSet rs = authSt.executeQuery();
		while (rs.next()) {
			String[] data = new String[] { String.valueOf(rs.getLong(1)), String.valueOf(rs.getLong(2)) };
			datas.add(data);
		}
		if (datas.isEmpty()) {
			return null;
		}
		return datas.toArray();
	}

	private static Object getProductProofs(PreparedStatement proofSt, long productId) throws SQLException {
		return get2StringData(proofSt, productId);
	}

	private static Object getProductCertificates(PreparedStatement certificatesSt, long productId) throws SQLException {
		return get2StringData(certificatesSt, productId);
	}

	private static Object getProductTestreports(PreparedStatement testreportSt, long productId) throws SQLException {
		return get2StringData(testreportSt, productId);
	}

	private static Object getProductThirdProofs(PreparedStatement thirdProofSt, long productId) throws SQLException {
		return get2StringData(thirdProofSt, productId);
	}

	private static Object get2StringData(PreparedStatement st, long productId) throws SQLException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		st.setLong(1, productId);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("pname", rs.getString(1));
			data.put("description", rs.getString(2));
			result.add(data);
		}
		if (result.isEmpty()) {
			return null;
		}
		return result.toArray();
	}

	private static Object getProductCategories(int categoryId) {
		List<Long> result = new ArrayList<Long>();
		int cId = categoryId;
		while (cId > 0) {
			if (result.contains(cId)) {
				break;
			}
			if (categories.containsKey(cId)) {
				result.add((long) cId);
				cId = categories.get(cId);
			} else {
				break;
			}
		}
		if (result.isEmpty()) {
			return null;
		}
		return result.toArray();
	}

	private static String getProductImage(PreparedStatement imageSt, long productId, String storeCode, String skuCode)
			throws SQLException {
		imageSt.setLong(1, productId);
		ResultSet rs = imageSt.executeQuery();
		String imageName = null;
		while (rs.next()) {
			// use SQL to select image order by is-default desc, so only try to
			// get one record
			imageName = rs.getString("image");
		}
		if (imageName == null) {
			return null;
		}

		String imgpath = new StringBuilder().append("/static").append("/").append(storeCode).append("/")
				.append("PRODUCT").append("/").append(skuCode).append("/").append(imageName).toString();
		return imgpath;
	}

	private static String getLanguageCode(long langId) {
		return languages.get((int)langId);
	}

	public static PreparedStatement createGetPriceStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(sqlGetProductPrice);
	}

	public static long getAudit(ResultSet rs) throws SQLException {
		return rs.getLong(3);
	}

	public static PreparedStatement createGetImageStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(sqlGetProductImage);
	}

	public static PreparedStatement createGetAuthStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(sqlGetProductAuth);
	}

	public static void prepare(Connection connection) throws SQLException {
		Statement comStatement = connection.createStatement();
		ResultSet rs = comStatement.executeQuery(sqlQueryCategory);
		categories = new HashMap<Integer, Integer>();
		while (rs.next()) {
			int cId = rs.getInt(1);
			int pId = rs.getInt(2);
			categories.put(cId, pId);
		}

		rs = comStatement.executeQuery(sqlQueryLanguage);
		languages = new HashMap<Integer, String>();
		while (rs.next()) {
			int lId = rs.getInt(1);
			String code = rs.getString(2);
			languages.put(lId, code);
		}
	}

	public static PreparedStatement createGetCertificatesStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(sqlGetProductCertificates);
	}

	public static PreparedStatement createGetProofStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(sqlGetProductProof);
	}

	public static PreparedStatement createGetThirdProofStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(sqlGetProductThirdProof);
	}

	public static PreparedStatement createGetTestreportStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(sqlGetProductTestreport);
	}

}
