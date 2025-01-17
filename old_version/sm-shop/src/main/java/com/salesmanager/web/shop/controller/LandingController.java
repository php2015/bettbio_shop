package com.salesmanager.web.shop.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.brand.service.BrandService;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.content.model.Content;
import com.salesmanager.core.business.content.model.ContentDescription;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.admin.controller.ControllerConstants.Tiles;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class LandingController {
	
	
	private final static String LANDING_PAGE = "LANDING_PAGE";
	
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductRelationshipService productRelationshipService;

	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	private MerchantStoreService merchantService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LandingController.class);
	private final static String HOME_LINK_CODE="HOME";
	
	@RequestMapping(value={"/", Constants.SHOP_URI + "/home.html",Constants.SHOP_URI +"/", Constants.SHOP_URI}, method=RequestMethod.GET)
	public String displayLanding(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		request.setAttribute(Constants.LINK_CODE, HOME_LINK_CODE);
		
		Content content = contentService.getByCode(LANDING_PAGE, store, language);
		
	
		/** Rebuild breadcrumb **/
		/**
		BreadcrumbItem item = new BreadcrumbItem();
		item.setItemType(BreadcrumbItemType.HOME);
		item.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, locale));
		item.setUrl(Constants.HOME_URL);

		
		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language);
		
		
		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(item);
		
		breadCrumb.setBreadCrumbs(items);
		request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
		request.setAttribute(Constants.BREADCRUMB, breadCrumb);
		/** **/
		
		if(content!=null) {
			
			ContentDescription description = content.getDescription();
			
			
			model.addAttribute("page",description);
			
			
			PageInformation pageInformation = new PageInformation();
			pageInformation.setPageTitle(description.getName());
			pageInformation.setPageDescription(description.getMetatagDescription());
			pageInformation.setPageKeywords(description.getMetatagKeywords());
			
			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
			
		}
		
		model.addAttribute("brandBanners", brandService.getPopularBrandBannerList());
		
		/** template **/
		StringBuilder template = new StringBuilder().append("landing.").append(store.getStoreTemplate());

		return template.toString();
	}
	
	@RequestMapping(value={Constants.SHOP_URI + "/mobile.html"}, method=RequestMethod.GET)
	public String displayMobileLanding(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		request.setAttribute(Constants.LINK_CODE, HOME_LINK_CODE);
		
		Content content = contentService.getByCode(LANDING_PAGE, store, language);
		
		if(content!=null) {
			
			ContentDescription description = content.getDescription();
			
			
			model.addAttribute("page",description);
			
			
			PageInformation pageInformation = new PageInformation();
			pageInformation.setPageTitle(description.getName());
			pageInformation.setPageDescription(description.getMetatagDescription());
			pageInformation.setPageKeywords(description.getMetatagKeywords());
			
			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
			
		}
		
		/** template **/
		StringBuilder template = new StringBuilder().append("mobile.").append(store.getStoreTemplate());

		return template.toString();
	}
	
	@RequestMapping( value=Constants.SHOP_URI + "/{store}", method=RequestMethod.GET)
	public String displayStoreLanding(@PathVariable final String store, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			request.getSession().invalidate();
			request.getSession().removeAttribute(Constants.MERCHANT_STORE);
			
			MerchantStore merchantStore = merchantService.getByCode(store);
			if(merchantStore!=null) {
				request.getSession().setAttribute(Constants.MERCHANT_STORE, merchantStore);
			} else {
				LOGGER.error("MerchantStore does not exist for store code " + store);
			}
			
		} catch(Exception e) {
			LOGGER.error("Error occured while getting store code " + store, e);
		}
		

		
		return "redirect:" + Constants.SHOP_URI;
	}
		
	@RequestMapping( value="/shop/promotions/{name}.html", method=RequestMethod.GET)
	public String displayMerchantPromotion(@PathVariable final String name, HttpServletRequest request, HttpServletResponse response) {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		return Tiles.Content.promotion+"."+store.getStoreTemplate()+"."+name;
	}

}
