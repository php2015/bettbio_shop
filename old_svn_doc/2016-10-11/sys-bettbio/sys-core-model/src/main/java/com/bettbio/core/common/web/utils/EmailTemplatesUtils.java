package com.bettbio.core.common.web.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.email.HtmlEmailSender;
import com.bettbio.core.model.SOrder;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SUser;

/**
 * 邮件操作类
 * 
 * @author 赵世昌
 *
 */
public class EmailTemplatesUtils {
	@Autowired
	private HtmlEmailSender htmlEmailSender;

	/**
	 * 发送注册会员邮件
	 */
	public void sendCreateUserEmail(SUser user) {
		try {
	
		} catch (Exception e) {
		}
	}

	/**
	 * 发送商家邮件激活
	 */
	public void sendCreateStoreEmail(SStore store) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	/**
	 * 发送会员忘记密码邮件
	 * 
	 * @param userName
	 * @param userCode
	 */
	public void sendUserResetPasswordEmail(SUser user) {

	}

	/**
	 * 发送商家忘记密码邮件
	 * 
	 * @param userName
	 * @param userCode
	 */
	public void sendStoreResetPasswordEmail(SStore store) {

	}

	/**
	 * 用户下单发送邮件
	 */
	public void sendPlaceOrder(SUser user, SStore store, SOrder order) {

	}
}
