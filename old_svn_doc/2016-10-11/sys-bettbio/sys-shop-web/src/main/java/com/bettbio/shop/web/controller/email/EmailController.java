package com.bettbio.shop.web.controller.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.email.Email;
import com.bettbio.core.common.email.constant.EmailConstants;
import com.bettbio.core.common.service.email.EmailService;
import com.bettbio.core.service.SysConfigService;

/**
 * 测试发送Email，需自己封装service
 * @author GuoChunbo
 *
 */
@Controller
public class EmailController {
	
	@Autowired
	EmailService emailService;

	SysConfigService sysConfigService;
	
	@RequestMapping("/sendEmail")
	public @ResponseBody String send() {
		Map<String, String> templateTokens = new HashMap<String, String>();
		
		Email email = new Email();

		email.setFrom("百图生物"); // 这两个参数可以读系统配置信息然后设置
		email.setFromEmail("service@bettbio.com");// 同上
		email.setSubject("Email Title");//邮件标题
		email.setTo("1161095164@qq.com");//收件人
		email.setTemplateName(EmailConstants.TEST_EMAIL_TPL);//模板
		email.setTemplateTokens(templateTokens);//模板对应的参数map
		List<String> cc = new ArrayList<String>();//抄送人集合
		//cc.add("1161095164@qq.com");
		email.setCc(cc);
		
		emailService.sendEmail(email);
		return "---T E S T  S E N D  E M A I L---";
	}
}
