package com.salesmanager.web.admin.controller.products;

import java.awt.Color;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.selfproof.ProductSelfproofService;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.service.BasedataTypeService;
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
import com.salesmanager.web.utils.CategoryNode;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.ImageMarkLogoByIcon;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.RadomSixNumber;

@Controller
public class ProductSelfproofController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSelfproofController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BasedataTypeService basedataTypeService;
	
	@Autowired
	private ProductSelfproofService productSelfproofService;
	
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
	
	@Autowired
	private CategoryNode categoryNode;
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/selfproofs.html", method=RequestMethod.GET)
	public String getProductselfproofs(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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

		return ControllerConstants.Tiles.Product.productSelfproofs;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/selfproof/edit.html", method=RequestMethod.GET)
	public String editProductSelfproof(@RequestParam("id") long productSelfproofId, @RequestParam("productId") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		
		ProductSelfproof selfproof = productSelfproofService.getById(productSelfproofId);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
//		selfproof.setProduct(product);
		setMenu(model,request);
		model.addAttribute("product",product);
		model.addAttribute("selfproof",selfproof);
		return ControllerConstants.Tiles.Product.productSelfproof;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/selfproofs/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageSelfproofs(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
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
			
			List<ProductSelfproof> selfproofs = productSelfproofService.getByProduct(product);
			
			
			for(ProductSelfproof selfproof : selfproofs) {
				Map entry = new HashMap();
				entry.put("selfproofId", selfproof.getId());
				entry.put("title", selfproof.getTitle());
				entry.put("order", selfproof.getOrder());

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
	@RequestMapping(value="/admin/products/selfproof/create.html", method=RequestMethod.GET)
	public String displayCreateProductSelfproof(@RequestParam("productId") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		ProductSelfproof selfproof = new ProductSelfproof();
		selfproof.setProduct(product);
		setMenu(model,request);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		model.addAttribute("product",product);
		model.addAttribute("selfproof",selfproof);
		return ControllerConstants.Tiles.Product.productSelfproof;
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/selfproof/save.html", method=RequestMethod.POST)
	public String saveProductSelfproof(@Valid @ModelAttribute("selfproof") ProductSelfproof selfproof, @RequestParam(required=false, value="image") MultipartFile imagefile, 
			BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		//dates after save
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Product product = selfproof.getProduct();
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
		
		if(selfproof.getTitle()==null || StringUtils.isBlank(selfproof.getTitle())){
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.selfproof.title", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if(selfproof.getSelfproofImage()==null &&  (imagefile==null || imagefile.isEmpty()) && selfproof.getId()==null){
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.selfproof.image", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Product.productSelfproof;
		}
		String imageName = "";
		if(imagefile!=null && !imagefile.isEmpty()) { 
			imageName = imagefile.getOriginalFilename();
			String imgName = RadomSixNumber.getImageName(imageName);
			selfproof.setSelfproofImage(imgName); //设定图片名称
			ImageMarkLogoByIcon fieImage = new ImageMarkLogoByIcon();
			String urlimage = "img/suiyin.png";
			InputStream in = fieImage.markImageByIcon(imagefile.getInputStream(),urlimage,imagefile.getOriginalFilename().substring(imagefile.getOriginalFilename().indexOf(".")+1,imagefile.getOriginalFilename().length()));
			ImageContentFile cmsContentImage = new ImageContentFile();
	        cmsContentImage.setFileName(imgName);
	        cmsContentImage.setFile( in );
	        cmsContentImage.setFileContentType(FileContentType.PRODUCT_SELFPROOF);
	        
	        productImageService.addProductRelatedImage(dbProduct, cmsContentImage);
		} 
		
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		
		Category category = cmap.get(Long.parseLong(dbProduct.getCategoryId()));
		String proofType=""; 
		
		switch (categoryNode.getCateType(category)){
			case 1:
				proofType="BTYPE_SELFPROOF";
				break;
			case 2:
				proofType="BTYPE_SELFPROOF_CONSUMABLE";
				break;
			case 3:
				proofType="BTYPE_SELFPROOF_INSTRUMENT";
				break;
			case 4:
				proofType="BTYPE_SELFPROOF_SERVICE";
				break;	
				
		}
		selfproof.setSelfproofType(proofType);
		
		productSelfproofService.saveOrUpdate(dbProduct,selfproof);
		dbProduct = productService.getById(product.getId());
		productService.saveOrUpdate(dbProduct);
		
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		model.addAttribute("selfproof",selfproof);
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.productSelfproof;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/selfproof/remove.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String deleteProductSelfproof(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sSelfproofId = request.getParameter("selfproofId");

		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();

		try {
			
			Long selfproofId = Long.parseLong(sSelfproofId);
			ProductSelfproof selfproof = productSelfproofService.getById(selfproofId);
			
			Product product = productService.getById(selfproof.getProduct().getId());
			if(selfproof==null) {
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
			selfproof.setProduct(product);
			//productSelfproofService.delete(selfproof);
			AuditSection auditSection = product.getAuditSection();
			productSelfproofService.delete(selfproof);
			product = productService.getById(product.getId());
			//重新计算分数
			productService.saveOrUpdate(product);
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.selfproof",customerLocale));
				}
			}
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product selfproof", e);
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
	@RequestMapping(value="/admin/products/selfproof/removeImage.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String iid = request.getParameter("selfproofId");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(iid);
			ProductSelfproof productSelfproof = productSelfproofService.getById(id);
			Product product = productService.getById(productSelfproof.getProduct().getId());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);		
					return resp.toJSONString();
				}
			}
			if(productSelfproof==null) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				productImageService.removeProductRelatedImage(product, productSelfproof.getSelfproofImage(), FileContentType.PRODUCT_SELFPROOF);
				productSelfproof.setSelfproofImage("");
				productSelfproofService.update(productSelfproof); //保持selfproof
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while saving productselfproof", e);
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
	@RequestMapping(value="/admin/products/selfproof/removes.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String deleteProductSelfproofs(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String selfproofIds = request.getParameter("selfproofId");//传递id集合，用逗号分隔
		AjaxResponse resp = new AjaxResponse();
		
		if (StringUtils.isBlank(selfproofIds)) {
			resp.setStatusMessage(messages.getMessage("message.error", locale));
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
			return resp.toJSONString();
		} else {
			String[] ids = selfproofIds.split(",");
			//删除商品价格信息
			for (int i = 0; i < ids.length; i++) {
				if (!StringUtils.isBlank(ids[i])) {
					resp = deleteSelfproof(ids[i], request, locale);
					if(resp.getStatus() == AjaxResponse.RESPONSE_STATUS_FAIURE) {
						return resp.toJSONString();
					}
				}
			}
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	private AjaxResponse deleteSelfproof(String sSelfproofid, HttpServletRequest request, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		try {
			Long selfproofId = Long.parseLong(sSelfproofid);
			ProductSelfproof selfproof = productSelfproofService.getById(selfproofId);
			Product product = productService.getById(selfproof.getProduct().getId());
			if(selfproof==null ) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp;
			} 
			AuditSection auditSection = product.getAuditSection();
			selfproof.setProduct(product);
			productSelfproofService.delete(selfproof);
			product = productService.getById(product.getId());
			productService.saveOrUpdate(product);
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.selfproof",customerLocale));
				}
			}
			//productSelfproofService.delete(selfproof);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch (Exception e) {
			LOGGER.error("Error while deleting product selfproof", e);
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
