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

	public void sendEmail(Email email) {

		htmlEmailSender.send(email);
	}

	public HtmlEmailSender getHtmlEmailSender() {
		return htmlEmailSender;
	}

	public void setHtmlEmailSender(HtmlEmailSender htmlEmailSender) {
		this.htmlEmailSender = htmlEmailSender;
	}
	
	
}
