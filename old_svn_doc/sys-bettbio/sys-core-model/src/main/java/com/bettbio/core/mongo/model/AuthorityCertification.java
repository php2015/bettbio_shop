package com.bettbio.core.mongo.model;

/**
 * 权威认证
 * 
 * @author chang
 *
 */
public class AuthorityCertification extends IncrementModel {
	private Buyers buyers;// 机构信息
	private String imgUrl;// 认证图片路径
	private String content;// 认证内容
	private String createDate;// 创建时间
	private String updateDate;// 更新时间
	private int index;// 序列

	public Buyers getBuyers() {
		return buyers;
	}

	public void setBuyers(Buyers buyers) {
		this.buyers = buyers;
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
