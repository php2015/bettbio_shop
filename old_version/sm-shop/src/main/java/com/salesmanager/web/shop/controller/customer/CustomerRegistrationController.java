package com.salesmanager.web.shop.controller.customer;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.salesmanager.core.business.catalog.product.model.image.CustomerImage;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.customer.CustomerRegistrationException;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.utils.BaseDataUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.customer.SecuredShopPersistableCustomer;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.RadomSixNumber;

/**
 * Registration of a new customer
 * @author Carl Samson
 *
 */

// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
//只保留手机号和邮箱后台校验
@Controller
@RequestMapping("/shop/customer")
public class CustomerRegistrationController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationController.class);
    
    
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
	private ContentService contentService;
	
	@SuppressWarnings("deprecation")
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value="/registration.html", method=RequestMethod.GET)
	public String displayRegistration(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		SecuredShopPersistableCustomer customer = new SecuredShopPersistableCustomer();
		//modify by cy custmer not banding with billing
		
		model.addAttribute("customer", customer);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(store.getStoreTemplate());

		return template.toString();

	}
	
	/**
	 * @author 百图生物
	 * @Date('2016/5/24')
	 * @exception
	 * 商家注册
	 */
	@RequestMapping(value="/storeinsertAdmin.html",method=RequestMethod.GET)
	public String storeinsertRegister(final Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		 MerchantStore merchantStore = (MerchantStore) request.getAttribute( Constants.MERCHANT_STORE );
		 User user = new User();
			
		 model.addAttribute("user", user);
		 StringBuilder template =
	                new StringBuilder().append("storeinsert").append( "." ).append( merchantStore.getStoreTemplate() );
	        return template.toString();
	}
    @RequestMapping( value = "/register.html", method = RequestMethod.POST )
    public String registerCustomer( @Valid
    @ModelAttribute("customer") SecuredShopPersistableCustomer customer, BindingResult bindingResult, Model model,
                                    HttpServletRequest request, final Locale locale,@RequestParam(required=false, value="licenceimg") CommonsMultipartFile licenceimg)

        throws Exception
    {
    	model.addAttribute("customer", customer);
        MerchantStore merchantStore = (MerchantStore) request.getAttribute( Constants.MERCHANT_STORE );
        Language language = super.getLanguage(request);
        
        String password = null;
                
        if ( StringUtils.isNotBlank( customer.getEmailAddress() ) && StringUtils.isNotBlank(customer.getPhone()) )
        {
            if ( customerFacade.checkIfUserExists( customer.getEmailAddress(), customer.getPhone() ) )
            {
                LOGGER.debug( "Customer with username {} already exists for this store ", customer.getUserName() );
            	FieldError error = new FieldError("userName","userName",messages.getMessage("registration.username.already.exists", locale));
            	bindingResult.addError(error);
            }
        }else{
        	 LOGGER.debug( "found {} validation error while validating in customer registration ",
                     bindingResult.getErrorCount() );
        	 StringBuilder template =
        			 new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
        	 return template.toString();
        }
        
        
        if ( StringUtils.isNotBlank( customer.getPassword() ) &&  StringUtils.isNotBlank( customer.getCheckPassword() ))
        {
            if (! customer.getPassword().equals(customer.getCheckPassword()) )
            {
            	FieldError error = new FieldError("password","password",messages.getMessage("message.password.checkpassword.identical", locale));
            	bindingResult.addError(error);

            }
            password = customer.getPassword();
            String reg = "^\\s*[^\\s\u4e00-\u9fa5]{6,99}\\s*$";
            if (!password.matches(reg)){
            	FieldError error = new FieldError("password","password",messages.getMessage("newpassword.not.week", locale));
            	bindingResult.addError(error);
            }
        }else{
	       	 LOGGER.debug( "found {} validation error while validating in customer registration ",
	                    bindingResult.getErrorCount() );
	       	 StringBuilder template =
	       			 new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
	       	 return template.toString();
        }
        
        //验证手机校验码
        /*String checkPhonecode = request.getParameter("checkPhonecode");
        if ( StringUtils.isNotBlank( checkPhonecode ))
        {
        	if(!customer.getPhone().equalsIgnoreCase((String)request.getSession().getAttribute("phonenum"))){
        		FieldError error = new FieldError("phone","phone",messages.getMessage("registration.phone.wrong.num", locale));
            	bindingResult.addError(error);
        	}
        	if (!checkPhonecode.equalsIgnoreCase((String)request.getSession().getAttribute("phonecode")))
            {
            	FieldError error = new FieldError("phonecode","phonecode",messages.getMessage("label.generic.phone.message.incorrect", locale));
            	bindingResult.addError(error);
            }
        }else{
	       	 LOGGER.debug( "found {} validation error while validating in customer registration ",
	                    bindingResult.getErrorCount() );
	       	 StringBuilder template =
	       			 new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
	       	 return template.toString();
        }

               
        if ( bindingResult.hasErrors() )
        {
            LOGGER.debug( "found {} validation error while validating in customer registration ",
                         bindingResult.getErrorCount() );
            StringBuilder template =
                new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
            request.setAttribute("c", "customer");
            return template.toString();

        }*/

        @SuppressWarnings( "unused" )
        CustomerEntity customerData = null;
        CustomerImage custometimage  =  null;

        /**
         * 证件照
         * */
        if (licenceimg!=null && licenceimg.getSize()>0) {
			String imageName = licenceimg.getOriginalFilename();
			String imageN = RadomSixNumber.getImageName(imageName);
			LOGGER.info(imageName);
            InputStream inputStream = licenceimg.getInputStream();
            custometimage  =  new CustomerImage();
            custometimage.setCustomerImage(imageN);
            custometimage.setImage(inputStream);          
		}
                   

        /**
         * Login user
         */
        
        try {
        	 
	        //refresh customer
        	//String phone = customer.getPhone();
	        /**
        	 * 注册积分
        	 * */
        	MemberPoints memberPoints = new MemberPoints();
//        	String points = getPoint(Constants.REGIST_SCORE);
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
        	        	
            //set user clear password
        	customer.setClearPassword(password);
     
        	customerData = customerFacade.registerCustomer( customer, merchantStore, language, memberPoints,custometimage);
        	//用户注册成功后，清除session中的phonecode
        	request.getSession().removeAttribute("phonecode");
        	request.getSession().removeAttribute("phonenum");
        	Customer c = memberPoints.getCustomer();
        	//authenticate
	        super.setSessionAttribute(Constants.CUSTOMER, c, request);
	        /**
             * Send registration email
             */  
	        //c=customerFacade.getCustomerForLogin(phone);
	        String msg = null;
	        try{
		        emailTemplatesUtils.sendActiveEmail(c.getId().toString(), merchantStore, request.getContextPath());
	        }catch(Exception e){
	        	/*FieldError error = new FieldError("email","email",messages.getMessage(e.getMessage(), locale));
	        	bindingResult.addError(error);*/
	        	msg = e.getMessage();
	        }
            StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.information).append(".").append(merchantStore.getStoreTemplate());
	        String[] paraMessage = {c.getEmailAddress()};
	        String smessage=  messages.getMessage("label.register.confirmation",paraMessage,locale);
	        if(msg != null)
	        {
	        	model.addAttribute( "information",paraMessage[0].toString()+":"+msg.toString());
	        }
	        else
	        {
	        	model.addAttribute( "information",smessage.toString());
	        }
	        return template.toString();
	        
	    
	       
        
        }catch ( CustomerRegistrationException cre ){
            LOGGER.error( "Error while registering customer.. ", cre);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
             return template.toString();
        }catch(Exception e) {
        	LOGGER.error("Cannot authenticate user ",e);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
        }finally {
        	request.setAttribute("c", "customer");
        }
        
        StringBuilder template =
                new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
        return template.toString();

    }

	@SuppressWarnings("unchecked")
	private String getPoint(String type) {
		return String.valueOf(BaseDataUtils.addCoustomerByOrader(type)) ;
	}
	
	
	@ModelAttribute("countryList")
	public List<Country> getCountries(final HttpServletRequest request){
	    
        Language language = (Language) request.getAttribute( "LANGUAGE" );
        try
        {
            if ( language == null )
            {
                language = (Language) request.getAttribute( "LANGUAGE" );
            }

            if ( language == null )
            {
                language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
            }
            List<Country> countryList=countryService.getCountries( language );
            return countryList;
        }
        catch ( ServiceException e )
        {
            LOGGER.error( "Error while fetching country list ", e );

        }
        return Collections.emptyList();
    }
	
	@ModelAttribute("zoneList")
    public List<Zone> getZones(final HttpServletRequest request){
	    return zoneService.list();
	}
	
	@RequestMapping(value="/protocol.html", method=RequestMethod.GET)
	public String displayProtocol(Model model,HttpServletRequest request, HttpServletResponse response) {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		StringBuilder template = new StringBuilder().append("userProtocol").append(".").append(store.getStoreTemplate());
		return template.toString();
	}
	/**
	 * 手机号和邮箱验证
	 * @throws UnsupportedEncodingException 
	 * */
	@RequestMapping(value="/validtion.html",method=RequestMethod.POST)
	public @ResponseBody String valitationByCustomer(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		response.setCharacterEncoding("html/text;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		AjaxResponse resp = new AjaxResponse();
		try {
			String phone =request.getParameter("phone");
			String emailAddress=request.getParameter("emailAddress");
			resp.setStatus(customerFacade.valitationByCustomer(phone,emailAddress));			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resp.toJSONString();
	}

	
	

	


}
