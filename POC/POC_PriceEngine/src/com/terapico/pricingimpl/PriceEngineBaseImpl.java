package com.terapico.pricingimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.terapico.pricing.PriceCalculateResult;
import com.terapico.pricing.PriceCalculator;
import com.terapico.pricing.PriceEngine;
import com.terapico.pricing.PriceResultSelector;
import com.terapico.pricing.PricingException;
import com.terapico.pricing.PricingOrder;
import com.terapico.pricing.PricingOrderItem;
import com.terapico.pricing.PricingPackage;

public class PriceEngineBaseImpl implements PriceEngine{
	
	protected List<PriceCalculator> calculators;
	protected PriceResultSelector priceResultSelector;
	
	
	/**
	 * 算价的步骤如下：
	 * <ol>
	 * <li>首先假设所有的优惠都可能被应用，因此先根据order的输入，对所有的优惠计算器{@link PriceCalculator}做遍历，获得有效的优惠计算器</li>
	 * <li>然后依次对有效的优惠计算器进行使用</li>
	 * <li>每个优惠计算器会得到一个当前计算器所使用的规则下，获得的最优算价结果</li>
	 * <li>然后由优惠选择器{@link PriceResultSelector}，根据计算结果，选出一个适用方案</li>
	 * <li>每个优惠计算器内部可能会进行同样的迭代过程,以保证正确的优惠重叠的结果</li>
	 * </ol>
	 * @param order
	 * @return
	 * @throws PricingException 
	 */
	public PriceCalculateResult calculate(PricingOrder order) throws PricingException {
		verifyOrderBasicInfo(order);
		
		PriceCalculateResult  result = new PriceCalculateResult();
		result.setInputOrder(order);
		List<PriceCalculator> validcalculators = findAllValidCalculators(order);
		if (validcalculators == null || validcalculators.isEmpty()){
			throw new PricingException("Cannot found any valid calculator");
		}
		result.setPossiblePricingCalculators(validcalculators);
		result.setPriceResultSelector(getPriceResultSelector());
		
		return doCalculate(result);
	}

	protected void verifyOrderBasicInfo(PricingOrder order) throws PricingException {
		List<PricingPackage> pcks = order.getPackages();
		if (pcks == null || pcks.isEmpty()){
			throw new PricingException("Order" + order.getSource()+"_"+order.getOrderId()+" is empty");
		}
		for(PricingPackage pck : pcks){
			List<PricingOrderItem> items = pck.getItems();
			if (items == null || items.isEmpty()){
				throw new PricingException("Order" + order.getSource()+"_"+order.getOrderId()+" has empty package");
			}
			for(PricingOrderItem item : items){
				if (item.getOrignalUnitPrice() == null){
					throw new PricingException("Order" + order.getSource()+"_"+order.getOrderId()+" item " + item.getSequenceNo()+" has no unit price");
				}
				if (item.getQuantity() == null){
					throw new PricingException("Order" + order.getSource()+"_"+order.getOrderId()+" item " + item.getSequenceNo()+" has no quantity");
				}
			}
		}
	}
	
	/**
	 * 算价方案是不能无限制扩展的，如果有新的算价方案，需要严格的对照已有的算价方案，确定其计算和过滤逻辑。
	 * 所以不建议重载这个函数。<br/>
	 * 这里返回固定的几个优惠计算器，第一个必然是目录价计算器{@link ListPriceCalculator}.
	 * 然后是预配置的几种优惠：品牌-商家，商家-类别，商家-产品
	 * @param order
	 * @return
	 */
	protected List<PriceCalculator> findAllValidCalculators(PricingOrder order) {
		List<PriceCalculator> result = new ArrayList<PriceCalculator>();
		result.add(new ListPriceCalculator());
		if (calculators == null || calculators.isEmpty()){
			return result;
		}
//		for(PriceCalculator calc : calculators){
//			if (calc.canApplyToOrder(order)){
//				result.add(calc);
//			}
//		}
		result.addAll(calculators);
		return result;
	}

	protected PriceCalculateResult doCalculate(PriceCalculateResult currentCalculateResult) throws PricingException{
		List<PriceCalculator> validCaculators = currentCalculateResult.getPossiblePricingCalculators();
		if (validCaculators == null || validCaculators.isEmpty()){
			return currentCalculateResult;
		}
		PriceCalculateResult lastResult = currentCalculateResult;
		for(PriceCalculator clcltr : validCaculators){
			if (!clcltr.canApplyToOrder(currentCalculateResult.getInputOrder())){
				continue;
			}
			PriceCalculateResult newResult = clcltr.calculate(currentCalculateResult);
			if (newResult == null){
				continue;
			}
			lastResult = currentCalculateResult.getPriceResultSelector().selectBetterPricingResult(lastResult, newResult);
		}
		return lastResult;
	}

	public List<PriceCalculator> getCalculators() {
		return calculators;
	}

	public void setCalculators(List<PriceCalculator> calculators) {
		this.calculators = calculators;
	}

	public PriceResultSelector getPriceResultSelector() {
		return priceResultSelector;
	}

	public void setPriceResultSelector(PriceResultSelector priceResultSelector) {
		this.priceResultSelector = priceResultSelector;
	}

}
