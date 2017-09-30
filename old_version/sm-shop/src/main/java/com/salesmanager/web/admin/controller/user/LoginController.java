package com.salesmanager.web.admin.controller.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.controller.user.facade.UserFacade;
import com.salesmanager.web.admin.entity.secutity.Password;
import com.salesmanager.web.admin.entity.userpassword.UserReset;
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
		return null;
	}
	

	@RequestMapping(value="/admin/denied.html", method=RequestMethod.GET)
	public String displayDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		//logoff the user
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	         new SecurityContextLogoutHandler().logout(request, response, auth);
	         //new PersistentTokenBasedRememberMeServices().logout(request, response, auth);
	    }
	    response.sendRedirect("/sm-shop/shop/customer/customLogon.html");
	    
		return null;
	}
	
	@RequestMapping(value="/admin/forget.html", method=RequestMethod.GET)
	public String forgetPassword(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		//user.setAdminPassword("ffff");
		model.addAttribute("user", user);
		return "admin/forgetPassword";
	}
	@RequestMapping(value="/admin/setnewp.html", method=RequestMethod.POST)
	public String getNew(@Valid @ModelAttribute("user") User user, BindingResult result,HttpServletRequest request, HttpServletResponse response, Locale locale,Model model) throws Exception {
		
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
		User dbuser = userService.getByUserName(user.getAdminName());
		if(dbuser==null){
			ObjectError error = new ObjectError("User not found",messages.getMessage("message.username.notfound", locale));
			result.addError(error);
		}else if(!dbuser.getAdminEmail().equalsIgnoreCase(user.getAdminEmail())){
			ObjectError error = new ObjectError("User email error",messages.getMessage("message.username.emailerror", locale));
			result.addError(error);
		}
		
		if(result.hasErrors()){
			return "admin/forgetPassword";
		}
		AuditSection auditSection=dbuser.getAuditSection();
		//閺堝鎱ㄩ弨鐟扮槕閻胶娈戠拠閿嬬湴
		auditSection.setAudit(1);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		this.sendActiveEmail(dbuser, store, request.getContextPath());
		//userService.update(dbuser);
		String message=  messages.getMessage("label.bettbio.modifypassword", locale);
		model.addAttribute( "information", message );	
		return "admin/information";
		
		
	}
	
	@RequestMapping(value="/admin/unauthorized.html", method=RequestMethod.GET)
	public String unauthorized(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "admin/unauthorized";
	}
	
