package com.bettbio.core.mongo.model;
/**
 * 买家信息
 * @author chang
 *
 */
public class Buyers {
 private Integer id;
 private String name;//买家名称或机构名称
 private int type;//0买家 1机构
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
 
}
