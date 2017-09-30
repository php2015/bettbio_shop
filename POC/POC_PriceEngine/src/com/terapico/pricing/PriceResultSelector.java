package com.terapico.pricing;

public interface PriceResultSelector {

	PriceCalculateResult selectBetterPricingResult(PriceCalculateResult lastResult, PriceCalculateResult newResult);

}
