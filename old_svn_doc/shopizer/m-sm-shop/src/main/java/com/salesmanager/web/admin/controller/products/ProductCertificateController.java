package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
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

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.certificate.ProductCertificateService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
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
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.PinyinUtils;
import com.salesmanager.web.utils.RadomSixNumber;

@Controller
public class ProductCertificateController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductCertificateController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCertificateService productCertificateService;
	
	@Autowired
	private BasedataTypeService basedataTypeService;
	
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	ProductImageService productImageService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/certificates.html", method=RequestMethod.GET)
	public String getProductcertificates(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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

		return ControllerConstants.Tiles.Product.productCertificates;
		
	}
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/certificate/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = basedataTypeService.getStoreName(BasedataTypeEnum.BTYPE_CERTIFICATE.name());
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
	@RequestMapping(value="/admin/products/certificate/edit.html", method=RequestMethod.GET)
	public String editProductCertificate(@RequestParam("id") long productCertificateId, @RequestParam("productId") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		ProductCertificate certificate = productCertificateService.getById(productCertificateId);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		//List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_CERTIFICATE.name()}, orderby);
//		List<BasedataType> baseTypelist = basedataTypeService
//		certificate.setProduct(product);
		model.addAttribute("certiname",certificate.getBasedataType().getName());
		setMenu(model,request);
		model.addAttribute("product",product);
		model.addAttribute("certificate",certificate);
		//model.addAttribute("basedataType", basedataTypeList);
		return ControllerConstants.Tiles.Product.productCertificate;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/certificates/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageCertificates(HttpServletRequest request, HttpServletResponse response) {

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
			
			List<ProductCertificate> certificates = productCertificateService.getByProduct(product);
			
			
			for(ProductCertificate certificate : certificates) {
				Map entry = new HashMap();
				entry.put("certificateId", certificate.getId());
				entry.put("name", certificate.getBasedataType().getName());
				entry.put("baseinfo", certificate.getBaseinfo());
				entry.put("title", certificate.getTitle());
				entry.put("order", certificate.getOrder());
				entry.put("docUrl", certificate.getDocUrl());

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
	@RequestMapping(value="/admin/products/certificate/create.html", method=RequestMethod.GET)
	public String displayCreateProductCertificate(@RequestParam("productId") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		ProductCertificate certificate = new ProductCertificate();
		certificate.setProduct(product);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		//List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_CERTIFICATE.name()}, orderby);
		//model.addAttribute("basedataType", basedataTypeList);
		setMenu(model,request);
		model.addAttribute("product",product);
		model.addAttribute("certificate",certificate);
		return ControllerConstants.Tiles.Product.productCertificate;
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/certificate/save.html", method=RequestMethod.POST)
	public String saveProductCertificate(@Valid @ModelAttribute("certificate") ProductCertificate certificate, @RequestParam(required=false, value="image") MultipartFile imagefile, 
			BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		//dates after save
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Product product = certificate.getProduct();
		Product dbProduct = productService.getById(product.getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){
			if(dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}
		
		model.addAttribute("product",dbProduct);
		
		BasedataType bs = certificate.getBasedataType();
		//新增
		String code = request.getParameter("searchstorename");
		boolean isCreate =false;
		if(code !=null && !StringUtils.isBlank(code)){
			model.addAttribute("certiname",code);
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
							if(bss.get(i).getType().equalsIgnoreCase(BasedataTypeEnum.BTYPE_CERTIFICATE.name())){
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
				bs.setCode("userdefine_certificate");
				bs.setType(BasedataTypeEnum.BTYPE_CERTIFICATE.name());
				bs.setValue("0.8");
				basedataTypeService.create(bs);				
			}
		}else{
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.certificate.name", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if(certificate.getBaseinfo()==null || certificate.getBaseinfo().equalsIgnoreCase("")){
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.certificate.baseinfo", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Product.productCertificate;
		}
		String imageName = "";
		if(imagefile!=null && !imagefile.isEmpty()) { 
			imageName = imagefile.getOriginalFilename();
			String imgName =RadomSixNumber.getImageName(imageName);
			certificate.setCertificateImage(imgName); //设定图片名称
			ImageContentFile cmsContentImage = new ImageContentFile();	       
	        cmsContentImage.setFile( imagefile.getInputStream() );
	        cmsContentImage.setFileContentType(FileContentType.PRODUCT_CERTIFICATE);
	        cmsContentImage.setFileName(imgName);
	        productImageService.addProductRelatedImage(dbProduct, cmsContentImage);
		}
		/**
		boolean isFirst = false;
		if (dbProduct.getCertificates() == null || dbProduct.getCertificates().size()==0) isFirst=true;		
		dbProduct.setQualityScore(BaseDataUtils.setProductQuality(dbProduct, basedataTypeService.getById(certificate.getBasedataType().getId()),isFirst));
		//certificate.setProduct(product);
		if (certificate.getId()!=null && certificate.getId()>0) {
			productCertificateService.update(certificate);
		} else {
			/*AuditSection audit = new AuditSection();
			audit.setDateCreated(dt);
			audit.setDateCreated(dt);
			certificate.setAuditSection(audit);*/
		/**
			productCertificateService.save(certificate);
		}
		productService.saveOrUpdate(dbProduct);*/
		certificate.setBasedataType(bs);
		
		productCertificateService.saveOrUpdate(certificate);
		//if(certificate.getBasedataType()!=null){
			//bs = basedataTypeService.getById(certificate.getBasedataType().getId());
			model.addAttribute("certiname",bs.getName());
		//}
		
			dbProduct = productService.getById(certificate.getProduct().getId());
			productService.saveOrUpdate(dbProduct);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		//List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_CERTIFICATE.name()}, orderby);
		//model.addAttribute("basedataType", basedataTypeList);
		
		model.addAttribute("certificate",certificate);
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.productCertificate;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/certificate/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProductCertificate(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sCertificateId = request.getParameter("certificateId");

		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();

		try {
			
			Long certificateId = Long.parseLong(sCertificateId);
			ProductCertificate certificate = productCertificateService.getById(certificateId);
			
			Product product = productService.getById(certificate.getProduct().getId());
			if(certificate==null) {
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
			/**
			boolean isLast = false;
			if(product.getCertificates().size()==1) isLast=true;
			product.setQualityScore(BaseDataUtils.removeProductQuality(product, basedataTypeService.getById(certificate.getBasedataType().getId()), isLast));
			productService.saveOrUpdate(product);
			//certificate.setProduct(product);
			productCertificateService.delete(certificate);
			//productService.saveOrUpdate(product);
			 * */
			AuditSection auditSection = product.getAuditSection();
			productCertificateService.deletC(certificate);
			product = productService.getById(certificate.getProduct().getId());
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.certificate",customerLocale));
				}
			}
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product certificate", e);
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
	@RequestMapping(value="/admin/products/certificate/removeImage.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String iid = request.getParameter("certificateId");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(iid);
			ProductCertificate productCertificate = productCertificateService.getById(id);
			Product product = productService.getById(productCertificate.getProduct().getId());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);		
					return resp.toJSONString();
				}
			}
			if (productCertificate == null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
			} else {
				productImageService.removeProductRelatedImage(product, productCertificate.getCertificateImage(), FileContentType.PRODUCT_CERTIFICATE);
				productCertificate.setCertificateImage("");
				productCertificateService.update(productCertificate); //保持certificate
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while saving productcertificate", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		LOGGER.info(returnString);
		return returnString;
	}
	
	/**
	 * 删除文献引用集合
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/certificate/removes.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProductCertificates(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String certificateIds = request.getParameter("certificateId");//传递id集合，用逗号分隔
		AjaxResponse resp = new AjaxResponse();
		
		if (StringUtils.isBlank(certificateIds)) {
			resp.setStatusMessage(messages.getMessage("message.error", locale));
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
			return resp.toJSONString();
		} else {
			String[] ids = certificateIds.split(",");
			//删除商品价格信息
			for (int i = 0; i < ids.length; i++) {
				if (!StringUtils.isBlank(ids[i])) {
					resp = deleteCertificate(ids[i], request, locale);
					if(resp.getStatus() == AjaxResponse.RESPONSE_STATUS_FAIURE) {
						return resp.toJSONString();
					}
				}
			}
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	private AjaxResponse deleteCertificate(String sCertificateid, HttpServletRequest request, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		try {
			Long certificateId = Long.parseLong(sCertificateid);
			ProductCertificate certificate = productCertificateService.getById(certificateId);
			Product product = productService.getById(certificate.getProduct().getId());
			if(certificate==null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp;
			} 
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
					return resp;
				}
			}/**
			boolean isLast = false;
			if(product.getCertificates().size()==1) isLast=true;
			product.setQualityScore(BaseDataUtils.removeProductQuality(product, basedataTypeService.getById(certificate.getBasedataType().getId()), isLast));
			productService.saveOrUpdate(product);
			//certificate.setProduct(product);
			productCertificateService.delete(certificate);*/
			AuditSection auditSection = product.getAuditSection();
			productCertificateService.deletC( certificate);
			product = productService.getById(certificate.getProduct().getId());
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.certificate",customerLocale));
				}
			}
			//
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch (Exception e) {
			LOGGER.error("Error while deleting product certificate", e);
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
