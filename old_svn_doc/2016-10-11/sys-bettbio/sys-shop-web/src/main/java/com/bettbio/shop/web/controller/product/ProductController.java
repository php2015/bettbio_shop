package com.bettbio.shop.web.controller.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.email.constant.EmailConstants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.BasedataType;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.mongo.model.AuthorityCertification;
import com.bettbio.core.mongo.model.ExperimentReport;
import com.bettbio.core.mongo.model.Literature;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.ProductPrice;
import com.bettbio.core.mongo.model.PurchaseVoucher;
import com.bettbio.core.mongo.service.ProductService;
import com.bettbio.core.service.BasedataTypeService;
import com.bettbio.core.service.ProductBrandService;
import com.bettbio.core.service.ProductClassificationService;
import com.bettbio.core.service.SStoreService;
import com.bettbio.core.service.SysEmailService;

/**
 * 产品控制器
 * 
 * @author chang
 *
 */
@Controller
@RequestMapping("admin/product")
public class ProductController extends BaseController {

	@Autowired
	ProductClassificationService productClassificationService;
	
	@Autowired
	ProductBrandService productBrandService;

	@Autowired
	ProductService productService;
	
	@Autowired
	SStoreService storeService;
	
	@Autowired
	BasedataTypeService basedataTypeService;

	@Autowired
	SysEmailService sysEmailService;
	
	String collectionName = "product";
	
	@RequestMapping("/create")
	public String create() {
		return "/admin/product/create";
	}

	@RequestMapping("/createService")
	public String createService() {
		return "/admin/product/createService";
	}

	@RequestMapping("/createConsumable")
	public String createConsumable() {
		return "/admin/product/createConsumable";
	}

	
	@RequestMapping("/create{p1}Page")
	public String createPricePage(@PathVariable String p1,int id, String code, Model model){
		model.addAttribute("id", id);
		model.addAttribute("code", code);
		return "/admin/product/create"+p1;
	}
	
	/**
	 * 价格集
	 * 
	 * @param model
	 * @param reagentProduct
	 * @return
	 */
	@RequestMapping("/price")
	public String price(Model model, int id) {
		Product product = new Product();
		product = productService.selectProductById(id);
		model.addAttribute("productPrices", product.getProductPrices());
		model.addAttribute("reagentProduct", product);
		return "/admin/product/price";
	}

	/**
	 * 凭证集
	 */
	@RequestMapping("/voucher")
	public String voucher(Model model, int id) {
		Product product = new Product();
		product = productService.selectProductById(id);
		model.addAttribute("purchaseVouchers", product.getPurchaseVouchers());
		model.addAttribute("reagentProduct", product);
		return "/admin/product/voucher";
	}

	/**
	 * 第三方认证集
	 */
	@RequestMapping("/attestation")
	public String attestation(Model model, int id) {
		Product product = new Product();
		product = productService.selectProductById(id);
		model.addAttribute("authorityCertifications", product.getAuthorityCertifications());
		model.addAttribute("reagentProduct", product);
		return "/admin/product/attestation";
	}

	/**
	 * 文献引用集
	 */
	@RequestMapping("/literature")
	public String literatures(Model model, int id) {
		Product product = new Product();
		product = productService.selectProductById(id);
		model.addAttribute("literatures", product.getLiteratures());
		model.addAttribute("reagentProduct", product);
		return "/admin/product/literature";
	}

	/**
	 * 实验报告集
	 */
	@RequestMapping("/experimentReport")
	public String experimentReport(Model model, int id) {
		Product product = new Product();
		product = productService.selectProductById(id);
		model.addAttribute("experimentReports", product.getExperimentReports());
		model.addAttribute("reagentProduct", product);
		return "/admin/product/experimentReport";
	}

