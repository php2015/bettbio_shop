package com.terapico.pricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terapico.pricing.PricingConstants.PRICE_SCOPE;

public class PricingDetail implements Cloneable{
	protected PRICE_SCOPE scope;
	protected String comments;
	protected BigDecimal ammount;
	protected int boundToSequenceNo;
	protected List<PricingDetail> childPriceDetails;
	protected Map<String, Object> additionalInfo;
	public PRICE_SCOPE getScope() {
		return scope;
	}
	public void setScope(PRICE_SCOPE scope) {
		this.scope = scope;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public BigDecimal getAmmount() {
		return ammount;
	}
	public void setAmmount(BigDecimal ammount) {
		this.ammount = ammount;
	}
	public int getBoundToSequenceNo() {
		return boundToSequenceNo;
	}
	public void setBoundToSequenceNo(int boundToSequenceNo) {
		this.boundToSequenceNo = boundToSequenceNo;
	}
	public List<PricingDetail> getChildPriceDetails() {
		return childPriceDetails;
	}
	public void setChildPriceDetails(List<PricingDetail> childPriceContents) {
		this.childPriceDetails = childPriceContents;
	}
	
	public static PricingDetail create(PRICE_SCOPE scop, BigDecimal ammount){
		PricingDetail result = new PricingDetail();
		result.setScope(scop);
		result.setAmmount(ammount);
		return result;
	}
	public void addChild(PricingDetail childContent) {
		if (childPriceDetails == null){
			childPriceDetails = new ArrayList<PricingDetail>();
		}
		childPriceDetails.add(childContent);
	}
	
	@Override
	public PricingDetail clone() throws CloneNotSupportedException {
		return deepCopy(this);
	}
	protected PricingDetail deepCopy(PricingDetail orgData) {
		if (orgData == null){
			return null;
		}
		PricingDetail newData = new PricingDetail();
		newData.setAmmount(orgData.getAmmount());
		newData.setBoundToSequenceNo(orgData.getBoundToSequenceNo());
		newData.setComments(orgData.getComments());
		newData.setScope(orgData.getScope());
		if (orgData.getAdditionalInfo() != null){
			newData.setAdditionalInfo(new HashMap<String, Object>(orgData.getAdditionalInfo()));
		}
		List<PricingDetail> orgChildren = orgData.getChildPriceDetails();
		if (orgChildren == null){
			return newData;
		}
		for(PricingDetail child:orgChildren){
			newData.addChild(deepCopy(child));
		}
		return newData;
	}
	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public void addAdditinalInfo(String key, Object value){
		ensureAdditinalInfo();
		additionalInfo.put(key, value);
	}
	private void ensureAdditinalInfo() {
		if (additionalInfo == null){
			additionalInfo = new HashMap<String, Object>();
		}
	}
	
}
