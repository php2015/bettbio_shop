package com.salesmanager.core.business.system.service;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.modules.email.EmailConfig;
import com.salesmanager.core.modules.email.HtmlEmailSender;
import com.salesmanager.core.utils.WorkModeConfiguration;


@Service("emailService")
public class EmailServiceImpl implements EmailService {
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	@Autowired
	private WorkModeConfiguration workMode;
	
	@Autowired
	private HtmlEmailSender sender;
	//@Autowired
	//Environment env;
	@Autowired
	private MerchantStoreService merchantStoreService;
	@Override
	public void sendHtmlEmail(MerchantStore store, Email email) throws ServiceException, Exception {

		EmailConfig emailConfig = getEmailConfiguration(store);
		
		setCCList(email, email.getTemplateName());
		sender.setEmailConfig(emailConfig);
		if (!workMode.isProductEnv()){
			String newSubject = email.getSubject()+" in " + workMode.getWorkMode()+ " for " + email.getTo();
			List<String> ccList = email.getCc();
			if (ccList != null){
				newSubject = newSubject + " and CC to: " + ccList;
			}
			email.setSubject(newSubject);
			System.out.println("Change email topic to " + email.getSubject());
			email.setTo(workMode.getEmailToAddress());
			email.setCc(workMode.getEmailCCAddress());
		}
		sender.send(email);
	}

	@Override
	public EmailConfig getEmailConfiguration(MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		EmailConfig emailConfig = null;
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				emailConfig = mapper.readValue(value, EmailConfig.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return emailConfig;
	}
	
	
	@Override
	public void saveEmailConfiguration(EmailConfig emailConfig, MerchantStore store) throws ServiceException {
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(Constants.EMAIL_CONFIG);
		}
		
		String value = emailConfig.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
	}

	@Override
	public void sendHtmlEmail(Email email) throws ServiceException, Exception{
		// TODO Auto-generated method stub
		//EmailConfig emailConfig = new EmailConfig();
		MerchantStore store = merchantStoreService.getByCode("DEFAULT");
		EmailConfig emailConfig = getEmailConfiguration(store);
		
		//TODO: Need to check below properties. When there are no record available in MerchantConfguration table with EMAIL_CONFIG key, 
		// instead of showing blank fields in setup screen, show default configured values from email.properties
		/**emailConfig.setProtocol(env.getProperty("mailSender.protocol"));
		emailConfig.setHost(env.getProperty("mailSender.host"));
		emailConfig.setPort(env.getProperty("mailSender.port}"));
		emailConfig.setUsername(env.getProperty("mailSender.username"));
		emailConfig.setPassword(env.getProperty("mailSender.password"));
		emailConfig.setSmtpAuth(Boolean.parseBoolean(env.getProperty("mailSender.mail.smtp.auth")));
		emailConfig.setStarttls(Boolean.parseBoolean(env.getProperty("mail.smtp.starttls.enable")));*/
		//
		//LOGGER.error("Email Config",emailConfig.toJSONString());
		setCCList(email, email.getTemplateName());
		sender.setEmailConfig(emailConfig);
		if (!workMode.isProductEnv()){
			String newSubject = email.getSubject()+" in " + workMode.getWorkMode()+ " for " + email.getTo();
			List<String> ccList = email.getCc();
			if (ccList != null){
				newSubject = newSubject + " and CC to: " + ccList;
			}
			email.setSubject(newSubject);
			System.out.println("Change email topic to " + email.getSubject());
			email.setTo(workMode.getEmailToAddress());
			email.setCc(workMode.getEmailCCAddress());
		}
		sender.send(email);
		
	}

	private void setCCList(Email email, String emailTemplateName) {
		List<String> ccList = workMode.getEmailCarbonCopyMap().get(emailTemplateName);
		if (ccList == null){
			System.out.println("WARNING: No CC list set for template "+ emailTemplateName);
			return;
		}
		List<String> orgList = email.getCc();
		if (orgList == null){
			email.setCc(ccList);
			return;
		}
		for(String emailAddr : ccList){
			if (orgList.contains(emailAddr)){
				continue;
			}
			orgList.add(emailAddr);
		}
		email.setCc(orgList);
	}
}
