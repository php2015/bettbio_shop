package com.salesmanager.web.admin.controller.products;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.catalog.ReadablePrice;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class ProductPriceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductPriceService productPriceService;
	
	@Autowired
	private ProductPriceUtils priceUtil;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
    private LanguageService languageService;
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/prices.html", method=RequestMethod.GET)
	public String getProductPrices(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		//get the product and validate it belongs to the current merchant
		Product product = productService.getById(productId);
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && request.isUserInRole("ADMIN")){
			if(product==null) {
				return "redirect:/admin/products/products.html";
			} 
		} else {
			if(product==null || product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}
		
		ProductAvailability productAvailability = null;
		for(ProductAvailability availability : product.getAvailabilities()) {
			if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {
				productAvailability = availability;
			}
		}

		model.addAttribute("product",product);
		model.addAttribute("availability",productAvailability);

		return ControllerConstants.Tiles.Product.productPrices;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/prices/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pagePrices(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		String findName = request.getParameter("findname");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		AjaxResponse resp = new AjaxResponse();
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			//admin has all products
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){ //非admin角色，则需要判断是否是本店铺的商品
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("Product id is not valid");
					String returnString = resp.toJSONString();
					return returnString;
				}
			} 
			
			
			ProductAvailability defaultAvailability = null;
			
			Set<ProductAvailability> availabilities = product.getAvailabilities();

			//get default availability
			for(ProductAvailability availability : availabilities) {
				if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {
					defaultAvailability = availability;
					break;
				}
			}
			
			if(defaultAvailability==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			Set<ProductPrice> prices = defaultAvailability.getPrices();
			
			
			for(ProductPrice price : prices) {
				Map entry = new HashMap();
				entry.put("priceId", price.getId());
				
				
				String priceName = "";
				Set<ProductPriceDescription> descriptions = price.getDescriptions();
				if(descriptions!=null) {
					for(ProductPriceDescription description : descriptions) {
						if(description.getLanguage().getCode().equals(language.getCode())) {
							priceName = description.getName(); 
						}
					}
				}
				

				entry.put("name", priceName);
				entry.put("price", priceUtil.getAdminFormatedAmountWithCurrency(store,price.getProductPriceAmount()));
				entry.put("specialPrice", priceUtil.getAdminFormatedAmountWithCurrency(store,price.getProductPriceSpecialAmount()));
				
				String discount = "";
				if(priceUtil.hasDiscount(price)) {
					discount = priceUtil.getAdminFormatedAmountWithCurrency(store,price.getProductPriceAmount());
				}
				entry.put("special", discount);
				//价格（即规格）所对应的库存
				entry.put("stock", price.getProductPriceStockAmount());
				//供货时间
				entry.put("period", price.getProductPricePeriod());
				entry.put("defaultPrice", price.isDefaultPrice());
				if (price.getProductPriceSpecialStartDate()!=null) {
					entry.put("begindt", DateUtil.formatDate(price.getProductPriceSpecialStartDate()));
				}
				if (price.getProductPriceSpecialEndDate()!=null) {
					entry.put("enddt", DateUtil.formatDate(price.getProductPriceSpecialEndDate()));
				}
				resp.addDataEntry(entry);
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/price/edit.html", method=RequestMethod.GET)
	public String editProductPrice(@RequestParam("id") long productPriceId, @RequestParam("productId") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}
		
		setMenu(model,request);
		return displayProductPrice(product, productPriceId, model, request, response);
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/price/create.html", method=RequestMethod.GET)
	public String displayCreateProductPrice(@RequestParam("productId") long productId,@RequestParam(value="availabilityId", required=false) Long avilabilityId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Product product = productService.getById(productId);
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}
		
		setMenu(model,request);
		return displayProductPrice(product, null, model, request, response);


		
	}
	
	private String displayProductPrice(Product product, Long productPriceId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

	
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		com.salesmanager.web.admin.entity.catalog.ProductPrice pprice = new com.salesmanager.web.admin.entity.catalog.ProductPrice();
		
		ProductPrice productPrice = null;
		ProductAvailability productAvailability = null;
		
		if(productPriceId!=null) {
		
			Set<ProductAvailability> availabilities = product.getAvailabilities();
	
			//get default availability
			for(ProductAvailability availability : availabilities) {
				if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {//TODO to be updated when multiple regions is implemented
					productAvailability = availability;
					Set<ProductPrice> prices = availability.getPrices();
					for(ProductPrice price : prices) {
						if(price.getId().longValue()==productPriceId.longValue()) {
							productPrice = price;
							if(price.getProductPriceSpecialStartDate()!=null) {
								pprice.setProductPriceSpecialStartDate(DateUtil.formatDate(price.getProductPriceSpecialStartDate()));
							}
							if(price.getProductPriceSpecialEndDate()!=null) {
								pprice.setProductPriceSpecialEndDate(DateUtil.formatDate(price.getProductPriceSpecialEndDate()));
							}
							pprice.setPriceText(priceUtil.getAdminFormatedAmount(store, price.getProductPriceAmount()));
							if(price.getProductPriceSpecialAmount()!=null) {
								pprice.setSpecialPriceText(priceUtil.getAdminFormatedAmount(store, price.getProductPriceSpecialAmount()));
							}
							break;
						}
					}
				}
			}
		
		}	
		
		if(productPrice==null) {
			productPrice = new ProductPrice();
			productPrice.setProductPriceType(ProductPriceType.ONE_TIME);
		}
		
		//descriptions
		List<Language> languages = store.getLanguages();
		
		Set<ProductPriceDescription> productPriceDescriptions = productPrice.getDescriptions();
		List<ProductPriceDescription> descriptions = new ArrayList<ProductPriceDescription>();
		for(Language l : languages) {
			ProductPriceDescription productPriceDesc = null;
			for(ProductPriceDescription desc : productPriceDescriptions) {
				Language lang = desc.getLanguage();
				if(lang.getCode().equals(l.getCode())) {
					productPriceDesc = desc;
				}
			}
			
			if(productPriceDesc==null) {
				productPriceDesc = new ProductPriceDescription();
				productPriceDesc.setLanguage(l);
				productPriceDescriptions.add(productPriceDesc);
			}	
			descriptions.add(productPriceDesc);
		}
		
		
		if(productAvailability==null) {
			Set<ProductAvailability> availabilities = product.getAvailabilities();
			for(ProductAvailability availability : availabilities) {
				if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {//TODO to be updated when multiple regions is implemented
					productAvailability = availability;
					break;
				}
			}
		}
		
		pprice.setDescriptions(descriptions);
		pprice.setProductAvailability(productAvailability);
		pprice.setPrice(productPrice);
		pprice.setProduct(product);
		

		model.addAttribute("product",product);
		//model.addAttribute("descriptions",descriptions);
		model.addAttribute("price",pprice);
		//model.addAttribute("availability",productAvailability);
		
		return ControllerConstants.Tiles.Product.productPrice;
	}
	
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/price/save.html", method=RequestMethod.POST)
	public String saveProductPrice(@Valid @ModelAttribute("price") com.salesmanager.web.admin.entity.catalog.ProductPrice price, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		//dates after save
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Product product = price.getProduct();
		Product dbProduct = productService.getById(product.getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){
			if(dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}
		
		model.addAttribute("product",dbProduct);
		
		//validate price
		BigDecimal submitedPrice = null;
		try {
			submitedPrice = priceUtil.getAmount(price.getPriceText());
		} catch (Exception e) {
			ObjectError error = new ObjectError("productPrice",messages.getMessage("NotEmpty.product.productPrice", locale));
			result.addError(error);
		}
		
		//validate discount price
		BigDecimal submitedDiscountPrice = null;
		
		if(!StringUtils.isBlank(price.getSpecialPriceText())) {
			try {
				submitedDiscountPrice = priceUtil.getAmount(price.getSpecialPriceText());
			} catch (Exception e) {
				ObjectError error = new ObjectError("productSpecialPrice",messages.getMessage("NotEmpty.product.productPrice", locale));
				result.addError(error);
			}
		}
		
		//validate start date
		if(!StringUtils.isBlank(price.getProductPriceSpecialStartDate())) {
			try {
				Date startDate = DateUtil.getDate(price.getProductPriceSpecialStartDate());
				price.getPrice().setProductPriceSpecialStartDate(startDate);
			} catch (Exception e) {
				ObjectError error = new ObjectError("productPriceSpecialStartDate",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}
		
		if(!StringUtils.isBlank(price.getProductPriceSpecialEndDate())) {
			try {
				Date endDate = DateUtil.getDate(price.getProductPriceSpecialEndDate());
				price.getPrice().setProductPriceSpecialEndDate(endDate);
			} catch (Exception e) {
				ObjectError error = new ObjectError("productPriceSpecialEndDate",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}
		
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Product.productPrice;
		}
		

		price.getPrice().setProductPriceAmount(submitedPrice);
		if(!StringUtils.isBlank(price.getSpecialPriceText())) {
			price.getPrice().setProductPriceSpecialAmount(submitedDiscountPrice);
		}
		
		ProductAvailability productAvailability = null;
		
		Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
		for(ProductAvailability availability : availabilities) {
			
			if(availability.getId().longValue()==price.getProductAvailability().getId().longValue()) {
				productAvailability = availability;
				break;
			}
			
			
		}
		
		Set<ProductPriceDescription> descriptions = new HashSet<ProductPriceDescription>();
		if(price.getDescriptions()!=null && price.getDescriptions().size()>0) {
			
			for(ProductPriceDescription description : price.getDescriptions()) {
				description.setProductPrice(price.getPrice());
				descriptions.add(description);
//				description.setProductPrice(price.getPrice());
			}
		}
		
		price.getPrice().setDescriptions(descriptions);
		price.getPrice().setProductAvailability(productAvailability);
		
		productPriceService.saveOrUpdate(price.getPrice());
		//是否是默认价格的逻辑处理，如果改为是默认价格，则需要更新其他的价格为非默认价格
		ProductPrice p = price.getPrice();
		if(p.isDefaultPrice()){
			productPriceService.updateDefaultPrices(p.getId());
		}
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.productPrice;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/price/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProductPrice(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sPriceid = request.getParameter("priceId");

		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long priceId = Long.parseLong(sPriceid);
			ProductPrice price = productPriceService.getById(priceId);
			

			if(price==null) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			} 
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(price.getProductAvailability().getProduct().getMerchantStore().getId().intValue()!=store.getId()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
					return resp.toJSONString();
				}
			}
			
			productPriceService.delete(price);
			
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		System.out.println(returnString);
		return returnString;
	}
	
	/**
	 * 删除产品价格集合
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/price/removes.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProductPrices(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String priceIds = request.getParameter("priceId");//传递id集合，用逗号分隔
		AjaxResponse resp = new AjaxResponse();
		Long productId = null;
		Product product = null;
		
		try {
			productId = Long.parseLong(request.getParameter("productId"));
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}
		product = productService.getById(productId);

		
		if(product==null) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}
		if (StringUtils.isBlank(priceIds)) {
			resp.setStatusMessage(messages.getMessage("message.error", locale));
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
			return resp.toJSONString();
		} else {
			String[] ids = priceIds.split(",");
			//删除商品价格信息
			for (int i = 0; i < ids.length; i++) {
				if (!StringUtils.isBlank(ids[i])) {
					resp = deletePrice(ids[i], request, locale);
					if(resp.getStatus() == AjaxResponse.RESPONSE_STATUS_FAIURE) {
						return resp.toJSONString();
					}
				}
			}
		}
		//有价格加一分		
		try {			
			product = productService.getById(productId);
			productService.saveOrUpdate(product);
		} catch (ServiceException e) {
			
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	private AjaxResponse deletePrice(String sPriceid, HttpServletRequest request, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		try {
			Long priceId = Long.parseLong(sPriceid);
			ProductPrice price = productPriceService.getById(priceId);
			if(price==null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp;
			} 
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(price.getProductAvailability().getProduct().getMerchantStore().getId().intValue()!=store.getId()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
					return resp;
				}
			}
			productPriceService.delete(price);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		return resp;
	}
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue-products", "catalogue-products");
		activeMenus.put("catalogues", "catalogues");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue-products");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	@SuppressWarnings("rawtypes")
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/prices/doSave.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String doSave(@RequestBody List<Map<String, String>> prices, 
			HttpServletRequest request, HttpServletResponse response, Locale locale) {
		List<ReadablePrice> pricesList = new ArrayList<ReadablePrice>();
		if (prices != null && prices.size()>0) {
			for (int i = 0; i < prices.size(); i++) {
				ReadablePrice read = new ReadablePrice();
				Map<String, String> map = prices.get(i);
				try {
					BeanUtils.populate(read, map);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				pricesList.add(read);
			}
		}
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();
		
		Long productId = null;
		Product product = null;
		
		try {
			productId = Long.parseLong(request.getParameter("productId"));
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			//admin has all products
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){ //非admin角色，则需要判断是否是本店铺的商品
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("Product id is not valid");
					String returnString = resp.toJSONString();
					return returnString;
				}
			} 
			List<ProductPrice> productPriceList = new ArrayList<ProductPrice>();
			for (int i = 0; i < pricesList.size(); i++) {
				com.salesmanager.web.admin.entity.catalog.ReadablePrice price = pricesList.get(i);
				ProductPrice newPrice = new ProductPrice();
				//获取dbprice信息
				if (StringUtils.isNotBlank(price.getPriceId())) {
					newPrice = productPriceService.getById(Long.valueOf(price.getPriceId()));
				}
				//validate price
				BigDecimal submitedPrice = null;
				try {
					submitedPrice = priceUtil.getAmount(price.getPrice());
				} catch (Exception e) {
					resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString(messages.getMessage("NotEmpty.product.productPrice", locale));
					String returnString = resp.toJSONString();
					return returnString;
				}
				
				//validate discount price
				BigDecimal submitedDiscountPrice = null;
				
				if(!StringUtils.isBlank(price.getSpecialPrice())) {
					try {
						submitedDiscountPrice = priceUtil.getAmount(price.getSpecialPrice());
					} catch (Exception e) {
						resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
						resp.setErrorString(messages.getMessage("NotEmpty.product.productPrice", locale));
						String returnString = resp.toJSONString();
						return returnString;
					}
				}
				
				//validate start date
				if(!StringUtils.isBlank(price.getBegindt())) {
					try {
						Date startDate = DateUtil.getDate(price.getBegindt());
						newPrice.setProductPriceSpecialStartDate(startDate);
					} catch (Exception e) {
						resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
						resp.setErrorString(messages.getMessage("message.invalid.date", locale));
						String returnString = resp.toJSONString();
						return returnString;
					}
				}
				
				if(!StringUtils.isBlank(price.getEnddt())) {
					try {
						Date endDate = DateUtil.getDate(price.getEnddt());
						newPrice.setProductPriceSpecialEndDate(endDate);
					} catch (Exception e) {
						resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
						resp.setErrorString(messages.getMessage("message.invalid.date", locale));
						String returnString = resp.toJSONString();
						return returnString;
					}
				}
				
				newPrice.setProductPriceAmount(submitedPrice);
				newPrice.setProductPriceSpecialAmount(submitedDiscountPrice);
				newPrice.setProductPricePeriod(price.getPeriod());
				
				ProductAvailability productAvailability = null;
				
				Set<ProductAvailability> availabilities = product.getAvailabilities();
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {//TODO to be updated when multiple regions is implemented
						productAvailability = availability;
						break;
					}
				}
				
				//Language language = (Language)request.getAttribute("LANGUAGE");
				Language language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
				//List<Language> languages = store.getLanguages();
				
				Set<ProductPriceDescription> descriptions = new HashSet<ProductPriceDescription>();
				ProductPriceDescription description = newPrice.getProductPriceDescription();
				if (description == null) {
					description = new ProductPriceDescription();
					description.setLanguage(language);
					description.setProductPrice(newPrice);
					descriptions.add(description);
					newPrice.setDescriptions(descriptions);
				}
				description.setName(price.getName());
				newPrice.setProductAvailability(productAvailability);
				productPriceList.add(newPrice);
			}
			
			//新增价格加一分
			boolean flag =true;
			for (int i = 0; i < productPriceList.size(); i++) {
				ProductPrice price = productPriceList.get(i);
				if(price.getId()!=null && price.getId()>0) {
					flag=false;
					break;
				}
			}	
				
			if(flag == true){
				flag=false;
				for (int i = 0; i < productPriceList.size(); i++) {
					ProductPrice price = productPriceList.get(i);
					int k=price.getProductPriceAmount().compareTo(BigDecimal.ZERO); 
					if(k==1) {
						flag = true;
						break;
					}
				}	
			}
			productPriceService.saveOrUpdateList(productPriceList);
			//新增价格加一分
			if(flag==true){
				product = productService.getById(product.getId());
//				int score = product.getQualityScore();
//				score++;
//				if(score>100) score=100;
				productService.saveOrUpdate(product);
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

}
