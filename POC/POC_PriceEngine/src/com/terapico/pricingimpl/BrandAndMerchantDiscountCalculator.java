package com.terapico.pricingimpl;

import java.math.BigDecimal;
import java.util.List;

import com.terapico.pricing.CalculationHistory;
import com.terapico.pricing.PriceCalculateResult;
import com.terapico.pricing.PricingConstants.PRICE_SCOPE;
import com.terapico.pricing.PricingDetail;
import com.terapico.pricing.PricingOrder;
import com.terapico.pricing.PricingOrderItem;
import com.terapico.pricing.PricingPackage;

public class BrandAndMerchantDiscountCalculator extends BasePromotionPriceCalculator {

	/**
	 * 折扣类算价器，如果订单已经算过了除list price和discount 以外的价格，就不能再用了
	 */
	@Override
	public boolean maybeUsedLater(PriceCalculateResult result) {
		List<CalculationHistory> list = result.getAppliedCalculationSteps();
		for(CalculationHistory his : list){
			switch(his.getCalculatorType()){
			case CALC_LIST_PRICE:
			case CALC_DISCOUNT:
				break;
			default:
				return false;
			}
		}
		return true;
	}

	/**
	 * 如果没算过ListPrice, 不能用。
	 * 如果已经算过Brand_Merchant, 也不能用。
	 */	@Override
	public boolean canApplyToPricingResult(PriceCalculateResult result) {
		boolean hasListPrice = false;
		boolean hasBrandMerchantDiscount = false;
		List<CalculationHistory> list = result.getAppliedCalculationSteps();
		for(CalculationHistory his : list){
			switch(his.getCalculatorType()){
			case CALC_LIST_PRICE:
				hasListPrice = true;
				break;
			case CALC_DISCOUNT:
				if (getSubType().equals(his.getSubCalculatorType())){
					hasBrandMerchantDiscount = true;
				}
				break;
			default:
				break;
			}
		}
		return hasListPrice && !hasBrandMerchantDiscount;
	}

	@Override
	public String getSubType() {
		return "brand_merchant_discount";
	}

	protected BigDecimal calcItemUnitPrice(PricingOrder order, PricingPackage pck, PricingOrderItem item) {
		// TODO Auto-generated method stub
		return item.getOrignalUnitPrice().multiply(new BigDecimal(0.78));
	}
	
	

}
