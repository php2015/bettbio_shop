package com.bettbio.shop.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.service.permission.PStoreUserService;

import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class StoreUserFilter extends PathMatchingFilter {

	@Autowired
	PStoreUserService pStoreUserService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String account = (String)SecurityUtils.getSubject().getPrincipal();
//      request.setAttribute(Constants.CURRENT_USER, pStoreUserService.findByAccount(account));
        
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getServletPath();
        if(path.indexOf(".")!=-1){
    		path = path.substring(0, path.indexOf("."));
        }
		
        Set<String> resources= pStoreUserService.findResources(account);
        if(!resources.contains(path)){
        	String requestType = req.getHeader("X-Requested-With");
        	if (requestType != null && requestType.equals("XMLHttpRequest")) {
        		//如果是ajax请求
        		WebUtils.issueRedirect(req, response, "/error/ajaxUnauthorized");
            } else {
        		WebUtils.issueRedirect(req, response, "/error/unauthorized");
            }
        	return false;
        }
        return true;
    }
}
