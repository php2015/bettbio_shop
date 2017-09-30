package com.bettbio.shop.web.controller.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.utils.SmsUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.PhoneCode;
import com.bettbio.core.model.SUser;
import com.bettbio.core.service.PhoneCodeService;
import com.bettbio.core.service.SUserService;
import com.bettbio.shop.base.controller.BaseShopController;

/**
 * APP登陆
 * 
 * @author chang
 *
 */
@Controller("appLoginController")
@RequestMapping("/app")
public class LoginController extends BaseShopController {
	@Autowired
	SUserService sUserService;
	
	@Autowired
	PhoneCodeService phoneCodeService;
	
	/**
	 * 买家用户登录
	 * 
	 * @param request
	 * @param model
	 * @param sUser
	 * @param result
	 * @return
	 */
	@RequestMapping("/userLogin")
	@ResponseBody
	public Map<String, Object> userLogin(SUser sUser,HttpServletResponse hsr,HttpServletRequest request) {
		setHeder(hsr);
		sUser = sUserService.login(sUser);
		if (sUser != null) {
			request.getSession().setAttribute(Constants.CURRENT_USER, sUser);
			request.getSession().setAttribute(Constants.CURRENT_USERNAME, sUser.getName());
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user", sUser);
			map.put("uid", request.getSession().getId());
			return map;
		}
		return null;
	}

	@RequestMapping("/logout")
	public AjaxResponse logout(HttpServletRequest request, ModelMap model,HttpServletResponse hsr) {
		setHeder(hsr);
		request.getSession().removeAttribute(Constants.CURRENT_USER);
		AjaxResponse ajaxResponse=new AjaxResponse();
		ajaxResponse.setSuccess(Boolean.TRUE);
		ajaxResponse.setMessage("用户退出成功");
		return ajaxResponse;
	}

	@RequestMapping("/sendSms")
	@ResponseBody
	public AjaxResponse sendSms(SUser sUser,HttpServletResponse hsr,HttpServletRequest request) {
		setHeder(hsr);
		SUser phoneUser = sUserService.selectByPhone(sUser);
		if(phoneUser==null){
			return AjaxResponse.fail("手机号不存在!");
		}
		String random = SmsUtils.getRadomNumber();
		sUser.setPhoneCode(random);
		int r = sUserService.updatePhoneCode(sUser);
		if(r>0){
			boolean flag = SmsUtils.sendMessage(sUser.getPhone(), SmsUtils.REGIST_TEMP,random,request);
			if(flag){
				return AjaxResponse.success("发送验证码成功!");
			}
		}
		
		return AjaxResponse.fail("发送验证码失败!");
	}
	
	
	@RequestMapping("/fastLogin")
	@ResponseBody
	public Map<String, Object> fastLogin(SUser sUser,HttpServletResponse hsr,HttpServletRequest request) {
		setHeder(hsr);
		Map<String, Object> map = new HashMap<String, Object>();
		SUser phoneUser = sUserService.selectByPhone(sUser);
		if(phoneUser==null){
			map.put("message", "手机号不存在!");
			return map;
		}
		sUser = sUserService.fastLogin(sUser);
		if (sUser != null) {
			request.getSession().setAttribute(Constants.CURRENT_USER, sUser);
			request.getSession().setAttribute(Constants.CURRENT_USERNAME, sUser.getName());
			
			map.put("user", sUser);
			map.put("uid", request.getSession().getId());
			return map;
		}else{
			map.put("message", "登录失败，验证码错误!");
		}
		return null;
	}
	
	/**
	 * 发送注册验证码
	 * 参数 phone
	 * @return
	 */
	@RequestMapping("/sendRegister")
	@ResponseBody
	public AjaxResponse sendRegister(PhoneCode phoneCode,HttpServletRequest request,HttpServletResponse hsr){

		String random = SmsUtils.getRadomNumber();//验证码code
		DateTime createTime = new DateTime();//发送时间
		DateTime invalidTime = createTime.plusMinutes(15);//失效时间
		phoneCode.setCreateTime(createTime.toDate());
		phoneCode.setInvalidTime(invalidTime.toDate());
		int r = phoneCodeService.save(phoneCode);
		if(r>0){
			boolean flag = SmsUtils.sendMessage(phoneCode.getPhone(), SmsUtils.REGIST_TEMP,random,request);
			if(flag){
				return AjaxResponse.success("发送验证码成功!");
			}
		}
		return AjaxResponse.fail("发送验证码失败!");
	}
	
	/**
	 * 验证注册验证码
	 * 参数 
	 * 	* phone
	 * 	* code
	 * @return
	 */
	@RequestMapping("/validaRegister")
	@ResponseBody
	public AjaxResponse validaRegister(PhoneCode phoneCode,HttpServletRequest request,HttpServletResponse hsr){

		phoneCode = phoneCodeService.invalidPhoneCode(phoneCode);
		if(phoneCode!=null){
			if(phoneCode.getIsInvalid()==1){
				return AjaxResponse.success("验证码正确!");
			}else{
				return AjaxResponse.fail("验证码已实效!");
			}
		}
		return AjaxResponse.fail("验证码错误!");
	}
}
