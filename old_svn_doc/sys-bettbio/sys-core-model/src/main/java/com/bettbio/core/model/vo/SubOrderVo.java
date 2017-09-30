package com.bettbio.core.model.vo;

import java.util.List;

import com.bettbio.core.model.SOrderDatails;
import com.bettbio.core.model.SSubOrder;
/**
 * 子订单业务类
 * @author chang
 *
 */
public class SubOrderVo extends SSubOrder{
	private static final long serialVersionUID = -2529600917510180205L;
    private List<SOrderDatails> orderDatails;
    private String userName;
	public List<SOrderDatails> getOrderDatails() {
		return orderDatails;
	}
	public void setOrderDatails(List<SOrderDatails> orderDatails) {
		this.orderDatails = orderDatails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
    
}
