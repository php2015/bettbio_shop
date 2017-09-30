package com.salesmanager.core.business.catalog.product.service.certificate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.salesmanager.core.business.catalog.product.dao.certificate.ProductCertificateDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;


@Service("productCertificateService")
public class ProductCertificateServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductCertificate> 
	implements ProductCertificateService {

	private ProductCertificateDao productCertificateDao;
	@Autowired 
	private ProductImageService productImageService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	public ProductCertificateServiceImpl(
			ProductCertificateDao productCertificateDao) {
			super(productCertificateDao);
			this.productCertificateDao = productCertificateDao;
	}

	@Override
	public List<ProductCertificate> getByProduct(Product product) {
		// TODO Auto-generated method stub
		return productCertificateDao.getByProduct(product);
	}
	
	@Override
	public void delete(ProductCertificate certificate) throws ServiceException {
		
		Product product = certificate.getProduct();
		//如果certificate存在图片，则要删除cms中的图片
		if (!StringUtils.isBlank(certificate.getCertificateImage())) {
			productImageService.removeProductRelatedImage(product, certificate.getCertificateImage(), FileContentType.PRODUCT_CERTIFICATE);
		}
		//override method, this allows the error that we try to remove a detached instance
		certificate = this.getById(certificate.getId());
		super.delete(certificate);
		
	}
	@Transactional(rollbackFor = { Exception.class }) 
	@Override
	public void saveOrUpdate(ProductCertificate certificate) {
		// TODO Auto-generated method stub
		/**
		boolean isFirst = false;
		if (product.getCertificates() == null || product.getCertificates().size()==0) isFirst=true;		
		product.setQualityScore(BaseDataUtils.setProductQuality(product, basedataType,isFirst));
		*/
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
			productService.saveOrUpdate(product);*/
			
			if (certificate.getId()!=null && certificate.getId()>0) {
				super.update(certificate);
			}else{
				super.save(certificate);
			}
		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}
	@Transactional(rollbackFor = { Exception.class }) 
	@Override
	public void deletC(ProductCertificate certificate) {
		// TODO Auto-generated method stub
		/**
		boolean isLast = false;
		if(product.getCertificates().size()==1) isLast=true;
		product.setQualityScore(BaseDataUtils.removeProductQuality(product, basedataType, isLast));*/
		try {
			this.delete(certificate);
			Product product = productService.getById(certificate.getProduct().getId());
			productService.saveOrUpdate(product);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}

}
