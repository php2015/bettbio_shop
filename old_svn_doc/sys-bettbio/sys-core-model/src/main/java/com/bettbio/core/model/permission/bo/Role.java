package com.bettbio.core.model.permission.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.bettbio.core.model.SysRole;

/**
 * 基于角色的抽象
 * 
 * @author GuoChunbo
 *
 */
public class Role extends SysRole {

	private List<Integer> resourceIds;

	public Role(){}
	
	public Role(String name,String code,Integer isDisable,List<Integer> resourceIds){
		setName(name);
		setCode(code);
		setIsDisable(isDisable);
		setCreateTime(new Date());
		this.resourceIds = resourceIds;
	}
	
	
	/**
	 * 权限集合转换为string用于添加
	 * 
	 * @return
	 */
	public String getResourceIdsStr() {
		if (CollectionUtils.isEmpty(resourceIds)) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		for (Integer roleId : resourceIds) {
			s.append(roleId);
			s.append(",");
		}
		return s.toString();
	}

	/**
	 * 取资源权限Ids集合
	 * 
	 * @return
	 */
	public List<Integer> getResourceIds() {
		if (resourceIds == null) {
			resourceIds = new ArrayList<Integer>();
		}
		if (StringUtils.isEmpty(getJurisdictionIds())) {
			return resourceIds;
		}
		String[] roleIdStrs = getJurisdictionIds().split(",");
		for (String roleIdStr : roleIdStrs) {
			if (StringUtils.isEmpty(roleIdStr)) {
				continue;
			}
			resourceIds.add(Integer.valueOf(roleIdStr));
		}
		return resourceIds;
	}

	public void setResourceIds(List<Integer> resourceIds) {
		this.resourceIds = resourceIds;
	}
	
	@Override
	public String toString() {
		 return "Role{" +
	                "name='" + getName() + '\'' +
	                ", code='" + getCode() + '\'' +
	                ", isDisable=" + getIsDisable() +
	                ", resourceIds='" + getJurisdictionIds() + '\'' +
	                '}';
	}
}