	/**
	 * 新增试剂产品
	 * 
	 * @param reagentProduct
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insertReagentProduct")
	public AjaxResponse insertReagentProduct(HttpServletRequest request, Product product) {
		try {
			SStoreUser sStoreUser = (SStoreUser) request.getSession().getAttribute(Constants.CURRENT_USER);
			SStore store = new SStore();
			store.setCode(sStoreUser.getStoreCode());
			store = storeService.selectByCode(store);
			product.setStore(store);
			product.setId(CodeUtils.getProductId());
			product.setCode(CodeUtils.getCode());
			product.setCreateDate(new Date());
			if(!StringUtils.isEmpty(product.getProductBrand().getName())){
				product.setProductBrand(productBrandService.selectByKey(product.getProductBrand().getId()));
			}
			String id = product.getProductClass().getCode();
			if(!StringUtils.isEmpty(id))
				product.setProductClass(productClassificationService.selectByKey(Integer.parseInt(id)));
			productService.insertProduct(product);
			//发送邮件
			Map<String, String> templateTokens =new HashMap<String, String>();
			templateTokens.put(EmailConstants.LABEL_HI,"Hi");
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, "BettBio");
	        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, "product sku:  "+product.getCode()+"\n product name:  "+product.getProductNameCh());
	        templateTokens.put("EMAIL_FOOTER_COPYRIGHT", "Copyright @ Shopizer 2016, All Rights Reserved!");
			templateTokens.put("EMAIL_DISCLAIMER", "Disclaimer text goes here...");
			templateTokens.put("EMAIL_SPAM_DISCLAIMER", "Spam Disclaimer text goes here...");
	        if(store.getLogoUrl()!=null) {
				StringBuilder logoPath = new StringBuilder();
				logoPath.append("<img src='").append("http").append("://").append(request.getContextPath()).append(store.getLogoUrl()).append("' style='max-width:400px;height:70px'>");
				templateTokens.put("LOGOPATH",logoPath.toString());
			} else {
				templateTokens.put("LOGOPATH",store.getName());
			}
	        sysEmailService.sendProductEmail(templateTokens);
		} catch (Exception e) {
			return AjaxResponse.fail("保存失败");
		}
		return AjaxResponse.success("保存成功");
	}

	/**
	 * 新增仪器耗材产品
	 * 
	 * @param consumableMaterialProduct
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insertConsumableMaterialProduct")
	public AjaxResponse insertConsumableMaterialProduct(HttpServletRequest request,
			Product product) {
		try {
			SStoreUser sStoreUser = (SStoreUser) request.getSession().getAttribute(Constants.CURRENT_USER);
			SStore store = new SStore();
			store.setCode(sStoreUser.getStoreCode());
			store = storeService.selectByCode(store);
			product.setStore(store);
			product.setId(CodeUtils.getProductId());
			product.setCode(CodeUtils.getCode());
			product.setCreateDate(new Date());
			if(!StringUtils.isEmpty(product.getProductBrand().getName())){
				product.setProductBrand(productBrandService.selectByKey(product.getProductBrand().getId()));
			}
			String id = product.getProductClass().getCode();
			if(!StringUtils.isEmpty(id))
				product.setProductClass(productClassificationService.selectByKey(Integer.parseInt(id)));
			productService.insertProduct(product);
			//发送邮件
			Map<String, String> templateTokens =new HashMap<String, String>();
			templateTokens.put(EmailConstants.LABEL_HI,"Hi");
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, "BettBio");
	        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, "product sku:  "+product.getCode()+"\n product name:  "+product.getProductNameCh());
	        templateTokens.put("EMAIL_FOOTER_COPYRIGHT", "Copyright @ Shopizer 2016, All Rights Reserved!");
			templateTokens.put("EMAIL_DISCLAIMER", "Disclaimer text goes here...");
			templateTokens.put("EMAIL_SPAM_DISCLAIMER", "Spam Disclaimer text goes here...");
	        if(store.getLogoUrl()!=null) {
				StringBuilder logoPath = new StringBuilder();
				logoPath.append("<img src='").append("http").append("://").append(request.getContextPath()).append(store.getLogoUrl()).append("' style='max-width:400px;height:70px'>");
				templateTokens.put("LOGOPATH",logoPath.toString());
			} else {
				templateTokens.put("LOGOPATH",store.getName());
			}
	        sysEmailService.sendProductEmail(templateTokens);
		} catch (Exception e) {
			return AjaxResponse.fail("保存失败");
		}
		return AjaxResponse.success("保存成功");
	}

	/**
	 * 新增服务产品
	 * 
	 * @param serviceProduct
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insertServiceProduct")
	public AjaxResponse insertServiceProduct(HttpServletRequest request, Product product) {
		try {
			SStoreUser sStoreUser = (SStoreUser) request.getSession().getAttribute(Constants.CURRENT_USER);
			SStore store = new SStore();
			store.setCode(sStoreUser.getStoreCode());
			store = storeService.selectByCode(store);
			product.setStore(store);
			product.setId(CodeUtils.getProductId());
			product.setCode(CodeUtils.getCode());
			product.setCreateDate(new Date());
			if(!StringUtils.isEmpty(product.getProductBrand().getName())){
				product.setProductBrand(productBrandService.selectByKey(product.getProductBrand().getId()));
			}
			String id = product.getProductClass().getCode();
			if(!StringUtils.isEmpty(id))
				product.setProductClass(productClassificationService.selectByKey(Integer.parseInt(id)));
			productService.insertProduct(product);
			//发送邮件
			Map<String, String> templateTokens =new HashMap<String, String>();
			templateTokens.put(EmailConstants.LABEL_HI,"Hi");
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, "BettBio");
	        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, "product sku:  "+product.getCode()+"\n product name:  "+product.getProductNameCh());
	        templateTokens.put("EMAIL_FOOTER_COPYRIGHT", "Copyright @ Shopizer 2016, All Rights Reserved!");
			templateTokens.put("EMAIL_DISCLAIMER", "Disclaimer text goes here...");
			templateTokens.put("EMAIL_SPAM_DISCLAIMER", "Spam Disclaimer text goes here...");
	        if(store.getLogoUrl()!=null) {
				StringBuilder logoPath = new StringBuilder();
				logoPath.append("<img src='").append("http").append("://").append(request.getContextPath()).append(store.getLogoUrl()).append("' style='max-width:400px;height:70px'>");
				templateTokens.put("LOGOPATH",logoPath.toString());
			} else {
				templateTokens.put("LOGOPATH",store.getName());
			}
	        sysEmailService.sendProductEmail(templateTokens);
		} catch (Exception e) {
			return AjaxResponse.fail("保存失败");
		}
		return AjaxResponse.success("保存成功");
	}

	/**
	 * 产品列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String productList(HttpServletRequest request, Model model, Product product,
			String findName) {
		SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		paramMap = getRequestParameter(request);
		paramMap.put("storeCode", sStoreUser.getStoreCode());
		Page<Product> pageInfo;
		try {
			pageInfo = productService.selectProducs(paramMap);
			model.addAttribute("pageInfo", pageInfo);
		} catch (Exception e) {
		}
		model.addAttribute("paramMap", paramMap);
		return "admin/product/list";
	}

	/**
	 * 试剂类商品详情
	 */
	@RequestMapping("/info")
	public String info(Model model, int id) {
		Product product = productService.selectProductById(id);
		model.addAttribute("reagentProduct", product);
		return "/admin/product/info";
	}

