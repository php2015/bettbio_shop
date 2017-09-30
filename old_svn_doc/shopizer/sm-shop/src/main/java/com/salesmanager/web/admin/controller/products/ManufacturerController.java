package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureCriteria;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.controller.customers.CustomerController;
import com.salesmanager.web.admin.controller.products.facade.ManufactureFacade;
import com.salesmanager.web.admin.entity.products.ReadableManufactureList;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.DeleteAll;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

@Controller
public class ManufacturerController {
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired 
	ManufactureFacade manufactureFacade ; 
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/list.html", method={RequestMethod.GET,RequestMethod.POST})
	public String getManufacturers(@Valid @ModelAttribute("criteria") ManufactureCriteria criteria,@RequestParam(value = "page", defaultValue = "1") final int page,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PaginationData paginaionData=createPaginaionData(page,15);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		if(criteria == null) criteria = new ManufactureCriteria();
		
		if (!store.getCode().equals(Constants.DEFAULT_STORE)) {
			criteria.setStoreId(store.getId()); //如果是非系统管理，则只允许查看到自己创建的品牌
		}
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		Language language = (Language)request.getAttribute("LANGUAGE");
		ReadableManufactureList olist = manufactureFacade.getByCriteria(criteria, language);
		
		model.addAttribute( "manus", olist);
		if(olist!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, olist.getTotalCount()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		
		
		this.setMenu(model, request,"manufacturer-list");
		
		return ControllerConstants.Tiles.Product.manufacturerList;
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/removes.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteManufactures(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		DeleteAll delteAll = new DeleteAll();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		try{
			
			delteAll.setService(manufacturerService);
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
		LOGGER.info(returnString);
		return returnString;
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/create.html", method=RequestMethod.GET)
	public String createManufacturer(  Model model,  HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return displayManufacturer(null,model,request,response);		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/edit.html", method=RequestMethod.GET)
	public String editManufacturer(@RequestParam("id") long manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayManufacturer(manufacturerId,model,request,response);
	}
	
	private String displayManufacturer(Long manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model,request,"manufacturer-create");
		
		//List<Language> languages = languageService.getLanguages();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		List<Language> languages = languageService.getLanguages();
		
		
		com.salesmanager.web.admin.entity.catalog.Manufacturer manufacturer = new com.salesmanager.web.admin.entity.catalog.Manufacturer();		
		List<ManufacturerDescription> descriptions = new ArrayList<ManufacturerDescription>();

		
		if( manufacturerId!=null && manufacturerId.longValue()!=0) {	//edit mode

			Manufacturer dbManufacturer = new Manufacturer();
			dbManufacturer = manufacturerService.getById( manufacturerId );
			
			if(dbManufacturer==null) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}
			if(!store.getCode().equals(Constants.DEFAULT_STORE)&&!dbManufacturer.getMerchantStore().getId().equals(store.getId())) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}
			
			Set<ManufacturerDescription> manufacturerDescriptions = dbManufacturer.getDescriptions();

			
			for(Language l : languages) {
				
				ManufacturerDescription manufDescription = null;
				if(manufacturerDescriptions!=null) {
					
					for(ManufacturerDescription desc : manufacturerDescriptions) {				
						String code = desc.getLanguage().getCode();
						if(code.equals(l.getCode())) {
							manufDescription = desc;
						}

					}
					
				}
				
				if(manufDescription==null) {
					manufDescription = new ManufacturerDescription();
					manufDescription.setLanguage(l);
				}
				
				manufacturer.getDescriptions().add(manufDescription);
				
			}
			
			manufacturer.setManufacturer( dbManufacturer );
		
			
			manufacturer.setOrder( dbManufacturer.getOrder() );
			
		} else {	// Create mode

			Manufacturer manufacturerTmp = new Manufacturer();
			manufacturer.setManufacturer( manufacturerTmp );
			
			for(Language l : languages) {// for each store language
				
				ManufacturerDescription manufacturerDesc = new ManufacturerDescription();
				manufacturerDesc.setLanguage(l);
				descriptions.add(  manufacturerDesc );
				manufacturer.setDescriptions(descriptions);
				
			}
		}

		model.addAttribute("languages",languages);
		model.addAttribute("manufacturer", manufacturer);
		
		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}
		
	@PreAuthorize("hasRole('PRODUCTS')")  
	@RequestMapping(value="/admin/catalogue/manufacturer/save.html", method=RequestMethod.POST)
	public String saveManufacturer( @Valid @ModelAttribute("manufacturer") com.salesmanager.web.admin.entity.catalog.Manufacturer manufacturer, BindingResult result, Model model,  HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		this.setMenu(model, request,"manufacturer-create");
		//save or edit a manufacturer

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		List<Language> languages = languageService.getLanguages();


		if (result.hasErrors()) {
			model.addAttribute("languages",languages);
			return ControllerConstants.Tiles.Product.manufacturerDetails;
		}
		Set<ManufacturerDescription> descriptions = new HashSet<ManufacturerDescription>();
		Manufacturer newManufacturer = manufacturer.getManufacturer();

		String bandname="";
		if(manufacturer.getDescriptions()!=null && manufacturer.getDescriptions().size()>0) {
			
			for(ManufacturerDescription desc : manufacturer.getDescriptions()) {
				
				desc.setManufacturer(newManufacturer);
				descriptions.add(desc);
				bandname=desc.getName();
			}
		}
		
		if ( newManufacturer.getId() !=null && newManufacturer.getId()  > 0 ){

			newManufacturer = manufacturerService.getById( manufacturer.getManufacturer().getId() );
			//非系统商铺不允许删除不是自己创建的品牌
			if(!store.getCode().equals(Constants.DEFAULT_STORE)&&!newManufacturer.getMerchantStore().getId().equals(store.getId())) {
				ObjectError error = new ObjectError("repeatName", messages.getMessage("message.manf.store.diff", locale));
				result.addError(error);
			}			
		} else {
			//品牌名称不能重复
			if(!StringUtils.isBlank(bandname)){
				List<Manufacturer> ms= manufacturerService.getListByName(bandname);
				if(ms !=null && ms.size()>0){
					ObjectError error = new ObjectError("repeatName", messages.getMessage("label.manufactureredit.manufacturername", locale)+messages.getMessage("label.common.already.exist", locale));
					result.addError(error);
				}else{
					newManufacturer.setMerchantStore(store);
				}	
			}else{
				ObjectError error = new ObjectError("notEmpty", messages.getMessage("label.manufactureredit.manufacturername", locale)+messages.getMessage("NotEmpty.normal", locale));
				result.addError(error);
			}	
		}
		
		if (result.hasErrors()) {
			model.addAttribute("languages",languages);
			return ControllerConstants.Tiles.Product.manufacturerDetails;
		}
		
		
		
		newManufacturer.setDescriptions(descriptions );
		newManufacturer.setOrder( manufacturer.getOrder() );


		if(manufacturer.getImage()!=null && !manufacturer.getImage().isEmpty()) {


		} else {
			Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
			manufacturerService.saveOrUpdate(newManufacturer,manMap);
		}

		model.addAttribute("manufacturer", manufacturer);
		model.addAttribute("languages",languages);
		model.addAttribute("success","success");

		return ControllerConstants.Tiles.Product.manufacturerDetails;

	}
	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageManufacturers(HttpServletRequest request, HttpServletResponse response) {
		
		AjaxResponse resp = new AjaxResponse();
		try {
			
			Language language = (Language)request.getAttribute("LANGUAGE");	
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			List<Manufacturer> manufacturers = null;				
			manufacturers = manufacturerService.listByStore(store, language);
				
			for(Manufacturer manufacturer : manufacturers) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", manufacturer.getId());
				
				ManufacturerDescription description = manufacturer.getDescriptions().iterator().next();
				
				entry.put("attribute", description.getName());
				entry.put("order", manufacturer.getOrder());
				resp.addDataEntry(entry);
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);	
		
		} catch (Exception e) {
			LOGGER.error("Error while paging Manufacturers", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/remove.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String deleteManufacturer(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		Long sid =  Long.valueOf(request.getParameter("entity") );
	
	
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		try{
			Manufacturer delManufacturer = manufacturerService.getById(sid);	
			//非系统商铺不允许删除不是自己创建的品牌
			if(delManufacturer==null || (!store.getCode().equals(Constants.DEFAULT_STORE)&&!delManufacturer.getMerchantStore().getId().equals(store.getId()))) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			} 
			
			int count = manufacturerService.getCountManufAttachedProducts( delManufacturer );  
			//IF already attached to products it can't be deleted
			if ( count > 0 ){
				resp.setStatusMessage(messages.getMessage("message.product.association", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}	
			Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
			manufacturerService.delete( delManufacturer,manMap );
			
			resp.setStatusMessage(messages.getMessage("message.success", locale));
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			
		} catch (Exception e) {
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);	
			LOGGER.error("Cannot delete manufacturer.", e);
		}
		
		String returnString = resp.toJSONString();
		LOGGER.info(returnString);
		return returnString;
		
	}
	
	private void setMenu(Model model, HttpServletRequest request, String activeMenu) throws Exception {		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();		
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put(activeMenu,activeMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}

}
