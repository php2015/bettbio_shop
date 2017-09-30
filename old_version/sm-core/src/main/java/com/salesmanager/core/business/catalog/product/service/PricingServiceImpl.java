package com.salesmanager.core.business.catalog.product.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.service.price.BrandDiscountService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.utils.ProductPriceUtils;

/**
 * Contains all the logic required to calculate product price
 * @author Carl Samson
 *
 */
@Service("pricingService")
public class PricingServiceImpl implements PricingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);
	

	@Autowired
	private ProductPriceUtils priceUtil;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandDiscountService brandDiscountService;
	@Override
	public FinalPrice calculateProductPrice(Product product) throws ServiceException {
		return priceUtil.getFinalPrice(product);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, Customer customer) throws ServiceException {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalPrice(product);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, List<ProductAttribute> attributes) throws ServiceException {
		return priceUtil.getFinalProductPrice(product, attributes);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, List<ProductAttribute> attributes, Customer customer) throws ServiceException {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalProductPrice(product, attributes);
	}

	@Override
	public String getDisplayAmount(BigDecimal amount, MerchantStore store) throws ServiceException {
		try {
			String price= priceUtil.getStoreFormatedAmountWithCurrency(store,amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}
	
	@Override
	public String getDisplayAmount(BigDecimal amount, Locale locale,
			Currency currency, MerchantStore store) throws ServiceException {
		try {
			String price= priceUtil.getFormatedAmountWithCurrency(locale, currency, amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amunt " + amount.toString() + " using locale " + locale.toString() + " and currency " + currency.toString());
			throw new ServiceException(e);
		}
	}
    
	@Override
	public String getStringAmount(BigDecimal amount, MerchantStore store)
			throws ServiceException {
		try {
			String price = priceUtil.getAdminFormatedAmount(store, amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public BigDecimal getFinalPrice(ProductPrice price) throws ServiceException {
		// TODO Auto-generated method stub
		return priceUtil.getFinalPrice(price);
	}

	/**
	 * 算价。 </p>
	 * 2017-3-27：目前算价的规则是这样：
	 * <li> 用户本身有折扣,以原始价格为基础 </li>
	 * <li> 产品对应的品牌有折扣，以原始价格为基础。这个以后会改成“对应的用户的类型+品牌+商家”有折扣。</li>
	 * <li> 产品本身有促销价，有时间段限制 </li>
	 * 取以上最小值。
	 */
	@Override
	public List<ProductPricingData> calculateProductListPrice(ProductPricingContext priceCtx, List<Long> prodIds) {
		List<Product>products =  productService.getProductListByIdsForPricing(prodIds);
		List<ProductPricingData> result = new ArrayList<ProductPricingData>(products.size());
		if (products.isEmpty()){
			return result;
		}
		//第一轮，填充基础价格
		fillListPrice(result, products);
		// 第二轮，计算用户折扣
		fillCustomerDiscount(priceCtx.getCustomer(), result);
		// 第三轮，计算品牌折扣
		fillBrandDiscount(priceCtx.getCustomer(), prodIds, result);
		return result;
	}

	private void fillBrandDiscount(Customer customer, List<Long> prodIds, List<ProductPricingData> result) {
		if (customer == null){
			return; // 如果用户未登录，不用计算品牌折扣
		}
		List<Object[]> discounts = brandDiscountService.getDiscountRateByProductIdList(prodIds);
		for(ProductPricingData productData : result){
			List<ProductSpecPricingData> specPrices = productData.getPrices();
			if (specPrices == null){
				continue;
			}
			Double dsctRate = findProductBrandDiscount(productData.getProductId(), discounts);
			if (dsctRate == null){
				continue;
			}
			for(ProductSpecPricingData specData : specPrices){
				if (specData.getListPrice() == null){
					continue;
				}
				BigDecimal newPrice = specData.getListPrice().multiply(new BigDecimal(dsctRate));
				if (newPrice.compareTo(specData.getFinalPrice()) <= 0){
					specData.setFinalPrice(newPrice);
					specData.setFinalPriceType(PRICE_TYPE_BRAND_DISCOUNT);
					specData.setFinalDiscount(dsctRate);
				}
			}
		}
	}

	
	private Double findProductBrandDiscount(Long productId, List<Object[]> discounts) {
		for(Object[] data: discounts){
			long pId = ((Number)data[0]).longValue();
			String userSegment = data[1].toString();
			if (pId == productId && Constants.USER_SEGEMNT_LOGGED_CUSTOMER.equals(userSegment)){
				return ((Number)data[2]).doubleValue();
			}
		}
		return null;
	}

	private void fillCustomerDiscount(Customer customer, List<ProductPricingData> result) {
		if (customer == null){
			// 如果未登录，全部没有优惠
			for(ProductPricingData productData : result){
				List<ProductSpecPricingData> specPrices = productData.getPrices();
				if (specPrices == null){
					continue;
				}
				for(ProductSpecPricingData specData : specPrices){
					specData.setFinalPriceType(null);
				}
			}
			return;
		}
		if (customer.getDiscount() == null){
			return;
		}
		double dsctRate = customer.getDiscount();
		for(ProductPricingData productData : result){
			List<ProductSpecPricingData> specPrices = productData.getPrices();
			if (specPrices == null){
				continue;
			}
			for(ProductSpecPricingData specData : specPrices){
				if (specData.getListPrice() == null){
					continue;
				}
				BigDecimal newPrice = specData.getListPrice().multiply(new BigDecimal(dsctRate));
				if (newPrice.compareTo(specData.getFinalPrice()) <= 0){
					specData.setFinalPrice(newPrice);
					specData.setFinalPriceType(PRICE_TYPE_CUSTOMER_DISCOUNT);
					specData.setFinalDiscount(dsctRate);
				}
			}
		}
	}

	protected void fillListPrice(List<ProductPricingData> result, List<Product> products) {
		for (Product product : products){
			ProductPricingData data = new ProductPricingData();
			data.setProductId(product.getId());
			data.setProduct(product);
			result.add(data);
			// 如果是service类产品，无价格，直接以sprice为准
			if (product.getServerPrice() != null){
				continue;
			}
			Set<ProductAvailability> avs = product.getAvailabilities();
			if (avs == null){
				continue;  // 一个 规格 也没有，跳过
			}
			for(ProductAvailability avb : avs){
				Set<ProductPrice> avps = avb.getPrices();
				if (avps == null){
					continue; //规格没有价格，跳过
				}
				ProductPrice avp = getDefaultPrice(avps);
				ProductSpecPricingData specData = new ProductSpecPricingData();
				specData.setSpecId(avp.getId());
				specData.setProductPrice(avp);
				specData.setSpecTitle(getSpecTitle(avp));
				specData.setListPrice(avp.getProductPriceAmount());
				specData.setFinalPrice(avp.getProductPriceAmount());
				specData.setFinalPriceType(PRICE_TYPE_LIST_PRICE);
				// 计算是否有促销价格
				if (avp.getProductPriceSpecialAmount() != null){
					Date nowDate = new Date();
					Date startDate = avp.getProductPriceSpecialStartDate();
					Date endDate = avp.getProductPriceSpecialEndDate();
					if (startDate == null && endDate == null){
						// 无效，跳过
					}else if(startDate == null && endDate != null){
						if (nowDate.before(endDate)){
							specData.setFinalPrice(avp.getProductPriceSpecialAmount());
							specData.setFinalPriceType(PRICE_TYPE_TIMED_PROMOTION);
						}
					}else if (startDate != null && endDate == null){
						if (nowDate.after(startDate)){
							specData.setFinalPrice(avp.getProductPriceSpecialAmount());
							specData.setFinalPriceType(PRICE_TYPE_TIMED_PROMOTION);
						}
					}else{
						if (nowDate.after(startDate) && nowDate.before(endDate)){
							specData.setFinalPrice(avp.getProductPriceSpecialAmount());
							specData.setFinalPriceType(PRICE_TYPE_TIMED_PROMOTION);
						}
					}
				}
				List<ProductSpecPricingData> list = data.getPrices();
				if (list == null){
					list = new ArrayList<ProductSpecPricingData>();
					data.setPrices(list);
				}
				list.add(specData);
			}
		}
	}

	private String getSpecTitle(ProductPrice avp) {
		Set<ProductPriceDescription> descs = avp.getDescriptions();
		if (descs == null || descs.isEmpty()){
			return null;
		}
		for(ProductPriceDescription desc : descs){
			return desc.getName();
		}
		return null;
	}

	private ProductPrice getDefaultPrice(Set<ProductPrice> avps) {
		if (avps == null){
			throw new RuntimeException("不要乱用这个函数，必须先校验");
		}
		Long pid = null;
		ProductPrice result = null;
		for(ProductPrice avp : avps){
			if (avp.isDefaultPrice()){
				return avp;
			}
			if (pid == null || avp.getId() > pid){
				pid = avp.getId();
				result = avp;
			}
		}
		return result;
	}

	
	
}
