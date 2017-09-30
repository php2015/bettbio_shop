package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.ServiceEntity;

public class ReadableOrderList extends ServiceEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total=0;
	//private SubOrder subOrder;
	//private List<ReadableOrder> orders;
	private List<EzybioOrder> ezybioOrder;
	
	
	public List<EzybioOrder> getEzybioOrder() {
		return ezybioOrder;
	}
	public void setEzybioOrder(List<EzybioOrder> ezybioOrder) {
		this.ezybioOrder = ezybioOrder;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
