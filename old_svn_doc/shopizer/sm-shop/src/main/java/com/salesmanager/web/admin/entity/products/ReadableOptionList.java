package com.salesmanager.web.admin.entity.products;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class ReadableOptionList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4725572951366664016L;
	private List<ReadableOption> options;
	public List<ReadableOption> getOptions() {
		return options;
	}
	public void setOptions(List<ReadableOption> options) {
		this.options = options;
	}

}
