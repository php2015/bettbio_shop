package com.salesmanager.web.admin.controller.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.WorkModeConfiguration;
import com.salesmanager.web.admin.controller.user.facade.UserFacade;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.customer.SecuredShopPersistableCustomer;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.RadomSixNumber;

@Controller
public class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	LabelUtils messages;
	@Autowired
	GroupService groupService;	
	@Autowired
	LanguageService languageService;
	@Autowired
	UserService userService;
	@SuppressWarnings("deprecation")
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	MerchantStoreService merchantStoreService;
	@Autowired
	UserFacade userFacade;
	@Autowired
	EmailService emailService;
	@Autowired
	CustomerService customerService;
	@Autowired
	WorkModeConfiguration workMode;
	@Autowired
	private CustomerFacade customerFacade;
	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;
	
	@SuppressWarnings("unused")
	private final static String NEW_USER_TMPL = "email_template_new_user.ftl";

	@RequestMapping(value="/admin/logon.html", method=RequestMethod.GET)
	public String displayLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return "admin/logon";
		
		
	}
	

	@RequestMapping(value="/admin/denied.html", method=RequestMethod.GET)
	public String displayDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		//logoff the user
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	         new SecurityContextLogoutHandler().logout(request, response, auth);
	         //new PersistentTokenBasedRememberMeServices().logout(request, response, auth);
	    }
		
		return "admin/logon";
		
		
	}
	
	@RequestMapping(value="/admin/registerUser.html", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		String msg = null;
		String captchaResponseField= request.getParameter("captchaResponseField");
		if ( StringUtils.isBlank( captchaResponseField )) {
			ObjectError error = new ObjectError("captchaResponseField",messages.getMessage("NotEmpty.contact.captchaResponseField", locale));
			result.addError(error);
		}else{
			HttpSession session = request.getSession();
			if(session.getAttribute("validaioncode")==null || !captchaResponseField.equalsIgnoreCase(session.getAttribute("validaioncode").toString())){
				 LOGGER.debug( "Captcha response does not matched" );        			
        			ObjectError error = new ObjectError("captchaResponseField",messages.getMessage("validaion.recaptcha.not.matched", locale));
        			result.addError(error);
			}
		}
		
		Language language = user.getDefaultLanguage();
		
		if(language == null || language.getId()==null){
			language = store.getDefaultLanguage();
		}
		Language l = languageService.getById(language.getId());
		
		user.setDefaultLanguage(l);
		
		Locale userLocale = LocaleUtils.getLocale(l);
		
		List<Group> groups = new ArrayList<Group>();
		List<Group> userGroups = groupService.listGroup(GroupType.ADMIN);
		for(Group group : userGroups) {
			if(!group.getGroupName().equals(Constants.GROUP_SUPERADMIN) && !group.getGroupName().equals(Constants.GROUP_ADMIN)) {
				groups.add(group);
			}
		}
		
		
		
	
	
		//新建名称重复
		User uname = userService.getByUserName(user.getAdminName());
		if(uname !=null){
			ObjectError error = new ObjectError("repeat",messages.getMessage("message.code.exist", locale));
			result.addError(error);
			
		}

				
		Set<Integer> ids = new HashSet<Integer>();
		
			for(Group group : groups) {
				ids.add(group.getId());
			}
				
		
			
			if(user.getAdminPassword().length()<6) {
				ObjectError error = new ObjectError("adminPassword",messages.getMessage("message.password.length", locale));
				result.addError(error);
			}
			
		

		if (ids!=null && ids.size()>0) {
			List<Group> newGroups = groupService.listGroupByIds(ids);
			//set actual user groups
			user.setGroups(newGroups);
		}
		
		//商铺名称不能重复
		String code = request.getParameter("storename");
		if(StringUtils.isBlank(code)) {
			ObjectError error = new ObjectError("nameRepeat",messages.getMessage("label.storename", locale)+messages.getMessage("NotEmpty.generic.name", locale));
			result.addError(error);
		}else{
			MerchantStore ustore = merchantStoreService.getByName(code);		
			if(ustore !=null ){
				ObjectError error = new ObjectError("nameRepeat",messages.getMessage("label.storename", locale)+messages.getMessage("label.common.already.exist", locale));
				result.addError(error);
			
		}
		
		if (result.hasErrors()) {
			return "admin/storeRegister";
		}
		
		//String decodedPassword = user.getAdminPassword();
		String plainPassword = user.getAdminPassword();
		String encoded = passwordEncoder.encodePassword(user.getAdminPassword(),null);
		user.setAdminPassword(encoded);
		
		
		MerchantStore newStore =  new MerchantStore();
		//设置店铺基本信息
		newStore.setCurrency(store.getCurrency());
		newStore.setCountry(store.getCountry());
		newStore.setZone(store.getZone());
		newStore.setStorestateprovince(store.getStorestateprovince());
		List<Language> supportedLanguagesList = new ArrayList<Language>();
		supportedLanguagesList.add(l);
		newStore.setLanguages(supportedLanguagesList);
		newStore.setDefaultLanguage(l);
		
		newStore.setDomainName(store.getDomainName());
		newStore.setDateBusinessSince(DateUtil.formatDate(new Date()));
		newStore.setStoreTemplate(store.getStoreTemplate());	
		newStore.setStorename(code);
		
		StringBuffer storeCode = new StringBuffer();
		storeCode.append("bett").append(RadomSixNumber.getRadomNumber());
		Long now = new Date().getTime();
		storeCode.append(now.toString());
		newStore.setCode(storeCode.toString());
		newStore.setStoreEmailAddress(user.getAdminEmail());
		merchantStoreService.saveOrUpdate(newStore);
		user.setMerchantStore(newStore);
		user.setMobile(newStore.getStoremobile());
		
		if(user.getId()==null || user.getId().longValue()==0) {
			//save or update user
			try{
				AuditSection auditSection = new AuditSection();
				auditSection.setAudit(0);
				user.setAuditSection(auditSection);
				userService.saveOrUpdate(user);
				if (createBuyerAutomaticaly(user, plainPassword, language, request)){
                	model.addAttribute("autoCreateBuyer", true);
                	model.addAttribute("buyerEmail", user.getAdminEmail());
                }
			}catch (Exception e){
				LOGGER.error("faild create user",e);
				merchantStoreService.delete(newStore);
				ObjectError error = new ObjectError("nameRepeat",messages.getMessage("message.error", locale));
				result.addError(error);
				return "admin/storeRegister";
			}
				
		}
		
		try {

			//creation of a user, send an email
			String userName = user.getFirstName();
			if(StringUtils.isBlank(userName)) {
				userName = user.getAdminName();
			}
			String[] userNameArg = {userName};
			
			
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, userLocale);
			templateTokens.put(EmailConstants.EMAIL_NEW_USER_TEXT, messages.getMessage("email.greeting", userNameArg, userLocale));
			templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, user.getFirstName());
			templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, user.getLastName());
			templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",userLocale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_NAME, user.getAdminName());
			templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_CREATED, messages.getMessage("email.newuser.text",userLocale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, "");
			templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD, "");
			templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",userLocale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, FilePathUtils.buildAdminUri(store, request));

			
			Email email = new Email();
			email.setFrom(store.getStorename());
			email.setFromEmail(store.getStoreEmailAddress());
			email.setSubject(messages.getMessage("email.newuser.title",userLocale));
			List<String> cc = new ArrayList<String>();
			cc.add(store.getStoreEmailAddress());
			email.setTo(user.getAdminEmail());
			email.setTemplateName(NEW_USER_TMPL);
			email.setCc(cc);
			email.setTemplateTokens(templateTokens);
			
			emailService.sendHtmlEmail(store, email);
		
		} catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
			msg = e.getMessage();
		}
			
		}
		String message=  messages.getMessage("label.bettbio.admin.user.create", locale);
		if(msg != null)
		{
			model.addAttribute( "information", (store.getStoreEmailAddress() == null ? "" : (store.getStoreEmailAddress()+":"))+""+msg.toString() );
		}
		else
		{
			model.addAttribute( "information", message );
		}
		
		
		return "admin/information";
	}
	
	@RequestMapping(value="/admin/register.html", method=RequestMethod.GET)
	public String register(Model model, HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		User user = new User();
		
		model.addAttribute("user", user);
		return "admin/storeRegister";
	}
	
	@RequestMapping(value="/admin/unauthorized.html", method=RequestMethod.GET)
	public String unauthorized(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "admin/unauthorized";
	}

	private boolean createBuyerAutomaticaly(User user, String plainPassword, Language language, HttpServletRequest request) {
		String mobile = user.getMobile();
		Customer existedCustomer = customerService.getByPhone(mobile);
		if (existedCustomer == null){
			existedCustomer = customerService.getByEmail(user.getAdminEmail());
		}
		if (existedCustomer != null){
			return false;
		}
		
		
		MemberPoints memberPoints = new MemberPoints();
//    	String points = getPoint(Constants.REGIST_SCORE);
    	String points = "50";
    	
    	memberPoints.setType(Constants.REGIST_SCORE);
    	memberPoints.setUpdateDate(new Date());
       
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.YEAR,1);
    	memberPoints.setDateValid(cal.getTime());
    	//memberPoints.setCustomer(c);
    	memberPoints.setStatas(0);
    	
    	memberPoints.setValue(points);
    	//set left
    	try{
    		memberPoints.setLtfePoint(Long.parseLong(points));
    	}catch(Exception e){
    		
    	}
    	
    	SecuredShopPersistableCustomer customer = new SecuredShopPersistableCustomer();
    	//set user clear password
    	customer.setAddressDefault(-1);
    	customer.setAnonymous(0);
    	customer.setAttributes(null);
    	customer.setCheckPassword(plainPassword);
    	customer.setClearPassword(plainPassword);
    	customer.setCompany(user.getMerchantStore().getStorename());
    	customer.setEmailAddress(user.getAdminEmail());
    	customer.setEncodedPassword(null);
    	customer.setGender(null);
    	customer.setGrade(0);
    	customer.setId(0L);
    	customer.setInvoiceaddressdefault(-1);
    	customer.setInvoiceDefault(-1);
    	customer.setLanguage(null);
    	customer.setNick(user.getAdminName());
    	customer.setPassword(plainPassword);
    	customer.setPhone(mobile);
    	customer.setPoints(null);
    	customer.setProject(user.getFirstName());
    	customer.setStoreCode(null);
    	customer.setUserName(user.getFirstName());
    	
    	MerchantStore merchantStore;
		try {
			merchantStore = merchantStoreService.getByCode(Constants.DEFAULT_STORE);
			CustomerEntity customerData = customerFacade.registerCustomer(customer, merchantStore, language, memberPoints);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("Cannot create buyer automaticaly with mobile number " + mobile);
			return false;
		}
		emailTemplatesUtils.sendActiveEmail(memberPoints.getCustomer().getId().toString(), merchantStore, request.getContextPath());
		return true;
	}
}
