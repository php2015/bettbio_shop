package com.salesmanager.web.admin.entity.products;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.web.entity.Entity;

public class ReadableOptionValue extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1266462846418974709L;
	private String name;
	private MultipartFile img;
	public MultipartFile getImg() {
		return img;
	}
	public void setImg(MultipartFile img) {
		this.img = img;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
