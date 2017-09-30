package com.salesmanager.web.shop.controller.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.search.model.BettQueryEntry;
import com.salesmanager.core.business.search.model.QueryEntry;
import com.salesmanager.core.business.search.model.SearchEntry;
import com.salesmanager.core.business.search.model.SearchKeywords;
import com.salesmanager.core.business.search.model.SearchResponse;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.core.utils.PinyinUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.ProductList;
import com.salesmanager.web.entity.catalog.category.ReadableCategory;
import com.salesmanager.web.entity.catalog.manufacturer.ManufacturerEntity;
import com.salesmanager.web.entity.catalog.manufacturer.ReadableManufacturer;
import com.salesmanager.web.entity.catalog.product.ListProduct;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.shop.model.search.AutoCompleteRequest;
import com.salesmanager.web.utils.PageBuilderUtils;

import edu.emory.mathcs.backport.java.util.Collections;

@Controller
public class SearchController {
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private PricingService pricingService;


	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
	
	private final static int AUTOCOMPLETE_ENTRIES_COUNT = 25;
	/**
	 * Retrieves a list of keywords for a given series of character typed by the end user
	 * This is used for auto complete on search input field
	 * @param json
	 * @param store
	 * @param language
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/services/public/search/{store}/{language}/autocomplete.html"}, method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	//@RequestMapping("/services/public/search/{store}/{language}/autocomplete.html",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String autocomplete(@RequestParam("q") String query, @PathVariable String store, @PathVariable final String language, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		LOGGER.info("Code go to autocomplate w/ query="+query+", store="+store+", lang="+language+"");
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null; //reset for the current request
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
			return null;
		}
		
		AutoCompleteRequest req = new AutoCompleteRequest(store,language);
		/** formatted toJSONString because of te specific field names required in the UI **/
		LOGGER.info("Autocomplete keywords search for index: "+ req.getCollectionName()+", query: "+req.toJSONString(query));
		
