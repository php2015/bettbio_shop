package com.salesmanager.core.business.search.model;



import java.util.List;



import org.json.simple.JSONArray;

import org.json.simple.JSONAware;

import org.json.simple.JSONObject;







public class QueryEntry implements JSONAware {

	private String query;

	private int queryType = 0;//查询

	private int orderType = -1;//排序

	//用于聚合统计

	private List<Long> categories;

	private List<Long> manufacturer;

	private List<Long> qualitys;

	private boolean certicate =false;

	private boolean proof = false;

	private boolean third = false;

	//用于分类

	private List<Long> category ;

	private String min_score = "0.3";

	public String getQuery() {

		return query;

	}

	public void setQuery(String query) {

		this.query = query;

	}

	public List<Long> getCategories() {

		return categories;

	}

	public void setCategories(List<Long> categories) {

		this.categories = categories;

	}

	public List<Long> getManufacturer() {

		return manufacturer;

	}

	public void setManufacturer(List<Long> manufacturer) {

		this.manufacturer = manufacturer;

	}

	public List<Long> getQualitys() {

		return qualitys;

	}

	public void setQualitys(List<Long> qualitys) {

		this.qualitys = qualitys;

	}

	

	protected String getChineseBlankEnglish(){

		 StringBuffer sBuffer = new StringBuffer();

		 boolean preChina = this.query.substring(0,1).matches("[\u4E00-\u9FA5]"); 

		 sBuffer.append(this.query.substring(0,1));

		 for (int i = 1; i < this.query.length(); i++) {

			 String retContent = this.query.substring(i, i + 1);

			 boolean curChina = retContent.matches("[\u4E00-\u9FA5]");

			 if(curChina != preChina){

				 sBuffer.append(" ");

			 }

			 preChina = curChina;

			 sBuffer.append(retContent);

		 }

		 return sBuffer.toString();

	}

	

	public  String toCategoryJson(){

		//JSONObject valueObj = new JSONObject();

		//JSONArray cats = new JSONArray();

		//cats.add("1");

		//cats.add("5");

		if(this.category !=null && this.category.size()>0){

			JSONObject cateObj = new JSONObject();

			cateObj.put("category", this.category) ;

			JSONObject termObj = new JSONObject();

			termObj.put("terms", cateObj);

			JSONObject filterObj = new JSONObject();

			filterObj.put("filter", termObj);

			JSONObject filteredObj = new JSONObject();

			filteredObj.put("filtered", filterObj);

			JSONObject queryObj = new JSONObject();

			queryObj.put("query",filteredObj);

			queryObj=this.getSort(queryObj);

			queryObj = this.getFacet(queryObj);

			return queryObj.toJSONString(); 

		}

		//valueObj.put("boost", 10);

		//valueObj.put("value", "8");

		

		return null;

	}

	

	public String toSearchJson(){

		

		

		//简单描述不加入查询条件 add by sam on 160519

		//fields.add("simpledesc^7");

		//fields.add("tags");

		

		

		



		JSONObject obj = new JSONObject();

		obj.put("query", getQueryObject());

		obj.put("min_score", min_score);

		

		obj=this.getSort(obj);

		

		obj = this.getFacet(obj);

		

		return obj.toJSONString();

	}

	protected JSONObject getQueryObject(){

		

		

		JSONObject queryObj = new JSONObject();

		queryObj.put("query_string", getQueryStringObject());

		return queryObj;

		

	}

	

	

	protected JSONObject getQueryStringObject(){

		JSONObject queryStringObj = new JSONObject();

		JSONArray fields = new JSONArray();

		if(this.queryType==0){

			fields.add("pname^20");

			fields.add("penname^1");

		}else if(this.queryType==1){

			fields.add("store^10");

		}else if(this.queryType==2){

			fields.add("code^7");

		}

		//fields.add("keyword^20");

		

		String queryParm = this.getChineseBlankEnglish().trim();

		

		queryStringObj.put("fields", fields);

		queryStringObj.put("query", queryParm);

		queryStringObj.put("use_dis_max", true);

		queryStringObj.put("minimum_should_match", 1);

		

		return queryStringObj;

		

	}

	

	

	

	@SuppressWarnings("unchecked")

	protected JSONObject getSort(JSONObject obj){

		//JSONArray fields = new JSONArray();

		//fields.add("quality");

		//fields.add("desc");

		//JSONObject sort = new JSONObject();

		//sort.put("sort", fields);obj

		JSONObject orderDesc = new JSONObject();

		orderDesc.put("order", "desc");



		JSONObject orderAsc = new JSONObject();

		orderAsc.put("order", "asc");

		

		JSONObject filed = new JSONObject();

		filed.put("quality", orderDesc);

		

		if(this.orderType == 0){

			//filed.put("quality", order);

		}else if(this.orderType == 1){

			filed.put("period", orderDesc);

		}else if(this.orderType == 2){

			//filed.put("quality", order);

		}else if(this.orderType == 3){

			filed.put("price", orderDesc);

		}

		

		obj.put("sort", filed);

		return obj;

	}

