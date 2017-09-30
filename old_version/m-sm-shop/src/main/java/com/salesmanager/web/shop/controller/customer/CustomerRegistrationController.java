package com.salesmanager.web.shop.controller.customer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.customer.CustomerRegistrationException;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.BaseDataUtils;
import com.salesmanager.core.utils.WorkModeConfiguration;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.user.facade.UserFacade;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.customer.SecuredShopPersistableCustomer;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.RadomSixNumber;

/**
 * Registration of a new customer
 * 
 * @author Carl Samson
 *
 */

// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
@Controller
@RequestMapping("/shop/customer")
public class CustomerRegistrationController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomerRegistrationController.class);

	@Autowired
	private LanguageService languageService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private ZoneService zoneService;

	@Autowired
	EmailService emailService;

	@Autowired
	private LabelUtils messages;

	@Autowired
	private CustomerFacade customerFacade;

	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	GroupService groupService;

	@Autowired
	UserService userService;

	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	WorkModeConfiguration workMode;

	@Autowired
	private UserFacade userFacade;

	@Autowired
	private CustomerService customerService;

	private final static String NEW_USER_TMPL = "email_template_new_user.ftl";

	@RequestMapping(value = "/registration.html", method = RequestMethod.GET)
	public String displayRegistration(final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		MerchantStore store = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		// 不做校验码验证 modify by sam at 20150714
		// model.addAttribute( "recapatcha_public_key",
		// coreConfiguration.getProperty( Constants.RECAPATCHA_PUBLIC_KEY ) );

		SecuredShopPersistableCustomer customer = new SecuredShopPersistableCustomer();
		// modify by cy custmer not banding with billing
		// AnonymousCustomer anonymousCustomer =
		// (AnonymousCustomer)request.getAttribute(Constants.ANONYMOUS_CUSTOMER);
		/**
		 * if(anonymousCustomer!=null) {
		 * customer.setBilling(anonymousCustomer.getBilling()); }
		 */

		model.addAttribute("customer", customer);

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.register)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@RequestMapping(value = "/registrationChoice.html", method = RequestMethod.GET)
	public String displayRegistrationChoice(final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		MerchantStore store = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.reType).append(".")
				.append(store.getStoreTemplate());

		return template.toString();

	}

	@RequestMapping(value = "/storeRegistration.html", method = RequestMethod.GET)
	public String storeRegistrationChoice(final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		MerchantStore store = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		User user = new User();

		model.addAttribute("user", user);

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.storeRegister)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@RequestMapping(value = "/registerStore.html", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult result, Model model, HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws Exception {

		// MerchantStore store =
		// (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		MerchantStore store = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);

		String captchaResponseField = request
				.getParameter("captchaResponseField");
		if (StringUtils.isBlank(captchaResponseField)) {
			ObjectError error = new ObjectError("captchaResponseField",
					messages.getMessage(
							"NotEmpty.contact.captchaResponseField", locale));
			result.addError(error);
		} else {
			HttpSession session = request.getSession();
			if (session.getAttribute("validaioncode") == null
					|| !captchaResponseField.equalsIgnoreCase(session
							.getAttribute("validaioncode").toString())) {
				LOGGER.debug("Captcha response does not matched");
				ObjectError error = new ObjectError("captchaResponseField",
						messages.getMessage("validaion.recaptcha.not.matched",
								locale));
				result.addError(error);
			}
		}
		Language language = (Language) request.getAttribute("LANGUAGE");
		// Language language = user.getDefaultLanguage();
		/**
		 * if(language == null || language.getId()==null){ language =
		 * store.getDefaultLanguage(); } Language l =
		 * languageService.getById(language.getId());
		 */
		user.setDefaultLanguage(language);

		Locale userLocale = LocaleUtils.getLocale(language);

		List<Group> groups = new ArrayList<Group>();
		List<Group> userGroups = groupService.listGroup(GroupType.ADMIN);
		for (Group group : userGroups) {
			if (!group.getGroupName().equals(Constants.GROUP_SUPERADMIN)
					&& !group.getGroupName().equals(Constants.GROUP_ADMIN)) {
				groups.add(group);
			}
		}

		// 短信验证码不正确
		String smsCode = request.getParameter("smsCode");
		String sentCode = (String) request.getSession().getAttribute("phonecode");
		request.getSession().removeAttribute("phonecode");
		if (smsCode == null || sentCode == null || !smsCode.equals(sentCode)){
			ObjectError error = new ObjectError("repeat", "无效的短信验证码");
			result.addError(error);
		}
		
		// 新建名称重复
		User uname = userService.getByUserName(user.getAdminName());
		if (uname != null) {
			ObjectError error = new ObjectError("repeat", messages.getMessage(
					"message.code.exist", locale));
			result.addError(error);

		}

		Set<Integer> ids = new HashSet<Integer>();

		for (Group group : groups) {
			ids.add(group.getId());
		}

		if (user.getAdminPassword().length() < 6) {
			ObjectError error = new ObjectError("adminPassword",
					messages.getMessage("message.password.length", locale));
			result.addError(error);
		}

		if (ids != null && ids.size() > 0) {
			List<Group> newGroups = groupService.listGroupByIds(ids);
			// set actual user groups
			user.setGroups(newGroups);
		}

		// 商铺名称不能重复
		String code = request.getParameter("storename");
		if (StringUtils.isBlank(code)) {
			ObjectError error = new ObjectError("nameRepeat",
					messages.getMessage("label.storename", locale)
							+ messages.getMessage("NotEmpty.generic.name",
									locale));
			result.addError(error);
		} else {
			MerchantStore ustore = merchantStoreService.getByName(code);
			if (ustore != null) {
				ObjectError error = new ObjectError("nameRepeat",
						messages.getMessage("label.storename", locale)
								+ messages.getMessage(
										"label.common.already.exist", locale));
				result.addError(error);

			}

			if (result.hasErrors()) {
				StringBuilder template = new StringBuilder()
						.append(ControllerConstants.Tiles.Customer.storeRegister)
						.append(".").append(store.getStoreTemplate());

				return template.toString();
			}

			// String decodedPassword = user.getAdminPassword();
			String plainPassword = user.getAdminPassword();
			String encoded = passwordEncoder.encodePassword(
					user.getAdminPassword(), null);
			user.setAdminPassword(encoded);

			MerchantStore newStore = new MerchantStore();
			// 设置店铺基本信息
			newStore.setCurrency(store.getCurrency());
			newStore.setCountry(store.getCountry());
			newStore.setZone(store.getZone());
			newStore.setStorestateprovince(store.getStorestateprovince());
			List<Language> supportedLanguagesList = new ArrayList<Language>();
			supportedLanguagesList.add(language);
			newStore.setLanguages(supportedLanguagesList);
			newStore.setDefaultLanguage(language);

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
			newStore.setStoremobile(user.getMobile());
			merchantStoreService.saveOrUpdate(newStore);
			user.setMerchantStore(newStore);
			model.addAttribute("fromRegisterUserAction", true);
			if (user.getId() == null || user.getId().longValue() == 0) {
				// save or update user
				try {
					AuditSection auditSection = new AuditSection();
					auditSection.setAudit(0);
					user.setAuditSection(auditSection);
					userService.saveOrUpdate(user);
					if (createBuyerAutomaticaly(user, plainPassword, language, request)){
	                	model.addAttribute("autoCreateBuyer", true);
	                	model.addAttribute("buyerEmail", user.getAdminEmail());
	                }
				} catch (Exception e) {
					LOGGER.error("faild create user", e);
					merchantStoreService.delete(newStore);
					ObjectError error = new ObjectError("nameRepeat",
							messages.getMessage("message.error", locale));
					result.addError(error);
					StringBuilder template = new StringBuilder()
							.append(ControllerConstants.Tiles.Customer.storeRegister)
							.append(".").append(store.getStoreTemplate());

					return template.toString();
				}

			}

			try {

				// creation of a user, send an email
				String userName = user.getFirstName();
				if (StringUtils.isBlank(userName)) {
					userName = user.getAdminName();
				}
				String[] userNameArg = { userName };

				Map<String, String> templateTokens = EmailUtils
						.createEmailObjectsMap(request.getContextPath(), store,
								messages, userLocale);
				templateTokens.put(EmailConstants.EMAIL_NEW_USER_TEXT, messages
						.getMessage("email.greeting", userNameArg, userLocale));
				templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME,
						user.getFirstName());
				templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME,
						user.getLastName());
				templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL,
						messages.getMessage("label.generic.username",
								userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_NAME,
						user.getAdminName());
				templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_CREATED,
						messages.getMessage("email.newuser.text", userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL,
						"");
				templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD, "");
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL,
						messages.getMessage("label.adminurl", userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL,
						FilePathUtils.buildAdminUri(store, request));

				Email email = new Email();
				email.setFrom(store.getStorename());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.newuser.title",
						userLocale));
				List<String> cc = new ArrayList<String>();
				cc.add(store.getStoreEmailAddress());
				email.setTo(user.getAdminEmail());
				email.setTemplateName(NEW_USER_TMPL);
				email.setCc(cc);
				email.setTemplateTokens(templateTokens);

				emailService.sendHtmlEmail(store, email);

			} catch (Exception e) {
				LOGGER.error("Cannot send email to user", e);
			}

		}
		String message = messages.getMessage("label.bettbio.admin.user.create",
				locale);
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Content.information)
				.append(".").append(store.getStoreTemplate());
		model.addAttribute("information", message.toString());
		return template.toString();
	}

	@RequestMapping(value = "/rfirst.html", method = RequestMethod.POST)
	public String first(
			@Valid @ModelAttribute("customer") SecuredShopPersistableCustomer customer,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, final Locale locale)

	throws Exception {

		MerchantStore merchantStore = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		super.setSessionAttribute("customer", customer, request);
		StringBuilder template = new StringBuilder().append("registerthree")
				.append(".").append(merchantStore.getStoreTemplate());
		return template.toString();
	}

	@RequestMapping(value = "/rsecond.html", method = RequestMethod.POST)
	public String second(
			@Valid @ModelAttribute("customer") SecuredShopPersistableCustomer customer,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, final Locale locale)

	throws Exception {

		MerchantStore merchantStore = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("registersecond")
				.append(".").append(merchantStore.getStoreTemplate());

		// 验证手机校验码
		String checkPhonecode = request.getParameter("checkPhonecode");
		if (StringUtils.isNotBlank(checkPhonecode)) {
			if (request.getSession().getAttribute("phonenum") != null) {
				if (!customer.getPhone().equalsIgnoreCase(
						(String) request.getSession().getAttribute("phonenum"))) {
					/*
					 * FieldError error = new
					 * FieldError("phone","phone",messages
					 * .getMessage("registration.phone.wrong.num", locale));
					 * bindingResult.addError(error);
					 */
					model.addAttribute("msg", messages.getMessage(
							"registration.phone.wrong.num", locale));
					return template.toString();
				}
			}

			if (!checkPhonecode.equalsIgnoreCase((String) request.getSession()
					.getAttribute("phonecode"))) {
				/*
				 * FieldError error = new
				 * FieldError("phonecode","phonecode",messages
				 * .getMessage("label.generic.phone.message.incorrect",
				 * locale)); bindingResult.addError(error);
				 */
				model.addAttribute("msg", messages.getMessage(
						"label.generic.phone.message.incorrect", locale));
				return template.toString();
			}
		} else {
			LOGGER.debug(
					"found {} validation error while validating in customer registration ",
					bindingResult.getErrorCount());

			return template.toString();
		}
		customer.setValiad(true);
		model.addAttribute("customer", customer);
		template = new StringBuilder().append("registerthree").append(".")
				.append(merchantStore.getStoreTemplate());
		return template.toString();
	}

	@RequestMapping(value = "/rthree.html", method = RequestMethod.POST)
	public String three(
			@Valid @ModelAttribute("customer") SecuredShopPersistableCustomer customer,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, final Locale locale) throws Exception {
		MerchantStore merchantStore = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		String password = customer.getPassword();
		String emailAddress = customer.getEmailAddress();
		customer = super.getSessionAttribute("customer", request);
		customer.setPassword(password);
		customer.setEmailAddress(emailAddress);
		/** 2017-3-22: below 4 lines was removed because change the mobile register process to only 2 steps now.
//		super.setSessionAttribute("customer", customer, request);
//		StringBuilder template = new StringBuilder().append("registerfour")
//				.append(".").append(merchantStore.getStoreTemplate());
//		return template.toString();
 * **/
		Language language = super.getLanguage(request);
		SecuredShopPersistableCustomer sessionCustomer=super.getSessionAttribute("customer", request);
		sessionCustomer.setEmailAddress(customer.getEmailAddress());
		sessionCustomer.setUserName(customer.getPhone()); //用户名默认填写为手机号码
		sessionCustomer.setProject(GlobalConstants.UI_FIELD_VALUE_NOT_SET);
		sessionCustomer.setCompany(GlobalConstants.UI_FIELD_VALUE_NOT_SET);
		sessionCustomer.setClearPassword(password);
		sessionCustomer.setPassword(passwordEncoder.encodePassword(password,null));
		@SuppressWarnings("unused")
		CustomerEntity customerData =null;
		try {
			// 注册积分
			String points ="50";
			MemberPoints memberPoints = new MemberPoints();

			memberPoints.setType(Constants.REGIST_SCORE);
			memberPoints.setUpdateDate(new Date());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.YEAR, 1);
			memberPoints.setDateValid(cal.getTime());
			// memberPoints.setCustomer(c);
			memberPoints.setStatas(0);

			memberPoints.setValue(points);

			// set left
			try {
				memberPoints.setLtfePoint(Long.parseLong(points));
			} catch (Exception e) {

			}
			// set user clear password
			customerData = customerFacade.registerCustomer(sessionCustomer,merchantStore, language, memberPoints);
			// 用户注册成功后，清除session中的phonecode
			request.getSession().removeAttribute("phonecode");
			request.getSession().removeAttribute("phonenum");
		} catch (CustomerRegistrationException cre) {
			LOGGER.error("Error while registering customer.. ", cre);
			ObjectError error = new ObjectError("registration",
					messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
			StringBuilder template = new StringBuilder()
					.append(ControllerConstants.Tiles.Customer.register)
					.append(".").append(merchantStore.getStoreTemplate());
			return template.toString();
		} catch (Exception e) {
			LOGGER.error("Error while registering customer.. ", e);
			ObjectError error = new ObjectError("registration",
					messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
			StringBuilder template = new StringBuilder()
					.append(ControllerConstants.Tiles.Customer.register)
					.append(".").append(merchantStore.getStoreTemplate());
			return template.toString();
		}

		/**
		 * Login user
		 */
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Content.information)
				.append(".").append(merchantStore.getStoreTemplate());
		try {

			// refresh customer
			Customer c = customerFacade
					.getCustomerForLogin(sessionCustomer.getPhone());
			// authenticate
			customerFacade.authenticate(c,c.getId().toString(),password);
			super.setSessionAttribute(Constants.CUSTOMER, c, request);
			/**
			 * Send registration email
			 */
			emailTemplatesUtils.sendActiveEmail(c.getId().toString(),
					merchantStore, request.getContextPath());
			String[] paraMessage = { c.getEmailAddress() };
			String smessage = messages.getMessage(
					"label.register.confirmation", paraMessage, locale);
			model.addAttribute("information", smessage.toString());

		} catch (Exception e) {
			LOGGER.error("Cannot authenticate user ", e);
			ObjectError error = new ObjectError("registration",
					messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
		}

		// 积分兑换成功页面的根路径
		/*
		 * StringBuilder template = new StringBuilder().append(
		 * ControllerConstants.Tiles.Customer.register ).append( "." ).append(
		 * merchantStore.getStoreTemplate() ); return template.toString();
		 */
		return template.toString();
	}

	@RequestMapping(value = "/register.html", method = RequestMethod.POST)
	public String registerCustomer(
			@Valid @ModelAttribute("customer") SecuredShopPersistableCustomer customer,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, final Locale locale) throws Exception {
		MerchantStore merchantStore = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		Language language = super.getLanguage(request);

	
		SecuredShopPersistableCustomer sessionCustomer=super.getSessionAttribute("customer", request);
		sessionCustomer.setEmailAddress(customer.getEmailAddress());
		sessionCustomer.setUserName(customer.getUserName());
		sessionCustomer.setProject(customer.getProject());
		sessionCustomer.setCompany(customer.getCompany());
		String password=sessionCustomer.getPassword();
		sessionCustomer.setClearPassword(password);
		sessionCustomer.setPassword(passwordEncoder.encodePassword(password,null));
		@SuppressWarnings("unused")
		CustomerEntity customerData =null;
		try {
			// 注册积分
			String points ="50";
			MemberPoints memberPoints = new MemberPoints();

			memberPoints.setType(Constants.REGIST_SCORE);
			memberPoints.setUpdateDate(new Date());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.YEAR, 1);
			memberPoints.setDateValid(cal.getTime());
			// memberPoints.setCustomer(c);
			memberPoints.setStatas(0);

			memberPoints.setValue(points);

			// set left
			try {
				memberPoints.setLtfePoint(Long.parseLong(points));
			} catch (Exception e) {

			}
			// set user clear password
			customerData = customerFacade.registerCustomer(sessionCustomer,merchantStore, language, memberPoints);
			// 用户注册成功后，清除session中的phonecode
			request.getSession().removeAttribute("phonecode");
			request.getSession().removeAttribute("phonenum");
		} catch (CustomerRegistrationException cre) {
			LOGGER.error("Error while registering customer.. ", cre);
			ObjectError error = new ObjectError("registration",
					messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
			StringBuilder template = new StringBuilder()
					.append(ControllerConstants.Tiles.Customer.register)
					.append(".").append(merchantStore.getStoreTemplate());
			return template.toString();
		} catch (Exception e) {
			LOGGER.error("Error while registering customer.. ", e);
			ObjectError error = new ObjectError("registration",
					messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
			StringBuilder template = new StringBuilder()
					.append(ControllerConstants.Tiles.Customer.register)
					.append(".").append(merchantStore.getStoreTemplate());
			return template.toString();
		}

		/**
		 * Login user
		 */
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Content.information)
				.append(".").append(merchantStore.getStoreTemplate());
		try {

			// refresh customer
			Customer c = customerFacade
					.getCustomerForLogin(sessionCustomer.getPhone());
			// authenticate
			customerFacade.authenticate(c,c.getId().toString(),password);
			super.setSessionAttribute(Constants.CUSTOMER, c, request);
			/**
			 * Send registration email
			 */
			emailTemplatesUtils.sendActiveEmail(c.getId().toString(),
					merchantStore, request.getContextPath());
			String[] paraMessage = { c.getEmailAddress() };
			String smessage = messages.getMessage(
					"label.register.confirmation", paraMessage, locale);
			model.addAttribute("information", smessage.toString());

		} catch (Exception e) {
			LOGGER.error("Cannot authenticate user ", e);
			ObjectError error = new ObjectError("registration",
					messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
		}

		// 积分兑换成功页面的根路径
		/*
		 * StringBuilder template = new StringBuilder().append(
		 * ControllerConstants.Tiles.Customer.register ).append( "." ).append(
		 * merchantStore.getStoreTemplate() ); return template.toString();
		 */
		return template.toString();
	}

	@SuppressWarnings("unchecked")
	private String getPoint(String type) {
		return String.valueOf(BaseDataUtils.addCoustomerByOrader(type));
	}

	@ModelAttribute("countryList")
	public List<Country> getCountries(final HttpServletRequest request) {

		Language language = (Language) request.getAttribute("LANGUAGE");
		try {
			if (language == null) {
				language = (Language) request.getAttribute("LANGUAGE");
			}

			if (language == null) {
				language = languageService
						.getByCode(Constants.DEFAULT_LANGUAGE);
			}

			List<Country> countryList = countryService.getCountries(language);
			return countryList;
		} catch (ServiceException e) {
			LOGGER.error("Error while fetching country list ", e);

		}
		return Collections.emptyList();
	}

	@ModelAttribute("zoneList")
	public List<Zone> getZones(final HttpServletRequest request) {
		return zoneService.list();
	}

	@RequestMapping(value = "/protocol.html", method = RequestMethod.GET)
	public String displayProtocol(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		MerchantStore store = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("userProtocol")
				.append(".").append(store.getStoreTemplate());
		return template.toString();

		// return "redirect:" + "/userProtocol";
	}

	@RequestMapping(value = "/checkStoreCode.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String checkStoreCode(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");

		AjaxResponse resp = new AjaxResponse();

		try {

			if (StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
				return resp.toJSONString();
			}

			MerchantStore store = merchantStoreService.getByName(code);

			if (store != null) {
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

	@RequestMapping(value = "/checkUserCode.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String checkUserCode(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();

		try {

			if (StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
				return resp.toJSONString();
			}

			User user = userService.getByUserName(code);

			if (!StringUtils.isBlank(id) && user != null) {
				try {
					Long lid = Long.parseLong(id);

					if (user.getAdminName().equals(code) && user.getId() == lid) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						return resp.toJSONString();
					}
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					return resp.toJSONString();
				}

			}

			if (StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}

			if (user != null) {
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

	/**
	 * 校验手机号
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping("/checkPhone.html")
	public @ResponseBody String checkPhone(Customer customer) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		try {
			if (!customerFacade.getCustomerExistsPhone(customer.getPhone())) {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			} else {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return ajaxResponse.toJSONString();
	}

	/**
	 * 校验验证码
	 * 
	 * @param imgCode
	 * @param request
	 * @return
	 */
	@RequestMapping("/valitationByImgCode.html")
	@ResponseBody
	public String valitationByImgCode(String imgCode, HttpServletRequest request) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		imgCode = imgCode.toLowerCase();
		String serverImgCode = request.getSession()
				.getAttribute("validaioncode").toString().toLowerCase();
		if (!"".equals(serverImgCode) && !"".equals(imgCode)) {
			if (imgCode.equals(serverImgCode)) {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			} else {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		return ajaxResponse.toJSONString();
	}

	/**
	 * 校验手机验证码
	 * 
	 * @param request
	 * @param phone
	 * @return
	 */
	@RequestMapping("/valitationBySMSCode.html")
	@ResponseBody
	public String valitationBySMSCode(String smsCode,String phone, HttpServletRequest request) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		smsCode = smsCode.toLowerCase();
		//Object phoneCode = request.getSession().getAttribute("phonecode"+smsCode);
		Object phoneCode = request.getSession().getAttribute("phonecode");
		if(phoneCode!=null){
			phoneCode=phoneCode.toString().toLowerCase();
		}
		if (!"".equals(phoneCode) && !"".equals(smsCode)) {
			if (smsCode.equals(phoneCode)) {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			} else {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		return ajaxResponse.toJSONString();
	}

	@RequestMapping("/valitationUser.html")
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
	/**
	 * 发送手机验证码
	 * 
	 * @param request
	 * @param phone
	 * @return
	 */
	@RequestMapping("/sendSMSCode.html")
	@ResponseBody
	public String sendSMSCode(HttpServletRequest request, String phone) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		try {
			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(phone,
					RadomSixNumber.REGIST_TEMP, radomNumber, "15", request);
			if (result) {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				if (!workMode.isProductEnv()){
					ajaxResponse.addEntry("_randomNumber", radomNumber);
				}
			} else {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		} catch (Exception e) {
			LOGGER.error("An error occured while trying to send an email", e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return ajaxResponse.toJSONString();
	}

	@RequestMapping("/sendLogonSMSCode.html")
	@ResponseBody
	public String sendLogonSMSCode(HttpServletRequest request, String phone) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		try {
			if (!"".equals(phone) && phone.contains("*")) {
				phone = request.getSession().getAttribute("phone").toString();
			}
			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(phone, RadomSixNumber.MOBILE_LOGON_TEMP, radomNumber, "15",
					request);
			if (result) {
				request.getSession().setAttribute("sendToMobile", phone);
				request.getSession().setAttribute("smsCodeRetryTimes", 0);
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				if (!workMode.isProductEnv()) {
					ajaxResponse.addEntry("_randomNumber", radomNumber);
				}
			} else {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		} catch (Exception e) {
			LOGGER.error("An error occured while trying to send SMS code for logon", e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return ajaxResponse.toJSONString();
	}
	
	/**
	 * 校验邮箱或手机号
	 * 
	 * @return
	 */
	@RequestMapping("/valitationEmailOrPhone.html")
	@ResponseBody
	public String valitationEmailOrPhone(String mail, String phone) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		try {
			if (!customerFacade.checkIfUserExists(mail, phone)) {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			} else {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