/*@RequestMapping(value="/admin/register.html", method=RequestMethod.GET)
	public String register(Model model, HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		User user = new User();
		
		model.addAttribute("user", user);
		return "admin/storeRegister";
	}*/
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/admin/savePassword.html", method=RequestMethod.POST)
	public String savePassword(@ModelAttribute("password") Password password, BindingResult result,Model model, HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		String userName = request.getSession().getAttribute("reuser").toString();
		User dbUser = userService.getByUserName(userName);
		if(dbUser == null){
			ObjectError error = new ObjectError("timeout",new StringBuilder().append(messages.getMessage("message.timeout", locale)).append(" ").toString());
			result.addError(error);
			
		}
		if(StringUtils.isBlank(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPassword",new StringBuilder().append(messages.getMessage("label.generic.newpassword", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
		}
		
		if(StringUtils.isBlank(password.getRepeatPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain",new StringBuilder().append(messages.getMessage("label.generic.newpassword.repeat", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
		}
		
		if(!password.getRepeatPassword().equals(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain",messages.getMessage("message.password.different", locale));
			result.addError(error);
		}
		
		String reg = "^\\s*[^\\s\u4e00-\u9fa5]{6,16}\\s*$";
        if (!password.getNewPassword().matches(reg)){
        	FieldError error = new FieldError("password","password",messages.getMessage("newpassword.not.week", locale));
        	result.addError(error);
        }		
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.password;
		}
		
		AuditSection auditSection=dbUser.getAuditSection();
		auditSection.setAudit(0);
		dbUser.setAnswer1("");
		
		String pass = passwordEncoder.encodePassword(password.getNewPassword(), null);
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);
		
		model.addAttribute("success","success");
		return "admin/password" ;
	}
	@RequestMapping(value="/admin/setPassword.html", method=RequestMethod.GET)
	public String setPassword(@RequestParam("account") String account,@RequestParam("code") String verityCode, Model model, HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		User user =  userService.getByUserName(account);
		if(user == null){
			String message=  messages.getMessage("label.bettbio.resetfailed", locale);
			 model.addAttribute( "information", message );	
			 return "admin/information";
		}
		AuditSection auditSection = user.getAuditSection();
		if(!user.getAnswer1().equalsIgnoreCase(verityCode) || auditSection.getAudit()!=1){
			String message=  messages.getMessage("label.bettbio.resetfailed", locale);
			 model.addAttribute( "information", message );	
			 return "admin/information";
		}
		//
		request.getSession().setAttribute("reuser", user.getAdminName());
		//model.addAttribute("user", user);
		return "admin/password";
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@RequestMapping(value="/admin/registerUser.html", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		request.setAttribute("c", "store");
        MerchantStore store = (MerchantStore)request.getAttribute("ADMIN_STORE");
        String msg = null;
        Language language = user.getDefaultLanguage();
        if(language == null || language.getId() == null)
            language = store.getDefaultLanguage();
        Language l = (Language)languageService.getById(language.getId());
        user.setDefaultLanguage(l);
        Locale userLocale = LocaleUtils.getLocale(l);
        List groups = new ArrayList();
        List userGroups = groupService.listGroup(GroupType.ADMIN);
        for(Iterator iterator = userGroups.iterator(); iterator.hasNext();)
        {
            Group group = (Group)iterator.next();
            if(!group.getGroupName().equals("SUPERADMIN") && !group.getGroupName().equals("ADMIN"))
                groups.add(group);
        }

        Set ids = new HashSet();
        Group group;
        for(Iterator iterator1 = groups.iterator(); iterator1.hasNext(); ids.add(group.getId()))
            group = (Group)iterator1.next();

        if(ids != null && ids.size() > 0)
        {
            List newGroups = groupService.listGroupByIds(ids);
            user.setGroups(newGroups);
        }
        String code = request.getParameter("storename");
        if(result.hasErrors())
        {
            StringBuilder template = (new StringBuilder()).append("admin-users").append(".").append(store.getStoreTemplate());
            return template.toString();
        }
        String plainPassword = user.getAdminPassword();
        String encoded = passwordEncoder.encodePassword(user.getAdminPassword(), null);
        user.setAdminPassword(encoded);
        MerchantStore newStore = new MerchantStore();
        newStore.setCurrency(store.getCurrency());
        newStore.setCountry(store.getCountry());
        newStore.setZone(store.getZone());
        newStore.setStorestateprovince(store.getStorestateprovince());
        List supportedLanguagesList = new ArrayList();
        supportedLanguagesList.add(l);
        newStore.setLanguages(supportedLanguagesList);
        newStore.setDefaultLanguage(l);
        newStore.setDomainName(store.getDomainName());
        newStore.setDateBusinessSince(DateUtil.formatDate(new Date()));
        newStore.setStoreTemplate(store.getStoreTemplate());
        newStore.setStorename(code);
        StringBuffer storeCode = new StringBuffer();
        storeCode.append("bett").append(RadomSixNumber.getRadomNumber());
        Long now = Long.valueOf((new Date()).getTime());
        storeCode.append(now.toString());
        newStore.setCode(storeCode.toString());
        newStore.setStoreEmailAddress(user.getAdminEmail());
        newStore.setStorephone(user.getMerchantStore().getStorephone());
        newStore.setStoremobile(user.getMerchantStore().getStoremobile());
        merchantStoreService.saveOrUpdate(newStore);
        user.setMerchantStore(newStore);
        user.setMobile(newStore.getStoremobile());
        model.addAttribute("fromRegisterUserAction", true);
        if(user.getId() == null || user.getId().longValue() == 0L)
            try
            {
                AuditSection auditSection = new AuditSection();
                auditSection.setAudit(0);
                user.setAuditSection(auditSection);
                userService.saveOrUpdate(user);
                if (createBuyerAutomaticaly(user, plainPassword, language, request)){
                	model.addAttribute("autoCreateBuyer", true);
                	model.addAttribute("buyerEmail", user.getAdminEmail());
                }
            }
            catch(Exception e)
            {
                LOGGER.error("faild create user", e);
                merchantStoreService.delete(newStore);
                ObjectError error = new ObjectError("nameRepeat", messages.getMessage("message.error", locale));
                result.addError(error);
                StringBuilder template = (new StringBuilder()).append("admin-users").append(".").append(store.getStoreTemplate());
                return template.toString();
            }
        try
        {
            String userName = user.getFirstName();
            if(StringUtils.isBlank(userName))
                userName = user.getAdminName();
            String userNameArg[] = {
                userName
            };
            Map templateTokens = EmailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, userLocale);
            templateTokens.put("EMAIL_NEW_USER_TEXT", messages.getMessage("email.greeting", userNameArg, userLocale));
            templateTokens.put("EMAIL_USER_FIRSTNAME", user.getFirstName());
            templateTokens.put("EMAIL_USER_LASTNAME", user.getLastName());
            templateTokens.put("EMAIL_ADMIN_USERNAME_LABEL", messages.getMessage("label.generic.username", userLocale));
            templateTokens.put("EMAIL_ADMIN_NAME", user.getAdminName());
            templateTokens.put("EMAIL_TEXT_NEW_USER_CREATED", messages.getMessage("email.newuser.text", userLocale));
            templateTokens.put("EMAIL_ADMIN_PASSWORD_LABEL", "");
            templateTokens.put("EMAIL_ADMIN_PASSWORD", "");
            templateTokens.put("EMAIL_ADMIN_URL_LABEL", messages.getMessage("label.adminurl", userLocale));
            templateTokens.put("EMAIL_ADMIN_URL", FilePathUtils.buildAdminUri(store, request));
            templateTokens.put("EMAIL_STORE_NAME", store.getStorename());
			templateTokens.put("EMAIL_FOOTER_COPYRIGHT", "www.bettbio.com");
			templateTokens.put("EMAIL_DISCLAIMER", "");
			templateTokens.put("EMAIL_SPAM_DISCLAIMER", "");
			
            Email email = new Email();
            email.setFrom(store.getStorename());
            email.setFromEmail(store.getStoreEmailAddress());
            email.setSubject(messages.getMessage("email.newuser.title", userLocale));
            List cc = new ArrayList();
            cc.add(store.getStoreEmailAddress());
            email.setTo(user.getAdminEmail());
            email.setTemplateName("email_template_new_user.ftl");
            email.setCc(cc);
            email.setTemplateTokens(templateTokens);
            emailService.sendHtmlEmail(store, email);
        }
        catch(Exception e)
        {
            LOGGER.error("Cannot send email to user", e);
            msg = e.getMessage();
        }

        StringBuilder template = (new StringBuilder()).append("information").append(".").append(store.getStoreTemplate());
        if(msg != null)
            model.addAttribute("information", (new StringBuilder(String.valueOf(user.getAdminEmail()))).append(":").append(msg.toString()).toString());
        else
		   // model.addAttribute("information","鎭枩"+user.getAdminName()+"娉ㄥ唽鎴愬姛,璧犻�绉垎50鍒�娑堟伅宸茬粡鍙戦�鍒颁綘鐨�+user.getAdminEmail()+"閭閲屼簡!");
            model.addAttribute("information", (new StringBuilder("\u606D\u559C")).append(user.getAdminName()).append("\u6CE8\u518C\u6210\u529F,\u6D88\u606F\u5DF2\u7ECF\u53D1\u9001\u5230\u4F60\u7684").append(user.getAdminEmail()).append("\u90AE\u7BB1\u91CC\u4E86!").toString());
		return template.toString();
	}
	

	@SuppressWarnings("deprecation")
	public int sendActiveEmail(User user,MerchantStore store,String contextPath){
		
		try {
			
			
			Language userLanguage = store.getDefaultLanguage();			
			Locale customerLocale = LocaleUtils.getLocale(userLanguage);
			String code = UserReset.generateRandomString();
			String encodedCode = passwordEncoder.encodePassword(code, null);
			user.setAnswer1(encodedCode);
			//send email
			
	
			//active of a user, send an email
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, store, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			String activeUrl = FilePathUtils.buildStoreUrisELF(store,contextPath);
			
			activeUrl = activeUrl +"/admin/setPassword.html?account="+user.getAdminName()+"&code="+encodedCode;
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, user.getAdminName());
	        
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_GREETING, messages.getMessage("email.user.resetpassword.link", customerLocale));
	        templateTokens.put("ACTIVELINK",activeUrl );
	        
	        String[] storeEmail = {store.getStoreEmailAddress()};
	        templateTokens.put("ACTIVECOPY",messages.getMessage("email.customer.activing2", customerLocale) );
	        templateTokens.put("ACTIVEINFO",messages.getMessage("email.customer.activing3", storeEmail,customerLocale) );
	        templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER, messages.getMessage("email.contactowner", storeEmail, customerLocale));

			Email email = new Email();
			email.setFrom(store.getStorename());
			email.setFromEmail(store.getStoreEmailAddress());
			email.setSubject(messages.getMessage("button.label.resetpassword",customerLocale));
			email.setTo(user.getAdminEmail());
			email.setTemplateName(EmailConstants.RESET_ACTIVE_TPL);
			email.setTemplateTokens(templateTokens);			
			emailService.sendHtmlEmail(email);
			//resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			userService.update(user);
			return 0;
		} catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
			return -1;
		}
	}
	
	@RequestMapping(value="/shop/validtion.html",method=RequestMethod.POST)
	public @ResponseBody String valitationByUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		AjaxResponse resp = new AjaxResponse();
		try {
			String param=request.getParameter("param");
			String value=request.getParameter("value");
			resp.setStatus(userService.queryByUser(param,value));			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resp.toJSONString();
	}
	/**
	 * 楠岃瘉鍥剧墖楠岃瘉鐮�
	 * @param imgCode
	 * @param request
	 * @return
	 */
   @RequestMapping("/shop/valitationByImgCode.html")
   @ResponseBody
   public String valitationByImgCode(String imgCode,HttpServletRequest request){
	  AjaxResponse ajaxResponse=new AjaxResponse();
	  imgCode=imgCode.toLowerCase();
	  String serverImgCode=request.getSession().getAttribute("validaioncode").toString().toLowerCase();//鎷垮埌鏈嶅姟鍣ㄩ獙璇佺爜
	  if(!"".equals(serverImgCode)&&!"".equals(imgCode)){//鍒ゆ柇涓よ�閮戒笉涓虹┖
		  if(imgCode.equals(serverImgCode)){//鍒ゆ柇涓よ�鏄惁涓�嚧
			  ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		  }else{
			  ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		  }
	  }
	  return ajaxResponse.toJSONString();
   }
   

   @RequestMapping("/shop/sendSMSCode.html")
   @ResponseBody
   public String sendSMSCode(HttpServletRequest request,String phone){
	     AjaxResponse ajaxResponse=new AjaxResponse();
		try {
			if(!"".equals(phone)&&phone.contains("*")){
					phone=request.getSession().getAttribute("account").toString();
			}
			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(phone, RadomSixNumber.REGIST_TEMP, radomNumber, "15", request);
			if(result){
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				if (!workMode.isProductEnv()){
					ajaxResponse.addEntry("_randomNumber", radomNumber);
				}
			}else{
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		} catch(Exception e) {
			LOGGER.error("An error occured while trying to send an email",e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
	   return ajaxResponse.toJSONString();
   }
   
   @RequestMapping("/shop/sendLogonSMSCode.html")
   @ResponseBody
   public String sendLogonSMSCode(HttpServletRequest request,String phone){
	     AjaxResponse ajaxResponse=new AjaxResponse();
		try {
			if(!"".equals(phone)&&phone.contains("*")){
					phone=request.getSession().getAttribute("phone").toString();
			}
			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(phone, RadomSixNumber.MOBILE_LOGON_TEMP, radomNumber, "15", request);
			if(result){
				request.getSession().setAttribute("sendToMobile", phone);
				request.getSession().setAttribute("smsCodeRetryTimes", 0);
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				if (!workMode.isProductEnv()){
					ajaxResponse.addEntry("_randomNumber", radomNumber);
				}
			}else{
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		} catch(Exception e) {
			LOGGER.error("An error occured while trying to send SMS code for logon",e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
	   return ajaxResponse.toJSONString();
   }
   
   /**
    * 鏍￠獙鎵嬫満楠岃瘉鐮�
    * @return
    */
   @RequestMapping("/shop/valitationBySMSCode.html")
   @ResponseBody
   public String valitationBySMSCode(String smsCode,HttpServletRequest request){
	   AjaxResponse ajaxResponse=new AjaxResponse();
	      smsCode=smsCode.toLowerCase();
		  String phoneCode=request.getSession().getAttribute("phonecode").toString().toLowerCase();//鎷垮埌鏈嶅姟鍣ㄩ獙璇佺爜
		  if(!"".equals(phoneCode)&&!"".equals(smsCode)){//鍒ゆ柇涓よ�閮戒笉涓虹┖
			  if(smsCode.equals(phoneCode)){//鍒ゆ柇涓よ�鏄惁涓�嚧
				  ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			  }else{
				  ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			  }
		  }
	   return ajaxResponse.toJSONString();
   }
   
   /**
    * 鏍￠獙鍟嗗鏄惁瀛樺湪
    * @param index 0鍟嗗閭 1鍟嗗鐢佃瘽 2鍟嗗鐢ㄦ埛鍚�3鍟嗗鍚嶇О 4 閭鎴栨墜鏈�
    * @param account
    * @return
    */
   @RequestMapping("/shop/valitationUser.html")
   @ResponseBody
   public String valitationUser(int index,String account){
	   AjaxResponse ajaxResponse=new AjaxResponse();
	   int count =userFacade.queryByAccount(index, account);
		  if(count==0){//鍒ゆ柇涓よ�鏄惁涓�嚧
			  ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		  }else{
			  ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		  }
	   return ajaxResponse.toJSONString();
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
    	customer.setHidenick(null);
    	customer.setId(0L);
    	customer.setImages(null);
    	customer.setInvoiceaddressdefault(-1);
    	customer.setInvoiceDefault(-1);
    	customer.setLanguage(null);
    	customer.setLogonType(null);
    	customer.setNick(user.getAdminName());
    	customer.setPassword(plainPassword);
    	customer.setPhone(mobile);
    	customer.setPoints(null);
    	customer.setProject(user.getFirstName());
    	customer.setRelationshipTel(null);
    	customer.setStoreCode(null);
    	customer.setUserName(user.getFirstName());
    	
    	MerchantStore merchantStore;
		try {
			merchantStore = merchantStoreService.getByCode(Constants.DEFAULT_STORE);
			CustomerEntity customerData = customerFacade.registerCustomer( customer, merchantStore, language, memberPoints, null);
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
