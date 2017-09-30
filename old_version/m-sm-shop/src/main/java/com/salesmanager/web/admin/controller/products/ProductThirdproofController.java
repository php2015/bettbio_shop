package com.salesmanager.web.admin.controller.products;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.thirdproof.ProductThirdproofService;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.service.BasedataTypeService;
import com.salesmanager.core.business.content.model.BasedataTypeEnum;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.ImageContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.PropertiesUtils;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.PinyinUtils;
import com.salesmanager.web.utils.RadomSixNumber;

@Controller
public class ProductThirdproofController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductThirdproofController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BasedataTypeService basedataTypeService;
	
	@Autowired
	private ProductThirdproofService productThirdproofService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	ProductImageService productImageService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproofs.html", method=RequestMethod.GET)
	public String getProductthirdproofs(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		//get the product and validate it belongs to the current merchant
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
		
		model.addAttribute("product",product);

		return ControllerConstants.Tiles.Product.productThirdproofs;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproof/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = basedataTypeService.getStoreName(BasedataTypeEnum.BTYPE_THIRDPROOF.name());
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
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproof/edit.html", method=RequestMethod.GET)
	public String editProductThirdproof(@RequestParam("id") long productThirdproofId, @RequestParam("productId") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		
		ProductThirdproof thirdproof = productThirdproofService.getById(productThirdproofId);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");	
		model.addAttribute("thirdname",thirdproof.getBasedataType().getName());
		//List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_THIRDPROOF.name()}, orderby);
		//model.addAttribute("basedataType", basedataTypeList);
//		thirdproof.setProduct(product);
		setMenu(model,request);
		model.addAttribute("product",product);
		model.addAttribute("thirdproof",thirdproof);
		return ControllerConstants.Tiles.Product.productThirdproof;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproofs/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageThirdproofs(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
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
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("Product id is not valid");
					String returnString = resp.toJSONString();
					return returnString;
				}
			}
			
			List<ProductThirdproof> thirdproofs = productThirdproofService.getByProduct(product);
			
			
			for(ProductThirdproof thirdproof : thirdproofs) {
				Map entry = new HashMap();
				entry.put("thirdproofId", thirdproof.getId());
				entry.put("title", thirdproof.getTitle());
				entry.put("thirddetection", thirdproof.getBasedataType().getName());
				entry.put("order", thirdproof.getOrder());

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
	@RequestMapping(value="/admin/products/thirdproof/create.html", method=RequestMethod.GET)
	public String displayCreateProductThirdproof(@RequestParam("productId") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		ProductThirdproof thirdproof = new ProductThirdproof();
		thirdproof.setProduct(product);
		setMenu(model,request);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		//List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_THIRDPROOF.name()}, orderby);
		//model.addAttribute("basedataType", basedataTypeList);
		model.addAttribute("product",product);
		model.addAttribute("thirdproof",thirdproof);
		return ControllerConstants.Tiles.Product.productThirdproof;
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproof/save.html", method=RequestMethod.POST)
	public String saveProductThirdproof(@Valid @ModelAttribute("thirdproof") ProductThirdproof thirdproof, @RequestParam(required=false, value="image") MultipartFile imagefile, 
			BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		//dates after save
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Product product = thirdproof.getProduct();
		Product dbProduct = productService.getById(product.getId());
		if(dbProduct==null) {
			return "redirect:/admin/products/products.html";
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){
			if(dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}
		model.addAttribute("product",dbProduct);
		//validate image
		/**
		if(imagefile!=null && !imagefile.isEmpty()) {
			try {
				String maxHeight = configuration.getProperty("PRODUCT_IMAGE_MAX_HEIGHT_SIZE");
				String maxWidth = configuration.getProperty("PRODUCT_IMAGE_MAX_WIDTH_SIZE");
				String maxSize = configuration.getProperty("PRODUCT_IMAGE_MAX_SIZE");
				BufferedImage image = ImageIO.read(imagefile.getInputStream());
				
				if(!StringUtils.isBlank(maxHeight)) {
					int maxImageHeight = Integer.parseInt(maxHeight);
					if(image.getHeight()>maxImageHeight) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.height", locale) + " {"+maxHeight+"}");
						result.addError(error);
					}
				}
				
				if(!StringUtils.isBlank(maxWidth)) {
					int maxImageWidth = Integer.parseInt(maxWidth);
					if(image.getWidth()>maxImageWidth) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.width", locale) + " {"+maxWidth+"}");
						result.addError(error);
					}
				}
				
				if(!StringUtils.isBlank(maxSize)) {
					int maxImageSize = Integer.parseInt(maxSize);
					if(imagefile.getSize()>maxImageSize) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.size", locale) + " {"+maxSize+"}");
						result.addError(error);
					}
				}
				
			} catch (Exception e) {
				LOGGER.error("Cannot validate product image", e);
			}

		}
		*/
		BasedataType bs = thirdproof.getBasedataType();
		String code = request.getParameter("searchstorename");
		boolean isCreate =false;
		if(code !=null && !StringUtils.isBlank(code)){
			model.addAttribute("thirdname",code);			
			if(bs.getId() ==null){
				isCreate=true;
			}else{
				bs = basedataTypeService.getById(bs.getId());
				if(!bs.getName().equalsIgnoreCase(code)){
					List<BasedataType> bss = basedataTypeService.getByName(code.trim());
					if(bss==null || bss.size()==0){
						isCreate=true;
					}else{
						for(int i=0;i<bss.size();i++){
							if(bss.get(i).getType().equalsIgnoreCase(BasedataTypeEnum.BTYPE_THIRDPROOF.name())){
								isCreate=false;
								bs=bss.get(i);
								break;
							}
						}
					}
				}
			}//新增
			if(isCreate==true){
				bs = new BasedataType();
				bs.setName(code);
				bs.setCode("userdefine_third");
				bs.setType(BasedataTypeEnum.BTYPE_THIRDPROOF.name());
				bs.setValue("0.6");
				basedataTypeService.create(bs);
			}
		}else{
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.thirdproof.thirddetection", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if(thirdproof.getThirdproofImage()==null &&  (imagefile==null || imagefile.isEmpty())){
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.thirdproof.image", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Product.productThirdproof;
		}
		String imageName = "";
		if(imagefile!=null && !imagefile.isEmpty()) { 
			imageName = imagefile.getOriginalFilename();
			String imageN = RadomSixNumber.getImageName(imageName);
			thirdproof.setThirdproofImage(imageN); //设定图片名称
			ImageContentFile cmsContentImage = new ImageContentFile();
	        cmsContentImage.setFileName(imageN);
	        cmsContentImage.setFile( imagefile.getInputStream() );
	        cmsContentImage.setFileContentType(FileContentType.PRODUCT_THIRDPROOF);
	        
	        productImageService.addProductRelatedImage(dbProduct, cmsContentImage);
		} 
		
		/**
		if (thirdproof.getId()!=null && thirdproof.getId()>0) {
			productThirdproofService.update(thirdproof);
		} else {
			/*AuditSection audit = new AuditSection();
			audit.setDateCreated(dt);
			audit.setDateCreated(dt);
			thirdproof.setAuditSection(audit);*/
		//	productThirdproofService.save(thirdproof);
		//}
		
		thirdproof.setBasedataType(bs);
		productThirdproofService.saveOrUpdate(thirdproof);
		dbProduct = productService.getById(product.getId());
		productService.saveOrUpdate(dbProduct);
		model.addAttribute("thirdname",bs.getName());
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_THIRDPROOF.name()}, orderby);
		model.addAttribute("basedataType", basedataTypeList);
		model.addAttribute("thirdproof",thirdproof);
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.productThirdproof;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproof/remove.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String deleteProductThirdproof(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sThirdproofId = request.getParameter("thirdproofId");

		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();

		try {
			
			Long thirdproofId = Long.parseLong(sThirdproofId);
			ProductThirdproof thirdproof = productThirdproofService.getById(thirdproofId);
			
			Product product = productService.getById(thirdproof.getProduct().getId());
			if(thirdproof==null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			} 
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
					return resp.toJSONString();
				}
			}
			thirdproof.setProduct(product);
			AuditSection auditSection = product.getAuditSection();
			productThirdproofService.delett(thirdproof);
			product = productService.getById(thirdproof.getProduct().getId());
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.thirdproof",customerLocale));
				}
			}
			//productThirdproofService.delete(thirdproof);
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product thirdproof", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	/**
	 * Removes a product image based on the productimage id
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproof/removeImage.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String iid = request.getParameter("thirdproofId");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(iid);
			ProductThirdproof productThirdproof = productThirdproofService.getById(id);
			Product product = productService.getById(productThirdproof.getProduct().getId());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);		
					return resp.toJSONString();
				}
			}
			if(productThirdproof==null) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				productImageService.removeProductRelatedImage(product, productThirdproof.getThirdproofImage(), FileContentType.PRODUCT_THIRDPROOF);
				productThirdproof.setThirdproofImage("");
				productThirdproofService.update(productThirdproof); //保持thirdproof
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while saving productthirdproof", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		LOGGER.info(returnString);
		return returnString;
	}
	
	/**
	 * 删除第三方认证集合
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/thirdproof/removes.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String deleteProductThirdproofs(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String thirdproofIds = request.getParameter("thirdproofId");//传递id集合，用逗号分隔
		AjaxResponse resp = new AjaxResponse();
		
		if (StringUtils.isBlank(thirdproofIds)) {
			resp.setStatusMessage(messages.getMessage("message.error", locale));
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
			return resp.toJSONString();
		} else {
			String[] ids = thirdproofIds.split(",");
			//删除商品价格信息
			for (int i = 0; i < ids.length; i++) {
				if (!StringUtils.isBlank(ids[i])) {
					resp = deleteThirdproof(ids[i], request, locale);
					if(resp.getStatus() == AjaxResponse.RESPONSE_STATUS_FAIURE) {
						return resp.toJSONString();
					}
				}
			}
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	private AjaxResponse deleteThirdproof(String sThirdproofid, HttpServletRequest request, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		try {
			Long thirdproofId = Long.parseLong(sThirdproofid);
			ProductThirdproof thirdproof = productThirdproofService.getById(thirdproofId);
			Product product = productService.getById(thirdproof.getProduct().getId());
			if(thirdproof==null || product.getMerchantStore().getId().intValue()!=store.getId()) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp;
			} 
			thirdproof.setProduct(product);
			AuditSection auditSection = product.getAuditSection();
			productThirdproofService.delett(thirdproof);
			product = productService.getById(thirdproof.getProduct().getId());
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.thirdproof",customerLocale));	
				}
			}
			//productThirdproofService.delete(thirdproof);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch (Exception e) {
			LOGGER.error("Error while deleting product thirdproof", e);
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

}
