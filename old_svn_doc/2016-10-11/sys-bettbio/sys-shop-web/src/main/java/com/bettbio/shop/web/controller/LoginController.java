package com.bettbio.shop.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.SUser;
import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.StoreUserService;
import com.bettbio.core.service.SubOrderService;
import com.bettbio.shop.base.controller.BaseAdminController;



@Controller
public class LoginController extends BaseAdminController{

	@Autowired
	SUserService sUserService;
	
	@Autowired
	StoreUserService storeUserService;
	@Autowired
	SubOrderService subOrderService;
	/**
	 * 买家用户登录
	 * @param request
	 * @param model
	 * @param sUser
	 * @param result
	 * @return
	 */
	@RequestMapping("/userLogin")
	public String userLogin(HttpServletRequest request, ModelMap model,@Valid SUser sUser, 
			BindingResult result){
		sUser = sUserService.login(sUser);
		if(sUser != null){
			request.getSession().setAttribute(Constants.USER_TYPE, 1);
			request.getSession().setAttribute(Constants.CURRENT_USER, sUser);
			request.getSession().setAttribute(Constants.CURRENT_USERNAME, sUser.getName());
			return "/index";
		}else{
			model.addAttribute("errorMess", "用户名或密码错误");
			model.addAttribute("class","active");
			return "/login";
		}
	}
	
	
	/**
	 * 买家快速登录
	 */
	@RequestMapping("/shortcutlogin")
	public String shortcutlogin(HttpServletRequest request,SUser sUser,ModelMap model){
		sUser = sUserService.selectByPhone(sUser);
		if(sUser != null){
			request.getSession().setAttribute(Constants.USER_TYPE, 1);
			request.getSession().setAttribute(Constants.CURRENT_USER, sUser);
			request.getSession().setAttribute(Constants.CURRENT_USERNAME, sUser.getName());
			return "/index";
		}else{
			model.addAttribute("mess", "手机号不存在");
			return "/login";
		}
	}
	
	/**
	 * 卖家用户登录
	 * @throws NotLoginEXception 
	 */
	@RequestMapping("/adminLogin")
	public String adminLogin(@Valid SStoreUser sStoreUser,HttpServletRequest request, ModelMap model) throws NotLoginEXception{
		sStoreUser = storeUserService.adminLogin(sStoreUser);
		if(sStoreUser != null){
			/*UsernamePasswordToken token = new UsernamePasswordToken(sStoreUser.getAccount(),sStoreUser.getParentCode(),false);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);*/
			request.getSession().setAttribute(Constants.USER_TYPE, 2);
			request.getSession().setAttribute(Constants.CURRENT_USER, sStoreUser);
			request.getSession().setAttribute(Constants.CURRENT_USERNAME, sStoreUser.getName());
			return adminHome(request,model);
		}else{
			model.addAttribute("adminErrorMess", "用户名或密码错误");
			model.addAttribute("class","active");
			return "/storelogin";
		}
	}
	
	/**
	 * 卖家快速登录
	 * @param request
	 * @param model
	 * @return
	 * @throws NotLoginEXception 
	 */
	@RequestMapping("/adminShortcutlogin")
	public String adminShortcutlogin(HttpServletRequest request,SStoreUser sStoreUser,ModelMap model) throws NotLoginEXception{
		sStoreUser = storeUserService.selectByPhone(sStoreUser);
		if(sStoreUser != null){
	/*		UsernamePasswordToken token = new UsernamePasswordToken(sStoreUser.getAccount(),sStoreUser.getParentCode(),false);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);*/
			request.getSession().setAttribute(Constants.USER_TYPE, 2);
			request.getSession().setAttribute(Constants.CURRENT_USER, sStoreUser);
			request.getSession().setAttribute(Constants.CURRENT_USERNAME, sStoreUser.getName());
			return adminHome(request,model);
		}else{
			model.addAttribute("mess", "手机号不存在");
			return "/storelogin";
		}
	}
	
	/**
	 * 商家首页
	 * @param request
	 * @param model
	 * @return
	 * @throws NotLoginEXception
	 */
	@RequestMapping("/adminHome")
	public String adminHome(HttpServletRequest request,ModelMap model) throws NotLoginEXception{
		paramMap=getRequestParameter(request);
		String storeCode=getCurrentUser(request).getStoreCode();
		paramMap.put("storeCode",storeCode);
		model.put("pageInfo",subOrderService.selectSubOrdersByMap(paramMap));
		model.put("orderCount", subOrderService.selectOrderCountByCode(storeCode));
		model.put("countPrice", subOrderService.selectCountPriceByCode(storeCode));
		model.addAttribute("paramMap", paramMap);
		return "/admin/index";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,ModelMap model){
		request.getSession().removeAttribute(Constants.CURRENT_USER);
		return "redirect:/index";
	}
}
