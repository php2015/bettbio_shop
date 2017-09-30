package com.terapico.pricing;

import java.util.List;

public class PricingPackage {
	protected int sequenceNo;
	protected List<PricingOrderItem> items;
	public List<PricingOrderItem> getItems() {
		return items;
	}
	public void setItems(List<PricingOrderItem> items) {
		this.items = items;
	}
	public int getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
}
