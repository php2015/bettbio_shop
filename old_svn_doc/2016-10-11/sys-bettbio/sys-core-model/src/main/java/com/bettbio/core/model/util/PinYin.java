package com.bettbio.core.model.util;

import java.io.Serializable;
import java.util.List;

import com.bettbio.core.model.bo.PinYinName;

public class PinYin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3329217859503038768L;
	String code;
	List<PinYinName> lists;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<PinYinName> getLists() {
		return lists;
	}
	public void setLists(List<PinYinName> lists) {
		this.lists = lists;
	}

}
