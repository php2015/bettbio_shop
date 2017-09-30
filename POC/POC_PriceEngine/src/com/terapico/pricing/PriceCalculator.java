package com.terapico.pricing;

import com.terapico.pricing.PricingConstants.CALCULATOR_TYPE;

/**
 * 优惠条款的应用器
 * @author clari
 *
 */
public interface PriceCalculator {
	/**
	 * 该算价器是否可以直接应用于Order.
	 * 目前只有ListPriceCalculator可以。但是不限定以后有新的算价器可以直接计算order价格
	 * @param order
	 * @return
	 */
	boolean canApplyToOrder(PricingOrder order);

	/**
	 * 执行算价。算价是基于链式的过程，每次的算价都是可能基于上一次的某次算价的结果再叠加的。
	 * @param currentCalculateResult 当前已经执行了的算价结果。
	 * @return 新的算价结果。通常是复制的过程，不会直接引用。
	 * @throws PricingException 
	 */
	PriceCalculateResult calculate(PriceCalculateResult currentCalculateResult) throws PricingException;

	/**
	 * 该算价器是否可以继续应用于当前的算价结果
	 * @param newResult
	 * @return
	 */
	boolean canApplyToPricingResult(PriceCalculateResult newResult);

	/**
	 * 正对现有的算价结果，该算价器是否在以后还会被用到
	 * @param newResult
	 * @return
	 */
	boolean maybeUsedLater(PriceCalculateResult newResult);
	
	CALCULATOR_TYPE getType();
	String getSubType();
}
