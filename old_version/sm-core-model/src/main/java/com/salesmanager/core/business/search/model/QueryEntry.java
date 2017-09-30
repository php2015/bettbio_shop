package com.salesmanager.core.business.search.model;



import java.util.List;

import java.util.Arrays;


import org.json.simple.JSONArray;

import org.json.simple.JSONAware;

import org.json.simple.JSONObject;







public class QueryEntry implements JSONAware {


	public static void main(String args[]){

		QueryEntry q=new BettQueryEntry();
		q.setQuery("CCK-8");
		q.setQueryType(0);

		System.out.println("Hello World!: "+q.toSearchJson());

	}

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

	private String min_score = "2.5";

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

		
/*

             "query":{
             
             
				"bool":{

    "must":{
				"query_string":{"minimum_should_match":1,"use_dis_max":true,"query":"CCK-8","fields":["pname^10","penname^10","keyword^20"]}
                
    }
    ,"must_not":{"terms":{"category":[15150,15159,15160,15161,15162,15163,15164,15165,15166,15167,15168,15169,15170,15171,15172,15173,15174,15175,15176,15177,15178,15179,15151,15180,15181,15152,15182,15183,15184,15185,15186,15187,15153,15188,15189,15190,15191,15192,15193,15194,15195,15196,15197,15198,15199,15154,15200,15201,15202,15203,15204,15205,15206,15207,15208,15209,15155,15210,15211,15212,15213,15214,15215,15216,15217,15218,15219,15220,15221,15222,15223,15224,15225,15226,15227,15156,15228,15229,15230,15231,15232,15233,15234,15235,15236,15237,15238,15239,15240,15241,15242,15157,15243,15244,15158,15250,15251,3]}
                }}
				
   }

*/
		


		JSONObject obj = new JSONObject();

		//if want to include all queries accross all categories, ucomment here obj.put("query", getQueryObject()); 

		obj.put("query", getBoolQueryObject()); //this statment elimilate the categories belongs to instruments
		obj.put("min_score", min_score);

		

		obj=this.getSort(obj);

		

		obj = this.getFacet(obj);

		

		return obj.toJSONString();

	}

	protected JSONObject getBoolObject(){

		JSONObject obj = new JSONObject();
		Object mustObj = getMustObject();
		// comment out below because require un-term search again.
/*		if (this.getQueryType() == 1){
			JSONObject brandTermMust = new JSONObject();
			JSONObject brandTerm = new JSONObject();
			brandTerm.put("brandName", this.getChineseBlankEnglish().trim());
			brandTermMust.put("term", brandTerm);
			//JSONArray mustArray = new JSONArray();
			//mustArray.add(brandTermMust);
			//mustArray.add(mustObj);
			obj.put("must",brandTermMust);
		}else {
			obj.put("must",mustObj);
		}*/
		obj.put("must",mustObj);
		
		// remove below because we don't need care about Baidu&360 filter any more
		//obj.put("must_not",getMustNotObject());
		
		return obj;



	}
	protected JSONObject getBoolQueryObject(){

		JSONObject obj = new JSONObject();
		obj.put("bool",getBoolObject());


		return obj;



	}
	protected JSONObject getMustObject(){
		return getQueryObject();


	}
	protected JSONObject getMustNotObject(){

		JSONObject obj = new JSONObject();
		obj.put("terms",getTermsObject());
		return obj;

	}
	protected JSONObject getTermsObject(){

		JSONObject obj = new JSONObject();
		obj.put("category",getCategoryObject());
		return obj;

	}

	protected JSONArray getCategoryObject(){
		//this is for instrument categories ids!
		JSONArray obj = new JSONArray();
		int [] excludeCategories = {15150,15159,15160,15161,15162,15163,15164,15165,15166,15167,15168,15169,15170,15171,15172,15173,15174,15175,15176,15177,15178,15179,15151,15180,15181,15152,15182,15183,15184,15185,15186,15187,15153,15188,15189,15190,15191,15192,15193,15194,15195,15196,15197,15198,15199,15154,15200,15201,15202,15203,15204,15205,15206,15207,15208,15209,15155,15210,15211,15212,15213,15214,15215,15216,15217,15218,15219,15220,15221,15222,15223,15224,15225,15226,15227,15156,15228,15229,15230,15231,15232,15233,15234,15235,15236,15237,15238,15239,15240,15241,15242,15157,15243,15244,15158,15250,15251,3};
		//obj.addAll(Arrays.asList(excludeCategories));//may be compile error here!
		for(int i:excludeCategories){
			obj.add(i);
		}
		//may be compile error here!

		return obj;

	}


	protected JSONObject getQueryObject(){

		
		JSONObject queryObj = new JSONObject();

		queryObj.put("query_string", getQueryStringObject());

		return queryObj;

		

	}

	protected boolean isCASCode(String queryString)
	{
		
		if(queryString.matches("^\\d{2,10}-\\d{2}-\\d{1}$")){
			return true;
				
		}
		return false;
		
	}
	protected JSONArray getQueryFields(String queryString){
			
		JSONArray fields = new JSONArray();
		 if(this.queryType==3){

			fields.add("store^10");
			return fields;

		}
		if(this.queryType==2){

			fields.add("code^7");
			return fields;

		}
		if(this.queryType==1){

			fields.add("brandName^10");
			return fields;

		}
		
		if(isCASCode(queryString)){
			
			fields.add("cas^10");
			return fields; 
		}
		
		//for default queries
		fields.add("pname^20");
		fields.add("penname^10");
		return fields;
		
		
	}
	

	protected JSONObject getQueryStringObject(){

		JSONObject queryStringObj = new JSONObject();

		

		

		//fields.add("keyword^20");

		

		String queryParm = this.getChineseBlankEnglish().trim();
		JSONArray fields = getQueryFields(queryParm);

		

		queryStringObj.put("fields", fields);

		queryStringObj.put("query", queryParm);

		queryStringObj.put("use_dis_max", true);

		if (getQueryType() == 1 || getQueryType() == 2){
			queryStringObj.put("minimum_should_match", queryParm.length());
		}else{
			queryStringObj.put("minimum_should_match", 1);
		}
		//queryStringObj.put("minimum_should_match", 1);
		

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

		

		//JSONObject filed = new JSONObject();
		JSONArray field = new JSONArray();

		JSONObject data;
		
		//filed.put("quality", orderDesc);
		// hard code, let diamond always as sort criteria
		//filed.put("diamond", orderDesc);

		

		if(this.orderType == 0){

			//filed.put("quality", order);

		}else if(this.orderType == 1){

			//filed.put("period", orderDesc);
			data = new JSONObject();
			data.put("period", orderDesc);
			field.add(data);

		}else if(this.orderType == 2){

			//filed.put("quality", order);

		}else if(this.orderType == 3){

			//filed.put("price", orderDesc);
			data = new JSONObject();
			data.put("price", orderDesc);
			field.add(data);

		}
		data = new JSONObject();
		data.put("diamond", orderDesc);
		field.add(data);

		data = new JSONObject();
		data.put("quality", orderDesc);
		field.add(data);
		
		obj.put("sort", field);

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


