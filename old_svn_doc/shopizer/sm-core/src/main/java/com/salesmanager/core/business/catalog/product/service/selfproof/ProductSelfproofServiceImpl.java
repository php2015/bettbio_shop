package com.salesmanager.core.business.catalog.product.service.selfproof;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.salesmanager.core.business.catalog.product.dao.selfproof.ProductSelfproofDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productSelfproofService")
public class ProductSelfproofServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductSelfproof> 
	implements ProductSelfproofService {

	private ProductSelfproofDao productSelfproofDao;
	@Autowired 
	private ProductImageService productImageService;
	@Autowired
	private ProductService productService;
	@Autowired
	public ProductSelfproofServiceImpl(
			ProductSelfproofDao productSelfproofDao) {
			super(productSelfproofDao);
			this.productSelfproofDao = productSelfproofDao;
	}
	
	@Override
	public List<ProductSelfproof> getByProduct(Product product) {
		// TODO Auto-generated method stub
		return productSelfproofDao.getByProduct(product);
	}
	@Transactional(rollbackFor = { Exception.class }) 
	@Override
	public void delete(ProductSelfproof selfproof) throws ServiceException {
		
		Product product = selfproof.getProduct();
		try{
			//如果selfproof存在图片，则要删除cms中的图片
			if (!StringUtils.isBlank(selfproof.getSelfproofImage())) {
				productImageService.removeProductRelatedImage(product, selfproof.getSelfproofImage(), FileContentType.PRODUCT_SELFPROOF);
			}
			//override method, this allows the error that we try to remove a detached instance
			selfproof = this.getById(selfproof.getId());
			super.delete(selfproof);
			//productService.saveOrUpdate(product);
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}

	@Override
	public void saveOrUpdate(Product product, ProductSelfproof productSelfproof) {
		// TODO Auto-generated method stub
		//AuditSection auditSection =product.getAuditSection();
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
			}
			productService.saveOrUpdate(product);
			*/
			if (productSelfproof.getId()!=null && productSelfproof.getId()>0) {
				super.update(productSelfproof);
			}else{
				super.save(productSelfproof);
			}
		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}

	

}
