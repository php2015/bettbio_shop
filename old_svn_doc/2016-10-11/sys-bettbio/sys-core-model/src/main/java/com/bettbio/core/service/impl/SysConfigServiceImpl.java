package com.bettbio.core.service.impl;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.email.Email;
import com.bettbio.core.service.SysConfigService;

/**
 * 获取系统配置属性
 * @author GuoChunbo
 *
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

	@Override
	public Email getEmailConfig() {
		Email email = new Email();
		email.setFrom("百图生物"); //系统配置项
		email.setFromEmail("service@bettbio.com");//
		return email;
	}

}
