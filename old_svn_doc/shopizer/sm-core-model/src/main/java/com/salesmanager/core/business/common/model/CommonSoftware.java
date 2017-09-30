package com.salesmanager.core.business.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name = "COMMON_SOFTWARE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class CommonSoftware extends SalesManagerEntity<Long, CommonSoftware>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1756598264132186105L;

	@Id
	@Column(name = "COMMON_SOFTWARE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "COMMONSOFTWARE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="TYPE", length=1)
	private Integer type=0; //定义软件类型，默认为0，常用软件

	@Column(name="NAME", length=100)
	private String name; //软件名称
	
	@Column(name="FILENAME", length=100)
	private String filename; //软件文件名
	
	@Column(name="ICONNAME", length=100)
	private String iconname; //软件图标
	
	@Column(name="SIZE", length=20)
	private String size; //软件大小
	
	@Column(name="BRIEF", length=1000)
	private String brief; //简单描述
	
	@Column(name="DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String description; //
	
	@Column(name="TORDER", length=9)
	private Integer torder=0; //
	
	@Column(name="CLICKNUM", length=9)
	private Integer clicknum=0; //下载次数
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTorder() {
		return torder;
	}

	public void setTorder(Integer torder) {
		this.torder = torder;
	}

	public Integer getClicknum() {
		return clicknum;
	}

	public void setClicknum(Integer clicknum) {
		this.clicknum = clicknum;
	}

	public String getIconname() {
		return iconname;
	}

	public void setIconname(String iconname) {
		this.iconname = iconname;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
}
