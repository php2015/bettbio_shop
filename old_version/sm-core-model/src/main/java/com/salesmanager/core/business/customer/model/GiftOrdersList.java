package com.salesmanager.core.business.customer.model;

import java.util.List;

import com.salesmanager.core.business.common.model.EntityList;

public class GiftOrdersList extends EntityList {

	/**
	 * 获取商品
	 */
	private static final long serialVersionUID = -3108842276158069739L;
	private List<GiftOrder> giftsOrder;
	public List<GiftOrder> getGiftsOrder() {
		return giftsOrder;
	}
	public void setGiftsOrder(List<GiftOrder> giftsOrder) {
		this.giftsOrder = giftsOrder;
	}
	


}
