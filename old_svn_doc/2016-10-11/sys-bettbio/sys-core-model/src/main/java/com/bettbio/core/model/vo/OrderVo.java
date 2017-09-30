
package com.bettbio.core.model.vo;

import java.util.List;

import com.bettbio.core.model.SOrder;

/**
 * 订单业务类
 * @author chang
 *
 */
public class OrderVo extends SOrder{

	private static final long serialVersionUID = -7770536072258665970L;
	//子订单集合
	private List<SubOrderVo> subOrderVos;
	public List<SubOrderVo> getSubOrderVos() {
		return subOrderVos;
	}
	public void setSubOrderVos(List<SubOrderVo> subOrderVos) {
		this.subOrderVos = subOrderVos;
	} 
	
}