	/**
	 * 耗材仪器类商品详情
	 */
	@RequestMapping("/consumableInfo")
	public String consumableInfo(Model model, int id) {
		Product product = productService.selectProductById(id);
		model.addAttribute("consumableMaterialProduct", product);
		return "/admin/product/consumableInfo";
	}

	/**
	 * 服务类商品详情
	 */
	@RequestMapping("/serviceInfo")
	public String serviceInfo(Model model, int id) {
		Product product = productService.selectProductById(id);
		model.addAttribute("serviceProduct", product);
		return "/admin/product/serviceInfo";
	}

	/**
	 * 更新试剂商品
	 */
	@ResponseBody
	@RequestMapping("/updateReagentProduct")
	public AjaxResponse updateReagentProduct(Product product) {
		Product oldProduct = productService.selectProductById(product.getId());
		oldProduct.setProductNo(product.getProductNo());
		oldProduct.setProductClass(productClassificationService.getClassById(Integer.parseInt(product.getProductClass().getCode())));
		oldProduct.setBatchCode(product.getBatchCode());
		oldProduct.setIsEnable(product.getIsEnable());
		oldProduct.setEnableDate(product.getEnableDate());
		oldProduct.setCasCode(product.getCasCode());
		oldProduct.setProductNameCh(product.getProductNameCh());
		oldProduct.setProductNameEn(product.getProductNameEn());
		oldProduct.setSimpleDescription(product.getSimpleDescription());
		oldProduct.setStorageCondition(product.getStorageCondition());
		oldProduct.setDetailedDescription(product.getDetailedDescription());
		oldProduct.setImgUrls(product.getImgUrls());
		oldProduct.setFiles(product.getFiles());
		if(!StringUtils.isEmpty(product.getProductBrand().getName())){
			oldProduct.setProductBrand(productBrandService.selectByKey(product.getProductBrand().getId()));
		}
		int r  = productService.updateOne(oldProduct);
		if(r > 0){
			return AjaxResponse.success("更新成功");
		}
		return AjaxResponse.fail("更新失败");
	}

