package com.salesmanager.core.business.catalog.product.service.browse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.browse.ProductBrowseDao;
import com.salesmanager.core.business.catalog.product.model.browse.ProductBrowse;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productBrowseService")
public class ProductBrowseServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductBrowse> implements
		ProductBrowseService {


	private ProductBrowseDao productBrowseDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	public ProductBrowseServiceImpl(
			ProductBrowseDao productBrowseDao) {
			super(productBrowseDao);
			this.productBrowseDao = productBrowseDao;
	}

	@Override
	public List<ProductBrowse> getByProductID(Long productID) {
		// TODO Auto-generated method stub
		return productBrowseDao.getByProductID(productID);
	}

	@Override
	public void deleteByProductId(Long productID) {
		// TODO Auto-generated method stub
		productBrowseDao.deleteByProductId(productID);
	}

	

}
