package com.salesmanager.web.services.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.web.admin.security.SecurityDataAccessException;


/**
 * 
 * @author casams1
 *         http://stackoverflow.com/questions/5105776/spring-security-with
 *         -custom-user-details
 */
@Service("customerDetailsService")
public class CustomerServicesImpl implements UserDetailsService{

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServicesImpl.class);
	
	@Autowired
	private CustomerService customerService;
	

	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	private void handleSmsCodeRetry(HttpServletRequest request) {
		String loginType = request.getParameter("type");
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

		Customer customer = null;
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		// once read, invalid
		String phonecode = (String) request.getSession().getAttribute("phonecode");
		String logonMobile = (String) request.getSession().getAttribute("sendToMobile");
		handleSmsCodeRetry(request);

		try {

				customer = customerService.getByID(userName);
			
				if(customer==null) {
					return null;
				}
	
			
			List<Integer> groupsId = new ArrayList<Integer>();
			List<Group> groups = customer.getGroups();
			
			for(Group group : groups) {
				groupsId.add(group.getId());
			}
			
	
			if(CollectionUtils.isNotEmpty(groupsId)) {
		    	List<Permission> permissions = permissionService.getPermissions(groupsId);
		    	for(Permission permission : permissions) {
		    		GrantedAuthority auth = new GrantedAuthorityImpl(permission.getPermissionName());
		    		authorities.add(auth);
		    	}
			}
			

			

		
		
		} catch (ServiceException e) {
			LOGGER.error("Exception while querrying customer",e);
			throw new SecurityDataAccessException("Cannot authenticate customer",e);
		}
		
		String authPassoword = customer.getPassword();
		String logonType = (String) request.getParameter("type");
		request.getSession().setAttribute("logonType", logonType);
		if (logonType != null && logonType.equals("sms")){
			if (logonMobile == null || !logonMobile.equals(customer.getPhone())){
				return null;
			}
			authPassoword = passwordEncoder.encodePassword(phonecode, null);
		}

		User authUser = new User(userName, authPassoword, true, true,
				true, true, authorities);
		
		return authUser;
		
		
	}
	




}
