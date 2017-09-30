package com.bettbio.shop.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.interceptor.AbstractHandlerInterceptorAdapter;
import com.bettbio.shop.web.exception.NoLoginException;

public class ManagerHandlerInterceptor extends AbstractHandlerInterceptorAdapter {
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(isInclude(request)){
			//拦截到的URL
			
//			if (isExclude(request)) {
//				return true;
//			}
			if(!isLogin(request)){
				throw new NoLoginException(Constants.MANAGE_NOLOGIN_CODE);
//				request.getRequestDispatcher("/joblogin").forward(request, response);
//				return false;
			}
			
			return true;
		}
		return true;
	}
}
