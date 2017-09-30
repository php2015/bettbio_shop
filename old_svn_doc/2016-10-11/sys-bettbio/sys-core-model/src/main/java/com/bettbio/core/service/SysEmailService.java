package com.bettbio.core.service;

import java.util.Map;

public interface SysEmailService {

	/*
	 * 订单邮件
	 */
	void sendOrderEmail(Map<String, String> templateTokens);
	
	/*
	 * 产品邮件
	 */
	void sendProductEmail(Map<String, String> templateTokens);
	
	/*
	 * 注册
	 */
	void sendRegisterEmail(String to,Map<String, String> templateTokens);
}
