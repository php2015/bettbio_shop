package com.salesmanager.web.admin.controller.categories;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.category.ReadableCatagoryList;
import com.salesmanager.web.entity.catalog.category.facade.CategoryFacade;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.DeleteAll;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

@Controller
public class CategoryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	LabelUtils messages;

	@Autowired
	CategoryFacade categoryFacade;
	
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/editCategory.html", method=RequestMethod.GET)
	public String displayCategoryEdit(@RequestParam("id") long categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayCategory(categoryId,model,request,response);

	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/createCategory.html", method=RequestMethod.GET)
	public String displayCategoryCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayCategory(null,model,request,response);

	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/available.html", method=RequestMethod.POST, produces="application/json;chartset=UTF-8")
	public @ResponseBody String available(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("id");
		String active = request.getParameter("active");
		

		AjaxResponse resp = new AjaxResponse();
		if(!StringUtils.isBlank(values) && !StringUtils.isBlank(active)) {
			
			try {
				
				Long id = Long.parseLong(values);
				Category category = categoryService.getById(id);
				category.setVisible(Boolean.parseBoolean(active));
				categoryService.saveOrUpdate(category);
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
	
	private String displayCategory(Long categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//display menu
		if(categoryId == null){
			setMenu(model,request,"catalogue-categories-create");
		}else{
			setMenu(model,request,"catalogue-categories-list");
		}
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		

		List<Language> languages = store.getLanguages();
		Category category = new Category();
		
		if(categoryId!=null && categoryId!=0) {//edit mode
			category = categoryService.getById(categoryId);
		
			
			
			if(category==null || category.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/categories/list.html";
			}
		} else {
			
			category.setVisible(true);
			
		}
		
		List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
		
		for(Language l : languages) {
			
			CategoryDescription description = null;
			if(category!=null) {
				for(CategoryDescription desc : category.getDescriptions()) {
					
					
					if(desc.getLanguage().getCode().equals(l.getCode())) {
						description = desc;
					}
					
					
				}
			}
			
			if(description==null) {
				description = new CategoryDescription();
				description.setLanguage(l);
			}
			
			descriptions.add(description);

		}
		
		category.setDescriptions(descriptions);
	

		
		model.addAttribute("category", category);
		model.addAttribute("categories", categories);
		

		
		return "catalogue-categories-category";
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/save.html", method=RequestMethod.POST)
	public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model, HttpServletRequest request) throws Exception {

		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		setMenu(model,request,"catalogue-categories-list");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		if(category.getId() != null && category.getId() >0) { //edit entry
			
			//get from DB
			Category currentCategory = categoryService.getById(category.getId());
			
			if(currentCategory==null || currentCategory.getMerchantStore().getId()!=store.getId()) {
				return "catalogue-categories";
			}

		}

			
			Map<String,Language> langs = languageService.getLanguagesMap();
			


			List<CategoryDescription> descriptions = category.getDescriptions();
			if(descriptions!=null) {
				
				for(CategoryDescription description : descriptions) {
					
					String code = description.getLanguage().getCode();
					Language l = langs.get(code);
					description.setLanguage(l);
					description.setCategory(category);
					
					
				}
				
			}
			
			//save to DB
			category.setMerchantStore(store);
		//}
		
		if (result.hasErrors()) {
			return "catalogue-categories-category";
		}
		Map <Long,Category> catMap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		//check parent
		if(category.getParent()!=null) {
			if(category.getParent().getId()==-1) {//this is a root category
				category.setParent(null);
				category.setLineage("/");
				category.setDepth(0);
			}
		}
		
		category.getAuditSection().setModifiedBy(request.getRemoteUser());
		categoryService.saveOrUpdate(category);

		//加载到内存中
		try{
			Category memC = catMap.get(category.getId());
			if(memC !=null){
				catMap.remove(category.getId());
			}
			catMap.put(category.getId(), category);
		}catch (Exception e){
			catMap.put(category.getId(), category);
		}
		
			
		//ajust lineage and depth
		if(category.getParent()!=null && category.getParent().getId()!=-1) { 
		
			Category parent = new Category();
			parent.setId(category.getParent().getId());
			parent.setMerchantStore(store);
			
			categoryService.addChild(parent, category);
		//顶级节点加载到内存
		}else{
			List<Category> topMap = (List<Category>) request.getSession().getServletContext().getAttribute("topCateGory");
			
			try{
				int index =-1;
				for(int i=0;i<topMap.size();i++){
					if(topMap.get(i).getId()==category.getId()){
						break;
					}
				}
				if(index !=-1){
					topMap.remove(index);
				}
				
				topMap.add( category);
			}catch (Exception e){
				//topMap.put(category.getId(), category);
			}
		}
		
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		model.addAttribute("categories", categories);
		

		model.addAttribute("success","success");
		return "catalogue-categories-category";
	}
	
	
	//category list
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/list.html", method={RequestMethod.GET,RequestMethod.POST})
	public String displayCategories(@Valid @ModelAttribute("criteria") Criteria criteria,Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", defaultValue = "1") final int page) throws Exception {
		//分页
		PaginationData paginaionData=createPaginaionData(page,15);
		if(criteria == null) criteria = new Criteria();
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		ReadableCatagoryList lists = categoryFacade.getByCriteria(criteria, language);
		model.addAttribute( "cats", lists);
		if(lists!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, lists.getTotalCount()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		setMenu(model,request,"catalogue-categories-list");
		
		model.addAttribute( "criteria", criteria);
		
		//does nothing, ajax subsequent request
		
		return "catalogue-categories";
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/hierarchy.html", method=RequestMethod.GET)
	public String displayCategoryHierarchy(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		
		setMenu(model,request,"catalogue-categories-hierarchy");
		
		//get the list of categories
		//Language language = (Language)request.getAttribute("LANGUAGE");
		//MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//List<Category> categories = categoryService.listByStore(store, language);
		
		//model.addAttribute("categories", categories);
		
		return "catalogue-categories-hierarchy";
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/categorys.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
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
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/remove.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String deleteCategory(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("entity");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(sid);
			
			Category category = categoryService.getById(id);
			
			if(category==null || category.getMerchantStore().getId().intValue() !=store.getId().intValue() ) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				categoryService.delete(category);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/categories/removes.html", method=RequestMethod.POST, produces="application/json;chartset=UTF-8")
	public @ResponseBody String deleteProducts(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		DeleteAll delteAll = new DeleteAll();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		try{
			
			delteAll.setService(categoryService);
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
	@RequestMapping(value="/admin/categories/moveCategory.html", method=RequestMethod.POST, produces="application/json;chartset=UTF-8")
	public @ResponseBody String moveCategory(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String parentid = request.getParameter("pid");
		String childid = request.getParameter("cid");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long parentId = Long.parseLong(parentid);
			Long childId = Long.parseLong(childid);
			
			Category child = categoryService.getById(childId);
			Category parent = categoryService.getById(parentId);
			
			if(child.getParent() != null && child.getParent().getId()==parentId){
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				return resp.toJSONString();
			}

			if(parentId!=1) {
			
				if(child==null || parent==null || child.getMerchantStore().getId()!=store.getId() || parent.getMerchantStore().getId()!=store.getId()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					return resp.toJSONString();
				}
				
				if(child.getMerchantStore().getId()!=store.getId() || parent.getMerchantStore().getId()!=store.getId()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					return resp.toJSONString();
				}
			
			}
			

			parent.getAuditSection().setModifiedBy(request.getRemoteUser());
			categoryService.addChild(parent, child);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while moving category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/categories/checkCategoryCode.html", method=RequestMethod.POST, produces="application/json;chartset=UTF-8")
	public @ResponseBody String checkCategoryCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");


		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		AjaxResponse resp = new AjaxResponse();
		
		if(StringUtils.isBlank(code)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			return resp.toJSONString();
		}

		
		try {
			
		Category category = categoryService.getByCode(store, code);
		
		if(category!=null && StringUtils.isBlank(id)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			return resp.toJSONString();
		}
		
		
		if(category!=null && !StringUtils.isBlank(id)) {
			try {
				Long lid = Long.parseLong(id);
				
				if(category.getCode().equals(code) && category.getId().longValue()==lid) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					return resp.toJSONString();
				}
			} catch (Exception e) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}

		}
		
		
		
		

	
		
			


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
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
