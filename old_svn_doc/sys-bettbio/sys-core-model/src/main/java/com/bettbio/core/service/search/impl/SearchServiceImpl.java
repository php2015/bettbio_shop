package com.bettbio.core.service.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.model.search.IndexProduct;
import com.bettbio.core.model.search.SearchEntry;
import com.bettbio.core.model.search.page.SearchPage;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.service.search.SearchService;
import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;

/**
 * 搜索
 * 
 * @author GuoChunbo
 *
 */
@Service("productSearchService")
public class SearchServiceImpl implements SearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Autowired
	private com.shopizer.search.services.SearchService searchService;

	private static final String COLLECTION_NAME = "product_zh_default";

	@Override
	public void initService() {
		searchService.initService();
	}

	public SearchServiceImpl() {
		System.out.println("--------------------------------------------------------------------------------------------");
		System.out.println("| -------------------------------E L A S T I C S E A R C H-------------------------------- |");
		System.out.println("--------------------------------------------------------------------------------------------");
	}
	
	@Override
	public void addIndex(Product product) {
		
		try {
			String json = convert(product).toJSONString();
			searchService.index(json, COLLECTION_NAME, "product_zh");
			LOGGER.info("Add Add Index By Product Id : "+ product.getId());
		} catch (Exception e) {
			LOGGER.error("Add Add Index By Product Id : "+ product.getId());
		}
	}
	
	@Override
	public SearchPage<IndexProduct> search(String jsonString, final SearchPage<IndexProduct> searchPage) {

		SearchRequest request = new SearchRequest();
		request.setCollection(COLLECTION_NAME);
		request.setJson(jsonString);
		request.setSize(searchPage.getPageSize());
		request.setStart(searchPage.getStartRow());
		
		return search(request,searchPage);
	}
	
	
	@Override
	public SearchPage<IndexProduct> search(SearchRequest request,final SearchPage<IndexProduct> searchPage) {
		
		try {
			SearchResponse response = searchService.search(request);
			SearchHits hits = response.getSearchHits();

			List<IndexProduct> indexProducts = new ArrayList<IndexProduct>();
			List<SearchEntry> products = new ArrayList<SearchEntry>();
			for (SearchHit hit : hits) {
				SearchEntry entry = new SearchEntry();
				Map<String, Object> metaEntries = hit.getSource();
				Map<String, HighlightField> fields = hit.getHighlightFields();
				if (fields != null) {
					for (HighlightField field : fields.values()) {

						Text[] text = field.getFragments();
						metaEntries.put(field.getName(), text[0]);
					}

				}

				IndexProduct indexProduct = new IndexProduct();

				if(!StringUtils.isEmpty(metaEntries.get("id")))
					indexProduct.setId(metaEntries.get("id").toString());

				if(!StringUtils.isEmpty(metaEntries.get("store")))
					indexProduct.setStore(metaEntries.get("store").toString());

				
				if(!StringUtils.isEmpty(metaEntries.get("code")))
					indexProduct.setCode(metaEntries.get("code").toString());

				if(!StringUtils.isEmpty(metaEntries.get("pname")))
					indexProduct.setPname(metaEntries.get("pname").toString());

				if(!StringUtils.isEmpty(metaEntries.get("penname")))				
					indexProduct.setEnname(metaEntries.get("penname").toString());
				
				if(!StringUtils.isEmpty(metaEntries.get("img")))
					indexProduct.setImage(metaEntries.get("img").toString());

				if(!StringUtils.isEmpty(metaEntries.get("quality")))
					indexProduct.setQuality((Integer)metaEntries.get("quality"));

				if(!StringUtils.isEmpty(metaEntries.get("storeCode")))
					indexProduct.setStoreCode(metaEntries.get("storeCode").toString());
				
				if(!StringUtils.isEmpty(metaEntries.get("pcode")))
					indexProduct.setPcode(metaEntries.get("pcode").toString());
				
				indexProducts.add(indexProduct);
				entry.setSocre(hit.getScore());
				entry.setProduct(metaEntries);
				products.add(entry);
			}
			searchPage.setEntries(products);
			searchPage.setList(indexProducts);
			searchPage.setTotal(response.getCount());

			Facets facets = response.getFacets();
			if(facets!=null) {
				Map<String,Object> fMap = new HashMap<String, Object>();
				for(Facet facet:facets){
					fMap.put(facet.getName(), facet);
				}
				searchPage.setIndexFacets(fMap);
			}
			
			return searchPage;
		} catch (Exception e) {
			LOGGER.error("Error while searching keywords " + request.getJson(),e);
			e.printStackTrace();
		}
		return searchPage;
	}
	
	private IndexProduct convert(Product product){
		IndexProduct index = new IndexProduct();
		index.setId(String.valueOf(product.getId()));
		index.setPname(product.getProductNameCh());
		index.setEnname(product.getProductNameEn());
		
		//add第三方凭证
		
		return index;
	}
}
