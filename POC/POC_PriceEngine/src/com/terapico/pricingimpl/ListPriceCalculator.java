package com.terapico.pricingimpl;

import java.math.BigDecimal;
import java.util.List;

import com.terapico.pricing.CalculationHistory;
import com.terapico.pricing.PriceCalculateResult;
import com.terapico.pricing.PricingDetail;
import com.terapico.pricing.PricingConstants.CALCULATOR_TYPE;
import com.terapico.pricing.PricingConstants.PRICE_SCOPE;
import com.terapico.pricing.PricingException;
import com.terapico.pricing.PricingOrder;
import com.terapico.pricing.PricingOrderItem;
import com.terapico.pricing.PricingPackage;

/**
 * 目录价 算价器只在开始算价时应用一次，必须应用于order，不能应用于PriceCalculateResult。
 */
public class ListPriceCalculator extends BasePricingCalculator {

	@Override
	public boolean canApplyToOrder(PricingOrder order) {
		return true;
	}
	

	@Override
	public boolean canApplyToPricingResult(PriceCalculateResult newResult) {
		return false;
	}

	@Override
	public boolean maybeUsedLater(PriceCalculateResult newResult) {
		return false;
	}

	@Override
	protected void calculatePrice(PriceCalculateResult priceResult) throws PricingException {
		PricingOrder order = priceResult.getInputOrder();
		List<PricingPackage> pcks = order.getPackages();
		// no need to check if pcks is null, it was checked in PriceEngineBaseImpl.calculate()
		assert(pcks != null);
		PricingDetail orderPriceContent = PricingDetail.create(PRICE_SCOPE.ORDER_TOTAL, null);
		BigDecimal orderTotal = new BigDecimal(0);
		for(PricingPackage pck : pcks){
			BigDecimal pckTotal = new BigDecimal(0);
			assert(pck.getItems() != null);
			PricingDetail packagePriceContent = PricingDetail.create(PRICE_SCOPE.PACKAGE_TOTAL, null);
			packagePriceContent.setBoundToSequenceNo(pck.getSequenceNo());
			for(PricingOrderItem item : pck.getItems()){
				BigDecimal itemTotal = item.getOrignalUnitPrice().multiply(item.getQuantity());
				
				PricingDetail itemTotalPriceContent = PricingDetail.create(PRICE_SCOPE.ITEM_TOTAL, itemTotal);
				itemTotalPriceContent.setBoundToSequenceNo(item.getSequenceNo());
				PricingDetail itemUnitPriceContent = PricingDetail.create(PRICE_SCOPE.ITEM_UNIT, item.getOrignalUnitPrice());
				itemUnitPriceContent.setBoundToSequenceNo(item.getSequenceNo());
				
				itemTotalPriceContent.addChild(itemUnitPriceContent);
				packagePriceContent.addChild(itemTotalPriceContent);
				
				item.setOrignalTotalPrice(itemTotal);
				pckTotal = pckTotal.add(itemTotal);
			}
			packagePriceContent.setAmmount(pckTotal);
			orderPriceContent.addChild(packagePriceContent);
			orderTotal = orderTotal.add(pckTotal);
		}
		orderPriceContent.setAmmount(orderTotal);
		
		CalculationHistory calcHistory = new CalculationHistory();
		calcHistory.setCalculatorType(getType());
		calcHistory.setSubCalculatorType(getSubType());
		calcHistory.setPriceDetail(orderPriceContent);
		priceResult.addCalculationHistory(calcHistory);
	}


	@Override
	public CALCULATOR_TYPE getType() {
		return CALCULATOR_TYPE.CALC_LIST_PRICE;
	}


	@Override
	public String getSubType() {
		return null;
	}

	
}
