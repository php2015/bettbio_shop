package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.products.UploadProductsResult;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PinyinUtils;
import com.salesmanager.web.utils.TransExcelView;

@Controller
public class ProductUploadController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductUploadController.class);
	@Autowired
	private TransExcelView transExcelView;
	@Autowired
	private ProductService productService;
	@Autowired
	private SearchService searchService;
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/toUpload.html", method=RequestMethod.GET)
	public String toUpload(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);	
		/**
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<MerchantStore> list = new ArrayList<MerchantStore>();
		//判断用户是否是系统管理员admin，如果是，则可以对所有的店铺商品信息上传进行操作，否则只允许上传本店铺商品
		if(auth != null && request.isUserInRole("ADMIN")){
			list = merchantStoreService.list();
		} else{
			list.add(store);
		}*/
	
		model.addAttribute("store", store);
		return ControllerConstants.Tiles.Product.productUpload;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/products/storeslist.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = merchantStoreService.getStoreName();
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
	@RequestMapping(value="/admin/products/upload.html", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String upload(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		String storeId = request.getParameter("storeId");
		String modelVersion=request.getParameter("modalVersion");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		MerchantStore load_store = null;
		if (uploadfile == null || uploadfile.isEmpty()) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("upload.error.02", locale));
			return resp.toJSONString();
		} 
		
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//判断用户是否是系统管理员admin，如果是，则可以对所有的店铺商品信息上传进行操作，否则只允许上传本店铺商品
		if(auth != null && request.isUserInRole("ADMIN")){
			load_store = merchantStoreService.getById(Long.valueOf(storeId));
		} else if(store.getId().equals(Long.valueOf(storeId))){ //选择的商铺是否同当前用户所属商铺是否一致
			load_store = store;
		} else {
			//非平台系统管理员不允许操作非本商铺的商品批量上传
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("upload.error.01", locale));
			return resp.toJSONString();
		}
		
		
		//判断上传文件格式是否是xls,或者是xlsx
		String originalFilename = uploadfile.getOriginalFilename();
		if(!(originalFilename.endsWith("xls")||originalFilename.endsWith("xlsx"))){
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("upload.error.02", locale));
			return resp.toJSONString();
		}
		
		/*int index = StringUtils.lastIndexOf(originalFilename, ".");
		String bakname = originalFilename.substring(0, index) + "_" + DateUtil.formatDate(dt, "HHmmss") + originalFilename.substring(index);
//		String bakname = originalFilename;
		//备份
		String realPath = request.getSession().getServletContext().getRealPath("/upload/"+store.getCode()+"/"+DateUtil.formatDate(dt, "yyyyMMdd")+"/");
		File file = new File(realPath, bakname);
		LOGGER.info("user=" + user.getAdminName() + " upload products ... fileName=" + originalFilename);
		FileUtils.copyInputStreamToFile(uploadfile.getInputStream(), file);*/
		//上传文件处理
		
		UploadProductsResult result = null;
		//判断批量导入的模板，0为试剂、耗材、仪器类商品，1为服务类商品
		String uploadtype = request.getParameter("uploadtype");
		if (StringUtils.isBlank(uploadtype)) {
			uploadtype = "0";
			result.getMessage();
		}
		transExcelView.setModelVersion(modelVersion);
		if (uploadtype.equals("1")) {//导入服务类商品
			result = transExcelView.importOthersProducts(uploadfile.getInputStream(), load_store, user,request.getSession().getServletContext());
		}else if(uploadtype.equals("2")) {
			result = transExcelView.importServiceProducts(uploadfile.getInputStream(), load_store, user,request.getSession().getServletContext());
		}else {
			result = transExcelView.importProducts(uploadfile.getInputStream(), load_store, user,request.getSession().getServletContext());
		}
		if (result.getStatus().equals(UploadProductsResult.StatusEnum.SUCCESS.getCode())) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			Map map = resp.getDataMap();
			map.put("successlines", result.getSuccesslines());
		} else if (result.getStatus().equals(UploadProductsResult.StatusEnum.PARTIALSUCCESS.getCode())) {
			resp.setStatus(1); //部分成功
			Map map = resp.getDataMap();
			map.put("successlines", result.getSuccesslines());
			map.put("errorlines", result.getErrorlines());
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(result.getErrorlines());
		}
		String json = resp.toJSONString();
		LOGGER.info(json);
		return json;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue-products", "catalogue-products");
		activeMenus.put("product-import", "product-import");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu)menus.get("catalogue-products");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
	}	

}
