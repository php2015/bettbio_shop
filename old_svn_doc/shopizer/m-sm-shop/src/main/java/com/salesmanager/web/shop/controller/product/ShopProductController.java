package com.salesmanager.web.shop.controller.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.catalog.product.service.review.ProductReviewService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CacheUtils;
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
import com.salesmanager.web.utils.BreadcrumbsUtils;
import com.salesmanager.web.utils.CategoryNode;
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
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductAttributeService productAttributeService;
	
	@Autowired
	private ProductRelationshipService productRelationshipService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ProductReviewService productReviewService;
	
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
	
	
	/**
	 * Display product details with reference to caller page
	 * @param friendlyUrl
	 * @param ref
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{friendlyUrl}.html/ref={ref}")
	public String displayProductWithReference(@PathVariable final String friendlyUrl, @PathVariable final String ref, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return display(ref, friendlyUrl, model, request, response, locale);
	}
	

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
	public String displayProduct(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return display(null, friendlyUrl, model, request, response, locale);
	}


	@SuppressWarnings("unchecked")
	public String display(final String reference, final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		Product product = productService.getBySeUrlId(store, friendlyUrl, locale);
				
		if(product==null) {
			return PageBuilderUtils.build404(store);
		}
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		
		ReadableProduct productProxy = populator.populate(product, new ReadableProduct(), store, language);


		PageInformation pageInformation = new PageInformation();

		pageInformation.setPageDescription(productProxy.getDescription().getSimpleDescription());

		pageInformation.setPageKeywords(productProxy.getDescription().getName() + "," + productProxy.getDescription().getEnName());
		pageInformation.setPageTitle(productProxy.getDescription().getTitle());
		pageInformation.setPageUrl(productProxy.getDescription().getFriendlyUrl());
		
		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
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

		
		Category c = cmap.get(Long.parseLong(product.getCategoryId()));
		int isService =categoryNode.getProductType(c);
		
		StringBuilder template = null;
		if(isService==3){
			template = new StringBuilder().append(ControllerConstants.Tiles.Product.service).append(".").append(store.getStoreTemplate());
		}else if(isService==1){
			template = new StringBuilder().append(ControllerConstants.Tiles.Product.product).append(".").append(store.getStoreTemplate());
		}else{
			template = new StringBuilder().append(ControllerConstants.Tiles.Product.instrument).append(".").append(store.getStoreTemplate());
		}
		/** template **/
		

		

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
	


}
