package com.salesmanager.web.shop.controller.store;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.content.model.Content;
import com.salesmanager.core.business.content.model.ContentDescription;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.ContactForm;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.RadomSixNumber;

@Controller
public class ContactController extends AbstractController {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContactController.class);
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private CoreConfiguration coreConfiguration;
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;
	
	@Autowired
	private CustomerFacade customerFacade;
	
	private final static String CONTACT_LINK = "CONTACT";
	
	
	@RequestMapping("/shop/store/contactus.html")
	public String displayContact(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		request.setAttribute(Constants.LINK_CODE, CONTACT_LINK);
		

		Language language = (Language)request.getAttribute("LANGUAGE");
		
		ContactForm contact = new ContactForm();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		Customer customer = customerFacade.getByID(auth.getName());
    		if(customer != null){
    			contact.setName(customer.getNick());
    		}

        }
		model.addAttribute("contact", contact);
		
		//model.addAttribute( "recapatcha_public_key", coreConfiguration.getProperty( Constants.RECAPATCHA_PUBLIC_KEY ) );
		Content content = contentService.getByCode(Constants.CONTENT_CONTACT_US, store, language);
		ContentDescription contentDescription = null;
		if(content!=null && content.isVisible()) {
			contentDescription = content.getDescription();
		}
		
		if(contentDescription!=null) {

			//meta information
			PageInformation pageInformation = new PageInformation();
			pageInformation.setPageDescription(contentDescription.getMetatagDescription());
			pageInformation.setPageKeywords(contentDescription.getMetatagKeywords());
			pageInformation.setPageTitle(contentDescription.getTitle());
			pageInformation.setPageUrl(contentDescription.getName());
			
			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
			
			model.addAttribute("content",contentDescription);

		} 

		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.contactus).append(".").append(store.getStoreTemplate());
		return template.toString();
		
		
	}
	@RequestMapping("/shop/store/code.html")
	public void getCode(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		 int width = 90;
		 int height = 25;
		 int codeCount = 4;
		 int xx = 15;
		 int fontHeight = 20;
		 int codeY = 20;
		 char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics gd = buffImg.getGraphics();
		
		Random random = new Random();
	
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		
		gd.setFont(font);

	
		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);

		
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		for (int i = 0; i < codeCount; i++) {
			
			String code = String.valueOf(codeSequence[random.nextInt(36)]);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			gd.setColor(new Color(red, green, blue));
			gd.drawString(code, (i + 1) * xx, codeY);

			
			randomCode.append(code);
		}
		
		
		HttpSession session = req.getSession();
		String flag = req.getParameter("flag");
		//指定随机码的自定义值
		if (StringUtils.isBlank(flag)) {
			session.setAttribute("validaioncode", randomCode.toString());
		} else {
			session.setAttribute("validaioncode"+flag, randomCode.toString());
		}
		
		
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);

		resp.setContentType("image/jpeg");
		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
	}
	
	@RequestMapping(value={"/shop/store/{storeCode}/contact"}, method=RequestMethod.POST)
	public @ResponseBody String sendEmail(@ModelAttribute(value="contact") ContactForm contact, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		try {
			
			if ( StringUtils.isBlank( contact.getCaptchaResponseField() )) {
    			//FieldError error = new FieldError("captchaResponseField","captchaResponseField",messages.getMessage("NotEmpty.contact.captchaResponseField", locale));
    			//bindingResult.addError(error);
	           // ajaxResponse.setErrorString("NotEmpty.contact.captchaResponseField");
	            ajaxResponse.setStatus(-2);
	            return ajaxResponse.toJSONString();
			}else{
				String validaioncode = contact.getCaptchaResponseField();
				HttpSession session = request.getSession();
				if(session.getAttribute("validaioncode")==null || !validaioncode.equalsIgnoreCase(session.getAttribute("validaioncode").toString())){
					 LOGGER.debug( "Captcha response does not matched" );
	        			//FieldError error = new FieldError("captchaChallengeField","captchaChallengeField",messages.getMessage("validaion.recaptcha.not.matched", locale));
	        			//bindingResult.addError(error);
					 //ajaxResponse.setErrorString("validaion.recaptcha.not.matched");
	        			  ajaxResponse.setStatus(-1);
	      	            return ajaxResponse.toJSONString();
				}
			}
	        
	        emailTemplatesUtils.sendContactEmail(contact, store, LocaleUtils.getLocale(store.getDefaultLanguage()), request.getContextPath());
			
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			//clear seesion
		} catch(Exception e) {
			LOGGER.error("An error occured while trying to send an email",e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return ajaxResponse.toJSONString();
		
		
	}
	@RequestMapping(value={"/shop/store/phoneCodeNum.html"}, method=RequestMethod.POST)
	public @ResponseBody String sendPhoneNum(HttpServletRequest request, HttpServletResponse response) throws Exception {

		AjaxResponse ajaxResponse = new AjaxResponse();
		HttpSession session = request.getSession();
		if(session.getAttribute("account")==null || session.getAttribute("validaioncode")==null){
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		String pid = request.getParameter("captchaResponseField");
		 Customer customer = customerFacade.getCustomerForLogin( session.getAttribute("account").toString());
		//String flag = StringUtils.isBlank(request.getParameter("flag"))?"":request.getParameter("flag");
		//验证用户手机号码是否已经注册过
		
		if(customer ==null){
			ajaxResponse.setStatus(-4);
			return ajaxResponse.toJSONString();
		}
		if(pid == null || StringUtils.isBlank(pid))  {
			ajaxResponse.setStatus(-2);
			return ajaxResponse.toJSONString();
		}else if(!pid.equalsIgnoreCase(session.getAttribute("validaioncode").toString())){
			ajaxResponse.setStatus(-3);
			return ajaxResponse.toJSONString();
		} 
		
		try {
			
			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(customer.getPhone(), RadomSixNumber.CHANGE_PW_TEMP, radomNumber, "15", "", request);
			if(result){
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				session.setAttribute("phonenum", customer.getPhone());
			}else{
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
			//clear seesion
		} catch(Exception e) {
			LOGGER.error("An error occured while trying to send an email",e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return ajaxResponse.toJSONString();
		
		
		
	}

	@RequestMapping(value={"/shop/store/phoneCodeNumTwo.html"}, method=RequestMethod.POST)
	public @ResponseBody String sendPhoneNumtow(HttpServletRequest request, HttpServletResponse response) throws Exception {

		AjaxResponse ajaxResponse = new AjaxResponse();
		HttpSession session = request.getSession();
		
		String phone = request.getParameter("phone");
		
		//验证用户手机号码是否已经注册过
		Customer c =customerFacade.getCustomerForLogin(phone);
		if(c !=null){
			ajaxResponse.setStatus(-5);
			return ajaxResponse.toJSONString();
		}
		 else if (StringUtils.isBlank(phone)) {
			ajaxResponse.setStatus(-4);
			return ajaxResponse.toJSONString();
		}
		
		try {
			
			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(phone, RadomSixNumber.REGIST_TEMP, radomNumber, "15", "", request);
			if(result){
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				session.setAttribute("phonenum", phone);
			}else{
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
			//clear seesion
		} catch(Exception e) {
			LOGGER.error("An error occured while trying to send an email",e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return ajaxResponse.toJSONString();
		
		
	}
	
	@RequestMapping(value={"/shop/store/phoneCode.html"}, method=RequestMethod.POST)
	public @ResponseBody String sendPhoneMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {

		AjaxResponse ajaxResponse = new AjaxResponse();
		HttpSession session = request.getSession();
		String pid = request.getParameter("captchaResponseField");
		String phone = request.getParameter("phone");
		String flag = StringUtils.isBlank(request.getParameter("flag"))?"":request.getParameter("flag");
		//验证用户手机号码是否已经注册过
		Customer c =customerFacade.getCustomerForLogin(phone);
		if(c !=null){
			ajaxResponse.setStatus(-5);
			return ajaxResponse.toJSONString();
		}
		if(pid == null || StringUtils.isBlank(pid))  {
			ajaxResponse.setStatus(-2);
			return ajaxResponse.toJSONString();
		}else if(session.getAttribute("validaioncode"+flag)==null || !pid.equalsIgnoreCase(session.getAttribute("validaioncode"+flag).toString())){
			ajaxResponse.setStatus(-3);
			return ajaxResponse.toJSONString();
		} else if (StringUtils.isBlank(phone)) {
			ajaxResponse.setStatus(-4);
			return ajaxResponse.toJSONString();
		}
		
		try {
			
			String radomNumber = RadomSixNumber.getRadomNumber();
			boolean result = RadomSixNumber.sendMessage(phone, RadomSixNumber.REGIST_TEMP, radomNumber, "15", "", request);
			if(result){
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				session.setAttribute("phonenum"+flag, phone);
			}else{
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
			//clear seesion
		} catch(Exception e) {
			LOGGER.error("An error occured while trying to send an email",e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return ajaxResponse.toJSONString();
		
		
	}
	@RequestMapping("/shop/pointChange.html")
	public String displayPointChange( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("pointChange").append(".").append(store.getStoreTemplate());
		return template.toString();
		
		
	}
	
	@RequestMapping("/shop/aboutUS.html")
	public String displayAboutUs( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("aboutUs").append(".").append(store.getStoreTemplate());
		return template.toString();
		
		
	}
	
	@RequestMapping("/shop/nonsupport.html")
	public String nonsupport( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		return "disclaimer";
	}
	
	@RequestMapping("/shop/privacyPolicy.html")
	public String displayPrivacyPolicy( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("privacyPolicy").append(".").append(store.getStoreTemplate());
		return template.toString();
		
		
	}
	@RequestMapping("/shop/quality.html")
	public String displayQuality( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("quality").append(".").append(store.getStoreTemplate());
		return template.toString();
	}
	@RequestMapping("/shop/detectService.html")
	public String displaydetectService( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("detectService").append(".").append(store.getStoreTemplate());
		return template.toString();
	}
	
	@RequestMapping("/shop/noviceguidelines.html")
	public String noviceguidelines( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("noviceguidelines").append(".").append(store.getStoreTemplate());
		return template.toString();
	}
	@RequestMapping("/shop/dispalyImage.html")
	public String dispalyImage( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pid = request.getParameter("ipath");
		model.addAttribute("pid", pid);
		return "disimage";
	}
	@RequestMapping("/shop/singleorder.html")
	public String singleorder( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("singleorder").append(".").append(store.getStoreTemplate());
		return template.toString();
	}
}
