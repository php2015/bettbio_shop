package com.salesmanager.core.business.search.model;
//package com.salesmanager.core.business.search.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*

from:

{
  "min_score": "0.3",
  "query": {
    "query_string": {
      "minimum_should_match": 1,
      "use_dis_max": true,
      "query": "乳酸脱氢酶细胞毒性检测试剂盒",
      "fields": [
        "pname^10",
        "penname^10",
        "keyword^20"
      ]
    }
  },
  "facets": {
    "category": {
      "terms": {
        "field": "category",
        "size": 2147483647
      }
    },
    "manufacturer": {
      "terms": {
        "field": "manufacturer",
        "size": 2147483647
      }
    }
  }
}


to: 

{
	  "query": {

	    "function_score": {
	      "functions": [
	        {
	          "script_score": {
	          "lang":"groovy",
	           "script_file": "qscore"
	          }
	        }
	      ],
	     "query":{"query_string":{"minimum_should_match":1,"use_dis_max":true,"query":"乳酸脱氢酶细胞毒性检测试剂盒","fields":["pname^10","penname^10","keyword^20"]}}
	    }
	  },
	  
	    "facets": {
	    "category": {
	      "terms": {
	        "field": "category",
	        "size": 2147483647
	      }
	    },
	    "manufacturer": {
	      "terms": {
	        "field": "manufacturer",
	        "size": 2147483647
	      }
	    }
	  }
	  
	  
	}'

*/

public class BettQueryEntry extends QueryEntry {
	
	
	private String min_score = "2.7";
	/*
	 * 
	 *  "script_score": {
	          "lang":"groovy",
	           "script_file": "qscore"
	          }
	 * 
	 * */
	protected JSONObject getScriptScoreBody()
	{
		JSONObject scriptScoreObject = new JSONObject();
		
		scriptScoreObject.put("lang", "groovy");
		scriptScoreObject.put("script_file", "qscore");
		
		return scriptScoreObject;
	}
	
	protected JSONObject getScriptScore()
	{
		JSONObject scriptScoreObject = new JSONObject();

		scriptScoreObject.put("script_score", getScriptScoreBody());
		
		return scriptScoreObject;
	}
	
	protected JSONArray getFunctions()
	{
		JSONArray scoreFunctionArray = new JSONArray();
		
		scoreFunctionArray.add(getScriptScore());
		
		return scoreFunctionArray;
	}
	
	protected JSONObject getFunctionScore()
	{
		JSONObject functionScoreObject = new JSONObject();
		
		functionScoreObject.put("functions", getFunctions());
		functionScoreObject.put("query", getBoolQueryObject());
		return functionScoreObject;
	}
	
	protected JSONObject getOutterQuery()
	{
		JSONObject outterQuery = new JSONObject();
		
		outterQuery.put("function_score", getFunctionScore());

		return outterQuery;
	}
	
	protected String getNewQuery(){
		return getNewQuery(this.min_score);
	}
	
	protected String getNewQuery(String minScore){
		JSONObject queryObject = new JSONObject();
		
		queryObject.put("query", getOutterQuery());
		
		queryObject.put("min_score", minScore);
		
		this.getFacet(queryObject);
		
		return queryObject.toJSONString();
	}	
	public String toSearchJson(){
		//JSONArray fields = new JSONArray();
		if(this.getQueryType()<=3){ //enable for product search and store search and code
			// changed by clariones, since brand search want exactly match
			if((getQueryType()==3 || getQueryType()==2 || getQueryType()==1)&&getOrderType()<=0){//this is store search and set min_score to lesser
				return getNewQuery("1.0");
				
			}
			if(this.getOrderType() <= 0){
				//
				return getNewQuery();
			}
			
		}
		
		return super.toSearchJson();
	}


	
}

