package com.bettbio.shop.web.controller.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.SUser;
import com.bettbio.core.mongo.model.AuthorityCertification;
import com.bettbio.core.mongo.model.Buyers;
import com.bettbio.core.mongo.model.ConsumableMaterialProduct;
import com.bettbio.core.mongo.model.ExperimentReport;
import com.bettbio.core.mongo.model.Literature;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.ProductBaseModel;
import com.bettbio.core.mongo.model.ProductPrice;
import com.bettbio.core.mongo.model.PurchaseVoucher;
import com.bettbio.core.mongo.model.ReagentProduct;
import com.bettbio.core.mongo.model.ServiceProduct;
import com.bettbio.core.mongo.service.ProductService;
import com.bettbio.core.service.ProductClassificationService;
import com.github.pagehelper.PageInfo;

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
	ProductService productService;

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

	/**
	 * 价格集
	 * 
	 * @param model
	 * @param reagentProduct
	 * @return
	 */
	@RequestMapping("/price")
	public String price(Model model, ReagentProduct reagentProduct) {
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		model.addAttribute("productPrices", reagentProduct.getProductPrices());
		model.addAttribute("reagentProduct", reagentProduct);
		return "/admin/product/price";
	}

	/**
	 * 凭证集
	 */
	@RequestMapping("/voucher")
	public String voucher(Model model, ReagentProduct reagentProduct) {
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		model.addAttribute("purchaseVouchers", reagentProduct.getPurchaseVouchers());
		model.addAttribute("reagentProduct", reagentProduct);
		return "/admin/product/voucher";
	}

	/**
	 * 第三方认证集
	 */
	@RequestMapping("/attestation")
	public String attestation(Model model, ReagentProduct reagentProduct) {
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		model.addAttribute("authorityCertifications", reagentProduct.getAuthorityCertifications());
		model.addAttribute("reagentProduct", reagentProduct);
		return "/admin/product/attestation";
	}

	/**
	 * 文献引用集
	 */
	@RequestMapping("/literature")
	public String literatures(Model model, ReagentProduct reagentProduct) {
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		model.addAttribute("literatures", reagentProduct.getLiteratures());
		model.addAttribute("reagentProduct", reagentProduct);
		return "/admin/product/literature";
	}

	/**
	 * 实验报告集
	 */
	@RequestMapping("/experimentReport")
	public String experimentReport(Model model, ReagentProduct reagentProduct) {
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		model.addAttribute("experimentReports", reagentProduct.getExperimentReports());
		model.addAttribute("reagentProduct", reagentProduct);
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
			product.setStoreCode(sStoreUser.getStoreCode());
			product.setId(CodeUtils.getProductId());
			product.setCode(CodeUtils.getCode());
			product.setCreateDate(new Date());
			String id = product.getProductClass().getCode();
			if(!StringUtils.isEmpty(id))
				product.setProductClass(productClassificationService.selectByKey(Integer.parseInt(id)));
			productService.insertProduct(product);
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
			product.setStoreCode(sStoreUser.getStoreCode());
			product.setId(CodeUtils.getProductId());
			product.setCode(CodeUtils.getCode());
			product.setCreateDate(new Date());
			String id = product.getProductClass().getCode();
			if(!StringUtils.isEmpty(id))
				product.setProductClass(productClassificationService.selectByKey(Integer.parseInt(id)));
			productService.insertProduct(product);
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
			product.setStoreCode(sStoreUser.getStoreCode());
			product.setId(CodeUtils.getProductId());
			product.setCode(CodeUtils.getCode());
			product.setCreateDate(new Date());
			String id = product.getProductClass().getCode();
			if(!StringUtils.isEmpty(id))
				product.setProductClass(productClassificationService.selectByKey(Integer.parseInt(id)));
			productService.insertProduct(product);
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
	public String productList(HttpServletRequest request, Model model, ProductBaseModel productBaseModel,
			String findName) {
		paramMap = getRequestParameter(request);
		Page<ProductBaseModel> pageInfo;
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
	public String info(Model model, ReagentProduct reagentProduct) {
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		model.addAttribute("reagentProduct", reagentProduct);
		return "/admin/product/info";
	}

	/**
	 * 耗材仪器类商品详情
	 */
	@RequestMapping("/consumableInfo")
	public String consumableInfo(Model model, ConsumableMaterialProduct consumableMaterialProduct) {
		consumableMaterialProduct = productService.selectOneByQuery(consumableMaterialProduct);
		model.addAttribute("consumableMaterialProduct", consumableMaterialProduct);
		return "/admin/product/consumableInfo";
	}

	/**
	 * 服务类商品详情
	 */
	@RequestMapping("/serviceInfo")
	public String serviceInfo(Model model, ServiceProduct serviceProduct) {
		serviceProduct = productService.selectOneByQuery(serviceProduct);
		model.addAttribute("serviceProduct", serviceProduct);
		return "/admin/product/serviceInfo";
	}

	/**
	 * 更新试剂商品
	 */
	@ResponseBody
	@RequestMapping("/updateReagentProduct")
	public AjaxResponse updateReagentProduct(ReagentProduct reagentProduct) {
		int i = productService.updateOne(reagentProduct, collectionName);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 更新耗材类商品
	 */
	@ResponseBody
	@RequestMapping("/updateConsumableMaterialProduct")
	public AjaxResponse updateConsumableMaterialProduct(ConsumableMaterialProduct consumableMaterialProduct) {
		int i = productService.updateOne(consumableMaterialProduct, collectionName);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 更新服务类商品
	 */
	@ResponseBody
	@RequestMapping("/updateServiceProduct")
	public AjaxResponse updateServiceProduct(ServiceProduct serviceProduct) {
		int i = productService.updateOne(serviceProduct, collectionName);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
	}

	/**
	 * 单个删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public AjaxResponse delete(int id) {
		try {
			productService.remove(id, collectionName);
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
			productService.removes(productIds, collectionName);
		} catch (Exception e) {
			return AjaxResponse.fail("删除失败");
		}
		return AjaxResponse.success("删除成功");
	}

	/**
	 * 确认上架
	 */
	@ResponseBody
	@RequestMapping("/isEnable")
	public AjaxResponse isEnable(ReagentProduct reagentProduct) {
		int i = productService.updateOne(reagentProduct, collectionName);
		if (i == 1) {
			return AjaxResponse.success("更新成功");
		} else {
			return AjaxResponse.fail("更新失败");
		}
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
	public AjaxResponse updatePurchaseVoucher(PurchaseVoucher purchaseVoucher, int productId, int buyerId) {
		// String content,String imgUrl,int productId,int buyerId,Date
		// buyingTime){
		// PurchaseVoucher purchaseVoucher = new PurchaseVoucher();
		Buyers buyers = new Buyers();
		purchaseVoucher.setBuyers(buyers);
		purchaseVoucher.getBuyers().setId(buyerId);

		ReagentProduct reagentProduct = new ReagentProduct();
		reagentProduct.setId(productId);
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		reagentProduct.getPurchaseVouchers().add(purchaseVoucher);

		int i = productService.updateOne(reagentProduct, collectionName);
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
	public AjaxResponse updateAuthorityCertification(String certificationName, String imgUrl, int productId,
			String certificationContent) {
		AuthorityCertification authorityCertification = new AuthorityCertification();
		Buyers buyers = new Buyers();
		buyers.setName(certificationName);
		authorityCertification.setBuyers(buyers);
		authorityCertification.setImgUrl(imgUrl);
		authorityCertification.setContent(certificationContent);

		ReagentProduct reagentProduct = new ReagentProduct();
		reagentProduct.setId(productId);
		reagentProduct = productService.selectOneByQuery(reagentProduct);
		reagentProduct.getAuthorityCertifications().add(authorityCertification);

		int i = productService.updateOne(reagentProduct, collectionName);
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
		ReagentProduct reagentProduct = new ReagentProduct();
		reagentProduct.setId(productId);
		reagentProduct = productService.selectOneByQuery(reagentProduct);

		reagentProduct.getProductPrices().add(productPrice);

		int i = productService.updateOne(reagentProduct, collectionName);
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
	public AjaxResponse updateLiterature(Literature literature, int productId) {
		ServiceProduct serviceProduct = new ServiceProduct();
		serviceProduct.setId(productId);

		serviceProduct = productService.selectOneByQuery(serviceProduct);
		serviceProduct.getLiteratures().add(literature);

		int i = productService.updateOne(serviceProduct, collectionName);
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
		ServiceProduct serviceProduct = new ServiceProduct();
		serviceProduct.setId(productId);

		serviceProduct = productService.selectOneByQuery(serviceProduct);
		serviceProduct.getExperimentReports().add(experimentReport);

		int i = productService.updateOne(serviceProduct, collectionName);
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
					product.getPurchaseVouchers().remove(certification);
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
					product.getPurchaseVouchers().remove(literature);
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
					product.getPurchaseVouchers().remove(experimentReport);
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
