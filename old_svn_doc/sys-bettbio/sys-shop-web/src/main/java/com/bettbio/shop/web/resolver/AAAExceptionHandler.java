package com.bettbio.shop.web.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.exception.NoauthorizedException;
import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.shop.web.exception.NoLoginException;

public class AAAExceptionHandler implements HandlerExceptionResolver {

	private String redirectPrefix = "redirect: ";

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		ModelAndView mv = new ModelAndView();
		mv.addObject("ex", ex);
		mv.setViewName("/error/error");// 默认走程序错误页面

		String ctxPath = request.getContextPath();
		String requestType = request.getHeader("X-Requested-With");// ajax标识

		if (ex instanceof NotLoginEXception) {// App未登录
			if (isAjax(requestType)) {
				// 如果是ajax请求
				mv.setViewName(redirectViewName(ctxPath, "/error/ajaxnologin"));
			} else {
				mv.setViewName(redirectViewName(ctxPath, "/login"));
			}
		} else if (ex instanceof NoLoginException) { // PC未登录
			if (isAjax(requestType)) {
				// 如果是ajax请求
				mv.setViewName("redirect: " + request.getContextPath() + "/error/ajaxnologin");
			} else {
				NoLoginException noLoginException = (NoLoginException) ex;
				if (noLoginException.getCode() == Constants.ADMIN_NOLOGIN_CODE) {
					mv.setViewName(redirectViewName(ctxPath, "/storelogin"));
				}else if (noLoginException.getCode() == Constants.MANAGE_NOLOGIN_CODE) {
					mv.setViewName(redirectViewName(ctxPath, "/joblogin"));
				}
			}
		} else if (ex instanceof NoauthorizedException) { // PC操作没有权限
			if (isAjax(requestType)) {
				mv.addObject("ajaxResponse", AjaxResponse.fail("很抱歉!您没有权限执行该操作!"));
				mv.setViewName("error/ajaxunauthorized");
			} else {
				mv.addObject("ex", ex);
				mv.setViewName("error/unauthorized");
			}
		} else if (isAjax(requestType)) { //默认ajax 异常
			mv.addObject("ajaxResponse", AjaxResponse.fail("操作失败!"));
			mv.setViewName("error/ajax");
		}

		return mv;
	}

	public String getRedirectPrefix() {
		return redirectPrefix;
	}

	public void setRedirectPrefix(String redirectPrefix) {
		this.redirectPrefix = redirectPrefix;
	}

	public String redirectViewName(String ctxPath, String viewName) {
		return redirectPrefix + ctxPath + viewName;
	}

	// 是否是Ajax
	private boolean isAjax(String requestType) {
		return requestType != null && requestType.equals("XMLHttpRequest");
	}
}