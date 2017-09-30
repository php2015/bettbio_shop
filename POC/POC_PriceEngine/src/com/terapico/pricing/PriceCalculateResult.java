package com.terapico.pricing;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PriceCalculateResult {
	public class BigDecimalAdapter implements JsonSerializer<BigDecimal>, JsonDeserializer<BigDecimal> {

		@Override
		public BigDecimal deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
			try {
				return new BigDecimal(df.parse(arg0.getAsString()).doubleValue());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public JsonElement serialize(BigDecimal src, Type srcType, JsonSerializationContext ctx) {
			if (src == null){
				return null;
			}
			return new JsonPrimitive(forDisplay(src));
		}

	}
	protected PricingOrder inputOrder;
	protected List<PriceCalculator> possiblePricingCalculators;
	protected List<CalculationHistory> appliedCalculationSteps;
	protected PriceResultSelector priceResultSelector;
	
	public PricingOrder getInputOrder() {
		return inputOrder;
	}

	public void setInputOrder(PricingOrder inputOrder) {
		this.inputOrder = inputOrder;
	}

	public List<PriceCalculator> getPossiblePricingCalculators() {
		return possiblePricingCalculators;
	}

	public void setPossiblePricingCalculators(List<PriceCalculator> possiblePricingCalculators) {
		this.possiblePricingCalculators = possiblePricingCalculators;
	}

	public List<CalculationHistory> getAppliedCalculationSteps() {
		return appliedCalculationSteps;
	}

	public void setAppliedCalculationSteps(List<CalculationHistory> appliedCalculationSteps) {
		this.appliedCalculationSteps = appliedCalculationSteps;
	}

	public PriceResultSelector getPriceResultSelector() {
		return priceResultSelector;
	}

	public void setPriceResultSelector(PriceResultSelector priceResultSelector) {
		this.priceResultSelector = priceResultSelector;
	}

	public void addCalculationHistory(CalculationHistory calcHistory) {
		ensureAppliedCalculationSteps();
		appliedCalculationSteps.add(calcHistory);
	}

	private void ensureAppliedCalculationSteps() {
		if (appliedCalculationSteps == null){
			appliedCalculationSteps = new ArrayList<CalculationHistory>();
		}
	}

	@Override
	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("PriceCalculateResult@").append(this.hashCode()).append("{\n");
//		toString4Order(sb);
//		toString4Calculators(sb);
//		return sb.toString();
		Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new ExclusionStrategy(){

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				if (PriceResultSelector.class.isAssignableFrom(clazz)){
					return true;
				}
				return false;
			}

			@Override
			public boolean shouldSkipField(FieldAttributes arg0) {
				if (arg0.getName().equals("pricingDetailIndexMap")){
					return true;
				}
				return false;
			}
		}).registerTypeAdapter(BigDecimal.class, new BigDecimalAdapter() ).create();
		return gson.toJson(this);
	}

	
	// order packages
	protected void toString4Order(StringBuilder sb) {
		if (inputOrder == null) {
			return;
		}
		sb.append("    order: {\n");
		for (PricingPackage pkg : inputOrder.getPackages()) {
			sb.append("        package_").append(pkg.getSequenceNo()).append(":[\n");
			for (PricingOrderItem item : pkg.getItems()) {
				sb.append("            {");
				sb.append("seq=").append(item.getSequenceNo());
				sb.append(",PID=").append(item.getProductId());
				sb.append(",SKU=").append(item.getSkuId());
				sb.append(",Merchant=").append(item.getMerchantKey());
				sb.append(",Brand=").append(item.getBrandKey());
				sb.append(",Category=").append(item.getCategories());
				sb.append(",UPrice=").append(forDisplay(item.getOrignalUnitPrice()));
				sb.append(",QTY=").append(forDisplay(item.getQuantity()));
				sb.append(",Total=").append(forDisplay(item.getOrignalTotalPrice()));
				sb.append("}\n");
			}
			sb.append("        ]\n");
		}
		sb.append("    }\n");
	}

	private static final DecimalFormat df = new DecimalFormat("#,##0.00#");  
	private String forDisplay(BigDecimal number) {
		return df.format(number);
	}

	
	
	
}
