package com.salesmanager.web.admin.controller.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.attribute.CustomerAttributeService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionSetService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.EmailTemplatesUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

@Controller
public class CustomerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerOptionService customerOptionService;
	
	@Autowired
	private CustomerOptionValueService customerOptionValueService;
	
	@Autowired
	private CustomerOptionSetService customerOptionSetService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private CustomerAttributeService customerAttributeService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;
	/**
	
	
	private void getCustomerOptions(Model model, Customer customer, MerchantStore store, Language language) throws Exception {

		Map<Long,CustomerOption> options = new HashMap<Long,CustomerOption>();
		//get options
		List<CustomerOptionSet> optionSet = customerOptionSetService.listByStore(store, language);
		if(!CollectionUtils.isEmpty(optionSet)) {
			
			
			CustomerOptionPopulator optionPopulator = new CustomerOptionPopulator();
			
			Set<CustomerAttribute> customerAttributes = customer.getAttributes();
			
			for(CustomerOptionSet optSet : optionSet) {
				
				com.salesmanager.core.business.customer.model.attribute.CustomerOption custOption = optSet.getCustomerOption();
				if(!custOption.isActive()) {
					continue;
				}
				CustomerOption customerOption = options.get(custOption.getId());
				
				optionPopulator.setOptionSet(optSet);
				
				
				
				if(customerOption==null) {
					customerOption = new CustomerOption();
					customerOption.setId(custOption.getId());
					customerOption.setType(custOption.getCustomerOptionType());
					customerOption.setName(custOption.getDescriptionsSettoList().get(0).getName());
					
				} 
				
				optionPopulator.populate(custOption, customerOption, store, language);
				options.put(customerOption.getId(), customerOption);

				if(!CollectionUtils.isEmpty(customerAttributes)) {
					for(CustomerAttribute customerAttribute : customerAttributes) {
						if(customerAttribute.getCustomerOption().getId().longValue()==customerOption.getId()){
							CustomerOptionValue selectedValue = new CustomerOptionValue();
							com.salesmanager.core.business.customer.model.attribute.CustomerOptionValue attributeValue = customerAttribute.getCustomerOptionValue();
							selectedValue.setId(attributeValue.getId());
							CustomerOptionValueDescription optValue = attributeValue.getDescriptionsSettoList().get(0);
							selectedValue.setName(optValue.getName());
							customerOption.setDefaultValue(selectedValue);
							if(customerOption.getType().equalsIgnoreCase(CustomerOptionType.Text.name())) {
								selectedValue.setName(customerAttribute.getTextValue());
							} 
						}
					}
				}
			}
		}
		
		
		model.addAttribute("options", options.values());

		
	}
	*/
		
	/**
	 * List of customers
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/list.html",  method={RequestMethod.GET,RequestMethod.POST})
	public String displayCustomers(@Valid @ModelAttribute("criteria") CustomerCriteria criteria, @RequestParam(value = "page", defaultValue = "1") final int page,Model model,HttpServletRequest request) throws Exception {
		PaginationData paginaionData=createPaginaionData(page,10);
		//CustomerCriteria criteria = new CustomerCriteria();
		if(criteria == null) criteria = new CustomerCriteria();
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		CustomerList customerList = customerService.listByCriteria(criteria);
		
		if(customerList.getCustomers()!=null) {
			model.addAttribute("customerList",customerList);
			model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData,Constants.MAX_ORDERS_PAGE, customerList.getTotalCount()));
		}else{
			model.addAttribute( "paginationData", null);
		}
		Map <String,Integer> cmap = (Map <String,Integer>)request.getSession().getServletContext().getAttribute("custmoerGradeMap");
		if(cmap != null){
			model.addAttribute("gradeMap",cmap);
		}
		if(criteria.getAnonymous() !=null && criteria.getAnonymous()!=""){
			model.addAttribute("cstatus",criteria.getAnonymous());
		}
		model.addAttribute("criteria",criteria);
		this.setMenu(model, request, "customer-list");
	
		return "admin-customers";
		
	}
	
	/**
	 * find customers
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/**
	@RequestMapping(value="/admin/customers/find.html", method=RequestMethod.GET)
	public String findCustomers( @Valid @ModelAttribute("criteria") CustomerCriteria criteria,@RequestParam(value = "page", defaultValue = "1") final int page,Model model,HttpServletRequest request) throws Exception {
		PaginationData paginaionData=createPaginaionData(page,10);
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		CustomerList customerList = customerService.listByCriteria(criteria);
		
		if(customerList.getCustomers()!=null) {
			model.addAttribute("customerList",customerList);
			model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData,Constants.MAX_ORDERS_PAGE, customerList.getTotalCount()));
		}else{
			model.addAttribute( "paginationData", null);
		}
		Map <String,Integer> cmap = (Map <String,Integer>)request.getSession().getServletContext().getAttribute("custmoerGradeMap");
		if(cmap != null){
			model.addAttribute("gradeMap",cmap);
		}
		this.setMenu(model, request, "customer-list");
	
		return "admin-customers";
		
	}*/
	/**
	 * active customer	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/active.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String activeCustomer(HttpServletRequest request) throws Exception {
		String customerId = request.getParameter("customerId");
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		int result = emailTemplatesUtils.sendActiveEmail(customerId, store, request.getContextPath());
		if(result == 0){
			resp.setStatus(1);
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
	}
		
	/**
	 * freeze customer	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/freeze.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String freezeCustomer(HttpServletRequest request) throws Exception {
		String customerId = request.getParameter("customerId");
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		int result = emailTemplatesUtils.sendFreezeMail(customerId, store, request.getContextPath());
		if(result==0){
			resp.setStatus(2);
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
	}
	
	/**
	 * unfreeze customer	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/unfreeze.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String unfreezeCustomer(HttpServletRequest request) throws Exception {
		String customerId = request.getParameter("customerId");
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		int result = emailTemplatesUtils.sendUnFreezeMail(customerId, store, request.getContextPath());
		if(result==0){
			resp.setStatus(3);
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
	}
	
	/**
	 * unfreeze customer	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/setGrade.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String setGrade(HttpServletRequest request) throws Exception {
		String customerId = request.getParameter("customerId");
		AjaxResponse resp = new AjaxResponse();
		try{
			Long id = Long.parseLong(customerId);
			Customer customer = customerService.getById(id);
			if(customer==null) {
				resp.setErrorString("Customer does not exist");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}	
			customer.setGrade(Integer.parseInt(request.getParameter("garde")));
			customerService.saveOrUpdate(customer);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		}catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return resp.toJSONString();
	}
	
	/**
	 * unfreeze customer	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/batchResetPassword.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String batchResetPassword(HttpServletRequest request) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		try{
			String [] items = request.getParameter("itmes").split(",");
			
			
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			String contextPath= request.getContextPath();
			ArrayList<String> erros= new ArrayList<String>();
			for(int j=0;j<items.length;j++){
				int result = emailTemplatesUtils.sendSetPasswordMail(items[j], store, contextPath);
				if(result==-1) {
					erros.add(items[j]);
				}	
			}
			if(erros.size()==items.length){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				
			}else{
				if(erros.size()>0){
					StringBuffer errorNams = new StringBuffer();
					errorNams.append(erros.get(0));
					for(int k=1;k<erros.size();k++){
						errorNams.append(",").append(erros.get(k));
					}
					HashMap<String, String> erronames = new HashMap<String, String>();
					erronames.put("erronames", erronames.toString());
					resp.setDataMap(erronames);
				}
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
		}catch (Exception e) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return resp.toJSONString();
	}
	
	/**
	 * Bacth active	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/batchActive.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String batchActive(HttpServletRequest request) throws Exception {
		return batchOperate(request,0);
	}
	
	/**
	 * Bacth freeze	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/batchFreeze.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String batchFreeze(HttpServletRequest request) throws Exception {
		return batchOperate(request,1);
	}
	
	/**
	 * Bacth freeze	 * 
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/batchUnFreeze.html", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody String batchUnFreeze(HttpServletRequest request) throws Exception {
		return batchOperate(request,2);
	}
	
	private String batchOperate(HttpServletRequest request,int code){
		AjaxResponse resp = new AjaxResponse();
		try{
			String [] items = request.getParameter("itmes").split(",");
			
			
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			String contextPath= request.getContextPath();
			ArrayList<String> erros= new ArrayList<String>();
			ArrayList<String> succes= new ArrayList<String>();
			for(int j=0;j<items.length;j++){
				int result =0;
				switch (code) {
				//active
				case 0:
					result =this.emailTemplatesUtils.sendActiveEmail(items[j], store, contextPath);
					break;
				case 1:
					result =this.emailTemplatesUtils.sendFreezeMail(items[j], store, contextPath);
					break;
				default:
					result =this.emailTemplatesUtils.sendUnFreezeMail(items[j], store, contextPath);
					break;
				}
				
				if(result==-1) {
					erros.add(items[j]);
				//已激活的不需要加入
				}else if(result==0){
					succes.add(items[j]);
				}
			}
			if(erros.size()==items.length){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				
			}else{
				HashMap<String, String> erronames = new HashMap<String, String>();
				//报错错误的ID
				if(erros.size()>0){
					StringBuffer errorNams = new StringBuffer();
					errorNams.append(erros.get(0));
					for(int k=1;k<erros.size();k++){
						errorNams.append(",").append(erros.get(k));
					}
					erronames.put("erronames", erronames.toString());
				}
				if(succes.size()>0){
				StringBuffer succesNams = new StringBuffer();
					succesNams.append(succes.get(0));
					for(int k=1;k<succes.size();k++){
						succesNams.append(",").append(succes.get(k));
					}
					erronames.put("succesname", succesNams.toString());
				}
				//test
				//erronames.put("erronames", items[0]);
				//erronames.put("succesname", items[0]);
				resp.setDataMap(erronames);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
		}catch (Exception e) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return resp.toJSONString();
	}
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/resetPassword.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody
	String resetPassword(HttpServletRequest request,HttpServletResponse response) {
		
		String customerId = request.getParameter("customerId");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();
		
		int result =emailTemplatesUtils.sendSetPasswordMail(customerId,store,request.getContextPath());
		if(result==0){
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
		
		
	}
	
	
	private void setMenu(Model model, HttpServletRequest request, String activeMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();		
		activeMenus.put("customer", "customer");
		activeMenus.put(activeMenu, activeMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("customer");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
    
}
