package com.bettbio.core.common.email;


public interface HtmlEmailSender {
	
	public void send(final Email email);

	public void setEmailConfig(EmailConfig emailConfig);

}
