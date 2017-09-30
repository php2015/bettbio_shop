package com.bettbio.common.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bettbio.core.common.email.Email;
import com.bettbio.core.common.email.HtmlEmailSender;
import com.bettbio.core.common.email.constant.EmailConstants;


@ContextConfiguration(locations="classpath:spring-test-common.xml")
public class EmailTest extends AbstractTransactionalJUnit4SpringContextTests{

	
	@Autowired
	HtmlEmailSender htmlEmailSender;
	
	@Test
	public void send() throws Exception{
		Map<String, String> templateTokens = new HashMap<String, String>();

		Email email = new Email();
		email.setSubject("Email Title");
		email.setTo("913365252@qq.com");
		email.setTemplateName(EmailConstants.TEST_EMAIL_TPL);
		email.setTemplateTokens(templateTokens);
		
		htmlEmailSender.send(email);
	}
}
