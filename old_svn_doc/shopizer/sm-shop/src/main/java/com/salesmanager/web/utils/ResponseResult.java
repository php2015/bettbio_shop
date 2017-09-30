package com.salesmanager.web.utils;

import java.io.Serializable;

public class ResponseResult implements Serializable {
	private static final long serialVersionUID = 608044226867635669L;
	public ResponseResult() {
		// TODO Auto-generated constructor stub
	}
	public ResponseResult(boolean success) {
		this.success = success;
	}
	public ResponseResult(boolean success,Object data) {
		this.success = success;
		this.data = data;
	}
	private boolean success;
	private Object data;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
