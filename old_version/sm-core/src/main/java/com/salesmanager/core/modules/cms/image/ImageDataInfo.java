package com.salesmanager.core.modules.cms.image;

import java.io.Serializable;

public class ImageDataInfo implements Serializable{
	protected String contentType;
	protected String uri;
	protected byte[] imageBlob;
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public byte[] getImageBlob() {
		return imageBlob;
	}
	public void setImageBlob(byte[] imageBlob) {
		this.imageBlob = imageBlob;
	}
	
	
}
