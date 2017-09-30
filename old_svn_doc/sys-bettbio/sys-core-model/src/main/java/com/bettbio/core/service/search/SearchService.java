package com.bettbio.core.service.search;

import com.bettbio.core.model.search.IndexProduct;
import com.bettbio.core.model.search.page.SearchPage;
import com.bettbio.core.mongo.model.Product;
import com.shopizer.search.services.SearchRequest;

public interface SearchService {

	void initService();

	void addIndex(Product product);
	
	SearchPage<IndexProduct> search(String jsonString,SearchPage<IndexProduct> searchPage);
	
	SearchPage<IndexProduct> search(SearchRequest request, SearchPage<IndexProduct> searchPage);
}
