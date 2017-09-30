package com.salesmanager.web.admin.batchdataoperation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.service.FullIndexCallback;
import com.salesmanager.core.business.catalog.product.service.FullIndexHelper;
import com.salesmanager.core.business.catalog.product.service.FullIndexParams;
import com.salesmanager.core.business.catalog.product.service.FullIndexService;
import com.salesmanager.core.business.catalog.product.service.ProductBatchDeletor;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.utils.ExceptionUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.constants.Constants;

@Controller
public class BatchDataOperationController {
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (this.excutorsvc != null){
			excutorsvc.shutdownNow();
		}
	}
	
	class BIPJob implements Runnable {
		List<Long> productIdList;
		int startFrom;
		int endBefore;

		@Override
		public void run() {
			doJobBatchInvalidProducts(productIdList, startFrom, endBefore);
		}
	}
	class BDPJob implements Runnable {
		List<Long> productIdList;
		int startFrom;
		int endBefore;

		@Override
		public void run() {
			doJobBatchDeleteProducts(productIdList, startFrom, endBefore);
		}
	}
	class BVPJob implements Runnable {
		List<Long> productIdList;
		int startFrom;
		int endBefore;

		@Override
		public void run() {
			doJobBatchValidProducts(productIdList, startFrom, endBefore);
		}
	}
	class BSDJob implements Runnable {
		List<Long> productIdList;
		int startFrom;
		int endBefore;
		boolean isDiamond;

		@Override
		public void run() {
			doJobBatchSetDiamond(productIdList, startFrom, endBefore, isDiamond);
		}

	}
	

	
	private static final String CONST_EXCEPT = "except";
	private static final String CONST_WHEN = "when";
	private static final String CONST_OPERATION_DELETE = "delete_product";
	private static final String CONST_OPERATION_INVALID = "invalid_product";
	private static final String CONST_OPERATION_VALID = "valid_product";
	private static final String CONST_OPERATION_SET_DIAMOND = "set_diamond";
	private static final String CONST_OPERATION_UNSET_DIAMOND = "unset_diamond";
	private static final String CONST_OPERATION_DELETE_PRODUCT = "delete_product";
	private static final String CONST_OPERATION_REINDEX_PRODUCT = "reindex_product";
	private static final String CONST_FULLINDEX_INDEXNAME = "product_zh_default";
	private static final String CONST_FULLINDEX_TYPENAME = "product_zh";
	

	protected AtomicLong totalNum = new AtomicLong(0);
	protected AtomicLong processedNum = new AtomicLong(0);
	protected AtomicLong runningJob = new AtomicLong(0);
	protected AtomicBoolean runningFlag = new AtomicBoolean(false);
	protected ExecutorService excutorsvc = null;
	protected static FullIndexCallback fullIndexCallback = new FullIndexCallbackImpl();

	@Autowired
	SearchService searchService;
	
	@Autowired
	UserService userService;

	@Autowired
	ManufacturerService manufacturerService;

	@Autowired
	MerchantStoreService merchantStoreService;

	@Autowired
	ProductService productService;

	@Autowired
	OrderService orderService;
	
	@Autowired
	DataSource datasource;
	
	@Autowired
	FullIndexService fullIndexService;
	
	@Autowired
	ProductBatchDeletor productBatchDeletor;

	@RequestMapping(value = { "/admin/batch/dataoperation.html" }, method = RequestMethod.GET)
	public String displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("batchDataOperation", "batchDataOperation");

		model.addAttribute("activeMenus", activeMenus);

		// get store information
		MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);

		// Map<String,Country> countries =
		// countryService.getCountriesMap(language);

		// Country storeCountry = store.getCountry();
		// Country country = countries.get(storeCountry.getIsoCode());

		String sCurrentUser = request.getRemoteUser();
		User currentUser = userService.getByUserName(sCurrentUser);

		model.addAttribute("store", store);
		// model.addAttribute("country", country);
		model.addAttribute("user", currentUser);

		return ControllerConstants.Tiles.batchDataOperation;
	}

	public void doJobBatchDeleteProducts(List<Long> productIdList, int startFrom, int endBefore) {
		for (int i = startFrom; i < endBefore; i++) {
			try {
				Long id = productIdList.get(i);
				Product product = productService.getById(id);
				productService.delete(product);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			processedNum.incrementAndGet();
		}
		if (runningJob.decrementAndGet() == 0) {
			runningFlag.set(false);
		}
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/queryExxcuteStatus.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String queryExxcuteStatus(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		if (runningFlag.get() == false) {
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
		}

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("total", totalNum.toString());
		dataMap.put("processed", processedNum.toString());
		resp.setDataMap(dataMap);
		return resp.toJSONString();
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/bipexecution.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String executeBIP(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();
		synchronized (runningFlag) {
			if (runningFlag.get() == true) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("已有其他批量处理任务在执行，请稍后再试");
				return resp.toJSONString();
			}
			String brandOperation = (String) args.get("brandOperation");
			String merchantOperation = (String) args.get("merchantOperation");
			List<String> brandNames = (List<String>) args.get("brandNames");
			List<String> merchantNames = (List<String>) args.get("merchantNames");
			int opType = calcOperationType(brandOperation, merchantOperation);
			switch (opType) {
			case 1:
				// "下架除指定商家外的，所有其他商家的指定品牌的产品。结果是只有指定商家才有该品牌的产品";
				List<Long> productIds = getBipIdListOfBrandsExceptSomeMerchant(brandNames, merchantNames);
				if (productIds == null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					break;
				}
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				startBatchInvalidOperation(productIds);
				break;
			case 2:
				// "下架指定商家的，指定品牌的产品。结果是指定的商家将不再有该品牌的产品";
				productIds = getBipIdListOfBrandsInsideSomeMerchant(brandNames, merchantNames);
				if (productIds == null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					break;
				}
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				startBatchInvalidOperation(productIds);
				break;
			case 3:
				// "下架除指定商家以外的，除指定品牌以外的产品。结果是其他商家都只有这个品牌的产品了。通常很少使用。";
				productIds = getBipIdListNotBrandsNorSomeMerchant(brandNames, merchantNames);
				if (productIds == null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					break;
				}
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				startBatchInvalidOperation(productIds);
				break;
			case 4:
				// "下架指定商家的，除指定品牌外的产品。即该商家将只有指定品牌的产品，其他产品全部下架";
				productIds = getBipIdListNotBrandsInsideSomeMerchant(brandNames, merchantNames);
				if (productIds == null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					break;
				}
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				startBatchInvalidOperation(productIds);
				break;
			default:
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				break;
			}
		}
		return resp.toJSONString();
	}

	protected static final int BATCH_SIZE = 1000;
	protected static final double BATCH_PIECES = 4.0;

	private void startBatchInvalidOperation(List<Long> productIds) {
		runningFlag.set(true);
		if (productIds == null || productIds.size() == 0) {
			runningFlag.set(false);
		}
		ensureExecutor();
		int len = productIds.size();
		totalNum.set(len);
		processedNum.set(0);
		runningJob.set(0);
		int pieceSize = (int) Math.round(len / BATCH_PIECES +0.5);
		int pos = 0;
		while (pos < productIds.size()) {
			BIPJob job = new BIPJob();
			job.productIdList = productIds;
			job.startFrom = pos;
			pos += pieceSize;
			job.endBefore = Math.min(pos, len);
			runningJob.getAndIncrement();
			excutorsvc.execute(job);
		}
	}

	public void doJobBatchInvalidProducts(List<Long> productIdList, int startFrom, int endBefore) {
		for (int i = startFrom; i < endBefore; i++) {
			try {
				Long id = productIdList.get(i);
				Product product = productService.getById(id);
//				if (!product.isAvailable()) {
					product.setAvailable(false);
					productService.update(product);
					searchService.deleteIndex(product.getMerchantStore(), product);
//				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			processedNum.incrementAndGet();
		}
		if (runningJob.decrementAndGet() == 0) {
			runningFlag.set(false);
		}
	}

	public void doJobBatchValidProducts(List<Long> productIdList, int startFrom, int endBefore) {
		for (int i = startFrom; i < endBefore; i++) {
			try {
				Long id = productIdList.get(i);
				Product product = productService.getById(id);
//				if (!product.isAvailable()) {
					product.setAvailable(true);
					productService.update(product);
					searchService.index(product.getMerchantStore(), product);
//				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			processedNum.incrementAndGet();
		}
		if (runningJob.decrementAndGet() == 0) {
			runningFlag.set(false);
		}
	}
	private List<Long> getBipIdListOfBrandsExceptSomeMerchant(List<String> brandNames, List<String> merchantNames) {
		if (brandNames == null || brandNames.isEmpty()) {
			return null;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			return null;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		// get all product IDs which match the brand name
		List<Long> productIds = productService.getAllByManufactureIds(menufactures);

		// and then get all products by merchant, one by one
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					return null;
				}
				List<Long> mchtProductIds = productService.getAllByManufactureAndMerchant(mcht, menufactures);
				productIds.removeAll(mchtProductIds);
			} catch (ServiceException e) {
				e.printStackTrace();
				return null;
			}
		}
		return productIds;
	}

	private List<Long> getBipIdListOfBrandsInsideSomeMerchant(List<String> brandNames, List<String> merchantNames) {
		if (brandNames == null || brandNames.isEmpty()) {
			return null;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			return null;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		// get all product IDs which match the brand name
		List<Long> productIds = new ArrayList<Long>();

		// and then get all products by merchant, one by one
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					return null;
				}
				List<Long> mchtProductIds = productService.getAllByManufactureAndMerchant(mcht, menufactures);
				productIds.addAll(mchtProductIds);
			} catch (ServiceException e) {
				e.printStackTrace();
				return null;
			}
		}
		return productIds;
	}

	private List<Long> getBipIdListNotBrandsNorSomeMerchant(List<String> brandNames, List<String> merchantNames) {
		if (brandNames == null || brandNames.isEmpty()) {
			return null;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			return null;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		// and then get all products by merchant, one by one
		Set<MerchantStore> merchants = new HashSet<MerchantStore>();
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					return null;
				}
				merchants.add(mcht);
			} catch (ServiceException e) {
				e.printStackTrace();
				return null;
			}
		}
		List<Long> mchtProductIds = productService.getAllByNotManufactureNorMerchant(merchants, menufactures);
		return mchtProductIds;
	}

	private List<Long> getBipIdListNotBrandsInsideSomeMerchant(List<String> brandNames, List<String> merchantNames) {
		if (brandNames == null || brandNames.isEmpty()) {
			return null;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			return null;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		List<Long> productIds = new ArrayList<Long>();

		// and then get all products by merchant, one by one
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					return null;
				}
				// 该商户所有的产品
				List<Object> mchtProdIds = productService.getAllProductIDByStoreID(mcht.getId());
				// 该商户符合品牌要求的产品
				List<Long> mchtBrandProdIds = productService.getAllByManufactureAndMerchant(mcht, menufactures);
				productIds.addAll(calcIdsNotBrands(mchtProdIds, mchtBrandProdIds));
			} catch (ServiceException e) {
				e.printStackTrace();
				return null;
			}
		}
		return productIds;
	}

	private Collection<Long> calcIdsNotBrands(List<Object> mchtProdIds, List<Long> mchtBrandProdIds) {
		Set<Long> result = new HashSet<Long>();
		for (Object bigint : mchtProdIds) {
			Number id = (Number) bigint;
			result.add(id.longValue());
		}
		result.removeAll(mchtBrandProdIds);
		return result;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/showSummary.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String showSummary(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		String brandOperation = (String) args.get("brandOperation");
		String merchantOperation = (String) args.get("merchantOperation");
		List<String> brandNames = (List<String>) args.get("brandNames");
		List<String> merchantNames = (List<String>) args.get("merchantNames");
		int opType = calcOperationType(brandOperation, merchantOperation);
		switch (opType) {
		case 1:
			// "下架除指定商家外的，所有其他商家的指定品牌的产品。结果是只有指定商家才有该品牌的产品";
			Map<String, String> summaryData = calcBIPSummaryOfBrandsExceptSomeMerchant(brandNames, merchantNames);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			resp.setDataMap(summaryData);
			break;
		case 2:
			// "下架指定商家的，指定品牌的产品。结果是指定的商家将不再有该品牌的产品";
			summaryData = calcBIPSummaryOfBrandsInsideSomeMerchant(brandNames, merchantNames);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			resp.setDataMap(summaryData);
			break;
		case 3:
			// "下架除指定商家以外的，除指定品牌以外的产品。结果是其他商家都只有这个品牌的产品了。通常很少使用。";
			summaryData = calcBIPSummaryOfNotBrandsNorSomeMerchant(brandNames, merchantNames);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			resp.setDataMap(summaryData);
			break;
		case 4:
			// "下架指定商家的，除指定品牌外的产品。即该商家将只有指定品牌的产品，其他产品全部下架";
			summaryData = calcBIPSummaryOfNotBrandsInsideSomeMerchant(brandNames, merchantNames);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			resp.setDataMap(summaryData);
			break;
		default:
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			break;
		}
		return resp.toJSONString();
	}

	/**
	 * 1. count all products in brands
	 * 
	 * @param brandNames
	 * @param merchantNames
	 * @return
	 */
	private Map<String, String> calcBIPSummaryOfBrandsExceptSomeMerchant(List<String> brandNames,
			List<String> merchantNames) {
		Map<String, String> dataMap = new HashMap<String, String>();
		if (brandNames == null || brandNames.isEmpty()) {
			dataMap.put("ERROR", "品牌名称为空");
			return dataMap;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			dataMap.put("ERROR", "保留品牌的商家为空");
			return dataMap;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		// get all product IDs which match the brand name
		List<Long> productIds = productService.getAllByManufactureIds(menufactures);
		dataMap.put("totalProducts", String.valueOf(productIds.size()));

		// and then get all products by merchant, one by one
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					dataMap.put("ERROR", "没有找到商家" + name);
					return dataMap;
				}
				List<Long> mchtProductIds = productService.getAllByManufactureAndMerchant(mcht, menufactures);
				dataMap.put(name, String.valueOf(mchtProductIds.size()));
				productIds.removeAll(mchtProductIds);
			} catch (ServiceException e) {
				e.printStackTrace();
				dataMap.put("ERROR", "找不到商家" + name);
				return dataMap;
			}
		}
		dataMap.put("affectedProducts", String.valueOf(productIds.size()));
		return dataMap;
	}

	private Map<String, String> calcBIPSummaryOfBrandsInsideSomeMerchant(List<String> brandNames,
			List<String> merchantNames) {
		Map<String, String> dataMap = new HashMap<String, String>();
		if (brandNames == null || brandNames.isEmpty()) {
			dataMap.put("ERROR", "品牌名称为空");
			return dataMap;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			dataMap.put("ERROR", "保留品牌的商家为空");
			return dataMap;
		}
		// get all manufacturers who will be touched
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		// get all product IDs which match the brand name
		List<Long> productIds = productService.getAllByManufactureIds(menufactures);
		dataMap.put("totalProducts", String.valueOf(productIds.size()));

		// and then get all products by merchant, one by one
		int totalRemove = 0;
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					dataMap.put("ERROR", "没有找到商家" + name);
					return dataMap;
				}
				List<Long> mchtProductIds = productService.getAllByManufactureAndMerchant(mcht, menufactures);
				dataMap.put(name, String.valueOf(mchtProductIds.size()));
				totalRemove += mchtProductIds.size();
			} catch (ServiceException e) {
				e.printStackTrace();
				dataMap.put("ERROR", "找不到商家" + name);
				return dataMap;
			}
		}
		dataMap.put("affectedProducts", String.valueOf(totalRemove));
		return dataMap;
	}

	protected Set<Manufacturer> getManufacturesByName(List<String> brandNames) {
		Set<Manufacturer> menufactures = new HashSet<Manufacturer>();
		Iterator<String> it = brandNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			List<Manufacturer> manufactures = manufacturerService.getListByBrandName(name);
			menufactures.addAll(manufactures);
		}
		return menufactures;
	}

	private Map<String, String> calcBIPSummaryOfNotBrandsNorSomeMerchant(List<String> brandNames,
			List<String> merchantNames) {
		Map<String, String> dataMap = new HashMap<String, String>();
		if (brandNames == null || brandNames.isEmpty()) {
			dataMap.put("ERROR", "品牌名称为空");
			return dataMap;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			dataMap.put("ERROR", "保留品牌的商家为空");
			return dataMap;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		// get all product IDs which match the brand name
		List<Long> productIds = productService.getAllByManufactureIds(menufactures);
		dataMap.put("totalProducts", String.valueOf(productIds.size()));

		// and then get all products by merchant, one by one
		Set<MerchantStore> merchants = new HashSet<MerchantStore>();
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					dataMap.put("ERROR", "没有找到商家" + name);
					return dataMap;
				}
				merchants.add(mcht);
				List<Long> mchtProductIds = productService.getAllByManufactureAndMerchant(mcht, menufactures);
				dataMap.put(name, String.valueOf(mchtProductIds.size()));
			} catch (ServiceException e) {
				e.printStackTrace();
				dataMap.put("ERROR", "找不到商家" + name);
				return dataMap;
			}
		}
		List<Long> mchtProductIds = productService.getAllByNotManufactureNorMerchant(merchants, menufactures);
		dataMap.put("affectedProducts", String.valueOf(mchtProductIds.size()));
		return dataMap;
	}

	private Map<String, String> calcBIPSummaryOfNotBrandsInsideSomeMerchant(List<String> brandNames,
			List<String> merchantNames) {
		Map<String, String> dataMap = new HashMap<String, String>();
		if (brandNames == null || brandNames.isEmpty()) {
			dataMap.put("ERROR", "品牌名称为空");
			return dataMap;
		}
		if (merchantNames == null || merchantNames.isEmpty()) {
			dataMap.put("ERROR", "保留品牌的商家为空");
			return dataMap;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturesByName(brandNames);
		int totalProducts = 0;
		int removeProducts = 0;

		// and then get all products by merchant, one by one
		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					dataMap.put("ERROR", "没有找到商家" + name);
					return dataMap;
				}
				// 该商户所有的产品
				List<Object> mchtProdIds = productService.getAllProductIDByStoreID(mcht.getId());
				// 该商户符合品牌要求的产品
				List<Long> mchtBrandProdIds = productService.getAllByManufactureAndMerchant(mcht, menufactures);
				totalProducts += mchtProdIds.size();
				removeProducts += (mchtProdIds.size() - mchtBrandProdIds.size());
				dataMap.put(name, String.valueOf(mchtBrandProdIds.size()));
			} catch (ServiceException e) {
				e.printStackTrace();
				dataMap.put("ERROR", "找不到商家" + name);
				return dataMap;
			}
		}
		dataMap.put("affectedProducts", String.valueOf(removeProducts));
		dataMap.put("totalProducts", String.valueOf(totalProducts));
		return dataMap;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/verifyBIPArgs.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String verifyBIpArgs(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		String brandOperation = (String) args.get("brandOperation");
		String merchantOperation = (String) args.get("merchantOperation");
		List<String> brandNames = (List<String>) args.get("brandNames");
		List<String> merchantNames = (List<String>) args.get("merchantNames");
		int opType = calcOperationType(brandOperation, merchantOperation);
		switch (opType) {
		case 1:
		case 2:
		case 3:
		case 4:
			// 校验产品数是否不为0，品牌是否都存在
			if (!verifyBatchInvalidProductBrandAndMerchant(brandNames, merchantNames)) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
				resp.setErrorString("部分品牌或者商家不存在");
				Map<String, String> dataMap = new HashMap<String, String>();
				if (brandNames.size() > 0) {
					dataMap.put("invalid_brands", brandNames.toString());
				}
				if (merchantNames.size() > 0) {
					dataMap.put("invalid_merchants", merchantNames.toString());
				}
				resp.setDataMap(dataMap);
			} else {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
			break;
		default:
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			break;
		}
		String result = resp.toJSONString();
		return result;
	}

	private int calcOperationType(String brandOperation, String merchantOperation) {
		int opType = 0;
		if (CONST_EXCEPT.equalsIgnoreCase(merchantOperation)) {
			if (CONST_WHEN.equalsIgnoreCase(brandOperation)) {
				opType = 1;// "下架除指定商家外的，所有其他商家的指定品牌的产品。结果是只有指定商家才有该品牌的产品";
			} else {
				opType = 3;// "下架除指定商家以外的，除指定品牌以外的产品。结果是其他商家都只有这个品牌的产品了。通常很少使用。";
			}
		} else {
			if (CONST_WHEN.equalsIgnoreCase(brandOperation)) {
				opType = 2;// "下架指定商家的，指定品牌的产品。结果是指定的商家将不再有该品牌的产品";
			} else {
				opType = 4;// "下架指定商家的，除指定品牌外的产品。即该商家将只有指定品牌的产品，其他产品全部下架";
			}
		}
		return opType;
	}

	private boolean verifyBatchInvalidProductBrandAndMerchant(List<String> brandNames, List<String> merchantNames) {
		if (brandNames == null || brandNames.isEmpty()) {
			return false;
		}
		Iterator<String> it;
		Set<Manufacturer> menufactures = getManufacturersByExistedNames(brandNames);
		if (menufactures.isEmpty()) {
			return false;
		}
		List<Long> productIds = productService.getAllByManufactureIds(menufactures);

		it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			MerchantStore merchant;
			try {
				merchant = merchantStoreService.getByName(name);
			} catch (ServiceException e) {
				e.printStackTrace();
				merchant = null;
			}
			if (merchant == null) {
				continue;
			} else {
				it.remove();
			}
		}
		return brandNames.isEmpty() && merchantNames.isEmpty() && productIds != null && productIds.size() > 0;
	}

	protected Set<Manufacturer> getManufacturersByExistedNames(List<String> brandNames) {
		Iterator<String> it = brandNames.iterator();
		Set<Manufacturer> menufactures = new HashSet<Manufacturer>();
		while (it.hasNext()) {
			String name = it.next();
			List<Manufacturer> manfs = manufacturerService.getListByBrandName(name);
			if (manfs == null || manfs.isEmpty()) {
				continue;
			} else {
				it.remove();
				menufactures.addAll(manfs);
			}
		}
		return menufactures;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/verifyBSDArgs.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String verifyBSDArgs(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		Boolean isDiamond = (Boolean) args.get("isDiamond");
		List<String> brandNames = (List<String>) args.get("brandNames");
		List<String> merchantNames = (List<String>) args.get("merchantNames");

		Map<String, String> dataMap = new HashMap<String, String>();
		List<Long> pids = new ArrayList<Long>();
		boolean paramsValid = prepareBatchSetDiamondOperation(merchantNames, brandNames, isDiamond, pids, dataMap);
		if (paramsValid) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		resp.setDataMap(dataMap);
		String result = resp.toJSONString();
		return result;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/executeBSDOperation.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String executeBSDOperation(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		Boolean isDiamond = (Boolean) args.get("isDiamond");
		List<String> brandNames = (List<String>) args.get("brandNames");
		List<String> merchantNames = (List<String>) args.get("merchantNames");

		Map<String, String> dataMap = new HashMap<String, String>();
		List<Long> productIds = new ArrayList<Long>();
		boolean paramsValid = prepareBatchSetDiamondOperation(merchantNames, brandNames, isDiamond, productIds,
				dataMap);
		resp.setDataMap(dataMap);
		if (!paramsValid) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			return resp.toJSONString();
		}

		synchronized (runningFlag) {
			if (runningFlag.get() == true) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("已有其他批量处理任务在执行，请稍后再试");
				return resp.toJSONString();
			}
			runningFlag.set(true);
			if (productIds == null || productIds.size() == 0) {
				runningFlag.set(false);
			}
			ensureExecutor();
			int len = productIds.size();
			totalNum.set(len);
			processedNum.set(0);
			runningJob.set(0);
			int pieceSize = (int)Math.round(len / BATCH_PIECES + 0.5);
			int pos = 0;
			while (pos < productIds.size()) {
				BSDJob job = new BSDJob();
				job.productIdList = productIds;
				job.startFrom = pos;
				pos += pieceSize;
				job.endBefore = Math.min(pos, len);
				job.isDiamond = isDiamond;
				runningJob.getAndIncrement();
				excutorsvc.execute(job); // doJobBatchSetDiamond
			}
		}
		resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
		String result = resp.toJSONString();
		return result;
	}

	protected void ensureExecutor() {
		if (excutorsvc == null) {
			excutorsvc = Executors.newFixedThreadPool((int)BATCH_PIECES);
		}
	}

	public void doJobBatchSetDiamond(List<Long> productIdList, int startFrom, int endBefore, boolean isDiamond) {
		for (int i = startFrom; i < endBefore; i++) {
			try {
				Long id = productIdList.get(i);
				Product product = productService.getById(id);
				if (product.isProductIsDiamond() != isDiamond) {
					product.setProductIsDiamond(isDiamond);
					productService.update(product);
					if (product.isAvailable()){
						searchService.index(product.getMerchantStore(), product);
					}else{
						searchService.deleteIndex(product.getMerchantStore(), product);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			processedNum.incrementAndGet();
		}
		if (runningJob.decrementAndGet() == 0) {
			runningFlag.set(false);
		}
	}

	boolean prepareBatchSetDiamondOperation(List<String> merchantNames, List<String> brandNames, boolean isDiamond,
			List<Long> resultProductIds, Map<String, String> ajaxResponseDataMap) {
		boolean allMerchants = isEmptyArray(merchantNames);
		boolean allBrands = isEmptyArray(brandNames);
		if (allMerchants && allBrands) {
			ajaxResponseDataMap.put("ERROR", "商家和品牌不能都是空");
			return false;
		}

		boolean hasError = false;
		Set<Manufacturer> manfs = null;
		if (!allBrands) {
			manfs = getManufacturersByExistedNames(brandNames);
			if (!brandNames.isEmpty()) {
				ajaxResponseDataMap.put("ERROR", "有些商家和品牌不存在");
				ajaxResponseDataMap.put("invalid_brands", brandNames.toString());
				hasError = true;
			}
		}
		Set<MerchantStore> merchants = null;
		if (!allMerchants) {
			merchants = getMerchantStoreByExistedNames(merchantNames);
			if (!merchantNames.isEmpty()) {
				ajaxResponseDataMap.put("ERROR", "有些商家和品牌不存在");
				ajaxResponseDataMap.put("invalid_merchants", merchantNames.toString());
				hasError = true;
			}
		}
		if (hasError){
			return false;
		}
		if (allMerchants) {
			List<Long> productIds = productService.getAllByManufactureIds(manfs);
			resultProductIds.addAll(productIds);
			ajaxResponseDataMap.put("store_未指定", "无");
		} else if (allBrands) {
			for (MerchantStore store : merchants) {
				List<Object> productIds = productService.getAllProductIDByStoreID(store.getId());
				for (Object pid : productIds) {
					Number id = (Number) pid;
					resultProductIds.add(id.longValue());
				}
				ajaxResponseDataMap.put("store_" + store.getStorename(), String.valueOf(productIds.size()));
			}
		} else {
			// both merchant and brands
			for (MerchantStore store : merchants) {
				List<Long> productIds = productService.getAllByManufactureAndMerchant(store, manfs);
				resultProductIds.addAll(productIds);
				ajaxResponseDataMap.put("store_" + store.getStorename(), String.valueOf(productIds.size()));
			}
		}
		ajaxResponseDataMap.put("totalProducts", String.valueOf(resultProductIds.size()));
		return true;
	}

	private boolean isEmptyArray(List<String> stringList) {
		return stringList == null || stringList.isEmpty() || (stringList.size()==1 && stringList.get(0).trim().isEmpty());
	}

	protected Set<MerchantStore> getMerchantStoreByExistedNames(List<String> merchantNames) {
		Set<MerchantStore> merchants = new HashSet<MerchantStore>();
		Iterator<String> it = merchantNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			try {
				MerchantStore mcht = merchantStoreService.getByName(name);
				if (mcht == null) {
					continue;
				}
				it.remove();
				merchants.add(mcht);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return merchants;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/verifySQLStatment.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String verifySQLStatment(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		String sqlStr = (String) args.get("sql_input");
		Map<String, String> dataMap = new HashMap<String, String>();
		List<Long> pids = new ArrayList<Long>();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		if (sqlStr == null || sqlStr.trim().isEmpty()){
			dataMap.put("ERROR", "SQL为空");
		}else{
			try {
				pids = productService.getIdsByNativeSql(sqlStr);
				dataMap.put("totalProducts", String.valueOf(pids.size()));
			} catch (TransactionSystemException e){
				String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
				dataMap.put("ERROR", "有异常");
				dataMap.put("stacktrace", stack.replace("\n", "\\n"));
			} catch (Throwable e) {
				e.printStackTrace();
				String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
				dataMap.put("ERROR", "有异常");
				dataMap.put("stacktrace", stack.replace("\n", "\\n"));
			}
		}
		resp.setDataMap(dataMap);
		String result = resp.toJSONString();//toJsonString(resp);
		return result;
	}
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/executeSBO.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String executeSQL(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();

		String operationType = (String) args.get("operation");
		String sqlStr = (String) args.get("sql_input");
		Map<String, String> dataMap = new HashMap<String, String>();
		List<Long> pids = new ArrayList<Long>();

		if (sqlStr == null || sqlStr.trim().isEmpty()) {
			dataMap.put("ERROR", "SQL为空");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setDataMap(dataMap);
			String result = resp.toJSONString();
			return result;
		}

		try {
			pids = productService.getIdsByNativeSql(sqlStr);
			synchronized (runningFlag) {
				if (runningFlag.get() == true) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("已有其他批量处理任务在执行，请稍后再试");
					return resp.toJSONString();
				}
				runningFlag.set(true);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				if (pids == null || pids.size() == 0) {
					runningFlag.set(false);
					return resp.toJSONString();
				}
				ensureExecutor();
				int len = pids.size();
				totalNum.set(len);
				processedNum.set(0);
				runningJob.set(0);
				int pieceSize = (int) Math.round(len / BATCH_PIECES + 0.5);
				int pos = 0;
				Runnable job;
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				while (pos < pids.size()) {
					job = createBatchJob(operationType, pids, len, pieceSize, pos);
					if (job == null) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						dataMap.put("ERROR", "尚未支持的操作");
						break;
					}
					pos += pieceSize;
					runningJob.getAndIncrement();
					excutorsvc.execute(job);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			dataMap.put("ERROR", "有异常");
			dataMap.put("stacktrace", stack);
		}

		resp.setDataMap(dataMap);
		String result = resp.toJSONString();
		return result;
	}

	private Runnable createBatchJob(String operationType, List<Long> pids, int len, int pieceSize, int pos) {
		if (CONST_OPERATION_SET_DIAMOND.equalsIgnoreCase(operationType)) {
			BSDJob job = new BSDJob();
			job.productIdList = pids;
			job.startFrom = pos;
			job.endBefore = Math.min(pos+pieceSize, len);
			job.isDiamond = true;
			return job;
		} else if (CONST_OPERATION_UNSET_DIAMOND.equalsIgnoreCase(operationType)) {
			BSDJob job = new BSDJob();
			job.productIdList = pids;
			job.startFrom = pos;
			job.endBefore = Math.min(pos+pieceSize, len);
			job.isDiamond = false;
			return job;
		} else if (CONST_OPERATION_VALID.equalsIgnoreCase(operationType)) {
			BVPJob job = new BVPJob();
			job.productIdList = pids;
			job.startFrom = pos;
			job.endBefore = Math.min(pos+pieceSize, len);
			return job;
		} else if (CONST_OPERATION_INVALID.equalsIgnoreCase(operationType)) {
			BIPJob job = new BIPJob();
			job.productIdList = pids;
			job.startFrom = pos;
			job.endBefore = Math.min(pos+pieceSize, len);
			return job;
		} else if (CONST_OPERATION_DELETE.equalsIgnoreCase(operationType)) {
			BDPJob job = new BDPJob();
			job.productIdList = pids;
			job.startFrom = pos;
			job.endBefore = Math.min(pos+pieceSize, len);
			return job;
		} else {
			return null;
		}
	}
	
	private String toJsonString(AjaxResponse resp){
		ObjectMapper  objectMapper = new ObjectMapper();

		try {
			return objectMapper.writeValueAsString(resp);
		} catch (IOException e) {
			return "{}";
		}
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/verifyFullIndex.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String verifyFullIndex(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		Map<String, String> dataMap = new HashMap<String, String>();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		Connection connection = null;
		try {
			connection = datasource.getConnection();
			long totalNumber = FullIndexHelper.getTotalNumber(connection);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			dataMap.put("totalProducts", String.valueOf(totalNumber));
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			dataMap.put("ERROR", "有异常");
			dataMap.put("stacktrace", stack.replace("\n", "\\n"));
		} catch (Throwable e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			dataMap.put("ERROR", "有异常");
			dataMap.put("stacktrace", stack.replace("\n", "\\n"));
		} finally {
			if (connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		resp.setDataMap(dataMap);
		String result = resp.toJSONString();// toJsonString(resp);
		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/executeFullIndex.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String executeFullIndex(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			synchronized (fullIndexCallback) {
				if (fullIndexCallback.isRunning() == true) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("已有其他全索引任务在执行，请稍后再试");
					return resp.toJSONString();
				}
				
				// use hard coded parameters. These parameters are got by performance testing. 
				// don't change them unless you know what you are doing
				FullIndexParams params = new FullIndexParams();
				params.setIndexName(CONST_FULLINDEX_INDEXNAME);
				params.setTypeName(CONST_FULLINDEX_TYPENAME);
				params.setBatchSize(500);
				params.setPieces(4);
				
				fullIndexCallback.reset();
				fullIndexService.setCallback(fullIndexCallback);
				fullIndexService.setParams(params);
				fullIndexService.setDataSource(datasource);

				boolean started = fullIndexService.startProcessing();
				if (started){
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				}else{
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("启动全索引失败. 查看日志获得错误详细信息");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
			fullIndexCallback.reset();
		}

		String result = resp.toJSONString();
		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/queryFullIndexStatus.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String queryFullIndexStatus(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		Map<String, String> dataMap = new HashMap<String, String>();
		FullIndexCallbackImpl cb = (FullIndexCallbackImpl) fullIndexCallback;
		dataMap.put("total", String.valueOf(cb.getTotal().get()));
		dataMap.put("indexed", String.valueOf(cb.getIndexed().get()));
		dataMap.put("failed", String.valueOf(cb.getFailed().get()));
		dataMap.put("ignored", String.valueOf(cb.getIgnored().get()));
		dataMap.put("passedTime", String.valueOf(System.currentTimeMillis() - cb.getStartTime()));
		dataMap.put("running", String.valueOf(cb.getRunning().get()));
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		resp.setDataMap(dataMap);
		String result = resp.toJSONString();
		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/verifyBVPArgs.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String verifyBVPArgs(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();
		List<String> brandNames = (List<String>) args.get("brandNames");
		List<String> merchantNames = (List<String>) args.get("merchantNames");
		Map<String, String> dataMap = new HashMap<String, String>();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		try {
			List<Long> productIds = prepareBatchValidProductIds(brandNames, merchantNames, dataMap);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			dataMap.put("totalProducts", String.valueOf(productIds.size()));
		} catch (TransactionSystemException e) {
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			dataMap.put("ERROR", "有异常");
			dataMap.put("stacktrace", stack.replace("\n", "\\n"));
		} catch (Throwable e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			dataMap.put("ERROR", "有异常");
			dataMap.put("stacktrace", stack.replace("\n", "\\n"));
		} finally {
		}
		resp.setDataMap(dataMap);
		String result = resp.toJSONString();
		return result;
	}

	private List<Long> prepareBatchValidProductIds(List<String> brandNames, List<String> merchantNames,
			Map<String, String> dataMap) throws Exception {
		List<Long> result = new ArrayList<Long>();
		boolean allManufacturer = isEmptyArray(brandNames);
		boolean allMerchant =isEmptyArray( merchantNames);
		Set<Manufacturer> menufactures = null;
		
		if (allManufacturer && allMerchant){
			dataMap.put("ERROR", "品牌和商家不能都是空");
			return result;
		}
		if (!allManufacturer){
			menufactures = getManufacturesByName(brandNames);
		}
		if (allMerchant){
			dataMap.put("store_未指定", "无");
			getAllInvalidProductIdsByManufacturers(menufactures, result);
		}else if (allManufacturer){
			getAllInvalidProductIdsByMerchantName(merchantNames, result, dataMap);
		}else{
			getAllInvalidProductIdsByMerchantAndManufacturer(menufactures, merchantNames, result, dataMap);
		}
		return result;
	}

	private void getAllInvalidProductIdsByMerchantAndManufacturer(Set<Manufacturer> menufactures,
			List<String> merchantNames, List<Long> result, Map<String, String> dataMap) throws ServiceException {
		List<Long> brandIds = new ArrayList<Long>();
		for(Manufacturer m : menufactures){
			brandIds.add(m.getId());
		}
		Long[] mIds = brandIds.toArray(new Long[brandIds.size()]);
		for(String storeName: merchantNames){
			List<Long> pids = productService.getAllInvalidProductIdsByMerchantAndManufacturer(storeName, brandIds);
			dataMap.put("store_"+storeName, String.valueOf(pids.size()));
			result.addAll(pids);
		}
	}

	private void getAllInvalidProductIdsByMerchantName(List<String> merchantNames, List<Long> result,
			Map<String, String> dataMap) throws ServiceException   {
		for(String storeName: merchantNames){
			List<Long> pids = productService.getAllInvalidProductIdsByMerchantName(storeName);
			dataMap.put("store_"+storeName, String.valueOf(pids.size()));
			result.addAll(pids);
		}
	}
		

	private void getAllInvalidProductIdsByManufacturers(Set<Manufacturer> menufactures, List<Long> result) throws ServiceException {
		List<Long> brandIds = new ArrayList<Long>();
		for (Manufacturer m : menufactures) {
			brandIds.add(m.getId());
		}
		Long[] mIds = brandIds.toArray(new Long[brandIds.size()]);
		List<Long> pids = productService.getAllInvalidProductIdsByManufacturers(brandIds);
		result.addAll(pids);
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/executeBVP.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String executeBVP(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> args) {
		AjaxResponse resp = new AjaxResponse();
		List<String> brandNames = (List<String>) args.get("brandNames");
		List<String> merchantNames = (List<String>) args.get("merchantNames");
		Map<String, String> dataMap = new HashMap<String, String>();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		try {
			synchronized (fullIndexCallback) {
				if (fullIndexCallback.isRunning() == true) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("已有其他建立索引的任务在执行，请稍后再试");
					return resp.toJSONString();
				}
				
				// use hard coded parameters. These parameters are got by performance testing. 
				// don't change them unless you know what you are doing
				FullIndexParams params = new FullIndexParams();
				params.setIndexName(CONST_FULLINDEX_INDEXNAME);
				params.setTypeName(CONST_FULLINDEX_TYPENAME);
				params.setBatchSize(500);
				params.setPieces(4);
				
				fullIndexCallback.reset();
				fullIndexService.setCallback(fullIndexCallback);
				fullIndexService.setParams(params);
				fullIndexService.setDataSource(datasource);
				
				List<Long> pIds = this.prepareBatchValidProductIds(brandNames, merchantNames, dataMap);

				boolean started = fullIndexService.startIndexingByIds(pIds, true);
				if (started){
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_RUNNING);
				}else{
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("操作索引失败. 查看日志获得错误详细信息");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(stack.replace("\n", "\\n"));
			fullIndexCallback.reset();
		}

		String result = resp.toJSONString();
		return result;
	}

	/** 批量删除商品 **/
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/queryAllUsedSqls.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> queryAllUsedSqls(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> args) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("length", 0);
		try {
			List<String> usedSqlNotes = productBatchDeletor.getUsedSqlNoteList();
			if (usedSqlNotes != null && !usedSqlNotes.isEmpty()){
				result.put("length", usedSqlNotes.size());
				result.put("data", usedSqlNotes);
			}
			result.remove("exception");
		} catch (TransactionSystemException e){
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			result.put("exception", stack);
		} catch (Exception e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			result.put("exception", stack);
		}

		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/verifyBRPArgs.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> verifyBRPArgs(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("length", 0);
		String sqlNote = (String) args.get("sqlNote");
		String sqlStatement = (String) args.get("sqlStatement");
		try {
			long pidCnt = productBatchDeletor.getProductCountBySql(sqlStatement);
			result.put("length", pidCnt);
		} catch (TransactionSystemException e){
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			result.put("exception", stack);
		} catch (Exception e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			result.put("exception", stack);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/getBrpSqlByNote.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> getBrpSqlByNote(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		Map<String, Object> result = new HashMap<String, Object>();
		String sqlNote = (String) args.get("note");
		try {
			String sqlStr = productBatchDeletor.getSqlByNote(sqlNote);
			result.put("data", sqlStr);
		} catch (TransactionSystemException e){
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			result.put("exception", stack);
		} catch (Exception e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			result.put("exception", stack);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/executeBRP.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> executeBRP(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("length", 0);
		String sqlNote = (String) args.get("sqlNote");
		String sqlStatement = (String) args.get("sqlStatement");
		Long totalCount = ((Number) args.get("totalCount")).longValue();
		try {
			productBatchDeletor.doProductRemove(sqlNote, sqlStatement, totalCount);
			result.put("success", true);
			result.put("status", productBatchDeletor.getStatisticData());
		} catch (TransactionSystemException e){
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e.getApplicationException());
			result.put("exception", stack);
		} catch (Exception e) {
			e.printStackTrace();
			String stack = ExceptionUtils.dumpSalesmanagerExceptionStack(e);
			result.put("exception", stack);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/stopBRP.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> stopBRP(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		Map<String, Object> result = new HashMap<String, Object>();
		productBatchDeletor.stopProductDeleting();
		result.put("status", productBatchDeletor.getStatisticData());
		return result;
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = {
			"/admin/batch/queryBRPStatus.html" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> queryBRPStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> args) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", productBatchDeletor.getStatisticData());
		return result;
	}
}
