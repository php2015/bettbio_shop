package com.salesmanager.web.admin.controller.merchant;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.model.DeliveryCompny;
import com.salesmanager.core.business.common.service.DeliveryCompnyService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.InputContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.customer.model.GftsOrderCriteria;
import com.salesmanager.core.business.customer.model.GiftOrder;
import com.salesmanager.core.business.customer.model.GiftOrdersList;
import com.salesmanager.core.business.customer.model.GiftStatus;
import com.salesmanager.core.business.customer.service.GiftsOrderService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.controller.merchant.facade.StoreFacade;
import com.salesmanager.web.admin.entity.products.UploadProductsResult;
import com.salesmanager.web.admin.entity.reference.Size;
import com.salesmanager.web.admin.entity.reference.Weight;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.ReadableGiftOrder;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.entity.shop.ReadableStore;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.DeleteAll;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LoadDataThread;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.PageBuilderUtils;
import com.salesmanager.web.utils.PinyinUtils;
import com.salesmanager.web.utils.RadomSixNumber;
import com.salesmanager.web.utils.StoresExcelView;
import com.salesmanager.web.utils.UserUtils;
import com.salesmanager.web.utils.ValidPageField;

@Controller
public class MerchantStoreController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantStoreController.class);
	
	@Autowired
	StoreFacade storeFacade;
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CurrencyService currencyService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private DeliveryCompnyService deliveryCompnyService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private GiftsOrderService giftOrderImpl;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private StoresExcelView storesExcelView;
	
	@Autowired
	LoadDataThread loadDataThread;
	
	private final static String NEW_STORE_TMPL = "email_template_new_store.ftl";
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/list.html", method={RequestMethod.GET,RequestMethod.POST})
	public String displayStores(@Valid @ModelAttribute("criteria") Criteria criteria,Model model, HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestParam(value = "page", defaultValue = "1") final int page) throws Exception {
		
		PaginationData paginaionData=createPaginaionData(page,15);
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		ReadableStore stores = storeFacade.getByCriteria(criteria);
		model.addAttribute( "stores", stores);
		if(stores!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, stores.getTotal()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		setMenu(model,request,"store-list");
		return ControllerConstants.Tiles.Store.stores;
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/available.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String available(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("id");
		String active = request.getParameter("active");
		

		AjaxResponse resp = new AjaxResponse();
		if(!StringUtils.isBlank(values) && !StringUtils.isBlank(active)) {
			
			try {
				
				Long id = Long.parseLong(values);
				MerchantStore store = merchantStoreService.getById(id);
				store.setSeeded(Boolean.parseBoolean(active));
				merchantStoreService.saveOrUpdate(store);
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
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/storeCreate.html", method=RequestMethod.GET)
	public String displayMerchantStoreCreate(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		
		setMenu(model,request,"create-store");

		MerchantStore store = new MerchantStore();
		
		MerchantStore sessionStore = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		store.setCurrency(sessionStore.getCurrency());
		store.setCountry(sessionStore.getCountry());
		store.setZone(sessionStore.getZone());
		store.setStorestateprovince(sessionStore.getStorestateprovince());
		store.setLanguages(sessionStore.getLanguages());
		store.setDomainName(sessionStore.getDomainName());
		

		return displayMerchantStore(store, model, request, response, locale);
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/map.html", method={RequestMethod.GET,RequestMethod.POST})
	public String displayStoreMap(@Valid @ModelAttribute("criteria") Criteria criteria,Model model, HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestParam(value = "page", defaultValue = "1") final int page) throws Exception {
		
		setMenu(model,request,"storeMap");
		return "admin-store-map";
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/storeShow.html", method={RequestMethod.GET,RequestMethod.POST})
	public String displayStoreShow(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model,request,"storeShow");
		return "admin-store-show";
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/delivery.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = deliveryCompnyService.getDeliveryName();
			
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
	
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/store/setdelivery.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String delivery(HttpServletRequest request) throws Exception {
		String suborderid = request.getParameter("suborderid");
		String dCode = request.getParameter("dCode");
		String dNo = request.getParameter("dNo");
		AjaxResponse resp = new AjaxResponse();
		if(!StringUtils.isBlank(suborderid)  && !StringUtils.isBlank(dCode) && !StringUtils.isBlank(dNo)){
			dCode.trim().toUpperCase();
			List<DeliveryCompny> dels = deliveryCompnyService.getDeliveryByName(dCode);
			if(dels ==null || dels.size()==0){
				DeliveryCompny deliveryCompny = new DeliveryCompny();
				deliveryCompny.setDeliveryName(dCode);
				deliveryCompnyService.create(deliveryCompny);
			}
			
			try{
				GiftOrder giftOrder = giftOrderImpl.getById(Long.parseLong(suborderid));
				giftOrder.setDeliveryCode(dCode);
				giftOrder.setDeliveryNumber(dNo);
				giftOrder.setStatus(GiftStatus.SHIPPED);
				giftOrderImpl.saveOrUpdate(giftOrder);
			}catch (Exception e){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
			
				resp.setStatus(1);
				resp.setStatusMessage(OrderStatus.SHIPPED.getValue().toUpperCase());
			
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return resp.toJSONString();
	}
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/memberpoint.html", method={RequestMethod.GET,RequestMethod.POST})
	public String displayMemberPoint(@Valid @ModelAttribute("criteria") GftsOrderCriteria criteria,final Model model,@RequestParam(value = "page", defaultValue = "1") final int page, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		
		PaginationData paginaionData=createPaginaionData(page,Constants.MAX_ORDERS_PAGE);
    	criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		GiftOrdersList gifts =giftOrderImpl.getByCriteria(criteria);
		 if(gifts!=null && gifts.getTotalCount()>0) {
	        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, gifts.getTotalCount()));
	        	List<ReadableGiftOrder> reads = new ArrayList<ReadableGiftOrder>();
		        	for(GiftOrder gorder:gifts.getGiftsOrder()){
		        		try{
		        			ReadableGiftOrder read = new ReadableGiftOrder();
		        			read.setId(gorder.getId());
			        		read.setCustomerName(gorder.getCustomer().getNick());
			        		//if(gorder.getCustomerAddress() !=null){
			        			read.setDeliverAddress(gorder.getShippingAddress());
			        			read.setDeliveryName(gorder.getCustomerName());
			        		//}
			        		if(!StringUtils.isBlank(gorder.getDeliveryCode()))read.setDeliverCompany(gorder.getDeliveryCode());
			        		if(!StringUtils.isBlank(gorder.getDeliveryNumber()))read.setDeliverNumber(gorder.getDeliveryNumber());
			        		read.setImge(gorder.getGifImge());
			        		read.setQulity(gorder.getNumber());
			        		read.setName(gorder.getGifName());
			        		read.setStatus(gorder.getStatus());
			        		read.setOrderDate(gorder.getCreateDate().toString());
			        		reads.add(read);		     
		        		}catch (Exception e){
		        			continue;
		        		}
		        		   		
		        	}
	        	model.addAttribute("emberPointList",reads);   
		 
		 } else {
	        	model.addAttribute( "paginationData", null);
	     }		 
		 GiftStatus[] allStatus = GiftStatus.values();
		 
		if(criteria.getStatus() !=null ){
			model.addAttribute("mpsStatus",criteria.getStatus());
		}
		
		
        model.addAttribute("giftStatus",allStatus);

		setMenu(model,request,"meberpoint");
		return "admin-store-memberpoint";
	}
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/store.html", method=RequestMethod.GET)
	public String displayMerchantStore(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		//modify by cy 如果不是默认商铺则不给其他菜单
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		setMenu(model,request,"storeDetails");
		return displayMerchantStore(store, model, request, response, locale);
	}
		
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/qqset.html", method=RequestMethod.GET)
	public String qqSet(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
				
		setMenu(model,request,"storeDetails");
		return "admin-store-qq";
	}

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/editStore.html", method=RequestMethod.GET)
	public String eidtMerchantStore(@ModelAttribute("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model,request,"storeDetails");
		MerchantStore store = merchantStoreService.getById(id);
		return displayMerchantStore(store, model, request, response, locale);
	}
	
	private String displayMerchantStore(MerchantStore store, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		//setMenu(model,request);
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Language> languages = languageService.getLanguages();
		List<Currency> currencies = currencyService.list();
		Date dt = store.getInBusinessSince();
		if(dt!=null) {
			store.setDateBusinessSince(DateUtil.formatDate(dt));
		} else {
			store.setDateBusinessSince(DateUtil.formatDate(new Date()));
		}
		
		if(store.getLanguages()==null || store.getLanguages().size()==0){
			store.setLanguages(languages);
		}
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		
		//display menu

		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		model.addAttribute("currencies",currencies);
		
		//model.addAttribute("weights",weights);
		//model.addAttribute("sizes",sizes);
		model.addAttribute("store", store);
		
		
		return "admin-store";
		
		
	}
	

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/save.html", method=RequestMethod.POST)
	public String saveMerchantStore(@Valid @ModelAttribute("store") MerchantStore store, BindingResult result, 
			@RequestParam(required=false, value="licenceimg") CommonsMultipartFile licenceimg, 
			Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Boolean isNew =false;
		if(store.getId()==null){
			isNew=true;
		}
		setMenu(model,request,"storeDetails");
		MerchantStore sessionStore = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		MerchantStore dbStore = null;
		if(store.getId()!=null) {
			dbStore = merchantStoreService.getById(store.getId());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(store.getId().intValue() != sessionStore.getId().intValue()) {//非管理员用户不允许编辑自己所属商铺
					return "redirect:/admin/store/store.html";
				} else {
					store.setDomainName(sessionStore.getDomainName()); //如果非系统管理员处理本商铺，统一默认使用DEFAULT商铺的域名					
				}
				//非admin不能更改种子用户的信息
				store.setSeeded(dbStore.getSeeded());
			} 
			
		//设置商品code
		}else{
			//商铺名称不能重复
			MerchantStore nameStore = merchantStoreService.getByName(store.getStorename());
			if(nameStore !=null ){
				ObjectError error = new ObjectError("nameRepeat",messages.getMessage("label.storename", locale)+messages.getMessage("label.common.already.exist", locale));
				if(error!=null)result.addError(error);
			}else{
				StringBuffer code = new StringBuffer();
				code.append("bett").append(RadomSixNumber.getRadomNumber());
				Long now = new Date().getTime();
				code.append(now.toString());
				store.setCode(code.toString());
			}
			
		}
		
		Date date = new Date();
		if(!StringUtils.isBlank(store.getDateBusinessSince())) {
			try {
				date = DateUtil.getDate(store.getDateBusinessSince());
				store.setInBusinessSince(date);
			} catch (Exception e) {
				ObjectError error = new ObjectError("dateBusinessSince",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}
		
		List<Currency> currencies = currencyService.list();
		
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Language> languages = languageService.getLanguages();
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		List<Weight> weights = new ArrayList<Weight>();
		weights.add(new Weight("LB",messages.getMessage("label.generic.weightunit.LB", locale)));
		weights.add(new Weight("KG",messages.getMessage("label.generic.weightunit.KG", locale)));
		
		List<Size> sizes = new ArrayList<Size>();
		sizes.add(new Size("CM",messages.getMessage("label.generic.sizeunit.CM", locale)));
		sizes.add(new Size("IN",messages.getMessage("label.generic.sizeunit.IN", locale)));
		
		model.addAttribute("weights",weights);
		model.addAttribute("sizes",sizes);
		
		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		model.addAttribute("currencies",currencies);
		
		
		Country c = store.getCountry();
		List<Zone> zonesList = zoneService.getZones(c, language);
		
		if((zonesList==null || zonesList.size()==0) && StringUtils.isBlank(store.getStorestateprovince())) {
			
			ObjectError error = new ObjectError("zone.code",messages.getMessage("merchant.zone.invalid", locale));
			result.addError(error);
			
		}
		//判断商铺代码是否唯一
		List<MerchantStore> tmp = merchantStoreService.findByProperties(new String[]{"code"}, new String[]{store.getCode()}, null);
		if (tmp!=null && tmp.size()>0) {
			if(!(store.getId()!=null&&tmp.get(0).getId().equals(store.getId()))){
				ObjectError error = new ObjectError("store.code",messages.getMessage("message.code.exist", locale));
				result.addError(error);
			}
		}
		//手机等验证
		if(store.getStoremobile()!=null && !StringUtils.isBlank(store.getStoremobile())){
			ObjectError error = ValidPageField.validmobile(store.getStoremobile(), locale,messages);
			if(error!=null)result.addError(error);
		}
		//电话等验证
		/**
		if(store.getStorephone()!=null && !StringUtils.isBlank(store.getStorephone())){
			ObjectError error = ValidPageField.validphone(store.getStorephone(), locale,messages);
			if(error!=null)result.addError(error);
		}
		//传真验证
		if(store.getStorefax()!=null && !StringUtils.isBlank(store.getStorefax())){
			ObjectError error = ValidPageField.validfax(store.getStorefax(), locale,messages);
			if(error!=null)result.addError(error);
		}*/
		//邮箱验证
		if(store.getStoreEmailAddress()!=null && !StringUtils.isBlank(store.getStoreEmailAddress())){
			ObjectError error = ValidPageField.validEmail(store.getStoreEmailAddress(), locale,messages);
			if(error!=null)result.addError(error);
		}

		if (result.hasErrors()) {
			return "admin-store";
		}
		
		//get country
		Country country = store.getCountry();
		country = countryService.getByCode(country.getIsoCode());
		Zone zone = store.getZone();
		if(zone!=null) {
			zone = zoneService.getByCode(zone.getCode());
		}
		Currency currency = store.getCurrency();
		currency = currencyService.getById(currency.getId());

		List<Language> supportedLanguages = store.getLanguages();
		List<Language> supportedLanguagesList = new ArrayList<Language>();
		Map<String,Language> languagesMap = languageService.getLanguagesMap();
		for(Language lang : supportedLanguages) {
			
			Language l = languagesMap.get(lang.getCode());
			if(l!=null) {
				supportedLanguagesList.add(l);
			}
			
		}
		
		Language defaultLanguage = store.getDefaultLanguage();
		defaultLanguage = languageService.getById(defaultLanguage.getId());
		if(defaultLanguage!=null) {
			store.setDefaultLanguage(defaultLanguage);
		}
		
		Locale storeLocale = LocaleUtils.getLocale(defaultLanguage);
		
		store.setStoreTemplate(sessionStore.getStoreTemplate());
		store.setCountry(country);
		store.setZone(zone);
		store.setCurrency(currency);
		store.setDefaultLanguage(defaultLanguage);
		store.setLanguages(supportedLanguagesList);
		//处理营业执照图片
		if (licenceimg!=null && licenceimg.getSize()>0) {
			String imageName = licenceimg.getOriginalFilename();
			String imageN = RadomSixNumber.getImageName(imageName);
			LOGGER.info(imageName);
            InputStream inputStream = licenceimg.getInputStream();
            InputContentFile cmsContentImage = new InputContentFile();
            cmsContentImage.setFileName(imageN);
            cmsContentImage.setMimeType( licenceimg.getContentType());
            cmsContentImage.setFile( inputStream );
            cmsContentImage.setFileContentType(FileContentType.STORELICENCE);
            
            contentService.addContentFile(store.getCode(), cmsContentImage);
            store.setBusinesslicence(imageN);
		}
		
		merchantStoreService.saveOrUpdate(store);
		//如果修改商铺的名称，则需要处理更新该商铺下的所有产品的ES中的商家信息
		if (dbStore != null&&!store.getStorename().equals(dbStore.getStorename())) {
			loadDataThread.setLanguage(language);
			loadDataThread.setFlag(LoadDataThread.updateindex);
			loadDataThread.setStoreId(dbStore.getId());
			Thread t = new Thread(loadDataThread);
			t.start();
		}
		
		if(isNew==true) {//create store
			
			
			try {


				Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, storeLocale);
				templateTokens.put(EmailConstants.EMAIL_NEW_STORE_TEXT, messages.getMessage("email.newstore.text", storeLocale));
				templateTokens.put(EmailConstants.EMAIL_STORE_NAME, messages.getMessage("email.newstore.name",new String[]{store.getStorename()},storeLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_STORE_INFO_LABEL, messages.getMessage("email.newstore.info",storeLocale));

				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",storeLocale));
				templateTokens.put("EMAIL_ADMIN_QQ_LABEL", messages.getMessage("email.newstore.qqInfo",storeLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, FilePathUtils.buildAdminUri(store, request));
	
				
				Email email = new Email();
				email.setFrom(store.getStorename());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.newstore.title",storeLocale));
				email.setTo(store.getStoreEmailAddress());
				email.setTemplateName(NEW_STORE_TMPL);
				email.setTemplateTokens(templateTokens);
	
	
				
				emailService.sendHtmlEmail(store, email);
			
			} catch (Exception e) {
				LOGGER.error("Cannot send email to user",e);
			}
			
		}

		sessionStore = merchantStoreService.getMerchantStore(sessionStore.getCode());
		
		
		//update session store
		request.getSession().setAttribute(Constants.ADMIN_STORE, sessionStore);


		model.addAttribute("success","success");
		model.addAttribute("store", store);

		
		return "admin-store";
	}
	
	
	@RequestMapping(value="/admin/store/checkStoreCode.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String checkStoreCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");


		AjaxResponse resp = new AjaxResponse();
		
		try {
			
			if(StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
				return resp.toJSONString();
			}
			
			MerchantStore store = merchantStoreService.getByName(code);
		


			
			if(store!=null) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}



			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/remove.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String removeMerchantStore(HttpServletRequest request, Locale locale) throws Exception {

		String sMerchantStoreId = request.getParameter("entity");

		AjaxResponse resp = new AjaxResponse();
		
		try {
			
			Long storeId = Long.parseLong(sMerchantStoreId);
			MerchantStore store = merchantStoreService.getById(storeId);
			
			User user = userService.getByUserName(request.getRemoteUser());
			
			/**
			 * In order to remove a Store the logged in ser must be SUPERADMIN
			 */

			//check if the user removed has group SUPERADMIN
			boolean isSuperAdmin = false;
			if(UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {
				isSuperAdmin = true;
			}

			
			if(!isSuperAdmin) {
				resp.setStatusMessage(messages.getMessage("message.security.caanotremovesuperadmin", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			
			merchantStoreService.delete(store);
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/store/removes.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String deleteProducts(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		DeleteAll delteAll = new DeleteAll();		
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		try{
			
			//check if the user removed has group SUPERADMIN
			User user = userService.getByUserName(request.getRemoteUser());
			boolean isSuperAdmin = false;
			if(UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {
				isSuperAdmin = true;
			}

			
			if(!isSuperAdmin) {
				resp.setStatusMessage(messages.getMessage("message.security.caanotremovesuperadmin", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			delteAll.setService(merchantStoreService);
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
	
	private void setMenu(Model model, HttpServletRequest request, String activeMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("store", "store");
		activeMenus.put(activeMenu, activeMenu);

		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("store");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/licence/removeImage.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeImage(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response, Locale locale) {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		//判断id是否是当前用户所属商铺，如果是admin管理员，则不作权限控制
		if(auth == null || !request.isUserInRole("ADMIN")){
			if (!store.getId().equals(id)) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString(messages.getMessage("message.access.denied", locale));
				return resp.toJSONString();
			}
		}
		
		try {
			String fileName = this.merchantStoreService.queryByStoreFileName(id);
			store.setBusinesslicence(fileName);
			contentService.removeFile(store.getCode(), FileContentType.STORELICENCE, store.getBusinesslicence());
			store.setBusinesslicence(null);
			merchantStoreService.update(store);
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting store business licence", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/storeImport.html", method=RequestMethod.GET)
	public String storeImport(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	//	MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);	
	//	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		setMenu(model, request, "storeImport");
		return ControllerConstants.Tiles.Store.storeImport;
	}
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/doStoreImport.html", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String upload(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		//Date dt = new Date();
		//MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		
		if (uploadfile == null || uploadfile.isEmpty()) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("upload.error.02", locale));
			return resp.toJSONString();
		} 
		//admin has all products
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	//	Language language = (Language)request.getAttribute("LANGUAGE");

		//判断上传文件格式是否是xls,或者是xlsx
		String originalFilename = uploadfile.getOriginalFilename();
		if(!(originalFilename.endsWith("xls")||originalFilename.endsWith("xlsx"))){
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("upload.error.02", locale));
			return resp.toJSONString();
		}
	
		//上传文件处理
		UploadProductsResult result = storesExcelView.importStores(uploadfile.getInputStream(), user);
		if (result.getStatus().equals(UploadProductsResult.StatusEnum.SUCCESS.getCode())) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			Map map = resp.getDataMap();
			map.put("successlines", result.getSuccesslines());
		} else if (result.getStatus().equals(UploadProductsResult.StatusEnum.PARTIALSUCCESS.getCode())) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
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
	
}
