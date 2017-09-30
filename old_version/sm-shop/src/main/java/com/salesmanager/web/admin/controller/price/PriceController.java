package com.salesmanager.web.admin.controller.price;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.price.BrandDiscount;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.BrandDiscountService;
import com.salesmanager.core.business.customer.service.UserSegmentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.utils.ExceptionUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

import edu.emory.mathcs.backport.java.util.Collections;

@Controller
public class PriceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceController.class);

	@Autowired
	private ManufacturerService manufactureService;
	
	@Autowired
	private MerchantStoreService merchantStoreService;

	@Autowired
	private BrandDiscountService brandDiscountService;

	@Autowired
	LabelUtils messages;

	private void setMenu(Model model, HttpServletRequest request, String menuName) throws Exception {

		// display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("price", "price");
		activeMenus.put(menuName, menuName);

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = (Menu) menus.get("price");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = "/admin/price/brands.html", method = RequestMethod.GET)
	public String brandPrice(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request, "price-brands");

		MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);

		// admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// if (auth != null && request.isUserInRole("ADMIN")) {
		// if (product == null) {
		// return "redirect:/admin/products/products.html";
		// }
		// } else {
		// if (product == null || product.getMerchantStore().getId().intValue()
		// != store.getId().intValue()) {
		// return "redirect:/admin/products/products.html";
		// }
		// }

		return ControllerConstants.Tiles.PriceManager.priceBrands;
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = "/admin/price/others.html", method = RequestMethod.GET)
	public String otherPrices(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request, "price-others");
		return ControllerConstants.Tiles.PriceManager.priceOthers;
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/getDiscountByBrandStoreName.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String getDiscountByBrandStoreName(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		Map<String, Object> resp = new HashMap<String, Object>();
		String brandName = (String) args.get("brandName");
		String storeName = (String) args.get("storeName");

		List<Manufacturer> list = manufactureService.listByBrandAndStoreName(brandName, storeName);
		List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>(list.size());
		for (Manufacturer mnf : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			BrandDiscount existedDiscount = brandDiscountService.getByBrandAndStoreId(mnf.getId(),
					mnf.getMerchantStore().getId());
			if (existedDiscount != null) {
				data.put("rate", existedDiscount.getDiscounts().get(Constants.USER_SEGEMNT_LOGGED_CUSTOMER));
				data.put("existed", true);
			} else {
				data.put("rate", 0.0);
				data.put("existed", false);
			}
			String bName = mnf.getDescription().getName();
			String sName = mnf.getMerchantStore().getStorename();
			data.put("brandId", mnf.getId());
			data.put("brandName", bName);
			data.put("storeId", mnf.getMerchantStore().getId());
			data.put("storeName", sName);
			data.put("sortKey", bName + " " + sName);
			resultData.add(data);
		}
		Collections.sort(resultData, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return ((String) o1.get("sortKey")).compareTo((String) o2.get("sortKey"));
			}
		});
		resp.put("status", AjaxResponse.RESPONSE_STATUS_SUCCESS);
		ObjectMapper mapper = new ObjectMapper();
		try {
			resp.put("listStr", resultData);
			return mapper.writeValueAsString(resp);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			AjaxResponse errResp = new AjaxResponse();
			errResp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			errResp.setErrorMessage(e);
			return errResp.toJSONString();
		}

	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/saveBrandDiscount.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String saveBrandDiscount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		String brandId = (String) args.get("brandId");
		String storeId = (String) args.get("storeId");
		Double discountRate = ((Number) args.get("discountRate")).doubleValue();

		try {
			long bId = Long.parseLong(brandId);
			long sId = Long.parseLong(storeId);

			BrandDiscount discount = brandDiscountService.getByBrandAndStoreId(bId, sId);
			if (discount == null) {
				LOGGER.info("Create BrandDiscount(brandId={0}, storeId={1}, discount={2}",
						new Object[] { brandId, storeId, discountRate });
				discount = new BrandDiscount();
				discount.setBrandId(bId);
				discount.setStoreId(sId);
				Map<String, Double> discounts = new HashMap<String, Double>();
				discounts.put(Constants.USER_SEGEMNT_LOGGED_CUSTOMER, discountRate);
				discount.setDiscounts(discounts);
				brandDiscountService.create(discount);
			} else {
				Map<String, Double> discounts = discount.getDiscounts();
				discounts.put(Constants.USER_SEGEMNT_LOGGED_CUSTOMER, discountRate);
				brandDiscountService.update(discount);
			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
		} catch (Throwable t) {
			t.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(t);
		}
		return resp.toJSONString();

	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/deleteBrandDiscount.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String deleteBrandDiscount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		String brandId = (String) args.get("brandId");
		String storeId = (String) args.get("storeId");

		try {
			long bId = Long.parseLong(brandId);
			long sId = Long.parseLong(storeId);

			BrandDiscount discount = brandDiscountService.getByBrandAndStoreId(bId, sId);
			if (discount != null) {
				LOGGER.info("Delete brand-discount " + discount.getId());
				brandDiscountService.delete(discount);
			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
		} catch (Throwable t) {
			t.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(t);
		}
		return resp.toJSONString();

	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/paginationBrandDiscount.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String paginationBrandDiscount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		
		brandDiscountService.cleanUpInvalidBrands();
		
		Map<String, Object> resp = new HashMap<String, Object>();
		String brandName = (String) args.get("brandName");
		String storeName = (String) args.get("storeName");
		int pageSize = (int) args.get("pageSize");
		int pageNo = (int) args.get("pageNo");

		int totalSize = brandDiscountService.getListSizeByBrandAndStoreName(brandName, storeName);
		int maxPageNo = Math.max((totalSize + pageSize - 1)/pageSize, 1);
		if (pageNo == 0){
			pageNo = 1;
		}
		if (pageNo >= maxPageNo || pageNo <= 0){
			pageNo = maxPageNo;
		}
		int startPos = (pageNo-1)*pageSize;
		Map<String, Integer> pageData = new HashMap<String, Integer>();
		pageData.put("total", totalSize);
		pageData.put("pageNo", pageNo);
		pageData.put("fromNo", totalSize<=0?0:startPos+1);
		pageData.put("toNo", Math.min(startPos+pageSize, totalSize));
		resp.put("pageData", pageData);
		
		LOGGER.info("Query Brand discount record: " + pageData);
		List<Long>list = brandDiscountService.getPageIdListByBrandAndStoreName(brandName, storeName, startPos, pageSize);
		List<Map<String, Object>> discountData = new ArrayList<Map<String, Object>>(list.size());
		for (Long id : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			BrandDiscount existedDiscount = brandDiscountService.getById(id);
			data.put("rate", existedDiscount.getDiscounts().get(Constants.USER_SEGEMNT_LOGGED_CUSTOMER));
			data.put("id", existedDiscount.getId());
			String bName = "N/A";
			Manufacturer mnf = manufactureService.getById(existedDiscount.getBrandId());
			if (mnf != null){
				bName = mnf.getDescription().getName();
			}
			MerchantStore store = merchantStoreService.getById(existedDiscount.getStoreId());
			String sName = "N/A";
			if (store != null){
				sName = store.getStorename();
			}
			data.put("brandId", existedDiscount.getBrandId());
			data.put("brandName", bName);
			data.put("storeId", existedDiscount.getStoreId());
			data.put("storeName", sName);
			discountData.add(data);
		}
		resp.put("discountData", discountData);
		resp.put("status", AjaxResponse.RESPONSE_STATUS_SUCCESS);
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(resp);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			AjaxResponse errResp = new AjaxResponse();
			errResp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			errResp.setErrorMessage(e);
			return errResp.toJSONString();
		}

	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/updateDiscount.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String updateDiscount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		long bdId = ((Number) args.get("bdId")).longValue();
		Double rate = ((Number) args.get("rate")).doubleValue();

		try {
			//long bId = Long.parseLong(bdId);

			BrandDiscount discount = brandDiscountService.getById(bdId);
			if (discount != null) {
				Map<String, Double> discounts = discount.getDiscounts();
				Double oldRate = discounts.get(Constants.USER_SEGEMNT_LOGGED_CUSTOMER);
				LOGGER.info("update brand-discount " + discount.getId()+" rate from " + oldRate+" to " + rate);
				discounts.put(Constants.USER_SEGEMNT_LOGGED_CUSTOMER, rate);
				brandDiscountService.update(discount); 
			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
		} catch (Throwable t) {
			t.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(t);
		}
		return resp.toJSONString();

	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/deleteDiscountById.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String deleteDiscountById(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		long bdId = ((Number) args.get("bdId")).longValue();

		try {
			//long bId = Long.parseLong(bdId);

			BrandDiscount discount = brandDiscountService.getById(bdId);
			if (discount != null) {
				LOGGER.info("delete brand-discount " + discount.getId());
				brandDiscountService.delete(discount); 
			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
		} catch (Throwable t) {
			t.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(t);
		}
		return resp.toJSONString();

	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/deleteAllDiscount.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String deleteAllDiscount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();


		try {

			brandDiscountService.deleteAll();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
		} catch (Throwable t) {
			t.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(t);
		}
		return resp.toJSONString();

	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/price/deleteDiscountByIdList.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String deleteDiscountByIdList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		@SuppressWarnings("unchecked")
		List<Number>ids= (List<Number>) args.get("ids");

		try {
			//long bId = Long.parseLong(bdId);
			for (Number id : ids) {
				long bdId = id.longValue();
				BrandDiscount discount = brandDiscountService.getById(bdId);
				if (discount != null) {
					LOGGER.info("delete brand-discount " + discount.getId());
					brandDiscountService.delete(discount);
				}
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
		} catch (Throwable t) {
			t.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(t);
		}
		return resp.toJSONString();

	}
}
