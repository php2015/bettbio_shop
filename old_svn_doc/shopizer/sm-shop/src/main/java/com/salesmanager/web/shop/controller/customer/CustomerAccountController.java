package com.salesmanager.web.shop.controller.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.customer.model.attribute.CustomerOptionType;
import com.salesmanager.core.business.customer.service.CustomerAddressService;
import com.salesmanager.core.business.customer.service.CustomerInvoiceService;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.attribute.CustomerAttributeService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionSetService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.userpassword.UserReset;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.CustomerAdress;
import com.salesmanager.web.entity.customer.CustomerPassword;
import com.salesmanager.web.entity.customer.DefautAddressInvoice;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerAddressFacade;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.RadomSixNumber;

@SuppressWarnings("deprecation")
/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerAccountController extends AbstractController {

	private static final String CUSTOMER_ID_PARAMETER = "customer";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomerAccountController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerOptionService customerOptionService;

	@Autowired
	private CustomerOptionValueService customerOptionValueService;

	@Autowired
	private CustomerOptionSetService customerOptionSetService;

	@Autowired
	private CustomerAttributeService customerAttributeService;

	@Autowired
	private LanguageService languageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CountryService countryService;

	@Autowired
	private CustomerAddressService customerAddressService;

	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;

	@Autowired
	private ZoneService zoneService;

	@Autowired
	private CustomerFacade customerFacade;

	@Autowired
	private CustomerAddressFacade customerAddressFacade;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private LabelUtils messages;

	@Autowired
	private CustomerInvoiceService customerInvoiceService;

	/**
	 * Dedicated customer logon page
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customLogon.html", method = RequestMethod.GET)
	public String displayLogon(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		// dispatch to dedicated customer logon

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.customerLogon)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * Dedicated customer logon page
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetPassword.html", method = RequestMethod.GET)
	public String resetPassword(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		/** template **/
		StringBuilder template = new StringBuilder().append("resetPassword")
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@RequestMapping(value = "/resetPass.html", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String verifyAccout(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);
		Language language = getSessionAttribute(Constants.LANGUAGE, request);

		Locale locale = LocaleUtils.getLocale(language);

		String account = request.getParameter("signin_userName");

		if (account == null || StringUtils.isBlank(account)) {
			model.addAttribute("erroInfo",
					messages.getMessage("NotEmpty.custmoer.account", locale));
			StringBuilder template = new StringBuilder()
					.append("resetPassword").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}

		String vrifycode = request.getParameter("captchaResponseField");
		if (vrifycode == null || StringUtils.isBlank(vrifycode)) {
			model.addAttribute("erroInfo",
					messages.getMessage("NotEmpty.store.validatecode", locale));
			StringBuilder template = new StringBuilder()
					.append("resetPassword").append(".")
					.append(store.getStoreTemplate());
			return template.toString();

		} else {
			HttpSession session = request.getSession();
			if (session.getAttribute("validaioncode") == null
					|| !vrifycode.equalsIgnoreCase(session.getAttribute(
							"validaioncode").toString())) {
				model.addAttribute("erroInfo", messages.getMessage(
						"validaion.recaptcha.not.matched", locale));
				StringBuilder template = new StringBuilder()
						.append("resetPassword").append(".")
						.append(store.getStoreTemplate());
				return template.toString();
			}
		}

		Customer customer = customerFacade.getCustomerForLogin(account);
		if (customer == null) {
			model.addAttribute("erroInfo",
					messages.getMessage("message.notexist.customer", locale));
			StringBuilder template = new StringBuilder()
					.append("resetPassword").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}
		String phone = customer.getPhone();
		// model.addAttribute("phonenum", phone);
		phone = phone.replaceAll(phone.substring(3, 8), "****");

		model.addAttribute("nick", customer.getNick());
		model.addAttribute("phone", phone);
		HttpSession session = request.getSession();
		session.setAttribute("account", customer.getPhone());
		/** template **/
		model.addAttribute("erroInfo", null);
		StringBuilder template = new StringBuilder().append("verifyCustmoer")
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * Dedicated customer logon page
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verifyPhone.html", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String verifyCustmoer(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		Locale locale = LocaleUtils.getLocale(language);
		HttpSession session = request.getSession();
		if (session.getAttribute("account") == null
				|| session.getAttribute("phonecode") == null) {
			model.addAttribute("erroInfo",
					messages.getMessage("message.timeout", locale));
			StringBuilder template = new StringBuilder()
					.append("verifyCustmoer").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}
		Customer customer = customerFacade.getCustomerForLogin(session
				.getAttribute("account").toString());

		String vrifycode = request.getParameter("phonecode");
		if (vrifycode == null || StringUtils.isBlank(vrifycode)) {
			model.addAttribute("erroInfo",
					messages.getMessage("NotEmpty.store.validatecode", locale));
			StringBuilder template = new StringBuilder()
					.append("verifyCustmoer").append(".")
					.append(store.getStoreTemplate());
			String phone = customer.getPhone();
			phone = phone.replaceAll(phone.substring(3, 8), "****");

			model.addAttribute("nick", customer.getNick());
			model.addAttribute("phone", phone);
			return template.toString();

		} else {
			if (!vrifycode.equalsIgnoreCase(session.getAttribute("phonecode")
					.toString())) {
				model.addAttribute("erroInfo", messages.getMessage(
						"validaion.recaptcha.not.matched", locale));
				StringBuilder template = new StringBuilder()
						.append("verifyCustmoer").append(".")
						.append(store.getStoreTemplate());
				String phone = customer.getPhone();
				phone = phone.replaceAll(phone.substring(3, 8), "****");

				model.addAttribute("nick", customer.getNick());
				model.addAttribute("phone", phone);
				return template.toString();
			}
		}

		String code = UserReset.generateRandomString();

		customer.setRecievePhoneTime(code);
		session.setAttribute("passCode", code);
		customerService.saveOrUpdate(customer);
		// dispatch to dedicated customer logon

		/** template **/
		StringBuilder template = new StringBuilder()
				.append("newPasswordCustmoer").append(".")
				.append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * Dedicated customer logon page
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/newPassword.html", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String newPassword(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		// dispatch to dedicated customer logon

		/** template **/
		StringBuilder template = new StringBuilder()
				.append("newPasswordCustmoer").append(".")
				.append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * Dedicated customer logon page
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/donePassword.html", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String donePassword(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		Locale locale = LocaleUtils.getLocale(language);

		String passwords = request.getParameter("passwords");
		if (passwords == null || StringUtils.isBlank(passwords)) {
			model.addAttribute("erroInfo",
					messages.getMessage("newpassword.not.empty", locale));
			StringBuilder template = new StringBuilder()
					.append("newPasswordCustmoer").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}

		String reg = "^\\s*[^\\s\u4e00-\u9fa5]{6,16}\\s*$";
		if (!passwords.matches(reg)) {
			model.addAttribute("erroInfo",
					messages.getMessage("newpassword.not.week", locale));
			StringBuilder template = new StringBuilder()
					.append("newPasswordCustmoer").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}

		String checkPasswords = request.getParameter("checkPassword");
		if (checkPasswords == null || StringUtils.isBlank(checkPasswords)) {
			model.addAttribute("erroInfo",
					messages.getMessage("repeatpassword.not.empty", locale));
			StringBuilder template = new StringBuilder()
					.append("newPasswordCustmoer").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}

		if (!passwords.equalsIgnoreCase(checkPasswords)) {
			model.addAttribute("erroInfo",
					messages.getMessage("password.notequal", locale));
			StringBuilder template = new StringBuilder()
					.append("newPasswordCustmoer").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}

		HttpSession session = request.getSession();
		if (session.getAttribute("account") == null
				|| session.getAttribute("passCode") == null) {
			model.addAttribute("erroInfo",
					messages.getMessage("message.timeout", locale));
			StringBuilder template = new StringBuilder()
					.append("newPasswordCustmoer").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}
		Customer customer = customerFacade.getCustomerForLogin(session
				.getAttribute("account").toString());
		String passCode = session.getAttribute("passCode").toString();

		if (!passCode.equalsIgnoreCase(customer.getRecievePhoneTime())) {
			model.addAttribute("erroInfo",
					messages.getMessage("message.timeout", locale));
			StringBuilder template = new StringBuilder()
					.append("newPasswordCustmoer").append(".")
					.append(store.getStoreTemplate());
			return template.toString();
		}

		session.removeAttribute("account");
		session.removeAttribute("passCode");

		String encodedPassword = passwordEncoder
				.encodePassword(passwords, null);

		customer.setPassword(encodedPassword);

		customerService.saveOrUpdate(customer);
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		emailTemplatesUtils.changePasswordNotificationEmail(customer, store,
				LocaleUtils.getLocale(customer.getDefaultLanguage()),
				request.getContextPath());
		// dispatch to dedicated customer logon

		/** template **/
		StringBuilder template = new StringBuilder()
				.append("getPasswordCustmoer").append(".")
				.append(store.getStoreTemplate());

		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/account.html", method = RequestMethod.GET)
	public String displayCustomerAccount(Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.customer)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/password.html", method = RequestMethod.GET)
	public String displayCustomerChangePassword(Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		CustomerPassword customerPassword = new CustomerPassword();
		model.addAttribute("password", customerPassword);
		model.addAttribute("activeMenu", "safetyCenter");

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.changePassword)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * 进入邮箱修改页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/email.html", method = RequestMethod.GET)
	public String displayCustomerChangeEmail(Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		model.addAttribute("email", ((Customer) request
				.getAttribute(Constants.CUSTOMER)).getEmailAddress());
		model.addAttribute("activeMenu", "safetyCenter");

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.changeEmail)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * 进入手机号码修改页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/phone.html", method = RequestMethod.GET)
	public String displayCustomerChangePhone(Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		model.addAttribute("phone", ((Customer) request
				.getAttribute(Constants.CUSTOMER)).getPhone());
		model.addAttribute("activeMenu", "safetyCenter");

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.changePhone)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * 访问账户安全中心
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/safetyCenter.html", method = RequestMethod.GET)
	public String displaySafetyCenter(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		CustomerPassword customerPassword = new CustomerPassword();
		model.addAttribute("password", customerPassword);
		model.addAttribute("activeMenu", "safetyCenter");

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.safetyCenter)
				.append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/changePassword.html", method = RequestMethod.POST)
	public String changePassword(
			@Valid @ModelAttribute(value = "password") CustomerPassword password,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.changePassword)
				.append(".").append(store.getStoreTemplate());

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}

		String currentPassword = password.getCurrentPassword();
		String encodedCurrentPassword = passwordEncoder.encodePassword(
				currentPassword, null);

		if (!StringUtils.equals(encodedCurrentPassword, customer.getPassword())) {
			FieldError error = new FieldError("password", "password",
					messages.getMessage("message.invalidpassword", locale));
			bindingResult.addError(error);
		}
		// 判断新输入的密码强度
		String reg = "^\\s*[^\\s\u4e00-\u9fa5]{6,16}\\s*$";
		if (!password.getPassword().matches(reg)) {
			FieldError error = new FieldError("password", "password",
					messages.getMessage("newpassword.not.week", locale));
			bindingResult.addError(error);
		}
		// 验证手机校验码 phonevalidate
		String checkPhonecode = request.getParameter("phonevalidate");
		if (StringUtils.isNotBlank(checkPhonecode)) {
			if (!checkPhonecode.equalsIgnoreCase((String) request.getSession()
					.getAttribute("phonecode"))) {
				FieldError error = new FieldError(
						"phone",
						"phone",
						messages.getMessage(
								"label.generic.phone.message.incorrect", locale));
				bindingResult.addError(error);
			}
		} else {
			FieldError error = new FieldError("phone", "phone",
					messages.getMessage("NotEmpty.store.validatecode", locale));
			bindingResult.addError(error);
		}

		if (bindingResult.hasErrors()) {
			LOGGER.info(
					"found {} validation error while validating customer password",
					bindingResult.getErrorCount());
			return template.toString();

		}

		CustomerPassword customerPassword = new CustomerPassword();
		model.addAttribute("password", customerPassword);

		String newPassword = password.getPassword();
		String encodedPassword = passwordEncoder.encodePassword(newPassword,
				null);

		customer.setPassword(encodedPassword);

		customerService.saveOrUpdate(customer);
		// 密码修改成功后清空session中的手机校验码信息
		request.getSession().removeAttribute("phonecode");
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		emailTemplatesUtils.changePasswordNotificationEmail(customer, store,
				LocaleUtils.getLocale(customer.getDefaultLanguage()),
				request.getContextPath());

		model.addAttribute("success", "success");
		model.addAttribute("activeMenu", "safetyCenter");
		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/changeEmail.html", method = RequestMethod.POST)
	public String changeEmail(@Valid @RequestParam("newEmail") String newEmail,
			Model model, HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);
		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.changeEmail)
				.append(".").append(store.getStoreTemplate());
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());
		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}
		List<String> errors = new ArrayList<String>();
		// 验证邮箱地址是否注册过
		if (StringUtils.isNotBlank(newEmail)) {
			if (customerService.getByEmail(newEmail) != null) {
				LOGGER.debug(
						"Customer with username {0} already exists for this store ",
						newEmail);
				errors.add(messages.getMessage(
						"registration.email.already.exists", locale));
			}
		} else {
			errors.add(messages.getMessage("NotEmpty.store.validatecode",
					locale));
		}
		// 验证手机校验码 phonevalidate
		String checkPhonecode = request.getParameter("phonevalidate");
		if (StringUtils.isNotBlank(checkPhonecode)) {
			if (!checkPhonecode.equalsIgnoreCase((String) request.getSession()
					.getAttribute("phonecode"))) {
				errors.add(messages.getMessage(
						"label.generic.phone.message.incorrect", locale));
			}
		} else {
			errors.add(messages.getMessage("NotEmpty.store.validatecode",
					locale));
		}
		if (errors.size() > 0) {
			model.addAttribute("email", ((Customer) request
					.getAttribute(Constants.CUSTOMER)).getEmailAddress());
			model.addAttribute("activeMenu", "safetyCenter");
			model.addAttribute("errors", errors);
			return template.toString();
		}

		customer.setEmailAddress(newEmail);
		customerService.saveOrUpdate(customer);
		// 邮箱修改成功后清空session中的手机校验码信息
		request.getSession().removeAttribute("phonecode");
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		emailTemplatesUtils.changeBasicinfoeNotificationEmail(customer, store,
				LocaleUtils.getLocale(customer.getDefaultLanguage()),
				request.getContextPath());

		model.addAttribute("success", "success");
		model.addAttribute("email", newEmail);
		model.addAttribute("activeMenu", "safetyCenter");
		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/changePhone.html", method = RequestMethod.POST)
	public String changePhone(Model model, HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws Exception {
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);
		String newPhone = request.getParameter("phone2");

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.changePhone)
				.append(".").append(store.getStoreTemplate());
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());
		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}
		List<String> errors = new ArrayList<String>();
		// 验证新手机是否注册过
		if (StringUtils.isNotBlank(newPhone)) {
			if (customerService.getByPhone(newPhone) != null) {
				LOGGER.debug(
						"Customer with phone {0} already exists for this store ",
						newPhone);
				errors.add(messages.getMessage(
						"registration.phone.already.exists", locale));
			}
		} else {
			errors.add(messages.getMessage("NotEmpty.store.validatecode",
					locale));
		}
		// 验证手机校验码 phonevalidate
		String checkPhonecode = request.getParameter("phonevalidate");
		if (StringUtils.isNotBlank(checkPhonecode)) {
			if (!checkPhonecode.equalsIgnoreCase((String) request.getSession()
					.getAttribute("phonecode"))) {
				errors.add(messages.getMessage(
						"label.generic.phone.message.incorrect", locale));
			}
		} else {
			errors.add(messages.getMessage("NotEmpty.store.validatecode",
					locale));
		}
		// 校验手机校验码 phonevalidate2
		String checkPhonecode2 = request.getParameter("phonevalidate2");
		if (StringUtils.isNotBlank(checkPhonecode2)) {
			if (!checkPhonecode2.equalsIgnoreCase((String) request.getSession()
					.getAttribute("phonecode2"))) {
				errors.add(messages.getMessage(
						"label.generic.phone.message.incorrect", locale));
			}
		} else {
			errors.add(messages.getMessage("NotEmpty.store.validatecode",
					locale));
		}
		if (errors.size() > 0) {
			model.addAttribute("email", ((Customer) request
					.getAttribute(Constants.CUSTOMER)).getEmailAddress());
			model.addAttribute("activeMenu", "safetyCenter");
			model.addAttribute("errors", errors);
			return template.toString();
		}

		customer.setPhone(newPhone);
		customerService.saveOrUpdate(customer);
		// 邮箱修改成功后清空session中的手机校验码信息
		request.getSession().removeAttribute("phonecode");
		request.getSession().removeAttribute("phonecode2");
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		emailTemplatesUtils.changeBasicinfoeNotificationEmail(customer, store,
				LocaleUtils.getLocale(customer.getDefaultLanguage()),
				request.getContextPath());

		model.addAttribute("success", "success");
		model.addAttribute("phone", newPhone);
		model.addAttribute("activeMenu", "safetyCenter");
		return template.toString();
	}

	@RequestMapping(value = { "/phoneCode.html" }, method = RequestMethod.POST)
	public @ResponseBody String sendPhoneMessage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}
		AjaxResponse ajaxResponse = new AjaxResponse();
		HttpSession session = request.getSession();
		String pid = request.getParameter("captchaResponseField");
		String phone = customer.getPhone();
		if (pid == null || StringUtils.isBlank(pid)) {
			ajaxResponse.setStatus(-2);
			return ajaxResponse.toJSONString();
		} else if (session.getAttribute("validaioncode") == null
				|| !pid.equalsIgnoreCase(session.getAttribute("validaioncode")
						.toString())) {
			ajaxResponse.setStatus(-3);
			return ajaxResponse.toJSONString();
		} else if (StringUtils.isBlank(phone)) {
			ajaxResponse.setStatus(-4);
			return ajaxResponse.toJSONString();
		}

		try {

			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(phone,
					RadomSixNumber.CHANGE_PW_TEMP, radomNumber, "15", request);
			if (result) {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			} else {
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
			// clear seesion
		} catch (Exception e) {
			LOGGER.error("An error occured while trying to send an email", e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		return ajaxResponse.toJSONString();

	}

	/**
	 * Manage the edition of customer attributes
	 * 
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = { "/attributes/save.html" }, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String saveCustomerAttributes(
			HttpServletRequest request, Locale locale) throws Exception {

		AjaxResponse resp = new AjaxResponse();

		MerchantStore store = (MerchantStore) request
				.getAttribute(Constants.MERCHANT_STORE);

		@SuppressWarnings("rawtypes")
		Enumeration parameterNames = request.getParameterNames();

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			LOGGER.error("Customer id [customer] is not defined in the parameters");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			return resp.toJSONString();
		}

		if (customer.getMerchantStore().getId().intValue() != store.getId()
				.intValue()) {
			LOGGER.error("Customer id does not belong to current store");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			return resp.toJSONString();
		}

		List<CustomerAttribute> customerAttributes = customerAttributeService
				.getByCustomer(store, customer);
		Map<Long, CustomerAttribute> customerAttributesMap = new HashMap<Long, CustomerAttribute>();

		for (CustomerAttribute attr : customerAttributes) {
			customerAttributesMap.put(attr.getCustomerOption().getId(), attr);
		}

		parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {

			String parameterName = (String) parameterNames.nextElement();
			String parameterValue = request.getParameter(parameterName);
			try {

				String[] parameterKey = parameterName.split("-");
				com.salesmanager.core.business.customer.model.attribute.CustomerOption customerOption = null;
				com.salesmanager.core.business.customer.model.attribute.CustomerOptionValue customerOptionValue = null;

				if (CUSTOMER_ID_PARAMETER.equals(parameterName)) {
					continue;
				}

				if (parameterKey.length > 1) {
					// parse key - value
					String key = parameterKey[0];
					String value = parameterKey[1];
					// should be on
					customerOption = customerOptionService
							.getById(new Long(key));
					customerOptionValue = customerOptionValueService
							.getById(new Long(value));

				} else {
					customerOption = customerOptionService.getById(new Long(
							parameterName));
					customerOptionValue = customerOptionValueService
							.getById(new Long(parameterValue));

				}

				// get the attribute
				CustomerAttribute attribute = customerAttributesMap
						.get(customerOption.getId());
				if (attribute == null) {
					attribute = new CustomerAttribute();
					attribute.setCustomer(customer);
					attribute.setCustomerOption(customerOption);
				} else {
					customerAttributes.remove(attribute);
				}

				if (customerOption.getCustomerOptionType().equals(
						CustomerOptionType.Text.name())) {
					if (!StringUtils.isBlank(parameterValue)) {
						attribute.setCustomerOptionValue(customerOptionValue);
						attribute.setTextValue(parameterValue);
					} else {
						attribute.setTextValue(null);
					}
				} else {
					attribute.setCustomerOptionValue(customerOptionValue);
				}

				if (attribute.getId() != null
						&& attribute.getId().longValue() > 0) {
					if (attribute.getCustomerOptionValue() == null) {
						customerAttributeService.delete(attribute);
					} else {
						customerAttributeService.update(attribute);
					}
				} else {
					customerAttributeService.save(attribute);
				}

			} catch (Exception e) {
				LOGGER.error("Cannot get parameter information "
						+ parameterName, e);
			}

		}

		// and now the remaining to be removed
		for (CustomerAttribute attr : customerAttributes) {
			customerAttributeService.delete(attr);
		}

		// refresh customer
		Customer c = customerService.getById(customer.getId());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);

		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		return resp.toJSONString();

	}

	// 查询
	// @PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/billing.html")
	public String displayCustomerBillingAddress(Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);
		Language language = getSessionAttribute(Constants.LANGUAGE, request);

		/*
		 * Authentication auth =
		 * SecurityContextHolder.getContext().getAuthentication(); Customer
		 * customer = null; if(auth != null
		 * &&request.isUserInRole("AUTH_CUSTOMER")) { customer =
		 * customerFacade.getByID(auth.getName());
		 * 
		 * }
		 * 
		 * if(customer==null) { return "redirect:/"+Constants.SHOP_URI; }
		 */
		Customer customer = super.getSessionAttribute(Constants.CUSTOMER,
				request);
		if (customer != null) {
			model.addAttribute("customer", customer);
			List<CustomerAdress> addresss = customerAddressFacade
					.getCustomerAddress(customer, language);
			Set<CustomerInvoice> invoice = customer.getInvoices();
			if (null != invoice) {
				model.addAttribute("totalinvoice", invoice.size());
				model.addAttribute("invoices", invoice);
			}
			if (null != addresss) {
				model.addAttribute("totaladdress", addresss.size());
				model.addAttribute("addresss", addresss);
			}
			model.addAttribute("address", new CustomerAdress());
			model.addAttribute("invoice", new CustomerInvoice());
			model.addAttribute("maxaddress", Constants.MAX_ADDRESS_NUMBER);
			model.addAttribute("maxinvoice", Constants.MAX_INVOICES_NUMBER);
			model.addAttribute("activeMenu", "billing");
		}

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.Billing).append(".")
				.append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * 获取当前登陆用户所有地址信息
	 * 
	 * @return
	 */
	@RequestMapping("/findAddress.html")
	public @ResponseBody DefautAddressInvoice getCustomerAdress(
			HttpServletRequest request) {
		Customer customer = super.getSessionAttribute(Constants.CUSTOMER,
				request);
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		try {
			if (customer != null) {
				DefautAddressInvoice  defautAddressInvoice=new DefautAddressInvoice();
				CustomerAdress defaultAddress = customerAddressFacade.getDefaultAdress(customer, language);
				defautAddressInvoice.setDefaultAddress(defaultAddress);
				defautAddressInvoice.setOthersAddresss(customerAddressFacade.getCustomerAddress(customer,language));
				return defautAddressInvoice;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/basicInfo.html", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String basicInfo(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}
		Map<String, Integer> cmap = (Map<String, Integer>) request.getSession()
				.getServletContext().getAttribute("custmoerGradeMap");
		BigDecimal grade = new BigDecimal(cmap.get(String.valueOf(customer
				.getGrade())));

		model.addAttribute("customer", customer);
		model.addAttribute("grade", grade);
		long totalordermoney = orderService.getTotalMoney(customer);
		BigDecimal total = new BigDecimal(totalordermoney);
		model.addAttribute("left", grade.subtract(total));
		model.addAttribute("activeMenu", "basicinfo");

		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.BasicInfo)
				.append(".").append(store.getStoreTemplate());
		return template.toString();
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/addInvoice.html", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addInvoice(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}

		model.addAttribute("invoice", new CustomerInvoice());

		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.Invoice).append(".")
				.append(store.getStoreTemplate());
		return template.toString();
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/updateBasicinfo.html", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String updateBasicInfo(
			@Valid @ModelAttribute("customer") Customer customer,
			BindingResult bindingResult, Model model,
			@RequestParam("captchaResponseField") String captchaResponseField,
			HttpServletRequest request, final Locale locale) throws Exception {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE,
				request);

		/** template **/
		StringBuilder template = new StringBuilder()
				.append(ControllerConstants.Tiles.Customer.BasicInfo)
				.append(".").append(store.getStoreTemplate());
		if (StringUtils.isBlank(captchaResponseField)) {
			FieldError error = new FieldError("captchaResponseField",
					"captchaResponseField", messages.getMessage(
							"NotEmpty.contact.captchaResponseField", locale));
			bindingResult.addError(error);
			return template.toString();
		} else {
			HttpSession session = request.getSession();
			if (session.getAttribute("validaioncode") == null
					|| !captchaResponseField.equalsIgnoreCase(session
							.getAttribute("validaioncode").toString())) {
				LOGGER.debug("Captcha response does not matched");
				FieldError error = new FieldError("captchaChallengeField",
						"captchaChallengeField", messages.getMessage(
								"validaion.recaptcha.not.matched", locale));
				bindingResult.addError(error);
				return template.toString();
			}
		}

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		// Customer customer = null;
		Customer c = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			c = customerFacade.getByID(auth.getName());

		}

		if (c == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}

		// 用户不允许修改手机号码和邮箱信息，只能在安全账户模块修改
		c.setNick(customer.getNick());
		c.setGender(customer.getGender());
		c.setCompnay(customer.getCompnay());
		c.setProject(customer.getProject());
		customerService.saveOrUpdate(c);
		customer.setPhone(c.getPhone());
		customer.setEmailAddress(c.getEmailAddress());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);
		// 重新认证

		emailTemplatesUtils.changeBasicinfoeNotificationEmail(c, store,
				LocaleUtils.getLocale(c.getDefaultLanguage()),
				request.getContextPath());
		Map<String, Integer> cmap = (Map<String, Integer>) request.getSession()
				.getServletContext().getAttribute("custmoerGradeMap");
		BigDecimal grade = new BigDecimal(
				cmap.get(String.valueOf(c.getGrade())));
		model.addAttribute("grade", grade);
		OrderCriteria criteria = new OrderCriteria();
		criteria.setcId(c.getId());
		long totalordermoney = orderService.getTotalMoney(customer);
		BigDecimal total = new BigDecimal(totalordermoney);
		model.addAttribute("left", grade.subtract(total));
		model.addAttribute("success", "success");

		return template.toString();

	}

	/**
	 * add
	 * */
	@RequestMapping(value = { "/alladdress.html" }, method = RequestMethod.POST)
	public @ResponseBody DefautAddressInvoice getAllAddress(
			@Valid @ModelAttribute("address") CustomerAdress address,
			final String setdefaultaddress, BindingResult bindingResult,
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());
		}
		if (customer == null) {
			return null;
		}

		if (bindingResult.hasErrors()) {
			LOGGER.info(
					"found {} error(s) while validating  customer address ",
					bindingResult.getErrorCount());
			return null;
		}
		Set<CustomerAddress> listAddres = customer.getAddresss();
		if (listAddres != null
				&& listAddres.size() >= Constants.MAX_ADDRESS_NUMBER) {
			return null;
		}
		Language language = getSessionAttribute(Constants.LANGUAGE, request);

		long addresID = customerAddressFacade.saveOrUpdateAddress(address,
				customer, language);

		// get custmor again
		customer = customerFacade.getByID(auth.getName());

		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		// get defaultaddress
		List<CustomerAdress> others = customerAddressFacade.getCustomerAddress(
				customer, language);
		if (setdefaultaddress != null
				&& setdefaultaddress.equalsIgnoreCase("on")) {
			CustomerAdress defaultAddres = customerAddressFacade.getById(
					addresID, customer, language);
			if (null != defaultAddres)
				defautAddressInvoice.setDefaultAddress(defaultAddres);
		} else {
			CustomerAdress defaultAddress = customerAddressFacade
					.getDefaultAdress(customer, language);
			if (null != defaultAddress)
				defautAddressInvoice.setDefaultAddress(defaultAddress);
		}
		CustomerInvoice defaultinvoice = customerInvoiceService
				.getDefaultInvoice(customer);
		if (null != defaultinvoice)
			defautAddressInvoice.setDefaultInvoice(defaultinvoice);
		if (null != others)
			defautAddressInvoice.setOthersAddresss(others);
		// get custmor again
		customer = customerFacade.getByID(auth.getName());
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		return defautAddressInvoice;

	}

	/**
	 * 删除
	 * */
	@RequestMapping(value = { "/removeInvoice.html" }, method = RequestMethod.POST)
	public @ResponseBody DefautAddressInvoice deleteInvoice(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return null;
		}
		String aid = request.getParameter("id");
		if (null != aid) {
			customerInvoiceService.delete(Long.parseLong(aid));

		}
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());

		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		// get defaultaddress

		CustomerInvoice defaultInvoice = customerInvoiceService
				.getDefaultInvoice(customer);
		if (null != defaultInvoice)
			defautAddressInvoice.setDefaultInvoice(defaultInvoice);

		defautAddressInvoice.setOthersInvoices(customer.getInvoices());
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		return defautAddressInvoice;
	}

	@RequestMapping(value = { "/defaultInvoice.html" }, method = RequestMethod.POST)
	public @ResponseBody DefautAddressInvoice defaultInvoice(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return null;
		}
		String aid = request.getParameter("id");
		if (null != aid) {
			customer.setInvoiceDefault(Long.parseLong(aid));
			customerService.update(customer);
		}
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());

		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		// get defaultaddress

		CustomerInvoice defaultInvoice = customerInvoiceService
				.getDefaultInvoice(customer);
		if (null != defaultInvoice)
			defautAddressInvoice.setDefaultInvoice(defaultInvoice);
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		CustomerAdress defaultaddress = customerAddressFacade.getDefaultAdress(
				customer, language);
		if (null != defaultaddress)
			defautAddressInvoice.setDefaultAddress(defaultaddress);
		defautAddressInvoice.setOthersInvoices(customer.getInvoices());
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		return defautAddressInvoice;
	}

	@RequestMapping(value = { "/defaultAddress.html" }, method = RequestMethod.POST)
	public @ResponseBody DefautAddressInvoice defaultAddress(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return null;
		}
		String aid = request.getParameter("id");
		if (null != aid) {
			customer.setAddressDefault(Long.parseLong(aid));
			customerService.update(customer);
		}
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());

		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		defautAddressInvoice.setDefaultAddresInvice(customer
				.getInvoiceaddressdefault());
		// get defaultaddress
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		CustomerAdress defaultAddre = customerAddressFacade.getDefaultAdress(
				customer, language);
		if (null != defaultAddre)
			defautAddressInvoice.setDefaultAddress(defaultAddre);
		List<CustomerAdress> others = customerAddressFacade.getCustomerAddress(
				customer, language);
		if (null != others)
			defautAddressInvoice.setOthersAddresss(others);
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		return defautAddressInvoice;
	}

	@RequestMapping(value = { "/defaultInvoiceAddress.html" }, method = RequestMethod.POST)
	public @ResponseBody DefautAddressInvoice defaultAddressInvoice(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return null;
		}
		String aid = request.getParameter("id");
		if (null != aid) {
			customer.setInvoiceaddressdefault(Long.parseLong(aid));
			customerService.update(customer);
		}
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());
		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		defautAddressInvoice.setDefaultAddresInvice(customer
				.getInvoiceaddressdefault());
		// get defaultaddress
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		CustomerAdress defaultAddre = customerAddressFacade.getDefaultAdress(
				customer, language);
		if (null != defaultAddre)
			defautAddressInvoice.setDefaultAddress(defaultAddre);
		List<CustomerAdress> others = customerAddressFacade.getCustomerAddress(
				customer, language);
		if (null != others)
			defautAddressInvoice.setOthersAddresss(others);
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		return defautAddressInvoice;
	}

	/**
	 * 删除
	 * */
	@RequestMapping(value = { "/removeAddress.html" }, method = RequestMethod.POST)
	public @ResponseBody DefautAddressInvoice removeAddres(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return null;
		}
		String aid = request.getParameter("id");
		if (!aid.equals(null)) {
			customerAddressFacade.remove(Long.parseLong(aid));
		}
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());

		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		defautAddressInvoice.setDefaultAddresInvice(customer
				.getInvoiceaddressdefault() == null ? 0 : customer
				.getInvoiceaddressdefault());
		// get defaultaddress
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		CustomerAdress defaultAddre = customerAddressFacade.getDefaultAdress(
				customer, language);
		if (null != defaultAddre)
			defautAddressInvoice.setDefaultAddress(defaultAddre);
		List<CustomerAdress> others = customerAddressFacade.getCustomerAddress(
				customer, language);
		if (null != others)
			defautAddressInvoice.setOthersAddresss(others);
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		return defautAddressInvoice;
	}

	@RequestMapping(value = { "/updateAddress.html" }, method = RequestMethod.POST)
	public @ResponseBody CustomerAdress updateassress(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return null;
		}

		String aid = request.getParameter("id");

		if (null != aid) {
			Language language = getSessionAttribute(Constants.LANGUAGE, request);
			CustomerAdress address = customerAddressFacade.getById(
					Long.valueOf(aid), customer, language);
			return address;

		}
		return null;

	}

	/**
	 * 添加客户地址
	 * 
	 * @return
	 */
	@RequestMapping("/saveCustomerAddress.html")
	@ResponseBody
	public String saveCustomerAddress(HttpServletRequest request,
			com.salesmanager.web.entity.customer.CustomerAdress customerAddress) {
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		Customer customer = super.getSessionAttribute(Constants.CUSTOMER,
				request);
		if (customer != null) {
			try {
				customerAddressFacade.saveOrUpdate(customerAddress, customer,
						language);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	@RequestMapping(value = { "/updateInvoice.html" }, method = RequestMethod.POST)
	public @ResponseBody CustomerInvoice updateinvoice(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}

		if (customer == null) {
			return null;
		}

		String aid = request.getParameter("id");

		if (null != aid && !aid.equalsIgnoreCase("-1")) {
			CustomerInvoice invoice = customerInvoiceService.getbyid(
					Long.parseLong(aid), customer);
			return invoice;

		}
		return new CustomerInvoice();

	}

	@RequestMapping(value = { "/allinvoices.html" }, method = RequestMethod.POST)
	public @ResponseBody DefautAddressInvoice getAllInvoices(
			@Valid @ModelAttribute("invoice") CustomerInvoice invoice,
			final String setdefaultinvoice, BindingResult bindingResult,
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth != null && request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getByID(auth.getName());

		}
		if (customer == null) {
			return null;
		}

		if (bindingResult.hasErrors()) {
			LOGGER.info(
					"found {} error(s) while validating  customer address ",
					bindingResult.getErrorCount());
			return null;
		}
		Set<CustomerInvoice> listInvoice = customer.getInvoices();
		if (listInvoice != null
				&& listInvoice.size() >= Constants.MAX_INVOICES_NUMBER) {
			return null;
		}
		Language language = getSessionAttribute(Constants.LANGUAGE, request);
		invoice.setCustomer(customer);
		String invoiceCampany = invoice.getCompany();
		if (!StringUtils.isBlank(invoiceCampany)) {
			String[] names = invoiceCampany.split(",");
			if (invoice.getType() == 0) {
				invoice.setCompany(names[0]);
				// 清空其他信息
				invoice.setBankAccount("");
				invoice.setBankName("");
				invoice.setCompanyAddress("");
				invoice.setCompanyTelephone("");
				invoice.setTaxpayerNumber("");
				invoice.setMemo("");
			} else {
				invoice.setCompany(names[1]);
			}
		}

		long addresID = customerInvoiceService.saveOrUpdateInvoice(invoice);
		;

		// get custmor again
		customer = customerFacade.getByID(auth.getName());
		// Customer c = customerService.getById(customer.getId());

		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		// get defaultaddress
		if (setdefaultinvoice != null
				&& setdefaultinvoice.equalsIgnoreCase("on")) {
			CustomerInvoice defaultInvoice = customerInvoiceService.getbyid(
					addresID, customer);
			if (null != defaultInvoice)
				defautAddressInvoice.setDefaultInvoice(defaultInvoice);
		} else {
			CustomerInvoice defaultInvoice = customerInvoiceService
					.getDefaultInvoice(customer);
			if (null != defaultInvoice)
				defautAddressInvoice.setDefaultInvoice(defaultInvoice);
		}
		CustomerAdress defaultaddress = customerAddressFacade.getDefaultAdress(
				customer, language);
		if (null != defaultaddress)
			defautAddressInvoice.setDefaultAddress(defaultaddress);
		defautAddressInvoice.setOthersInvoices(customer.getInvoices());
		// get custmoer again
		customer = customerFacade.getByID(auth.getName());
		super.setSessionAttribute(Constants.CUSTOMER, customer, request);
		return defautAddressInvoice;

	}

	@ModelAttribute("countries")
	protected List<Country> getCountries(final HttpServletRequest request) {

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

}
