package com.salesmanager.core.business.catalog.product.model.manufacturer;

import com.salesmanager.core.business.common.model.Criteria;

public class ManufactureCriteria extends Criteria {
	
	private Long srcStoreId =-1l;

	public Long getSrcStoreId() {
		return srcStoreId;
	}

	public void setSrcStoreId(Long srcStoreId) {
		this.srcStoreId = srcStoreId;
	}
	
}
