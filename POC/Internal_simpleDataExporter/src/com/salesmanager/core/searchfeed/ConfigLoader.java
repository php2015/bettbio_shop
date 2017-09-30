package com.salesmanager.core.searchfeed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {

	private Properties props;

	public void setProps(Properties orignalProps) {
		this.props = orignalProps;
	}

	public List<String> getStringList(String name) {
		List<String> result=new ArrayList<String>();
		String value = props.getProperty(name);
		if (value == null || value.isEmpty()){
			return result;
		}
		String[] list = value.split(",");
		for(String str: list){
			result.add(str.trim());
		}
		return result;
	}

	public FeedConfig getFeedCfg(String type) {
		FeedConfig cfg = new FeedConfig();
		String keyProcessor = type+".processor";
		String keyElasticHost = type+".elasticsearch.host";
		String keyElasticIndex = type+".elasticsearch.index";
		cfg.setProcessor(getString(keyProcessor));
		cfg.setHost(getString(keyElasticHost));
		cfg.setIndexName(getString(keyElasticIndex));
		cfg.setTypeName(type);
		cfg.setQuerySql(getString(type+".sql.query"));
		cfg.setOutputMap(getMapping(type+".output.map."));
		cfg.setIdFieldName(getString(type+".output.idfield"));
		cfg.setBatchSize(getInt(type+".batch.size"));
		cfg.setWorkers(getInt(type+".batch.workers"));
		cfg.setHelpers(getStringList(type+".helpers"));
		return cfg;
	}

	private String getString(String name) {
		return props.getProperty(name);
	}

	public FeedHelperConfig getHelperCfg(String typeName, String helperName) {
		FeedHelperConfig cfg = new FeedHelperConfig();
		String namePrefix = typeName+".helper."+helperName+".";
		cfg.setHelperName(helperName);
		cfg.setEnable(getBoolean(namePrefix+"enable"));
		cfg.setProcessor(getString(namePrefix+"processor"));
		cfg.setIdFieldName(getString(namePrefix+"idfield"));
		cfg.setInputFieldName(getString(namePrefix+"inputfield"));
		cfg.setOutputMap(getMapping(namePrefix+"output.map."));
		cfg.setQuerySql(getString(namePrefix+"sql.query"));
		cfg.setParams(getMapping(namePrefix+"param."));
		return cfg;
	}

	private Map<String, String> getMapping(String prefix) {
		Map<String, String> result = new HashMap<String, String>();
		for(Object name : props.keySet()){
			String key = (String) name;
			if (key.startsWith(prefix)){
				result.put(key.substring(prefix.length()), props.getProperty(key));
			}
		}
		return result;
	}

	private Boolean getBoolean(String name) {
		String value = props.getProperty(name);
		if (value == null){
			return null;
		}
		return Boolean.valueOf(value);
	}
	private Integer getInt(String name) {
		String value = props.getProperty(name);
		if (value == null){
			return null;
		}
		return Integer.valueOf(value);
	}

	public String getDbDriver() {
		return getString("db_jdbc_driver");
	}

	public String getDbUrl() {
		return getString("db_url");
	}

	public String getDbUser() {
		return getString("db_username");
	}

	public String getDbPassword() {
		return getString("db_password");
	}

}
