package com.bettbio.core.common.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.utils.SmsUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;

@Controller
@RequestMapping("/sms")
public class SmsController {

	/**
	 * 注册
	 * 
	 * @param request
	 * @param phone
	 * @return
	 */
	@RequestMapping("/register")
	@ResponseBody
	public AjaxResponse send(HttpServletRequest request, String phone,HttpServletResponse hsr) {
		
		setHeder(hsr);
		AjaxResponse ajaxResponse = new AjaxResponse();
		boolean flag = SmsUtils.sendMessage(phone, SmsUtils.REGIST_TEMP, request);
		if (flag) {
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("发送验证码成功!");
		} else {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("发送验证码失败!");
		}

		return ajaxResponse;
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param phone
	 * @return
	 */
	@RequestMapping("/change")
	@ResponseBody
	public AjaxResponse change(HttpServletRequest request, String phone,HttpServletResponse response) {
		
		setHeder(response);
		AjaxResponse ajaxResponse = new AjaxResponse();
		boolean flag = SmsUtils.sendMessage(phone, SmsUtils.CHANGE_PW_TEMP, request);
		if (flag) {
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("发送验证码成功!");
		} else {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("发送验证码失败!");
		}

		return ajaxResponse;
	}

	/**
	 * 验证手机验证码
	 * 
	 * @param code
	 * @param request
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public AjaxResponse valitationBySMSCode(String code,String phone, HttpServletRequest request,HttpServletResponse response) {
		
		setHeder(response);
		code = code.toLowerCase();
		
		String phoneCode = (String) request.getSession().getAttribute("phonecode");
		
		
		if(StringUtils.isEmpty(code)){
			return AjaxResponse.fail("请填写手机验证码!");
		}
		
		if(StringUtils.isEmpty(phoneCode)){
			return AjaxResponse.fail("请发送手机验证码!");
		}
		
		if (code.equals(phoneCode.toLowerCase())) {
			return AjaxResponse.success("手机验证码正确!");
		} 

		return AjaxResponse.fail("手机验证码错误!");
	}
	
	  public void setHeder(HttpServletResponse hsr){
		  hsr.setHeader("Access-Control-Allow-Origin", "*");
	  }
}
