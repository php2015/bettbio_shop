package com.salesmanager.web.admin.entity.products;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class ReadableOptionValueList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4789945594033236459L;
	
	private List<ReadableOptionValue> values ;

	public List<ReadableOptionValue> getValues() {
		return values;
	}

	public void setValues(List<ReadableOptionValue> values) {
		this.values = values;
	}

}
