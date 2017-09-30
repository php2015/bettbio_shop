package com.bettbio.core.common.web.exception;

/**
 * 未登录异常
 * @author chang
 *
 */
public class NotLoginEXception extends Exception{
	
	private static final long serialVersionUID = -7844590064435759055L;

	public NotLoginEXception(String msg){
		 super("用户未登录");
	}
}