	protected JSONObject getFacet(JSONObject obj){

		//facet

		JSONObject facets = new JSONObject();

		JSONObject filterObj = new JSONObject();

		JSONObject fTermObj = new JSONObject();

		fTermObj.put("field", "category");

		fTermObj.put("size", Integer.MAX_VALUE);

		JSONObject fCate = new JSONObject();

		fCate.put("terms", fTermObj);

		

		

		JSONObject mTermObj = new JSONObject();

		mTermObj.put("field", "manufacturer");

		mTermObj.put("size", Integer.MAX_VALUE);

		JSONObject mCate = new JSONObject();

		mCate.put("terms", mTermObj);

		

		

		JSONObject term = new JSONObject();

		JSONObject catFacetTerm = new JSONObject();

		JSONObject manFacetTerm = new JSONObject();

		JSONArray existFields = new JSONArray();

		JSONObject catFacetFilter = new JSONObject();

		JSONObject manFacetFilter = new JSONObject();

		

		if(categories != null && categories.size()>0){

			term.put("category", categories);

			catFacetTerm.put("category",categories);

			manFacetTerm.put("category",categories);

		}

				

		

		if(manufacturer != null && manufacturer.size()>0){			

			term.put("manufacturer", manufacturer);

			manFacetTerm.put("manufacturer", manufacturer);

			catFacetTerm.put("manufacturer", manufacturer);

		}

		

		if(certicate == true) existFields.add("certificates");

		

		if(proof == true) existFields.add("proofs");

		

		if(third == true) existFields.add("thirdproofs");

		

		if(existFields !=null && existFields.size()>0){

			JSONObject exsitFiles = new JSONObject();

			exsitFiles.put("field", existFields);

			filterObj.put("exists", exsitFiles);

			catFacetFilter.put("exists", exsitFiles);

			manFacetFilter.put("exists", exsitFiles);

		}

		

		if(catFacetTerm != null && catFacetTerm.size()>0 ) catFacetFilter.put("terms", catFacetTerm);

		

		if(manFacetTerm !=null && manFacetTerm.size()>0) manFacetFilter.put("terms", manFacetTerm);

		

		if(term != null && term.size()>0)filterObj.put("terms", term);

		

		if(filterObj !=null && filterObj.size()>0) obj.put("post_filter", filterObj);

		

		if(catFacetFilter!= null && catFacetFilter.size()>0)fCate.put("facet_filter", catFacetFilter);

		if(manFacetFilter !=null && manFacetFilter.size()>0)mCate.put("facet_filter", manFacetFilter);

				

		facets.put("category",fCate);

		facets.put("manufacturer",mCate);

		obj.put("facets", facets);

		return obj;

	}

	

	protected JSONObject getCateFacet(JSONObject obj){

		//facet

		JSONObject facets = new JSONObject();

		JSONObject filterObj = new JSONObject();

		JSONObject fTermObj = new JSONObject();

		fTermObj.put("field", "category");

		fTermObj.put("size", Integer.MAX_VALUE);

		JSONObject fCate = new JSONObject();

		fCate.put("terms", fTermObj);

		

		

		JSONObject mTermObj = new JSONObject();

		mTermObj.put("field", "manufacturer");

		mTermObj.put("size", Integer.MAX_VALUE);

		JSONObject mCate = new JSONObject();

		mCate.put("terms", mTermObj);

		

		

		JSONObject term = new JSONObject();

		JSONObject catFacetTerm = new JSONObject();

		JSONObject manFacetTerm = new JSONObject();

		JSONArray existFields = new JSONArray();

		JSONObject catFacetFilter = new JSONObject();

		JSONObject manFacetFilter = new JSONObject();

		

		if(categories != null && categories.size()>0){

			term.put("category", category);

			catFacetTerm.put("category",category);

			manFacetTerm.put("category",category);

		}

				

		

		if(manufacturer != null && manufacturer.size()>0){			

			term.put("manufacturer", manufacturer);

			manFacetTerm.put("manufacturer", manufacturer);

			catFacetTerm.put("manufacturer", manufacturer);

		}

		

		if(certicate == true) existFields.add("certificates");

		

		if(proof == true) existFields.add("proofs");

		

		if(third == true) existFields.add("thirdproofs");

		

		if(existFields !=null && existFields.size()>0){

			JSONObject exsitFiles = new JSONObject();

			exsitFiles.put("field", existFields);

			filterObj.put("exists", exsitFiles);

			catFacetFilter.put("exists", exsitFiles);

			manFacetFilter.put("exists", exsitFiles);

		}

		

		if(catFacetTerm != null && catFacetTerm.size()>0 ) catFacetFilter.put("terms", catFacetTerm);

		

		if(manFacetTerm !=null && manFacetTerm.size()>0) manFacetFilter.put("terms", manFacetTerm);

		

		if(term != null && term.size()>0)filterObj.put("terms", term);

		

		if(filterObj !=null && filterObj.size()>0) obj.put("post_filter", filterObj);

		

		if(catFacetFilter!= null && catFacetFilter.size()>0)fCate.put("facet_filter", catFacetFilter);

		if(manFacetFilter !=null && manFacetFilter.size()>0)mCate.put("facet_filter", manFacetFilter);

				

		facets.put("category",fCate);

		facets.put("manufacturer",mCate);

		obj.put("facets", facets);

		return obj;

	}

	

	@Override

	public String toJSONString() {

		// TODO Auto-generated method stub

		//query	

		if(category !=null && category.size()>0){

			return toCategoryJson();

		}else{

			return toSearchJson();

		}

		

	}

	public boolean isCerticate() {

		return certicate;

	}

	public void setCerticate(boolean certicate) {

		this.certicate = certicate;

	}

	public boolean isProof() {

		return proof;

	}

	public void setProof(boolean proof) {

		this.proof = proof;

	}

	public boolean isThird() {

		return third;

	}

	public void setThird(boolean third) {

		this.third = third;

	}

	public List<Long> getCategory() {

		return category;

	}

	public void setCategory(List<Long> category) {

		this.category = category;

	}

	

	public int getOrderType() {

		return orderType;

	}

	

	public void setOrderType(int orderType) {

		this.orderType = orderType;

	}

	

	public int getQueryType() {

		return queryType;

	}

	

	public void setQueryType(int queryType) {

		this.queryType = queryType;

	}

}


