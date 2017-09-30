package com.bettbio.shop.web.controller.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.service.async.AsyncTaskService;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.utils.SmsUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.PhoneCode;
import com.bettbio.core.model.SUser;
import com.bettbio.core.model.SUserPoints;
import com.bettbio.core.model.UserTmp;
import com.bettbio.core.service.PhoneCodeService;
import com.bettbio.core.service.SUserPointsService;
import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.UserGradeService;
import com.bettbio.core.service.UserTmpService;
import com.bettbio.shop.base.controller.BaseShopController;

@Controller("appRegisterController")
@RequestMapping("/app")
public class RegisterController extends BaseShopController{
	@Autowired
	SUserService sUserService;
	
	@Autowired
	UserGradeService userGradeService;
	
	@Autowired
	PhoneCodeService phoneCodeService;
	
	@Autowired
	UserTmpService userTmpService;
	
	@Autowired
	AsyncTaskService asyncTaskService;
	
	@Autowired
	SUserPointsService sUserPointsService;
	
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
		phoneCode.setCode(random);//code 
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
	public String validaRegister(PhoneCode phoneCode,HttpServletRequest request,HttpServletResponse hsr){

		phoneCode = phoneCodeService.invalidPhoneCode(phoneCode);
		if(phoneCode!=null){
			if(phoneCode.getIsInvalid()==1){
				UserTmp userTmp = new UserTmp();
				userTmp.setCode(CodeUtils.getCode());
				userTmp.setPhone(phoneCode.getPhone());
				userTmpService.save(userTmp);
				return userTmp.getCode();
			}else{
				return "0";
			}
		}
		return "0";
	}
	
	/**
	 * 提交密码
	 */
	@RequestMapping("/savePassword")
	@ResponseBody
	public String savePassword(String code,String password){
		UserTmp userTmp = userTmpService.getUserTmpByCode(code);
		userTmp.setPassword(password);
		int i = userTmpService.updateNotNull(userTmp);
		if(i ==1){
			return userTmp.getCode();
		}
		return "0";
	}
	
	/**
	 * 提交资料
	 */
	@RequestMapping("/saveData")
	@ResponseBody
	public AjaxResponse saveData(UserTmp userTmp){
		UserTmp user = userTmpService.getUserTmpByCode(userTmp.getCode());
		userTmp.setPhone(user.getPhone()); 
		userTmp.setPassword(user.getPassword());
		SUser sUser = new SUser();
		// 完善用户信息
		sUser.setAccount(userTmp.getEmail());
		sUser.setEmail(userTmp.getEmail());
		sUser.setPassword(PasswordHelper.newPassword(userTmp.getPassword()));
		sUser.setCompany(userTmp.getCompany());
		sUser.setPhone(userTmp.getPhone());
		sUser.setName(userTmp.getUserName());
		sUser.setMechanismSubCode(userTmp.getMechanismSubCode());
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
			return AjaxResponse.success("注册成功");
		} else {
			return AjaxResponse.success("注册失败");
		}
		
		
	}
}
