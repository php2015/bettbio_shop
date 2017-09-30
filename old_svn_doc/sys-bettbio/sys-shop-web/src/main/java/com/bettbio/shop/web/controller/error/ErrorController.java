package com.bettbio.shop.web.controller.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.exception.NoauthorizedException;
import com.bettbio.core.common.web.validate.AjaxResponse;

@Controller
@RequestMapping("/error")
public class ErrorController {

	/**
	 * 没有权限普通请求错误
	 * 
	 * @return
	 */
	@RequestMapping("/unauthorized")
	public String unauthorized(HttpServletRequest request) {
		request.setAttribute("ex", new NoauthorizedException(Constants.NO_AUTHORIZED_EXCEPTION, null));
		return "/error/unauthorized";
	}

	/**
	 * 没有权限ajax错误
	 * 
	 * @return
	 */
	@RequestMapping("/ajaxUnauthorized")
	public @ResponseBody AjaxResponse ajaxUnauthorized(HttpServletRequest request) {
		return AjaxResponse.fail("很抱歉!您没有权限执行该操作!");
	}

	/**
	 * 普通ajax错误
	 * 
	 * @return
	 */
	@RequestMapping("/ajax")
	public @ResponseBody AjaxResponse ajaxErr(HttpServletRequest request) {

		return AjaxResponse.fail("操作失败");
	}
	
	
	@RequestMapping("/ajaxnologin")
	public @ResponseBody AjaxResponse ajaxNologin(HttpServletRequest request){
		
		return AjaxResponse.nologin("请先登录");
	}

//	@RequestMapping("/nologin")
//	public String nologin(){
//		return "";
//	}
}
