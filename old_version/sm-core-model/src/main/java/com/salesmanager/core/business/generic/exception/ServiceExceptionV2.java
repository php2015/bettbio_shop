package com.salesmanager.core.business.generic.exception;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ServiceExceptionV2 extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7734581920583131159L;

	protected String messageKey;
	protected List<Object> messageParams;
	protected int errorCode;
	public ServiceExceptionV2() {
		super();
	}
	public ServiceExceptionV2(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public ServiceExceptionV2(String message, Throwable cause) {
		super(message, cause);
	}
	public ServiceExceptionV2(String message) {
		super(message);
	}
	public ServiceExceptionV2(Throwable cause) {
		super(cause);
	}
	public ServiceExceptionV2(int code, String messageKey, Object... messageParams) {
		super();
		this.messageKey = messageKey;
		if (messageParams != null) {
			this.messageParams = Arrays.asList(messageParams);
		}
		this.errorCode = code;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public List<Object> getMessageParams() {
		return messageParams;
	}
	public void setMessageParams(List<Object> messageParams) {
		this.messageParams = messageParams;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	
	
}
