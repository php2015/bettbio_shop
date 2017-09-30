package com.bettbio.core.model.permission.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.bettbio.core.model.SStoreUser;

/**
 * 基于商家用户的抽象
 * 
 * @author GuoChunbo
 *
 */
public class StoreUser extends SStoreUser {

	private List<Integer> roleIdsList;
	
	public StoreUser(){}
	
	public StoreUser(Integer id,String roleIds){
		setId(id);
		setRoleIds(roleIds);
	}
	/**
	 * 权限集合转换为string用于添加
	 * 
	 * @return
	 */
	public String getRoleIdsStr() {
		if (CollectionUtils.isEmpty(roleIdsList)) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		for (Integer roleId : roleIdsList) {
			s.append(roleId);
			s.append(",");
		}
		return s.toString();
	}

	/**
	 * 取角色Ids集合
	 * 
	 * @return
	 */
	public List<Integer> getRoleIdsList() {
		if (roleIdsList == null) {
			roleIdsList = new ArrayList<Integer>();
		}
		if (StringUtils.isEmpty(getRoleIds())) {
			return roleIdsList;
		}
		String[] roleIdStrs = getRoleIds().split(",");
		for (String roleIdStr : roleIdStrs) {
			if (StringUtils.isEmpty(roleIdStr)) {
				continue;
			}
			roleIdsList.add(Integer.valueOf(roleIdStr));
		}
		return roleIdsList;
	}

	public void setRoleIdsList(List<Integer> roleIdsList) {
		this.roleIdsList = roleIdsList;
	}
	
	@Override
	public String toString() {
		return "StoreUser{"
				+ "id=" + getId()
				+ ", account='" + getAccount() + '\''
				+ ", roleIds='"+ getRoleIds() + '\''
				+ "}";
	}
}
