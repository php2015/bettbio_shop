package com.bettbio.core.model.permission.bo;

import com.bettbio.core.model.SysJurisdiction;

/**
 * 基于权限的抽象
 * @author GuoChunbo
 *
 */
public class Resource extends SysJurisdiction {

	public Resource(){}
	
	public Resource(String code,String name,String url,Integer isDisable,String parentCode){
		setCode(code);
		setName(name);
		setUrl(url);
		setIsDisable(isDisable);
		setParentCode(parentCode);
	}
	
	
	@Override
	public String toString() {
		return "Resource{"
				+ "id=" + getId()
				+ ", code='"+getCode() + '\''
				+ ", name='"+getName() + '\''
				+ ", url='"+getUrl() + '\''
				+ ", isDisable="+getIsDisable()
				+ ", parentCode='"+getParentCode() + '\''
				+ "}";
	}
}
