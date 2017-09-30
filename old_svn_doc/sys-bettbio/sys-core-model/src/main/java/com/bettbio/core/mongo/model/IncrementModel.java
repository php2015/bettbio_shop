package com.bettbio.core.mongo.model;

import com.bettbio.core.common.web.utils.CodeUtils;

public class IncrementModel {

	private Integer id;

	public IncrementModel() {
		this.id = CodeUtils.getProductId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
