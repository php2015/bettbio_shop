package com.bettbio.shop.web.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.exception.NoauthorizedException;
import com.bettbio.core.common.web.interceptor.AbstractHandlerInterceptorAdapter;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.permission.bo.StoreUser;
import com.bettbio.core.service.permission.PStoreUserService;
import com.bettbio.shop.web.exception.NoLoginException;

public class AdminHandlerInterceptor extends AbstractHandlerInterceptorAdapter {

	@Autowired
	PStoreUserService pStoreUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(isInclude(request)){
			//拦截到的URL
			
//			if (isExclude(request)) {
//				return true;
//			}
			if(!isLogin(request)){
				//直接返回 或者 抛出异常在异常拦截器处理
				throw new NoLoginException(Constants.ADMIN_NOLOGIN_CODE);
//				request.getRequestDispatcher("/storelogin").forward(request, response);
//				return false;
			}

//			String path = request.getServletPath();
//	        if(path.indexOf(".")!=-1){
//	    		path = path.substring(0, path.indexOf("."));
//	        }
			
//	        String account = ((SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER)).getAccount();
//			Set<String> resources= pStoreUserService.findResources(account);
//	        
//	        if(!resources.contains(path)){
//	        	throw new NoauthorizedException(Constants.NO_AUTHORIZED_EXCEPTION, null);
//	        }
			return true;
		}
		return true;
	}
}
