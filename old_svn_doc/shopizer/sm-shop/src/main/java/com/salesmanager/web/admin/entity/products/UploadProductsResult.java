package com.salesmanager.web.admin.entity.products;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义批量上传产品信息处理结果
 * @class com.salesmanager.web.admin.entity.products.UploadProductsResult.java
 * @author sam
 * @date 2015年12月5日
 */
public class UploadProductsResult implements Serializable {

	private String status; //处理结果状态
	private String message;
	private Map<String, String> successMap = new HashMap<String, String>();
	private Map<String, String> unsuccessMap = new HashMap<String, String>();
	private String successlines = "";
	private String errorlines = "";
	
	public enum StatusEnum
	{
		UNSUCCESS("unsuccess"),
		SUCCESS("success"),
		PARTIALSUCCESS("partialsuccess");
		private String code;
		  
		private StatusEnum(String code) {
		  this.code = code;
		}

		public String getCode() {
			return code;
		}
	
		public void setCode(String code) {
			this.code = code;
		}
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSuccesslines() {
		return successlines;
	}
	public void setSuccesslines(String successlines) {
		this.successlines = successlines;
	}
	public String getErrorlines() {
		return errorlines;
	}
	public void setErrorlines(String errorlines) {
		this.errorlines = errorlines;
	}
	public void addSuccesslines(String line) {
		successlines += line+"##";
	}
	public void addUnsuccesslines(String line, String error) {
		errorlines += line + ";;error=" + error + "##";
	}
	public Map<String, String> getSuccessMap() {
		return successMap;
	}
	public void setSuccessMap(Map<String, String> successMap) {
		this.successMap = successMap;
	}
	public Map<String, String> getUnsuccessMap() {
		return unsuccessMap;
	}
	public void setUnsuccessMap(Map<String, String> unsuccessMap) {
		this.unsuccessMap = unsuccessMap;
	}
	
}
