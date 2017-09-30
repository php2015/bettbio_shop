package com.shopizer.search.services.worker;

import javax.inject.Inject;

import com.shopizer.search.services.impl.SearchDelegate;
import com.shopizer.search.utils.SearchClient;


public class DeleteObjectImpl implements DeleteObjectWorker {
	
	@Inject
	private SearchDelegate searchDelegate;

	public void deleteObject(SearchClient client, String collection, String object, String id, ExecutionContext context) throws Exception {
		
		
		//need to get the original entry
		com.shopizer.search.services.GetResponse r = searchDelegate.getObject(collection, object, id);
		
		if(r!=null) {
			
			if(context==null) {
				context = new ExecutionContext();
			}
			
			
			if(r.getFields()!=null && r.getFields().size()>0) {
			
				
				
				context.setObject("indexData", r.getFields());
			
			}
			
		}
		

		searchDelegate.delete(collection, object, id);

	}


	public void deleteObject(SearchClient client, String collection, String id)
			throws Exception {
		throw new Exception("Not implemented");
		
	}


}
