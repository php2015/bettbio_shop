package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.controller.products.facade.RelationshipFacade;
import com.salesmanager.web.admin.entity.products.ReadableRalationshipList;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.DeleteAll;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;


@Controller
public class CustomProductGroupsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomProductGroupsController.class);
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRelationshipService productRelationshipService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	RelationshipFacade relationshipFacade;
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/groups/list.html",  method={RequestMethod.GET,RequestMethod.POST})
	public String displayProductGroups(@Valid @ModelAttribute("criteria") Criteria criteria,@RequestParam(value = "page", defaultValue = "1") final int page,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PaginationData paginaionData=createPaginaionData(page,15);
		if(criteria == null) criteria = new Criteria();
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		
		ReadableRalationshipList olist = relationshipFacade.getByCriteria(criteria);
		
		model.addAttribute( "ships", olist);
		if(olist!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, olist.getTotalCount()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		
		model.addAttribute( "criteria", criteria);
		setMenu(model,request,"catalogue-products-custom-group");
		return ControllerConstants.Tiles.Product.customGroups;
		
	}
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/groups/removes.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String removeProductGroups(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		DeleteAll delteAll = new DeleteAll();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		try{
			
			delteAll.setService(productRelationshipService);
			delteAll.setResp(resp);
			if(request.getParameter("entitis") !=null){
				String []sid = request.getParameter("entitis").split(",");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				if(sid !=null && sid.length>0){
					delteAll.setSid(sid);
					resp = delteAll.call();
				}
			}else{
				Criteria criteria = new Criteria();
				//admin has all options
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);	
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
	@RequestMapping(value="/admin/products/groups/available.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String available(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("id");
		String active = request.getParameter("active");
		

		AjaxResponse resp = new AjaxResponse();
		if(!StringUtils.isBlank(values) && !StringUtils.isBlank(active)) {
			
			try {
				
				Long id = Long.parseLong(values);
				ProductRelationship relationship = productRelationshipService.getById(id);
				relationship.setActive(Boolean.parseBoolean(active));
				productRelationshipService.saveOrUpdate(relationship);
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
	@RequestMapping(value="/admin/products/groups/addgroup.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String addGroup(HttpServletRequest request, HttpServletResponse response,Locale locale) {
		
		String code = request.getParameter("code");
		AjaxResponse resp = new AjaxResponse();
		
		if(StringUtils.isBlank(code)) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			//resp.setErrorMessage(messages.getMessage("message.group.required",locale));
			String returnString = resp.toJSONString();
			return returnString;
		}
		
		try {
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			List<ProductRelationship> groups = productRelationshipService.getGroups(store);
			for(ProductRelationship grp : groups) {
				if(grp.getCode().equalsIgnoreCase(code)) {
					resp.setStatus(AjaxPageableResponse.CODE_ALREADY_EXIST);
					String returnString = resp.toJSONString();
					return returnString;
				}
			}
		

			ProductRelationship relationship = new ProductRelationship();
			relationship.setActive(true);
			relationship.setCode(code);
			relationship.setStore(store);
			
			productRelationshipService.saveOrUpdate(relationship);
			

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	/**
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/groups/save.html", method=RequestMethod.POST)
	public String saveCustomProductGroup(@Valid @ModelAttribute("criteria") Criteria criteria,@RequestParam(value = "page", defaultValue = "1") final int page,@ModelAttribute("group") ProductRelationship group, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model,request,"catalogue-products-custom-group");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		PaginationData paginaionData=createPaginaionData(page,15);
		if( criteria ==null) criteria = new Criteria();
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		
		ReadableRalationshipList olist = relationshipFacade.getByCriteria(criteria);
		
		model.addAttribute( "ships", olist);
		if(olist!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, olist.getTotalCount()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		//check if group already exist
		
		
		if(StringUtils.isBlank(group.getCode())) {
			FieldError fieldError = new FieldError("group","code",group.getCode(),false,null,null,messages.getMessage("message.group.required",locale));
			result.addError(fieldError);
			return ControllerConstants.Tiles.Product.customGroups;
		}
		
		//String msg = messages.getMessage("message.group.alerady.exists",locale);
		//String[] messages = {msg};
		
		String[] messages = {"message.group.alerady.exists"};
		
		List<ProductRelationship> groups = productRelationshipService.getGroups(store);
		for(ProductRelationship grp : groups) {
			if(grp.getCode().equalsIgnoreCase(group.getCode())) {
				String[] args = {group.getCode()};
				FieldError fieldError = new FieldError("group","code",group.getCode(),false,messages,args,null);
				result.addError(fieldError);
			}
		}
		
		if(result.hasErrors()) {
			return ControllerConstants.Tiles.Product.customGroups;
		}

		group.setActive(true);
		group.setStore(store);
		
			
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.customGroups;
		
	}
	*/
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/groups/remove.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String removeCustomProductGroup(HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("entity");

		AjaxResponse resp = new AjaxResponse();


		try {
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
//			productRelationshipService.deleteGroup(store, id);
			productRelationshipService.deleteByProperties(new String[]{"store.id", "id"}, new Object[]{store.getId(), Long.parseLong(id)});
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while deleting a group", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return returnString;

	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/groups/update.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String activateProductGroup(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("_oldValues");
		String active = request.getParameter("active");
		

		AjaxResponse resp = new AjaxResponse();

		try {
			
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("rawtypes")
			Map conf = mapper.readValue(values, Map.class);
			String groupCode = (String)conf.get("code");

			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			//get groups
			List<ProductRelationship> groups = productRelationshipService.getGroups(store);
			
			for(ProductRelationship relation : groups) {
				if(relation.getCode().equals(groupCode)) {
					if("true".equals(active)) {
						relation.setActive(true);
					} else {
						relation.setActive(false);
					}
					productRelationshipService.saveOrUpdate(relation);
				}
			}
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while updateing groups", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/group/edit.html", method=RequestMethod.GET)
	public String displayCustomProductGroup(@RequestParam("id") String groupCode, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request,"catalogue-products-custom-group");
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		List<Category> categories = categoryService.listByStore(store,language);//for categories
		
		
		model.addAttribute("group", groupCode);
		model.addAttribute("categories", categories);
		return ControllerConstants.Tiles.Product.customGroupsDetails;
		
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/group/details/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageProducts(HttpServletRequest request, HttpServletResponse response) {
		
		String code = request.getParameter("code");
		AjaxResponse resp = new AjaxResponse();
		
		try {
			

			
			Language language = (Language)request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			

			List<ProductRelationship> relationships = productRelationshipService.getByGroup(store, code, language);
			
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
	
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/group/details/addItem.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String addItem(HttpServletRequest request, HttpServletResponse response) {
		
		String code = request.getParameter("code");
		String productId = request.getParameter("productId");
		AjaxResponse resp = new AjaxResponse();
		
		try {
			

			Long lProductId = Long.parseLong(productId);

			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			Product product = productService.getById(lProductId);
			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}
			
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}


			ProductRelationship relationship = new ProductRelationship();
			relationship.setActive(true);
			relationship.setCode(code);
			relationship.setStore(store);
			relationship.setRelatedProduct(product);
			
			productRelationshipService.saveOrUpdate(relationship);
			

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
	@RequestMapping(value="/admin/products/group/details/removeItem.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeItem(HttpServletRequest request, HttpServletResponse response) {
		
		String code = request.getParameter("code");
		String productId = request.getParameter("productId");
		AjaxResponse resp = new AjaxResponse();
		
		try {
			

			Long lproductId = Long.parseLong(productId);

			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			Product product = productService.getById(lproductId);
			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}
			
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}
			
			
			ProductRelationship relationship = null;
			List<ProductRelationship> relationships = productRelationshipService.getByGroup(store, code);
			
			for(ProductRelationship r : relationships) {
				if(r.getRelatedProduct().getId().longValue()==lproductId.longValue()) {
					relationship = r;
					break;
				}
			}
			
			if(relationship==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}
			
			if(relationship.getStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}


			
			
			productRelationshipService.delete(relationship);
			

			resp.setStatus(AjaxPageableResponse.RESPONSE_OPERATION_COMPLETED);
		
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

}
