package com.salesmanager.core.searchfeed;

import java.util.Map;

public class FeedHelperConfig {
	protected String helperName;
	protected Boolean enable;
	protected String processor;
	protected Map<String, String> outputMap;
	protected String idFieldName;
	protected String inputFieldName;
	protected String querySql;
	protected Map<String, String> params;
	
	
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public Map<String, String> getOutputMap() {
		return outputMap;
	}
	public void setOutputMap(Map<String, String> outputMap) {
		this.outputMap = outputMap;
	}
	public String getIdFieldName() {
		return idFieldName;
	}
	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}
	public String getInputFieldName() {
		return inputFieldName;
	}
	public void setInputFieldName(String inputFieldName) {
		this.inputFieldName = inputFieldName;
	}
	public String getHelperName() {
		return helperName;
	}
	public void setHelperName(String helperName) {
		this.helperName = helperName;
	}
}
