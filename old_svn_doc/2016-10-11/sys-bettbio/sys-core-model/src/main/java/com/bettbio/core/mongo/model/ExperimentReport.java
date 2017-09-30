package com.bettbio.core.mongo.model;

/**
 * 实验报告
 * 
 * @author chang
 *
 */
public class ExperimentReport extends IncrementModel {
	private String remarks;// 实验备注
	private String imgUrl;// 实验报告图片路径
	private String content;// 实验报告内容
	private String createDate;// 创建时间
	private String updateDate;// 更新时间

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	
}
