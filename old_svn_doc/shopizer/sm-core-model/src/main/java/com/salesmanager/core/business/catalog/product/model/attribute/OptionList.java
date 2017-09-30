package com.salesmanager.core.business.catalog.product.model.attribute;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class OptionList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8967767702505765771L;
	private List<ProductOption> options;
	public List<ProductOption> getOptions() {
		return options;
	}
	public void setOptions(List<ProductOption> options) {
		this.options = options;
	}

}
