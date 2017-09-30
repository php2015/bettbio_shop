package com.bettbio.shop.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.email.constant.EmailConstants;
import com.bettbio.core.common.service.async.AsyncTaskService;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.utils.SmsUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.SUser;
import com.bettbio.core.model.SUserPoints;
import com.bettbio.core.model.vo.StoreRegisterVo;
import com.bettbio.core.service.SStoreService;
import com.bettbio.core.service.SUserPointsService;
import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.StoreUserService;
import com.bettbio.core.service.SysEmailService;
import com.bettbio.core.service.UserGradeService;

/**
 * 注册控制
 * 
 * @author simon
 *
 */
@Controller
public class RegisterController {

	@Autowired
	SUserService sUserService;

	@Autowired
	StoreUserService storeUserService;

	@Autowired
	SUserPointsService sUserPointsService;

	@Autowired
	SStoreService storeService;
	
	@Autowired
	UserGradeService userGradeService;

	@Autowired
	AsyncTaskService asyncTaskService;
	
	@Autowired
	SysEmailService sysEmailService;

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param model
	 * @param sUser
	 * @param result
	 * @return
	 */
	@RequestMapping("/completeUserRegister")
	public String userRegister(final SUser sUser) {
		// 完善用户信息
		sUser.setAccount(sUser.getEmail());
		sUser.setPassword(PasswordHelper.newPassword(sUser.getPassword()));
		String userCode = CodeUtils.getCode();
		sUser.setCode(userCode);
		sUser.setGrade(1);// 默认等级为1级
		sUser.setBalance(userGradeService.selectValue(1));
		int i = sUserService.save(sUser);
		if (i == 1) {
			// 设置注册送积分
			final SUserPoints sUserPoints = new SUserPoints();
			sUserPoints.setCode(userCode);
			sUserPoints.setPoints(Constants.DEFAULT_REGISTER_POINTS);
			sUserPoints.setPointsType(Constants.POINTS_REGISTER);
			// 发送邮件
			// 异步添加积分
			asyncTaskService.run(new Runnable() {
				@Override
				public void run() {
					if (sUserPointsService.save(sUserPoints) == 1) {
						sUser.setIntegral(Constants.DEFAULT_REGISTER_POINTS);
						sUserService.updateUserPoints(sUser);
						System.out.println("register----异步更新结束");
					}
				}
			});
			System.out.println("register----用户添加完成");
			return "index";
		} else {
			return "error";
		}
	}

	/**
	 * 商家注册
	 * 
	 * @param request
	 * @param model
	 * @param sStoreUser
	 * @param result
	 * @return
	 */
	@RequestMapping("/completeStoreRegister")
	public String storeRegister(SStoreUser storeUser, SStore store, StoreRegisterVo storeRegisterVo) {

		// 设置商铺信息
		store.setName(storeRegisterVo.getStoreName());
		String storeCode = CodeUtils.getCode();
		store.setCode(storeCode);
		// 设置基本用户信息
		storeUser.setName(storeRegisterVo.getStoreUserName());
		storeUser.setPassword(PasswordHelper.newPassword(storeUser.getPassword()));
		storeUser.setCode(CodeUtils.getCode());
		storeUser.setStoreCode(storeCode);
		storeUser.setIsDeleted(1);// 默认正常
		storeUser.setParentCode("default");
		storeUserService.createStore(storeUser, store);
		// 发送邮件
		Map<String, String> templateTokens =new HashMap<String, String>();
		templateTokens.put(EmailConstants.EMAIL_NEW_STORE_TEXT, "商家注册");
		templateTokens.put(EmailConstants.EMAIL_STORE_NAME, store.getName());
		//templateTokens.put(EmailConstants.EMAIL_ADMIN_STORE_INFO_LABEL, "infoLABLE");
		//templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, "url");
		templateTokens.put("EMAIL_ADMIN_QQ_LABEL",storeUser.getQq());
		//templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, store.getStoreUrl());
		templateTokens.put("EMAIL_FOOTER_COPYRIGHT", "Copyright @ Shopizer 2016, All Rights Reserved!");
		templateTokens.put("EMAIL_DISCLAIMER", "Disclaimer text goes here...");
		templateTokens.put("EMAIL_SPAM_DISCLAIMER", "Spam Disclaimer text goes here...");
		sysEmailService.sendRegisterEmail(store.getEmail(), templateTokens);
		return "/storeRegister";
	}

	/**
	 * 验证手机号
	 * 
	 * @param request
	 * @param model
	 * @param phone
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/verifyPhone")
	public AjaxResponse verifyPhone(String phone) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		String i = sUserService.verifyPhone(phone);
		if ("0".equals(i)) {
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("手机号验证通过!");
		} else {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("该手机号已存在!");
		}
		return ajaxResponse;
	}

	/**
	 * 校验邮箱是否存在
	 * 
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/verifyEmail")
	public AjaxResponse verifyEmail(String email) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		String i = sUserService.verifyEmail(email);
		if ("0".equals(i)) {
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("改邮箱验证通过");
		} else {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("该邮箱已存在!");
		}
		return ajaxResponse;
	};

	/**
	 * 校验商家名称
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/verifyStoreName")
	@ResponseBody
	public AjaxResponse verifyStoreName(String name) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		int i = storeService.verifyStoreName(name);
		if (i == 0) {
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("验证通过");
		} else {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("该商家已存在");
		}
		return ajaxResponse;
	}

	/**
	 * 校验用户名
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/verifyStoreUserName")
	@ResponseBody
	public AjaxResponse verifyStoreUserName(String name) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		int i = storeUserService.verifyStoreUserName(name);
		if (i == 0) {
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("验证通过");
		} else {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("该用户已存在");
		}
		return ajaxResponse;
	}

	/**
	 * 忘记密码
	 * 
	 * @return
	 */
	@RequestMapping("/retrievePassword")
	public String retrievePassword() {
		return "/common/retrievePassword";
	}

	/**
	 * 发送密码
	 * 
	 * @return
	 */
	@RequestMapping("/sendPwd")
	@ResponseBody
	public AjaxResponse sendPwd(String phone,HttpServletRequest request) {
		try {
			SUser user = new SUser();
			user.setPhone(phone);
			user = sUserService.selectByPhone(user);
			String newPwd = PasswordHelper.generatePassword();
			int i=0;
			if (user != null) {
				user.setPassword(PasswordHelper.newPassword(newPwd));
				i=sUserService.updateNotNull(user);

			} else {
				SStoreUser sStoreUser = new SStoreUser();
				sStoreUser.setPhone(phone);
				sStoreUser = storeUserService.selectByPhone(sStoreUser);
				sStoreUser.setPassword(PasswordHelper.newPassword(newPwd));
				i=storeUserService.updateNotNull(sStoreUser);
			}
			if(i>0){
				asyncTaskService.run(new Runnable() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(Long.parseLong((20*1000)+""));
							SmsUtils.sendMessage(phone,SmsUtils.REGIST_TEMP,newPwd,"","", request);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				return AjaxResponse.success("密码发送成功,请注意查收");
			}else{
				AjaxResponse.fail("发送密码失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResponse.fail("发送密码失败");
	}
}
