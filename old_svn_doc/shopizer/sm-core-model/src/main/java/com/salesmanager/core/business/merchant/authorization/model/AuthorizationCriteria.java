package com.salesmanager.core.business.merchant.authorization.model;

import com.salesmanager.core.business.common.model.Criteria;

public class AuthorizationCriteria extends Criteria {
	
	private Long srcStoreId =-1l;

	public Long getSrcStoreId() {
		return srcStoreId;
	}

	public void setSrcStoreId(Long srcStoreId) {
		this.srcStoreId = srcStoreId;
	}
	
}