	/**
	 * 更新耗材类商品
	 */
	@ResponseBody
	@RequestMapping("/updateConsumableMaterialProduct")
	public AjaxResponse updateConsumableMaterialProduct(Product product) {
		Product oldProduct = productService.selectProductById(product.getId());
		oldProduct.setProductNo(product.getProductNo());
		oldProduct.setProductClass(productClassificationService.getClassById(Integer.parseInt(product.getProductClass().getCode())));
		oldProduct.setModel(product.getModel());
		oldProduct.setIsEnable(product.getIsEnable());
		oldProduct.setEnableDate(product.getEnableDate());
		oldProduct.setPlaceOfOrigin(product.getPlaceOfOrigin());
		oldProduct.setProductNameCh(product.getProductNameCh());
		oldProduct.setProductNameEn(product.getProductNameEn());
		oldProduct.setSimpleDescription(product.getSimpleDescription());
		oldProduct.setDetailedDescription(product.getDetailedDescription());
		oldProduct.setImgUrls(product.getImgUrls());
		oldProduct.setFiles(product.getFiles());
		if(!StringUtils.isEmpty(product.getProductBrand().getName())){
			oldProduct.setProductBrand(productBrandService.selectByKey(product.getProductBrand().getId()));
		}
		int r  = productService.updateOne(oldProduct);
		if(r > 0){
			return AjaxResponse.success("更新成功");
		}
		return AjaxResponse.fail("更新失败");
	}

	/**
	 * 更新服务类商品
	 */
	@ResponseBody
	@RequestMapping("/updateServiceProduct")
	public AjaxResponse updateServiceProduct(Product product) {
		Product oldProduct = productService.selectProductById(product.getId());
		oldProduct.setProductNo(product.getProductNo());
		oldProduct.setProductClass(productClassificationService.getClassById(Integer.parseInt(product.getProductClass().getCode())));
		oldProduct.setPrice(product.getPrice());
		oldProduct.setIsEnable(product.getIsEnable());
		oldProduct.setEnableDate(product.getEnableDate());
		oldProduct.setProductNameCh(product.getProductNameCh());
		oldProduct.setProductNameEn(product.getProductNameEn());
		oldProduct.setSimpleDescription(product.getSimpleDescription());
		oldProduct.setDetailedDescription(product.getDetailedDescription());
		oldProduct.setImgUrls(product.getImgUrls());
		oldProduct.setFiles(product.getFiles());
		if(!StringUtils.isEmpty(product.getProductBrand().getName())){
			oldProduct.setProductBrand(productBrandService.selectByKey(product.getProductBrand().getId()));
		}
		int r  = productService.updateOne(oldProduct);
		if(r > 0){
			return AjaxResponse.success("更新成功");
		}
		return AjaxResponse.fail("更新失败");
	}

