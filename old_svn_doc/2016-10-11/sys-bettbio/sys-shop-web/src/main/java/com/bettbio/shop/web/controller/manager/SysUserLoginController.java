package com.bettbio.shop.web.controller.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.enums.Delete;
import com.bettbio.core.enums.Disable;
import com.bettbio.core.model.SysUser;
import com.bettbio.core.service.SysUserService;

/**
 * 百图员工登录
 * @author GuoChunbo
 *
 */
@Controller
public class SysUserLoginController extends BaseController {

	@Autowired
	SysUserService sysUserService;
	
	private String loginUrl = "/jobLogin";
	
	private String loginSuccessUrl = "/manager/index";
	
	@RequestMapping("/jobLogin")
	public String login(SysUser sysUser,ModelMap modelMap,HttpServletRequest request){

		SysUser tempUser = sysUserService.findSysUserByLogin(sysUser);
		
		if(tempUser==null){
			modelMap.addAttribute(Constants.ERROR, "改帐号不存在!");//不存在
		}else if(tempUser.getIsDisable()==Disable.DISABLE.getVal()){
			modelMap.addAttribute(Constants.ERROR, "该帐号已禁用!");//已禁用
		}else if(tempUser.getIsDeleted()==Delete.DELETE.getVal()) {
			modelMap.addAttribute(Constants.ERROR, "该帐号已删除!");//已删除
		}
		
		if(modelMap.containsKey(Constants.ERROR)) return loginUrl;
		
		sysUser = sysUserService.login(sysUser);//登录
		
		if(sysUser==null){
			modelMap.addAttribute(Constants.ERROR, "密码不正确!");
		}else{
			request.getSession().setAttribute(Constants.USER_TYPE, 3);
			request.getSession().setAttribute(Constants.CURRENT_USER, sysUser);
			request.getSession().setAttribute(Constants.CURRENT_USERNAME, sysUser.getName());
			return loginSuccessUrl;
		}
		
		return loginUrl;
	}
}
