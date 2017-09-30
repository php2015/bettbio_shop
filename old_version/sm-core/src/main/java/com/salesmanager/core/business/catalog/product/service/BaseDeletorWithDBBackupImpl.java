package com.salesmanager.core.business.catalog.product.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseDeletorWithDBBackupImpl implements ProductBatchDeletor {
	protected final int BATCH_SIZE = 1000;
	protected AtomicBoolean stopCommandFlag = new AtomicBoolean(false);
	private static final Map<String, String> backupTableMap = new Hashtable<String, String>();
	
	protected PreparedStatement prepareGetIdStatement(Connection conn, String tableName, String targetColumnName, String refColumnName, List<Long> refValues) throws SQLException {
		if (refValues == null || refValues.isEmpty()){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select ").append(targetColumnName).append(" from ").append(tableName).append(" where ").append(refColumnName).append(" in (");
		boolean isFirst = true;
		for(Long refValue : refValues){
			if (isFirst){
				isFirst = false;
			}else{
				sql.append(',');
			}
			sql.append('?');
		}
		sql.append(')');
		
		PreparedStatement st = conn.prepareStatement(sql.toString());
		st.setFetchSize(1000);
		for(int i=0;i<refValues.size();i++){
			Long refValue = refValues.get(i);
			st.setLong(i+1, refValue);
		}
//		System.out.println(sql.toString());
		return st;
	}

	protected int executeDelete(Connection conn, String tableName, String columnName, List<Long> columnValue) throws SQLException {
		if (columnValue == null || columnValue.isEmpty()){
			return 0;
		}
		long startTs = System.currentTimeMillis();
		String backTableName = getBackupTableName(conn, tableName);
		String sqlDropExistedBakcup = "delete from " + backTableName + " where " + columnName;
		PreparedStatement pst = null;
//		pst = conn.prepareStatement(sqlDropExistedBakcup);
//		for(Long value : columnValue){
//			pst.setLong(1, value);
//			pst.addBatch();
//		}
//		pst.executeBatch();
//		pst.close();
		
		String columnNames = BatchDeletorWithDBBackupConsts.tableColumnsMap.get(tableName.toUpperCase());
		String sqlMovetoBakcup = "insert into " + backTableName + "(" + columnNames +", REC_DELETED_TIME) select " + columnNames +  ",now() from " + tableName + " where " + columnName;
		pst = createStatementWithInClause(conn, sqlMovetoBakcup, columnValue);
		pst.execute();
		pst.close();
		
		sqlDropExistedBakcup = "delete from " + tableName + " where " + columnName;
		pst = createStatementWithInClause(conn, sqlDropExistedBakcup, columnValue);
		pst.execute();
		pst.close();
		
		long endTs = System.currentTimeMillis();
		writeRemoveLog(tableName, columnValue.size(), 0, columnName, null, startTs, endTs);
		return columnValue.size();
	}

	private PreparedStatement createStatementWithInClause(Connection conn, String sqlWithoutParam,
			List<Long> columnValue) throws SQLException {
		// columnValue already checked
		StringBuilder sb = new StringBuilder(sqlWithoutParam);
		sb.append(" in (");
		boolean isFirst = true;
		for(Long val : columnValue){
			if (isFirst){
				isFirst = false;
			}else{
				sb.append(',');
			}
			sb.append('?');
		}
		sb.append(')');
		PreparedStatement st = conn.prepareStatement(sb.toString());
		for(int i=0;i<columnValue.size();i++){
			st.setLong(i+1, columnValue.get(i));
		}
		return st;
	}

	protected int executeDelete_batch(Connection conn, String tableName, String columnName, List<Long> columnValue) throws SQLException {
		if (columnValue == null || columnValue.isEmpty()){
			return 0;
		}
		long startTs = System.currentTimeMillis();
		String backTableName = getBackupTableName(conn, tableName);
		String sqlDropExistedBakcup = "delete from " + backTableName + " where " + columnName +"=?";
		PreparedStatement pst = null;
//		pst = conn.prepareStatement(sqlDropExistedBakcup);
//		for(Long value : columnValue){
//			pst.setLong(1, value);
//			pst.addBatch();
//		}
//		pst.executeBatch();
//		pst.close();
		
		String columnNames = BatchDeletorWithDBBackupConsts.tableColumnsMap.get(tableName.toUpperCase());
		String sqlMovetoBakcup = "insert into " + backTableName + "(" + columnNames +") select " + columnNames +  " from " + tableName + " where " + columnName +"=?";
		pst = conn.prepareStatement(sqlMovetoBakcup);
		for(Long value : columnValue){
			pst.setLong(1, value);
			pst.addBatch();
		}
		pst.executeBatch();
		pst.close();
		
		sqlDropExistedBakcup = "delete from " + tableName + " where " + columnName +"=?";
		pst = conn.prepareStatement(sqlDropExistedBakcup);
		for(Long value : columnValue){
			pst.setLong(1, value);
			pst.addBatch();
		}
		pst.executeBatch();
		pst.close();
		long endTs = System.currentTimeMillis();
		writeRemoveLog(tableName, columnValue.size(), 0, columnName, null, startTs, endTs);
		return columnValue.size();
	}

	private String getBackupTableName(Connection conn, String tableName) {
		String backTblName = backupTableMap.get(tableName);
		if (backTblName != null){
			return backTblName;
		}
		String defBackupTblName = "BAK_"+tableName.toUpperCase();
		synchronized (backupTableMap){
			try {
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("show tables like '"+defBackupTblName+"'");
				if (rs != null && rs.next()){
					// already has the table
					backupTableMap.put(tableName, defBackupTblName);
					rs.close();
					st.close();
					return defBackupTblName;
				}
				// no bakcup table created yet.
				String ddl = BatchDeletorWithDBBackupConsts.tableDdlMap.get(tableName.toUpperCase());
				System.out.println("Create bakcup table: " + ddl);
				st.execute(ddl);
				backupTableMap.put(tableName, defBackupTblName);
				rs.close();
				st.close();
				return defBackupTblName;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Cannot create backup table for "+tableName+". Please check DB.");
	}

	protected boolean requiredStop() {
		return stopCommandFlag.get();
	}

	protected void writeDeleteLog(String tableName, int tableDeleted, int totalDeleted, String columnName, List<Long> ids, long startTs, long endTs) {
		System.out.println("Remove from " + tableName +": " + tableDeleted+"/"+totalDeleted +" totally used " + ((endTs-startTs)/1000.0) +" Seconds");
	}
	protected void writeRemoveLog(String tableName, int tableDeleted, int totalDeleted, String columnName, List<Long> ids, long startTs, long endTs) {
		System.out.println("  Drop from " + tableName +": " + tableDeleted+"/"+totalDeleted +" used " + ((endTs-startTs)/1000.0) +" Seconds");
	}

	protected void setStopFlag() {
		stopCommandFlag.set(true);
	}
	protected void clearStopFlag() {
		stopCommandFlag.set(false);
	}
	
/** 产品恢复使用的SQL
 * 注意顺序
insert into PRODUCT (PRODUCT_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, AVAILABLE, BATCHNUM, CAS, CODE, DATE_AVAILABLE, DATE_ChargeBegin, DATE_ChargeEnd, PRODUCT_FREE, QUANTITY_ORDERED, REVIEW_AVG, REVIEW_COUNT, QUALITY_SCORE, SKU, MANUFACTURER_ID, MERCHANT_ID, Parcel_ID, PRODUCT_TYPE_ID, SORT_ORDER, SPRICE, PRODUCT_DIAMOND) select PRODUCT_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, AVAILABLE, BATCHNUM, CAS, CODE, DATE_AVAILABLE, DATE_ChargeBegin, DATE_ChargeEnd, PRODUCT_FREE, QUANTITY_ORDERED, REVIEW_AVG, REVIEW_COUNT, QUALITY_SCORE, SKU, MANUFACTURER_ID, MERCHANT_ID, Parcel_ID, PRODUCT_TYPE_ID, SORT_ORDER, SPRICE, PRODUCT_DIAMOND from BAK_PRODUCT;
insert into PRODUCT_ATTRIBUTE (PRODUCT_ATTRIBUTE_ID, PRODUCT_ATTRIBUTE_DEFAULT, PRODUCT_ATTRIBUTE_DISCOUNTED, PRODUCT_ATTRIBUTE_FOR_DISP, PRODUCT_ATTRIBUTE_REQUIRED, PRODUCT_ATTRIBUTE_FREE, PRODUCT_ATRIBUTE_PRICE, PRODUCT_ATTRIBUTE_WEIGHT, PRODUCT_ATTRIBUTE_SORT_ORD, PRODUCT_ID, OPTION_ID, OPTION_VALUE_ID) select PRODUCT_ATTRIBUTE_ID, PRODUCT_ATTRIBUTE_DEFAULT, PRODUCT_ATTRIBUTE_DISCOUNTED, PRODUCT_ATTRIBUTE_FOR_DISP, PRODUCT_ATTRIBUTE_REQUIRED, PRODUCT_ATTRIBUTE_FREE, PRODUCT_ATRIBUTE_PRICE, PRODUCT_ATTRIBUTE_WEIGHT, PRODUCT_ATTRIBUTE_SORT_ORD, PRODUCT_ID, OPTION_ID, OPTION_VALUE_ID from BAK_PRODUCT_ATTRIBUTE;
insert into PRODUCT_AVAILABILITY (PRODUCT_AVAIL_ID, DATE_AVAILABLE, FREE_SHIPPING, QUANTITY, QUANTITY_ORD_MAX, QUANTITY_ORD_MIN, STATUS, REGION, REGION_VARIANT, PRODUCT_ID) select PRODUCT_AVAIL_ID, DATE_AVAILABLE, FREE_SHIPPING, QUANTITY, QUANTITY_ORD_MAX, QUANTITY_ORD_MIN, STATUS, REGION, REGION_VARIANT, PRODUCT_ID from BAK_PRODUCT_AVAILABILITY;
insert into PRODUCT_PRICE (PRODUCT_PRICE_ID, PRODUCT_PRICE_CODE, DEFAULT_PRICE, PRODUCT_PRICE_AMOUNT, PRODUCT_PRICE_PERIOD, PRODUCT_PRICE_SPECIAL_AMOUNT, PRODUCT_PRICE_SPECIAL_END_DATE, PRODUCT_PRICE_SPECIAL_ST_DATE, PRODUCT_PRICE_STOCK_AMOUNT, PRODUCT_PRICE_TYPE, PRODUCT_AVAIL_ID) select PRODUCT_PRICE_ID, PRODUCT_PRICE_CODE, DEFAULT_PRICE, PRODUCT_PRICE_AMOUNT, PRODUCT_PRICE_PERIOD, PRODUCT_PRICE_SPECIAL_AMOUNT, PRODUCT_PRICE_SPECIAL_END_DATE, PRODUCT_PRICE_SPECIAL_ST_DATE, PRODUCT_PRICE_STOCK_AMOUNT, PRODUCT_PRICE_TYPE, PRODUCT_AVAIL_ID from BAK_PRODUCT_PRICE;
insert into PRODUCT_PRICE_DESCRIPTION (DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, LANGUAGE_ID, PRODUCT_PRICE_ID) select DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, LANGUAGE_ID, PRODUCT_PRICE_ID from BAK_PRODUCT_PRICE_DESCRIPTION;
insert into PRODUCT_BROWSW (PRODUCT_BROWSE_ID, BROWSE_DATE, CUSTMOER_ID, PRODUCT_ID, IP) select PRODUCT_BROWSE_ID, BROWSE_DATE, CUSTMOER_ID, PRODUCT_ID, IP from BAK_PRODUCT_BROWSW;
insert into PRODUCT_CATEGORY (PRODUCT_ID, CATEGORY_ID) select PRODUCT_ID, CATEGORY_ID from BAK_PRODUCT_CATEGORY;
insert into PRODUCT_CERTIFICATE (PRODUCT_CERTIFICATE_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, BASEINFO, CERTIFICATE_IMAGE, DESCRIPTION, DOC_URL, SORT_ORDER, TITLE, BASEDATATYPE_ID, PRODUCT_ID) select PRODUCT_CERTIFICATE_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, BASEINFO, CERTIFICATE_IMAGE, DESCRIPTION, DOC_URL, SORT_ORDER, TITLE, BASEDATATYPE_ID, PRODUCT_ID from BAK_PRODUCT_CERTIFICATE;
insert into PRODUCT_LITERATURE_PATH (PRODUCT_LITERATURE_PATH_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, PATH, PRODUCT_CERTIFICATE_ID) select PRODUCT_LITERATURE_PATH_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, PATH, PRODUCT_CERTIFICATE_ID from BAK_PRODUCT_LITERATURE_PATH;
insert into PRODUCT_DESCRIPTION (DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, EN_NAME, META_DESCRIPTION, META_KEYWORDS, META_TITLE, METHOD_DESCRIPTION, DOWNLOAD_LNK, PRODUCT_HIGHLIGHT, SEF_URL, SIMPLE_DESCRIPTION, STORECOND_DESCRIPTION, TEST_DESCRIPTION, LANGUAGE_ID, PRODUCT_ID) select DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, EN_NAME, META_DESCRIPTION, META_KEYWORDS, META_TITLE, METHOD_DESCRIPTION, DOWNLOAD_LNK, PRODUCT_HIGHLIGHT, SEF_URL, SIMPLE_DESCRIPTION, STORECOND_DESCRIPTION, TEST_DESCRIPTION, LANGUAGE_ID, PRODUCT_ID from BAK_PRODUCT_DESCRIPTION;
insert into PRODUCT_DIGITAL (PRODUCT_DIGITAL_ID, FILE_NAME, PRODUCT_ID) select PRODUCT_DIGITAL_ID, FILE_NAME, PRODUCT_ID from BAK_PRODUCT_DIGITAL;
insert into PRODUCT_IMAGE (PRODUCT_IMAGE_ID, DEFAULT_IMAGE, IMAGE_CROP, IMAGE_TYPE, PRODUCT_IMAGE, PRODUCT_ID) select PRODUCT_IMAGE_ID, DEFAULT_IMAGE, IMAGE_CROP, IMAGE_TYPE, PRODUCT_IMAGE, PRODUCT_ID from BAK_PRODUCT_IMAGE;
insert into PRODUCT_IMAGE_DESCRIPTION (DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, ALT_TAG, LANGUAGE_ID, PRODUCT_IMAGE_ID) select DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, ALT_TAG, LANGUAGE_ID, PRODUCT_IMAGE_ID from BAK_PRODUCT_IMAGE_DESCRIPTION;
insert into PRODUCT_PROOF (PRODUCT_PROOF_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DATE_BUYED, DESCRIPTION, SORT_ORDER, RPOOF_IMAGE, TITLE, BASEDATATYPE_ID, PRODUCT_ID) select PRODUCT_PROOF_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DATE_BUYED, DESCRIPTION, SORT_ORDER, RPOOF_IMAGE, TITLE, BASEDATATYPE_ID, PRODUCT_ID from BAK_PRODUCT_PROOF;
insert into PRODUCT_RELATIONSHIP (PRODUCT_RELATIONSHIP_ID, ACTIVE, CODE, PRODUCT_ID, RELATED_PRODUCT_ID, MERCHANT_ID) select PRODUCT_RELATIONSHIP_ID, ACTIVE, CODE, PRODUCT_ID, RELATED_PRODUCT_ID, MERCHANT_ID from BAK_PRODUCT_RELATIONSHIP;
insert into PRODUCT_REVIEW (PRODUCT_REVIEW_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, REVIEW_DATE, REVIEWS_RATING, REVIEWS_READ, STATUS, CUSTOMERS_ID, ORDER_PRODUCT_ID, PRODUCT_ID) select PRODUCT_REVIEW_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, REVIEW_DATE, REVIEWS_RATING, REVIEWS_READ, STATUS, CUSTOMERS_ID, ORDER_PRODUCT_ID, PRODUCT_ID from BAK_PRODUCT_REVIEW;
insert into PRODUCT_REVIEW_DESCRIPTION (DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, LANGUAGE_ID, PRODUCT_REVIEW_ID) select DESCRIPTION_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, NAME, TITLE, LANGUAGE_ID, PRODUCT_REVIEW_ID from BAK_PRODUCT_REVIEW_DESCRIPTION;
insert into PRODUCT_SELFPROOF (PRODUCT_SELFPROOF_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, SORT_ORDER, SELFPROOF_IMAGE, TITLE, PRODUCT_ID, SELFPROOF_TYPE) select PRODUCT_SELFPROOF_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, SORT_ORDER, SELFPROOF_IMAGE, TITLE, PRODUCT_ID, SELFPROOF_TYPE from BAK_PRODUCT_SELFPROOF;
insert into PRODUCT_THIRDPROOF (PRODUCT_THIRDPROOF_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, SORT_ORDER, THIRDPROOF_IMAGE, TITLE, BASEDATATYPE_ID, PRODUCT_ID) select PRODUCT_THIRDPROOF_ID, PRODUCT_AUDIT, DATE_CREATED, DATE_MODIFIED, UPDT_ID, DESCRIPTION, SORT_ORDER, THIRDPROOF_IMAGE, TITLE, BASEDATATYPE_ID, PRODUCT_ID from BAK_PRODUCT_THIRDPROOF;
**/

}
