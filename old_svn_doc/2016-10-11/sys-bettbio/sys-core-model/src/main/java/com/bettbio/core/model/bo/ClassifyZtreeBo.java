package com.bettbio.core.model.bo;

/**
 * ztree后台模型
 * @author simon
 *
 */
public class ClassifyZtreeBo {
	private int classId ;//ID
	private String id; //分类编号
	private String code;
	private int pId ;//父级分类
	private String name ;//分类名称
	private String isParent;;//是否为父节点 
	private int isDisable;//确认禁用
	
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsDisable() {
		return isDisable;
	}
	public void setIsDisable(int isDisable) {
		this.isDisable = isDisable;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
