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
	
	
	private String min_score = "0.3";
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
		functionScoreObject.put("query", getQueryObject());
		return functionScoreObject;
	}
	
	protected JSONObject getOutterQuery()
	{
		JSONObject outterQuery = new JSONObject();
		
		outterQuery.put("function_score", getFunctionScore());

		return outterQuery;
	}
	
	protected String getNewQuery(){
		JSONObject queryObject = new JSONObject();
		
		queryObject.put("query", getOutterQuery());
		//queryObject.put("facets", this.getFacet(queryObject));
		
		
		this.getFacet(queryObject);
		
		return queryObject.toJSONString();
	}
	
	public String toSearchJson(){
		//JSONArray fields = new JSONArray();
		if(this.getQueryType()<=2){ //enable for product search and store search and code
			
			if(this.getOrderType() <= 0){
				//
				return getNewQuery();
			}
			
		}
		
		return super.toSearchJson();
	}
	
}

