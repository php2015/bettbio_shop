package com.bettbio.shop.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.model.SUser;

/**
 * 业务层父控制器
 * @author chang
 *
 */
public abstract class BaseShopController extends BaseController{
	
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
	public SUser getCurrentUser(HttpServletRequest request) throws NotLoginEXception{
		Object obj=request.getSession().getAttribute(Constants.CURRENT_USER);
		if(obj!=null){
			return  (SUser)obj;
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
			return  ((SUser)obj).getCode();
		}else{
			throw new NotLoginEXception("用户未登录");
		}
	}
	
	
  public void setHeder(HttpServletResponse hsr){
	  hsr.setHeader("Access-Control-Allow-Origin", "*");
  }
}
