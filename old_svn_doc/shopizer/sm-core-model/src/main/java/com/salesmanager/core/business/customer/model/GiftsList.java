package com.salesmanager.core.business.customer.model;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class GiftsList extends EntityList {

	/**
	 * 获取商品
	 */
	private static final long serialVersionUID = -3108842276158069739L;
	private List<Gifts> gifts;
	public List<Gifts> getGifts() {
		return gifts;
	}
	public void setGifts(List<Gifts> gifts) {
		this.gifts = gifts;
	}
	


}