		String queryType = request.getParameter("queryType");
		
		
		String queryJson = req.toJSONString(query,queryType);
		LOGGER.info("The query json string is w/ query type("+queryType+"): "+queryJson);
		SearchKeywords keywords = searchService.searchForKeywords(req.getCollectionName(), queryJson , AUTOCOMPLETE_ENTRIES_COUNT);
		
		
		
		
		//SearchKeywords keywords = searchService.searchForKeywords("product_zh_default", req.toJSONString(query), AUTOCOMPLETE_ENTRIES_COUNT);
		LOGGER.info("The returing string is: "+keywords.toJSONString());
		return keywords.toJSONString();
		
	}

	
	
	
	
	/**
	 * Displays the search result page
	 * @param json
	 * @param store
	 * @param language
	 * @param start
	 * @param max
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/services/public/search/products/list.html")
	@ResponseBody
	public ManufacturerEntity search(HttpServletRequest request, HttpServletResponse response) {
		ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
		if(request.getParameter("query")!=null && !StringUtils.isBlank(request.getParameter("query"))){
			
			try {
				ProductCriteria criteria = this.getCriteria(request);				
				String json = this.toQueryJSONString(criteria, request.getParameter("query"));

				LOGGER.info("ES query w/ Query Type: "+criteria.getQueryType()+"and Order Type='"+criteria.getOrderType()+"' json = " + json);
				ProductList productList = new ProductList();
				int page =1;
				String strPage = request.getParameter("page");
				if(strPage !=null && !strPage.equalsIgnoreCase("1")){
					page = Integer.parseInt(strPage);
				}
				int pageNum =20;
				
				PaginationData paginaionData=createPaginaionData(page,pageNum);
				Language language = (Language)request.getSession().getAttribute(Constants.DEFAULT_LANGUAGE);
				MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.MERCHANT_STORE);

				SearchResponse resp = searchService.search(store, language.getCode(), json,paginaionData.getPageSize(),(paginaionData.getOffset() -1));
				
				if(resp ==null || resp.getEntries() ==null || resp.getEntries().size()<1) return manufacturerEntity;
				List<SearchEntry> entries = resp.getEntries();
				ReadableProductPopulator populator = new ReadableProductPopulator();
				Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
				Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
				populator.setManMap(manMap);
				populator.setCatMap(cmap);
				List<ListProduct> products = populator.getListProducts(entries,cmap);
				productList.setListProducts(products);
				productList.setProductCount(resp.getTotalCount());
				productList.setPaginationData(PageBuilderUtils.calculatePaginaionData(paginaionData,Constants.MAX_ORDERS_PAGE, productList.getProductCount()));
				manufacturerEntity.setProductList(productList);
				
				//get facet
				Map<String,Object> facets = resp.getIndexFacets();
				//if(request.getParameter("categoryId") ==null || StringUtils.isBlank(request.getParameter("categoryId"))){
					List<ReadableCategory> categorys = populator.getCagoryList(facets.get("category"));
					manufacturerEntity.setCategoryList(categorys);
				//}
				//if(request.getParameter("manufacturers") ==null || StringUtils.isBlank(request.getParameter("manufacturers"))){

					List<ReadableManufacturer> manufacturerList =populator.getManufacturerList(facets.get("manufacturer"));
					sortManufacturerList(manufacturerList);
					manufacturerEntity.setManufacturerList(manufacturerList);
				//}
				
				return manufacturerEntity;
			} catch (Exception e) {
				LOGGER.error("Exception occured while querying " ,e);
			}	
			
		}
		
		return manufacturerEntity;
	}
	
	private void sortManufacturerList(List<ReadableManufacturer> manufacturerList) {
		Collections.sort(manufacturerList, new Comparator<ReadableManufacturer>(){

			@Override
			public int compare(ReadableManufacturer o1, ReadableManufacturer o2) {
				String name1 = PinyinUtils.getSortSpell(o1.getManName());
				String name2 = PinyinUtils.getSortSpell(o2.getManName());
				return name1.compareTo(name2);
			}});
	}





	private String toQueryJSONString(ProductCriteria criteria,String query){
		QueryEntry queryEntry = new BettQueryEntry();
		queryEntry.setQuery(query);
		if(criteria.getCategoryIds() != null && criteria.getCategoryIds().size()>0)queryEntry.setCategories(criteria.getCategoryIds());
		if(criteria.getManufacturers() != null && criteria.getManufacturers().size()>0) queryEntry.setManufacturer(criteria.getManufacturers());
		queryEntry.setCerticate(criteria.isCerticate());
		queryEntry.setProof(criteria.isProof());
		queryEntry.setThird(criteria.isThird());
		if(criteria.getOrderType()!=-1) queryEntry.setOrderType(criteria.getOrderType());
		queryEntry.setQueryType(criteria.getQueryType());
		return queryEntry.toJSONString();
	}
	
	/**
	 * Displays the search page after a search query post
	 * @param query
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/search/search.html"}, method={RequestMethod.POST,RequestMethod.GET})
	public String displaySearch(@RequestParam("q") String query, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		model.addAttribute("q",query);
		model.addAttribute("qt", request.getParameter("queryType"));
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Search.search).append(".").append(store.getStoreTemplate());
		System.out.println( template.toString());
		return template.toString();
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	
		
	private ProductCriteria getCriteria(HttpServletRequest request){
		ProductCriteria criteria = new ProductCriteria();
		
		if(request.getParameter("categoryId") !=null && !StringUtils.isBlank(request.getParameter("categoryId"))){
			String []cids = request.getParameter("categoryId").split(",");
			List<Long> ids = new ArrayList<Long>();
			if(cids !=null && cids.length>0){
				Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
				for(int i=0;i<cids.length;i++){
					Category cat = cmap.get(Long.parseLong(cids[i]));
					if(cat !=null){
						ids=this.getids(ids, cat);
					}
					ids.add(Long.parseLong(cids[i]));						
				}
		
				criteria.setCategoryIds(ids);
			}
		}
		if(request.getParameter("manufacturers") !=null && !StringUtils.isBlank(request.getParameter("manufacturers"))){
			String []mids = request.getParameter("manufacturers").split(",");
			if(mids !=null && mids.length>0){
				List<Long> ids = new ArrayList<Long>();
				for(int i=0;i<mids.length;i++){
					ids.add(Long.parseLong(mids[i]));
				}
				criteria.setManufacturers(ids);
			}
		}						
			
		if(request.getParameter("qulity") !=null && !StringUtils.isBlank(request.getParameter("qulity"))){
			String []qids = request.getParameter("qulity").split(",");
			for(int i=0; i<qids.length;i++){
				switch (qids[i]) {
				case "0":
					criteria.setCerticate(true);
					break;
				case "1":
					criteria.setThird(true);
					break;
				case "2":
					criteria.setProof(true);
					break;
				case "3":
					criteria.setSelf(true);
					break;
				}
			}
		}
		if(request.getParameter("orderBy") !=null && !StringUtils.isBlank(request.getParameter("orderBy"))){
			criteria.setOrderType(Integer.parseInt(request.getParameter("orderBy")));
		}
		if(request.getParameter("queryType") !=null && !StringUtils.isBlank(request.getParameter("queryType"))){
			criteria.setQueryType(Integer.parseInt(request.getParameter("queryType")));
		}
		if(request.getParameter("auth1") !=null && !StringUtils.isBlank(request.getParameter("auth1"))){
			
		}
		if(request.getParameter("auth2") !=null && !StringUtils.isBlank(request.getParameter("auth2"))){
			
		}
		return criteria;
	}
	
	private List<Long> getids(List<Long> ids,Category category){
		List<Category> cats = category.getCategories();
		if (cats !=null && cats.size()>0 ){
			for (Category id:cats){
				ids.add(id.getId());
				if(id.getCategories()!=null && id.getCategories().size()>0){
					ids = getids(ids,id);
				}
			}
		}
		return ids;
	}
	
	@RequestMapping(value={"/shop/brand/{brandName}/list.html"}, method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView displayBrand(@PathVariable String brandName, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		model.addAttribute("qt",1);
		model.addAttribute("q",brandName);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Search.search).append(".").append(store.getStoreTemplate());
		ModelAndView mv = new ModelAndView(template.toString());
		mv.addObject("qt",1);
		mv.addObject("q",brandName);
		return mv;
	}
}
