package com.salesmanager.web.utils;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.salesmanager.core.business.generic.exception.ServiceExceptionV2;

public class LabelUtils implements ApplicationContextAware {

	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}
	
	public String getMessage(String key, Locale locale) {
		return applicationContext.getMessage(key, null, locale);
	}
	
	public String getMessage(String key, Locale locale, String defaultValue) {
		try {
			return applicationContext.getMessage(key, null, locale);
		} catch(Exception ignore) {}
		return defaultValue;
	}
	
	public String getMessage(String key, String[] args, Locale locale) {
		return applicationContext.getMessage(key, args, locale);
	}

	public String getServiceExceptionV2Message(ServiceExceptionV2 e, String messageKeyPrefix, String defMessage, Locale locale) {
		String[] args = null;
		List<Object> params = e.getMessageParams();
		if (params != null){
			args = new String[params.size()];
			for(int i=0;i<params.size();i++){
				args[i] = String.valueOf(params.get(i));
			}
		}
		String msg = getMessage(messageKeyPrefix+e.getMessageKey(), args, locale);
		if (msg == null){
			return defMessage;
		}
		return msg;
	}

}
