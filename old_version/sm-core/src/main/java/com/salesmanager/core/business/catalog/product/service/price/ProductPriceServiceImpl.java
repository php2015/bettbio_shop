package com.salesmanager.core.business.catalog.product.service.price;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.price.ProductPriceDao;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productPrice")
public class ProductPriceServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductPrice> 
	implements ProductPriceService {

	private ProductPriceDao productPriceDao;
	@Autowired
	public ProductPriceServiceImpl(ProductPriceDao productPriceDao) {
		super(productPriceDao);
		this.productPriceDao = productPriceDao;
	}

	@Override
	public void addDescription(ProductPrice price,
			ProductPriceDescription description) throws ServiceException {
		price.getDescriptions().add(description);
		//description.setPrice(price);
		update(price);
	}
	
	
	@Override
	public void saveOrUpdate(ProductPrice price) throws ServiceException {
		
		if(price.getId()!=null && price.getId()>0) {
			this.update(price);
		} else {
			
			Set<ProductPriceDescription> descriptions = price.getDescriptions();
			price.setDescriptions(new HashSet<ProductPriceDescription>());
			this.create(price);
			for(ProductPriceDescription description : descriptions) {
				description.setProductPrice(price);
				this.addDescription(price, description);
			}
			
		}
	}
	
	/**
	 * 批量更新价格列表
	 */
	@Override
	public void saveOrUpdateList(List<ProductPrice> prices) throws ServiceException {
		for (int i = 0; i < prices.size(); i++) {
			ProductPrice price = prices.get(i);
			if(price.getId()!=null && price.getId()>0) {
				this.update(price);
			} else {
				Set<ProductPriceDescription> descriptions = price.getDescriptions();
				price.setDescriptions(new HashSet<ProductPriceDescription>());
				this.create(price);
				for(ProductPriceDescription description : descriptions) {
					description.setProductPrice(price);
					this.addDescription(price, description);
				}
			}
		}
	}
	
	@Override
	public void delete(ProductPrice price) throws ServiceException {
		
		//override method, this allows the error that we try to remove a detached instance
		price = this.getById(price.getId());
		super.delete(price);
		
	}
	
	/**
	 * 批量更新主键ID！=id的其他规格价格的默认价格属性，更新为false
	 * @param id
	 */
	public void updateDefaultPrices(Long id) {
		productPriceDao.updateDefaultPrices(id);
	}

	@Override
	public List<ProductPrice> getByProductID(Long id) {
		// TODO Auto-generated method stub
		return productPriceDao.getByProductID(id);
	}

}
