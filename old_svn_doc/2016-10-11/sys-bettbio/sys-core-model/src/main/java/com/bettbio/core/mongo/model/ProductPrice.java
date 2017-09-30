package com.bettbio.core.mongo.model;

import java.text.DecimalFormat;
import java.util.Date;

import com.bettbio.core.common.utils.DecimalFormatUtils;

/**
 * 产品价格信息
 * 
 * @author chang
 *
 */
public class ProductPrice extends IncrementModel {
	private String specifications;// 规格
	private double price;// 单价
	private String supplyCycle;// 供应周期
	private double discountPrice;// 折扣价格
	private Date startDate;// 折扣开始时间
	private Date endDate;// 折扣结束时间

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSupplyCycle() {
		return supplyCycle;
	}

	public void setSupplyCycle(String supplyCycle) {
		this.supplyCycle = supplyCycle;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public double getFinalPrice(){
		if(this.discountPrice==0) return this.price; 
		
		Date now = new Date();
		if(this.startDate !=null && this.endDate!=null){
		
			if(	now.getTime()>=	this.startDate.getTime() && now.getTime()<= this.endDate.getTime()	){				
				return this.discountPrice;
			}
		}
		
		return this.price;
	}
	
	@Override
	public String toString() {
		return "ProductPrice("
				+ "id=" + this.getId()
				+ " ,specifications=" + this.specifications
				+ " ,price=" + this.price
				+ ")";
	}
}
