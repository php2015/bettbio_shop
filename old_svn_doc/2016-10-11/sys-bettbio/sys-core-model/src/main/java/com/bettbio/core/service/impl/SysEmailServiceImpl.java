package com.bettbio.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.common.email.Email;
import com.bettbio.core.common.email.constant.EmailConstants;
import com.bettbio.core.common.service.email.EmailService;
import com.bettbio.core.service.SysConfigService;
import com.bettbio.core.service.SysEmailService;
@Service
public class SysEmailServiceImpl implements SysEmailService {
	
	@Autowired
	SysConfigService sysConfigService;

	@Autowired
	EmailService emailService;
	
	@Override
	public void sendOrderEmail(Map<String, String> templateTokens) {
		Email email = sysConfigService.getEmailConfig();
		/*templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, merchantStore.getStorename());
		templateTokens.put(EmailConstants.PRODUCT_AUDIT, messages.getMessage("email.audit.text", downloadMessage, customerLocale));
		templateTokens.put(EmailConstants.PRODUCT_BETTER, "");*/
		email.setTemplateName(EmailConstants.ORDER_STATUS_TMPL);//订单模板
		email.setSubject("百图生物-订单");//邮件标题
		email.setTo("1161095164@qq.com");//收件人
		email.setTemplateTokens(templateTokens);
		emailService.sendEmail(email);
	}

	@Override
	public void sendProductEmail(Map<String, String> templateTokens) {
		/*templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, "BettBio");
        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, "product sku:  "+product.getSku()+"\n product name:  "+product.getProductDescription().getName());
		*/
		Email email = sysConfigService.getEmailConfig();
		email.setTemplateName(EmailConstants.EMAIL_NOTIFICATION_TMPL);
		email.setSubject("百图生物-商品添加");//邮件标题
		email.setTo("1161095164@qq.com");//收件人
		email.setTemplateTokens(templateTokens);
		emailService.sendEmail(email);
	}

	@Override
	public void sendRegisterEmail(String to, Map<String, String> templateTokens) {
		/*templateTokens.put(EmailConstants.EMAIL_NEW_STORE_TEXT, messages.getMessage("email.newstore.text", storeLocale));
		templateTokens.put(EmailConstants.EMAIL_STORE_NAME, messages.getMessage("email.newstore.name",new String[]{store.getStorename()},storeLocale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_STORE_INFO_LABEL, messages.getMessage("email.newstore.info",storeLocale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",storeLocale));
		templateTokens.put("EMAIL_ADMIN_QQ_LABEL", messages.getMessage("email.newstore.qqInfo",storeLocale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, FilePathUtils.buildAdminUri(store, request));
		 */
		Email email = sysConfigService.getEmailConfig();
		email.setTemplateName(EmailConstants.NEW_STORE_TMPL);
		email.setSubject("Email Title");//邮件标题
		email.setTo(to);//收件人
		List<String> cc = new ArrayList<String>();//抄送人集合
		cc.add("1161095164@qq.com");
		email.setCc(cc);
		email.setTemplateTokens(templateTokens);
		emailService.sendEmail(email);
	}

}
