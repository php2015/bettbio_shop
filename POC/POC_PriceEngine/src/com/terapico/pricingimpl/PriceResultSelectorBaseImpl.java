package com.terapico.pricingimpl;

import java.util.List;

import com.terapico.pricing.CalculationHistory;
import com.terapico.pricing.PriceCalculateResult;
import com.terapico.pricing.PriceResultSelector;
import com.terapico.pricing.PricingDetail;

public class PriceResultSelectorBaseImpl implements PriceResultSelector {

	@Override
	public PriceCalculateResult selectBetterPricingResult(PriceCalculateResult lastResult,
			PriceCalculateResult newResult) {
		// 先以“非空优于空值”选择
		if (lastResult == newResult){
			return newResult;
		}
		if (lastResult == null){
			return newResult;
		}
		if (newResult == null){
			return lastResult;
		}
		
		// 再以“算过优于未算”选择
		List<CalculationHistory> history0 = lastResult.getAppliedCalculationSteps();
		List<CalculationHistory> history1 = newResult.getAppliedCalculationSteps();
		if (history0 == null || history0.isEmpty()){
			return newResult;
		}
		if (history1 == null || history1.isEmpty()){
			return lastResult;
		}
		// 再以“更多细节优先”选择
		int n0 = history0.get(history0.size()-1).getPricingDetailsCount();
		int n1 = history0.get(history0.size()-1).getPricingDetailsCount();
		if (n0 > n1){
			return lastResult;
		}
		if (n1 > n0){
			return newResult;
		}
		// 最后选择“总价最低”
		PricingDetail detail0 = history0.get(history0.size()-1).getPriceDetail();
		PricingDetail detail1 = history1.get(history0.size()-1).getPriceDetail();
		if (detail1.getAmmount().compareTo(detail0.getAmmount()) > 0){
			return lastResult;
		}
		return newResult;
	}

}
