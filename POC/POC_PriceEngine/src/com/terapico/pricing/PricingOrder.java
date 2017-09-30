package com.terapico.pricing;

import java.util.List;

import com.terapico.pricing.PricingConstants.ORDER_SOURCE;

/**
 * <pre>
 * order: {
	version: string. MUST HAVE.
			default is "1.0". This used for compatibility in future, in case we have major structure change. 
	source: string. MUST HAVE.
			where the order come from. must be a string same as what defined in OrderConstants.ORDER_SOURCE
	orderId: string, MUST HAVE.
			this is order id in its "source" side. Together with 'source' will locate the whole order-id. for example: BAITU_SITE/4500
	buyerInfo : {
		source: string. 
				indicate where to find the buyer. must same as defined in OrderConstants.BUYER_SOURCE. for example: BAITU_CUSTOMER
				if this empty, will take the default value base on the order.source.
		classifyInSource: string. 
				Classify of customers in source side. Can be empty if not needed. It's depends on buyerInfo.source.
				For example, if 'buyerType'='BAITU_CUSTOMER', this is empty. but in case of 'SOME_HUGE_ORGNIZATION', maybe it has 2 user data tables, may use this field.
		key: string. MUST have.
				indicate by which to find the buyer.
				For example, if 'source'='BAITU_CUSTOMER', it must be CUSTOMER_ID in database.
		additionalData: { object
				this is a map<string, object>. depends on which info you need. Can be empty.
				for example: {
					orgnizations :[ {
							orginizationType: string. currently can be 'region|corperation|Laboratory|project'
							orginizationKey: in this case it should be xxx_id
						},...],
					buyerType : so far can be 'research|industrial|dealer'
				}
				and you still can add any info you need
		}
	},
	shippingAddresses : [{
		receiverName: string. MUST HAVE
		contactMethod: string.
				how to contact with the receiver.
				must be same as defined in OrderConstants.CONTACT_METHOD, e.g EMAIL. all upper case. can be empty.
		contactKey: string.
				use which to contact with the reciever.
				according to contactMethod, can be different format. can be empty.
		country : string. MUST HAVE.
				country  regular name . Must be same as defined in OrderConstants.CONTRY. all upper case.
		areaLevel1: string. MUST HAVE.
				for example: 湖北|hubei|HB|PROVINCE_022
				make sure understood by any necessary actor which inside the order processing, for example, a promotion which defined with "湖北".
		areaLevel2: string.
		areaLevel3: string.
		postCode: string. 
				according to country, maybe in different format. Can be empty
		addressLine1: string. MUST HAVE.
		addressLine2: string.
		itemList : [{
				it's the list of commerceItems.sequenceNo.
				If empty, means all "un-decided items". For example, only 1 shipping address, this left empty means all items.
			sequenceNo: int, MUST HAVE
			quantity: big-decimal
					if empty, means "all left"
		}],
		currentState: {
			stateCode: string. which defined by Baitu. It's unique for all systems.
			stateSubCode: string. which defined by each system. Baitu core-system will ignore this.
		}
		additionalData: object.
				It's a map<string, object>. can be empty.
	}],
	payments: [{
			depends on ORDER_SOURCE, the payment method maybe different rule. 
			If no multi-payment, this is empty in most case. 
			Make sure the payment method is understood for any actor in order processing flow
			so far payments has no relation to commerce items.  
		payMethod: string. MUST HAVE
				should be same as defined in OrderConstants.PAY_METHOD
		payAccountType: string
				depends on payMethod, this may have diffrent means
				for example, if "payMethod" is CREDIT, this should be a bank code.
		payAccountKey: string
		ammount: big decimal. 
		currency: string
		currentState: {
			stateCode: string. which defined by Baitu. It's unique for all systems.
			stateSubCode: string. which defined by each system. Baitu core-system will ignore this.
		}
		additionalData: object.
				It's a map<string, object>. can be empty.
	}],
	invoices : [{
		title: string. MUST HAVE
		content: string.
				invoice content. depends on which kind of order/customer, the content can be in different format which negotiated by related people.
				for example, if order source=BAITU_SITE. the content is empty.
		country : string. MUST HAVE.
				Which county's format the invoice should follow. Affect 'invoiceType'
		invoiceType: string. MUST_HAVE.
			according to 'country', can be different value. Make sure it's understood by any actor in order procedure.
		receiveAddress : object. 
				Its definition is same as 'shippingAddress'. Can be empty.
				Accroding to order.source, empty maybe have different means. 
				For example, source=integle means need send the invoice to an integeration API, so we don't need any address infomation here.
		currentState: {
			stateCode: string. which defined by Baitu. It's unique for all systems.
			stateSubCode: string. which defined by each system. Baitu core-system will ignore this.
		}
		additionalData: object.
				It's a map<string, object>. can be empty.
	}],
	commerceItems : [{	MUST HAVE
		sequenceNo: int. MUST HAVE
				unique int number in this order. like a row-number. This will be used by other data like "shippingAddress", "pricingList".
		itemSource: string
				where to find the item. so far it can be empty, means "from baitu self database"
				later, should be same as defined in OrderConstants.PRODUCT_SOURCE
		productKey: string. MUST HAVE
				should be product level key. for example, it's "PRODUCT_ID" for PRODUCT_SOURCE.BAITU_DATABASE
		skuKey: string. MUST HAVE
				shoule be sku level key, for example, it's a "specification.name" for PRODUCT_SOURCE.BAITU_DATABASE.
		quantity: big-decimal. 
				For example, 0.25inch pipe.
		displayName: string.
		measurementUnit: string, like "inch|pound"
		unitPrice: big-decimal. MUST HAVE
		totalPrice: big-decimal. MUST HAVE
		currency: string. 
		additionalData: { object.
				It's a map<string, object>. for example:
			prodcutLink: uri. an URI lead to product detail page.
			productImage: uri. an URI lead to product mainly picture.
			packageSize: string, like "11x0.5x0.5 inch", a cuboid post
			
		}
	}],
	pricingList : [{
			depends on order process, maybe required pay by different way.
			for example, some order must pay 1000 "points" and all cost cash. In this case, may have 2 pricing records.
		pricingScope: string.
				by default, it's "ORDER". It should be a string same as defined in PRICING_SCOPE
		validPayMethod: [] string list
				can be empty in most case. If have, should keep constancy with payments.payMethod
		orignalAmount: big-decimal. MUST HAVE
		finalAmount: big-decimal. MUST HAVE
		currency: string
		itemList: [{
				same definition of shippingAddresses.itemList
				depends on pricingScope, it may be empty, or a list of ordered products e.g in item scope.
			sequenceNo: int, MUST HAVE
			quantity: big-decimal
					if empty, means "all left"
		}],
		subPricingList: [{...}] same definition of 'pricingList'
		adjustments: [{
			adjustmentType: string. for example "manually change"
			adjustmentKey: string. for example, if adjustmentType is sth like "not count the small change"， this may be an id of action record.
			ammount: big-decimal
			currency: string
		}],
		additionalData: object.
			It's a map<string, object>. can be empty.
	]},
	appliedPromotions : [{
		pricingScope: string. same definition of pricingList.pricingScope
		promotionType: string
		promotionKey:
		ammount: big-decimal
		currency: string
		appliedToItems: [{}] same definition of shippingAddresses.itemList
		subPromotions: [{}] same definition of 'appliedPromotions' with diffrent pricingScope
	}],
	additionalData: object.
			It's a map<string, object>. can be empty.
}
</pre>
 * @author clari
 *
 */
public class PricingOrder {
	protected List<PricingPackage> packages;
	protected ORDER_SOURCE source;
	protected String orderId;

	public List<PricingPackage> getPackages() {
		return packages;
	}

	public void setPackages(List<PricingPackage> packages) {
		this.packages = packages;
	}

	public ORDER_SOURCE getSource() {
		return source;
	}

	public void setSource(ORDER_SOURCE source) {
		this.source = source;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
}
