/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.bettbio.core.common.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 基础控制器
 * <p>
 * User:
 * <p>
 * Date: 13-2-23 下午3:56
 * <p>
 * Version: 1.0
 */
public abstract class BaseController {

	protected static final String PAGE_NUM = "page";
	protected static final String PAGE_SIZE = "row";
	
	private String viewPrefix;

	protected Map<String, Object> paramMap = null;

	protected BaseController() {
		setViewPrefix(defaultViewPrefix());
	}

	/**
	 * 设置通用数据
	 *
	 * @param model
	 */
	protected void setCommonData(Model model) {
	}

	/**
	 * 当前模块 视图的前缀 默认 1、获取当前类头上的@RequestMapping中的value作为前缀 2、如果没有就使用当前模型小写的简单类名
	 */
	public void setViewPrefix(String viewPrefix) {
//		if (viewPrefix.startsWith("/")) {
//			viewPrefix = viewPrefix.substring(1);
//		}
		this.viewPrefix = viewPrefix;
	}

	public String getViewPrefix() {
		return viewPrefix;
	}

	/**
	 * 获取视图名称：即prefixViewName + "/" + suffixName
	 *
	 * @return
	 */
	public String viewName(String suffixName) {
		if (!suffixName.startsWith("/")) {
			suffixName = "/" + suffixName;
		}
		return getViewPrefix() + suffixName;
	}

	/**
	 * @param backURL
	 *            null 将重定向到默认getViewPrefix()
	 * @return
	 */
	protected String redirectToUrl(String backURL) {
		if (StringUtils.isEmpty(backURL)) {
			backURL = getViewPrefix();
		}
		if (!backURL.startsWith("/") && !backURL.startsWith("http")) {
			backURL = "/" + backURL;
		}
		return "redirect:" + backURL;
	}

	protected String defaultViewPrefix() {
		String currentViewPrefix = "";
		RequestMapping requestMapping = AnnotationUtils.findAnnotation(getClass(), RequestMapping.class);
		if (requestMapping != null && requestMapping.value().length > 0) {
			currentViewPrefix = requestMapping.value()[0];
		}

		return currentViewPrefix;
	}

	protected Map<String, Object> getRequestParameter(HttpServletRequest request) {
		paramMap = new HashMap<String, Object>(16);
		StringBuffer buffer = new StringBuffer();
		for (Enumeration<String> enumeration = request.getParameterNames(); enumeration.hasMoreElements();) {
			String key = enumeration.nextElement();
			String val = request.getParameter(key);
			if(key.equals("page")||key.equals("row")){
				paramMap.put(key, Integer.parseInt(val));
			}else{
				if(StringUtils.isEmpty(val)) continue;
				paramMap.put(key, val);
				buffer.append("&").append(key).append("=").append(val);
			}
		}
		if(StringUtils.isEmpty(paramMap.get("page"))){
			paramMap.put("page",1);
			paramMap.put("row", 10);
		}
		if(buffer.toString().length()>0){
			paramMap.put("pageUrl",buffer.toString());
		}
		return paramMap;
	}
	
}
