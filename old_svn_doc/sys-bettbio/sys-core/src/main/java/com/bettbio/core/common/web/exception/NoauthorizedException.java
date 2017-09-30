package com.bettbio.core.common.web.exception;

import com.bettbio.core.common.exception.BaseException;

public class NoauthorizedException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3152144240077167360L;

	
	public NoauthorizedException(String code, Object[] args) {
		
		super("AUTHORIZ", code, args);
	}




	
}
