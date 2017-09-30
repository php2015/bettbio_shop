package com.salesmanager.core.business.catalog.product.service;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

public interface FullIndexService {

	//Connection getConnection();

	//void setConnection(Connection connection);

	FullIndexParams getParams();

	void setParams(FullIndexParams params);

	FullIndexCallback getCallback();

	void setCallback(FullIndexCallback calback);

	boolean startProcessing();

	void clearIndex();

	void setDataSource(DataSource datasource);

	boolean startIndexingByIds(List<Long> pIds, boolean autoAvailable);

}