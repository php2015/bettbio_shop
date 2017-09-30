package com.salesmanager.web.shop.controller.customer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.review.ProductReviewService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.PersistableProductReview;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProductReview;
import com.salesmanager.web.populator.catalog.PersistableProductReviewPopulator;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.populator.catalog.ReadableProductReviewPopulator;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.LabelUtils;

/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping(Constants.SHOP_URI + "/customer")
public class CustomerProductReviewController extends AbstractController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderProductService orderProductService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ProductReviewService productReviewService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerFacade customerFacade;
	
	@Autowired
	private LabelUtils messages;

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/review.html", method=RequestMethod.GET)
	public String displayProductReview(@RequestParam Long productId, @RequestParam Long orderProductId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    Language language = super.getLanguage(request);

        
        
        //get product
        Product product = productService.getById(productId);
        if(product==null) {
        	return "redirect:" + Constants.SHOP_URI;
        }
        
        //不做商品所属商品判断
        /*if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
        	return "redirect:" + Constants.SHOP_URI;
        }*/
        
        
        //create readable product
        ReadableProduct readableProduct = new ReadableProduct();
        ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
        readableProductPopulator.setPricingService(pricingService);
        readableProductPopulator.populate(product, readableProduct,  store, language);
        model.addAttribute("product", readableProduct);
        

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());

        }
        
        List<ProductReview> reviews = productReviewService.getByProduct(product, language);
        /*for(ProductReview r : reviews) {
	    	if(r.getCustomer().getId().longValue()==customer.getId().longValue()) {
	    		
				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);
	    		
	    		model.addAttribute("customerReview", rev);
	    		break;
	    	}
	    }*/
	    
	  //判断用户是否对该订单商品做过评价
        List<ProductReview> productReviewList = productReviewService.getByOrderProductAndCustomer(orderProductId, product.getId(), customer.getId());
	    
	    if (productReviewList!=null && productReviewList.size()>0) {
			ProductReview vo = productReviewList.get(0);
			ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
			ReadableProductReview rev = new ReadableProductReview();
			reviewPopulator.populate(vo, rev, store, language);
    		
    		model.addAttribute("customerReview", rev);
		}
        
        
        ProductReview review = new ProductReview();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setOrderProduct(new OrderProduct(orderProductId));
        ReadableProductReview productReview = new ReadableProductReview();
        ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
        reviewPopulator.populate(review,  productReview, store, language);
        
        model.addAttribute("review", productReview);
        model.addAttribute("reviews", reviews);
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/review/submit.html", method=RequestMethod.POST)
	public String submitProductReview(@ModelAttribute("review") PersistableProductReview review, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    Language language = getLanguage(request);
	   
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getByID(auth.getName());

        }
        
        if(customer==null) {
        	return "redirect:" + Constants.SHOP_URI;
        }

	    
	    Product product = productService.getById(review.getProductId());
	    if(product==null) {
	    	return "redirect:" + Constants.SHOP_URI;
	    }
	    
	    if(StringUtils.isBlank(review.getDescription())) {
	    	FieldError error = new FieldError("description","description",messages.getMessage("NotEmpty.review.description", locale));
			bindingResult.addError(error);
	    }
	    

	    
        ReadableProduct readableProduct = new ReadableProduct();
        ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
        readableProductPopulator.setPricingService(pricingService);
        readableProductPopulator.populate(product, readableProduct,  store, language);
        model.addAttribute("product", readableProduct);
	    

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

        if ( bindingResult.hasErrors() )
        {

            return template.toString();

        }
		
        
        //check if customer has already evaluated the product
	    //List<ProductReview> reviews = productReviewService.getByProduct(product);
        /*for(ProductReview r : reviews) {
	    	if(r.getCustomer().getId().longValue()==customer.getId().longValue()) {
				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);
	    		
	    		model.addAttribute("customerReview", rev);
	    		return template.toString();
	    	}
    	}*/
	    //判断用户是否对该订单商品做过评价
	    List<ProductReview> productReviewList = productReviewService.getByOrderProductAndCustomer(review.getOrderProductId(), product.getId(), customer.getId());
	    if (productReviewList!=null && productReviewList.size()>0) {
			ProductReview vo = productReviewList.get(0);
			ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
			ReadableProductReview rev = new ReadableProductReview();
			reviewPopulator.populate(vo, rev, store, language);
    		
    		model.addAttribute("customerReview", rev);
    		return template.toString();
		}
	    

	    
	    PersistableProductReviewPopulator populator = new PersistableProductReviewPopulator();
	    populator.setCustomerService(customerService);
	    populator.setLanguageService(languageService);
	    populator.setProductService(productService);
	    populator.setOrderProductService(orderProductService);
	    
	    review.setDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
	    review.setCustomerId(customer.getId());
	    
	    ProductReview productReview = populator.populate(review, store, language);
	    productReviewService.create(productReview);
        
        model.addAttribute("review", review);
        model.addAttribute("success", "success");
        
		ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
		ReadableProductReview rev = new ReadableProductReview();
		reviewPopulator.populate(productReview, rev, store, language);
		
        model.addAttribute("customerReview", rev);
        //refresh rating information
        int count = readableProduct.getRatingCount();
        
        Double averageRating = readableProduct.getRating();
		if(averageRating==null) {
			averageRating = 0d;
		}
		BigDecimal totalRating = BigDecimal.valueOf(averageRating).multiply(new BigDecimal(count));
		totalRating = totalRating.add(new BigDecimal(review.getRating()));
		
		count = count + 1;
		double avg = totalRating.doubleValue() / count;
		double rating = Math.round(avg * 2) / 2.0f;
		readableProduct.setRatingCount(count);
		readableProduct.setRating(rating);
		return template.toString();
		
	}
	
	
	
}
