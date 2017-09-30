package com.salesmanager.web.admin.entity.digital;

import java.io.Serializable;

public class DownLoadFile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4313480822012534208L;
	private String fileName;
	private String url;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
