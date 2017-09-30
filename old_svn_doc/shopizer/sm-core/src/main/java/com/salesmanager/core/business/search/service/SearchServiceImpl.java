package com.salesmanager.core.business.search.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.search.model.IndexProduct;
import com.salesmanager.core.business.search.model.SearchEntry;
import com.salesmanager.core.business.search.model.SearchKeywords;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.utils.CoreConfiguration;
import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;


@Service("productSearchService")
public class SearchServiceImpl implements SearchService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);
	
	
	private final static String PRODUCT_INDEX_NAME = "product";
	private final static String UNDERSCORE = "_";
	private final static String INDEX_PRODUCTS = "INDEX_PRODUCTS";

	@Autowired
	private com.shopizer.search.services.SearchService searchService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private CoreConfiguration configuration;

	@Autowired
	private ProductService productService;
	
	@Override
	public void initService() {
		searchService.initService();
	}

	public SearchServiceImpl() {
		System.out.println("**************this is searchSerivceImpl Constructor*********************");
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void index(MerchantStore store, Product product)
			throws ServiceException {
		
		/**
		 * When a product is saved or updated the indexing process occurs
		 * 
		 * A product entity will have to be transformed to a bean ProductIndex
		 * which contains the indices as described in product.json
		 * 
		 * {"product": {
						"properties" :  {
							"name" : {"type":"string","index":"analyzed"},
							"price" : {"type":"string","index":"not_analyzed"},
							"category" : {"type":"string","index":"not_analyzed"},
							"lang" : {"type":"string","index":"not_analyzed"},
							"available" : {"type":"string","index":"not_analyzed"},
							"description" : {"type":"string","index":"analyzed","index_analyzer":"english"}, 
							"tags" : {"type":"string","index":"not_analyzed"} 
						 } 
			            }
			}
		 *
		 * productService saveOrUpdate as well as create and update will invoke
		 * productSearchService.index	
		 * 
		 * A copy of properies between Product to IndexProduct
		 * Then IndexProduct will be transformed to a json representation by the invocation
		 * of .toJSONString on IndexProduct
		 * 
		 * Then index product
		 * searchService.index(json, "product_<LANGUAGE_CODE>_<MERCHANT_CODE>", "product");
		 * 
		 * example ...index(json,"product_en_default",product)
		 * 
		 */
		
		if(configuration.getProperty(INDEX_PRODUCTS)==null || configuration.getProperty(INDEX_PRODUCTS).equals(Constants.FALSE)) {
			return;
		}
		
		//FinalPrice price = pricingService.calculateProductPrice(product);

		
		Set<ProductDescription> descriptions = product.getDescriptions();
		for(ProductDescription description : descriptions) {
			//set all product default
			//StringBuilder collectionName = new StringBuilder();
			//System.out.println("id:" + product.getId());
			//set all product default
			//collectionName.append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).append(UNDERSCORE).append(store.getCode().toLowerCase());
			
			IndexProduct index = new IndexProduct();

			index.setId(String.valueOf(product.getId()));
			index.setPname(description.getName());
			index.setEnname(description.getEnName());
			
			index.setLang(description.getLanguage().getCode());
			
			index.setFree(product.getProductIsFree());
			index.setSimpledesc(description.getSimpleDescription());
			index.setFriendlyUrl(description.getSeUrl());
			// add keyweord
			index.setKeyword(description.getName());
			index.setCode(product.getCode());
			index.setEnKey(description.getEnName());
			//add第三方凭证
			Set<ProductCertificate> certificates = product.getCertificates();
			if(certificates !=null){
				
				String[][] certis = new String[certificates.size()][2];
				 for(ProductCertificate cerfiCertificate :certificates) {
					 int i=0;
					 if (null != cerfiCertificate.getBasedataType().getName()){
						 certis[i][0]=cerfiCertificate.getBasedataType().getName();
					 }
					 if(null != cerfiCertificate.getBaseinfo()){						 
						 certis[i][1]='"'+cerfiCertificate.getBaseinfo()+'"';
					 }
					 i++;
				 }
				 index.setCertificates(certis);
			}//add第三方凭证
			Set<ProductProof> productProofs = product.getProofs();
			if(productProofs !=null){			
				 String[][] ps = new String[productProofs.size()][2];
				 int i=0;
				 for(ProductProof proofs :productProofs) {					 
					 if (null != proofs.getBasedataType().getName()){
						 ps[i][0]=proofs.getBasedataType().getName();
					 }
					 if(null != proofs.getDateBuyed()){
						 ps[i][1]=proofs.getDateBuyed().toString();
					 }
					 i++;
				 }
				 index.setProofs(ps);
			}
			//add第三方凭证
			Set<ProductThirdproof> productThirdproofs = product.getThirdproofs();
			if(productThirdproofs !=null){				
				String[][] ts = new String[productThirdproofs.size()][2];
				int i=0; 
				for(ProductThirdproof third :productThirdproofs) {
					
					 if (null != third.getBasedataType().getName()){
						 ts[i][0]=third.getBasedataType().getName();
					 }
					 if(null != third.getDescription()){
						 ts[i][1]=third.getDescription();
					 }
					i++;
				 }
				 index.setThirdproofs(ts);
			}
			//add 实验报告
			Set<ProductSelfproof> selfs = product.getSelfProofs();
			if(selfs !=null){				
				String[][] ts = new String[selfs.size()][2];
				int i=0; 
				for(ProductSelfproof third :selfs) {
					
					 if (null != third.getTitle()){
						 ts[i][0]=third.getTitle();
					 }
					 if(null != third.getDescription()){
						 ts[i][1]=third.getDescription();
					 }
					i++;
				 }
				 index.setTestreports(ts);
			}
			//add img
			ProductImage image = product.getProductImage();
			if(image!=null) {		
				String imgpath = new StringBuilder().append("/static").append("/").append(store.getCode()).append("/").append(FileContentType.PRODUCT.name()).append("/")
				.append(product.getSku()).append("/").append(image.getProductImage()).toString();				
				index.setImage(imgpath);
			}
			if(product.getManufacturer()!=null) {
				Set<Authorization> authSet = product.getManufacturer().getAuthorization();
				List<Authorization> alist = new ArrayList<Authorization>();
				for(Authorization au : authSet){
					if (store.getId().longValue() == au.getMerchantStore().getId().longValue()) {
						alist.add(au);
					}
				}
				if (alist != null && alist.size() > 0){
					String[][] ma_au = new String[alist.size()][2];
					int i = 0;
					for(Authorization au : alist){
						if(au.getId()!=null){
							ma_au[i][0] = au.getId().toString();
						}
						ma_au[i][1] = au.getAuth_type() + "";
						i++;
					}
					index.setAuth(ma_au);
					index.setManufacturer(product.getManufacturer().getId());
				}
				
				index.setManufacturer(product.getManufacturer().getId());
			}
			//add store
			index.setStore(product.getMerchantStore().getStorename());
			index.setFree(product.getProductIsFree());
			
			//add price period
			BigDecimal prc = new BigDecimal(0);
			int period = 0;
			boolean preBool = false;
			
			ProductCriteria criteria = new ProductCriteria();
			List<Long> ids = new ArrayList<Long>();
			ids.add(product.getId());
			criteria.setProductIds(ids);
			List<ProductAvailability> availabilities = productService.getPrice(criteria);
			
//			Set<ProductAvailability> availabilities =product.getAvailabilities();
			if(!CollectionUtils.isEmpty(availabilities))
				for (ProductAvailability productAvailability : availabilities) {
					Set<ProductPrice> prices = productAvailability.getPrices();
					if(!CollectionUtils.isEmpty(prices)){
						int i = 0;
						for (Iterator iterator = prices.iterator(); iterator
								.hasNext();i++) {
							ProductPrice price = (ProductPrice) iterator.next();
							BigDecimal pfc = price.getFinalPrice();
							if(i==0) prc = pfc; 
							if(prc.compareTo(pfc)==1) prc = pfc;
							if(preBool) continue;
							if("现货".equals(price.getProductPricePeriod())){
								period = 1;
								preBool = true;
							}
						}
					}
				}
			index.setPrice(prc.longValue());
			index.setPeriod(period);
			/**
			if(price!=null) {
				//index.setPrice(price.getFinalPrice().doubleValue());
			}*/
			//index.setHighlight(description.getProductHighlight());
			/**
			if(!StringUtils.isBlank(description.getMetatagKeywords())){
				String[] tags = description.getMetatagKeywords().split(",");
				@SuppressWarnings("unchecked")
				List<String> tagsList = new ArrayList(Arrays.asList(tags));
				index.setTags(tagsList);
			}*/
			if(product.getQualityScore()!=null)index.setQuality(product.getQualityScore());
						
			Set<Category> categories = product.getCategories();
			if(!CollectionUtils.isEmpty(categories)) {
				List<Long> categoryList = new ArrayList<Long>();
				for(Category category : categories) {
					categoryList.add(category.getId());
				}
				index.setCategories(categoryList);
			}
			
			String jsonString = index.toJSONString();
			try {
				//set all product default
				String res = new StringBuilder().append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).toString();
				searchService.index(jsonString, "product_zh_default", res);
				LOGGER.info("Add Add Index By Product Id : "+ product.getId());
			} catch (Exception e) {
				throw new ServiceException("Cannot index product id [" + product.getId() + "], " + e.getMessage() ,e);
			}
		}
	}

	@Override
	public void deleteIndex(MerchantStore store, Product product) throws ServiceException {
		
		if(configuration.getProperty(INDEX_PRODUCTS)==null || configuration.getProperty(INDEX_PRODUCTS).equals(Constants.FALSE)) {
			return;
		}
		
		Set<ProductDescription> descriptions = product.getDescriptions();
		for(ProductDescription description : descriptions) {
			
			StringBuilder collectionName = new StringBuilder();
			collectionName.append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).append(UNDERSCORE).append(store.getCode().toLowerCase());
			//set all product default
			try {
				searchService.deleteObject("product_zh_default", new StringBuilder().append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).toString(), String.valueOf(product.getId()));
			} catch (Exception e) {
				LOGGER.error("Cannot delete index for product id [" + product.getId() + "], ",e);
			}
		}
	
	}
	
	@Override
	public SearchKeywords searchForKeywords(String collectionName, String jsonString, int entriesCount) throws ServiceException {
		
		/**
		 * 	$('#search').searchAutocomplete({
			url: '<%=request.getContextPath()%>/search/autocomplete/keyword_en'
		  		//filter: function() { 
				//return '\"filter\" : {\"numeric_range\" : {\"price\" : {\"from\" : \"22\",\"to\" : \"45\",\"include_lower\" : true,\"include_upper\" : true}}}';
		  		//}
     		});
     		
     	**/	
     		
		try {

			SearchResponse response = searchService.searchAutoComplete(collectionName, jsonString, entriesCount);
			
			SearchKeywords keywords = new SearchKeywords();
			keywords.setKeywords(Arrays.asList(response.getInlineSearchList()));
			
			return keywords;
			
		} catch (Exception e) {
			LOGGER.error("Error while searching keywords " + jsonString,e);
			throw new ServiceException(e);
		}

		
	}
	
	@Override
	public com.salesmanager.core.business.search.model.SearchResponse search(MerchantStore store, String languageCode, String jsonString, int entriesCount, int startIndex) throws ServiceException {
		initService();
		try {
			StringBuilder collectionName = new StringBuilder();
			collectionName.append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(languageCode).append(UNDERSCORE).append(store.getCode().toLowerCase());
			
			
			SearchRequest request = new SearchRequest();
			request.setCollection(collectionName.toString());
			request.setSize(entriesCount);
			request.setStart(startIndex);
			request.setJson(jsonString);
			
			SearchResponse response =searchService.search(request);
			
			com.salesmanager.core.business.search.model.SearchResponse resp = new com.salesmanager.core.business.search.model.SearchResponse();
			
			
			resp.setTotalCount(response.getCount());
			
			//List<IndexProduct> entries = new ArrayList<IndexProduct>();
			List<SearchEntry> products = new ArrayList<SearchEntry>();
			
			SearchHits hits = response.getSearchHits();
			for(org.elasticsearch.search.SearchHit hit : hits) {
				SearchEntry entry = new SearchEntry();
				Map<String,Object> metaEntries = hit.getSource();
				Map<String, HighlightField> fields = hit.getHighlightFields();
				if(fields!=null) {
					
					for(HighlightField field : fields.values()) {
												
						Text[] text = field.getFragments();
						//keywords的替换pname
//						if(field.getName().equalsIgnoreCase("pname")){
//							if(metaEntries.get("pname") !=null ){
// 								continue;
//							}
//						}
						if(field.getName().equalsIgnoreCase("chkey")){
							metaEntries.put("pname", text[0]);
						}
						metaEntries.put(field.getName(), text[0]);
						//text[0]
					}
				}
				entry.setSocre(hit.getScore());
				entry.setProduct(metaEntries);
				products.add(entry);
			}
			resp.setEntries(products);
			//resp.setIndexProduct(entries);
			
			Facets facets = response.getFacets();
			if(facets!=null) {
				Map<String,Object> fMap = new HashMap<String, Object>();
				for(Facet facet:facets){
					fMap.put(facet.getName(), facet);
				}
				resp.setIndexFacets(fMap);
			
			}
			
			
			
			return resp;
			
			
		} catch (Exception e) {
			LOGGER.error("Error while searching keywords " + jsonString,e);
			return null;
			//throw new ServiceException(e);
		}
		
	}
	
	
	public com.salesmanager.core.business.search.model.SearchResponse search(MerchantStore store, String languageCode, String jsonString) throws ServiceException {
		initService();
		try {
			StringBuilder collectionName = new StringBuilder();
			collectionName.append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(languageCode).append(UNDERSCORE).append(store.getCode().toLowerCase());
			
			
			SearchRequest request = new SearchRequest();
			request.setCollection(collectionName.toString());
			//request.setSize(entriesCount);
			//request.setStart(startIndex);
			request.setJson(jsonString);
			
			SearchResponse response =searchService.search(request);
			
			com.salesmanager.core.business.search.model.SearchResponse resp = new com.salesmanager.core.business.search.model.SearchResponse();
			
			
			resp.setTotalCount(response.getCount());
			
			List<SearchEntry> entries = new ArrayList<SearchEntry>();
			
			SearchHits hits = response.getSearchHits();
			for(org.elasticsearch.search.SearchHit hit : hits) {
				
				SearchEntry entry = new SearchEntry();
				
				Map<String,Object> metaEntries = hit.getSource();
				IndexProduct indexProduct = new IndexProduct();
				//Map sourceEntries = (Map)metaEntries.get("source");
				
				Map<String, HighlightField> fields = hit.getHighlightFields();
				if(fields!=null) {
					for(HighlightField field : fields.values()) {
												
						Text[] text = field.getFragments();
						//text[0]
						metaEntries.put(field.getName(), text[0]);
						
					}
					
					
				
				}
				
				//indexProduct.setDescription((String)sourceEntries.get("description"));
				//indexProduct.setHighlight((String)sourceEntries.get("highlight"));
				indexProduct.setId((String)metaEntries.get("id"));
				indexProduct.setLang((String)metaEntries.get("lang"));
				indexProduct.setPname(((String)metaEntries.get("pname")));
				indexProduct.setManufacturer((Long)metaEntries.get("manufacturer"));
				//indexProduct.setPrice(((Double)metaEntries.get("price")));
				//indexProduct.setStore(((String)metaEntries.get("store")));
				indexProduct.setImage((String)metaEntries.get("img"));
				indexProduct.setCertificates((String[][])metaEntries.get("certificates"));
				indexProduct.setProofs((String[][])metaEntries.get("proofs"));
				indexProduct.setThirdproofs((String[][])metaEntries.get("thirdproofs"));
				indexProduct.setFriendlyUrl((String)metaEntries.get("friendlyUrl"));
				indexProduct.setEnname((String)metaEntries.get("quality"));
				indexProduct.setAuth((String[][])metaEntries.get("auth"));
				entry.setIndexProduct(indexProduct);
				entries.add(entry);
			}
			
			resp.setEntries(entries);
			
			/**
			Map<String,Facet> facets = response.getFacets();
			if(facets!=null) {
				Map<String,List<SearchFacet>> searchFacets = new HashMap<String,List<SearchFacet>>();
				for(String key : facets.keySet()) {
					
					Facet f = facets.get(key);
					
					List<SearchFacet> fs = searchFacets.get(key);
					if(fs==null) {
						fs = new ArrayList<SearchFacet>();
						searchFacets.put(key, fs);
					}
					
					List<com.shopizer.search.services.Entry> facetEntries = f.getEntries();
					for(com.shopizer.search.services.Entry facetEntry : facetEntries) {
					
						SearchFacet searchFacet = new SearchFacet();
						searchFacet.setKey(facetEntry.getName());
						searchFacet.setName(facetEntry.getName());
						//setCount by facetEntry.getCount() modify by sam at 2015-8-13
//						searchFacet.setCount(f.getEntries().size());
						searchFacet.setCount(facetEntry.getCount());
						
						fs.add(searchFacet);
					
					}
					
				}
				
				resp.setFacets(searchFacets);
			
			}**/
			Facets facets = response.getFacets();
			if(facets!=null) {
				Map<String,Object> fMap = new HashMap<String, Object>();
				fMap.put("indexface", facets);
				resp.setIndexFacets(fMap);
			
			}
						
			return resp;
			
			
		} catch (Exception e) {
			LOGGER.error("Error while searching keywords " + jsonString,e);
			throw new ServiceException(e);
		}
		
	}
	/**
	private String[][] proofs(ArrayList<HashMap<String,String>> lists){
		if(null != lists && lists.size()>0){
			String[][]proofs = new String[lists.size()][2];
			for(int i=0;i<lists.size();i++){
				HashMap<String, String> map = lists.get(i);
				proofs[i][0] = map.get("pname");
				proofs[i][1] = map.get("description");
			}
			return proofs;
		}
		return null;
		
	}**/
}

