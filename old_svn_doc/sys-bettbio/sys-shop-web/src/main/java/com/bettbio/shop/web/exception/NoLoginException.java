package com.bettbio.shop.web.exception;


public class NoLoginException extends RuntimeException {

	private Integer code;

	public NoLoginException(Integer code) {
		super("用户未登录!");
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