	/**
	 * 单个删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public AjaxResponse delete(int id) {
		try {
			productService.remove(id);
		} catch (Exception e) {
			return AjaxResponse.fail("删除失败");
		}
		return AjaxResponse.success("删除成功");
	}

	/**
	 * 批量删除
	 */
	@ResponseBody
	@RequestMapping("/deletes")
	public AjaxResponse deletes(String ids) {
		String[] id = ids.split(",");
		ArrayList<Integer> productIds = new ArrayList<Integer>();
		for (int i = 0; i < id.length; i++) {
			productIds.add(Integer.parseInt(id[i]));
		}
		try {
			productService.removes(productIds);
		} catch (Exception e) {
			return AjaxResponse.fail("删除失败");
		}
		return AjaxResponse.success("删除成功");
	}
	
	/**
	 * 全部删除
	 */
	@ResponseBody
	@RequestMapping("/deleteAll")
	public AjaxResponse deleteAll(HttpServletRequest request){
		SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		productService.removeAll(sStoreUser.getStoreCode());
		return AjaxResponse.success("删除成功");
	}
	
	/**
	 * 确认上架
	 */
	@ResponseBody
	@RequestMapping("/isEnable")
	public AjaxResponse isEnable(Product product) {
		int isEnable = product.getIsEnable();
		product = productService.selectProductById(product.getId());
		product.setIsEnable(isEnable);
		int r = productService.updateOne(product);
		if (r > 0) {
			return AjaxResponse.success("操作成功");
		}
		return AjaxResponse.fail("操作失败");
	}

