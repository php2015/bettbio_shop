package com.salesmanager.core.business.catalog.product.service.thirdproof;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.salesmanager.core.business.catalog.product.dao.thirdproof.ProductThirdproofDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productThirdproofService")
public class ProductThirdproofServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductThirdproof> 
	implements ProductThirdproofService {

	private ProductThirdproofDao productThirdproofDao;
	@Autowired 
	private ProductImageService productImageService;
	@Autowired
	private ProductService productService;
	@Autowired
	public ProductThirdproofServiceImpl(
			ProductThirdproofDao productThirdproofDao) {
			super(productThirdproofDao);
			this.productThirdproofDao = productThirdproofDao;
	}
	
	@Override
	public List<ProductThirdproof> getByProduct(Product product) {
		// TODO Auto-generated method stub
		return productThirdproofDao.getByProduct(product);
	}

	@Override
	public void delete(ProductThirdproof thirdproof) throws ServiceException {
		
		Product product = thirdproof.getProduct();
		//如果certificate存在图片，则要删除cms中的图片
		if (!StringUtils.isBlank(thirdproof.getThirdproofImage())) {
			productImageService.removeProductRelatedImage(product, thirdproof.getThirdproofImage(), FileContentType.PRODUCT_THIRDPROOF);
		}
		//override method, this allows the error that we try to remove a detached instance
		thirdproof = this.getById(thirdproof.getId());
		super.delete(thirdproof);
		
	}

	@Override
	public void saveOrUpdate(ProductThirdproof productThirdproof) {
	//	AuditSection auditSection =product.getAuditSection();
		try {
			/**
			 * audit ==1 是设置为再审核状态，其他待审
			 */
			/**
			if(auditSection.getAudit() ==Constants.AUDIT_AUDITED) {
				auditSection.setAudit(Constants.AUDIT_AGAIN);
				product.setAuditSection(auditSection);
				
			}else {
				auditSection.setAudit(Constants.AUDIT_PRE);
				product.setAuditSection(auditSection);
			}*/
			//productService.saveOrUpdate(product);
			
			if (productThirdproof.getId()!=null && productThirdproof.getId()>0) {
				super.update(productThirdproof);
			}else{
				super.save(productThirdproof);
			}
		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

	@Override
	public void delett(ProductThirdproof productThirdproof) {
		
		try {
			this.delete(productThirdproof);
			Product product = productService.getById(productThirdproof.getProduct().getId());
			productService.saveOrUpdate(product);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

}
