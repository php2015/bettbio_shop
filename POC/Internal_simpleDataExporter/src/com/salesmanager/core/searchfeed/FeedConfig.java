package com.salesmanager.core.searchfeed;

import java.util.List;
import java.util.Map;

public class FeedConfig {
	protected String typeName;
	protected String processor;
	protected String host;
	protected String indexName;
	protected String querySql;
	protected String idFieldName;
	protected Map<String, String> outputMap;
	protected int batchSize;
	protected int workers;
	protected List<String> helpers;
	
	
	public List<String> getHelpers() {
		return helpers;
	}
	public void setHelpers(List<String> helpers) {
		this.helpers = helpers;
	}
	public int getWorkers() {
		return workers;
	}
	public void setWorkers(int workers) {
		this.workers = workers;
	}
	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	public String getIdFieldName() {
		return idFieldName;
	}
	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}
	public Map<String, String> getOutputMap() {
		return outputMap;
	}
	public void setOutputMap(Map<String, String> outputMap) {
		this.outputMap = outputMap;
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
