package com.salesmanager.web.admin.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.salesmanager.core.business.common.model.UserLogin;
import com.salesmanager.core.business.common.service.UserLoginService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.web.utils.GeoLocationUtils;

public class UserAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserLoginService userLoginService;
	
	  @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		  // last access timestamp
		  String userName = authentication.getName();
		  
		  try {
			  User user = userService.getByUserName(userName);
			  
			  Date lastAccess =   new Date();
			  userService.saveOrUpdate(user);
			  UserLogin userLogin  = new UserLogin();
			  userLogin.setUser(user);
			  userLogin.setDateCreated(lastAccess);
			  user.setLastAccess(lastAccess);
			//  user.setLoginTime(new Date());
			  //get ip
			  String ip = GeoLocationUtils.getClientIpAddress(request);
			  if(ip!=null || !ip.equalsIgnoreCase("")){
				  userLogin.setIp(ip);
			  }
			  userLoginService.saveOrUpdate(userLogin);			  
			  response.sendRedirect(request.getContextPath() + "/admin/home.html");
		  } catch (Exception e) {
			 response.sendRedirect(request.getContextPath() + "shop/customer/customLogon.html");
		  }
	   }

}
