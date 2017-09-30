package com.terapico.pricingimpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.terapico.pricing.PriceCalculateResult;
import com.terapico.pricing.PriceCalculator;
import com.terapico.pricing.PricingException;
import com.terapico.pricing.PricingOrder;
import com.terapico.pricing.PricingOrderItem;
import com.terapico.pricing.PricingPackage;

public class PriceEngineBaseImplTest {
	private static int sequenceNo = 0;
	@Test
	public void testCalculate() {
		//fail("Not yet implemented");
		TestPriceEngine engine = new TestPriceEngine();
		preparePriceEngine(engine);
		PricingOrder order = prepareOrder();
		try {
			PriceCalculateResult result = engine.calculate(order);
//			System.out.println(result);
			assertTrue("OK", true);
		} catch (PricingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private PricingOrder prepareOrder() {
		PricingOrder order = new PricingOrder();
		PricingPackage package1 = new PricingPackage();
		package1.setSequenceNo(sequenceNo++);
		PricingPackage package2 = new PricingPackage();
		List<PricingPackage> packages = new ArrayList<PricingPackage>();
		package2.setSequenceNo(sequenceNo++);
		packages.add(package1);
		packages.add(package2);
		order.setPackages(packages);
		
		List<PricingOrderItem> items = new ArrayList<PricingOrderItem>();
		items.add(newItem("400", "50ml", "yuanye", "corning", 800.78, 2));
		items.add(newItem("450", "pound", "yuanye", null, 1200, 0.075));
		package1.setItems(items);
		
		items = new ArrayList<PricingOrderItem>();
		items.add(newItem("500", "box", "ronghe", null, 99.99, 2));
		items.add(newItem("550", "pound", "ronghe", "corning", 1200, 0.075));
		package2.setItems(items);
		
		return order;
	}

	private PricingOrderItem newItem(String pId, String skuId, String merchantKey, String brandKey, double price, double quantity) {
		PricingOrderItem item = new PricingOrderItem();
		item.setProductId(pId);
		item.setSkuId(skuId);
		item.setMerchantKey(merchantKey);
		item.setBrandKey(brandKey);
		item.setCategories(newList());
		item.setOrignalUnitPrice(new BigDecimal(price));
		item.setQuantity(new BigDecimal(quantity));
		item.setSequenceNo(sequenceNo++);
		return item;
	}

	private List<String> newList(String ... values) {
		return Arrays.asList(values);
	}

	private void preparePriceEngine(TestPriceEngine engine) {
		engine.setPriceResultSelector(new PriceResultSelectorBaseImpl());
		
		BrandAndMerchantDiscountCalculator bmdCalculator = new BrandAndMerchantDiscountCalculator();
		List<PriceCalculator> calculators = new ArrayList<PriceCalculator>();
		calculators.add(bmdCalculator);
		engine.setCalculators(calculators);
	}

}
