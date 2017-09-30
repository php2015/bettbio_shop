package com.salesmanager.core.business.customer.model;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;

/**
 * 商品信息
 * @author Lgh
 *
 */
public class GftsOrderCriteria extends Criteria {
	
	
	private Long id;
	private Long customerId;
	private Long number; //商品数量
	private String name; //商品名称
	
	private Long points; //积分数量
	
	private GiftStatus status =null;
		    
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	
	public void setStatus(GiftStatus status) {
		this.status = status;
	}

	public GiftStatus getStatus() {
		return status;
	}
	
	
}
