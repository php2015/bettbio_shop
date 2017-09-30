package com.bettbio.core.mongo.model;

import com.bettbio.core.common.web.utils.CodeUtils;

/**
 * 文献引用
 * 
 * @author chang
 *
 */
public class Literature extends IncrementModel{
	private String periodicalName;// 期刊名称
	private String periodicalCode;// 期刊号
	private String title;// 标题
	private String url;// 文献链接地址
	private String imgUrl;// 文献图片路径
	private String content;// 文献文档
	private String createDate;// 创建时间
	private String updateDate;// 更新时间
	private int index;// 序列

	public String getPeriodicalName() {
		return periodicalName;
	}

	public void setPeriodicalName(String periodicalName) {
		this.periodicalName = periodicalName;
	}

	public String getPeriodicalCode() {
		return periodicalCode;
	}

	public void setPeriodicalCode(String periodicalCode) {
		this.periodicalCode = periodicalCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
