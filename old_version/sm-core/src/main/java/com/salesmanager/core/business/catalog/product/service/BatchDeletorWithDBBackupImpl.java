/**
 * 一段用来层叠删除数据库表的代码 
 */
package com.salesmanager.core.business.catalog.product.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BatchDeletorWithDBBackupImpl extends BaseDeletorWithDBBackupImpl {
  	public int cascadeDeleteCustomer_option(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Customer_option_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "CUSTOMER_OPTION", "OPTION_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Customer_option_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Customer_option_idList.add(rs.getLong(1));
 			}
 			if (Customer_option_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "CUSTOMER_ATTRIBUTE", "OPTION_ID", Customer_option_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_OPTION_DESC", "CUSTOMER_OPTION_ID", Customer_option_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_OPTION_SET", "CUSTOMER_OPTION_ID", Customer_option_idList);
 			Customer_option_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "CUSTOMER_OPTION", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("CUSTOMER_OPTION", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteShopping_cart(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Shp_cart_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "SHOPPING_CART", "SHP_CART_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Shp_cart_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Shp_cart_idList.add(rs.getLong(1));
 			}
 			if (Shp_cart_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteShopping_cart_item(conn, "SHP_CART_ID", Shp_cart_idList);
 			Shp_cart_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "SHOPPING_CART", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("SHOPPING_CART", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteAuthorization(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Auth_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "AUTHORIZATION", "AUTH_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Auth_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Auth_idList.add(rs.getLong(1));
 			}
 			if (Auth_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "AUTH_BRAND", "AUTH_ID", Auth_idList);
 			Auth_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "AUTHORIZATION", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("AUTHORIZATION", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteCustomer(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Customer_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "CUSTOMER", "CUSTOMER_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Customer_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Customer_idList.add(rs.getLong(1));
 			}
 			if (Customer_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "CUSTOMER_ADDRESS", "CUSTOMER_ID", Customer_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_ATTRIBUTE", "CUSTOMER_ID", Customer_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_GROUP", "CUSTOMER_ID", Customer_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_IMAGE", "CUSTOMER_ID", Customer_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_INVOICE", "CUSTOMER_ID", Customer_idList);
 			totalDeleted += executeDelete(conn, "GIFT_ORDER", "CUSTOMER_ID", Customer_idList);
 			totalDeleted += executeDelete(conn, "MEMBER_POINTS", "CUSTOMER_ID", Customer_idList);
 			totalDeleted += cascadeDeleteParcel(conn, "CUSTMOER_ID", Customer_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_BROWSW", "CUSTMOER_ID", Customer_idList);
 			totalDeleted += cascadeDeleteProduct_review(conn, "CUSTOMERS_ID", Customer_idList);
 			Customer_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "CUSTOMER", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("CUSTOMER", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteOrder_account(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Order_account_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "ORDER_ACCOUNT", "ORDER_ACCOUNT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Order_account_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Order_account_idList.add(rs.getLong(1));
 			}
 			if (Order_account_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "ORDER_ACCOUNT_PRODUCT", "ORDER_ACCOUNT_ID", Order_account_idList);
 			Order_account_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "ORDER_ACCOUNT", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("ORDER_ACCOUNT", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteCustomer_option_value(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Customer_option_value_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "CUSTOMER_OPTION_VALUE", "OPTION_VALUE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Customer_option_value_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Customer_option_value_idList.add(rs.getLong(1));
 			}
 			if (Customer_option_value_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "CUSTOMER_ATTRIBUTE", "OPTION_VALUE_ID", Customer_option_value_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_OPT_VAL_DESCRIPTION", "CUSTOMER_OPT_VAL_ID", Customer_option_value_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_OPTION_SET", "CUSTOMER_OPTION_VALUE_ID", Customer_option_value_idList);
 			Customer_option_value_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "CUSTOMER_OPTION_VALUE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("CUSTOMER_OPTION_VALUE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteCurrency(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Currency_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "CURRENCY", "CURRENCY_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Currency_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Currency_idList.add(rs.getLong(1));
 			}
 			if (Currency_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteMerchant_store(conn, "CURRENCY_ID", Currency_idList);
 			totalDeleted += cascadeDeleteOrders(conn, "CURRENCY_ID", Currency_idList);
 			Currency_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "CURRENCY", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("CURRENCY", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteBasedata_type(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Basedata_type_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "BASEDATA_TYPE", "BASEDATATYPE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Basedata_type_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Basedata_type_idList.add(rs.getLong(1));
 			}
 			if (Basedata_type_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteProduct_certificate(conn, "BASEDATATYPE_ID", Basedata_type_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_PROOF", "BASEDATATYPE_ID", Basedata_type_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_THIRDPROOF", "BASEDATATYPE_ID", Basedata_type_idList);
 			Basedata_type_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "BASEDATA_TYPE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("BASEDATA_TYPE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteParcel(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Parcel_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PARCEL", "Parcel_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Parcel_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Parcel_idList.add(rs.getLong(1));
 			}
 			if (Parcel_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteProduct(conn, "Parcel_ID", Parcel_idList);
 			Parcel_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PARCEL", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PARCEL", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteBrand_discount(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Brand_discount_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "BRAND_DISCOUNT", "BRAND_DISCOUNT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Brand_discount_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Brand_discount_idList.add(rs.getLong(1));
 			}
 			if (Brand_discount_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "BRAND_USERSEGMENT_DISCOUNT", "BRAND_DISCOUNT_ID", Brand_discount_idList);
 			Brand_discount_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "BRAND_DISCOUNT", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("BRAND_DISCOUNT", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteMerchant_rank(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Rank_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "MERCHANT_RANK", "RANK_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Rank_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Rank_idList.add(rs.getLong(1));
 			}
 			if (Rank_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteMerchant_rank_profile(conn, "RANK_ID", Rank_idList);
 			Rank_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "MERCHANT_RANK", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("MERCHANT_RANK", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteShopping_cart_item(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Shp_cart_item_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "SHOPPING_CART_ITEM", "SHP_CART_ITEM_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Shp_cart_item_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Shp_cart_item_idList.add(rs.getLong(1));
 			}
 			if (Shp_cart_item_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "SHOPPING_CART_ATTR_ITEM", "SHP_CART_ITEM_ID", Shp_cart_item_idList);
 			Shp_cart_item_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "SHOPPING_CART_ITEM", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("SHOPPING_CART_ITEM", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_image(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_image_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_IMAGE", "PRODUCT_IMAGE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_image_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_image_idList.add(rs.getLong(1));
 			}
 			if (Product_image_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "PRODUCT_IMAGE_DESCRIPTION", "PRODUCT_IMAGE_ID", Product_image_idList);
 			Product_image_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_IMAGE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_IMAGE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_price(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_price_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_PRICE", "PRODUCT_PRICE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_price_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_price_idList.add(rs.getLong(1));
 			}
 			if (Product_price_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			
 			if (Product_price_idList.size() > 0){
 				totalDeleted += executeDelete(conn, "PRODUCT_PRICE_DESCRIPTION", "PRODUCT_PRICE_ID", Product_price_idList);
 				Product_price_idList.clear();
 			}
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_PRICE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_PRICE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteMerchant_store(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Merchant_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "MERCHANT_STORE", "MERCHANT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Merchant_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Merchant_idList.add(rs.getLong(1));
 			}
 			if (Merchant_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteCustomer(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteAuthorization(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteCategory(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteContent(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteCustomer_option(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteCustomer_option_value(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += executeDelete(conn, "FILE_HISTORY", "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteManufacturer(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += executeDelete(conn, "MERCHANT_CONFIGURATION", "MERCHANT_ID", Merchant_idList);
 			totalDeleted += executeDelete(conn, "MERCHANT_LANGUAGE", "stores_MERCHANT_ID", Merchant_idList);
 			totalDeleted += executeDelete(conn, "MERCHANT_LOG", "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteProduct(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteProduct_option(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteProduct_option_value(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_RELATIONSHIP", "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteShopping_cart(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteSuborders(conn, "MERCHANTID", Merchant_idList);
 			totalDeleted += executeDelete(conn, "SYSTEM_NOTIFICATION", "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteTax_class(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteTax_rate(conn, "MERCHANT_ID", Merchant_idList);
 			totalDeleted += cascadeDeleteUser(conn, "MERCHANT_ID", Merchant_idList);
 			Merchant_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "MERCHANT_STORE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("MERCHANT_STORE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_review(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_review_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_REVIEW", "PRODUCT_REVIEW_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_review_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_review_idList.add(rs.getLong(1));
 			}
 			if (Product_review_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "PRODUCT_REVIEW_DESCRIPTION", "PRODUCT_REVIEW_ID", Product_review_idList);
 			Product_review_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_REVIEW", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_REVIEW", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeletePermission(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Permission_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PERMISSION", "PERMISSION_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Permission_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Permission_idList.add(rs.getLong(1));
 			}
 			if (Permission_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "PERMISSION_GROUP", "PERMISSION_ID", Permission_idList);
 			Permission_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PERMISSION", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PERMISSION", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_type(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_type_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_TYPE", "PRODUCT_TYPE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_type_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_type_idList.add(rs.getLong(1));
 			}
 			if (Product_type_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteProduct(conn, "PRODUCT_TYPE_ID", Product_type_idList);
 			Product_type_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_TYPE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_TYPE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteOrder_product(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Order_product_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "ORDER_PRODUCT", "ORDER_PRODUCT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Order_product_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Order_product_idList.add(rs.getLong(1));
 			}
 			if (Order_product_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "ORDER_ACCOUNT_PRODUCT", "ORDER_PRODUCT_ID", Order_product_idList);
 			totalDeleted += executeDelete(conn, "ORDER_PRODUCT_ATTRIBUTE", "ORDER_PRODUCT_ID", Order_product_idList);
 			totalDeleted += executeDelete(conn, "ORDER_PRODUCT_DOWNLOAD", "ORDER_PRODUCT_ID", Order_product_idList);
 			totalDeleted += executeDelete(conn, "ORDER_PRODUCT_PRICE", "ORDER_PRODUCT_ID", Order_product_idList);
 			totalDeleted += cascadeDeleteProduct_review(conn, "ORDER_PRODUCT_ID", Order_product_idList);
 			Order_product_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "ORDER_PRODUCT", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("ORDER_PRODUCT", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_availability(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_avail_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_AVAILABILITY", "PRODUCT_AVAIL_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_avail_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_avail_idList.add(rs.getLong(1));
 			}
 			if (Product_avail_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteProduct_price(conn, "PRODUCT_AVAIL_ID", Product_avail_idList);
 			Product_avail_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_AVAILABILITY", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_AVAILABILITY", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_option(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_option_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_OPTION", "OPTION_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_option_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_option_idList.add(rs.getLong(1));
 			}
 			if (Product_option_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "PRODUCT_ATTRIBUTE", "OPTION_ID", Product_option_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_OPTION_DESC", "PRODUCT_OPTION_ID", Product_option_idList);
 			Product_option_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_OPTION", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_OPTION", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteCountry(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Country_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "COUNTRY", "COUNTRY_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Country_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Country_idList.add(rs.getLong(1));
 			}
 			if (Country_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "COUNTRY_DESCRIPTION", "COUNTRY_ID", Country_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_ADDRESS", "BILLING_COUNTRY_ID", Country_idList);
 			totalDeleted += cascadeDeleteMerchant_store(conn, "COUNTRY_ID", Country_idList);
 			totalDeleted += cascadeDeleteOrders(conn, "BILLING_COUNTRY_ID", Country_idList);
 			totalDeleted += cascadeDeleteOrders(conn, "INVOICE_COUNTRY_ID", Country_idList);
 			totalDeleted += cascadeDeleteTax_rate(conn, "COUNTRY_ID", Country_idList);
 			totalDeleted += cascadeDeleteZone(conn, "COUNTRY_ID", Country_idList);
 			Country_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "COUNTRY", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("COUNTRY", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteMerchant_rank_profile(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Mchtrankprofile_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "MERCHANT_RANK_PROFILE", "MCHTRANKPROFILE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Mchtrankprofile_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Mchtrankprofile_idList.add(rs.getLong(1));
 			}
 			if (Mchtrankprofile_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteMerchant_store(conn, "MCHTRANKPROFILE_ID", Mchtrankprofile_idList);
 			Mchtrankprofile_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "MERCHANT_RANK_PROFILE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("MERCHANT_RANK_PROFILE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteSuborders(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Suborder_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "SUBORDERS", "SUBORDER_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Suborder_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Suborder_idList.add(rs.getLong(1));
 			}
 			if (Suborder_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteOrder_product(conn, "SUBORDER_ID", Suborder_idList);
 			Suborder_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "SUBORDERS", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("SUBORDERS", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT", "PRODUCT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_idList.add(rs.getLong(1));
 			}
 			if (Product_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "PRODUCT_ATTRIBUTE", "PRODUCT_ID", Product_idList);
 			totalDeleted += cascadeDeleteProduct_availability(conn, "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_BROWSW", "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_CATEGORY", "PRODUCT_ID", Product_idList);
 			totalDeleted += cascadeDeleteProduct_certificate(conn, "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_DESCRIPTION", "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_DIGITAL", "PRODUCT_ID", Product_idList);
 			totalDeleted += cascadeDeleteProduct_image(conn, "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_PROOF", "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_RELATIONSHIP", "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_RELATIONSHIP", "RELATED_PRODUCT_ID", Product_idList);
 			totalDeleted += cascadeDeleteProduct_review(conn, "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_SELFPROOF", "PRODUCT_ID", Product_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_THIRDPROOF", "PRODUCT_ID", Product_idList);
 			Product_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteContent(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Content_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "CONTENT", "CONTENT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Content_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Content_idList.add(rs.getLong(1));
 			}
 			if (Content_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "CONTENT_DESCRIPTION", "CONTENT_ID", Content_idList);
 			Content_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "CONTENT", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("CONTENT", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteCategory(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Category_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "CATEGORY", "PARENT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Category_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Category_idList.add(rs.getLong(1));
 			}
 			if (Category_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteCategory(conn, "PARENT_ID", Category_idList);
 			totalDeleted += executeDelete(conn, "CATEGORY_DESCRIPTION", "CATEGORY_ID", Category_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_CATEGORY", "CATEGORY_ID", Category_idList);
 			Category_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "CATEGORY", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("CATEGORY", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteSm_group(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Group_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "SM_GROUP", "GROUP_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Group_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Group_idList.add(rs.getLong(1));
 			}
 			if (Group_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "CUSTOMER_GROUP", "GROUP_ID", Group_idList);
 			totalDeleted += executeDelete(conn, "PERMISSION_GROUP", "GROUP_ID", Group_idList);
 			totalDeleted += executeDelete(conn, "USER_GROUP", "GROUP_ID", Group_idList);
 			Group_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "SM_GROUP", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("SM_GROUP", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteTax_class(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Tax_class_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "TAX_CLASS", "TAX_CLASS_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Tax_class_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Tax_class_idList.add(rs.getLong(1));
 			}
 			if (Tax_class_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteTax_rate(conn, "TAX_CLASS_ID", Tax_class_idList);
 			Tax_class_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "TAX_CLASS", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("TAX_CLASS", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteManufacturer(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Manufacturer_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "MANUFACTURER", "BRAND_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Manufacturer_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Manufacturer_idList.add(rs.getLong(1));
 			}
 			if (Manufacturer_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "AUTH_BRAND", "BRAND_ID", Manufacturer_idList);
 			totalDeleted += executeDelete(conn, "MANUFACTURER_DESCRIPTION", "MANUFACTURER_ID", Manufacturer_idList);
 			totalDeleted += cascadeDeleteProduct(conn, "MANUFACTURER_ID", Manufacturer_idList);
 			Manufacturer_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "MANUFACTURER", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("MANUFACTURER", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteOrders(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Order_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "ORDERS", "ORDER_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Order_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Order_idList.add(rs.getLong(1));
 			}
 			if (Order_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteOrder_account(conn, "ORDER_ID", Order_idList);
 			totalDeleted += executeDelete(conn, "ORDER_STATUS_HISTORY", "ORDER_ID", Order_idList);
 			totalDeleted += executeDelete(conn, "ORDER_TOTAL", "ORDER_ID", Order_idList);
 			totalDeleted += executeDelete(conn, "SM_TRANSACTION", "ORDER_ID", Order_idList);
 			totalDeleted += cascadeDeleteSuborders(conn, "ORDER_ID", Order_idList);
 			Order_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "ORDERS", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("ORDERS", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_option_value(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_option_value_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_OPTION_VALUE", "OPTION_VALUE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_option_value_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_option_value_idList.add(rs.getLong(1));
 			}
 			if (Product_option_value_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "PRODUCT_ATTRIBUTE", "OPTION_VALUE_ID", Product_option_value_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_OPTION_VALUE_DESCRIPTION", "PRODUCT_OPTION_VALUE_ID", Product_option_value_idList);
 			Product_option_value_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_OPTION_VALUE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_OPTION_VALUE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteLanguage(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Language_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "LANGUAGE", "LANGUAGE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Language_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Language_idList.add(rs.getLong(1));
 			}
 			if (Language_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteCustomer(conn, "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "CATEGORY_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "CONTENT_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "COUNTRY_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_OPT_VAL_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "CUSTOMER_OPTION_DESC", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "GEOZONE_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "MANUFACTURER_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "MERCHANT_LANGUAGE", "languages_LANGUAGE_ID", Language_idList);
 			totalDeleted += cascadeDeleteMerchant_store(conn, "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_IMAGE_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_OPTION_DESC", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_OPTION_VALUE_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_PRICE_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "PRODUCT_REVIEW_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "TAX_RATE_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			totalDeleted += cascadeDeleteUser(conn, "LANGUAGE_ID", Language_idList);
 			totalDeleted += executeDelete(conn, "ZONE_DESCRIPTION", "LANGUAGE_ID", Language_idList);
 			Language_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "LANGUAGE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("LANGUAGE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteUser(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> User_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "USER", "USER_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (User_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				User_idList.add(rs.getLong(1));
 			}
 			if (User_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "SYSTEM_NOTIFICATION", "USER_ID", User_idList);
 			totalDeleted += executeDelete(conn, "USER_GROUP", "USER_ID", User_idList);
 			totalDeleted += executeDelete(conn, "USERLOGIN", "USER_ID", User_idList);
 			User_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "USER", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("USER", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteProduct_certificate(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Product_certificate_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "PRODUCT_CERTIFICATE", "PRODUCT_CERTIFICATE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Product_certificate_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Product_certificate_idList.add(rs.getLong(1));
 			}
 			if (Product_certificate_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "PRODUCT_LITERATURE_PATH", "PRODUCT_CERTIFICATE_ID", Product_certificate_idList);
 			Product_certificate_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "PRODUCT_CERTIFICATE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("PRODUCT_CERTIFICATE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteZone(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Zone_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "ZONE", "BILLING_ZONE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Zone_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Zone_idList.add(rs.getLong(1));
 			}
 			if (Zone_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += executeDelete(conn, "CUSTOMER_ADDRESS", "BILLING_ZONE_ID", Zone_idList);
 			totalDeleted += cascadeDeleteMerchant_store(conn, "ZONE_ID", Zone_idList);
 			totalDeleted += cascadeDeleteOrders(conn, "BILLING_ZONE_ID", Zone_idList);
 			totalDeleted += cascadeDeleteOrders(conn, "INVOICE_ZONE_ID", Zone_idList);
 			totalDeleted += cascadeDeleteTax_rate(conn, "ZONE_ID", Zone_idList);
 			totalDeleted += executeDelete(conn, "ZONE_DESCRIPTION", "ZONE_ID", Zone_idList);
 			Zone_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "ZONE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("ZONE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteTax_rate(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Tax_rate_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "TAX_RATE", "PARENT_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Tax_rate_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Tax_rate_idList.add(rs.getLong(1));
 			}
 			if (Tax_rate_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteTax_rate(conn, "PARENT_ID", Tax_rate_idList);
 			totalDeleted += executeDelete(conn, "TAX_RATE_DESCRIPTION", "TAX_RATE_ID", Tax_rate_idList);
 			Tax_rate_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "TAX_RATE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("TAX_RATE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 	public int cascadeDeleteGeozone(Connection conn, String columnName, List<Long> ids) throws SQLException{
 		long startTs = System.currentTimeMillis();
 		List<Long> Geozone_idList = new ArrayList<Long>();
 		int totalDeleted = 0;
 		PreparedStatement st = prepareGetIdStatement(conn, "GEOZONE", "GEOZONE_ID", columnName, ids);
 		if (st == null){
 			return 0;
 		}
 		ResultSet rs = st.executeQuery();
 		while(true){
 			boolean hasMore = rs.next();
 			if (Geozone_idList.isEmpty() && !hasMore){
 				break;
 			}
 			if (hasMore){
 				Geozone_idList.add(rs.getLong(1));
 			}
 			if (Geozone_idList.size() < BATCH_SIZE && hasMore && !requiredStop()){
 				continue;
 			}
 			totalDeleted += cascadeDeleteCountry(conn, "GEOZONE_ID", Geozone_idList);
 			totalDeleted += executeDelete(conn, "GEOZONE_DESCRIPTION", "GEOZONE_ID", Geozone_idList);
 			Geozone_idList.clear();
 			if (requiredStop()){
 				break;
 			}
 		}
 		rs.close();
 		st.close();
 		int mineDeleted = executeDelete(conn, "GEOZONE", columnName, ids);
 		totalDeleted += mineDeleted;
 		long endTs = System.currentTimeMillis();
 		writeDeleteLog("GEOZONE", mineDeleted, totalDeleted, columnName, ids, startTs, endTs);
 		return totalDeleted;
 	}
 	
 }