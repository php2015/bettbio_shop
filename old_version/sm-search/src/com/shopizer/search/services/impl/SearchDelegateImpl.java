package com.shopizer.search.services.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.facet.Facets;

import com.shopizer.search.services.IndexKeywordRequest;
import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;
import com.shopizer.search.services.field.BooleanField;
import com.shopizer.search.services.field.DateField;
import com.shopizer.search.services.field.DoubleField;
import com.shopizer.search.services.field.Field;
import com.shopizer.search.services.field.IntegerField;
import com.shopizer.search.services.field.ListField;
import com.shopizer.search.services.field.LongField;
import com.shopizer.search.services.field.StringField;
import com.shopizer.search.utils.SearchClient;

@SuppressWarnings("deprecation")
public class SearchDelegateImpl implements SearchDelegate {

	private SearchClient searchClient = null;
	public SearchClient getSearchClient() {
		return searchClient;
	}

	public void setSearchClient(SearchClient searchClient) {
		this.searchClient = searchClient;
	}

	private static Logger log = Logger.getLogger(SearchDelegateImpl.class);
	

	
	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#indexExist(java.lang.String)
	 */
	@Override
	public boolean indexExist(String indexName) throws Exception {
		Client client = searchClient.getClient();
		
		IndicesExistsRequestBuilder indiceRequestBuilder = client.admin().indices().prepareExists(indexName);
		IndicesExistsResponse indiceResponse = indiceRequestBuilder.execute().actionGet(); 
		return indiceResponse.isExists();
		
	}
	
	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#createIndice(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createIndice(String mapping,String settings,String collection,String object) throws Exception {
		
	
		
		Client client = searchClient.getClient();

		//maintain a list of created index
		
		CreateIndexRequest indexRequest = new CreateIndexRequest(collection);
		if(mapping!=null) {
			indexRequest.mapping(object, mapping);
		}
		if(settings!=null) {
			indexRequest.settings(settings);
		}

