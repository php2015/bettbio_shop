package com.salesmanager.core.business.common.model.audit;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.utils.CloneUtils;

@Embeddable
//@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuditSection implements Serializable {


	private static final long serialVersionUID = -1934446958975060889L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_MODIFIED")
	private Date dateModified;

	@Column(name = "UPDT_ID", length=20)
	private String modifiedBy;
	
	/**
	 * -2：审核失败
	 * -1:未提交审核资料
	 * 0:待审核
	 * 1:已审核
	 * 2:待再次审核
	 */
	@Column(name = "PRODUCT_AUDIT")
	private int audit = -1;
	
	public AuditSection() {
		Date dt = new Date();
		dateCreated = dt;
		dateModified = dt;
	}
	
	public AuditSection(String modifiedBy) {
		Date dt = new Date();
		this.dateCreated = dt;
		this.dateModified = dt;
		this.modifiedBy = modifiedBy;
	}

	public Date getDateCreated() {
		return CloneUtils.clone(dateCreated);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = CloneUtils.clone(dateCreated);
	}

	public Date getDateModified() {
		return CloneUtils.clone(dateModified);
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = CloneUtils.clone(dateModified);
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}
}
