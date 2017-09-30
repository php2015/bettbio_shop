package com.terapico.pricingimpl;

import java.math.BigDecimal;
import java.util.List;

import com.terapico.pricing.CalculationHistory;
import com.terapico.pricing.PriceCalculateResult;
import com.terapico.pricing.PricingConstants.CALCULATOR_TYPE;
import com.terapico.pricing.PricingConstants.PRICE_SCOPE;
import com.terapico.pricing.PricingDetail;
import com.terapico.pricing.PricingOrder;
import com.terapico.pricing.PricingOrderItem;
import com.terapico.pricing.PricingPackage;

public class BasePromotionPriceCalculator extends BasePricingCalculator {

	@Override
	public boolean canApplyToOrder(PricingOrder order) {
		// 优惠信息永远不能直接应用于order，必须在ListPrice算过之后才能使用。
		return false;
	}

	@Override
	public boolean maybeUsedLater(PriceCalculateResult newResult) {
		throw new RuntimeException("You must override boolean maybeUsedLater(PriceCalculateResult)");
	}
	
	@Override
	/**
	 * 缺省不能使用任何新的促销信息。任何实现必须重载此方法。
	 * @param newResult
	 * @return
	 */
	public boolean canApplyToPricingResult(PriceCalculateResult result) {
		throw new RuntimeException("You must override boolean canApplyToPricingResult(PriceCalculateResult)");
	}

	@Override
	/**
	 * 缺省不会使用任何新的促销信息算价。任何实现必须重载此方法。
	 * @param newResult
	 */
	protected void calculatePrice(PriceCalculateResult result) {
		PricingDetail detail = calcDiscount(result);
		appendCalcateHistory(result, detail);
	}

	protected void appendCalcateHistory(PriceCalculateResult result, PricingDetail detail) {
		CalculationHistory calcHistory = new CalculationHistory();
		calcHistory.setCalculatorType(getType());
		calcHistory.setSubCalculatorType(getSubType());
		calcHistory.setPriceDetail(detail);
		result.addCalculationHistory(calcHistory);
	}

	/**
	 * 规则先搞简单点：不论之前有没有优惠被使用，重新以原始价格为基础计算所有的
	 * @param result
	 * @return 
	 */
	protected  PricingDetail calcDiscount(PriceCalculateResult result) {
		PricingOrder order = result.getInputOrder();
		List<PricingPackage> pcks = order.getPackages();
		BigDecimal orderTotal = new BigDecimal(0);
		
		PricingDetail orderPricingDetail = PricingDetail.create(PRICE_SCOPE.ORDER_TOTAL, null);
		for(PricingPackage pck : pcks){
			PricingDetail packagePricingDetail = PricingDetail.create(PRICE_SCOPE.PACKAGE_TOTAL, null);
			packagePricingDetail.setBoundToSequenceNo(pck.getSequenceNo());
			BigDecimal pckTotal = new BigDecimal(0);
			List<PricingOrderItem> items = pck.getItems();
			for(PricingOrderItem item : items){
				BigDecimal discountUnitPrice = calcItemUnitPrice(order, pck, item);
				PricingDetail itemUnitPricingDetail = PricingDetail.create(PRICE_SCOPE.ITEM_UNIT, discountUnitPrice);
				BigDecimal itemTotal = discountUnitPrice.multiply(item.getQuantity());
				PricingDetail itemTotalPricingDetail = PricingDetail.create(PRICE_SCOPE.ITEM_TOTAL, itemTotal);
				itemTotalPricingDetail.addChild(itemUnitPricingDetail);
				packagePricingDetail.addChild(itemTotalPricingDetail);
				pckTotal = pckTotal.add(itemTotal);
			}
			packagePricingDetail.setAmmount(pckTotal);
			orderTotal = orderTotal.add(pckTotal);
			orderPricingDetail.addChild(packagePricingDetail);
		}
		orderPricingDetail.setAmmount(orderTotal);
		return orderPricingDetail;
	}

	protected BigDecimal calcItemUnitPrice(PricingOrder order, PricingPackage pck, PricingOrderItem item) {
		throw new RuntimeException("You must override boolean calcItemUnitPrice(PricingOrder, PricingPackage, PricingOrderItem)");
	}

	@Override
	public CALCULATOR_TYPE getType() {
		return CALCULATOR_TYPE.CALC_DISCOUNT;
	}

	@Override
	public String getSubType() {
		throw new RuntimeException("You must override boolean getSubType()");
	}

	

}
