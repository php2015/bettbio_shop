package com.salesmanager.web.shop.controller.category;

import java.text.NumberFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductPricingContext;
import com.salesmanager.core.business.catalog.product.service.ProductPricingData;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.ProductSpecPricingData;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.BrandDiscountService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.search.model.QueryEntry;
import com.salesmanager.core.business.search.model.SearchEntry;
import com.salesmanager.core.business.search.model.SearchResponse;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.core.utils.PinyinUtils;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.ProductList;
import com.salesmanager.web.entity.catalog.category.ReadableCategory;
import com.salesmanager.web.entity.catalog.manufacturer.ManufacturerEntity;
import com.salesmanager.web.entity.catalog.manufacturer.ReadableManufacturer;
import com.salesmanager.web.entity.catalog.product.ListProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProductPrice;
import com.salesmanager.web.entity.shop.Breadcrumb;
import com.salesmanager.web.populator.catalog.ReadableCategoryPopulator;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.BreadcrumbsUtils;
import com.salesmanager.web.utils.CategoryNode;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

import edu.emory.mathcs.backport.java.util.Collections;


/**
 * Renders a given category page based on friendly url
 * Can also filter by facets such as manufacturer
 * @author Carl Samson
 *
 */
@Controller
public class ShoppingCategoryController {
	

	@Autowired
	private CategoryNode categoryNode;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private BreadcrumbsUtils breadcrumbsUtils;
	
	@Autowired
	private CacheUtils cache;
	
	@Autowired
	private PricingService pricingService;
	@Autowired
	private SearchService searchService;

	@Autowired
	private BrandDiscountService brandDiscountService;
	