	/**
	 * 添加购买凭证集
	 * 
	 * @param content
	 * @param imgUrl
	 * @param productId
	 * @param buyerId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updatePurchaseVoucher")
	public AjaxResponse updatePurchaseVoucher(PurchaseVoucher purchaseVoucher, int productId, int dataTypeId) {
		BasedataType basedataType = basedataTypeService.selectByKey(dataTypeId);
		purchaseVoucher.setBasedataType(basedataType);
		Product product = new Product();
		product.setId(productId);
		product = productService.selectProductById(productId);
		product.getPurchaseVouchers().add(purchaseVoucher);
		int i = productService.updateOne(product);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 添加第三方认证集
	 * 
	 * @param content
	 * @param imgUrl
	 * @param productId
	 * @param buyerId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAuthorityCertification")
	public AjaxResponse updateAuthorityCertification(int dataTypeId, String imgUrl, int productId,
			String certificationContent) {
		AuthorityCertification authorityCertification = new AuthorityCertification();
		BasedataType basedataType = basedataTypeService.selectByKey(dataTypeId);
		authorityCertification.setBasedataType(basedataType);
		authorityCertification.setImgUrl(imgUrl);
		authorityCertification.setContent(certificationContent);
		Product product = new Product();
		product = productService.selectProductById(productId);
		product.getAuthorityCertifications().add(authorityCertification);
		int i = productService.updateOne(product);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 规格价格集保存
	 */
	@ResponseBody
	@RequestMapping("/updateproductPrices")
	public AjaxResponse updateproductPrices(ProductPrice productPrice, int productId) {
		Product product = new Product();
		product = productService.selectProductById(productId);
		product.getProductPrices().add(productPrice);
		int i = productService.updateOne(product);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 文献集保存
	 */
	@ResponseBody
	@RequestMapping("/updateLiterature")
	public AjaxResponse updateLiterature(int dataTypeId,Literature literature, int productId) {
		Product product = new Product();
		BasedataType basedataType = basedataTypeService.selectByKey(dataTypeId);
		literature.setBasedataType(basedataType);

		product = productService.selectProductById(productId);
		product.getLiteratures().add(literature);
		int i = productService.updateOne(product);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 试验集保存
	 */
	@ResponseBody
	@RequestMapping("/updateExperimentReport")
	public AjaxResponse updateExperimentReport(ExperimentReport experimentReport, int productId) {
		Product product = new Product();

		product = productService.selectProductById(productId);
		product.getExperimentReports().add(experimentReport);
		int i = productService.updateOne(product);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 删除商品规格价格
	 */
	@ResponseBody
	@RequestMapping("/deletePrice")
	public AjaxResponse deletePrice(String ids,int productId) {
		String priceIds[] = ids.split(",");
		Product product = new Product();
		product = productService.selectProductById(productId);
		for(String id : priceIds){
			for (ProductPrice price : product.getProductPrices()) {
				if(id.equals(price.getId()+"")){ 
					product.getProductPrices().remove(price);
					break;
				}
			}
		}
		int r  = productService.updateOne(product);
		if(r > 0){
			return AjaxResponse.success("删除成功");
		}
		return AjaxResponse.fail("删除失败");
	}
	
	/**
	 * 删除购买凭证集
	 */
	@ResponseBody
	@RequestMapping("/deleteVoucher")
	public AjaxResponse deleteVoucher(String ids,int productId) {
		String priceIds[] = ids.split(",");
		Product product = new Product();
		product = productService.selectProductById(productId);
		for(String id : priceIds){
			for (PurchaseVoucher purchaseVoucher : product.getPurchaseVouchers()) {
				if(id.equals(purchaseVoucher.getId()+"")){ 
					product.getPurchaseVouchers().remove(purchaseVoucher);
					break;
				}
			}
		}
		int r  = productService.updateOne(product);
		if(r > 0){
			return AjaxResponse.success("删除成功");
		}
		return AjaxResponse.fail("删除失败");
	}
	
	/**
	 * 删除第三方认证集
	 */
	@ResponseBody
	@RequestMapping("/deleteCertification")
	public AjaxResponse deleteCertification(String ids,int productId) {
		String priceIds[] = ids.split(",");
		Product product = new Product();
		product = productService.selectProductById(productId);
		for(String id : priceIds){
			for (AuthorityCertification certification : product.getAuthorityCertifications()) {
				if(id.equals(certification.getId()+"")){ 
					product.getAuthorityCertifications().remove(certification);
					break;
				}
			}
		}
		int r  = productService.updateOne(product);
		if(r > 0){
			return AjaxResponse.success("删除成功");
		}
		return AjaxResponse.fail("删除失败");
	}
	
	/**
	 * 删除文献引用集
	 */
	@ResponseBody
	@RequestMapping("/deleteLiterature")
	public AjaxResponse deleteLiterature(String ids,int productId) {
		String priceIds[] = ids.split(",");
		Product product = new Product();
		product = productService.selectProductById(productId);
		for(String id : priceIds){
			for (Literature literature : product.getLiteratures()) {
				if(id.equals(literature.getId()+"")){ 
					product.getLiteratures().remove(literature);
					break;
				}
			}
		}
		int r  = productService.updateOne(product);
		if(r > 0){
			return AjaxResponse.success("删除成功");
		}
		return AjaxResponse.fail("删除失败");
	}
	
	/**
	 * 删除实验报告集
	 */
	@ResponseBody
	@RequestMapping("/deleteExperimentReport")
	public AjaxResponse deleteExperimentReport(String ids,int productId) {
		String priceIds[] = ids.split(",");
		Product product = new Product();
		product = productService.selectProductById(productId);
		for(String id : priceIds){
			for (ExperimentReport experimentReport : product.getExperimentReports()) {
				if(id.equals(experimentReport.getId())){ 
					product.getExperimentReports().remove(experimentReport);
					break;
				}
			}
		}
		int r  = productService.updateOne(product);
		if(r > 0){
			return AjaxResponse.success("删除成功");
		}
		return AjaxResponse.fail("删除失败");
	}
	
}
