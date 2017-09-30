package com.salesmanager.core.business.newscontent.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Type;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CustomDateSerializer;

@Entity
@Table(name = "NEWS_CONTENT", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class NewsContent extends SalesManagerEntity<Long, NewsContent> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3701535155763337394L;

	@Id
	@Column(name = "NEWS_CONTENT_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "NEWS_CONTENT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	

	@Column(name="CODE", length=100)
	private String code;
	
	@Column(name="NAME", length=255)
	private String name;
	
	/**
	 * 链接的地址
	 */
	@Column(name="LINKHREF", length=500)
	private String linkHref;
	/**
	 * 链接的标题
	 */
	@Column(name="LINKTEXT", length=200)
	private String linkText;
	/**
	 * 关键词
	 */
	@Column(name="KEYWORDS", length=200)
	private String keywords;
	/**
	 * 摘要
	 */
	@Column(name="SUMMARY", length=1000)
	private String summary;
	/**
	 * 内容
	 */
	@Column(name="CONTENT")
	@Type(type = "org.hibernate.type.StringClobType")
	private String content;
	/**
	 * 图片
	 * @return
	 */
	@Column(name="IMAGE", length=200)
	private String image;
	/**
	 * 数据来源类型
	 */
	@Column(name="TYPE", length=2)
	private WebsiteEnumType type;
	/**
	 * 发布时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_PUBLISH")
	private Date publishdt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED")
	private Date createdt;
	
	@Column(name="ISPUBLISH", length=1)
	private Boolean ispublish=true;  //是否发布，0不发布，1发布
	
	@Column(name="USERCODE", length=50)
	private String usercode="system";
	
	
	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public NewsContent() {
		super();
	}

	public NewsContent(WebsiteEnumType type) {
		super();
		this.type = type;
	}

	public NewsContent(WebsiteEnumType type, Date createdt) {
		super();
		this.type = type;
		this.createdt = createdt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLinkHref() {
		return linkHref;
	}

	public void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public WebsiteEnumType getType() {
		return type;
	}

	public void setType(WebsiteEnumType type) {
		this.type = type;
	}

	public Date getPublishdt() {
		return publishdt;
	}

	public void setPublishdt(Date publishdt) {
		this.publishdt = publishdt;
	}

	public Date getCreatedt() {
		return createdt;
	}

	public void setCreatedt(Date createdt) {
		this.createdt = createdt;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Boolean getIspublish() {
		return ispublish;
	}

	public void setIspublish(Boolean ispublish) {
		this.ispublish = ispublish;
	}
	
}
