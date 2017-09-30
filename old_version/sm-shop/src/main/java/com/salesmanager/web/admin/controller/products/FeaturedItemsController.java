package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.ProductsList;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.PageBuilderUtils;


@Controller
public class FeaturedItemsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeaturedItemsController.class);
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRelationshipService productRelationshipService;
	
	private int productShowPage=15;
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/featured/list.html", method=RequestMethod.GET)
	public String displayFeaturedItems(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request,"catalogue-featured");
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		List<Category> categories = categoryService.listByStore(store,language);
		
		model.addAttribute("categories", categories);
		return "admin-catalogue-featured";
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/featured/categorys.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody com.salesmanager.web.admin.entity.catalog.Category getCategorys(HttpServletRequest request, HttpServletResponse response) {
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		com.salesmanager.web.admin.entity.catalog.Category root = new com.salesmanager.web.admin.entity.catalog.Category();
		root.setCategoryID(-1);
		root.setCategoryName("root");
		
		try {
			Map<String, Category> objects = categoryService.listTwoDepthByLanguage(store,language);
			if(objects !=null && objects.size()>0){
				List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
				nodes.add(this.getNode(objects.get("01")));
				nodes.add(this.getNode(objects.get("02")));
				nodes.add(this.getNode(objects.get("03")));
				nodes.add(this.getNode(objects.get("04")));
				root.setCategorys(nodes);
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error while paging category", e);
		}
		
		return root;
	}
	
	private com.salesmanager.web.admin.entity.catalog.Category getNode(Category cat){
		com.salesmanager.web.admin.entity.catalog.Category node = new com.salesmanager.web.admin.entity.catalog.Category();
		node.setCategoryID(cat.getId());
		node.setCategoryName(cat.getDescinfo());
		node.setCategorys(this.getNodes(cat));
		return node;
	}
	private List<com.salesmanager.web.admin.entity.catalog.Category> getNodes(Category cat){		
		List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
		if(cat.getCategories() !=null && cat.getCategories().size()>0){
			for(Category c :cat.getCategories()){				
				com.salesmanager.web.admin.entity.catalog.Category n = new com.salesmanager.web.admin.entity.catalog.Category();
				n.setCategoryID(c.getId());
				n.setCategoryName(c.getDescinfo());
				if(c.getCategories() != null && c.getCategories().size()>0){
					n.setCategorys(getNodes(c));
				}
				nodes.add(n);
			}
		}
		return nodes;
	}
	/**
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/featured/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageProducts(HttpServletRequest request, HttpServletResponse response) {
		
		
		AjaxResponse resp = new AjaxResponse();
		
		try {
			

			
			Language language = (Language)request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			

			List<ProductRelationship> relationships = productRelationshipService.getByType(store, ProductRelationshipType.FEATURED_ITEM, language);
			
			for(ProductRelationship relationship : relationships) {
				
				Product product = relationship.getRelatedProduct();
				Map entry = new HashMap();
				entry.put("relationshipId", relationship.getId());
				entry.put("productId", product.getId());
				
				ProductDescription description = product.getDescriptions().iterator().next();
				Set<ProductDescription> descriptions = product.getDescriptions();
				for(ProductDescription desc : descriptions) {
					if(desc.getLanguage().getId().intValue()==language.getId().intValue()) {
						description = desc;
					}
				}
				
				entry.put("name", description.getName());
				entry.put("sku", product.getSku());
				entry.put("available", product.isAvailable());
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
	*/
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/featured/products.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
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
		String featureCode = request.getParameter("featurecode");
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		
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
				
				if(!avStatus.equalsIgnoreCase("null")){
					criteria.setAvailableP(Boolean.valueOf(avStatus));
				}
				
				if(!StringUtils.isBlank(featureCode) && !featureCode.equalsIgnoreCase("all")){
					criteria.setRelationship(featureCode);
					
				}
				if(!StringUtils.isBlank(findName)){
					criteria.setFindName(findName);
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
					//criteria.setRelationship(true);
					ProductList productList = productService.listPsByCriteria(language, criteria);
					List<Product> plist = productList.getProducts();
					ProductsList pList = new ProductsList();
					if(plist!=null) {
						List <com.salesmanager.web.entity.catalog.product.Product> ps = new ArrayList<com.salesmanager.web.entity.catalog.product.Product>();
						for(Product product : plist) {
							
							com.salesmanager.web.entity.catalog.product.Product p = new com.salesmanager.web.entity.catalog.product.Product();
							
							ProductDescription description = product.getDescriptions().iterator().next();
							
							p.setAvailable(product.isAvailable());
							p.setCode(product.getCode());
							p.setId(product.getId());
							p.setProductName(description.getName());
							p.setSku(product.getSku());
							p.setStoreName(product.getMerchantStore().getStorename());
							p.setId(product.getId());
							Set<ProductRelationship> relationships = product.getRelationships();
							if(relationships != null && relationships.size()>0){
								StringBuffer res = new StringBuffer();
								for(ProductRelationship re:relationships){
									res.append(",").append(re.getCode());
								}
								p.setRelations(res.substring(1).toString());
							}
							
							ps.add(p);
						}
						pList.setProducts(ps);
						pList.setTotalCount(ps.size());
						pList.setPaginationData(PageBuilderUtils.calculatePaginaionData(paginaionData,Constants.MAX_ORDERS_PAGE, productList.getTotalCount()));
						
						return pList;
					}
			}catch (Exception e) {
				LOGGER.error("When get products ..... " + e);
				
				return null;
			}
			
		
		
		//model.addAttribute("criteria",criteria);
		//this.setMenu(model, request, "customer-list");
	
		//return "admin-customers";
		return null;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/featured/available.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
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
	@RequestMapping(value="/admin/catalogue/featured/featuredlist.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String getitems(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
	
		try {
			
			MerchantStore store ;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(auth != null &&
	        		 request.isUserInRole("ADMIN")){
				store=null;
			}else{
				store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			}
			StringBuffer returnStr = new StringBuffer();
			List<ProductRelationship> relationships = productRelationshipService.getGroups(store);
			if(relationships !=null && relationships.size()>0){
				HashMap<String, String> reMap = new HashMap<String, String>();
				for(ProductRelationship re :relationships){
					returnStr.append(",").append(re.getCode());
				}
				reMap.put("flist", returnStr.substring(1).toString());
				resp.setDataMap(reMap);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				return resp.toJSONString();
			}else{
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}

		
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			return resp.toJSONString();
		}
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/featured/addToGroup.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String addItem(HttpServletRequest request, HttpServletResponse response) {
		
		String productId = request.getParameter("productId");
		String groupcode = request.getParameter("groupId");
		AjaxResponse resp = new AjaxResponse();
		
		try {
			

			String []pid = productId.split(",");
			ArrayList<String> erros= new ArrayList<String>();
			for (int i=0; i<pid.length;i++){
				try{
					Long lProductId = Long.parseLong(pid[i]);
		
					MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
					
					Product product = productService.getById(lProductId);
					
					if(product==null) {
						erros.add(pid[i]);
					} else {
						List<ProductRelationship> tmp = productRelationshipService.findByPropertiesHQL(new String[]{"code", "store.id", "product.id"}, 
								new Object[]{groupcode, store.getId(), product.getId()}, null);
						//判断是否已经建立关系，如果已经建立关系，则不再需要重新建立
						if (tmp==null ||tmp.size()==0) {
							ProductRelationship relationship = new ProductRelationship();
							relationship.setActive(true);
							relationship.setCode(groupcode);
							relationship.setStore(store);
							relationship.setProduct(product);
							productRelationshipService.saveOrUpdate(relationship);
						}
					}
					/**
					if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
						resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
						return resp.toJSONString();
					}
					 */
		
					
				}catch (Exception e){
					LOGGER.error("Error while set product to group", e);
					erros.add(pid[i]);
					continue;
				}
			
			}
			if(erros.size() == pid.length){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}else if(erros.size()>0){
				HashMap<String, String> erronames = new HashMap<String, String>();
				StringBuffer errorNams = new StringBuffer();
				errorNams.append(erros.get(0));
				for(int k=1;k<erros.size();k++){
					errorNams.append(",").append(erros.get(k));
				}
				erronames.put("erronames", erronames.toString());
				resp.setDataMap(erronames);
				resp.setStatus(1);
			}else{
				resp.setStatus(AjaxPageableResponse.RESPONSE_OPERATION_COMPLETED);
			}
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/featured/removeItem.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String removeItem(HttpServletRequest request, HttpServletResponse response) {
		
		String productId = request.getParameter("productId");
		String groupcode = request.getParameter("groupId");
		AjaxResponse resp = new AjaxResponse();
		
		try {
			
			String []pid = productId.split(",");
			ArrayList<String> erros= new ArrayList<String>();
			
				for (int i=0; i<pid.length;i++){
					try{
						Long lproductId = Long.parseLong(pid[i]);
						
						Product product = productService.getById(lproductId);
						
						if(product==null) {
							erros.add(pid[i]);
						}else{
							List<ProductRelationship> relationships = productRelationshipService.getByCodeProduct(groupcode,lproductId);
							for(ProductRelationship r :relationships){
								if(r==null) {
									erros.add(pid[i]);
								}else{
									productRelationshipService.delete(r);
								}
							}
							
						}
						/**
						if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
							resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
							return resp.toJSONString();
						}
						
						*/
						
						/**
						if(relationship.getStore().getId().intValue()!=store.getId().intValue()) {
							resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
							return resp.toJSONString();
						}
			*/
						
						
					
					}catch (Exception e){
						LOGGER.error("Error while set product to group", e);
						erros.add(pid[i]);
						continue;
					}
				}
				if(erros.size() == pid.length){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				}else if(erros.size()>0){
					HashMap<String, String> erronames = new HashMap<String, String>();
					StringBuffer errorNams = new StringBuffer();
					errorNams.append(erros.get(0));
					for(int k=1;k<erros.size();k++){
						errorNams.append(",").append(erros.get(k));
					}
					erronames.put("erronames", erronames.toString());
					resp.setDataMap(erronames);
					resp.setStatus(1);
				}else{
					resp.setStatus(AjaxPageableResponse.RESPONSE_OPERATION_COMPLETED);
				}
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	
	private void setMenu(Model model, HttpServletRequest request, String activeMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put(activeMenu, activeMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
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
