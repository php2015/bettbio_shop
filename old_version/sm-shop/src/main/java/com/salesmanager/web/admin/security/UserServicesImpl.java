package com.salesmanager.web.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.web.constants.Constants;


/**
 * 
 * @author casams1
 *         http://stackoverflow.com/questions/5105776/spring-security-with
 *         -custom-user-details
 */
@Service("userDetailsService")
public class UserServicesImpl implements WebUserServices{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServicesImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	

	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	private void handleSmsCodeRetry(HttpServletRequest request) {
		String loginType = request.getParameter("logonType");
		if (!"sms".equals(loginType)){
			return;
		}
		Integer smsCodeRetryTimes = (Integer) request.getSession().getAttribute("smsCodeRetryTimes");
		if (smsCodeRetryTimes == null){
			smsCodeRetryTimes = 0;
		}
		smsCodeRetryTimes++;
		if (smsCodeRetryTimes >= GlobalConstants.SMS_LOGIN_RETRY_TIMES){
			smsCodeRetryTimes = 0;
			request.getSession().removeAttribute("phonecode");
			request.getSession().removeAttribute("sendToMobile");
		}
		request.getSession().setAttribute("smsCodeRetryTimes", smsCodeRetryTimes);
	}
	
	@SuppressWarnings("deprecation")
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {

		com.salesmanager.core.business.user.model.User user = null;
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String phonecode= (String) request.getSession().getAttribute("phonecode");
		String logonMobile = (String) request.getSession().getAttribute("sendToMobile");
		
		handleSmsCodeRetry(request);
		
		try {


			user = userService.getByUserName(userName);
			if(user==null) {
				user = userService.getByUserMobile(userName);
			}
			if (user == null) {
				user = userService.queryUserByEmail(userName);
			}
			setLogFailCase(userName, user);
			if (user == null){
				return null;
			}
			
			userName = user.getAdminName();

			GrantedAuthority role = new GrantedAuthorityImpl(Constants.PERMISSION_AUTHENTICATED);//required to login
			authorities.add(role);
	
			List<Integer> groupsId = new ArrayList<Integer>();
			addBuyerRoles(user, groupsId);
			List<Group> groups = user.getGroups();
			for(Group group : groups) {
				groupsId.add(group.getId());
			}
	
	    	
	    	List<Permission> permissions = permissionService.getPermissions(groupsId);
	    	for(Permission permission : permissions) {
	    		GrantedAuthority auth = new GrantedAuthorityImpl(permission.getPermissionName());
	    		authorities.add(auth);
	    	}
    	
		} catch (Exception e) {
			LOGGER.error("Exception while querrying user",e);
			throw new SecurityDataAccessException("Exception while querrying user",e);
		}
		
		
		
		String authPassoword = user.getAdminPassword();
		String logonType = (String) request.getParameter("logonType");
		request.getSession().setAttribute("logonType", logonType);
		if (logonType != null && logonType.equals("sms")){
			if (logonMobile == null || !logonMobile.equals(user.getMobile())){
				return null;
			}
			authPassoword = passwordEncoder.encodePassword(phonecode, null);
		}
		
		User secUser = new User(userName, authPassoword, user.isActive(), true,
				true, true, authorities);
		return secUser;
	}
	
	
	private void setLogFailCase(String userName, com.salesmanager.core.business.user.model.User user) {
		Customer customer = customerService.getByPhone(userName);
		if (customer == null){
			customer = customerService.getByEmail(userName);
		}
		if (customer == null){
			request.getSession().setAttribute("sellerLogonError", user==null?"no_buyer_no_seller":"no_buyer_maybe_seller");
		}else{
			request.getSession().setAttribute("sellerLogonError", user==null?"maybe_buyer_no_seller":"maybe_buyer_maybe_seller");
		}
	}


	private void addBuyerRoles(com.salesmanager.core.business.user.model.User user, List<Integer> groupsId) {
		String mobile = user.getMobile();
		if (mobile == null){
			return;
		}
		Customer customer = customerService.getByPhone(mobile);
		if (customer == null){
			return;
		}
		for(Group group : customer.getGroups()) {
			groupsId.add(group.getId());
		}
	}


	public void createDefaultAdmin() throws Exception {
		
		  //TODO create all groups and permissions
		
		  MerchantStore store = merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);

		  String password = passwordEncoder.encodePassword("password", null);
		  
		  List<Group> groups = groupService.listGroup(GroupType.ADMIN);
		  
		  //creation of the super admin admin:password)
		  com.salesmanager.core.business.user.model.User user = new com.salesmanager.core.business.user.model.User("admin",password,"admin@shopizer.com");
		  user.setFirstName("Administrator");
		  user.setLastName("User");
		  
		  for(Group group : groups) {
			  if(group.getGroupName().equals(Constants.GROUP_SUPERADMIN) || group.getGroupName().equals(Constants.GROUP_ADMIN)) {
				  user.getGroups().add(group);
			  }
		  }

		  user.setMerchantStore(store);		  
		  userService.create(user);
		
		
	}



}
