package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.ProductsList;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.CategoryNode;
import com.salesmanager.web.utils.DeleteAll;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;
import com.salesmanager.web.utils.UserUtils;

@Controller
public class ProductsController {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CategoryNode categoryNode;
	
	@Autowired
	UserService userService;
	
	private int productShowPage=15;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/products.html", method=RequestMethod.GET)
	public String displayProducts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request,"catalogues");
		return "admin-products";
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/import.html", method=RequestMethod.GET)
	public String importProducts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request,"catalogues");
		return "admin-products";
		
	}
	
	/**
	 * @author 百图生物
	 * 商品列表分页
	 * */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody ProductsList displayProduct(HttpServletRequest request, HttpServletResponse response) {
		int page =1;
		String strPage = request.getParameter("page");
		if(strPage !=null && !strPage.equalsIgnoreCase("1")){
			page = Integer.parseInt(strPage);
		}
		ProductCriteria criteria = new ProductCriteria();
		PaginationData paginaionData=createPaginaionData(page,this.productShowPage);
		
		String categoryId = request.getParameter("categoryId");
		
		String avStatus = request.getParameter("avstatus");
		
		String findName = request.getParameter("findname");
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		ProductsList pList =null;
		try {
				MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);	
				if(!StringUtils.isBlank(categoryId) && !categoryId.equals("-1")) {
					
						//get other filters
						Long lcategoryId =Long.parseLong(categoryId);						
						if(lcategoryId>0) {
							Category category = categoryService.getById(lcategoryId);				
							//get all sub categories
							StringBuilder lineage = new StringBuilder();
							lineage.append(category.getLineage()).append(category.getId()).append("/");
							List<Category> categories = categoryService.listByLineage(store, lineage.toString());
							List<Long> categoryIds = new ArrayList<Long>();
							
							for(Category cat : categories) {
								categoryIds.add(cat.getId());
							}
							categoryIds.add(category.getId());
							criteria.setCategoryIds(categoryIds);
						}
					}
				
				if(!StringUtils.isBlank(avStatus)&&!avStatus.equalsIgnoreCase("null")){
					criteria.setAvailableP(Boolean.valueOf(avStatus));
				}
				
				if(!StringUtils.isBlank(findName)){
					criteria.setFindName(findName.trim());
				}
					//admin has all products
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					
					if(auth != null &&
			        		 request.isUserInRole("ADMIN")){
						criteria.setStoreId(-1l);
					}else{
						criteria.setStoreId(store.getId());
					}
					
					Language language = (Language)request.getAttribute("LANGUAGE");
					ProductList productList = productService.listPsByCriteria(language, criteria);
					List<Object[]> plist = productList.getObjs();
					pList = new ProductsList();
					Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
					if(plist!=null) {
						List <com.salesmanager.web.entity.catalog.product.Product> ps = new ArrayList<com.salesmanager.web.entity.catalog.product.Product>();
						for(Object[] product : plist) {
							try{
								com.salesmanager.web.entity.catalog.product.Product p = new com.salesmanager.web.entity.catalog.product.Product();
								
								/**ProductDescription description = product.getDescriptions().iterator().next();*/
								if(product[11] !=null && !StringUtils.isBlank(product[11].toString())){
	 								Category c = cmap.get(Long.parseLong(product[11].toString()));
	 								p.setType(categoryNode.getProductType(c));								
								}
								p.setAvailable(product[6]==null?null:new Boolean(product[6].toString()));
								p.setCode(product[4]==null?null:product[4].toString());
								p.setId(new Long(product[0].toString()));
								p.setProductName(product[1].toString());
								p.setProductEnName(product[2]==null?"":product[2].toString());
								p.setSku(product[3].toString());
								p.setStoreName(product[9].toString());							
								p.setAvailableDate(product[7]==null?null:product[7].toString().substring(0, 10));
								p.setAudit(product[5]==null?-1:Integer.parseInt(product[5].toString()));
								p.setQualitysocre(product[10]==null?-1:Integer.parseInt(product[10].toString()));							
								p.setCreateDate(product[8]==null?null:product[8].toString().substring(0,19));
								ps.add(p);
							}catch (Exception e){
								LOGGER.error("When set products ..... " + product[0]);
								continue;
							}
							
						}
						pList.setProducts(ps);
						pList.setTotalCount(ps.size());
						pList.setPaginationData(PageBuilderUtils.calculatePaginaionData(paginaionData,Constants.MAX_ORDERS_PAGE, productList.getTotalCount()));
						
						return pList;
					}
			}catch (Exception e) {
				LOGGER.error("When get products ..... " + e);
				
				return pList;
			}
		
		return pList;
		
	}
	
	/**
	 * 根据分类节点查询商品记录
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/all.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String displayProductAll(HttpServletRequest request, HttpServletResponse response) {
		//TODO what if ROOT
		
				String categoryId = request.getParameter("categoryId");
				String sku = request.getParameter("sku");
				String available = request.getParameter("available");
				String name = request.getParameter("name");
				
				AjaxPageableResponse resp = new AjaxPageableResponse();
				
				try {
					
				
					int startRow = Integer.parseInt(request.getParameter("_startRow"));
					int endRow = Integer.parseInt(request.getParameter("_endRow"));
					
					Language language = (Language)request.getAttribute("LANGUAGE");
					MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
					
					ProductCriteria criteria = new ProductCriteria();
					
					criteria.setStartIndex(startRow);
					criteria.setMaxCount(endRow);
					
					
					if(!StringUtils.isBlank(categoryId) && !categoryId.equals("-1")) {
						
						//get other filters
						Long lcategoryId = 0L;
						try {
							lcategoryId = Long.parseLong(categoryId);
						} catch (Exception e) {
							LOGGER.error("Product page cannot parse categoryId " + categoryId );
							resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
							String returnString = resp.toJSONString();
							return returnString;
						} 
						
						

						if(lcategoryId>0) {
						
							Category category = categoryService.getById(lcategoryId);
			
							if(category==null || category.getMerchantStore().getId()!=store.getId()) {
								resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
								String returnString = resp.toJSONString();
								return returnString;
							}
							
							//get all sub categories
							StringBuilder lineage = new StringBuilder();
							lineage.append(category.getLineage()).append(category.getId()).append("/");
							
							List<Category> categories = categoryService.listByLineage(store, lineage.toString());
							
							List<Long> categoryIds = new ArrayList<Long>();
							
							for(Category cat : categories) {
								categoryIds.add(cat.getId());
							}
							categoryIds.add(category.getId());
							criteria.setCategoryIds(categoryIds);
						
						}
						


						
					}
					
					if(!StringUtils.isBlank(sku)) {
						criteria.setCode(sku);
					}
					
					if(!StringUtils.isBlank(name)) {
						criteria.setProductName(name);
					}
					
					if(!StringUtils.isBlank(available)) {
						if(available.equals("true")) {
							criteria.setAvailableP(new Boolean(true));
						} else {
							criteria.setAvailableP(new Boolean(false));
						}
					}
					
					ProductList productList = productService.listByStore(store, language, criteria);
					resp.setEndRow(productList.getTotalCount());
					resp.setStartRow(startRow);
					List<Product> plist = productList.getProducts();
					
					if(plist!=null) {
					
						for(Product product : plist) {
							
							Map entry = new HashMap();
							entry.put("productId", product.getId());
							
							ProductDescription description = product.getDescriptions().iterator().next();
							
							entry.put("name", description.getName());
							entry.put("sku", product.getSku());
							entry.put("available", product.isAvailable());
							resp.addDataEntry(entry);
							
							
							
						}
					
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
	@RequestMapping(value="/admin/products/available.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String available(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("id");
		String active = request.getParameter("active");
		

		AjaxResponse resp = new AjaxResponse();
		if(!StringUtils.isBlank(values) && !StringUtils.isBlank(active)) {
			
			try {
				
				Long id = Long.parseLong(values);
				Product product = productService.getById(id);
				product.setAvailable(Boolean.parseBoolean(active));
				productService.saveOrUpdate(product);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			} catch (Exception e) {
				LOGGER.error("Error while set available ", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}else{
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/categorys.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody com.salesmanager.web.admin.entity.catalog.Category getCategorys(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			com.salesmanager.web.admin.entity.catalog.Category root = categoryNode.getRoot(request);
			root.setCategoryID(-1l);
			root.setCategoryName("root");
			return root;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error while paging category", e);
		}
		return null;
	}
	
	//单个商品删除
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProduct(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		if(request.getParameter("productId") !=null){
			try{
				Long id = Long.parseLong(request.getParameter("productId"));
				Product product = productService.getById(id);
				//get store information
				MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
				if(!product.getMerchantStore().getCode().equalsIgnoreCase(store.getCode())){
					User user = userService.getByUserName(request.getRemoteUser());					
					if(!UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
						return resp.toJSONString();
					}
				}
				productService.delete(product);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}catch (Exception e){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}		
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/removes.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProducts(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		DeleteAll delteAll = new DeleteAll();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		try{
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);	
			delteAll.setStore(store);
			User user = userService.getByUserName(request.getRemoteUser());
			delteAll.setUser(user);
			delteAll.setService(productService);
			delteAll.setResp(resp);
			if(request.getParameter("productId") !=null){
				String []sid = request.getParameter("productId").split(",");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				if(sid !=null && sid.length>0){
					delteAll.setSid(sid);
					resp = delteAll.call();
				}
			}else{
				Criteria criteria = new Criteria();
				//admin has all products
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				
				if(auth != null &&
		        		 request.isUserInRole("ADMIN")){
					criteria.setStoreId(-1l);
				}else{
					criteria.setStoreId(store.getId());
				}
				delteAll.setCriteria(criteria);
				delteAll.setRequest(request);
				resp = delteAll.call();
				
			}
		}catch (Exception e){
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/percent.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getPercent(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		//resp.setStatus(0);		
		HttpSession session = request.getSession();
		if(null != session.getAttribute("percentnum")){
			resp.setStatus(Integer.parseInt(session.getAttribute("percentnum").toString()));
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return resp.toJSONString();
	}
		
	private void setMenu(Model model, HttpServletRequest request, String activMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue-products", "catalogue-products");
		activeMenus.put(activMenu, activMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue-products");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	
	
}
