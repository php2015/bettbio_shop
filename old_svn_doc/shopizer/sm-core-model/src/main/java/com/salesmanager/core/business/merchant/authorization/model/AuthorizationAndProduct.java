package com.salesmanager.core.business.merchant.authorization.model;

import java.io.Serializable;

public class AuthorizationAndProduct implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer authid;
    private Integer authtypeid;
	public Integer getAuthid() {
		return authid;
	}
	public void setAuthid(Integer authid) {
		this.authid = authid;
	}
	public Integer getAuthtypeid() {
		return authtypeid;
	}
	public void setAuthtypeid(Integer authtypeid) {
		this.authtypeid = authtypeid;
	}
   
}
