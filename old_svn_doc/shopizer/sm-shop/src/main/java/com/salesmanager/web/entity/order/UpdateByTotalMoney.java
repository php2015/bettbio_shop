package com.salesmanager.web.entity.order;

import java.util.List;

public class UpdateByTotalMoney {
	private Long orderid;
	private Double subMoney;
	private Double totalMoney;
	private Integer score;
	private Long mid;
	private List<String> userNameList;
	private Double price;
	private Double totalprice;
	private Long suid;
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	public Double getSubMoney() {
		return subMoney;
	}
	public void setSubMoney(Double subMoney) {
		this.subMoney = subMoney;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public List<String> getUserNameList() {
		return userNameList;
	}
	public void setUserNameList(List<String> userNameList) {
		this.userNameList = userNameList;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}
	public Long getSuid() {
		return suid;
	}
	public void setSuid(Long suid) {
		this.suid = suid;
	}

}
