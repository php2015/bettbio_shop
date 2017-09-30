package com.bettbio.core.mongo.model;

import java.util.Date;

import com.bettbio.core.model.BasedataType;

/**
 * 购买凭证
 * @author chang
 *
 */
public class PurchaseVoucher extends IncrementModel{
	private BasedataType basedataType;//购买人信息
	private Date buyingTime;//购买时间
	private String imgUrl;//凭证 图片
	private String content;//购买内容
	private String createDate;//创建时间
	private String updateDate;//更新时间
	private int index;//序列

	public BasedataType getBasedataType() {
		return basedataType;
	}
	public void setBasedataType(BasedataType basedataType) {
		this.basedataType = basedataType;
	}
	public Date getBuyingTime() {
		return buyingTime;
	}
	public void setBuyingTime(Date buyingTime) {
		this.buyingTime = buyingTime;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
