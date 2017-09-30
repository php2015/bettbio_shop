package com.salesmanager.core.business.merchant.model;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class StoreList extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3031813961455856439L;
	private List<MerchantStore> stores;
	public List<MerchantStore> getStores() {
		return stores;
	}
	public void setStores(List<MerchantStore> stores) {
		this.stores = stores;
	}
	
}
