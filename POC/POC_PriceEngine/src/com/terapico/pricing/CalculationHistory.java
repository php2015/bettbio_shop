package com.terapico.pricing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terapico.pricing.PricingConstants.CALCULATOR_TYPE;
import com.terapico.pricing.PricingConstants.PRICE_SCOPE;

public class CalculationHistory {

	protected CALCULATOR_TYPE calculatorType;
	protected String subCalculatorType;
	protected PricingDetail priceDetail;
	protected Map<String, PricingDetail> pricingDetailIndexMap;
	protected Map<String, Object> additionalInfo;
	public CALCULATOR_TYPE getCalculatorType() {
		return calculatorType;
	}
	public void setCalculatorType(CALCULATOR_TYPE calculatorType) {
		this.calculatorType = calculatorType;
	}
	public String getSubCalculatorType() {
		return subCalculatorType;
	}
	public void setSubCalculatorType(String subCalculatorType) {
		this.subCalculatorType = subCalculatorType;
	}
	public PricingDetail getPriceDetail() {
		return priceDetail;
	}
	public void setPriceDetail(PricingDetail priceContent) {
		this.priceDetail = priceContent;
		this.rebuildPricingDetailIndex();
	}
	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public void addAdditionalInfo(String key, Object value){
		ensureAdditionalInfo();
		additionalInfo.put(key, value);
	}
	protected void ensureAdditionalInfo() {
		if (additionalInfo == null){
			additionalInfo = new HashMap<String, Object>();
		}
	}
	public void rebuildPricingDetailIndex(){
		pricingDetailIndexMap = null;
		ensurePricingDetailIndexMap();
		addPricingDetailIntoIndex(priceDetail);
	}
	
	protected void addPricingDetailIntoIndex(PricingDetail priceDetail) {
		if (priceDetail == null){
			return;
		}
		String key = clacPricingDetailKey(priceDetail.getScope(), priceDetail.getBoundToSequenceNo());
		pricingDetailIndexMap.put(key, priceDetail);
		List<PricingDetail> children = priceDetail.getChildPriceDetails();
		if (children == null){
			return;
		}
		for(PricingDetail child:children){
			addPricingDetailIntoIndex(child);
		}
	}
	protected String clacPricingDetailKey(PRICE_SCOPE scope, int boundToSequenceNo) {
		switch (scope){
		case PACKAGE_TOTAL:
		case ITEM_TOTAL:
		case ITEM_UNIT:
		case SHIPPING_FEE:
		case TAX_FEE:
			return scope.name()+"_"+boundToSequenceNo;
		default:
			break;
		}
		return scope.name();
	}
	protected void ensurePricingDetailIndexMap() {
		if (pricingDetailIndexMap == null){
			pricingDetailIndexMap = new HashMap<String, PricingDetail>();
		}
	}
	public int getPricingDetailsCount(){
		ensurePricingDetailIndexMap();
		return pricingDetailIndexMap.size();
	}
}
