package com.salesmanager.web.admin.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.service.CountryService;
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
import com.salesmanager.core.utils.VerificationUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.controller.user.facade.UserFacade;
import com.salesmanager.web.admin.entity.secutity.Password;
import com.salesmanager.web.admin.entity.user.UserList;
import com.salesmanager.web.admin.entity.userpassword.UserReset;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.DeleteAll;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.PageBuilderUtils;
import com.salesmanager.web.utils.PinyinUtils;
import com.salesmanager.web.utils.UserUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

@Controller
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	UserService userService;

	@Autowired
	GroupService groupService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	UserFacade userFacade;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final static String QUESTION_1 = "question1";
	private final static String QUESTION_2 = "question2";
	private final static String QUESTION_3 = "question3";
	private final static String RESET_PASSWORD_TPL = "email_template_password_reset_user.ftl";	
	private final static String NEW_USER_TMPL = "email_template_new_user.ftl";
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/users/list.html", method={RequestMethod.GET,RequestMethod.POST})
	public String displayUsers(@Valid @ModelAttribute("criteria") Criteria criteria,Model model, HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestParam(value = "page", defaultValue = "1") final int page) throws Exception {

		//The users are retrieved from the paging method
		PaginationData paginaionData=createPaginaionData(page,15);
		if(criteria == null) criteria = new Criteria();
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		UserList ulist = userFacade.getByCriteria(criteria);
		model.addAttribute( "users", ulist);
		if(ulist!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, ulist.getTotalCount()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		model.addAttribute( "criteria", criteria);
		setMenu(model,request,"users");
		return ControllerConstants.Tiles.User.users;
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/users/available.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String available(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("id");
		String active = request.getParameter("active");
		

		AjaxResponse resp = new AjaxResponse();
		if(!StringUtils.isBlank(values) && !StringUtils.isBlank(active)) {
			
			try {
				
				Long id = Long.parseLong(values);
				User user = userService.getById(id);
				user.setActive(Boolean.parseBoolean(active));
				userService.saveOrUpdate(user);
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
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/password.html", method=RequestMethod.GET)
	public String displayChangePassword(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request,"change-password");
		String userName = request.getRemoteUser();
		User user = userService.getByUserName(userName);
		
		Password password = new Password();
		password.setUser(user);
		
		model.addAttribute("password",password);
		model.addAttribute("user",user);
		return ControllerConstants.Tiles.User.password;
	}
	
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/savePassword.html", method=RequestMethod.POST)
	public String changePassword(@ModelAttribute("password") Password password, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request,"change-password");
		String userName = request.getRemoteUser();
		User dbUser = userService.getByUserName(userName);
		

		if(password.getUser().getId().longValue()!= dbUser.getId().longValue()) {
				return "redirect:/admin/users/displayUser.html";
		}
		
		//validate password not empty
		if(StringUtils.isBlank(password.getPassword())) {
			ObjectError error = new ObjectError("password",new StringBuilder().append(messages.getMessage("label.generic.password", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
		}
		

		String tempPass = passwordEncoder.encodePassword(password.getPassword(), null);
		
		//password match
		if(!tempPass.equals(dbUser.getAdminPassword())) {
			ObjectError error = new ObjectError("password",messages.getMessage("message.password.invalid", locale));
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
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
		//设置用户已经更改密码了
		auditSection.setAudit(0);
		
		String pass = passwordEncoder.encodePassword(password.getNewPassword(), null);
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);
		
		model.addAttribute("success","success");
		return ControllerConstants.Tiles.User.password;
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/users/createUser.html", method=RequestMethod.GET)
	public String displayUserCreate(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return displayUser(null,model,request,response,locale);
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = merchantStoreService.getStoreName();
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

	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/admin/users/removes.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeUsers(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		DeleteAll delteAll = new DeleteAll();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		try{
			
			delteAll.setService(userService);
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
	/**
	 * From user list
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/displayStoreUser.html", method=RequestMethod.GET)
	public String displayStoreUser(@ModelAttribute("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		User dbUser = userService.getById(id);
		
		if(dbUser==null) {
			LOGGER.info("User is null for id " + id);
			return "redirect://admin/users/list.html";
		}
		
		
		return displayUser(dbUser,model,request,response,locale);

	}
	
	/**
	 * From user profile
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/displayUser.html", method=RequestMethod.GET)
	public String displayUserEdit(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		String userName = request.getRemoteUser();
		User user = userService.getByUserName(userName);
		return displayUser(user,model,request,response,locale);

	}
	
	private void populateUserObjects(User user, MerchantStore store, Model model, Locale locale) throws Exception {
		
		//get groups
		List<Group> groups = new ArrayList<Group>();
		List<Group> userGroups = groupService.listGroup(GroupType.ADMIN);
		for(Group group : userGroups) {
			if(!group.getGroupName().equals(Constants.GROUP_SUPERADMIN)) {
				groups.add(group);
			}
		}
		
		model.addAttribute("languages", store.getLanguages());
		model.addAttribute("groups", groups);
		
		
	}
	
	
	
	private String displayUser(User user, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

		//display menu
		if(user ==null){
			setMenu(model,request,"create-user");
		}else{
			setMenu(model,request,"myprofile");
		}
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);



		
		if(user==null) {
			user = new User();
		} else {
			user.setAdminPassword("TRANSIENT");
		}
		
		this.populateUserObjects(user, store, model, locale);
		

		model.addAttribute("user", user);

		return ControllerConstants.Tiles.User.profile;
	}
	
	
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/checkUserCode.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String checkUserCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();
		
		try {
			
			if(StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}
			
			User user = userService.getByUserName(code);
		
		
			if(!StringUtils.isBlank(id)&& user!=null) {
				try {
					Long lid = Long.parseLong(id);
					
					if(user.getAdminName().equals(code) && user.getId()==lid) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						return resp.toJSONString();
					}
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					return resp.toJSONString();
				}
	
			}

			
			if(StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}

			if(user!=null) {
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
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/save.html", method=RequestMethod.POST)
	public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		setMenu(model,request,"myprofile");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		
		this.populateUserObjects(user, store, model, locale);
		
		Language language = user.getDefaultLanguage();
		
		if(language == null || language.getId()==null){
			language = store.getDefaultLanguage();
		}
		Language l = languageService.getById(language.getId());
		
		user.setDefaultLanguage(l);
		
		Locale userLocale = LocaleUtils.getLocale(l);
		
		
		
		User dbUser = null;
		
		//edit mode, need to get original user important information
		if(user.getId()!=null) {
			dbUser = userService.getByUserName(user.getAdminName());
			//名称重复
			if(dbUser !=null && (!dbUser.getId().equals(user.getId()) )){
				ObjectError error = new ObjectError("repeat",messages.getMessage("message.code.exist", locale));
				result.addError(error);
			}else{
				dbUser =userService.getById(user.getId());
			}
		}else{
			//新建名称重复
			User uname = userService.getByUserName(user.getAdminName());
			if(uname !=null){
				ObjectError error = new ObjectError("repeat",messages.getMessage("message.code.exist", locale));
				result.addError(error);
			}
		}
		
		// mobile cannot be empty
		if (user.getMobile()==null || user.getMobile().trim().isEmpty() || !VerificationUtils.isMobileNumber(user.getMobile())){
			ObjectError error = new ObjectError("mobile",messages.getMessage("messages.invalid.phone", locale));
			result.addError(error);
		}else if(user.getId()!=null && user.getId() > 0) {
			//check mobile repeated or not
			if(dbUser == null) {
				dbUser = userService.getById(user.getId());
			}
			User existedUser = userService.getByUserMobile(user.getMobile());
			if (existedUser != null && !existedUser.getId().equals(user.getId())){
				// 手机重复
				ObjectError error = new ObjectError("mobile",messages.getMessage("message.phone.exist", locale));
				result.addError(error);
			}
		}else{
			//新建手机重复
			User uname = userService.getByUserMobile(user.getMobile());
			if(uname !=null){
				ObjectError error = new ObjectError("mobile",messages.getMessage("message.phone.exist", locale));
				result.addError(error);
			}
		}

		List<Group> submitedGroups = user.getGroups();
		
		//角色组处理，Admin用户才允许修改角色
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<Integer> ids = new HashSet<Integer>();
		if(auth != null && request.isUserInRole("ADMIN")){
			if (submitedGroups!=null) {
				for(Group group : submitedGroups) {
					ids.add(Integer.parseInt(group.getGroupName()));
				}
			} else {
				ObjectError error = new ObjectError("no groups",messages.getMessage("NotEmpty.user.group", locale));
				result.addError(error);
			}
		} else {//不是admin管理员，则保存默认的用户权限
			List<Group> groups = dbUser.getGroups();
			for(Group group : groups) {
				ids.add(group.getId());
			}
		}
		
		
		Group superAdmin = null;
		
		if(user.getId()!=null && user.getId()>0) {			
			
			List<Group> groups = dbUser.getGroups();
			//boolean removeSuperAdmin = true;
			for(Group group : groups) {
				//can't revoke super admin
				if(group.getGroupName().equals("SUPERADMIN")) {
					superAdmin = group;
				}
			}

		} else {
			
			if(user.getAdminPassword().length()<6) {
				ObjectError error = new ObjectError("adminPassword",messages.getMessage("message.password.length", locale));
				result.addError(error);
			}
			
		}
		
		if(superAdmin!=null) {
			ids.add(superAdmin.getId());
		}

		if (ids!=null && ids.size()>0) {
			List<Group> newGroups = groupService.listGroupByIds(ids);
			//set actual user groups
			user.setGroups(newGroups);
		}
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.profile;
		}
		
		String decodedPassword = user.getAdminPassword();
		if(user.getId()!=null && user.getId()>0) {
			user.setAdminPassword(dbUser.getAdminPassword());
		} else {
			String encoded = passwordEncoder.encodePassword(user.getAdminPassword(),null);
			user.setAdminPassword(encoded);
		}
		
		MerchantStore ustore = user.getMerchantStore();
		ustore = merchantStoreService.getById(ustore.getId());
		user.setMerchantStore(ustore);
		if(user.getId()==null || user.getId().longValue()==0) {
			//save or update user
			userService.saveOrUpdate(user);
			
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
				templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD, decodedPassword);
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, FilePathUtils.buildAdminUri(store, request));
	
				
				Email email = new Email();
				email.setFrom(store.getStorename());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.newuser.title",userLocale));
				email.setTo(user.getAdminEmail());
				
				email.setTemplateName(NEW_USER_TMPL);
				email.setTemplateTokens(templateTokens);
	
				emailService.sendHtmlEmail(store, email);
			
			} catch (Exception e) {
				LOGGER.error("Cannot send email to user",e);
			}
			
		} else {
			
			//save or update user
			userService.saveOrUpdate(user);
		}
		
		model.addAttribute("success","success");
		return ControllerConstants.Tiles.User.profile;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/users/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeUser(HttpServletRequest request, Locale locale) throws Exception {
		
		//do not remove super admin
		
		String sUserId = request.getParameter("entity");

		AjaxResponse resp = new AjaxResponse();
		
		String userName = request.getRemoteUser();
		User remoteUser = userService.getByUserName(userName);

		
		try {
			
			Long userId = Long.parseLong(sUserId);
			User user = userService.getById(userId);
			
			/**
			 * In order to remove a User the logged in ser must be STORE_ADMIN
			 * or SUPER_USER
			 */
			

			if(user==null){
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			
			if(!request.isUserInRole(Constants.GROUP_ADMIN)) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}

			
			//check if the user removed has group ADMIN
			boolean isAdmin = false;
			if(UserUtils.userInGroup(remoteUser, Constants.GROUP_ADMIN) || UserUtils.userInGroup(remoteUser, Constants.GROUP_SUPERADMIN)) {
				isAdmin = true;
			}

			
			if(!isAdmin) {
				resp.setStatusMessage(messages.getMessage("message.security.caanotremovesuperadmin", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			
			userService.delete(user);
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
	}
	
	
	private void setMenu(Model model, HttpServletRequest request,String activeMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("profile", "profile");
		activeMenus.put(activeMenu, activeMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("profile");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	//password reset functionality  ---  Sajid Shajahan  
	@RequestMapping(value="/admin/users/resetPassword.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String resetPassword(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		AjaxResponse resp = new AjaxResponse();
		String userName = request.getParameter("username");
		
		
		
		/**
		 * Get User with userService.getByUserName
		 * Get 3 security questions from User.getQuestion1, user.getQuestion2, user.getQuestion3
		 */
		
		HttpSession session = request.getSession();
		session.setAttribute("username_reset", userName);
		
		try {
				if(!StringUtils.isBlank(userName)){
					
						User dbUser = userService.getByUserName(userName);
						
						if(dbUser==null) {
							resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
							resp.setStatusMessage(messages.getMessage("message.username.notfound", locale));
							return resp.toJSONString();
						}
					
						Map<String,String> entry = new HashMap<String,String>();
						entry.put(QUESTION_1, dbUser.getQuestion1());
						entry.put(QUESTION_2, dbUser.getQuestion2());
						entry.put(QUESTION_3, dbUser.getQuestion3());
						resp.addDataEntry(entry);
						resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
				}else
				{
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						resp.setStatusMessage(messages.getMessage("User.resetPassword.Error", locale));
				
				}
			} catch (Exception e) {
						e.printStackTrace();
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						resp.setStatusMessage(messages.getMessage("User.resetPassword.Error", locale));
						return resp.toJSONString();
			}
	
		
		
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	//password reset functionality  ---  Sajid Shajahan
	@RequestMapping(value="/admin/users/resetPasswordSecurityQtn.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String resetPasswordSecurityQtn(@ModelAttribute(value="userReset") UserReset userReset,HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language userLanguage = null; 
		Locale userLocale =  null; 
		AjaxResponse resp = new AjaxResponse();
		
		//String question1 = request.getParameter("question1");
		//String question2 = request.getParameter("question2");
		//String question3 = request.getParameter("question3");

		String answer1 = request.getParameter("answer1");
		String answer2 = request.getParameter("answer2");
		String answer3 = request.getParameter("answer3");
		
		try {
			
			HttpSession session = request.getSession();
			User dbUser = userService.getByUserName((String) session.getAttribute("username_reset"));
			
			if(dbUser!= null){
				
				if(dbUser.getAnswer1().equals(answer1.trim()) && dbUser.getAnswer2().equals(answer2.trim()) && dbUser.getAnswer3().equals(answer3.trim())){
					userLanguage = dbUser.getDefaultLanguage();	
					userLocale =  LocaleUtils.getLocale(userLanguage);
					
					String tempPass = userReset.generateRandomString();
					String pass = passwordEncoder.encodePassword(tempPass, null);
					
					dbUser.setAdminPassword(pass);
					userService.update(dbUser);
					
					//send email
					
					try {
						String[] storeEmail = {store.getStoreEmailAddress()};						
						
						Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, userLocale);
						templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT, messages.getMessage("email.user.resetpassword.text", userLocale));
						templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER, messages.getMessage("email.contactowner", storeEmail, userLocale));
						templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",userLocale));
						templateTokens.put(EmailConstants.EMAIL_USER_PASSWORD, tempPass);

						Email email = new Email();
						email.setFrom(store.getStorename());
						email.setFromEmail(store.getStoreEmailAddress());
						email.setSubject(messages.getMessage("label.generic.changepassword",userLocale));
						email.setTo(dbUser.getAdminEmail() );
						email.setTemplateName(RESET_PASSWORD_TPL);
						email.setTemplateTokens(templateTokens);
						
						emailService.sendHtmlEmail(store, email);
					
					} catch (Exception e) {
						LOGGER.error("Cannot send email to user",e);
					}
					
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resp.setStatusMessage(messages.getMessage("User.resetPassword.resetSuccess", locale));
				}
				else{
					  resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					  resp.setStatusMessage(messages.getMessage("User.resetPassword.wrongSecurityQtn", locale));
					  
				  }
			  }else{
				  resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				  resp.setStatusMessage(messages.getMessage("User.resetPassword.userNotFound", locale));
				  
			  }
			
		} catch (ServiceException e) {
			e.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(messages.getMessage("User.resetPassword.Error", locale));
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	}
