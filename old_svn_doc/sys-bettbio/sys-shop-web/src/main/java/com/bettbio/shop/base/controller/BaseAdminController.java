package com.bettbio.shop.base.controller;

import javax.servlet.http.HttpServletRequest;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.model.SStoreUser;

/**
 * 后台控制器父类
 * @author chang
 *
 */
public class BaseAdminController extends BaseController {
	/**
	 * 确认登陆
	 * @param request
	 * @return
	 */
	public boolean isLanding(HttpServletRequest request){
		Object obj=request.getSession().getAttribute(Constants.CURRENT_USER);
		if(obj!=null){
			return true;
		}
		return false;
	}
	/**
	 * 获取当前登陆用户
	 * @param request
	 * @return
	 * @throws NotLoginEXception 
	 */
	public SStoreUser getCurrentUser(HttpServletRequest request) throws NotLoginEXception{
		Object obj=request.getSession().getAttribute(Constants.CURRENT_USER);
		if(obj!=null){
			return  (SStoreUser)obj;
		}else{
			throw new NotLoginEXception("用户未登录");
		}
	}
	
	/**
	 * 获取当前用户编号 
	 * @param request
	 * @return
	 * @throws NotLoginEXception 
	 */
	public String getCurrentUserCode(HttpServletRequest request) throws NotLoginEXception{
		Object obj=request.getSession().getAttribute(Constants.CURRENT_USER);
		if(obj!=null){
			return  ((SStoreUser)obj).getCode();
		}else{
			throw new NotLoginEXception("用户未登录");
		}
	}
	
	
}
