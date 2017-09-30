package com.bettbio.shop.web.controller.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.common.web.utils.SmsUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SUser;
import com.bettbio.core.model.SUserPoints;
import com.bettbio.core.service.InvoiceInfoService;
import com.bettbio.core.service.SUserPointsService;
import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.UserAddressService;
import com.bettbio.shop.base.controller.BaseShopController;

/**
 * 用户信息控制器
 * 
 * @author chang
 *
 */
@Controller("appUserInfoController")
@RequestMapping("app")
public class UserInfoController extends BaseShopController {
	@Autowired
	SUserService userService;
	@Autowired
	SUserPointsService SUserPointsService;
	@Autowired
	UserAddressService userAddressService;
	@Autowired
	InvoiceInfoService invoiceInfoService;

	/**
	 * 个人中心
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping("/personalCenter")
	@ResponseBody
	public SUser personalCenter(HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		
		return userService.selectByKey(getCurrentUser(request).getId());
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/updateUser")
	@ResponseBody
	public AjaxResponse updateUser(SUser user,HttpServletResponse hsr) {
		
		int i = userService.updateNotNull(user);
		if (i == 1) {
			return AjaxResponse.success("操作成功");
		} else {
			return AjaxResponse.fail("操作失败");
		}
	}

	/**
	 * 获取用户积分列表
	 * 
	 * @return
	 */
	@RequestMapping("/findUserPorints")
	@ResponseBody
	public Page<SUserPoints> findUserPorints(HttpServletRequest request, ModelMap model,HttpServletResponse hsr) throws NotLoginEXception{
		
		model.put("userInfo", userService.selectByKey(getCurrentUser(request).getId()));
		paramMap = getRequestParameter(request);
		paramMap.put("userCode", getCurrentUser(request).getCode());
		return  SUserPointsService.selectByPage(paramMap);
	}
	
	/**
	 * 账户安全
	 * @return
	 */
	@RequestMapping("/safe/{viewName}")
	@ResponseBody
	public SUser safeInfo(HttpServletRequest request, ModelMap model,@PathVariable String viewName,HttpServletResponse hsr) throws NotLoginEXception{
		
			return userService.selectByKey(getCurrentUser(request).getId());
	}
	
	/**
	 * 校验密码是否存在
	 * @return
	 */
	@RequestMapping("/verifyPassword")
	@ResponseBody
	public AjaxResponse verifyPassword(String passwd,HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		
		SUser sUser=getCurrentUser(request);
		if(sUser.getPassword().equals(PasswordHelper.newPassword(passwd))){
			return AjaxResponse.success("操作成功");
		}else{
			return AjaxResponse.fail("请正确输入旧密码");
		}
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping("/changePassword")
	@ResponseBody
	public AjaxResponse changePassword(String newPasswd,HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		
		SUser sUser=new SUser();
		sUser.setPassword(PasswordHelper.newPassword(newPasswd));
		sUser.setId(getCurrentUser(request).getId());
		int i = userService.updateNotNull(sUser);
		if(i == 1){
			request.getSession().removeAttribute(Constants.CURRENT_USER);
			return AjaxResponse.success("密码修改成功,请使用新密码重新登录");
		}else{
			return AjaxResponse.fail("操作失败");
		}
	}
	
	/**
	 * 修改邮箱
	 * @param newEamil
	 * @param request
	 * @return
	 */
	@RequestMapping("/changeEmail")
	@ResponseBody
	public AjaxResponse changeEmail(String newEamil,HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		
		SUser sUser=new SUser();
		sUser.setEmail(newEamil);
		sUser.setId(getCurrentUser(request).getId());
		int i = userService.updateNotNull(sUser);
		if(i == 1){
			request.getSession().removeAttribute(Constants.CURRENT_USER);
			return AjaxResponse.success("邮箱修改成功,请重新登录！");
		}else{
			return AjaxResponse.fail("操作失败");
		}
	}
	
	/**
	 * 修改手机号
	 * @param newEamil
	 * @param request
	 * @return
	 */
	@RequestMapping("/changePhone")
	@ResponseBody
	public AjaxResponse changePhone(String newPhone,HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		
		SUser sUser=new SUser();
		sUser.setPhone(newPhone);
		sUser.setId(getCurrentUser(request).getId());
		int i = userService.updateNotNull(sUser);
		if(i == 1){
			request.getSession().removeAttribute(Constants.CURRENT_USER);
			return AjaxResponse.success("手机修改成功,请重新登录！");
		}else{
			return AjaxResponse.fail("操作失败");
		}
	}
	
	/**
	 * 发送旧手机验证码 phonecode
	 * @return
	 */
	@RequestMapping("/sendOldPhoneCode")
	@ResponseBody
	public AjaxResponse sendOldPhoneCode(String phone,HttpServletRequest request,HttpServletResponse hsr) {
		
		boolean flag = SmsUtils.sendMessage(phone, SmsUtils.REGIST_TEMP,SmsUtils.getRadomNumber(),"","Old",request);
		if (flag) {
			return AjaxResponse.success("发送验证码成功!");
		} else {
			return AjaxResponse.fail("发送验证码失败!");
		}
	}
	
	/**
	 * 发送新手机验证码
	 * @return
	 */
	@RequestMapping("/sendNewPhoneCode")
	@ResponseBody
	public AjaxResponse sendNewPhoneCode(String phone,HttpServletRequest request,HttpServletResponse hsr) {
		
		boolean flag = SmsUtils.sendMessage(phone, SmsUtils.REGIST_TEMP,SmsUtils.getRadomNumber(),"","New",request);
		if (flag) {
			return AjaxResponse.success("发送验证码成功!");
		} else {
			return AjaxResponse.fail("发送验证码失败!");
		}
	}
	
	/**
	 * 校验旧手机验证码
	 * @return
	 */
	@RequestMapping("/valitationOldSMSCode")
	@ResponseBody
	public AjaxResponse valitationOldSMSCode(String code,HttpServletRequest request,HttpServletResponse hsr) {
		
		code = code.toLowerCase();
		String phoneCode = (String) request.getSession().getAttribute("phonecodeOld");
		
		
		if(StringUtils.isEmpty(code)){
			return AjaxResponse.fail("请填写手机验证码!");
		}
		
		if(StringUtils.isEmpty(phoneCode)){
			return AjaxResponse.fail("请发送手机验证码!");
		}
		
		if (code.equals(phoneCode.toString().toLowerCase())) {
			return AjaxResponse.success("手机验证码正确!");
		} 

		return AjaxResponse.fail("手机验证码错误!");
	}
	
	/**
	 * 校验新手机验证码
	 * @return
	 */
	@RequestMapping("/valitationNewSMSCode")
	@ResponseBody
	public AjaxResponse valitationNewSMSCode(String code,HttpServletRequest request,HttpServletResponse hsr) {
		
		code = code.toLowerCase();
		String phoneCode = (String) request.getSession().getAttribute("phonecodeNew");
		
		
		if(StringUtils.isEmpty(code)){
			return AjaxResponse.fail("请填写手机验证码!");
		}
		
		if(StringUtils.isEmpty(phoneCode)){
			return AjaxResponse.fail("请发送手机验证码!");
		}
		
		if (code.equals(phoneCode.toString().toLowerCase())) {
			return AjaxResponse.success("手机验证码正确!");
		} 

		return AjaxResponse.fail("手机验证码错误!");
	}

}
