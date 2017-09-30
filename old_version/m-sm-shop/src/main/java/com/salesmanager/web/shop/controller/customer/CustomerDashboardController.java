package com.salesmanager.web.shop.controller.customer;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.MemberPointsService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;

/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerDashboardController extends AbstractController {
	
	//customer的option不知道有什么用，先屏蔽
	//@Autowired
    //private AuthenticationManager customerAuthenticationManager;
	
	//customer的option不知道有什么用，先屏蔽
	//@Autowired
	//private CustomerOptionSetService customerOptionSetService;
	
	//add by cy
	@Autowired
	OrderService orderService;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	
	@Autowired
	private MemberPointsService memberPointsService;
	
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/dashboard.html", method=RequestMethod.GET)
	public String displayCustomerDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    
	    
		Customer customer = (Customer)request.getAttribute(Constants.CUSTOMER);
		//customer的option不知道有什么用，先屏蔽
		//Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		//getCustomerOptions(model, customer, store, language);
		long totalorders = orderService.getTotal(customer);
		OrderCriteria criteria = new OrderCriteria();
		criteria.setcId(customer.getId());
		double totalordermoney = orderService.getTotalMoney(criteria);
		//ReadableOrderList readable= orderFacade.getReadableOrderList(customer, 0,3, language);
		Map <String,Integer> cmap = (Map <String,Integer>)request.getSession().getServletContext().getAttribute("custmoerGradeMap");
		BigDecimal grade = new BigDecimal(cmap.get(String.valueOf(customer.getGrade())));
		double usedordermoney = orderService.getTotalMoney(customer);
        BigDecimal total = new BigDecimal(usedordermoney);
        model.addAttribute( "left", grade.subtract(total));
		
		 /** there must be a cart in the session **/
        String cartCode = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
              
        ShoppingCartData shoppingCart = shoppingCartFacade.getShoppingCartData(customer, store, cartCode);
        if(shoppingCart !=null){
        	 model.addAttribute("quantity",shoppingCart.getQuantity());
        }else{
        	 model.addAttribute("quantity",0);
        }
        if(customer !=null){
       	   model.addAttribute( "customer",  customer);
              model.addAttribute( "queryByMemberPoints",memberPointsService.queryByMemberPoints(customer.getId()));
          }
      	
       
		model.addAttribute("section","dashboard");
		model.addAttribute("orders", totalorders);
		model.addAttribute("total", totalordermoney);
		model.addAttribute("customer", customer);
		// model.addAttribute( "customerOrders", readable);
		 model.addAttribute("activeMenu","dashboard");
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	//customer的option不知道有什么用，先屏蔽
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
				if(!custOption.isActive() || !custOption.isPublicOption()) {
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
	

}
