package com.salesmanager.core.business.catalog.product.service;

import java.util.List;
import java.util.Map;

public interface ProductBatchDeletor {

	List<String> getUsedSqlNoteList() throws Exception;

	long getProductCountBySql(String sqlStatement)throws Exception;

	String getSqlByNote(String sqlNote) throws Exception;

	void doProductRemove(String sqlNote, String sqlStatement, Long totalCount) throws Exception;

	Map<String, Object> getStatisticData();

	void stopProductDeleting();

}
