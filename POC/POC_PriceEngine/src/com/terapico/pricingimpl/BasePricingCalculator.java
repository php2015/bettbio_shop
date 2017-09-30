package com.terapico.pricingimpl;

import java.util.ArrayList;
import java.util.List;

import com.terapico.pricing.CalculationHistory;
import com.terapico.pricing.PriceCalculateResult;
import com.terapico.pricing.PriceCalculator;
import com.terapico.pricing.PriceResultSelector;
import com.terapico.pricing.PricingException;

/**
 * 基础的算价器。
 * @author clari
 *
 */
public abstract class BasePricingCalculator implements PriceCalculator {

	/**
	 * 完成算价的完整操作。
	 * @param currentCalculateResult
	 * @return
	 * @throws PricingException 
	 */
	public PriceCalculateResult calculate(PriceCalculateResult currentCalculateResult) throws PricingException {
		// 1. create result object to store result of this step
		PriceCalculateResult newResult = prepareNewCalculateResult(currentCalculateResult);
		// 2. do the real calculation. include add new CalculationHistory
		calculatePrice(newResult);
		// TODO: debug
		System.out.println("\ncalculator /" + this.getType()+"/"+this.getSubType()+" result:" + newResult);
		// 3. if no more possible calculators, then stop the calculating-chain
		List<PriceCalculator> oldCalculators = currentCalculateResult.getPossiblePricingCalculators();
		if (oldCalculators == null || oldCalculators.isEmpty()){
			return newResult;
		}
		
		// if still have more calculators, do the iteration of calculation
		// 4. filter calculators which never used in this path
		List<PriceCalculator> leftCalculators = new ArrayList<PriceCalculator>();
		for(PriceCalculator calctor : oldCalculators){
			if (calctor.maybeUsedLater(newResult)){
				leftCalculators.add(calctor);
			}
		}
		newResult.setPossiblePricingCalculators(leftCalculators);
		
		// 5. go through any left calculators
		PriceCalculateResult lastResult = newResult;
		PriceResultSelector prcSelector = newResult.getPriceResultSelector();
		for(PriceCalculator clcltr : leftCalculators){
			// filter any calculators which not available anymore
			if (!clcltr.canApplyToPricingResult(newResult)){
				continue;
			}
			PriceCalculateResult newerResult = clcltr.calculate(newResult);
			if (newerResult == null){
				continue;
			}
			lastResult = prcSelector.selectBetterPricingResult(newerResult, newerResult);
		}
		return lastResult;
	}
	
	/**
	 * 使用本‘算价器’的规则，对订单价格结果进行修正,包括添加新的CalculationHistory
	 * @param newResult
	 * @throws PricingException 
	 */
	protected abstract void calculatePrice(PriceCalculateResult newResult) throws PricingException;

	/**
	 * 为新的计算准备result对象。<p/>
	 * 1. 订单本身引用过来。
	 * 2. 可能的算价器列表不拷贝，需要后面计算过程中调整。
	 * 3. 结果选择器引用过来。
	 * 4. 算价历史拷贝过来（浅拷贝）
	 * @param currentCalculateResult
	 * @return
	 */
	protected PriceCalculateResult prepareNewCalculateResult(PriceCalculateResult currentCalculateResult) {
		PriceCalculateResult newResult = new PriceCalculateResult();
		newResult.setInputOrder(currentCalculateResult.getInputOrder());	// 1
		newResult.setPriceResultSelector(currentCalculateResult.getPriceResultSelector()); // 3
		if (currentCalculateResult.getAppliedCalculationSteps() != null) { // 4
			newResult.setAppliedCalculationSteps(
					new ArrayList<CalculationHistory>(currentCalculateResult.getAppliedCalculationSteps()));
		}
		return newResult;
	}

}