		client.admin().indices().  
	    create(indexRequest).
	    actionGet();

	}
	
	
    /* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#index(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void index(String json, String collection, String object, String id) {
		

		
		Client client = searchClient.getClient();

        @SuppressWarnings("unused")
		IndexResponse r = client.prepareIndex(collection, object, id) 
        .setSource(json) 
        .execute() 
        .actionGet();
		
	}
	
	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#delete(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String collection, String object, String id) throws Exception {
		
		if(this.indexExist(collection)) {
		
			Client client = searchClient.getClient();
	
			@SuppressWarnings("unused")
			DeleteResponse r = client.prepareDelete(collection, object, id) 
	        .execute() 
	        .actionGet();
		
		}
		

	}
	
	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#bulkDeleteIndex(java.util.Collection, java.lang.String)
	 */
	@Override
	public void bulkDeleteIndex(Collection<String> ids, String collection) throws Exception {
		

		if(this.indexExist(collection)) {
		
			Client client = searchClient.getClient();
			
			if(ids!=null && ids.size()>0) {
				
				BulkRequestBuilder bulkRequest = client.prepareBulk();
				
				for(String s : ids) {
					
					
					DeleteRequest dr = new DeleteRequest();
					dr.type("keyword").index(collection).id(s);
					
					//System.out.println(dr.toString());
					
					bulkRequest.add(dr);
					
				}
				
				BulkResponse bulkResponse = bulkRequest.execute().actionGet(); 
				if (bulkResponse.hasFailures()) { 
				    // process failures by iterating through each bulk response item 
					System.out.println("has failures");
				}
				
			}
		}
		

	}
	

	
	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#bulkIndexKeywords(java.util.Collection, java.lang.String, java.lang.String)
	 */
	@Override
	public void bulkIndexKeywords(Collection<IndexKeywordRequest> bulks, String collection, String object) {
		
		
		try {
			

			Client client = searchClient.getClient();
			BulkRequestBuilder bulkRequest = client.prepareBulk(); 
			
			//@todo, index in appropriate Locale
			for(IndexKeywordRequest key : bulks) {
			
				// either use client#prepare, or use Requests# to directly build index/delete requests 
				//bulkRequest.add(client.prepareIndex(collection, object, key.getId()) 
				
				String id = key.getKey();
				if(id.length()>25) {
					id = id.substring(0,25);
				}
				id = id.trim().toLowerCase();
				
				XContentBuilder b = jsonBuilder() 
                .startObject() 
                	.field("id", id)//index name is the value trimed and lower cased
                    .field("keyword", key.getKey())
                    .field("_id_", key.getId());
                    
                    @SuppressWarnings("rawtypes")
					Collection fields = key.getFilters();
                    if(fields.size()>0) {
                    	
                    	for(Object o : fields) {
                    		
                    		if(o instanceof BooleanField) {
                    			
                    			Boolean val = ((BooleanField)o).getValue();
                    			b.field(((Field)o).getName(), val.booleanValue());
                    			
                    		} else if(o instanceof IntegerField) {
                    			
                    			Integer val = ((IntegerField)o).getValue();
                    			b.field(((Field)o).getName(), val.intValue());
                    			
                    			
                    		} else if(o instanceof LongField) {
                    			
                    			Long val = ((LongField)o).getValue();
                    			b.field(((Field)o).getName(), val.longValue());
                    			
                    			
                    		} else if(o instanceof ListField) {
                    			
                    			@SuppressWarnings("rawtypes")
								List val = ((ListField)o).getValue();
                    			b.field(((Field)o).getName(), val);
                    			
                    		} else if(o instanceof DoubleField) {
                    			
                    			Double val = ((DoubleField)o).getValue();
                    			b.field(((Field)o).getName(), val.doubleValue());
                    			
                    		} else if(o instanceof DateField) {
                    			
                    			Date val = ((DateField)o).getValue();
                    			b.field(((Field)o).getName(), val);
                    			
                    		} else {
                    			
                    			String val = ((StringField)o).getValue();
                    			b.field(((Field)o).getName(), val);
                    			
                    		}
                    	}
                    }
                    
                b.endObject();

				
				bulkRequest.add(client.prepareIndex(collection, object).setSource(b));
			}
			 
			log.debug("Adding to collection " + collection);
			         
			BulkResponse bulkResponse = bulkRequest.execute().actionGet(); 
			if (bulkResponse.hasFailures()) { 
			    // process failures by iterating through each bulk response item 
				System.out.println("Has failures");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#getObject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public com.shopizer.search.services.GetResponse getObject(String collection, String object, String id) throws Exception {
		
		
	
		
		/**
		 * This method throws an exception
		 */
		GetResponse response = searchClient.getClient().prepareGet(collection, object, id)
		.setOperationThreaded(true) 
		.setFields("_source")
        .execute() 
        .actionGet();
				
		
		com.shopizer.search.services.GetResponse r = null;
		if(response!=null) {
			
			r = new com.shopizer.search.services.GetResponse(response);
			
		}
		
		return r;
		
	}
	
	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#search(com.shopizer.search.services.SearchRequest)
	 */
	@Override
	public SearchResponse search(SearchRequest request) throws Exception {

		SearchResponse response = new SearchResponse();
		try {
			
			
			org.elasticsearch.action.search.SearchRequestBuilder builder = searchClient.getClient().prepareSearch(request.getCollection())
	        //.setQuery("{ \"term\" : { \"keyword\" : \"dynamic\" }}")
			//.setQuery(request.getJson())
			.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			.addHighlightedField("pname")
			//.addHighlightedField("simpledesc")
			//.addHighlightedField("tags")
			.addHighlightedField("penname")
			.addHighlightedField("chkey") //store
			.addHighlightedField("store")
			.addHighlightedField("brandName")
			.addHighlightedField("code")
			//设定匹配关键词高亮样式
			//.setHighlighterPreTags("<span style=\'background:#FF6C00;color:#fff\'>")
			.setHighlighterPreTags("<span style=\'color:#ff313b\'>")
			.setHighlighterPostTags("</span>")
			//with extra you can set everything
			//.addFacet(facetBuilder)
			//.addFacet(manfacet)
			.setExtraSource(request.getJson())
	        .setExplain(false);
			//.setHighlighterRequireFieldMatch(true)
			//.addHighlightedField("pinyinname")
			//.setHighlighterNumOfFragments(0)
			builder.setFrom(request.getStart());
			if(request.getSize()>-1) {
				builder.setSize(request.getSize());
			}
			

	
	        org.elasticsearch.action.search.SearchResponse rsp = builder.execute().actionGet();	
	        //不再需呀ids/
	       // SearchHit[] docs = rsp.getHits().getHits();
			//List<com.shopizer.search.services.SearchHit> hits = new ArrayList<com.shopizer.search.services.SearchHit>();
	        response.setCount((int)rsp.getHits().getTotalHits());
	        //@SuppressWarnings("rawtypes")
	        //List ids = new ArrayList();
	        // total nubs
	        //response.setCount(docs.length);
	        
	       
	        
	        //response.setIds(ids);*/
	        response.setSearchHits(rsp.getHits());
	        
	        Facets facets = rsp.getFacets();
	        if(facets!=null) {
	        	//Map<Facet> facetsMap = new HashMap<Facet>();
	        	
	        	response.setFacets(facets);
	        }
	        
	        //response.setSearchHits(hits);
	        return response;
        
		} catch (Exception e) {
			throw e;
		}
		
		
	}

	/* (non-Javadoc)
	 * @see com.shopizer.search.services.impl.SearchService#searchAutocomplete(java.lang.String, java.lang.String, int)
	 */
	@Override
	public Set<String> searchAutocomplete(String collection,String json,int size) {
		
		Set<String> returnList = new HashSet();
		
		
		try {
			

	       

			//SearchResponse searchResponse = client.prepareSearch().setQuery("{ \"term\" : { \"field1\" : \"value1_1\" }}").execute().actionGet();
			SearchRequestBuilder builder = searchClient.getClient().prepareSearch(collection)
			.setQuery(json)
	        	;
	        
		      	System.out.println("Query for auto complete: "+json+" with builder: "+builder);
	        


	
	        org.elasticsearch.action.search.SearchResponse rsp = builder.execute().actionGet();
	        SearchHit[] docs = rsp.getHits().getHits();
	        for (SearchHit sd : docs) {
	          //to get explanation you'll need to enable this when querying:
	          //System.out.println(sd.getExplanation().toString());
	
	          // if we use in mapping: "_source" : {"enabled" : false}
	          // we need to include all necessary fields in query and then to use doc.getFields()
	          // instead of doc.getSource()
	        	Map source = sd.getSource();
	        	//System.out.println(sd.getType());
	        	//System.out.println(sd.getExplanation().toString());
	        	//System.out.println(sd.fields().toString());
	        	//System.out.println(sd.getMatchedFilters().length);
	        	//SearchHitField f = sd.field("keyword");
				//VERY TEMPARORY SOLUTION HERE, no model support!!!!!!
				if(json.contains("pname")){
					String f = (String)source.get("pname");
					returnList.add(f);
	            
				}
	        	if(json.contains("code")){
					String f = (String)source.get("code");
					returnList.add(f);
	            
				}
				if(json.contains("store")){
					String f = (String)source.get("store");
					returnList.add(f);
	            
				}
				if(json.contains("brandName")){
					String f = (String)source.get("brandName");
					returnList.add(f);
				}
				//System.out.println(sd.sourceAsString());
	            //System.out.println(sd.getScore());
	        	//System.out.println("Adding text: '"+f+"' to the collection");
	        	//returnList.add(sd.sourceAsString());
	        	
	        }
        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnList;
		
	}

	@Override
	public BulkResponse bulkDeleteObjectIndex(String collection, String object, Collection<String> ids) throws Exception {
		if(this.indexExist(collection)) {
			Client client = searchClient.getClient();
			if(ids!=null && ids.size()>0) {
				BulkRequestBuilder bulkRequest = client.prepareBulk();
				for(String s : ids) {
					DeleteRequestBuilder dr = client.prepareDelete(collection, object, s);
					bulkRequest.add(dr);
				}
				return bulkRequest.execute().actionGet(); 
			}
		}
		return null;
	}

	
}
