package com.salesmanager.core.business.catalog.product.service.proof;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.salesmanager.core.business.catalog.product.dao.proof.ProductProofDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productProofService")
public class ProductProofServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductProof> 
	implements ProductProofService {

	private ProductProofDao productProofDao;
	@Autowired 
	private ProductImageService productImageService;
	@Autowired
	private ProductService productService;
	
	@Autowired
	public ProductProofServiceImpl(
			ProductProofDao productProofDao) {
			super(productProofDao);
			this.productProofDao = productProofDao;
	}
	
	@Override
	public List<ProductProof> getByProduct(Product product) {
		// TODO Auto-generated method stub
		return productProofDao.getByProduct(product);
	}

	@Override
	public void delete(ProductProof proof) throws ServiceException {
		
		Product product = proof.getProduct();
		//如果certificate存在图片，则要删除cms中的图片
		if (!StringUtils.isBlank(proof.getProofImage())) {
			productImageService.removeProductRelatedImage(product, proof.getProofImage(), FileContentType.PRODUCT_PROOF);
		}
		//override method, this allows the error that we try to remove a detached instance
		proof = this.getById(proof.getId());
		super.delete(proof);
		
	}

	@Override
	public void saveOrUpdate(ProductProof productProof) {
		/**
		boolean isFirst = false;
		if (product.getCertificates() == null || product.getCertificates().size()==0) isFirst=true;		
		product.setQualityScore(BaseDataUtils.setProductQuality(product, basedataType,isFirst));
		AuditSection auditSection =product.getAuditSection();*/
		try {
			/**
			 * audit ==1 是设置为再审核状态，其他待审
			 
			if(auditSection.getAudit() ==Constants.AUDIT_AUDITED) {
				auditSection.setAudit(Constants.AUDIT_AGAIN);
				product.setAuditSection(auditSection);
				
			}else {
				auditSection.setAudit(Constants.AUDIT_PRE);
				product.setAuditSection(auditSection);
			}
			productService.saveOrUpdate(product);*/
			
			if (productProof.getId()!=null && productProof.getId()>0) {
				super.update(productProof);
			}else{
				super.save(productProof);
			}
		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

	@Override
	public void deletp(ProductProof productProof) {
		
		
		try {			
				this.delete(productProof);
				Product product = productService.getById(productProof.getProduct().getId());
				productService.saveOrUpdate(product);
				
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

}