	private NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.CHINA);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryController.class);
	
	
	/**
	 * 
	 * @param friendlyUrl
	 * @param ref
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/category/{friendlyUrl}.html/ref={ref}")
	public String displayCategoryWithReference(@PathVariable final String friendlyUrl, @PathVariable final String ref, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		
		return this.displayCategory(model,request,response);
	}
	
	
	@RequestMapping(value="/shop/category/navmenu.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody List<com.salesmanager.web.admin.entity.catalog.Category> getCategorys(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			List<com.salesmanager.web.admin.entity.catalog.Category> topmenus = categoryNode.getTopList(request);
			return topmenus;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error while paging category", e);
		}
		
		return null;
	}
	
	/**
	 * 针对移动端，只加载4个一级菜单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/shop/category/mobileNavMenu.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody List<com.salesmanager.web.admin.entity.catalog.Category> getMobileNavMenu(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			List<Category> tops = (List<Category>)request.getSession().getServletContext().getAttribute("topCateGory");
			List<com.salesmanager.web.admin.entity.catalog.Category> webtops = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
			for (Category cat : tops) {
				com.salesmanager.web.admin.entity.catalog.Category node = new com.salesmanager.web.admin.entity.catalog.Category();
				node.setCategoryID(cat.getId());
				node.setCategoryName(cat.getDescription().getName());
				node.setUrl(cat.getUrl());
				webtops.add(node);
			}
			return webtops;
		} catch (Exception e) {
			LOGGER.error("Error while paging category", e);
		}
		return null;
	}

	
	/**
	 * Category page entry point
	 * @param friendlyUrl
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/category/category.html")
	public String displayCategoryNoReference(  Model model , HttpServletRequest request, HttpServletResponse response) throws Exception {

		return this.displayCategory(model,request,response);
	}
	
	@SuppressWarnings("unchecked")
	private String displayCategory(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getSession().getAttribute(Constants.DEFAULT_LANGUAGE);
		if(request.getParameter("categoryId")!=null && !StringUtils.isBlank(request.getParameter("categoryId"))){
			Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
			Category cate = cmap.get(Long.parseLong(request.getParameter("categoryId")));
			ReadableCategoryPopulator populator = new ReadableCategoryPopulator();
			ReadableCategory categoryProxy = populator.populate(cate, new ReadableCategory(), store, language);
			
			//meta information
			/**PageInformation pageInformation = new PageInformation();
			pageInformation.setPageDescription(categoryProxy.getDescription().getMetaDescription());
			pageInformation.setPageKeywords(categoryProxy.getDescription().getKeyWords());
			pageInformation.setPageTitle(categoryProxy.getDescription().getTitle());
			pageInformation.setPageUrl(categoryProxy.getDescription().getFriendlyUrl());*/
			
		//	request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
			//** retrieves category id drill down**//
			
			model.addAttribute("category", categoryProxy);
			/** template **/
			StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Category.category).append(".").append(store.getStoreTemplate());

			return template.toString();
		}else{
			LOGGER.error("No category found for id: " + request.getParameter("categoryId"));
			//redirect on page not found
			return PageBuilderUtils.build404(store);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/shop/category/category/{categoryId}.html")
	private String displayCategory(@PathVariable final String categoryId, Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getSession().getAttribute(Constants.DEFAULT_LANGUAGE);
		if(!StringUtils.isBlank(categoryId)){
			Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
			Category cate = cmap.get(Long.parseLong(categoryId));
			ReadableCategoryPopulator populator = new ReadableCategoryPopulator();
			ReadableCategory categoryProxy = populator.populate(cate, new ReadableCategory(), store, language);
			
			//meta information
			/**PageInformation pageInformation = new PageInformation();
			pageInformation.setPageDescription(categoryProxy.getDescription().getMetaDescription());
			pageInformation.setPageKeywords(categoryProxy.getDescription().getKeyWords());
			pageInformation.setPageTitle(categoryProxy.getDescription().getTitle());
			pageInformation.setPageUrl(categoryProxy.getDescription().getFriendlyUrl());
			
			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);*/
			//** retrieves category id drill down**//
			
			model.addAttribute("category", categoryProxy);
			/** template **/
			StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Category.category).append(".").append(store.getStoreTemplate());

			return template.toString();
		}else{
			LOGGER.error("No category found for id: " + categoryId);
			//redirect on page not found
			return PageBuilderUtils.build404(store);
		}
	}
	
	/**
	 * Returns all categories for a given MerchantStore
	 */
	@RequestMapping("/services/public/category/{store}/{language}")
	@ResponseBody
	public List<ReadableCategory> getCategories(@PathVariable final String language, @PathVariable final String store, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Language> langs = languageService.getLanguagesMap();
		Language l = langs.get(language);
		if(l==null) {
			l = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}
		
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
		
		List<Category> categories = categoryService.listByStore(merchantStore, l);
		
		ReadableCategoryPopulator populator = new ReadableCategoryPopulator();
		
		List<ReadableCategory> returnCategories = new ArrayList<ReadableCategory>();
		for(Category category : categories) {
			ReadableCategory categoryProxy = populator.populate(category, new ReadableCategory(), merchantStore, l);
			returnCategories.add(categoryProxy);
		}
		
		return returnCategories;
	}

	
	@RequestMapping(value="/services/public/products/list.html",  method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ManufacturerEntity  getAllProducts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
			if(request.getParameter("categoryId")!=null && !StringUtils.isBlank(request.getParameter("categoryId"))){
				
				try {
					long cateid =Long.parseLong(request.getParameter("categoryId"));
					Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
					Category cate = cmap.get(cateid);
					//boolean isServer=false;
					List<Long> ids = new ArrayList<Long>();
					ids = this.getids(ids, cate);
					ids.add(cate.getId());
					ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
					ProductCriteria criteria = this.getCriteria(request);
					//ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
					String json = this.toQueryJSONString(criteria,ids);
					if(json == null) return null;
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
					if(resp ==null || resp.getEntries() ==null || resp.getEntries().size()==0) return null;
					List<SearchEntry> entries = resp.getEntries();
					ReadableProductPopulator populator = new ReadableProductPopulator();
					Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
					
					
					Breadcrumb breadCrumb = breadcrumbsUtils.buildCategoryBreadcrumb(cate, store, language, request.getContextPath(),cmap);
					manufacturerEntity.setBreadcrumb(breadCrumb);
					
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
						List<ReadableCategory> categorys = populator.getCagoryList(facets.get("category"),cate);
						manufacturerEntity.setCategoryList(categorys);
					//}
					if(request.getParameter("manufacturers") ==null || StringUtils.isBlank(request.getParameter("manufacturers"))){

						List<ReadableManufacturer> manufacturerList =populator.getManufacturerList(facets.get("manufacturer"));
						sortManufacturerList(manufacturerList);
						manufacturerEntity.setManufacturerList(manufacturerList);
					}
					
					return manufacturerEntity;
				} catch (Exception e) {
					System.out.println(e.getMessage());
					LOGGER.error("Exception occured while querying " ,e);
				}
								
			}
			return null;
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
	
	@RequestMapping("/services/public/products/price.html")
	@ResponseBody
	public	List<ReadableProduct> getPrice(HttpServletRequest request, HttpServletResponse response) {
		String productIds = request.getParameter("prodcutId");
		String services = request.getParameter("services");
		
		// TODO: from here, is new code for product-price
		ProductPricingContext priceCtx = new ProductPricingContext();
		Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);
		priceCtx.setCustomer(customer);
		List<Long> prodIds = new ArrayList<Long>();
		if (StringUtils.isNotBlank(productIds)){
			String []cids = productIds.split(",");
			for(int i=0;i<cids.length;i++) {
				if (StringUtils.isNotBlank(cids[i])) {
					prodIds.add(Long.parseLong(cids[i].trim()));
				}
			}
		}
		if (StringUtils.isNotBlank(services)){
			String []cids = services.split(",");
			for(int i=0;i<cids.length;i++) {
				if (StringUtils.isNotBlank(cids[i])) {
					prodIds.add(Long.parseLong(cids[i].trim()));
				}
			}
		}
		List<ProductPricingData> priceDatas = pricingService.calculateProductListPrice(priceCtx, prodIds);
		List<ReadableProduct> rplikst = populateProductForPricing(customer, priceDatas);
		return rplikst;
	}
	
	
	private List<ReadableProduct> populateProductForPricing(Customer customer, List<ProductPricingData> priceDatas) {
		List<ReadableProduct> result = new ArrayList<ReadableProduct>();
		if (priceDatas == null || priceDatas.isEmpty()){
			return result;
		}
		for (ProductPricingData prodData: priceDatas){
			ReadableProduct rProduct = new ReadableProduct();
			result.add(rProduct);
			rProduct.setId(prodData.getProductId());
			if (prodData.getProduct().getServerPrice() != null){
				rProduct.setsPrice(prodData.getProduct().getServerPrice());
				continue;
			}
			List<ProductSpecPricingData> specPriceList = prodData.getPrices();
			if (specPriceList == null){
				continue;
			}
			List<ReadableProductPrice> prices = new ArrayList<ReadableProductPrice>();
			rProduct.setPrices(prices);
			for(ProductSpecPricingData specPrice: specPriceList){
				ReadableProductPrice rpPrice = new ReadableProductPrice();
				rpPrice.setId(specPrice.getSpecId());
				List<ReadableProductPrice> rppList = rProduct.getPrices();
				if (rppList == null){
					rppList = new ArrayList<ReadableProductPrice>();
					rProduct.setPrices(rppList);
				}
				rppList.add(rpPrice);
				rpPrice.setCode(specPrice.getSpecTitle());
				if (specPrice.getListPrice() == null){
					rpPrice.setListPrice(null);
					continue;
				}
				rpPrice.setListPrice(specPrice.getListPrice().doubleValue());
				if (specPrice.getListPrice().doubleValue() == 0) {
					rpPrice.setPriceValue("0");
				} else {
					rpPrice.setPriceValue(currencyInstance.format(specPrice.getListPrice().doubleValue())
							.replace(Constants.CURRENCY_CHINA_YUAN, "¥"));
				}
				
				if (customer == null){
					rpPrice.setDiscountType(null);
					continue;
				}
				rpPrice.setDiscountType(specPrice.getFinalPriceType());
				rpPrice.setDiscountRate(specPrice.getFinalDiscount());
				String priceStr = currencyInstance.format(specPrice.getFinalPrice().doubleValue());
				rpPrice.setDiscountPrice(priceStr.replace(Constants.CURRENCY_CHINA_YUAN, "¥"));
				
			}
		}
		return result;
	}

	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
	    {
	        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
	       
	        return paginaionData;
	    }
	 
	 private String toQueryJSONString(ProductCriteria criteria,List<Long> cid){
			QueryEntry queryEntry = new QueryEntry();
			queryEntry.setCategory(cid);;
			if(criteria.getCategoryIds() != null && criteria.getCategoryIds().size()>0)queryEntry.setCategories(criteria.getCategoryIds());
			if(criteria.getManufacturers() != null && criteria.getManufacturers().size()>0) queryEntry.setManufacturer(criteria.getManufacturers());
			queryEntry.setCerticate(criteria.isCerticate());
			queryEntry.setProof(criteria.isProof());
			queryEntry.setThird(criteria.isThird());
			//if(criteria.get)
			return queryEntry.toJSONString();
		}
	 
	 private ProductCriteria getCriteria(HttpServletRequest request){
			ProductCriteria criteria = new ProductCriteria();
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
					default:
						criteria.setProof(true);
						break;
					}
				}
			}
			return criteria;
		}
		

}
