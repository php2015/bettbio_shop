package com.salesmanager.core.business.catalog.product.model.attribute;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class OptionValueList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9073747871488424673L;
	List<ProductOptionValue> values ;
	public List<ProductOptionValue> getValues() {
		return values;
	}
	public void setValues(List<ProductOptionValue> values) {
		this.values = values;
	}

}
