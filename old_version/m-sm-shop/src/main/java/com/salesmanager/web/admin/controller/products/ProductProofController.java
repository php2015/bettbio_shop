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

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.proof.ProductProofService;
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
import com.salesmanager.web.utils.CategoryNode;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.PinyinUtils;
import com.salesmanager.web.utils.RadomSixNumber;

@Controller
public class ProductProofController {
private static final Logger LOGGER = LoggerFactory.getLogger(ProductProofController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BasedataTypeService basedataTypeService;
	
	@Autowired
	private ProductProofService productProofService;
	
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
	@RequestMapping(value="/admin/products/proofs.html", method=RequestMethod.GET)
	public String getProductproofs(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		Category c = cmap.get(Long.parseLong(product.getCategoryId()));
		
		model.addAttribute("product",product);
		if(categoryNode.getProductType(c)==3){
			return "admin-services-proofs";
		}		
		

		return ControllerConstants.Tiles.Product.productProofs;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/proof/edit.html", method=RequestMethod.GET)
	public String editProductProof(@RequestParam("id") long productProofId, @RequestParam("productId") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		
		ProductProof proof = productProofService.getById(productProofId);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");	
		model.addAttribute("proofname",proof.getBasedataType().getName());
		
		setMenu(model,request);
		model.addAttribute("product",product);
		model.addAttribute("proof",proof);
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		Category c = cmap.get(Long.parseLong(product.getCategoryId()));
		if(categoryNode.getProductType(c)==3){
			return "admin-services-proof";
		}
		return ControllerConstants.Tiles.Product.productProof;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/proofs/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageProofs(HttpServletRequest request, HttpServletResponse response) {

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
			
			List<ProductProof> proofs = productProofService.getByProduct(product);
			
			
			for(ProductProof proof : proofs) {
				Map entry = new HashMap();
				entry.put("proofId", proof.getId());
				entry.put("title", proof.getTitle());
				entry.put("buyer", proof.getBasedataType().getName());
				entry.put("dateBuyed", DateUtil.formatDate(proof.getDateBuyed()));
				entry.put("order", proof.getOrder());

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
	@RequestMapping(value="/admin/products/proof/create.html", method=RequestMethod.GET)
	public String displayCreateProductProof(@RequestParam("productId") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		ProductProof proof = new ProductProof();
		proof.setProduct(product);
		setMenu(model,request);
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		//List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_PROOF.name()}, orderby);
		//model.addAttribute("basedataType", basedataTypeList);
		model.addAttribute("product",product);
		model.addAttribute("proof",proof);
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		Category c = cmap.get(Long.parseLong(product.getCategoryId()));
		if(categoryNode.getProductType(c)==3){
			return "admin-services-proof";
		}
		return ControllerConstants.Tiles.Product.productProof;
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/proof/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = basedataTypeService.getStoreName(BasedataTypeEnum.BTYPE_PROOF.name());
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
	@RequestMapping(value="/admin/products/proof/save.html", method=RequestMethod.POST)
	public String saveProductProof(@Valid @ModelAttribute("proof") ProductProof proof, @RequestParam(required=false, value="image") MultipartFile imagefile, 
			BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		//dates after save
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Product product = proof.getProduct();
		Product dbProduct = productService.getById(product.getId());
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

		}*/
		BasedataType bs = proof.getBasedataType();
		String code = request.getParameter("searchstorename");
		boolean isCreate =false;
		if(code !=null && !StringUtils.isBlank(code)){
			model.addAttribute("proofname",code);
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
							if(bss.get(i).getType().equalsIgnoreCase(BasedataTypeEnum.BTYPE_PROOF.name())){
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
				bs.setCode("userdefine_proof");
				bs.setType(BasedataTypeEnum.BTYPE_PROOF.name());
				bs.setValue("0.6");
				basedataTypeService.create(bs);
			}
		}else{
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.proof.buyer", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if(proof.getDateBuyed()==null ){
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.proof.dateBuyed", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if(proof.getProofImage()==null &&  (imagefile==null || imagefile.isEmpty())){
			ObjectError error = new ObjectError("name",messages.getMessage("label.product.proof.image", locale) + messages.getMessage("NotEmpty.normal", locale));
			result.addError(error);
		}
		if (result.hasErrors()) {		
			
			return ControllerConstants.Tiles.Product.productProof;
		}
		String imageName = "";
		if(imagefile!=null && !imagefile.isEmpty()) { 
			imageName = imagefile.getOriginalFilename();
			String imgName =RadomSixNumber.getImageName(imageName);
			proof.setProofImage(imgName); //设定图片名称
			ImageContentFile cmsContentImage = new ImageContentFile();
	        cmsContentImage.setFileName(imgName);
	        cmsContentImage.setFile( imagefile.getInputStream() );
	        cmsContentImage.setFileContentType(FileContentType.PRODUCT_PROOF);
	        
	        productImageService.addProductRelatedImage(dbProduct, cmsContentImage);
		} 
		
		/**
		if (proof.getId()!=null && proof.getId()>0) {
			productProofService.update(proof);
		} else {
			/*AuditSection audit = new AuditSection();
			audit.setDateCreated(dt);
			audit.setDateCreated(dt);
			proof.setAuditSection(audit);*/
		//	productProofService.save(proof);
		//}
		
		proof.setBasedataType(bs);
		productProofService.saveOrUpdate(proof);
		dbProduct = productService.getById(product.getId());
		productService.saveOrUpdate(dbProduct);
		model.addAttribute("proofname",bs.getName());
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("order", "asc");		
		//List<BasedataType> basedataTypeList = basedataTypeService.findByProperties(new String[]{"type"}, new Object[]{BasedataTypeEnum.BTYPE_PROOF.name()}, orderby);
		//model.addAttribute("basedataType", basedataTypeList);
		model.addAttribute("proof",proof);
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.productProof;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/proof/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProductProof(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sProofId = request.getParameter("proofId");

		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();

		try {
			
			Long proofId = Long.parseLong(sProofId);
			ProductProof proof = productProofService.getById(proofId);
			
			Product product = productService.getById(proof.getProduct().getId());
			if(proof==null) {

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
			proof.setProduct(product);
			AuditSection auditSection = product.getAuditSection();
			productProofService.deletp(proof);
			product = productService.getById(proof.getProduct().getId());
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.proof",customerLocale));
				}
			}
			//productProofService.delete(proof);
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product proof", e);
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
	@RequestMapping(value="/admin/products/proof/removeImage.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String iid = request.getParameter("proofId");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(iid);
			ProductProof productProof = productProofService.getById(id);
			Product product = productService.getById(productProof.getProduct().getId());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);		
					return resp.toJSONString();
				}
			}
			if(productProof==null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
			} else {
				
				productImageService.removeProductRelatedImage(product, productProof.getProofImage(), FileContentType.PRODUCT_PROOF);
				productProof.setProofImage("");
				productProofService.update(productProof); //保持proof
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while saving productproof", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		LOGGER.info(returnString);
		return returnString;
	}
	
	/**
	 * 删除购买凭证集合
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/proof/removes.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProductProofs(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String proofIds = request.getParameter("proofId");//传递id集合，用逗号分隔
		AjaxResponse resp = new AjaxResponse();
		
		if (StringUtils.isBlank(proofIds)) {
			resp.setStatusMessage(messages.getMessage("message.error", locale));
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
			return resp.toJSONString();
		} else {
			String[] ids = proofIds.split(",");
			//删除商品购买凭证信息
			for (int i = 0; i < ids.length; i++) {
				if (!StringUtils.isBlank(ids[i])) {
					resp = deleteProof(ids[i], request, locale);
					if(resp.getStatus() == AjaxResponse.RESPONSE_STATUS_FAIURE) {
						return resp.toJSONString();
					}
				}
			}
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	private AjaxResponse deleteProof(String sProofid, HttpServletRequest request, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		try {
			Long proofId = Long.parseLong(sProofid);
			ProductProof proof = productProofService.getById(proofId);
			Product product = productService.getById(proof.getProduct().getId());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(proof==null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp;
			} 
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
					return resp;
				}
			}
			proof.setProduct(product);
			AuditSection auditSection = product.getAuditSection();
			//productProofService.delete(proof);
			productProofService.deletp(proof);
			product = productService.getById(proof.getProduct().getId());
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				if(auditSection.getAudit()>0){
					Language language = (Language)request.getAttribute("LANGUAGE");
					Locale customerLocale = LocaleUtils.getLocale(language);
					String result =messages.getMessage("label.bettbio.product.failed.audit",customerLocale);
					emailTemplatesUtils.sendDeleteQualityEmail(product, store, customerLocale,  request.getContextPath(), result, messages.getMessage("label.product.proof",customerLocale));
				}
			}
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch (Exception e) {
			LOGGER.error("Error while deleting product proof", e);
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
