package com.salesmanager.web.admin.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.utils.CategoryNode;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PinyinUtils;

@Controller
public class ServiceController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);
	

	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	private ProductTypeService productTypeService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private ProductPriceUtils priceUtil;

	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CategoryNode categoryNode;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired
	CategoryService categoryService;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/services/editService.html", method=RequestMethod.GET)
	public String displayProductEdit(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(productId,model,request,response);

	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/services/createService.html", method=RequestMethod.GET)
	public String displayProductCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(null,model,request,response);

	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/services/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = manufacturerService.getStoreName();
			List<String> ch = new ArrayList<String>();
			ch.add("b");
			ch.add("g");
			ch.add("k");
			ch.add("n");
			ch.add("r");
			ch.add("s");
			ch.add("w");
			ch.add("z");
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores,ch);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	private String displayProduct(Long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//display menu
		if(productId == null){
			setMenu(model,request,"catalogue-products-create");
		}else{
				setMenu(model,request,"catalogues");
		}
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//		List<Manufacturer> manufacturers = manufacturerService.listByStore(store, language);
		//对于品牌，不做控制，允许查看到所有的品牌集合
		//List<Manufacturer> manufacturers = manufacturerService.list();
		
		//List<ProductType> productTypes = productTypeService.list();
		
				
		List<Language> languages = store.getLanguages();
		if(languages==null || languages.size()==0){
			languages = new ArrayList<Language>();
			languages.add(store.getDefaultLanguage());
		}

		
		com.salesmanager.web.admin.entity.catalog.Product product = new com.salesmanager.web.admin.entity.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();

		if(productId!=null && productId!=0) {//edit mode

			Product dbProduct = productService.getById(productId);
			if(auth != null && request.isUserInRole("ADMIN")){
				if(dbProduct==null) {
					return "redirect:/admin/products/products.html";
				} 
			} else {
				if(dbProduct==null || dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					return "redirect:/admin/products/products.html";
				}
			}
			
			product.setProduct(dbProduct);
			Set<ProductDescription> productDescriptions = dbProduct.getDescriptions();
			
			for(Language l : languages) {
				
				ProductDescription productDesc = null;
				for(ProductDescription desc : productDescriptions) {
					
					Language lang = desc.getLanguage();
					if(lang.getCode().equals(l.getCode())) {
						productDesc = desc;
					}

				}
				
				if(productDesc==null) {
					productDesc = new ProductDescription();
					productDesc.setLanguage(l);
				}

				descriptions.add(productDesc);
				
			}
			
			for(ProductImage image : dbProduct.getImages()) {
				if(image.isDefaultImage()) {
					product.setProductImage(image);
					break;
				}

			}
			
			
			ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			
			Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
								product.setProductPrice(priceUtil.getAdminFormatedAmount(store, productPrice.getProductPriceAmount()));
								product.setProductPriceStock(price.getProductPriceStockAmount());
							}
						}
					}
				}
			}
			
			if(productAvailability==null) {
				productAvailability = new ProductAvailability();
			}
			
			if(productPrice==null) {
				productPrice = new ProductPrice();
			}
			
			product.setAvailability(productAvailability);
			product.setPrice(productPrice);
			product.setDescriptions(descriptions);
			
			
			product.setDateAvailable(DateUtil.formatDate(dbProduct.getDateAvailable()));


		} else {


			for(Language l : languages) {
				
				ProductDescription desc = new ProductDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
			Product prod = new Product();
			
			prod.setAvailable(true);
			
			ProductAvailability productAvailability = new ProductAvailability();
			ProductPrice price = new ProductPrice();
			product.setPrice(price);
			product.setAvailability(productAvailability);
			product.setProduct(prod);
			product.setDescriptions(descriptions);
			product.setDateAvailable(DateUtil.formatDate(new Date()));


		}
		
		Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
		if(product.getProduct() !=null && product.getProduct().getManufacturer() !=null){
			Manufacturer man = manMap.get(product.getProduct().getManufacturer().getId());
			model.addAttribute("manName",man.getDescription().getName());
		}
		
		model.addAttribute("product",product);
		//model.addAttribute("manufacturers", manufacturers);
		//model.addAttribute("productTypes", productTypes);
		
		return "admin-serivce-edit";
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/services/categorys.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody com.salesmanager.web.admin.entity.catalog.Category getCategorys(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			com.salesmanager.web.admin.entity.catalog.Category root = categoryNode.getRootService(request);
			root.setCategoryID(-1l);
			root.setCategoryName("root");
			return root;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error while paging category", e);
		}
		return null;
	}
	
	private void setMenu(Model model, HttpServletRequest request,String activeMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue-products", "catalogue-products");
		activeMenus.put(activeMenu, activeMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue-products");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	

}
