package com.bettbio.shop.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.common.web.interceptor.AbstractHandlerInterceptorAdapter;

public class AppHandlerInterceptor extends AbstractHandlerInterceptorAdapter {
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(isInclude(request)){
			//拦截到的URL
			setHeaderAccessControlAllowOrigin(response);
			
//			if (isExclude(request)) {
//				return true;
//			}
//			if(!isLogin(request)){
//				
//				throw new NotLoginEXception("用户未登录!");
//			}
			return true;
		}
		return true;
	}

	public void setHeaderAccessControlAllowOrigin(final HttpServletResponse hsr) {
		hsr.setHeader("Access-Control-Allow-Origin", "*");
	}
}
