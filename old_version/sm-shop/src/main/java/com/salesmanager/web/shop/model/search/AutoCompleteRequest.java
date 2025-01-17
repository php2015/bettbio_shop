package com.salesmanager.web.shop.model.search;

import org.json.simple.JSONObject;

public class AutoCompleteRequest {
	
	//private String collectionName;
	//private String query;
	//private String filter;
	private String merchantCode;
	private String languageCode;
	
	
	private final static String WILDCARD_QUERY = "wildcard";
	private final static String KEYWORD = "pname";
	private final static String PRODUCT = "product";
	private final static String UNDERSCORE = "_";
	private final static String ALL = "*";
	private final static String TYPE = "type";
	private final static String TYPE_PHRASE = "phrase_prefix";
	private final static String QUERY = "query";
	private final static String MATCH = "match";
	
	public AutoCompleteRequest(String merchantCode, String languageCode) {
		this.merchantCode = merchantCode;
		this.languageCode = languageCode;
	}

	@SuppressWarnings("unchecked")
	public String toJSONString(String query) {

		JSONObject keyword = new JSONObject();
		JSONObject q = new JSONObject();
		JSONObject match = new JSONObject();
		
		q.put(QUERY, query);
		q.put(TYPE, TYPE_PHRASE);
		
		keyword.put(KEYWORD, q);
		
		match.put(MATCH, keyword);
		
		//StringBuilder qValueBuilder = new StringBuilder();
		//qValueBuilder.append(query.toLowerCase()).append(ALL);
		
		//q.put(KEYWORD, qValueBuilder.toString());
		//wildcard.put(WILDCARD_QUERY, q);
		
		
		return match.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public String toJSONString(String query, String queryType) {
	
		if(queryType == null){
			
			return toJSONString(query);
		
		}
		JSONObject keyword = new JSONObject();
		JSONObject q = new JSONObject();
		JSONObject match = new JSONObject();
		
		q.put(QUERY, query);
		q.put(TYPE, TYPE_PHRASE);
		
		if(queryType.trim().equals("0")){
			keyword.put(KEYWORD, q);
		}
		if(queryType.trim().equals("1")){
			keyword.put("brandName", q);
		}
		if(queryType.trim().equals("2")){
			keyword.put("code", q);
		}
		if(queryType.trim().equals("3")){
			keyword.put("store", q);
		}
		
		
		match.put(MATCH, keyword);
		
		//StringBuilder qValueBuilder = new StringBuilder();
		//qValueBuilder.append(query.toLowerCase()).append(ALL);
		
		//q.put(KEYWORD, qValueBuilder.toString());
		//wildcard.put(WILDCARD_QUERY, q);
		
		
		return match.toJSONString();
	}
	
	/** keyword_en_default **/
	public String getCollectionName() {
		StringBuilder qBuilder = new StringBuilder();
		qBuilder.append(PRODUCT).append(UNDERSCORE).append(getLanguageCode()).append(UNDERSCORE)
		.append(getMerchantCode());
		
		return qBuilder.toString().toLowerCase();
	}
	
	

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

}
