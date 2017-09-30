package com.salesmanager.web.entity.shop;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.ServiceEntity;

public class ReadableStore extends ServiceEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7685675356662550348L;
	private int total=0;
	private List<Store> stores;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Store> getStores() {
		return stores;
	}
	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

}
