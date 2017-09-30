package com.bettbio.core.common.service.email;

import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.email.Email;
import com.bettbio.core.common.email.HtmlEmailSender;

/**
 * 统一发送邮件Service
 * 
 * @author GuoChunbo
 *
 */
public class EmailService {

	private HtmlEmailSender htmlEmailSender;

	/**
	 * 系统配置service去查系统配置，走缓存
	 */
	// @Autowired
	// SysConfigService sysConfigService;

	public void sendEmail(Email email) {

//		email.setFrom("ADMIN"); // 这两个参数可以读系统配置信息然后设置
//		email.setFromEmail("service@bettbio.com");// 同上

		htmlEmailSender.send(email);
	}

	public HtmlEmailSender getHtmlEmailSender() {
		return htmlEmailSender;
	}

	public void setHtmlEmailSender(HtmlEmailSender htmlEmailSender) {
		this.htmlEmailSender = htmlEmailSender;
	}
	
	
}
