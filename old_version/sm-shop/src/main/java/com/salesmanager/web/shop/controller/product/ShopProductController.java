package com.salesmanager.web.shop.controller.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.browse.ProductBrowse;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductPricingContext;
import com.salesmanager.core.business.catalog.product.service.ProductPricingData;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.ProductSpecPricingData;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.browse.ProductBrowseService;
import com.salesmanager.core.business.catalog.product.service.file.DigitalProductService;
import com.salesmanager.core.business.catalog.product.service.price.BrandDiscountService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.catalog.product.service.review.ProductReviewService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.authorization.model.AuthorizationAndProduct;
//import com.salesmanager.core.business.merchant.authorization.model.AuthorizationAndProduct;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.admin.entity.digital.DownLoadFile;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProductPrice;
import com.salesmanager.web.entity.catalog.product.ReadableProductReview;
import com.salesmanager.web.entity.shop.Breadcrumb;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.populator.catalog.ReadableProductPricePopulator;
import com.salesmanager.web.populator.catalog.ReadableProductReviewPopulator;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.BreadcrumbsUtils;
import com.salesmanager.web.utils.CategoryNode;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.GeoLocationUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Populates the product details page
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/product")
public class ShopProductController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ShopProductController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private DigitalProductService digitalProductService;
	
	@Autowired
	private ProductAttributeService productAttributeService;
	
	@Autowired
	private ProductRelationshipService productRelationshipService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ProductReviewService productReviewService;
	
	@Autowired
	private CustomerFacade customerFacade;
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private CacheUtils cache;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BreadcrumbsUtils breadcrumbsUtils;
	
	@Autowired
	private CategoryNode categoryNode;
	
	@Autowired
	private ProductPriceService productPriceService;
	
	@Autowired
	private ProductBrowseService productBrowseService;
	
	@Autowired
	private BrandDiscountService brandDiscountService;
	
	//private static final Logger LOG = LoggerFactory.getLogger(ShopProductController.class);
	

	
	/**
	 * Display product details no reference
	 * @param friendlyUrl
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{friendlyUrl}.html")
	public String displayProduct(@PathVariable final String friendlyUrl,Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return display(friendlyUrl, model, request, response, locale);
	}


	@SuppressWarnings("unchecked")
	public String display(final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		try{
			//品牌
			String authidt=request.getParameter("id");
			String authname=request.getParameter("typename");
			Integer authid=0;
			Integer authtypeid=0;
			if(authidt ==null || authname==null){
				//查询authid+authType
				Authorization  auth = productService.queryByAuthorization(Long.parseLong(friendlyUrl));
				authid = Integer.parseInt(auth.getId()+"");
				authtypeid = Integer.parseInt(auth.getAuth_type()+"");
			}else{
				authid= Integer.parseInt(authidt);
				authtypeid= Integer.parseInt(authname);
			}
			
			
			AuthorizationAndProduct ap = new AuthorizationAndProduct();
			ap.setAuthid(authid);
			ap.setAuthtypeid(authtypeid);
			
			model.addAttribute("auth",ap);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Product product = productService.getBySeUrl(store, friendlyUrl, locale);
		
		if(product==null) {
			return PageBuilderUtils.build404(store);
		}
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		
		ReadableProduct productProxy = populator.populate(product, new ReadableProduct(), store, language);
		
		//设置商品的log路径
		if(productProxy.getStore().getStoreLogo()!=null && !productProxy.getStore().getStoreLogo().equalsIgnoreCase("")){
			MerchantStore pstore = product.getMerchantStore();
			StringBuilder imagePath = new StringBuilder();
			imagePath.append("http").append("://")
			.append(pstore.getDomainName())
			.append(request.getContextPath());
			
			
			
			imagePath	//.append(scheme).append("://").append(merchantStore.getDomainName())\
				.append(Constants.STATIC_URI).append("/")
				.append(pstore.getCode()).append("/").append("LOGO")
				.append("/").append(productProxy.getStore().getStoreLogo());
			productProxy.getStore().setStoreLogo(imagePath.toString());
		}

		//meta information
		
		PageInformation pageInformation = new PageInformation();

		pageInformation.setPageDescription(productProxy.getDescription().getSimpleDescription());

		pageInformation.setPageKeywords(productProxy.getDescription().getName() + "," + productProxy.getDescription().getEnName());
		pageInformation.setPageTitle(productProxy.getDescription().getTitle());
		pageInformation.setPageUrl(productProxy.getDescription().getFriendlyUrl());
		
		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		String reference = product.getCategories().iterator().next().getLineage();
		StringBuffer sb= new StringBuffer();
		
		if(reference.equalsIgnoreCase("/")){
			sb.append("/").append(product.getCategories().iterator().next().getId().toString());			
		}else{
			sb.append(reference).append(product.getCategories().iterator().next().getId().toString()).append("/");			
		}
		
		reference = sb.toString();
		Breadcrumb breadCrumb = breadcrumbsUtils.buildProductBreadcrumb(reference, productProxy, store, language, request.getContextPath(),cmap);
		request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
		request.setAttribute(Constants.BREADCRUMB, breadCrumb);

		List<ProductReview> reviews = productReviewService.getByProduct(product, language);
		if(!CollectionUtils.isEmpty(reviews)) {
			List<ReadableProductReview> revs = new ArrayList<ReadableProductReview>();
			ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
			for(ProductReview review : reviews) {
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(review, rev, store, language);
				revs.add(rev);
			}
			model.addAttribute("reviews", revs);
		}
		
			
		model.addAttribute("product", productProxy);
		int isService=1;
		try{
			Category c = cmap.get(Long.parseLong(product.getCategoryId()));
			isService =categoryNode.getCateType(c);
		}catch (Exception e){
			//默认为1
		}		
		
		StringBuilder template = null;
		switch (isService) {
			case 2:
				template = new StringBuilder().append(ControllerConstants.Tiles.Product.consumptive).append(".").append(store.getStoreTemplate());
				break;
			case 3:
				template = new StringBuilder().append(ControllerConstants.Tiles.Product.instrument).append(".").append(store.getStoreTemplate());
				break;
			case 4:
				template = new StringBuilder().append(ControllerConstants.Tiles.Product.service).append(".").append(store.getStoreTemplate());
				break;
			default:
				template = new StringBuilder().append(ControllerConstants.Tiles.Product.product).append(".").append(store.getStoreTemplate());
				break;
		}
		
		
		/** template **/
		//记录浏览信息
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String ip = GeoLocationUtils.getClientIpAddress(request);
		ProductBrowse productBrowse = new ProductBrowse();
		productBrowse.setIp(ip);
		productBrowse.setBrowseDate(new Date());
		productBrowse.setProduct(product);
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());

        }    	
	     if(customer != null){
		    	 productBrowse.setCustomer(customer);
	     }
	     try{
	    	 productBrowseService.save(productBrowse);
	     }catch (Exception e){
	    	 
	     }
		return template.toString();
	}
	
	
    @RequestMapping(value={"/{productId}/calculatePrice.html"}, method=RequestMethod.POST)
	public @ResponseBody
	ReadableProductPrice calculatePrice(@RequestParam(value="attributeIds[]") Long[] attributeIds, @PathVariable final Long productId, final HttpServletRequest request, final HttpServletResponse response, final Locale locale) throws Exception {

    	
    	MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		Product product = productService.getById(productId);
		
		@SuppressWarnings("unchecked")
		List<Long> ids = new ArrayList<Long>(Arrays.asList(attributeIds));
		List<ProductAttribute> attributes = productAttributeService.getByAttributeIds(store, ids);
		
		for(ProductAttribute attribute : attributes) {
			if(attribute.getProduct().getId().longValue()!=productId.longValue()) {
				return null;
			}
		}
		
		FinalPrice price = pricingService.calculateProductPrice(product, attributes);
    	ReadableProductPrice readablePrice = new ReadableProductPrice();
    	ReadableProductPricePopulator populator = new ReadableProductPricePopulator();
    	populator.setPricingService(pricingService);
    	populator.populate(price, readablePrice, store, language);
    	return readablePrice;
    	
    }
    
    
    
    @RequestMapping(value={"/{productId}/price.html"}, method=RequestMethod.POST)
	public @ResponseBody
	List<ReadableProductPrice> calculatePrice(@PathVariable final Long productId,final HttpServletRequest request, final HttpServletResponse response, final Locale locale) throws Exception {

    	
    	MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);	
    	Customer customer = (Customer)request.getAttribute(Constants.CUSTOMER);	
		
		List<ProductPrice> prs = productPriceService.getByProductID(productId);
		
		List<ReadableProductPrice> prices = new ArrayList<ReadableProductPrice>();
		//price
		for(ProductPrice price:prs){
			ReadableProductPrice price1 = new ReadableProductPrice();
			for(ProductPriceDescription pname : price.getDescriptions()){
				price1.setName(pname.getName());
				break;
			}
			BigDecimal sprice = pricingService.getFinalPrice(price); //获取产品打折价格
			if(null != sprice ){
				price1.setSpecial(pricingService.getDisplayAmount(sprice, store));
			}
			price1.setListPrice(price.getProductPriceAmount().doubleValue());
			price1.setPrice(pricingService.getDisplayAmount(price.getProductPriceAmount(), store));
			BigDecimal tmp = price.getProductPriceAmount();
			if (tmp!=null&&tmp.intValue()==0) {
				price1.setPriceValue("0"); 
			}
			price1.setCode(price.getCode());
			price1.setId(price.getId());
			price1.setPeriod(price.getProductPricePeriod());
			prices.add(price1);
			
		}
		
		/** changed 3017-3-27 use new price rule
		 */
		ProductPricingContext priceCtx = new ProductPricingContext();
		priceCtx.setCustomer(customer);
		List<Long> pIds = Arrays.asList(new Long[]{productId});
		
		List<ProductPricingData> productPriceDatas = pricingService.calculateProductListPrice(priceCtx, pIds);
		if (productPriceDatas != null){
			ProductPricingData ppData = productPriceDatas.get(0);
			for(ReadableProductPrice price: prices){
				if (price.getListPrice() <= 0){
					continue;
				}
				ProductSpecPricingData specData = ProductPriceUtils.getSpecPriceData(ppData, price.getId());
				if (specData ==null){
					continue;
				}
				price.setDiscountType(specData.getFinalPriceType());
				if (specData.getFinalPriceType() == null){
					continue;
				}
				if (specData.getFinalPriceType().equals(PricingService.PRICE_TYPE_LIST_PRICE)){
					continue;
				}
				price.setDiscountRate(specData.getFinalDiscount());
				price.setDiscountPrice(pricingService.getDisplayAmount(specData.getFinalPrice(), store));
			}
		}
		return prices;
    }
    
    


	@RequestMapping(value={"/{productId}/files.html"}, method=RequestMethod.POST)
	public @ResponseBody
	List<DownLoadFile> getFiles( @PathVariable final Long productId, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    	List<DownLoadFile> fs = new ArrayList<DownLoadFile>();
    	
    	List<DigitalProduct> ps = digitalProductService.queryByDigitalProduct(productId);
    	if(ps !=null && ps.size()>0){
    		for(DigitalProduct p:ps){
    			StringBuilder filePath = new StringBuilder();
    			HttpSession session = request.getSession();
    			MerchantStore store = p.getProduct().getMerchantStore();
    			@SuppressWarnings("unchecked")
    			Map<String,String> configurations = (Map<String, String>)session.getAttribute(Constants.STORE_CONFIGURATION);
    			String scheme = Constants.HTTP_SCHEME;
    			if(configurations!=null) {
    				scheme = (String)configurations.get("scheme");
    			}
    			

    			
    			filePath.append(scheme).append("://")
    			.append(store.getDomainName())
    			//.append("/")
    			.append(request.getContextPath());
    			
    			filePath
    				.append(FilePathUtils.buildAdminDownloadProductFilePath(store, p)).toString();
    			DownLoadFile df = new DownLoadFile();
    			df.setFileName(p.getProductFileName());
    			df.setUrl(filePath.toString());
    			fs.add(df);
    		}
    	}
    	return fs;
    	
    }

}
