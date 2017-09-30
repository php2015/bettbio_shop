package com.salesmanager.core.business.customer.model;

import java.util.Date;

import com.salesmanager.core.business.common.model.Criteria;

/**
 * 商品信息
 * @author Lgh
 *
 */
public class GiftsCriteria extends Criteria {
	
	
	private Long id;
	
	private String name; //商品名称
	
	private Long points; //积分数量
	
	private Long price; //商品价格
	private Long type;//商品类别
	private Long total;//兑换总数
	private Date deadline;//截止时间
	

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

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
}
