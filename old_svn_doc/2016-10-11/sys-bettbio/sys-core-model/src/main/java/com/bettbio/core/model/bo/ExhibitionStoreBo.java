package com.bettbio.core.model.bo;

import com.bettbio.core.model.SStore;

public class ExhibitionStoreBo extends SStore{

	private Integer isAffirm;

	private Integer exhibitionId;

	private String fileUrl;
	
	public Integer getIsAffirm() {
		return isAffirm;
	}

	public void setIsAffirm(Integer isAffirm) {
		this.isAffirm = isAffirm;
	}

	public Integer getExhibitionId() {
		return exhibitionId;
	}

	public void setExhibitionId(Integer exhibitionId) {
		this.exhibitionId = exhibitionId;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
}
